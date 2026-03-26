package com.jjarroyo.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.text.DecimalFormat;

public class JStatCard extends VBox {

    private final Label titleLabel;
    private final Label valueLabel;
    private final Label trendLabel;
    private final StackPane iconPane;
    
    private final DoubleProperty currentValue = new SimpleDoubleProperty(0.0);
    private Timeline timeline;
    
    private String prefix = "";
    private String suffix = "";
    private int decimalPlaces = 0;
    private DecimalFormat formatter = new DecimalFormat("#,##0");
    
    public JStatCard() {
        getStyleClass().add("j-stat-card");
        setPadding(new Insets(20));
        setSpacing(16);
        
        // --- Header (Title + Icon) ---
        titleLabel = new Label("Title");
        titleLabel.getStyleClass().add("j-stat-title");
        
        iconPane = new StackPane();
        iconPane.getStyleClass().add("j-stat-icon-pane");
        iconPane.setVisible(false);
        iconPane.setManaged(false); // Hide by default until an icon is set
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        HBox headerBox = new HBox(8);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.getChildren().addAll(titleLabel, spacer, iconPane);
        
        // --- Value Block ---
        valueLabel = new Label("0");
        valueLabel.getStyleClass().add("j-stat-value");
        
        // Listener to format the double property to string on every tick
        currentValue.addListener((obs, oldVal, newVal) -> {
            valueLabel.setText(prefix + formatter.format(newVal.doubleValue()) + suffix);
        });

        // --- Footer (Trend) ---
        trendLabel = new Label();
        trendLabel.getStyleClass().add("j-stat-trend");
        trendLabel.setVisible(false);
        trendLabel.setManaged(false);
        
        getChildren().addAll(headerBox, valueLabel, trendLabel);
    }
    
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    
    public void setIcon(Node icon, String colorClass) {
        if (icon != null) {
            iconPane.getChildren().setAll(icon);
            iconPane.setVisible(true);
            iconPane.setManaged(true);
            
            // Clean previous colors
            iconPane.getStyleClass().removeAll("primary", "success", "danger", "warning", "info", "slate");
            if (colorClass != null && !colorClass.isEmpty()) {
                iconPane.getStyleClass().add(colorClass);
            }
        } else {
            iconPane.getChildren().clear();
            iconPane.setVisible(false);
            iconPane.setManaged(false);
        }
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix != null ? prefix : "";
        updateText();
    }
    
    public void setSuffix(String suffix) {
        this.suffix = suffix != null ? suffix : "";
        updateText();
    }
    
    public void setDecimalPlaces(int places) {
        this.decimalPlaces = places;
        if (places > 0) {
            StringBuilder pattern = new StringBuilder("#,##0.");
            for (int i = 0; i < places; i++) {
                pattern.append("0");
            }
            formatter = new DecimalFormat(pattern.toString());
        } else {
            formatter = new DecimalFormat("#,##0");
        }
        updateText();
    }
    
    public void setValue(double targetValue) {
        animateValue(targetValue, 1000); // Default 1s animation
    }
    
    public void setValue(double targetValue, int durationMs) {
        animateValue(targetValue, durationMs);
    }
    
    private void animateValue(double targetValue, int durationMs) {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            timeline.stop();
        }
        
        timeline = new Timeline(
            new KeyFrame(
                Duration.ZERO, 
                new KeyValue(currentValue, currentValue.get())
            ),
            new KeyFrame(
                Duration.millis(durationMs), 
                new KeyValue(currentValue, targetValue)
            )
        );
        timeline.play();
    }
    
    public void setTrend(String text, boolean isPositive) {
        if (text != null && !text.isEmpty()) {
            trendLabel.setText(text);
            trendLabel.setVisible(true);
            trendLabel.setManaged(true);
            
            trendLabel.getStyleClass().removeAll("positive", "negative");
            if (isPositive) {
                trendLabel.getStyleClass().add("positive");
            } else {
                trendLabel.getStyleClass().add("negative");
            }
        } else {
            trendLabel.setText("");
            trendLabel.setVisible(false);
            trendLabel.setManaged(false);
        }
    }
    
    private void updateText() {
        // Force an update using current value
        valueLabel.setText(prefix + formatter.format(currentValue.get()) + suffix);
    }
}
