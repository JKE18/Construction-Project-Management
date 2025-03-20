package com.example.constructionsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;

public class VerifyCodeController {

    @FXML
    private TextField codeField;
    private String login;

    private Stage loginStage;
    Database db = new Database();
    Alert alert;

    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //test
    @FXML
    public void verifyCode() {
        String sql = "SELECT verification_code FROM users WHERE login = ?";
        db.connection = Database.connectDB();

        try {
            db.preparedStatement = db.connection.prepareStatement(sql);
            db.preparedStatement.setString(1, login);
            db.resultSet = db.preparedStatement.executeQuery();
            if (db.resultSet.next()) {
                String correctCode = db.resultSet.getString("verification_code");
                System.out.println("zczytany kod z bazy");
                if (codeField.getText().equals(correctCode)) {
                    showAlert("Sukces!", "Kod poprawny! Logowanie zakończone.", Alert.AlertType.INFORMATION);


                    openMainApplication();
                    ((Stage) codeField.getScene().getWindow()).close();  // Zamykamy okno kodu
                    if (loginStage != null) {
                        loginStage.close();
                    }

                    // Zerowanie kodu weryfikacyjnego w bazie
                    String sql2 = "UPDATE users SET verification_code = NULL WHERE login = ?";
                    try {
                        db.connection = Database.connectDB();
                        db.preparedStatement = db.connection.prepareStatement(sql2);
                        db.preparedStatement.setString(1, login);
                        db.preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else {
                    showAlert("Błąd!", "Niepoprawny kod!", Alert.AlertType.ERROR);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void openMainApplication() {
        try {
            GlobalData gd= new GlobalData(1200,600);
            Parent root2 = FXMLLoader.load(getClass().getResource("lobby.fxml"));
            Scene scene2 = new Scene(root2, gd.XWidth, gd.YHeight);
            Stage stage2 = new Stage();

            SessionManager.getInstance().setStage(stage2);

            //nasluchiwanie myszki i klawiatury
            scene2.addEventFilter(MouseEvent.ANY, event -> SessionManager.getInstance().resetActivity());
            scene2.addEventFilter(KeyEvent.ANY, event -> SessionManager.getInstance().resetActivity());

            stage2.setMinWidth(1231);
            stage2.setMaxWidth(1231);
            stage2.setMinHeight(631);
            stage2.setMaxHeight(631);
            Image icon = new Image(getClass().getResourceAsStream("/images/ikona_wejsciowa.png"));
            stage2.getIcons().add(icon);
            stage2.setTitle("Aplikacja do zarządzania projektem budowlanym");
            stage2.initStyle(StageStyle.DECORATED);
            stage2.setScene(scene2);
            stage2.show();
            //pobieranie wymiarow ekranu i obliczanie
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage2.setX(((screenBounds.getWidth() - gd.XWidth) / 2));
            stage2.setY(((screenBounds.getHeight() - gd.YHeight) / 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        codeField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                verifyCode();
            }
        });
    }

}

