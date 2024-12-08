package com.cgvsu.rasterization;

import javafx.application.Application;
import javafx.stage.Stage;

public class TestApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Ничего не делаем, это только для инициализации JavaFX
    }

    public static void initToolkit() {
        new Thread(() -> Application.launch(TestApp.class)).start();
        try {
            Thread.sleep(1000); // Даем JavaFX время для инициализации
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
