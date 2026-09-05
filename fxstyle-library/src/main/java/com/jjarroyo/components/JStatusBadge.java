package com.jjarroyo.components;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 * JStatusBadge — Insignia de estado con indicador de punto brillante (*status dot*),
 * animación de pulso opcional para estados en vivo (online / live status) y estilos contextuales Metronic/Tailwind.
 */
public class JStatusBadge extends HBox {

    public enum Status {
        SUCCESS("status-badge-success"),
        ONLINE("status-badge-success"),
        DANGER("status-badge-danger"),
        OFFLINE("status-badge-danger"),
        WARNING("status-badge-warning"),
        AWAY("status-badge-warning"),
        INFO("status-badge-info"),
        PROCESSING("status-badge-info"),
        MUTED("status-badge-muted"),
        IDLE("status-badge-muted"),
        PURPLE("status-badge-purple");

        private final String styleClass;

        Status(String styleClass) {
            this.styleClass = styleClass;
        }

        public String getStyleClass() {
            return styleClass;
        }
    }

    private final Region dot;
    private final Label label;
    private Status status = Status.SUCCESS;
    private Animation pulseAnimation;

    public JStatusBadge() {
        this("Active", Status.SUCCESS, false);
    }

    public JStatusBadge(String text) {
        this(text, Status.SUCCESS, false);
    }

    public JStatusBadge(String text, Status status) {
        this(text, status, false);
    }

    public JStatusBadge(String text, Status status, boolean pulse) {
        dot = new Region();
        dot.getStyleClass().add("j-status-badge-dot");
        dot.setMinSize(8, 8);
        dot.setPrefSize(8, 8);
        dot.setMaxSize(8, 8);
        dot.setShape(new javafx.scene.shape.Circle(4));

        label = new Label(text);
        label.getStyleClass().add("j-status-badge-text");

        getStyleClass().add("j-status-badge");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(6);
        getChildren().addAll(dot, label);

        setStatus(status);
        setPulse(pulse);
    }

    public JStatusBadge setStatus(Status status) {
        if (status == null) status = Status.SUCCESS;
        if (this.status != null) {
            getStyleClass().remove(this.status.getStyleClass());
        }
        this.status = status;
        getStyleClass().add(status.getStyleClass());
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public JStatusBadge setText(String text) {
        label.setText(text);
        return this;
    }

    public String getText() {
        return label.getText();
    }

    public JStatusBadge setPulse(boolean pulse) {
        if (pulse) {
            if (pulseAnimation == null) {
                FadeTransition fade = new FadeTransition(Duration.millis(900), dot);
                fade.setFromValue(1.0);
                fade.setToValue(0.3);
                fade.setAutoReverse(true);
                fade.setCycleCount(Animation.INDEFINITE);

                ScaleTransition scale = new ScaleTransition(Duration.millis(900), dot);
                scale.setFromX(1.0);
                scale.setFromY(1.0);
                scale.setToX(1.35);
                scale.setToY(1.35);
                scale.setAutoReverse(true);
                scale.setCycleCount(Animation.INDEFINITE);

                pulseAnimation = new ParallelTransition(fade, scale);
            }
            pulseAnimation.play();
        } else {
            if (pulseAnimation != null) {
                pulseAnimation.stop();
                dot.setOpacity(1.0);
                dot.setScaleX(1.0);
                dot.setScaleY(1.0);
            }
        }
        return this;
    }

    public boolean isPulse() {
        return pulseAnimation != null && pulseAnimation.getStatus() == Animation.Status.RUNNING;
    }

    public JStatusBadge withStatus(Status status) {
        return setStatus(status);
    }

    public JStatusBadge withText(String text) {
        return setText(text);
    }

    public JStatusBadge withPulse(boolean pulse) {
        return setPulse(pulse);
    }
}
