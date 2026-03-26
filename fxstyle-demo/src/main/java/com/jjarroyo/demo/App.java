package com.jjarroyo.demo;

import com.jjarroyo.demo.layout.MainLayout;
import com.jjarroyo.FxStyle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        MainLayout mainLayout = new MainLayout();
        
        Scene scene = new Scene(mainLayout, 1280, 800);
        
        // Load CSS
        FxStyle.init(scene);
        stage.setTitle("FxStyle - JavaFX Design System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


