package com.example.constructionsystem;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.time.Instant;

public class SessionManager {
    private User loggedInUser;
    public Instant lastActivity;
    private Timer sessionTimer;
    private Stage lobbyStage;
    private static SessionManager instance;
    private int inactivityCounter = 0;
    private static final long TIMEOUT_DURATION = 900;
    private static final long WARNING_TIME = 840; //ostrzezenie minute przed
    private boolean activeSession;

    Alert alert;

    private SessionManager() {
        resetActivity();
        startSessionTimer();
        activeSession = false;
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    public void startSession() {
        activeSession = true;
    }

    public void login(User user) {
        this.loggedInUser = user;
        resetActivity();
    }

    public void setStage(Stage stage) {
        this.lobbyStage = stage;
    }

    public void logout() {
        Platform.runLater(() -> {
            System.out.println("Sesja wygasła. Wylogowywanie użytkownika...");

            if (lobbyStage != null) {
                lobbyStage.close(); // Zamknięcie głównego okna
            } else {
                System.out.println("Błąd: lobbyStage jest null!");
            }
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informacja zwrotna");
            alert.setHeaderText(null);
            alert.setContentText("Wylogowano z konta z powodu bezczynności!");
            alert.showAndWait();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Parent root = loader.load();
                GlobalData gd = new GlobalData(600, 400);
                Scene scene = new Scene(root, gd.XWidth, gd.YHeight);
                Stage stage = new Stage();
                stage.setMinWidth(gd.XWidth + 15);
                stage.setMaxWidth(gd.XWidth + 15);
                stage.setMinHeight(gd.YHeight + 42);
                stage.setMaxHeight(gd.YHeight + 42);
                Image icon = new Image(getClass().getResourceAsStream("/images/ikona_wejsciowa.png"));
                stage.getIcons().add(icon);
                stage.setTitle("Aplikacja do zarządzania projektem budowlanym");
                stage.setScene(scene);
                stage.show();
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX(((screenBounds.getWidth() - gd.XWidth) / 2));
                stage.setY(((screenBounds.getHeight() - gd.YHeight) / 2));

                SessionManager.getInstance().setStage(stage); // Aktualizacja stage

                activeSession = false;
                loggedInUser = null;
                stopSessionTimer();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
    }

    public void resetActivity() {
        lastActivity = Instant.now();
        inactivityCounter = 0;
    }

    public boolean isActive() {
        return activeSession;
    }

    private void startSessionTimer() {
        sessionTimer = new Timer(true);
        sessionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (loggedInUser != null) {
                    long elapsedTime = Instant.now().getEpochSecond() - lastActivity.getEpochSecond();

                    if (elapsedTime > inactivityCounter) {
                        inactivityCounter = (int) elapsedTime;
                        System.out.println("Nieaktywny przez: " + inactivityCounter + " sekund");
                    }

                    // ostrzezenie minute przed
                    if (elapsedTime == WARNING_TIME) {
                        showWarning();
                    }

                    // logout
                    if (elapsedTime >= TIMEOUT_DURATION) {
                        logout();
                    }
                }
            }
        }, 1000, 1000); // Sprawdzanie co sekundę
    }
    private void showWarning() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ostrzeżenie");
            alert.setHeaderText("Twoja sesja wygaśnie za 1 minutę!");
            alert.setContentText("Jeśli chcesz kontynuować, wykonaj dowolną akcję.");
            alert.show();
        });
    }


    private void stopSessionTimer() {
        if (sessionTimer != null) {
            sessionTimer.cancel();
            System.out.println("Licznik sesji zatrzymany.");
        }
    }
}
