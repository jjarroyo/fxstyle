package com.jjarroyo.components;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * A continuous scrolling banner/ticker control (Marquee effect).
 * Supports text scrolling, customizable speed, and pause-on-hover.
 */
public class JMarquee extends StackPane {

    private final StringProperty text = new SimpleStringProperty("");
    private final DoubleProperty speed = new SimpleDoubleProperty(60.0); // pixels per second
    private final BooleanProperty pauseOnHover = new SimpleBooleanProperty(true);

    private final Label scrollLabel = new Label();
    private final Rectangle clipRect = new Rectangle();
    private TranslateTransition transition;

    public JMarquee() {
        this("");
    }

    public JMarquee(String text) {
        getStyleClass().add("j-marquee");
        setClip(clipRect);

        scrollLabel.getStyleClass().add("marquee-label");
        getChildren().add(scrollLabel);

        layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            clipRect.setWidth(newVal.getWidth());
            clipRect.setHeight(newVal.getHeight());
            restartAnimation();
        });

        scrollLabel.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> restartAnimation());

        this.text.addListener((obs, oldVal, newVal) -> {
            scrollLabel.setText(newVal != null ? newVal : "");
            restartAnimation();
        });

        speed.addListener((obs, oldVal, newVal) -> restartAnimation());

        setOnMouseEntered(e -> {
            if (getPauseOnHover() && transition != null) {
                transition.pause();
            }
        });

        setOnMouseExited(e -> {
            if (getPauseOnHover() && transition != null && transition.getStatus() == Animation.Status.PAUSED) {
                transition.play();
            }
        });

        setText(text);
    }

    public void restartAnimation() {
        if (transition != null) {
            transition.stop();
        }

        double containerWidth = getWidth();
        double labelWidth = scrollLabel.getLayoutBounds().getWidth();

        if (containerWidth <= 0 || labelWidth <= 0 || getSpeed() <= 0) {
            return;
        }

        double startX = containerWidth;
        double endX = -labelWidth;
        double totalDistance = startX - endX;
        double durationSeconds = totalDistance / getSpeed();

        scrollLabel.setTranslateX(startX);

        transition = new TranslateTransition(Duration.seconds(durationSeconds), scrollLabel);
        transition.setFromX(startX);
        transition.setToX(endX);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.play();
    }

    // ── GETTERS & SETTERS ────────────────────────────────────────────────

    public String getText() {
        return text.get();
    }

    public void setText(String val) {
        text.set(val != null ? val : "");
    }

    public StringProperty textProperty() {
        return text;
    }

    public double getSpeed() {
        return speed.get();
    }

    public void setSpeed(double val) {
        speed.set(Math.max(10.0, val));
    }

    public DoubleProperty speedProperty() {
        return speed;
    }

    public boolean getPauseOnHover() {
        return pauseOnHover.get();
    }

    public void setPauseOnHover(boolean val) {
        pauseOnHover.set(val);
    }

    public BooleanProperty pauseOnHoverProperty() {
        return pauseOnHover;
    }

    public Label getLabel() {
        return scrollLabel;
    }
}
