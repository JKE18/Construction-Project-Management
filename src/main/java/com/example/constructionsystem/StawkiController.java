package com.example.constructionsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class StawkiController {

    @FXML
    private TextField textfield_tynkowanie;

    @FXML
    private TextField textfield_malowanie;

    @FXML
    private TextField textfield_szpachlowanie;

    @FXML
    private TextField textfield_murowanie;

    @FXML
    private TextField textfield_montazGK;

    @FXML
    private TextField textfield_panele;

    @FXML
    private TextField textfield_instalacja;

    @FXML
    private TextField textfield_bialymontaz;

    @FXML
    private TextField textfield_montazdrzwi;

    @FXML
    private TextField textfield_oknaremont;

    @FXML
    private TextField textfield_plytki;

    @FXML
    private TextField textfield_kosztyremont;

    @FXML
    private TextField textfield_fundament;

    @FXML
    private TextField textfield_hydroizolacja;

    @FXML
    private TextField textfield_murowanienosne;

    @FXML
    private TextField textfield_murowaniedzialowe;

    @FXML
    private TextField textfield_zbrojenie;

    @FXML
    private TextField textfield_strop;

    @FXML
    private TextField textfield_wiezba;

    @FXML
    private TextField textfield_pokrycie;

    @FXML
    private TextField textfield_kominy;

    @FXML
    private TextField textfield_oknadom;

    @FXML
    private TextField textfield_elewacja;

    @FXML
    private TextField textfield_kosztydom;

    private Database db = new Database();
    private Alert alert;

    @FXML
    private void zapiszStawkiDom() {
        String sqlDom = "UPDATE stawkidom SET fundamenty = ?, hydroizolacja_fundamentow = ?, murowanie_nosne = ?, " +
                "murowanie_dzialowe = ?, zbrojenie = ?, strop = ?, wiezba = ?, pokrycie = ?, kominy = ?, montaz_okien = ?, " +
                "elewacja = ?, pozostale_koszty = ? WHERE id_stawkiDom = 1";

        db.connection = Database.connectDB();

        try {
            // Aktualizacja tabeli stawkidom
            db.preparedStatement = db.connection.prepareStatement(sqlDom);
            db.preparedStatement.setDouble(1, Double.parseDouble(textfield_fundament.getText()));
            db.preparedStatement.setDouble(2, Double.parseDouble(textfield_hydroizolacja.getText()));
            db.preparedStatement.setDouble(3, Double.parseDouble(textfield_murowanienosne.getText()));
            db.preparedStatement.setDouble(4, Double.parseDouble(textfield_murowaniedzialowe.getText()));
            db.preparedStatement.setDouble(5, Double.parseDouble(textfield_zbrojenie.getText()));
            db.preparedStatement.setDouble(6, Double.parseDouble(textfield_strop.getText()));
            db.preparedStatement.setDouble(7, Double.parseDouble(textfield_wiezba.getText()));
            db.preparedStatement.setDouble(8, Double.parseDouble(textfield_pokrycie.getText()));
            db.preparedStatement.setDouble(9, Double.parseDouble(textfield_kominy.getText()));
            db.preparedStatement.setDouble(10, Double.parseDouble(textfield_oknadom.getText()));
            db.preparedStatement.setDouble(11, Double.parseDouble(textfield_elewacja.getText()));
            db.preparedStatement.setDouble(12, Double.parseDouble(textfield_kosztydom.getText()));
            db.preparedStatement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Stawki zostały pomyślnie zaktualizowane.");
            alert.showAndWait();


        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wprowadź poprawne wartości numeryczne dla stawek.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wystąpił problem podczas zapisu stawek do bazy.");
            alert.showAndWait();
        }
    }

    @FXML
    private void zapiszStawkiRemont() {

        String sqlRemont = "UPDATE stawkiremont SET tynkowanie = ?, malowanie = ?, szpachlowanie = ?, murowanie = ?, " +
                "montazGK = ?, ukladanie_paneli = ?, instalacja = ?, bialy_montaz = ?, montaz_drzwi = ?, montaz_okna = ?, " +
                "ukladanie_plytek = ?, pozostale_koszta = ? WHERE id_stawkiRemont = 1";

        db.connection = Database.connectDB();

        try {
            db.preparedStatement = db.connection.prepareStatement(sqlRemont);
            db.preparedStatement.setDouble(1, Double.parseDouble(textfield_tynkowanie.getText()));
            db.preparedStatement.setDouble(2, Double.parseDouble(textfield_malowanie.getText()));
            db.preparedStatement.setDouble(3, Double.parseDouble(textfield_szpachlowanie.getText()));
            db.preparedStatement.setDouble(4, Double.parseDouble(textfield_murowanie.getText()));
            db.preparedStatement.setDouble(5, Double.parseDouble(textfield_montazGK.getText()));
            db.preparedStatement.setDouble(6, Double.parseDouble(textfield_panele.getText()));
            db.preparedStatement.setDouble(7, Double.parseDouble(textfield_instalacja.getText()));
            db.preparedStatement.setDouble(8, Double.parseDouble(textfield_bialymontaz.getText()));
            db.preparedStatement.setDouble(9, Double.parseDouble(textfield_montazdrzwi.getText()));
            db.preparedStatement.setDouble(10, Double.parseDouble(textfield_oknaremont.getText()));
            db.preparedStatement.setDouble(11, Double.parseDouble(textfield_plytki.getText()));
            db.preparedStatement.setDouble(12, Double.parseDouble(textfield_kosztyremont.getText()));
            db.preparedStatement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Stawki zostały pomyślnie zaktualizowane.");
            alert.showAndWait();


        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wprowadź poprawne wartości numeryczne dla stawek.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wystąpił problem podczas zapisu stawek do bazy. " + e.getMessage());
            alert.showAndWait();
        }
    }
    private StawkiDom stawkiDom;
    private StawkiRemont stawkiRemont;
    public StawkiRemont pobierzStawkiR() {
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
    public StawkiDom pobierzStawkiD() {
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

    private void setStawkiTextfield() {

        if (stawkiDom != null) {
            textfield_fundament.setText(String.valueOf(stawkiDom.getZalewanieFundamentow()));
            textfield_hydroizolacja.setText(String.valueOf(stawkiDom.getHydroizolacjaFundamentow()));
            textfield_murowanienosne.setText(String.valueOf(stawkiDom.getMurowanieScianNosnych()));
            textfield_murowaniedzialowe.setText(String.valueOf(stawkiDom.getMurowanieScianDzialowych()));
            textfield_zbrojenie.setText(String.valueOf(stawkiDom.getZbrojenieStalowe()));
            textfield_strop.setText(String.valueOf(stawkiDom.getStropISchody()));
            textfield_wiezba.setText(String.valueOf(stawkiDom.getMontazWiezbyDachowej()));
            textfield_pokrycie.setText(String.valueOf(stawkiDom.getPokrycieDachowe()));
            textfield_kominy.setText(String.valueOf(stawkiDom.getKominyIWentylacja()));
            textfield_oknadom.setText(String.valueOf(stawkiDom.getMontazOkienDachowych()));
            textfield_elewacja.setText(String.valueOf(stawkiDom.getElewacja()));
            textfield_kosztydom.setText(String.valueOf(stawkiDom.getPozostaleKosztyDodatkowe()));
        }

        if (stawkiRemont != null) {
            textfield_tynkowanie.setText(String.valueOf(stawkiRemont.getTynkowanieScian()));
            textfield_malowanie.setText(String.valueOf(stawkiRemont.getMalowanieITapetowanie()));
            textfield_szpachlowanie.setText(String.valueOf(stawkiRemont.getSzpachlowanieScian()));
            textfield_murowanie.setText(String.valueOf(stawkiRemont.getMurowanieScian()));
            textfield_montazGK.setText(String.valueOf(stawkiRemont.getMontazPlytGK()));
            textfield_panele.setText(String.valueOf(stawkiRemont.getUkladaniePaneliIParkietu()));
            textfield_instalacja.setText(String.valueOf(stawkiRemont.getPraceInstalacyjne()));
            textfield_bialymontaz.setText(String.valueOf(stawkiRemont.getBialyMontaz()));
            textfield_montazdrzwi.setText(String.valueOf(stawkiRemont.getMontazDrzwiWewnetrznych()));
            textfield_oknaremont.setText(String.valueOf(stawkiRemont.getMontazOkien()));
            textfield_plytki.setText(String.valueOf(stawkiRemont.getUkladaniePlytek()));
            textfield_kosztyremont.setText(String.valueOf(stawkiRemont.getPozostaleKosztyDodatkowe()));
        }
    }


    @FXML
    public void initialize(){
        stawkiDom = pobierzStawkiD();
        stawkiRemont = pobierzStawkiR();
        setStawkiTextfield();
    }

}
