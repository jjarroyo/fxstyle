package com.jjarroyo.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class JSidebarItem extends HBox {

    private Node icon;
    private Label label;
    private BooleanProperty active = new SimpleBooleanProperty(false);
    private Runnable action;

    public JSidebarItem(String text) {
        this(text, null);
    }

    public JSidebarItem(String text, Node icon) {
        this.icon = icon;
        this.label = new Label(text);
        init();
    }

    private void init() {
        getStyleClass().add("j-sidebar-item");
        
        // Layout
        setSpacing(12);
        setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        // Icon
        if (icon != null) {
            // potential wrapper for icon style?
            getChildren().add(icon);
        }
        
        // Label
        label.getStyleClass().add("j-sidebar-item-label");
        getChildren().add(label);
        
        // Spacer (optional, if we want badges on right)
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        getChildren().add(spacer);

        // Events
        setOnMouseClicked(e -> {
            if (action != null) action.run();
        });

        // Active State Listener
        active.addListener((obs, old, isActive) -> {
            if (isActive) {
                if (!getStyleClass().contains("active")) {
                    getStyleClass().add("active");
                }
            } else {
                getStyleClass().remove("active");
            }
        });
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public void setActive(boolean isActive) {
        this.active.set(isActive);
    }

    public boolean isActive() {
        return active.get();
    }
    
    public BooleanProperty activeProperty() {
        return active;
    }

    public void setIcon(Node icon) {
        this.icon = icon;
        getChildren().clear();
        if (icon != null) getChildren().add(icon);
        getChildren().addAll(label); // Re-add label and spacer mechanism if needed
        // Simpler: just rebuild
        getChildren().clear();
        if (icon != null) getChildren().add(icon);
        getChildren().add(label);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        getChildren().add(spacer);
    }
    
    public Node getIcon() {
        return icon;
    }
    
    public String getText() {
        return label.getText();
    }
}

