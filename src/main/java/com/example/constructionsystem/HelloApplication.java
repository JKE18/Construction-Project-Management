package com.example.constructionsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        GlobalData gd = new GlobalData(600, 400);
        Scene scene = new Scene(fxmlLoader.load(), gd.XWidth, gd.YHeight);
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
    public static void main(String[] args) {
        launch();
    }
}