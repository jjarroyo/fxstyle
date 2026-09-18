package com.jjarroyo.components;

import javafx.animation.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * A StackPane container that animates transitions when swapping child views.
 * Inspired by Flutter's AnimatedSwitcher and Framer Motion's AnimatePresence.
 */
public class JAnimatedSwitcher extends StackPane {

    public enum SwitchAnimation {
        FADE,
        SLIDE_LEFT,
        SLIDE_RIGHT,
        SLIDE_UP,
        SLIDE_DOWN,
        ZOOM
    }

    private final ObjectProperty<SwitchAnimation> switchAnimation = new SimpleObjectProperty<>(SwitchAnimation.FADE);
    private final ObjectProperty<Duration> duration = new SimpleObjectProperty<>(Duration.millis(300));

    private Node currentContent;

    public JAnimatedSwitcher() {
        getStyleClass().add("j-animated-switcher");
    }

    public JAnimatedSwitcher(Node initialContent) {
        this();
        setContent(initialContent);
    }

    public void setContent(Node newContent) {
        if (newContent == currentContent) {
            return;
        }

        Node oldContent = currentContent;
        currentContent = newContent;

        if (newContent == null) {
            if (oldContent != null) {
                getChildren().remove(oldContent);
            }
            return;
        }

        if (oldContent == null) {
            getChildren().add(newContent);
            return;
        }

        // Both oldContent and newContent exist: animate transition
        getChildren().add(newContent);
        animateTransition(oldContent, newContent);
    }

    public Node getContent() {
        return currentContent;
    }

    private void animateTransition(Node oldNode, Node newNode) {
        Duration dur = getDuration();
        SwitchAnimation anim = getSwitchAnimation();

        ParallelTransition parallel = new ParallelTransition();

        switch (anim) {
            case FADE: {
                FadeTransition fadeOut = new FadeTransition(dur, oldNode);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);

                FadeTransition fadeIn = new FadeTransition(dur, newNode);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);

                parallel.getChildren().addAll(fadeOut, fadeIn);
                break;
            }

            case SLIDE_LEFT: {
                double width = getWidth() > 0 ? getWidth() : 300;

                TranslateTransition slideOut = new TranslateTransition(dur, oldNode);
                slideOut.setFromX(0);
                slideOut.setToX(-width);

                newNode.setTranslateX(width);
                TranslateTransition slideIn = new TranslateTransition(dur, newNode);
                slideIn.setFromX(width);
                slideIn.setToX(0);

                parallel.getChildren().addAll(slideOut, slideIn);
                break;
            }

            case SLIDE_RIGHT: {
                double width = getWidth() > 0 ? getWidth() : 300;

                TranslateTransition slideOut = new TranslateTransition(dur, oldNode);
                slideOut.setFromX(0);
                slideOut.setToX(width);

                newNode.setTranslateX(-width);
                TranslateTransition slideIn = new TranslateTransition(dur, newNode);
                slideIn.setFromX(-width);
                slideIn.setToX(0);

                parallel.getChildren().addAll(slideOut, slideIn);
                break;
            }

            case SLIDE_UP: {
                double height = getHeight() > 0 ? getHeight() : 200;

                TranslateTransition slideOut = new TranslateTransition(dur, oldNode);
                slideOut.setFromY(0);
                slideOut.setToY(-height);

                newNode.setTranslateY(height);
                TranslateTransition slideIn = new TranslateTransition(dur, newNode);
                slideIn.setFromY(height);
                slideIn.setToY(0);

                parallel.getChildren().addAll(slideOut, slideIn);
                break;
            }

            case SLIDE_DOWN: {
                double height = getHeight() > 0 ? getHeight() : 200;

                TranslateTransition slideOut = new TranslateTransition(dur, oldNode);
                slideOut.setFromY(0);
                slideOut.setToY(height);

                newNode.setTranslateY(-height);
                TranslateTransition slideIn = new TranslateTransition(dur, newNode);
                slideIn.setFromY(-height);
                slideIn.setToY(0);

                parallel.getChildren().addAll(slideOut, slideIn);
                break;
            }

            case ZOOM: {
                ScaleTransition scaleOut = new ScaleTransition(dur, oldNode);
                scaleOut.setToX(0.8);
                scaleOut.setToY(0.8);
                FadeTransition fadeOut = new FadeTransition(dur, oldNode);
                fadeOut.setToValue(0.0);

                newNode.setScaleX(1.2);
                newNode.setScaleY(1.2);
                newNode.setOpacity(0.0);
                ScaleTransition scaleIn = new ScaleTransition(dur, newNode);
                scaleIn.setToX(1.0);
                scaleIn.setToY(1.0);
                FadeTransition fadeIn = new FadeTransition(dur, newNode);
                fadeIn.setToValue(1.0);

                parallel.getChildren().addAll(scaleOut, fadeOut, scaleIn, fadeIn);
                break;
            }
        }

        parallel.setInterpolator(Interpolator.EASE_BOTH);
        parallel.setOnFinished(e -> {
            getChildren().remove(oldNode);
            // Reset transforms on oldNode
            oldNode.setTranslateX(0);
            oldNode.setTranslateY(0);
            oldNode.setScaleX(1.0);
            oldNode.setScaleY(1.0);
            oldNode.setOpacity(1.0);

            // Reset transforms on newNode
            newNode.setTranslateX(0);
            newNode.setTranslateY(0);
            newNode.setScaleX(1.0);
            newNode.setScaleY(1.0);
            newNode.setOpacity(1.0);
        });

        parallel.play();
    }

    // ── GETTERS & SETTERS ────────────────────────────────────────────────

    public SwitchAnimation getSwitchAnimation() {
        return switchAnimation.get();
    }

    public void setSwitchAnimation(SwitchAnimation val) {
        switchAnimation.set(val != null ? val : SwitchAnimation.FADE);
    }

    public ObjectProperty<SwitchAnimation> switchAnimationProperty() {
        return switchAnimation;
    }

    public Duration getDuration() {
        return duration.get();
    }

    public void setDuration(Duration val) {
        duration.set(val != null ? val : Duration.millis(300));
    }

    public ObjectProperty<Duration> durationProperty() {
        return duration;
    }
}
