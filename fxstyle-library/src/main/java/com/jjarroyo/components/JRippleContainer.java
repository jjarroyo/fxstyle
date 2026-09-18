package com.jjarroyo.components;

import javafx.animation.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * A container component that adds Material/Tailwind style ripple wave effects on mouse click.
 * Wraps any JavaFX control or node.
 */
public class JRippleContainer extends StackPane {

    private final ObjectProperty<Node> content = new SimpleObjectProperty<>();
    private final ObjectProperty<Paint> rippleColor = new SimpleObjectProperty<>(Color.web("#3b82f6", 0.25));
    private final ObjectProperty<Duration> duration = new SimpleObjectProperty<>(Duration.millis(450));

    private final Rectangle clipRect = new Rectangle();

    public JRippleContainer() {
        getStyleClass().add("j-ripple-container");
        setClip(clipRect);

        layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            clipRect.setWidth(newVal.getWidth());
            clipRect.setHeight(newVal.getHeight());
        });

        content.addListener((obs, oldVal, newVal) -> {
            if (oldVal != null) getChildren().remove(oldVal);
            if (newVal != null) {
                getChildren().add(0, newVal); // Add as base layer
            }
        });

        addEventHandler(MouseEvent.MOUSE_PRESSED, this::createRipple);
    }

    public JRippleContainer(Node contentNode) {
        this();
        setContent(contentNode);
    }

    private void createRipple(MouseEvent e) {
        double mouseX = e.getX();
        double mouseY = e.getY();

        double width = getWidth() > 0 ? getWidth() : 100;
        double height = getHeight() > 0 ? getHeight() : 100;
        double targetRadius = Math.max(width, height) * 1.5;

        Circle circle = new Circle(mouseX, mouseY, 0, getRippleColor());
        circle.setOpacity(0.5);
        circle.setMouseTransparent(true);

        // Put ripple circle behind or in front of content
        getChildren().add(circle);

        Duration dur = getDuration();

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(circle.radiusProperty(), 0),
                new KeyValue(circle.opacityProperty(), 0.5)
            ),
            new KeyFrame(dur,
                new KeyValue(circle.radiusProperty(), targetRadius, Interpolator.EASE_OUT),
                new KeyValue(circle.opacityProperty(), 0.0, Interpolator.EASE_OUT)
            )
        );

        timeline.setOnFinished(evt -> getChildren().remove(circle));
        timeline.play();
    }

    // ── GETTERS & SETTERS ────────────────────────────────────────────────

    public Node getContent() {
        return content.get();
    }

    public void setContent(Node node) {
        content.set(node);
    }

    public ObjectProperty<Node> contentProperty() {
        return content;
    }

    public Paint getRippleColor() {
        return rippleColor.get();
    }

    public void setRippleColor(Paint color) {
        rippleColor.set(color != null ? color : Color.web("#3b82f6", 0.25));
    }

    public ObjectProperty<Paint> rippleColorProperty() {
        return rippleColor;
    }

    public Duration getDuration() {
        return duration.get();
    }

    public void setDuration(Duration val) {
        duration.set(val != null ? val : Duration.millis(450));
    }

    public ObjectProperty<Duration> durationProperty() {
        return duration;
    }
}
