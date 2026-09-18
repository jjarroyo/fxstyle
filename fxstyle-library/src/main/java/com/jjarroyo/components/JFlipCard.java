package com.jjarroyo.components;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * A 3D Flip Card component with front and back sides.
 * Rotates 180 degrees on Y or X axis smoothly upon click or hover.
 */
public class JFlipCard extends StackPane {

    private final ObjectProperty<Node> front = new SimpleObjectProperty<>();
    private final ObjectProperty<Node> back = new SimpleObjectProperty<>();
    private final BooleanProperty flipped = new SimpleBooleanProperty(false);
    private final BooleanProperty flipOnHover = new SimpleBooleanProperty(false);
    private final ObjectProperty<Duration> duration = new SimpleObjectProperty<>(Duration.millis(500));
    private final ObjectProperty<Point3D> axis = new SimpleObjectProperty<>(Rotate.Y_AXIS);

    private final Rotate rotateTransform = new Rotate(0, Rotate.Y_AXIS);
    private final Rotate backMirrorTransform = new Rotate(180, Rotate.Y_AXIS);
    private Timeline activeTimeline;

    public JFlipCard() {
        getStyleClass().add("j-flip-card");
        getTransforms().add(rotateTransform);

        rotateTransform.axisProperty().bind(axis);
        backMirrorTransform.axisProperty().bind(axis);

        front.addListener((obs, oldVal, newVal) -> {
            if (oldVal != null) getChildren().remove(oldVal);
            if (newVal != null) {
                getChildren().add(newVal);
                newVal.setVisible(!isFlipped());
            }
        });

        back.addListener((obs, oldVal, newVal) -> {
            if (oldVal != null) {
                oldVal.getTransforms().remove(backMirrorTransform);
                getChildren().remove(oldVal);
            }
            if (newVal != null) {
                if (!newVal.getTransforms().contains(backMirrorTransform)) {
                    newVal.getTransforms().add(backMirrorTransform);
                }
                getChildren().add(newVal);
                newVal.setVisible(isFlipped());
            }
        });

        setOnMouseClicked(e -> {
            if (!getFlipOnHover()) {
                flip();
            }
        });

        setOnMouseEntered(e -> {
            if (getFlipOnHover() && !isFlipped()) {
                flipToBack();
            }
        });

        setOnMouseExited(e -> {
            if (getFlipOnHover() && isFlipped()) {
                flipToFront();
            }
        });
    }

    public JFlipCard(Node frontNode, Node backNode) {
        this();
        setFront(frontNode);
        setBack(backNode);
    }

    public void flip() {
        if (isFlipped()) {
            flipToFront();
        } else {
            flipToBack();
        }
    }

    public void flipToBack() {
        animateRotation(180.0, true);
    }

    public void flipToFront() {
        animateRotation(0.0, false);
    }

    private void animateRotation(double targetAngle, boolean targetFlippedState) {
        if (activeTimeline != null) {
            activeTimeline.stop();
        }

        double startAngle = rotateTransform.getAngle();
        Duration totalDuration = getDuration();

        activeTimeline = new Timeline();

        // Keyframe at 50%: swap visibility
        KeyFrame halfWayFrame = new KeyFrame(
            totalDuration.multiply(0.5),
            e -> {
                flipped.set(targetFlippedState);
                if (getFront() != null) getFront().setVisible(!targetFlippedState);
                if (getBack() != null) getBack().setVisible(targetFlippedState);
            },
            new KeyValue(rotateTransform.angleProperty(), (startAngle + targetAngle) / 2.0, Interpolator.EASE_BOTH)
        );

        // Keyframe at 100%: complete rotation
        KeyFrame endFrame = new KeyFrame(
            totalDuration,
            new KeyValue(rotateTransform.angleProperty(), targetAngle, Interpolator.EASE_BOTH)
        );

        activeTimeline.getKeyFrames().addAll(halfWayFrame, endFrame);
        activeTimeline.play();
    }

    // ── GETTERS & SETTERS ────────────────────────────────────────────────

    public Node getFront() {
        return front.get();
    }

    public void setFront(Node node) {
        front.set(node);
    }

    public ObjectProperty<Node> frontProperty() {
        return front;
    }

    public Node getBack() {
        return back.get();
    }

    public void setBack(Node node) {
        back.set(node);
    }

    public ObjectProperty<Node> backProperty() {
        return back;
    }

    public boolean isFlipped() {
        return flipped.get();
    }

    public BooleanProperty flippedProperty() {
        return flipped;
    }

    public boolean getFlipOnHover() {
        return flipOnHover.get();
    }

    public void setFlipOnHover(boolean val) {
        flipOnHover.set(val);
    }

    public BooleanProperty flipOnHoverProperty() {
        return flipOnHover;
    }

    public Duration getDuration() {
        return duration.get();
    }

    public void setDuration(Duration val) {
        duration.set(val != null ? val : Duration.millis(500));
    }

    public ObjectProperty<Duration> durationProperty() {
        return duration;
    }

    public Point3D getAxis() {
        return axis.get();
    }

    public void setAxis(Point3D val) {
        axis.set(val != null ? val : Rotate.Y_AXIS);
    }

    public ObjectProperty<Point3D> axisProperty() {
        return axis;
    }
}
