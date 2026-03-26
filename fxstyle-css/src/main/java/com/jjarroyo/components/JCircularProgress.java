package com.jjarroyo.components;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;

/**
 * A circular progress indicator.
 */
public class JCircularProgress extends StackPane {

    private final Circle track;
    private final Arc bar;
    private final DoubleProperty progress = new SimpleDoubleProperty(0); // 0.0 to 1.0

    /**
     * Creates a circular progress with default radius 70 and stroke width 20.
     */
    public JCircularProgress() {
        this(70, 20);
    }

    /**
     * Creates a circular progress with the specified radius and stroke width.
     */
    public JCircularProgress(double radius, double strokeWidth) {
        getStyleClass().add("circular-progress");

        track = new Circle(radius);
        track.getStyleClass().add("circular-progress-track");
        track.setFill(Color.TRANSPARENT);
        track.setStrokeWidth(strokeWidth);

        bar = new Arc();
        bar.getStyleClass().add("circular-progress-bar");
        bar.setRadiusX(radius);
        bar.setRadiusY(radius);
        bar.setStartAngle(90);
        bar.setLength(0);
        bar.setType(ArcType.OPEN);
        bar.setFill(Color.TRANSPARENT);
        bar.setStrokeWidth(strokeWidth);
        bar.setStrokeLineCap(StrokeLineCap.ROUND);

        // Placing shapes in a Group ensures their relative positioning (origin) is maintained,
        // avoiding StackPane centering bounding boxes of partial arcs.
        Group graphicGroup = new Group(track, bar);
        
        getChildren().add(graphicGroup);

        progress.addListener((obs, oldVal, newVal) -> {
            updateProgress(newVal.doubleValue());
        });
        
        // Ensure the component requests enough size based on its shapes
        double size = (radius * 2) + strokeWidth;
        setMinSize(size, size);
        setPrefSize(size, size);
        setMaxSize(size, size);
    }

    /**
     * Sets the progress value.
     * @param value A value between 0.0 and 1.0
     */
    public void setProgress(double value) {
        this.progress.set(Math.max(0, Math.min(1.0, value)));
    }

    public double getProgress() {
        return progress.get();
    }
    
    public DoubleProperty progressProperty() {
        return progress;
    }
    
    /**
     * Overrides the bar color via inline CSS.
     */
    public void setColor(String webColor) {
        bar.setStyle("-fx-stroke: " + webColor + ";");
    }
    
    /**
     * Overrides the track color via inline CSS.
     */
    public void setTrackColor(String webColor) {
        track.setStyle("-fx-stroke: " + webColor + ";");
    }

    private void updateProgress(double value) {
        // For clock-wise progress, we sweep negatively from 90 degrees.
        double length = -360 * value;
        bar.setLength(length);
    }
}
