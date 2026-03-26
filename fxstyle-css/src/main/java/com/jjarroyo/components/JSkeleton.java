package com.jjarroyo.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * JSkeleton — Placeholders animados de carga (shimmer/pulse).
 *
 * <pre>
 * JSkeleton.text(3)       // 3 líneas de texto
 * JSkeleton.card()        // card skeleton
 * JSkeleton.avatar()      // circular avatar
 * JSkeleton.rect(200, 40) // rectángulo custom
 * </pre>
 */
public class JSkeleton extends Region {

    private final Timeline animation;

    private JSkeleton(double width, double height) {
        getStyleClass().add("j-skeleton");
        setPrefSize(width, height);
        setMinSize(width, height);
        setMaxSize(width, height);
        setOpacity(0.6);

        // Pulse animation
        animation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(opacityProperty(), 0.6)),
            new KeyFrame(Duration.millis(800), new KeyValue(opacityProperty(), 0.3)),
            new KeyFrame(Duration.millis(1600), new KeyValue(opacityProperty(), 0.6))
        );
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();

        // Stop when removed from scene
        sceneProperty().addListener((obs, old, scene) -> {
            if (scene == null) animation.stop();
            else animation.play();
        });
    }

    /** Stops the animation */
    public void stop() {
        animation.stop();
        setOpacity(0);
    }

    // ─── Factory Methods ─────────────────────────────────────────────────────────

    /** Single line skeleton */
    public static JSkeleton line(double width) {
        JSkeleton s = new JSkeleton(width, 14);
        s.getStyleClass().add("j-skeleton-line");
        return s;
    }

    /** Rectangle skeleton */
    public static Region rect(double width, double height) {
        JSkeleton s = new JSkeleton(width, height);
        s.getStyleClass().add("j-skeleton-rect");
        return s;
    }

    /** Circular avatar skeleton */
    public static Region avatar(double size) {
        JSkeleton s = new JSkeleton(size, size);
        s.getStyleClass().add("j-skeleton-avatar");
        return s;
    }

    /** Default avatar (40px) */
    public static Region avatar() {
        return avatar(40);
    }

    /** Multiple text lines */
    public static Region text(int lines) {
        VBox box = new VBox(8);
        for (int i = 0; i < lines; i++) {
            double width = (i == lines - 1) ? 160 : 280; // last line shorter
            box.getChildren().add(line(width));
        }
        return box;
    }

    /** Card skeleton (image + lines) */
    public static Region card() {
        VBox card = new VBox(12);
        card.getStyleClass().add("j-skeleton-card");
        card.setPrefWidth(300);

        // Image placeholder
        card.getChildren().add(rect(300, 140));

        // Title
        card.getChildren().add(line(220));

        // Text lines
        card.getChildren().add(line(280));
        card.getChildren().add(line(200));

        return card;
    }

    /** Row with avatar + text lines */
    public static Region userRow() {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox textBox = new VBox(8);
        textBox.getChildren().addAll(line(160), line(120));

        row.getChildren().addAll(avatar(40), textBox);
        return row;
    }

    /** Table skeleton */
    public static Region table(int rows, int cols) {
        VBox table = new VBox(8);
        for (int r = 0; r < rows; r++) {
            HBox row = new HBox(12);
            for (int c = 0; c < cols; c++) {
                double w = (c == 0) ? 120 : 80;
                row.getChildren().add(line(w));
            }
            table.getChildren().add(row);
        }
        return table;
    }
}
