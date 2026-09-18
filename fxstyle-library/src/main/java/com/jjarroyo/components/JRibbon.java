package com.jjarroyo.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * A decorative corner ribbon badge for highlighting cards, products, or status badges.
 */
public class JRibbon extends Label {

    public enum Variant {
        PRIMARY,
        SUCCESS,
        DANGER,
        WARNING,
        PURPLE,
        DARK
    }

    public enum Position {
        TOP_RIGHT,
        TOP_LEFT
    }

    private final ObjectProperty<Variant> variant = new SimpleObjectProperty<>(Variant.PRIMARY);
    private final ObjectProperty<Position> position = new SimpleObjectProperty<>(Position.TOP_RIGHT);

    public JRibbon() {
        this("NEW", Variant.PRIMARY, Position.TOP_RIGHT);
    }

    public JRibbon(String text) {
        this(text, Variant.PRIMARY, Position.TOP_RIGHT);
    }

    public JRibbon(String text, Variant variant) {
        this(text, variant, Position.TOP_RIGHT);
    }

    public JRibbon(String text, Variant variant, Position position) {
        super(text);
        getStyleClass().add("j-ribbon");

        this.variant.addListener((obs, oldVal, newVal) -> updateStyleClasses());
        this.position.addListener((obs, oldVal, newVal) -> updateStyleClasses());

        setVariant(variant);
        setPosition(position);
    }

    private void updateStyleClasses() {
        getStyleClass().removeIf(style -> style.startsWith("ribbon-"));

        // Position class
        if (getPosition() == Position.TOP_LEFT) {
            getStyleClass().add("ribbon-top-left");
        } else {
            getStyleClass().add("ribbon-top-right");
        }

        // Variant class
        switch (getVariant()) {
            case SUCCESS -> getStyleClass().add("ribbon-success");
            case DANGER -> getStyleClass().add("ribbon-danger");
            case WARNING -> getStyleClass().add("ribbon-warning");
            case PURPLE -> getStyleClass().add("ribbon-purple");
            case DARK -> getStyleClass().add("ribbon-dark");
            default -> getStyleClass().add("ribbon-primary");
        }
    }

    // ── FLUENT BUILDERS & SETTERS ────────────────────────────────────────

    public JRibbon withVariant(Variant v) {
        setVariant(v);
        return this;
    }

    public JRibbon withPosition(Position p) {
        setPosition(p);
        return this;
    }

    public Variant getVariant() {
        return variant.get();
    }

    public void setVariant(Variant v) {
        variant.set(v != null ? v : Variant.PRIMARY);
    }

    public ObjectProperty<Variant> variantProperty() {
        return variant;
    }

    public Position getPosition() {
        return position.get();
    }

    public void setPosition(Position p) {
        position.set(p != null ? p : Position.TOP_RIGHT);
    }

    public ObjectProperty<Position> positionProperty() {
        return position;
    }

    // ── STATIC WRAPPER HELPER ────────────────────────────────────────────

    public static JRibbonContainer wrap(Node targetNode, String text, Variant variant) {
        return wrap(targetNode, text, variant, Position.TOP_RIGHT);
    }

    public static JRibbonContainer wrap(Node targetNode, String text, Variant variant, Position position) {
        JRibbon ribbon = new JRibbon(text, variant, position);
        return new JRibbonContainer(targetNode, ribbon);
    }

    /**
     * Container that clips bounds and dynamically positions the JRibbon over a target node.
     * Automatically adjusts ribbon height and offset based on text length.
     */
    public static class JRibbonContainer extends StackPane {

        private final Rectangle clipRect = new Rectangle();
        private final JRibbon ribbon;

        public JRibbonContainer(Node content, JRibbon ribbon) {
            this.ribbon = ribbon;
            getStyleClass().add("j-ribbon-container");
            setClip(clipRect);

            layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
                clipRect.setWidth(newVal.getWidth());
                clipRect.setHeight(newVal.getHeight());
            });

            if (content != null) {
                getChildren().add(content);
            }

            if (ribbon != null) {
                ribbon.setManaged(false); // Managed false so we control exact 45° corner hypotenuse math!
                getChildren().add(ribbon);

                ribbon.textProperty().addListener((obs, o, n) -> requestLayout());
                ribbon.positionProperty().addListener((obs, o, n) -> requestLayout());
                ribbon.variantProperty().addListener((obs, o, n) -> requestLayout());
                ribbon.boundsInLocalProperty().addListener((obs, o, n) -> requestLayout());
            }
        }

        @Override
        protected void layoutChildren() {
            super.layoutChildren();

            if (ribbon != null) {
                ribbon.autosize();
                double ribbonWidth = ribbon.prefWidth(-1);
                double ribbonHeight = ribbon.prefHeight(-1);
                if (ribbonWidth <= 0) ribbonWidth = ribbon.getBoundsInLocal().getWidth();
                if (ribbonHeight <= 0) ribbonHeight = ribbon.getBoundsInLocal().getHeight();

                double containerWidth = getWidth();
                double c = (ribbonWidth / 2.0) * 0.70710678; // w / (2 * sqrt(2))

                if (ribbon.getPosition() == Position.TOP_LEFT) {
                    ribbon.setLayoutX(c - (ribbonWidth / 2.0));
                    ribbon.setLayoutY(c - (ribbonHeight / 2.0));
                } else {
                    ribbon.setLayoutX(containerWidth - c - (ribbonWidth / 2.0));
                    ribbon.setLayoutY(c - (ribbonHeight / 2.0));
                }
            }
        }

        public JRibbon getRibbon() {
            return ribbon;
        }
    }
}
