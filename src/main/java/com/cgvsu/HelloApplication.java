package com.cgvsu;

import com.cgvsu.math.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AnchorPane viewport = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/hello-view.fxml")));

        Scene scene = new Scene(viewport);
        stage.setMinWidth(1500);
        stage.setMinHeight(800);
        viewport.prefWidthProperty().bind(scene.widthProperty());
        viewport.prefHeightProperty().bind(scene.heightProperty());

        stage.setTitle("Компьютерная графика - финальный проект");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}