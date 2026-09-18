package com.jjarroyo.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;

/**
 * A split button control combining a primary action button on the left
 * and a dropdown menu trigger arrow on the right.
 */
public class JSplitButton extends HBox {

    public enum Variant {
        PRIMARY,
        SECONDARY,
        SUCCESS,
        DANGER,
        WARNING,
        DARK,
        OUTLINE
    }

    private final JButton mainButton;
    private final JButton arrowButton;
    private final ContextMenu contextMenu = new ContextMenu();
    private final ObjectProperty<Variant> variant = new SimpleObjectProperty<>(Variant.PRIMARY);

    public JSplitButton() {
        this("Action", (Node) null, null);
    }

    public JSplitButton(String text) {
        this(text, (Node) null, null);
    }

    public JSplitButton(String text, Runnable action) {
        this(text, (Node) null, action);
    }

    public JSplitButton(String text, Node icon) {
        this(text, icon, null);
    }

    public JSplitButton(String text, JIcon icon) {
        this(text, icon != null ? icon.view() : null, null);
    }

    public JSplitButton(String text, JIcon icon, Runnable action) {
        this(text, icon != null ? icon.view() : null, action);
    }

    public JSplitButton(String text, Node icon, Runnable action) {
        getStyleClass().add("j-split-button");

        mainButton = new JButton(text);
        if (icon != null) {
            mainButton.setGraphic(icon);
        }
        mainButton.getStyleClass().add("split-main-btn");
        mainButton.setMaxHeight(Double.MAX_VALUE);

        arrowButton = new JButton();
        arrowButton.getStyleClass().add("split-arrow-btn");
        arrowButton.setMaxHeight(Double.MAX_VALUE);

        if (action != null) {
            mainButton.setOnAction(e -> action.run());
        }

        arrowButton.setOnAction(e -> {
            if (!contextMenu.getItems().isEmpty()) {
                contextMenu.show(arrowButton, Side.BOTTOM, 0, 4);
            }
        });

        getChildren().addAll(mainButton, arrowButton);

        variant.addListener((obs, oldVal, newVal) -> updateVariantStyles());
        setVariant(Variant.PRIMARY);
    }

    private void updateVariantStyles() {
        getStyleClass().removeIf(style -> style.startsWith("split-"));
        mainButton.getStyleClass().removeIf(style -> style.startsWith("btn-"));
        arrowButton.getStyleClass().removeIf(style -> style.startsWith("btn-"));

        String btnClass = switch (getVariant()) {
            case SECONDARY -> {
                getStyleClass().add("split-secondary");
                yield "btn-secondary";
            }
            case SUCCESS -> {
                getStyleClass().add("split-success");
                yield "btn-success";
            }
            case DANGER -> {
                getStyleClass().add("split-danger");
                yield "btn-danger";
            }
            case WARNING -> {
                getStyleClass().add("split-warning");
                yield "btn-warning";
            }
            case DARK -> {
                getStyleClass().add("split-dark");
                yield "btn-dark";
            }
            case OUTLINE -> {
                getStyleClass().add("split-outline");
                yield "btn-outline";
            }
            default -> {
                getStyleClass().add("split-primary");
                yield "btn-primary";
            }
        };

        mainButton.getStyleClass().add(btnClass);
        arrowButton.getStyleClass().add(btnClass);
        arrowButton.setIcon(JIcon.EXPAND_MORE);
    }

    // ── MENU & ACTION HELPERS ───────────────────────────────────────────

    public JSplitButton setOnAction(EventHandler<ActionEvent> value) {
        mainButton.setOnAction(value);
        return this;
    }

    public JSplitButton withAction(Runnable action) {
        if (action != null) {
            mainButton.setOnAction(e -> action.run());
        }
        return this;
    }

    public JSplitButton addMenuItem(String title, Runnable action) {
        return addMenuItem(title, null, action);
    }

    public JSplitButton addMenuItem(String title, Node icon, Runnable action) {
        MenuItem item = new MenuItem(title);
        if (icon != null) {
            item.setGraphic(icon);
        }
        if (action != null) {
            item.setOnAction(e -> action.run());
        }
        contextMenu.getItems().add(item);
        return this;
    }

    public JSplitButton addMenuItem(MenuItem menuItem) {
        contextMenu.getItems().add(menuItem);
        return this;
    }

    public JSplitButton addSeparator() {
        contextMenu.getItems().add(new SeparatorMenuItem());
        return this;
    }

    // ── GETTERS & SETTERS ────────────────────────────────────────────────

    public JButton getMainButton() {
        return mainButton;
    }

    public JButton getArrowButton() {
        return arrowButton;
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public Variant getVariant() {
        return variant.get();
    }

    public void setVariant(Variant val) {
        variant.set(val != null ? val : Variant.PRIMARY);
    }

    public ObjectProperty<Variant> variantProperty() {
        return variant;
    }

    public JSplitButton withVariant(Variant v) {
        setVariant(v);
        return this;
    }

    public String getText() {
        return mainButton.getText();
    }

    public void setText(String text) {
        mainButton.setText(text);
    }

    public Node getIcon() {
        return mainButton.getGraphic();
    }

    public void setIcon(Node icon) {
        mainButton.setGraphic(icon);
    }
}
