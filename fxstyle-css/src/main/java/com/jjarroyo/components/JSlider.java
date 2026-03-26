package com.jjarroyo.components;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Slider;

public class JSlider extends Slider {

    public JSlider() {
        super();
        init();
    }

    public JSlider(double min, double max, double value) {
        super(min, max, value);
        init();
    }

    private void init() {
        getStyleClass().add("j-slider");
        
        // Listener to update the filled track visualization
        valueProperty().addListener((obs, oldVal, newVal) -> updateTrackFill());
        
        // Wait for skin/scene to be ready to perform initial lookups
        skinProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Platform.runLater(this::updateTrackFill);
            }
        });
    }

    private void updateTrackFill() {
        Node track = lookup(".track");
        if (track != null) {
            double percentage = (getValue() - getMin()) / (getMax() - getMin()) * 100;
            // Clamp percentage between 0 and 100
            percentage = Math.max(0, Math.min(100, percentage));
            
            String style = String.format(
                "-fx-background-color: linear-gradient(to right, -color-primary-500 %.2f%%, -color-slate-200 %.2f%%);",
                percentage, percentage
            );
            track.setStyle(style);
        }
    }
}
