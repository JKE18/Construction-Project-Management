package com.example.constructionsystem;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.mindrot.jbcrypt.BCrypt;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginController {

    private Stage LoginStage;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginbtn;
    @FXML
    private Button managmentbtn;

    @FXML
    private TextField username2;
    @FXML
    private PasswordField password2;
    @FXML
    private PasswordField password2repeat;
    @FXML
    private TextField email;
    @FXML
    private Button createbtn;
    @FXML
    private Button editbtn;
    @FXML
    private Button backbtn;
    @FXML
    private Button checkerbtn;

    //obiekty do sprawdzania konta
    @FXML
    private TextField loginChecker;
    @FXML
    private PasswordField passwordChecker;

    //zmienne do sprawdzania ilosci logowania itp
    public static final int MAX_ATTEMPTS = 5; // max il. prob
    public static final int MAX_BLOCKS = 3; // Maksymalna liczba blokad przed wydłużeniem blokad
    public static final long BLOCK_TIME = 600; // blokada na 10min
    public static final long LONG_BLOCK_TIME = 3600; // Blokada na 1 godzinę po 3 blokadach
    public static int failedAttempts = 0;  // proby
    public static int blockCount = 0; // Liczba blokad
    public static Instant blockEndTime = null;  // czas do ponownej proby
    Alert alert;
    public Database db = new Database();
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    //metoda na potrzeby testów
    public static boolean isUserBlocked() {
        return failedAttempts >= MAX_ATTEMPTS && blockEndTime != null && Instant.now().isBefore(blockEndTime);
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void managementAccount() throws IOException{

        boolean accountExists = checkAccountExists();
        System.out.println(checkAccountExists());

        if (accountExists) {
            showAccountCheckerWindow();
        }else{
            LoginStage.hide();
            showAccountManager();
        }

    }

    private void showAccountManager() throws IOException{


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("accountCreate.fxml"));
        GlobalData gd = new GlobalData(600, 400);
        Scene scene = new Scene(fxmlLoader.load(), gd.XWidth, gd.YHeight);

        Stage stage = new Stage();
        stage.setMinWidth(gd.XWidth + 15);
        stage.setMaxWidth(gd.XWidth + 15);
        stage.setMinHeight(gd.YHeight + 42);
        stage.setMaxHeight(gd.YHeight + 42);
        stage.setTitle("Aplikacja do zarządzania projektem budowlanym");
        Image icon = new Image(getClass().getResourceAsStream("/images/ikona_wejsciowa.png"));
        stage.getIcons().add(icon);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.show();
        //pobieranie wymiarow ekranu i obliczanie
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(((screenBounds.getWidth() - gd.XWidth) / 2));
        stage.setY(((screenBounds.getHeight() - gd.YHeight) / 2));
    }
    private void showAccountCheckerWindow() throws IOException {
        LoginStage.hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("accountChecker.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        GlobalData gd = new GlobalData(400, 220);
        stage.setMinWidth(gd.XWidth+15);
        stage.setMaxWidth(gd.XWidth+15);
        stage.setMinHeight(gd.YHeight+42);
        stage.setMaxHeight(gd.YHeight+42);
        stage.setTitle("Aplikacja do zarządzania projektem budowlanym");
        Image icon = new Image(getClass().getResourceAsStream("/images/ikona_wejsciowa.png"));
        stage.getIcons().add(icon);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(new Scene(root));
        stage.show();
        //pobieranie wymiarow ekranu i obliczanie
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(((screenBounds.getWidth() - gd.XWidth) / 2));
        stage.setY(((screenBounds.getHeight() - gd.YHeight) / 2));

    }

    public void accountCheck() throws IOException {
        String login = loginChecker.getText();
        String password = passwordChecker.getText();

        String sql = "SELECT * FROM users WHERE login = ?";
        db.connection = Database.connectDB();
        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.preparedStatement.setString(1, login);
            db.resultSet = db.preparedStatement.executeQuery();

            if (db.resultSet.next()) {
                String hashedPassword = db.resultSet.getString("password"); // Hasło przechowywane w bazie
                // Porównujemy hasło wprowadzone przez użytkownika z hasłem w bazie
                if (BCrypt.checkpw(password, hashedPassword)) {
                    checkerbtn.getScene().getWindow().hide();

                    showAccountManager();
                } else {
                    showAlert("Błąd", "Niepoprawne hasło.", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Błąd", "Konto nie istnieje.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Błąd", "Błąd bazy danych: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean checkAccountExists() {
        String sql = "SELECT COUNT(*) FROM users";
        db.connection = Database.connectDB();
        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.resultSet = db.preparedStatement.executeQuery();
            db.resultSet.next();
            int count = db.resultSet.getInt(1);
            if(count>0){
                return true;
            }else{return false;}
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Błąd", "Błąd bazy danych: " + e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }



    @FXML
    public void backtoLog() throws IOException{
        backbtn.getScene().getWindow().hide();


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        GlobalData gd = new GlobalData(600, 400);
        Scene scene = new Scene(fxmlLoader.load(), gd.XWidth, gd.YHeight);
        Stage stage = new Stage();
        stage.setMinWidth(gd.XWidth+15);
        stage.setMaxWidth(gd.XWidth+15);
        stage.setMinHeight(gd.YHeight+42);
        stage.setMaxHeight(gd.YHeight+42);

        stage.setTitle("Aplikacja do zarządzania projektem budowlanym");
        Image icon = new Image(getClass().getResourceAsStream("/images/ikona_wejsciowa.png"));
        stage.getIcons().add(icon);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.show();
        //pobieranie wymiarow ekranu i obliczanie
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(((screenBounds.getWidth() - gd.XWidth) / 2));
        stage.setY(((screenBounds.getHeight() - gd.YHeight) / 2));
    }


    @FXML
    private void createAccount() {
        String login = username2.getText().trim();
        String emailText = email.getText().trim();
        String password = password2.getText().trim();
        String repeatPassword = password2repeat.getText().trim();

        if (login.isEmpty() || emailText.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            showAlert("Błąd", "Wszystkie pola muszą być wypełnione!", Alert.AlertType.ERROR);
            return;
        }
        // Sprawdzamy, czy email ma poprawny format
        if (!emailText.matches(EMAIL_REGEX)) {
            showAlert("Błąd", "Podaj poprawny adres e-mail!", Alert.AlertType.ERROR);
            return;
        }
        if(!isValidPassword(password)){
            showAlert("Błąd", "Hasło musi zawierać co najmniej 12 znaków, jedną wielką literę oraz cyfrę.", Alert.AlertType.ERROR);
            return;
        }
        if (!password.equals(repeatPassword)) {
            showAlert("Błąd", "Podane hasła nie są identyczne!", Alert.AlertType.ERROR);
            return;
        }


        String sqlCheck = "SELECT COUNT(*) FROM users";
        String sqlInsert = "INSERT INTO users (login, password, email) VALUES (?, ?, ?)";

        try {
            db.connection = Database.connectDB();
            db.preparedStatement = db.connection.prepareStatement(sqlCheck);
            db.resultSet = db.preparedStatement.executeQuery();

            // Warunek na istnienie konta
            if (db.resultSet.next() && db.resultSet.getInt(1) > 0) {
                showAlert("Błąd", "Konto już istnieje! Nie można dodać więcej niż jednego konta.", Alert.AlertType.ERROR);
            } else {
                String hashedPassword;

                try {
                    hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                db.preparedStatement = db.connection.prepareStatement(sqlInsert);
                db.preparedStatement.setString(1, login);
                db.preparedStatement.setString(2, hashedPassword);
                db.preparedStatement.setString(3, emailText);
                int rowsInserted = db.preparedStatement.executeUpdate();

                if (rowsInserted > 0) {
                    showAlert("Sukces", "Konto zostało pomyślnie utworzone!", Alert.AlertType.INFORMATION);
                    username2.setText("");
                    password2.setText("");
                    password2repeat.setText("");
                    email.setText("");
                } else {
                    showAlert("Błąd", "Wystąpił problem z utworzeniem konta.", Alert.AlertType.ERROR);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Błąd", "Błąd bazy danych: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void updateAccount() {
        String newLogin = username2.getText().trim();
        String newEmail = email.getText().trim();
        String newPassword = password2.getText().trim();
        String confirmPassword = password2repeat.getText().trim();

        if (newLogin.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Błąd", "Wszystkie pola muszą być wypełnione!", Alert.AlertType.ERROR);
            return;
        }

        // Sprawdzamy, czy email ma poprawny format
        if (!newEmail.matches(EMAIL_REGEX)) {
            showAlert("Błąd", "Podaj poprawny adres e-mail!", Alert.AlertType.ERROR);
            return;
        }
        if(!isValidPassword(newPassword)){
            showAlert("Błąd", "Hasło musi zawierać co najmniej 12 znaków, jedną wielką literę oraz cyfrę.", Alert.AlertType.ERROR);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Błąd", "Hasła nie są takie same!", Alert.AlertType.ERROR);
            return;
        }

        try {
            db.connection = Database.connectDB();

            String sql = "SELECT COUNT(*) FROM users";
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.resultSet = db.preparedStatement.executeQuery();
            db.resultSet.next();

            if (db.resultSet.getInt(1) == 0) {
                showAlert("Błąd", "Nie znaleziono konta do edycji!", Alert.AlertType.ERROR);
                return;
            }

            // Haszujemy nowe hasło przed zapisaniem do bazy
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));

            // Aktualizacja konta w bazie
            String updateQuery = "UPDATE users SET login = ?, email = ?, password = ? WHERE id = 1";
            db.preparedStatement = db.connection.prepareStatement(updateQuery);
            db.preparedStatement.setString(1, newLogin);
            db.preparedStatement.setString(2, newEmail);
            db.preparedStatement.setString(3, hashedPassword);

            int rowsAffected = db.preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Sukces", "Dane konta zostały zaktualizowane!", Alert.AlertType.INFORMATION);
                username2.setText("");
                password2.setText("");
                password2repeat.setText("");
                email.setText("");
            } else {
                showAlert("Błąd", "Wystąpił problem podczas aktualizacji danych!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Błąd", "Wystąpił problem z bazą danych!", Alert.AlertType.ERROR);
        }
    }

    public boolean isValidPassword(String password) {
        // warunek na duza litere, liczbe i 12 znakow
        String regex = "^(?=.*[A-Z])(?=.*\\d).{12,}$";
        return password.matches(regex);
    }

    @FXML
    public void login() {
        String sql = "SELECT * FROM users WHERE login = ?";

        db.connection = Database.connectDB();

        try {
            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                showAlert("Wystąpił błąd!", "Wpisz swój login i hasło", Alert.AlertType.ERROR);
                return;
            }
            String log = username.getText();

            // Sprawdzanie czy użytkownik jest zablokowany
            if (blockEndTime != null && Instant.now().isBefore(blockEndTime)) {
                long remainingTime = blockEndTime.getEpochSecond() - Instant.now().getEpochSecond();
                showAlert("Konto zablokowane!", "Spróbuj ponownie za " + (remainingTime / 60) + " minut.", Alert.AlertType.ERROR);
                return;
            } else if (blockEndTime != null) {
                // Resetujemy licznik blokad po zakończeniu blokady
                blockEndTime = null;
            }

            db.preparedStatement = db.connection.prepareStatement(sql);
            db.preparedStatement.setString(1, username.getText());
            db.resultSet = db.preparedStatement.executeQuery();

            if (db.resultSet.next()) {
                // Pobranie hasha z bazy
                String hashedPassword = db.resultSet.getString("password");
                String email = db.resultSet.getString("email");

                if (BCrypt.checkpw(password.getText(), hashedPassword)) {
                    String verificationCode = generateVerificationCode();

                    //reset
                    failedAttempts = 0;
                    blockCount = 0;
                    blockEndTime = null;

                    // Zapisujemy kod do bazy
                    String updateSQL = "UPDATE users SET verification_code = ? WHERE login = ?";
                    PreparedStatement updateStatement = db.connection.prepareStatement(updateSQL);
                    updateStatement.setString(1, verificationCode);
                    updateStatement.setString(2, username.getText());
                    updateStatement.executeUpdate();

                    sendVerificationEmail(email, verificationCode);

                    Stage loginStage = (Stage) username.getScene().getWindow();

                    User user = new User(username.getText());
                    SessionManager.getInstance().login(user);
                    System.out.println("Użytkownik zalogowany: " + user.getLogin());

                    openVerificationWindow(username.getText(), loginStage);


                } else {
                    handleFailedLogin();
                }
            } else {
                handleFailedLogin();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Błąd!", "Błąd w połączeniu z bazą", Alert.AlertType.ERROR);
        }
    }


    public void handleFailedLogin() {
        failedAttempts++;

        if (failedAttempts >= MAX_ATTEMPTS) {
            failedAttempts = 0;
            blockCount++;

            if (blockCount >= MAX_BLOCKS) {
                blockEndTime = Instant.now().plusSeconds(LONG_BLOCK_TIME);
                showAlert("Konto zablokowane!", "Przekroczono 3 blokady. Spróbuj ponownie za 1 godzinę.", Alert.AlertType.ERROR);
            } else {
                blockEndTime = Instant.now().plusSeconds(BLOCK_TIME);
                showAlert("Konto zablokowane!", "Przekroczono 5 prób logowania. Spróbuj ponownie za 10 minut.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Wystąpił błąd!", "Błędny login lub hasło! Pozostałe próby: " + (MAX_ATTEMPTS - failedAttempts), Alert.AlertType.ERROR);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }


    private void sendVerificationEmail(String email, String code) {
        String subject = "Kod weryfikacyjny do aplikacji budowlanej";
        String message = "Twój kod weryfikacyjny to: " + code + "\nKod jest jednorazowy.";
        try {
            MimeMessage mimeMessage = MailMessagePreparer.prepareTextMessage(email,subject, message);
            Transport.send(mimeMessage);
            showAlert("Sukces", "Kod weryfikacyjny wysłany na email!", Alert.AlertType.INFORMATION);
        } catch (MessagingException e) {
            showAlert("Błąd", "Nie udało się wysłać emaila!", Alert.AlertType.ERROR);
            throw new RuntimeException(e.getMessage());
        }
    }

    private void openVerificationWindow(String login, Stage loginStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("verifyCode.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            GlobalData gd = new GlobalData(400, 180);
            stage.setMinWidth(gd.XWidth+15);
            stage.setMaxWidth(gd.XWidth+15);
            stage.setMinHeight(gd.YHeight+42);
            stage.setMaxHeight(gd.YHeight+42);

            VerifyCodeController controller = loader.getController();
            controller.setLogin(login);
            controller.setLoginStage(loginStage);

            stage.setTitle("Aplikacja do zarządzania projektem budowlanym");
            Image icon = new Image(getClass().getResourceAsStream("/images/ikona_wejsciowa.png"));
            stage.getIcons().add(icon);
            stage.initStyle(StageStyle.DECORATED);
            stage.setScene(new Scene(root));
            stage.show();
            //pobieranie wymiarow ekranu i obliczanie
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(((screenBounds.getWidth() - gd.XWidth) / 2));
            stage.setY(((screenBounds.getHeight() - gd.YHeight) / 2));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> loginbtn.requestFocus());      //zbijam focus z textfieldow
        Platform.runLater(() -> createbtn.requestFocus());      //zbijam focus z textfieldow
        Platform.runLater(() -> LoginStage = (Stage) managmentbtn.getScene().getWindow());


    }
}
