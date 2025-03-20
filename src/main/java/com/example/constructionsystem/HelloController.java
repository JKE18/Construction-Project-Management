package com.example.constructionsystem;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;


public class HelloController {

    int id_employee_selected;
    int id_project_selected;
    int id_project_selected_kosztorys;
    int id_project_selected_raport;
    int id_listtodo_selected;
    int id_raport_selected;
    int globalOplacona;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    @FXML
    public AnchorPane main_form;
    @FXML
    private Button logoutbtn;
    @FXML
    private Button calendarbtn;
    @FXML
    private TextArea taskInput;
    @FXML
    private DatePicker deadlineInput;
    @FXML
    private Label label_employeecount;
    @FXML
    private Label label_activeproject;
    @FXML
    private Button menubtn;

    @FXML
    private Button employeebtn;

    @FXML
    private Button projectbtn;

    @FXML
    private Button raportbtn;

    @FXML
    private Button kosztorysbtn;

    @FXML
    private AnchorPane menuanchor;

    @FXML
    private AnchorPane employeeanchor;

    @FXML
    private AnchorPane projectanchor;

    @FXML
    private AnchorPane kosztorysanchor;

    @FXML
    private AnchorPane raportanchor;

    @FXML
    private TableView<Employee> Employee_table;

    @FXML
    private TableColumn<Employee, String> col_imie;

    @FXML
    private TableColumn<Employee, String> col_nazwisko;

    @FXML
    private TableColumn<Employee, String> col_stan;

    @FXML
    private TableColumn<Employee, String> col_rok;

    @FXML
    private TableColumn<Employee, String> col_salary;

    @FXML
    private TableColumn<Employee, String> col_email;

    @FXML
    private TableColumn<Employee, String> col_phone;

    @FXML
    private TextField textfield_imie;

    @FXML
    private TextField textfield_nazwisko;

    @FXML
    private TextField textfield_rok;

    @FXML
    private TextField textfield_wynagrodzenie;

    @FXML
    private TextField textfield_email;

    @FXML
    private TextField textfield_telefon;
    @FXML
    private ComboBox<String> combo_stan;
    @FXML
    private ComboBox<String> combo_stanowisko;
    @FXML
    private TableColumn<Employee, String> col_stanowisko;
    @FXML
    private TextField search;
    private Database db = new Database();
    Alert alert;

    private Stage stage;


    @FXML
    public void logout(){

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacja zwrotna");
        alert.setHeaderText(null);
        alert.setContentText("Pomyślnie wylogowano z konta!");
        alert.showAndWait();
        ((Stage)logoutbtn.getScene().getWindow()).close();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
            GlobalData gd= new GlobalData(600,400);
            Scene scene = new Scene(root, gd.XWidth, gd.YHeight);
            Stage stage = new Stage();
            stage.setMinWidth(gd.XWidth+15);
            stage.setMaxWidth(gd.XWidth+15);
            stage.setMinHeight(gd.YHeight+42);
            stage.setMaxHeight(gd.YHeight+42);
            Image icon = new Image(getClass().getResourceAsStream("/images/ikona_wejsciowa.png"));
            stage.getIcons().add(icon);
            stage.setTitle("Aplikacja do zarządzania projektem budowlanym");
            stage.initStyle(StageStyle.DECORATED);
            stage.setScene(scene);
            stage.show();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(((screenBounds.getWidth() - gd.XWidth) / 2));
            stage.setY(((screenBounds.getHeight() - gd.YHeight) / 2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void updateLobbyLabels() {
        String sqlEmployee = "SELECT COUNT(*) FROM employee";
        int employeeCount = 0;

        String sqlProject = "SELECT COUNT(*) FROM project";
        int projectCount = 0;

        try {
            db.connection = Database.connectDB();

            // Liczenie pracowników
            db.preparedStatement = db.connection.prepareStatement(sqlEmployee);
            db.resultSet = db.preparedStatement.executeQuery();
            if (db.resultSet.next()) {
                employeeCount = db.resultSet.getInt(1);
            }

            // Liczenie projektów
            db.preparedStatement = db.connection.prepareStatement(sqlProject);
            db.resultSet = db.preparedStatement.executeQuery();
            if (db.resultSet.next()) {
                projectCount = db.resultSet.getInt(1);
            }

            label_employeecount.setText(""+employeeCount);
            label_activeproject.setText(""+projectCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //LISTA TODO
    @FXML
    private ListView<Task> listtodo;


    public void ListtodoSelect() {
        Task selectedTask = listtodo.getSelectionModel().getSelectedItem();

        id_listtodo_selected = selectedTask.getId_task();
        taskInput.setText(selectedTask.getDescription());
        deadlineInput.setValue(selectedTask.getDeadline());

    }

    ObservableList<Task> taskData = FXCollections.observableArrayList();
    @FXML
    public ObservableList<Task> addTaskDataList(){

        String sql = "SELECT * FROM tasks";

        db.connection = Database.connectDB();
        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.resultSet = db.preparedStatement.executeQuery();
            while(db.resultSet.next()) {

                Task task = new Task(
                        db.resultSet.getInt("id_task"),
                        db.resultSet.getString("description"),
                        db.resultSet.getDate("deadline").toLocalDate());
                        taskData.add(task);
            }
        }catch(Exception e){e.printStackTrace();}
        return taskData;
    }

    @FXML
    public void ListtodoShowData(){
        ObservableList<Task> addTaskList = addTaskDataList();

        listtodo.setItems(addTaskList);


        listtodo.setCellFactory(lv -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);

                if (empty || task == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    // Obliczanie dni do terminu zadania
                    long daysToDeadline = ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadline());

                    // Ustawianie koloru na podstawie pozostałych dni do terminu
                    if (daysToDeadline <= 0) {
                        setStyle("-fx-border-color: #f34545");
                        setStyle("-fx-background-color: #f34545;"); // Czerwony
                    } else if (daysToDeadline <= 7) {
                        setStyle("-fx-border-color: #ffcccc");
                        setStyle("-fx-background-color: #ffcccc;"); // Jasny czerwony
                    } else if (daysToDeadline <= 14) {
                        setStyle("-fx-border-color: #fff3cd");
                        setStyle("-fx-background-color: #fff3cd;"); // Żółty
                    } else {
                        setStyle("-fx-border-color: #d4edda");
                        setStyle("-fx-background-color: #d4edda;"); // Zielony
                    }

                    // Tworzenie widoku dla każdego elementu
                    HBox hBox = new HBox(20);
                    Text descriptionText = new Text(task.getDescription());
                    Text deadlineText = new Text("Termin: " + task.getDeadline().toString());

                    hBox.getChildren().addAll(descriptionText, deadlineText);
                    setGraphic(hBox);
                }
            }
        });
    }

    @FXML
    public void addTask() {
        String description = taskInput.getText().trim();
        LocalDate deadline = deadlineInput.getValue();

        boolean isAdded = taskAdd(description, deadline);

        if (isAdded) {
            taskInput.clear();
            deadlineInput.setValue(null);
            listtodo.getItems().clear();
            ListtodoShowData();
        }
    }

    public boolean taskAdd(String description, LocalDate deadline) {

        if (description.isEmpty() || deadline == null) {
            showAlert("Błąd", "Wypełnij wszystkie pola przed dodaniem zadania!", Alert.AlertType.ERROR);
            return false;
        }

        if (deadline.isBefore(LocalDate.now())) {
            showAlert("Błąd", "Nie można dodać zadania z przeszłą datą!", Alert.AlertType.ERROR);
            return false;
        }

        String sql = "INSERT INTO tasks (description, deadline) VALUES (?, ?)";
        db.connection = Database.connectDB();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Potwierdzenie");
        confirmationAlert.setHeaderText("Czy na pewno chcesz dodać to zadanie?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                db.preparedStatement = db.connection.prepareStatement(sql);
                db.preparedStatement.setString(1, description);
                db.preparedStatement.setDate(2, Date.valueOf(deadline));
                db.preparedStatement.executeUpdate();

                showAlert("Sukces", "Zadanie zostało dodane.", Alert.AlertType.INFORMATION);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Błąd", "Nie udało się dodać zadania.", Alert.AlertType.ERROR);
            }
        }
        return false;
    }
    @FXML
    public void removeTask() {
        Task selectedTask = listtodo.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            showAlert("Brak zaznaczenia", "Nie zaznaczono żadnego zadania do usunięcia!", Alert.AlertType.WARNING);
            return;
        }

        boolean isDeleted = taskRemove(selectedTask.getId_task());

        if (isDeleted) {
            listtodo.getItems().clear();
            ListtodoShowData();
        }
    }

    public boolean taskRemove(int taskId) {
        if (taskId <= 0) {
            showAlert("Błąd", "ID zadania jest niepoprawne!", Alert.AlertType.ERROR);
            return false;
        }
        if (!taskExists(taskId)) {
            showAlert("Błąd", "ID zadania nie istnieje!", Alert.AlertType.ERROR);
            return false;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Potwierdzenie usunięcia");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Czy na pewno chcesz usunąć to zadanie?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "DELETE FROM tasks WHERE id_task = ?";
            db.connection = Database.connectDB();

            try {
                db.preparedStatement = db.connection.prepareStatement(sql);
                db.preparedStatement.setInt(1, taskId);
                db.preparedStatement.executeUpdate();

                showAlert("Sukces", "Zadanie zostało usunięte!", Alert.AlertType.INFORMATION);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Błąd", "Nie udało się usunąć zadania.", Alert.AlertType.ERROR);
            }
        }
        return false;
    }

    public boolean taskExists(int taskID){
        String sql = "SELECT COUNT(*) FROM tasks where id_task = ?";
        db.connection = Database.connectDB();

        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.preparedStatement.setInt(1, taskID);
            db.resultSet = db.preparedStatement.executeQuery();
            if(db.resultSet.next()){
                return db.resultSet.getInt(1) > 0;
            }

        }catch (SQLException e){e.printStackTrace();}
        return false;
    }

    @FXML
    public void editTask() {
        Task selectedTask = listtodo.getSelectionModel().getSelectedItem();
        String updatedDescription = taskInput.getText().trim();
        LocalDate updatedDeadline = deadlineInput.getValue();

        boolean isUpdated = taskEdit(id_listtodo_selected, updatedDescription, updatedDeadline);

        if (isUpdated) {
            listtodo.getItems().clear();
            ListtodoShowData();
        }
    }

    public boolean taskEdit(int taskId, String description, LocalDate deadline) {
        if (taskId <= 0) {
            showAlert("Błąd", "ID zadania jest niepoprawne!", Alert.AlertType.ERROR);
            return false;
        }
        if (!taskExists(taskId)) {
            showAlert("Błąd", "ID zadania nie istnieje!", Alert.AlertType.ERROR);
            return false;
        }

        if (description.isEmpty() || deadline == null) {
            showAlert("Błąd edycji", "Opis i termin zadania muszą być wypełnione!", Alert.AlertType.ERROR);
            return false;
        }

        if (deadline.isBefore(LocalDate.now())) {
            showAlert("Błąd edycji", "Data zakończenia zadania nie może być w przeszłości!", Alert.AlertType.ERROR);
            return false;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Potwierdzenie edycji");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Czy na pewno chcesz edytować to zadanie?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "UPDATE tasks SET description = ?, deadline = ? WHERE id_task = ?";
            db.connection = Database.connectDB();

            try {
                db.preparedStatement = db.connection.prepareStatement(sql);
                db.preparedStatement.setString(1, description);
                db.preparedStatement.setDate(2, Date.valueOf(deadline));
                db.preparedStatement.setInt(3, taskId);
                db.preparedStatement.executeUpdate();

                showAlert("Sukces", "Zadanie zostało zaktualizowane!", Alert.AlertType.INFORMATION);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Błąd", "Nie udało się edytować zadania.", Alert.AlertType.ERROR);
            }
        }
        return false;
    }


    private void showAlert(String title, String content, Alert.AlertType alertType) {
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void switchForm(ActionEvent event){

        if(event.getSource() == menubtn){
            menuanchor.setVisible(true);
            employeeanchor.setVisible(false);
            projectanchor.setVisible(false);
            raportanchor.setVisible(false);
            kosztorysanchor.setVisible(false);
        }else if(event.getSource() == employeebtn){
            menuanchor.setVisible(false);
            employeeanchor.setVisible(true);
            projectanchor.setVisible(false);
            raportanchor.setVisible(false);
            kosztorysanchor.setVisible(false);
            addstanlist();
            addstanowiskolist();
        }else if(event.getSource() == projectbtn){
            menuanchor.setVisible(false);
            employeeanchor.setVisible(false);
            projectanchor.setVisible(true);
            raportanchor.setVisible(false);
            kosztorysanchor.setVisible(false);
        }else if(event.getSource() == raportbtn){
            menuanchor.setVisible(false);
            employeeanchor.setVisible(false);
            projectanchor.setVisible(false);
            raportanchor.setVisible(true);
            kosztorysanchor.setVisible(false);
        }else if(event.getSource() == kosztorysbtn){
            menuanchor.setVisible(false);
            employeeanchor.setVisible(false);
            projectanchor.setVisible(false);
            raportanchor.setVisible(false);
            kosztorysanchor.setVisible(true);
        }
    }
    @FXML
    public void openCalendar(){
        calendarbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage calendarStage = new Stage(); // Nowe okno dla kalendarza
                CalendarView calendarView = new CalendarView();

                // Tworzenie kalendarzy (np. urodziny, święta)
                Calendar birthdays = new Calendar("Birthdays");
                Calendar holidays = new Calendar("Holidays");

                birthdays.setStyle(Calendar.Style.STYLE1);
                holidays.setStyle(Calendar.Style.STYLE2);

                CalendarSource myCalendarSource = new CalendarSource("My Calendars");
                myCalendarSource.getCalendars().addAll(birthdays, holidays);

                calendarView.getCalendarSources().addAll(myCalendarSource);

                // Dodanie przykładowego wydarzenia
                // Tworzymy obiekt Entry i ustawiamy wszystkie właściwości
                Entry<String> holidaysEntry = new Entry<>("Spotkanie z zespołem");

                // Ustawiamy daty i godziny rozpoczęcia oraz zakończenia wydarzenia
                holidaysEntry.changeStartDate(LocalDate.now());
                holidaysEntry.changeStartTime(LocalTime.of(10, 0));
                holidaysEntry.changeEndTime(LocalTime.of(11, 0));

                // Dodajemy wydarzenie do kalendarza
                holidays.addEntry(holidaysEntry);
                // Ustawienie widoku kalendarza na dzisiejszy dzień
                calendarView.setRequestedTime(LocalTime.now());

                // Aktualizacja daty i czasu co 10 sekund
                Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
                    @Override
                    public void run() {
                        while (true) {
                            Platform.runLater(() -> {
                                calendarView.setToday(LocalDate.now());
                                calendarView.setTime(LocalTime.now());
                            });
                            try {
                                sleep(10000); // update co 10 sekund
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                updateTimeThread.setPriority(Thread.MIN_PRIORITY);
                updateTimeThread.setDaemon(true);
                updateTimeThread.start();

                // Tworzenie i ustawienie sceny
                Scene calendarScene = new Scene(calendarView);
                calendarStage.setTitle("Kalendarz");
                Image icon = new Image(getClass().getResourceAsStream("/images/ikona_wejsciowa.png"));
                calendarStage.getIcons().add(icon);
                calendarStage.setScene(calendarScene);
                calendarStage.setWidth(1300);
                calendarStage.setHeight(1000);
                calendarStage.centerOnScreen();
                calendarStage.show();
            }
        });
    }

    ObservableList<Employee> employeeData = FXCollections.observableArrayList();

    //PRACOWNICY
    @FXML
    public ObservableList<Employee> addEmployeeDataList(){

        String sql = "SELECT * FROM employee";

        db.connection = Database.connectDB();
        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.resultSet = db.preparedStatement.executeQuery();
            while (db.resultSet.next()) {
                int id = db.resultSet.getInt("id_employee");
                String imie = db.resultSet.getString("imie");
                String nazwisko = db.resultSet.getString("nazwisko");
                String stanPracownika = db.resultSet.getString("stan_pracownika");
                int rokUrodzenia = db.resultSet.getInt("rok_urodzenia");

                String encryptedSalary = db.resultSet.getString("wynagrodzenie");
                String encryptedEmail = db.resultSet.getString("email");
                String encryptedPhone = db.resultSet.getString("phone");

                String decryptedSalary = AESUtil.decrypt(encryptedSalary);
                String decryptedEmail = AESUtil.decrypt(encryptedEmail);
                String decryptedPhone = AESUtil.decrypt(encryptedPhone);

                if (decryptedSalary == null) {
                    showAlert("Błąd", "Nie udało się odszyfrować wynagrodzenia!", Alert.AlertType.ERROR);
                    return null;
                }
                if (decryptedEmail == null) {
                    showAlert("Błąd", "Nie udało się odszyfrować emailu!", Alert.AlertType.ERROR);
                    return null;
                }
                if (decryptedPhone == null) {
                    showAlert("Błąd", "Nie udało się odszyfrować telefonu!", Alert.AlertType.ERROR);
                    return null;
                }

                String stanowisko = db.resultSet.getString("stanowisko");

                Employee employee = new Employee(id, imie, nazwisko, stanPracownika, rokUrodzenia, decryptedSalary, decryptedEmail, decryptedPhone, stanowisko);
                employeeData.add(employee);
            }
        }catch(Exception e){e.printStackTrace();}
        return employeeData;
    }

    @FXML
    public void addEmployeeShowData(){
        ObservableList<Employee> addEmployeeList = addEmployeeDataList();

        col_imie.setCellValueFactory(new PropertyValueFactory<>("imie"));
        col_nazwisko.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        col_stan.setCellValueFactory(new PropertyValueFactory<>("stan_pracownika"));
        col_rok.setCellValueFactory(new PropertyValueFactory<>("rok_urodzenia"));
        col_salary.setCellValueFactory(new PropertyValueFactory<>("wynagrodzenie"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_stanowisko.setCellValueFactory(new PropertyValueFactory<>("stanowisko"));

        Employee_table.setItems(addEmployeeList);
    }
    public void EmployeeSelect(){
        Employee employeeData = Employee_table.getSelectionModel().getSelectedItem();
        int number = Employee_table.getSelectionModel().getSelectedIndex();
        if (number < 0) {
            return;
        }

        id_employee_selected = employeeData.getEmployeeid();
        textfield_imie.setText(employeeData.getImie());
        textfield_nazwisko.setText(employeeData.getNazwisko());
        textfield_rok.setText(String.valueOf(employeeData.getRok_urodzenia()));
        textfield_wynagrodzenie.setText(String.valueOf(employeeData.getWynagrodzenie()));
        textfield_email.setText(employeeData.getEmail());
        textfield_telefon.setText(String.valueOf(employeeData.getPhone()));
        combo_stan.setValue(employeeData.getStan_pracownika());
        combo_stanowisko.setValue(employeeData.getStanowisko());
    }

    @FXML
    public void addEmployeeAdd() {
        String imie = textfield_imie.getText();
        String nazwisko = textfield_nazwisko.getText();
        String stanPracownika = combo_stan.getSelectionModel().getSelectedItem();
        String rokUrodzenia = textfield_rok.getText();
        String wynagrodzenie = textfield_wynagrodzenie.getText();
        String email = textfield_email.getText();
        String phone = textfield_telefon.getText();
        String stanowisko = combo_stanowisko.getSelectionModel().getSelectedItem();

        boolean isAdded = addEmployee(imie, nazwisko, stanPracownika, rokUrodzenia, wynagrodzenie, email, phone, stanowisko);

        if (isAdded) {
            Employee_table.getItems().clear();
            addEmployeeShowData();
            addEmployeeClear();
            chartStan.getData().clear();
            barChart.getData().clear();
            chartEmployeeStanShow();
            chartEmployeeSalaryShow();
        }
    }
    public boolean addEmployee(String imie, String nazwisko, String stanPracownika, String rokUrodzenia, String wynagrodzenie, String email, String phone, String stanowisko) {
        String sql = "INSERT INTO employee(imie, nazwisko, stan_pracownika, rok_urodzenia, wynagrodzenie, email, phone, stanowisko) VALUES (?,?,?,?,?,?,?,?)";
        db.connection = Database.connectDB();

        try {
            if (imie.isEmpty() || nazwisko.isEmpty() || stanPracownika == null ||
                    rokUrodzenia.isEmpty() || wynagrodzenie.isEmpty() ||
                    email.isEmpty() || phone.isEmpty() || stanowisko == null) {
                showAlert("Błąd", "Uzupełnij wszystkie pola poprawnie przed dodaniem", Alert.AlertType.ERROR);
                return false;
            }

            double salary = Double.parseDouble(wynagrodzenie);
            if (salary < 0) {
                showAlert("Błąd", "Wynagrodzenie nie może być ujemne!", Alert.AlertType.ERROR);
                return false;
            }

            int telefon = Integer.parseInt(phone);
            if (telefon < 0) {
                showAlert("Błąd", "Telefon nie może być ujemny!", Alert.AlertType.ERROR);
                return false;
            }

            if (!phone.matches("\\d{9}")) {
                showAlert("Błąd", "Numer telefonu musi mieć 9 cyfr.", Alert.AlertType.ERROR);
                return false;
            }

            int parsedRokUrodzenia = Integer.parseInt(rokUrodzenia);
            if (parsedRokUrodzenia < 1900 || parsedRokUrodzenia > 2025) {
                showAlert("Błąd", "Rok urodzenia musi być pomiędzy 1900 a 2025.", Alert.AlertType.ERROR);
                return false;
            }

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie");
            alert.setHeaderText(null);
            alert.setContentText("Czy na pewno chcesz dodać pracownika?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                // Szyfrowanie wynagrodzenia
                String encryptedSalary = AESUtil.encrypt(wynagrodzenie);
                String encryptedEmail = AESUtil.encrypt(email);
                String encryptedPhone = AESUtil.encrypt(phone);

                if (encryptedSalary == null) {
                    showAlert("Błąd", "Nie udało się zaszyfrować wynagrodzenia!", Alert.AlertType.ERROR);
                    return false;
                }
                if (encryptedEmail == null) {
                    showAlert("Błąd", "Nie udało się zaszyfrować emailu!", Alert.AlertType.ERROR);
                    return false;
                }
                if (encryptedPhone == null) {
                    showAlert("Błąd", "Nie udało się zaszyfrować telefonu!", Alert.AlertType.ERROR);
                    return false;
                }

                db.preparedStatement = db.connection.prepareStatement(sql);
                db.preparedStatement.setString(1, imie);
                db.preparedStatement.setString(2, nazwisko);
                db.preparedStatement.setString(3, stanPracownika);
                db.preparedStatement.setInt(4, parsedRokUrodzenia);
                db.preparedStatement.setString(5, encryptedSalary);
                db.preparedStatement.setString(6, encryptedEmail);
                db.preparedStatement.setString(7, encryptedPhone);
                db.preparedStatement.setString(8, stanowisko);
                db.preparedStatement.executeUpdate();

                showAlert("Sukces", "Pracownik został pomyślnie dodany!", Alert.AlertType.INFORMATION);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Błąd", "Wystąpił problem podczas dodawania pracownika!", Alert.AlertType.ERROR);
        }
        return false;
    }


    @FXML
    public void addEmployeeClear(){
        textfield_imie.setText("");
        textfield_nazwisko.setText("");
        combo_stan.getSelectionModel().clearSelection();
        textfield_rok.setText("");
        textfield_wynagrodzenie.setText("");
        textfield_email.setText("");
        textfield_telefon.setText("");
        combo_stanowisko.getSelectionModel().clearSelection();
    }

    private String[] stanPracownikaOptions = {"Obecny", "Nieobecny", "Delegacja", "Chory"};
    public void addstanlist(){
        List<String> listStan = new ArrayList<>();
        for(String data: stanPracownikaOptions){
            listStan.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList("Obecny", "Nieobecny", "Delegacja", "Chory");
        combo_stan.setItems(listData);

    }
    private String[] stanowiskoOptions = {"Brygadzista", "Tynkarz", "Malarz", "Murarz",
            "Elektryk", "Hydraulik", "Pomocnik"};
    public void addstanowiskolist(){
        ArrayList<String> listStanowisko = new ArrayList<>();
        for(String data: stanowiskoOptions){
            listStanowisko.add(data);
        }

        ObservableList<String> listData = FXCollections.observableArrayList("Brygadzista", "Tynkarz", "Malarz", "Murarz", "Elektryk", "Hydraulik", "Pomocnik");
        combo_stanowisko.setItems(listData);

    }

    public boolean updateEmployee(int id, String imie, String nazwisko, String stanPracownika, String rokUrodzenia, String wynagrodzenie, String email, String phone, String stanowisko) {
        String sql = "UPDATE employee SET imie = ?, nazwisko = ?, stan_pracownika = ?, rok_urodzenia = ?, wynagrodzenie = ?, email = ?, phone = ?, stanowisko = ? WHERE id_employee = ?";

        db.connection = Database.connectDB();

        try {
            if (imie.isEmpty() || nazwisko.isEmpty() || stanPracownika == null ||
                    rokUrodzenia.isEmpty() || wynagrodzenie.isEmpty() ||
                    email.isEmpty() || phone.isEmpty() || stanowisko == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText(null);
                alert.setContentText("Uzupełnij wszystkie pola poprawnie przed dodaniem");
                alert.showAndWait();
                return false;
            }
            double salary = Double.parseDouble(wynagrodzenie);
            if(salary < 0 ){
                showAlert("Błąd", "Wynagrodzenie nie może być ujemne!", Alert.AlertType.ERROR);
                return false;
            }
            int telefon = Integer.parseInt(phone);
            if( telefon < 0 ){
                showAlert("Błąd", "Telefon nie może być ujemny!", Alert.AlertType.ERROR);
                return false;
            }

            if (!phone.matches("\\d{9}")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText(null);
                alert.setContentText("Numer telefonu musi mieć 9 cyfr.");
                alert.showAndWait();
                return false;
            }

            int parsedRokUrodzenia = Integer.parseInt(rokUrodzenia);
            if (parsedRokUrodzenia < 1900 || parsedRokUrodzenia > 2025) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText(null);
                alert.setContentText("Rok urodzenia musi być pomiędzy 1900 a 2025.");
                alert.showAndWait();
                return false;
            }

            if (id <= 0) {
                showAlert("Błąd", "Nie wybrano pracownika do edycji.", Alert.AlertType.ERROR);
                return false;
            }
            if (!employeeExists(id)) {
                showAlert("Błąd", "Nie znaleziono pracownika o podanym ID.", Alert.AlertType.ERROR);
                return false;
            }

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie edycji");
            alert.setHeaderText(null);
            alert.setContentText("Czy na pewno chcesz edytować pracownika?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                String encryptedSalary = AESUtil.encrypt(wynagrodzenie);
                String encryptedEmail = AESUtil.encrypt(email);
                String encryptedPhone = AESUtil.encrypt(phone);

                if (encryptedSalary == null) {
                    showAlert("Błąd", "Nie udało się zaszyfrować wynagrodzenia!", Alert.AlertType.ERROR);
                    return false;
                }
                if (encryptedEmail == null) {
                    showAlert("Błąd", "Nie udało się zaszyfrować emailu!", Alert.AlertType.ERROR);
                    return false;
                }
                if (encryptedPhone == null) {
                    showAlert("Błąd", "Nie udało się zaszyfrować telefonu!", Alert.AlertType.ERROR);
                    return false;
                }

                db.preparedStatement = db.connection.prepareStatement(sql);
                db.preparedStatement.setString(1, imie);
                db.preparedStatement.setString(2, nazwisko);
                db.preparedStatement.setString(3, stanPracownika);
                db.preparedStatement.setInt(4, parsedRokUrodzenia);
                db.preparedStatement.setString(5, encryptedSalary);
                db.preparedStatement.setString(6, encryptedEmail);
                db.preparedStatement.setString(7, encryptedPhone);
                db.preparedStatement.setString(8, stanowisko);
                db.preparedStatement.setInt(9, id);
                db.preparedStatement.executeUpdate();

                showAlert("Sukces","Edycja zakończona sukcesem!", Alert.AlertType.INFORMATION);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Błąd!" ,"Wystąpił problem podczas edycji danych!", Alert.AlertType.ERROR);
        }
        return false;
    }

    public boolean employeeExists(int employeeID){
        String sql = "SELECT COUNT(*) FROM employee where id_employee = ?";
        db.connection = Database.connectDB();

        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.preparedStatement.setInt(1, employeeID);
            db.resultSet = db.preparedStatement.executeQuery();
            if(db.resultSet.next()){
                return db.resultSet.getInt(1) > 0;
            }

        }catch (SQLException e){e.printStackTrace();}
        return false;
    }

    @FXML
    public void EmployeeUpdate() {
        String imie = textfield_imie.getText();
        String nazwisko = textfield_nazwisko.getText();
        String stanPracownika = combo_stan.getSelectionModel().getSelectedItem();
        String rokUrodzenia = textfield_rok.getText();
        String wynagrodzenie = textfield_wynagrodzenie.getText();
        String email = textfield_email.getText();
        String phone = textfield_telefon.getText();
        String stanowisko = combo_stanowisko.getSelectionModel().getSelectedItem();

        boolean isUpdated = updateEmployee(id_employee_selected, imie, nazwisko, stanPracownika, rokUrodzenia, wynagrodzenie, email, phone, stanowisko);

        if (isUpdated) {
            Employee_table.getItems().clear();
            addEmployeeShowData();
            addEmployeeClear();
            chartStan.getData().clear();
            barChart.getData().clear();
            chartEmployeeStanShow();
            chartEmployeeSalaryShow();
        }
    }

    @FXML
    public void EmployeeDelete() {
        boolean isDeleted = deleteEmployee(id_employee_selected);

        if (isDeleted) {
            Employee_table.getItems().clear();
            addEmployeeClear();
            addEmployeeShowData();
            chartStan.getData().clear();
            barChart.getData().clear();
            chartEmployeeStanShow();
            chartEmployeeSalaryShow();
        }
    }

    public boolean deleteEmployee(int id) {
        String sql = "DELETE FROM employee WHERE id_employee = ?";

        db.connection = Database.connectDB();

        try {
            if (id <= 0) {
                showAlert("Błąd", "Niepoprawne ID pracownika!", Alert.AlertType.ERROR);
                return false;
            }
            if (!employeeExists(id)) {
                showAlert("Błąd", "Nie znaleziono pracownika o podanym ID.", Alert.AlertType.ERROR);
                return false;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie usunięcia");
            alert.setHeaderText(null);
            alert.setContentText("Czy na pewno chcesz usunąć pracownika?");
            Optional<ButtonType> optional = alert.showAndWait();

            if (optional.isPresent() && optional.get() == ButtonType.OK) {
                db.preparedStatement = db.connection.prepareStatement(sql);
                db.preparedStatement.setInt(1, id);
                int affectedRows = db.preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    showAlert("Sukces", "Usunięcie pracownika zakończone sukcesem!", Alert.AlertType.INFORMATION);
                    return true;
                } else {
                    showAlert("Błąd", "Nie znaleziono pracownika o podanym ID.", Alert.AlertType.ERROR);
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Błąd", "Wystąpił problem podczas usuwania pracownika!", Alert.AlertType.ERROR);
        }
        return false;
    }


    @FXML
    public void searchEmployeeByName(String name) {
        FilteredList<Employee> filteredData = new FilteredList<>(employeeData, e -> true);
        filteredData.setPredicate(employee -> {
            if (name == null || name.isEmpty()) {
                return true;
            }
            return employee.getNazwisko().toLowerCase().startsWith(name.toLowerCase());
        });

        SortedList<Employee> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(Employee_table.comparatorProperty());
        Employee_table.setItems(sortedData);
    }

    @FXML
    public void exportDataExcel(){

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Pracownicy");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Imię", "Nazwisko", "Stan", "Rok Urodzenia", "Wynagrodzenie", "Email", "Telefon", "Stanowisko"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }
        // Dodaj dane z tabeli do pliku Excela
        for (int i = 0; i < employeeData.size(); i++) {
            Employee employee = employeeData.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(employee.getImie());
            row.createCell(1).setCellValue(employee.getNazwisko());
            row.createCell(2).setCellValue(employee.getStan_pracownika());
            row.createCell(3).setCellValue(employee.getRok_urodzenia());
            row.createCell(4).setCellValue(employee.getWynagrodzenie());
            row.createCell(5).setCellValue(employee.getEmail());
            row.createCell(6).setCellValue(employee.getPhone());
            row.createCell(7).setCellValue(employee.getStanowisko());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setTitle("Zapisz plik Excel");
        fileChooser.setInitialFileName("pracownicy.xlsx");
        File file = fileChooser.showSaveDialog(Employee_table.getScene().getWindow());

        if (file != null) {
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                workbook.close();

                // Informacja zwrotna dla użytkownika
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Eksport zakończony");
                alert.setHeaderText(null);
                alert.setContentText("Dane zostały pomyślnie wyeksportowane do pliku Excel!");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd eksportu");
                alert.setHeaderText(null);
                alert.setContentText("Wystąpił problem podczas zapisu pliku.");
                alert.showAndWait();
            }
        }
    }

    //PROJEKT


    @FXML
    private TableView<Project> project_table;

    @FXML
    private TableColumn<Project, String> col_nazwa;

    @FXML
    private TableColumn<Project, String> col_klient;

    @FXML
    private TableColumn<Project, String> col_miasto;

    @FXML
    private TableColumn<Project, String> col_deadline;

    @FXML
    private TableColumn<Project, String> col_etap;

    @FXML
    private TableColumn<Project, String> col_kosztorys;
    @FXML
    private TableColumn<Project, String> col_kwota;

    @FXML
    private TextField textfield_nazwa;

    @FXML
    private TextField textfield_klient;

    @FXML
    private TextField textfield_miasto;

    @FXML
    private DatePicker data_deadline;
    @FXML
    private ComboBox<String> combo_etap;
    @FXML
    private TextField textfield_kwota;
    @FXML
    private TextField textfield_kosztorys;
    @FXML
    private TextArea text_opis;
    @FXML
    private TextField searchproject;

    private String[] etapProjektu = {"Nie dotyczy", "Rozpoczecie", "Fundamenty", "Płyta", "Murowanie ścian",
            "Zbrojenie stropu", "Strop", "Wieźba dachu", "Pokrycie dachu", "Murowanie ścian nośnych", "Elewacja", "Koniec"};
    public void addEtaplist(){
        ArrayList<String> listEtap = new ArrayList<>();
        for(String data: etapProjektu){
            listEtap.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(etapProjektu);
        combo_etap.setItems(listData);
    }

    ObservableList<Project> projectData = FXCollections.observableArrayList();

    @FXML
    public ObservableList<Project> addProjectDataList(){

        String sql = "SELECT * FROM project";

        db.connection = Database.connectDB();
        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.resultSet = db.preparedStatement.executeQuery();
            while(db.resultSet.next()) {
                Integer id = db.resultSet.getInt("id_project");
                String nazwa = db.resultSet.getString("nazwa");
                String klient = AESUtil.decrypt(db.resultSet.getString("klient"));
                String miejscowosc = db.resultSet.getString("miejscowosc");
                LocalDate deadline = db.resultSet.getDate("deadline").toLocalDate();
                String etap = db.resultSet.getString("etap");
                String kosztorys = AESUtil.decrypt(db.resultSet.getString("kosztorys"));
                String oplacona = AESUtil.decrypt(db.resultSet.getString("oplacona"));
                String opis = db.resultSet.getString("opis");

                if (klient == null) {
                    showAlert("Błąd", "Nie udało się odszyfrować danych klienta!", Alert.AlertType.ERROR);
                    return null;
                }
                if (kosztorys == null) {
                    showAlert("Błąd", "Nie udało się odszyfrować ceny kosztorysu!", Alert.AlertType.ERROR);
                    return null;
                }
                if (oplacona == null) {
                    showAlert("Błąd", "Nie udało się odszyfrować opłaconej ceny!", Alert.AlertType.ERROR);
                    return null;
                }

                projectData.add(new Project(id, nazwa, klient, miejscowosc, deadline, etap, kosztorys, oplacona, opis));
            }
        }catch(Exception e){e.printStackTrace();}
        return projectData;
    }
    @FXML
    public void ProjectShowData(){
        ObservableList<Project> projectList = addProjectDataList();

        col_nazwa.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        col_klient.setCellValueFactory(new PropertyValueFactory<>("klient"));
        col_miasto.setCellValueFactory(new PropertyValueFactory<>("miejscowosc"));
        col_deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        col_etap.setCellValueFactory(new PropertyValueFactory<>("etap"));
        col_kosztorys.setCellValueFactory(new PropertyValueFactory<>("kosztorys"));
        col_kwota.setCellValueFactory(new PropertyValueFactory<>("oplacona"));
        project_table.setItems(projectList);

        col_kosztorys_nazwa.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        col_kosztorys_miasto.setCellValueFactory(new PropertyValueFactory<>("miejscowosc"));
        col_kosztorys_kosztorys.setCellValueFactory(new PropertyValueFactory<>("kosztorys"));
        kosztorys_table.setItems(projectList);

        colProjectName.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        colProjectClient.setCellValueFactory(new PropertyValueFactory<>("klient"));
        projectTable.setItems(projectList);
    }

    public void ProjectSelect(){
        Project projectData = project_table.getSelectionModel().getSelectedItem();
        int number = project_table.getSelectionModel().getSelectedIndex();
        if (number < 0) {
            return;
        }

        id_project_selected = projectData.getProjectID();
        textfield_nazwa.setText(projectData.getNazwa());
        textfield_klient.setText(projectData.getKlient());
        textfield_miasto.setText(projectData.getMiejscowosc());
        data_deadline.setValue(projectData.getDeadline());
        combo_etap.setValue(projectData.getEtap());
        textfield_kosztorys.setText(String.valueOf(projectData.getKosztorys()));
        textfield_kwota.setText(String.valueOf(projectData.getOplacona()));
        text_opis.setText(projectData.getOpis());
    }

    @FXML
    public void ProjectAdd() {
        String nazwa = textfield_nazwa.getText();
        String klient = textfield_klient.getText();
        String miejscowosc = textfield_miasto.getText();
        LocalDate deadline = data_deadline.getValue();
        String etap = combo_etap.getSelectionModel().getSelectedItem();
        String kosztorysStr = textfield_kosztorys.getText();
        String oplaconaStr = textfield_kwota.getText();
        String opis = text_opis.getText();

        boolean isAdded = addProject(nazwa, klient, miejscowosc, deadline, etap, kosztorysStr, oplaconaStr, opis);

        if (isAdded) {
            project_table.getItems().clear();
            ProjectShowData();
            ProjectClear();
            chartProjectStatus.getData().clear();
            chartDeadlineStatus.getData().clear();
            chartCost.getData().clear();
            chartProjectEnded();
            chartDeadlineStatus();
            chartPaymentStatus();
        }
    }

    public boolean addProject(String nazwa, String klient, String miejscowosc, LocalDate deadline, String etap, String kosztorysStr, String oplaconaStr, String opis) {
        String sql = "INSERT INTO project(nazwa, klient, miejscowosc, deadline, etap, kosztorys, oplacona, opis) VALUES (?,?,?,?,?,?,?,?)";
        db.connection = Database.connectDB();
        try {
            if (nazwa.isEmpty() || klient.isEmpty() || miejscowosc.isEmpty() || deadline == null ||
                    etap == null || kosztorysStr.isEmpty() || oplaconaStr.isEmpty() || opis.isEmpty()) {
                showAlert("Błąd", "Uzupełnij wszystkie pola przed edycją", Alert.AlertType.ERROR);
                return false;
            }

            int kosztorys = Integer.parseInt(kosztorysStr);
            int oplacona = Integer.parseInt(oplaconaStr);

            if(kosztorys < 0 ){
                showAlert("Błąd", "Kosztorys nie może być ujemny!", Alert.AlertType.ERROR);
                return false;
            }
            if( oplacona < 0 ){
                showAlert("Błąd", "Opłacona kwota nie może być ujemny!", Alert.AlertType.ERROR);
                return false;
            }

            if(kosztorys < oplacona){
                showAlert("Błąd", "Opłacona kwota nie może być większa niż wycena projektu!", Alert.AlertType.ERROR);
                return false;
            }

            if (deadline.isBefore(LocalDate.now())) {
                showAlert("Błąd", "Data zakończenia projektu nie może być w przeszłości!", Alert.AlertType.ERROR);
                return false;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Potwierdzenie");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Czy na pewno chcesz dodać projekt?");
            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                String encryptedKlient = AESUtil.encrypt(klient);
                String encryptedKosztorys = AESUtil.encrypt(String.valueOf(kosztorys));
                String encryptedOplacona = AESUtil.encrypt(String.valueOf(oplacona));


                if (encryptedKlient == null) {
                    showAlert("Błąd", "Nie udało się zaszyfrować danych klienta!", Alert.AlertType.ERROR);
                    return false;
                }
                if (encryptedKosztorys == null) {
                    showAlert("Błąd", "Nie udało się zaszyfrować ceny kosztorysu!", Alert.AlertType.ERROR);
                    return false;
                }
                if (encryptedOplacona == null) {
                    showAlert("Błąd", "Nie udało się zaszyfrować opłaconej ceny!", Alert.AlertType.ERROR);
                    return false;
                }

                db.preparedStatement = db.connection.prepareStatement(sql);
                db.preparedStatement.setString(1, nazwa);
                db.preparedStatement.setString(2, encryptedKlient);
                db.preparedStatement.setString(3, miejscowosc);
                db.preparedStatement.setDate(4, Date.valueOf(deadline));
                db.preparedStatement.setString(5, etap);
                db.preparedStatement.setString(6, encryptedKosztorys);
                db.preparedStatement.setString(7, encryptedOplacona);
                db.preparedStatement.setString(8, opis);
                db.preparedStatement.executeUpdate();

                showAlert("Sukces", "Projekt został pomyślnie dodany!", Alert.AlertType.INFORMATION);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Błąd", "Wystąpił problem podczas dodawania projektu!", Alert.AlertType.ERROR);
        }
        return false;
    }




    @FXML
    public void ProjectClear(){
        textfield_nazwa.clear();
        textfield_klient.clear();
        textfield_miasto.clear();
        data_deadline.setValue(null);
        combo_etap.getSelectionModel().clearSelection();
        textfield_kosztorys.clear();
        textfield_kwota.clear();
        text_opis.clear();
    }

    @FXML
    public void ProjectUpdate() {
        String nazwa = textfield_nazwa.getText();
        String klient = textfield_klient.getText();
        String miejscowosc = textfield_miasto.getText();
        LocalDate deadline = data_deadline.getValue();
        String etap = combo_etap.getSelectionModel().getSelectedItem();
        String kosztorysStr = textfield_kosztorys.getText();
        String oplaconaStr = textfield_kwota.getText();
        String opis = text_opis.getText();

        boolean isUpdated = updateProject(id_project_selected, nazwa, klient, miejscowosc, deadline, etap, kosztorysStr, oplaconaStr, opis);

        if (isUpdated) {
            project_table.getItems().clear();
            ProjectShowData();
            ProjectClear();
            chartProjectStatus.getData().clear();
            chartDeadlineStatus.getData().clear();
            chartCost.getData().clear();
            chartProjectEnded();
            chartDeadlineStatus();
            chartPaymentStatus();
        }
    }


    public boolean updateProject(int projectId, String nazwa, String klient, String miejscowosc, LocalDate deadline, String etap, String kosztorysStr, String oplaconaStr, String opis) {
        String sql = "UPDATE project SET nazwa = ?, klient = ?, miejscowosc = ?, deadline = ?, etap = ?, kosztorys = ?, oplacona = ?, opis = ? WHERE id_project = ?";
        db.connection = Database.connectDB();

        try {
            if (nazwa.isEmpty() || klient.isEmpty() || miejscowosc.isEmpty() || deadline == null ||
                    etap == null || kosztorysStr.isEmpty() || oplaconaStr.isEmpty() || opis.isEmpty()) {
                showAlert("Błąd", "Uzupełnij wszystkie pola przed edycją", Alert.AlertType.ERROR);
                return false;
            }

            int kosztorys = Integer.parseInt(kosztorysStr);
            int oplacona = Integer.parseInt(oplaconaStr);

            if(kosztorys < 0 ){
                showAlert("Błąd", "Kosztorys nie może być ujemny!", Alert.AlertType.ERROR);
                return false;
            }
            if( oplacona < 0 ){
                showAlert("Błąd", "Opłacona kwota nie może być ujemny!", Alert.AlertType.ERROR);
                return false;
            }

            if(kosztorys < oplacona){
                showAlert("Błąd", "Opłacona kwota nie może być większa niż wycena projektu!", Alert.AlertType.ERROR);
                return false;
            }

            if (deadline.isBefore(LocalDate.now())) {
                showAlert("Błąd", "Data zakończenia projektu nie może być w przeszłości!", Alert.AlertType.ERROR);
                return false;
            }

            if (projectId <= 0) {
                showAlert("Błąd", "Nie wybrano pracownika do edycji.", Alert.AlertType.ERROR);
                return false;
            }
            if (!projectExists(projectId)) {
                showAlert("Błąd", "Nie znaleziono projektu o podanym ID.", Alert.AlertType.ERROR);
                return false;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Potwierdzenie edycji");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Czy na pewno chcesz edytować projekt?");
            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                String encryptedKlient = AESUtil.encrypt(klient);
                String encryptedKosztorys = AESUtil.encrypt(String.valueOf(kosztorys));
                String encryptedOplacona = AESUtil.encrypt(String.valueOf(oplacona));


                if (encryptedKlient == null) {
                    showAlert("Błąd", "Nie udało się zaszyfrować danych klienta!", Alert.AlertType.ERROR);
                    return false;
                }
                if (encryptedKosztorys == null) {
                    showAlert("Błąd", "Nie udało się zaszyfrować ceny kosztorysu!", Alert.AlertType.ERROR);
                    return false;
                }
                if (encryptedOplacona == null) {
                    showAlert("Błąd", "Nie udało się zaszyfrować opłaconej ceny!", Alert.AlertType.ERROR);
                    return false;
                }

                db.preparedStatement = db.connection.prepareStatement(sql);
                db.preparedStatement.setString(1, nazwa);
                db.preparedStatement.setString(2, encryptedKlient);
                db.preparedStatement.setString(3, miejscowosc);
                db.preparedStatement.setDate(4, Date.valueOf(deadline));
                db.preparedStatement.setString(5, etap);
                db.preparedStatement.setString(6, encryptedKosztorys);
                db.preparedStatement.setString(7, encryptedOplacona);
                db.preparedStatement.setString(8, opis);
                db.preparedStatement.setInt(9, projectId);
                db.preparedStatement.executeUpdate();

                showAlert("Sukces", "Projekt został zaktualizowany!", Alert.AlertType.INFORMATION);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Błąd", "Wystąpił problem podczas edycji projektu!", Alert.AlertType.ERROR);
        }
        return false;
    }



    public boolean projectExists(int projectID){
        String sql = "SELECT COUNT(*) FROM project where id_project = ?";
        db.connection = Database.connectDB();

        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.preparedStatement.setInt(1, projectID);
            db.resultSet = db.preparedStatement.executeQuery();
            if(db.resultSet.next()){
                return db.resultSet.getInt(1) > 0;
            }

        }catch (SQLException e){e.printStackTrace();}
        return false;
    }

    @FXML
    public void ProjectDelete() {
        boolean isDeleted = deleteProject(id_project_selected);

        if (isDeleted) {
            project_table.getItems().clear();
            ProjectClear();
            ProjectShowData();
            chartProjectStatus.getData().clear();
            chartDeadlineStatus.getData().clear();
            chartCost.getData().clear();
            chartProjectStatus.applyCss();
            chartProjectStatus.layout();
            chartDeadlineStatus.applyCss();
            chartDeadlineStatus.layout();
            chartCost.applyCss();
            chartCost.layout();
            chartProjectEnded();
            chartDeadlineStatus();
            chartPaymentStatus();
        }
    }

    public boolean deleteProject(int projectId) {
        String sql = "DELETE FROM project WHERE id_project = ?";

        db.connection = Database.connectDB();

        try {
            if (projectId <= 0) {
                showAlert("Błąd", "Nie wybrano pracownika do edycji.", Alert.AlertType.ERROR);
                return false;
            }
            if (!projectExists(projectId)) {
                showAlert("Błąd", "Nie znaleziono pracownika o podanym ID.", Alert.AlertType.ERROR);
                return false;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Potwierdzenie usunięcia");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Czy na pewno chcesz usunąć projekt?");
            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                db.preparedStatement = db.connection.prepareStatement(sql);
                db.preparedStatement.setInt(1, projectId);
                db.preparedStatement.executeUpdate();

                if (hasReports(projectId)) {
                    deleteReport(projectId);
                }

                showAlert("Sukces", "Usunięcie projektu zakończone sukcesem!", Alert.AlertType.INFORMATION);
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Błąd", "Wystąpił problem podczas usuwania projektu!", Alert.AlertType.ERROR);
        }
        return false;
    }

    public boolean hasReports(int projectId) {
        String sql = "SELECT COUNT(*) FROM reports WHERE id_project = ?";
        db.connection = Database.connectDB();

        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.preparedStatement.setInt(1, projectId);
            db.resultSet = db.preparedStatement.executeQuery();

            if (db.resultSet.next()) {
                return db.resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    public void searchProject(String name) {
        FilteredList<Project> filteredData = new FilteredList<>(projectData, e -> true);
        filteredData.setPredicate(project -> {
            if (name == null || name.isEmpty()) {
                return true;
            }
            return project.getNazwa().toLowerCase().startsWith(name.toLowerCase());
        });

        SortedList<Project> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(project_table.comparatorProperty());
        project_table.setItems(sortedData);
    }

    @FXML
    public void exportDataExcelProject(){


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Projekty");


        Row headerRow = sheet.createRow(0);
        String[] headers = {"Nazwa projektu", "Klient", "Miejscowość", "Data zakończenia", "Etap", "Kosztorys", "Opłacona kwota", "Opis"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        // Dodaj dane z tabeli do pliku Excela
        for (int i = 0; i < projectData.size(); i++) {
            Project project = projectData.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(project.getNazwa());
            row.createCell(1).setCellValue(project.getKlient());
            row.createCell(2).setCellValue(project.getMiejscowosc());
            row.createCell(3).setCellValue(project.getDeadline());
            row.createCell(4).setCellValue(project.getEtap());
            row.createCell(5).setCellValue(project.getKosztorys());
            row.createCell(6).setCellValue(project.getOplacona());
            row.createCell(7).setCellValue(project.getOpis());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setTitle("Zapisz plik Excel");
        fileChooser.setInitialFileName("projekty.xlsx");
        File file = fileChooser.showSaveDialog(project_table.getScene().getWindow());

        if (file != null) {
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                workbook.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Eksport zakończony");
                alert.setHeaderText(null);
                alert.setContentText("Dane zostały pomyślnie wyeksportowane do pliku Excel!");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd eksportu");
                alert.setHeaderText(null);
                alert.setContentText("Wystąpił problem podczas zapisu pliku.");
                alert.showAndWait();
            }
        }
    }


    //KOSZTORYS

    @FXML
    private TableView<Project> kosztorys_table;

    @FXML
    private TableColumn<Project, String> col_kosztorys_nazwa;

    @FXML
    private TableColumn<Project, String> col_kosztorys_miasto;

    @FXML
    private TableColumn<Project, String> col_kosztorys_kosztorys;

    @FXML
    private Label label_nazwa;

    @FXML
    private Label label_kosztorys;


    public void KosztorysSelect(){
        Project projectData = kosztorys_table.getSelectionModel().getSelectedItem();
        int number = kosztorys_table.getSelectionModel().getSelectedIndex();
        if (number < 0) {
            return;
        }
        globalOplacona = Integer.parseInt(projectData.getOplacona());
        id_project_selected_kosztorys = projectData.getProjectID();
        label_nazwa.setText(projectData.getNazwa());
        label_kosztorys.setText(String.valueOf(projectData.getKosztorys()) + " zł");
    }
    @FXML
    private TextField textfield_fundamenty;

    @FXML
    private Label label_fundamenty;

    @FXML
    private TextField textfield_hydroizolacja;

    @FXML
    private Label label_hydroizolacja;

    @FXML
    private TextField textfield_murowanienosne;

    @FXML
    private Label label_murowanienosne;

    @FXML
    private TextField textfield_murowaniedzialowe;

    @FXML
    private Label label_murowaniedzialowe;

    @FXML
    private TextField textfield_zbrojenie;

    @FXML
    private Label zbrojenie;

    @FXML
    private TextField textfield_strop;

    @FXML
    private Label label_strop;

    @FXML
    private TextField textfield_wiezba;

    @FXML
    private Label label_wiezba;

    @FXML
    private TextField textfield_pokrycie;

    @FXML
    private Label label_pokrycie;

    @FXML
    private TextField textfield_kominy;

    @FXML
    private Label label_kominy;

    @FXML
    private TextField textfield_oknadom;

    @FXML
    private Label label_oknadom;

    @FXML
    private TextField textfield_elewacja;

    @FXML
    private Label label_elewacja;

    @FXML
    private TextField textfield_kosztydom;

    @FXML
    private Label label_kosztydom;
    public StawkiDom stawkiDom;
    public StawkiDom pobierzStawkiDom() {
        String sql = "SELECT * FROM stawkidom";

        db.connection = Database.connectDB();
        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.resultSet = db.preparedStatement.executeQuery();
            if (db.resultSet.next()) {
                stawkiDom = new StawkiDom(
                        db.resultSet.getDouble("fundamenty"),
                        db.resultSet.getDouble("hydroizolacja_fundamentow"),
                        db.resultSet.getDouble("murowanie_nosne"),
                        db.resultSet.getDouble("murowanie_dzialowe"),
                        db.resultSet.getDouble("zbrojenie"),
                        db.resultSet.getDouble("strop"),
                        db.resultSet.getDouble("wiezba"),
                        db.resultSet.getDouble("pokrycie"),
                        db.resultSet.getDouble("kominy"),
                        db.resultSet.getDouble("montaz_okien"),
                        db.resultSet.getDouble("elewacja"),
                        db.resultSet.getDouble("pozostale_koszty")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stawkiDom;
    }

    @FXML
    private TextField textfield_tynkowanie;

    @FXML
    private Label label_tynkowanie;

    @FXML
    private TextField textfield_malowanie;

    @FXML
    private Label label_malowanie;

    @FXML
    private TextField textfield_szpachlowanie;

    @FXML
    private Label label_szpachlowanie;

    @FXML
    private TextField textfield_murowanieremont;

    @FXML
    private Label label_murowanieremont;

    @FXML
    private TextField textfield_montazGK;

    @FXML
    private Label label_montazGK;

    @FXML
    private TextField textfield_panele;

    @FXML
    private Label label_panele;

    @FXML
    private TextField textfield_instalacje;

    @FXML
    private Label label_instalacje;

    @FXML
    private TextField textfield_bialymontaz;

    @FXML
    private Label label_bialymontaz;

    @FXML
    private TextField textfield_montazdrzwi;

    @FXML
    private Label label_montazdrzwi;

    @FXML
    private TextField textfield_okienremont;

    @FXML
    private Label label_oknaremont;

    @FXML
    private TextField textfield_plytki;

    @FXML
    private Label label_plytki;

    @FXML
    private TextField textfield_kosztyremont;

    @FXML
    private Label label_kosztyremont;
    public StawkiRemont stawkiRemont;
    public StawkiRemont pobierzStawkiRemont() {
        String sql = "SELECT * FROM stawkiremont";

        db.connection = Database.connectDB();
        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.resultSet = db.preparedStatement.executeQuery();
            if (db.resultSet.next()) {
                stawkiRemont = new StawkiRemont(
                        db.resultSet.getDouble("tynkowanie"),
                        db.resultSet.getDouble("malowanie"),
                        db.resultSet.getDouble("szpachlowanie"),
                        db.resultSet.getDouble("murowanie"),
                        db.resultSet.getDouble("montazGK"),
                        db.resultSet.getDouble("ukladanie_paneli"),
                        db.resultSet.getDouble("instalacja"),
                        db.resultSet.getDouble("bialy_montaz"),
                        db.resultSet.getDouble("montaz_drzwi"),
                        db.resultSet.getDouble("montaz_okna"),
                        db.resultSet.getDouble("ukladanie_plytek"),
                        db.resultSet.getDouble("pozostale_koszta")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stawkiRemont;
    }

    @FXML
    public void KosztorysClear(){

        //dom
        textfield_fundamenty.setText("0");
        textfield_hydroizolacja.setText("0");
        textfield_murowanienosne.setText("0");
        textfield_murowaniedzialowe.setText("0");
        textfield_zbrojenie.setText("0");
        textfield_strop.setText("0");
        textfield_wiezba.setText("0");
        textfield_pokrycie.setText("0");
        textfield_kominy.setText("0");
        textfield_oknadom.setText("0");
        textfield_elewacja.setText("0");
        textfield_kosztydom.setText("0");

        //remont

        textfield_tynkowanie.setText("0");
        textfield_malowanie.setText("0");
        textfield_szpachlowanie.setText("0");
        textfield_murowanieremont.setText("0");
        textfield_montazGK.setText("0");
        textfield_panele.setText("0");
        textfield_instalacje.setText("0");
        textfield_bialymontaz.setText("0");
        textfield_montazdrzwi.setText("0");
        textfield_okienremont.setText("0");
        textfield_plytki.setText("0");
        textfield_kosztyremont.setText("0");


    }
    @FXML
    public void obliczKosztorys() {
        double totalCost = 0.0;

        try {
            // dom
            if (!textfield_fundamenty.getText().isEmpty()) {
                double kosztFundamenty = Double.parseDouble(textfield_fundamenty.getText()) * stawkiDom.getZalewanieFundamentow();
                label_fundamenty.setText(" : " + kosztFundamenty + " zł");
                totalCost += kosztFundamenty;
            }

            double kosztHydroizolacja = Double.parseDouble(textfield_hydroizolacja.getText()) * stawkiDom.getHydroizolacjaFundamentow();
            label_hydroizolacja.setText(" : " + kosztHydroizolacja + " zł");
            totalCost += kosztHydroizolacja;

            double kosztMurowanieNosne = Double.parseDouble(textfield_murowanienosne.getText()) * stawkiDom.getMurowanieScianNosnych();
            label_murowanienosne.setText(" : " + kosztMurowanieNosne + " zł");
            totalCost += kosztMurowanieNosne;

            double kosztMurowanieDzialowe = Double.parseDouble(textfield_murowaniedzialowe.getText()) * stawkiDom.getMurowanieScianDzialowych();
            label_murowaniedzialowe.setText(" : " + kosztMurowanieDzialowe + " zł");
            totalCost += kosztMurowanieDzialowe;

            double kosztZbrojenie = Double.parseDouble(textfield_zbrojenie.getText()) * stawkiDom.getZbrojenieStalowe();
            zbrojenie.setText(" : " + kosztZbrojenie + " zł");
            totalCost += kosztZbrojenie;

            double kosztStrop = Double.parseDouble(textfield_strop.getText()) * stawkiDom.getStropISchody();
            label_strop.setText(" : " + kosztStrop + " zł");
            totalCost += kosztStrop;

            double kosztWiezba = Double.parseDouble(textfield_wiezba.getText()) * stawkiDom.getMontazWiezbyDachowej();
            label_wiezba.setText(" : " + kosztWiezba + " zł");
            totalCost += kosztWiezba;

            double kosztPokrycie = Double.parseDouble(textfield_pokrycie.getText()) * stawkiDom.getPokrycieDachowe();
            label_pokrycie.setText(" : " + kosztPokrycie + " zł");
            totalCost += kosztPokrycie;

            double kosztKominy = Double.parseDouble(textfield_kominy.getText()) * stawkiDom.getKominyIWentylacja();
            label_kominy.setText(" : " + kosztKominy + " zł");
            totalCost += kosztKominy;

            double kosztOknaDom = Double.parseDouble(textfield_oknadom.getText()) * stawkiDom.getMontazOkienDachowych();
            label_oknadom.setText(" : " + kosztOknaDom + " zł");
            totalCost += kosztOknaDom;

            double kosztElewacja = Double.parseDouble(textfield_elewacja.getText()) * stawkiDom.getElewacja();
            label_elewacja.setText(" : " + kosztElewacja + " zł");
            totalCost += kosztElewacja;

            // remont
            double kosztTynkowanie = Double.parseDouble(textfield_tynkowanie.getText()) * stawkiRemont.getTynkowanieScian();
            label_tynkowanie.setText(" : " + kosztTynkowanie + " zł");
            totalCost += kosztTynkowanie;

            double kosztMalowanie = Double.parseDouble(textfield_malowanie.getText()) * stawkiRemont.getMalowanieITapetowanie();
            label_malowanie.setText(" : " + kosztMalowanie + " zł");
            totalCost += kosztMalowanie;

            double kosztSzpachlowanie = Double.parseDouble(textfield_szpachlowanie.getText()) * stawkiRemont.getSzpachlowanieScian();
            label_szpachlowanie.setText(" : " + kosztSzpachlowanie + " zł");
            totalCost += kosztSzpachlowanie;

            double kosztMurowanieRemont = Double.parseDouble(textfield_murowanieremont.getText()) * stawkiRemont.getMurowanieScian();
            label_murowanieremont.setText(" : " + kosztMurowanieRemont + " zł");
            totalCost += kosztMurowanieRemont;

            double kosztMontazGK = Double.parseDouble(textfield_montazGK.getText()) * stawkiRemont.getMontazPlytGK();
            label_montazGK.setText(" : " + kosztMontazGK + " zł");
            totalCost += kosztMontazGK;

            double kosztPanele = Double.parseDouble(textfield_panele.getText()) * stawkiRemont.getUkladaniePaneliIParkietu();
            label_panele.setText(" : " + kosztPanele + " zł");
            totalCost += kosztPanele;

            double kosztInstalacje = Double.parseDouble(textfield_instalacje.getText()) * stawkiRemont.getPraceInstalacyjne();
            label_instalacje.setText(" : " + kosztInstalacje + " zł");
            totalCost += kosztInstalacje;

            double kosztBialyMontaz = Double.parseDouble(textfield_bialymontaz.getText()) * stawkiRemont.getBialyMontaz();
            label_bialymontaz.setText(" : " + kosztBialyMontaz + " zł");
            totalCost += kosztBialyMontaz;

            double kosztMontazDrzwi = Double.parseDouble(textfield_montazdrzwi.getText()) * stawkiRemont.getMontazDrzwiWewnetrznych();
            label_montazdrzwi.setText(" : " + kosztMontazDrzwi + " zł");
            totalCost += kosztMontazDrzwi;

            double kosztMontazOkienRemont = Double.parseDouble(textfield_okienremont.getText()) * stawkiRemont.getMontazOkien();
            label_oknaremont.setText(" : " + kosztMontazOkienRemont + " zł");
            totalCost += kosztMontazOkienRemont;

            double kosztPlytki = Double.parseDouble(textfield_plytki.getText()) * stawkiRemont.getUkladaniePlytek();
            label_plytki.setText(" : " + kosztPlytki + " zł");
            totalCost += kosztPlytki;

            if (!textfield_kosztydom.getText().isEmpty()) {
                double kosztPozostaleDom = Double.parseDouble(textfield_kosztydom.getText()) * stawkiDom.getPozostaleKosztyDodatkowe();
                label_kosztydom.setText(" : " + kosztPozostaleDom + " zł");
                totalCost += kosztPozostaleDom;
            }

            if (!textfield_kosztyremont.getText().isEmpty()) {
                double kosztPozostaleRemont = Double.parseDouble(textfield_kosztyremont.getText()) * stawkiRemont.getPozostaleKosztyDodatkowe();
                label_kosztyremont.setText(" : " + kosztPozostaleRemont + " zł");
                totalCost += kosztPozostaleRemont;
            }

            if(totalCost < globalOplacona){
                showAlert("Błąd" , " Kosztorys nie może być wiekszy niż opłacona kwota przez klienta!", Alert.AlertType.ERROR);
                return;
            }

            String sql = "UPDATE project SET kosztorys = ? WHERE id_project = ?";
            String encryptedKosztorys;
            try {
                encryptedKosztorys = AESUtil.encrypt(String.valueOf(totalCost));
            } catch (Exception e) {
                showAlert("Błąd", "Nie udało się zaszyfrować ceny kosztorysu!", Alert.AlertType.ERROR);
                return;
            }

            db.preparedStatement = db.connection.prepareStatement(sql);
            db.preparedStatement.setString(1, encryptedKosztorys);
            db.preparedStatement.setInt(2, id_project_selected_kosztorys);
            db.preparedStatement.executeUpdate();

            // Informacja zwrotna dla użytkownika
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Kosztorys został obliczony i zapisany!");
            alert.showAndWait();

            kosztorys_table.getItems().clear();
            label_kosztorys.setText(totalCost + " zł");
            ProjectShowData();
            KosztorysClear();

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Upewnij się, że wszystkie pola zawierają liczby!");
            alert.showAndWait();
        }
    }

    @FXML
    public void StawkiWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stawki.fxml"));
            GlobalData gd = new GlobalData(500, 580);
            Scene scene = new Scene(fxmlLoader.load(), gd.XWidth, gd.YHeight);
            //nasluchiwanie myszki i klawiatury
            scene.addEventFilter(MouseEvent.ANY, event -> SessionManager.getInstance().resetActivity());
            scene.addEventFilter(KeyEvent.ANY, event -> SessionManager.getInstance().resetActivity());

            Stage stawkiStage = new Stage();
            stawkiStage.setTitle("Zmień stawki kosztorysu");
            stawkiStage.setScene(scene);
            Image icon = new Image(getClass().getResourceAsStream("/images/ikona_wejsciowa.png"));
            stawkiStage.getIcons().add(icon);
            stawkiStage.initStyle(StageStyle.DECORATED);
            stawkiStage.initModality(Modality.APPLICATION_MODAL);

            stawkiStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //RAPORTOWANIE

    @FXML
    public PieChart chartStan;
    @FXML
    public void chartEmployeeStanShow () {

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Na delegacji", getEmployeeCountByState("delegacja")),
                new PieChart.Data("Chorzy", getEmployeeCountByState("chory")),
                new PieChart.Data("Obecni", getEmployeeCountByState("obecny")),
                new PieChart.Data("Nieobecni", getEmployeeCountByState("nieobecny"))
        );

        chartStan.setData(pieChartData);
    }
    public int getEmployeeCountByState (String state){
        String sql = "SELECT COUNT(*) FROM employee WHERE stan_pracownika = ?";
        db.connection = Database.connectDB();
        int count = 0;

        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.preparedStatement.setString(1, state);
            db.resultSet = db.preparedStatement.executeQuery();

            if (db.resultSet.next()) {
                count = db.resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @FXML
    private BarChart<String, Integer> barChart;
    public void chartEmployeeSalaryShow(){

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("ponizej 5 tys");
        series.getData().add(new XYChart.Data<>("", getEmployeeBySalary(0, 4999)));

        XYChart.Series<String, Integer> series2 = new XYChart.Series<>();
        series2.setName("5-6 tys");
        series2.getData().add(new XYChart.Data<>("", getEmployeeBySalary(5000, 6999)));

        XYChart.Series<String, Integer> series3 = new XYChart.Series<>();
        series3.setName("7-8 tys");
        series3.getData().add(new XYChart.Data<>("", getEmployeeBySalary(7000, 8999)));

        XYChart.Series<String, Integer> series4 = new XYChart.Series<>();
        series4.setName("9-10 tys");
        series4.getData().add(new XYChart.Data<>("", getEmployeeBySalary(9000, 10999)));

        barChart.getData().addAll(series,series2,series3,series4);
    }

    @FXML
    public int getEmployeeBySalary(int min, int max) {
        String sql = "SELECT wynagrodzenie FROM employee";
        db.connection = Database.connectDB();
        int count = 0;

        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.resultSet = db.preparedStatement.executeQuery();

            while (db.resultSet.next()) {
                String encryptedSalary = db.resultSet.getString("wynagrodzenie");
                String decryptedSalaryStr;
                try {
                    decryptedSalaryStr = AESUtil.decrypt(encryptedSalary);
                } catch (Exception e) {
                    showAlert("Błąd", "Nie udało się zaszyfrować ceny kosztorysu!", Alert.AlertType.ERROR);
                    return 0;
                }

                if (decryptedSalaryStr != null) {
                    double decryptedSalary = Double.parseDouble(decryptedSalaryStr);

                    if (decryptedSalary >= min && decryptedSalary <= max) {
                        count++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }


    @FXML
    public PieChart chartProjectStatus;

    public void chartProjectEnded(){
        int completedProjects = getProjectByStatus("Koniec");
        int otherProjects = getProjectByOtherStatuses("Koniec");

        ObservableList<PieChart.Data> projectStatusData = FXCollections.observableArrayList(
                new PieChart.Data("Zakończone", completedProjects),
                new PieChart.Data("W toku", otherProjects)
        );
        chartProjectStatus.setData(projectStatusData);
    }
    public int getProjectByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM project WHERE etap = ?";
        db.connection = Database.connectDB();
        int count = 0;

        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.preparedStatement.setString(1, status);
            db.resultSet = db.preparedStatement.executeQuery();

            if (db.resultSet.next()) {
                count = db.resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public int getProjectByOtherStatuses(String excludeStatus) {
        String sql = "SELECT COUNT(*) FROM project WHERE etap != ?";
        db.connection = Database.connectDB();
        int count = 0;

        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.preparedStatement.setString(1, excludeStatus);
            db.resultSet = db.preparedStatement.executeQuery();

            if (db.resultSet.next()) {
                count = db.resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @FXML
    public PieChart chartDeadlineStatus;
    public void chartDeadlineStatus(){
        ObservableList<PieChart.Data> deadlineData = FXCollections.observableArrayList(
                new PieChart.Data("W terminie", getProjectsOnTime(true)),
                new PieChart.Data("Po terminie", getProjectsOnTime(false))
        );

        chartDeadlineStatus.setData(deadlineData);

    }

    public int getProjectsOnTime(boolean onTime) {
        String sql = onTime
                ? "SELECT COUNT(*) FROM project WHERE deadline >= NOW()"
                : "SELECT COUNT(*) FROM project WHERE deadline < NOW()";
        db.connection = Database.connectDB();
        int count = 0;

        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.resultSet = db.preparedStatement.executeQuery();

            if (db.resultSet.next()) {
                count = db.resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @FXML
    private BarChart<String, Number> chartCost;


    private void chartPaymentStatus() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Zapłacono w całości");

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Niepełna zapłata");

        int fullyPaid = getProjectOplacone();
        int partiallyPaid = getProjectNieOplacone();

        series.getData().add(new XYChart.Data<>("", fullyPaid));
        series2.getData().add(new XYChart.Data<>("", partiallyPaid));

        chartCost.getData().addAll(series,series2);

    }

    private int getProjectOplacone() {
        String sql = "SELECT oplacona, kosztorys FROM project";
        int count = 0;

        try {
            db.connection = Database.connectDB();
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.resultSet = db.preparedStatement.executeQuery();

            while (db.resultSet.next()) {
                String encryptedOplacona = db.resultSet.getString(1);
                String encryptedKosztorys = db.resultSet.getString(2);

                try {
                    double oplacona = Double.parseDouble(AESUtil.decrypt(encryptedOplacona));
                    double kosztorys = Double.parseDouble(AESUtil.decrypt(encryptedKosztorys));

                    if (oplacona == kosztorys) {
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Błąd", "Nie udało się odszyfrować wartości!", Alert.AlertType.ERROR);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (db.resultSet != null) db.resultSet.close();
                if (db.preparedStatement != null) db.preparedStatement.close();
                if (db.connection != null) db.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return count;
    }


    private int getProjectNieOplacone() {
        String sql = "SELECT oplacona, kosztorys FROM project";
        int count = 0;

        try {
            db.connection = Database.connectDB();
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.resultSet = db.preparedStatement.executeQuery();

            while (db.resultSet.next()) {
                String encryptedOplacona = db.resultSet.getString(1);
                String encryptedKosztorys = db.resultSet.getString(2);

                try {
                    double oplacona = Double.parseDouble(AESUtil.decrypt(encryptedOplacona));
                    double kosztorys = Double.parseDouble(AESUtil.decrypt(encryptedKosztorys));

                    if (oplacona < kosztorys) {
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Błąd", "Nie udało się odszyfrować wartości!", Alert.AlertType.ERROR);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }


    public void ShowCharts(){
        chartEmployeeStanShow();
        chartEmployeeSalaryShow();
        chartProjectEnded();
        chartDeadlineStatus();
        chartPaymentStatus();
    }

    @FXML
    private TableView<Project> projectTable;
    @FXML
    private TableColumn<Project, String> colProjectName;
    @FXML
    private TableColumn<Project, String> colProjectClient;
    @FXML
    private ListView<Report> reportListView;
    @FXML
    private TextArea textareaRaport;

    public void projectRaportSelect() {
        Project selectedProject = projectTable.getSelectionModel().getSelectedItem();
        if (selectedProject == null) return;

        id_project_selected_raport = selectedProject.getProjectID();
        ShowReports();
    }
    public void ShowReports(){
        ObservableList<Report> reports = getReportsForProject(id_project_selected_raport);
        reportListView.setItems(reports);
        reportListView.setCellFactory(lv -> new ListCell<Report>() {
            @Override
            protected void updateItem(Report report, boolean empty) {
                super.updateItem(report, empty);
                if (empty || report == null) {
                    setText(null);
                } else {
                    setText(report.getContent() + " - Data dodania: " + report.getDate());
                }
            }
        });
    }
    public void ReportSelect() {
        Report selectedReport = reportListView.getSelectionModel().getSelectedItem();
        if (selectedReport == null) return;

        id_raport_selected = selectedReport.getId();
        textareaRaport.setText(selectedReport.getContent());
    }
    public ObservableList<Report> getReportsForProject(int projectId) {
        ObservableList<Report> reportList = FXCollections.observableArrayList();
        String sql = "SELECT id_report, report_content, report_date FROM reports WHERE id_project = ?";
        db.connection = Database.connectDB();

        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.preparedStatement.setInt(1, projectId);
            db.resultSet = db.preparedStatement.executeQuery();

            while (db.resultSet.next()) {
                Integer id = db.resultSet.getInt("id_report");
                String content = db.resultSet.getString("report_content");
                LocalDate date = db.resultSet.getDate("report_date").toLocalDate();

                Report report = new Report(id,content, date);
                reportList.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reportList;
    }

    @FXML
    public void addReport() {
        String reportContent = textareaRaport.getText();
        LocalDate reportDate = LocalDate.now();
        int projectId = id_project_selected_raport;

        boolean isAdded = reportAdd(projectId, reportContent, reportDate);

        if (isAdded) {
            reportListView.getItems().clear();
            ShowReports();
        }
    }

    public boolean reportAdd(int projectId, String reportContent, LocalDate reportDate) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Potwierdzenie");
        confirmationAlert.setHeaderText("Czy na pewno chcesz dodać raport?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (reportContent.isEmpty() || reportDate == null){
            showAlert("Błąd", "Uzupełnij wszystkie pola przed dodaniem", Alert.AlertType.ERROR);
            return false;
        }
        if (projectId <= 0) {
            showAlert("Błąd", "ID projektu nie może być ujemne!", Alert.AlertType.ERROR);
            return false;
        }
        if (!projectExists(projectId)) {
            showAlert("Błąd", "Nie znaleziono raportów o podanym ID projektu.", Alert.AlertType.ERROR);
            return false;
        }
        if (reportDate.isBefore(LocalDate.now())) {
            showAlert("Błąd", "Data dodania raportu nie moze być z przeszłości!", Alert.AlertType.ERROR);
            return false;
        }

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "INSERT INTO reports (id_project, report_content, report_date) VALUES (?, ?, ?)";
            db.connection = Database.connectDB();

            try {
                db.preparedStatement = db.connection.prepareStatement(sql);
                db.preparedStatement.setInt(1, projectId);
                db.preparedStatement.setString(2, reportContent);
                db.preparedStatement.setDate(3, java.sql.Date.valueOf(reportDate));
                db.preparedStatement.executeUpdate();

                showAlert("Sukces!", "Raport został pomyślnie dodany.", Alert.AlertType.INFORMATION);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Błąd!", "Nie udało się dodać raportu.", Alert.AlertType.ERROR);
            }
        }
        return false;
    }

    @FXML
    public void removeReport() {
        Report selectedReport = reportListView.getSelectionModel().getSelectedItem();

        if (selectedReport == null) {
            showAlert("Ostrzeżenie", "Nie można usunąć raportu. Nie wybrano raportu do usunięcia.", Alert.AlertType.WARNING);
            return;
        }

        boolean isDeleted = deleteReport(selectedReport.getId());
        if (isDeleted) {
            reportListView.getItems().clear();
            ShowReports();
        }
    }
    public boolean deleteReport(int reportId) {

        if (reportId <= 0) {
            showAlert("Błąd", "Wybrano rekord o mniejszej wartości niz 0.", Alert.AlertType.ERROR);
            return false;
        }
        if (!raportExists(reportId)) {
            showAlert("Błąd", "Nie znaleziono projektu o podanym ID.", Alert.AlertType.ERROR);
            return false;
        }


        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Potwierdzenie");
        confirmationAlert.setHeaderText("Czy na pewno chcesz usunąć raport?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "DELETE FROM reports WHERE id_report = ?";
            db.connection = Database.connectDB();

            try {
                db.preparedStatement = db.connection.prepareStatement(sql);
                db.preparedStatement.setInt(1, reportId);
                db.preparedStatement.executeUpdate();

                showAlert("Sukces", "Raport został pomyślnie usunięty.", Alert.AlertType.INFORMATION);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Błąd", "Nie udało się usunąć raportu.", Alert.AlertType.ERROR);
            }
        }
        return false;
    }

    public boolean raportExists(int reportID){
        String sql = "SELECT COUNT(*) FROM reports where id_report = ?";
        db.connection = Database.connectDB();

        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.preparedStatement.setInt(1, reportID);
            db.resultSet = db.preparedStatement.executeQuery();
            if(db.resultSet.next()){
                return db.resultSet.getInt(1) > 0;
            }

        }catch (SQLException e){e.printStackTrace();}
        return false;
    }
    @FXML
    public void editReport() {
        Report selectedReport = reportListView.getSelectionModel().getSelectedItem();
        String newContent = textareaRaport.getText();
        LocalDate reportDate = LocalDate.now();

        boolean isUpdated = updateReport(selectedReport.getId(), newContent, reportDate);
        if (isUpdated) {
            reportListView.getItems().clear();
            ShowReports();
        }
    }

    public boolean updateReport(int reportId, String newContent, LocalDate reportDate) {

        if (newContent.isEmpty() || reportDate == null){
            showAlert("Błąd", "Nie można edytować raportu. Nie wybrano raportu do edycji lub brak treści.", Alert.AlertType.ERROR);
            return false;
        }
        if (reportId <= 0) {
            showAlert("Błąd", "ID projektu nie może być ujemne!", Alert.AlertType.ERROR);
            return false;
        }
        if (!raportExists(reportId)) {
            showAlert("Błąd", "Nie znaleziono raportów o podanym ID projektu.", Alert.AlertType.ERROR);
            return false;
        }
        if (reportDate.isBefore(LocalDate.now())) {
            showAlert("Błąd", "Data dodania raportu nie moze być z przeszłości!", Alert.AlertType.ERROR);
            return false;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Potwierdzenie");
        confirmationAlert.setHeaderText("Czy na pewno chcesz edytować raport?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "UPDATE reports SET report_content = ?, report_date = ? WHERE id_report = ?";
            db.connection = Database.connectDB();

            try {
                db.preparedStatement = db.connection.prepareStatement(sql);
                db.preparedStatement.setString(1, newContent);
                db.preparedStatement.setDate(2, java.sql.Date.valueOf(reportDate));
                db.preparedStatement.setInt(3, reportId);
                db.preparedStatement.executeUpdate();

                showAlert("Sukces", "Raport został pomyślnie zaktualizowany.", Alert.AlertType.INFORMATION);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Błąd", "Nie udało się zaktualizować raportu.", Alert.AlertType.ERROR);
            }
        }
        return false;
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            addEmployeeShowData();
            ProjectShowData();
            ListtodoShowData();
            Employee_table.setOnMouseClicked(event -> {
                EmployeeSelect();
            });
            project_table.setOnMouseClicked(event -> {
                ProjectSelect();
            });
            kosztorys_table.setOnMouseClicked(event -> {
                KosztorysSelect();
            });
            projectTable.setOnMouseClicked(event -> {
                projectRaportSelect();
            });
            listtodo.setOnMouseClicked(event -> {
                ListtodoSelect();
            });
            reportListView.setOnMouseClicked(event -> {
                ReportSelect();
            });

            addstanlist();
            addstanowiskolist();
            addEtaplist();

            stawkiDom = pobierzStawkiDom();
            stawkiRemont = pobierzStawkiRemont();
            if (stawkiDom == null || stawkiRemont == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText(null);
                alert.setContentText("Nie udało się załadować stawek z bazy danych.");
                alert.showAndWait();
            }
            KosztorysClear();

            col_nazwisko.setCellValueFactory(new PropertyValueFactory<Employee, String>("nazwisko"));
            search.textProperty().addListener((observable, oldValue, newValue) -> {
                searchEmployeeByName(newValue);
            });

            col_nazwa.setCellValueFactory(new PropertyValueFactory<Project, String>("nazwa"));
            searchproject.textProperty().addListener((observable, oldValue, newValue) -> {
                searchProject(newValue);
            });
            updateLobbyLabels();
            ShowCharts();

        });
    }

}




