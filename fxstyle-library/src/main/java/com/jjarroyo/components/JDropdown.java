package com.jjarroyo.components;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class JDropdown extends MenuButton {

    public JDropdown(String text) {
        super(text);
        init();
    }

    public JDropdown(String text, Node graphic) {
        super(text, graphic);
        init();
    }

    private void init() {
        getStyleClass().add("j-dropdown");
    }

    /**
     * Fluent API to add a single menu item.
     */
    public JDropdown addItem(MenuItem item) {
        getItems().add(item);
        return this;
    }

    /**
     * Fluent API to add multiple menu items.
     */
    public JDropdown addItems(MenuItem... items) {
        getItems().addAll(items);
        return this;
    }
    
    /**
     * Quick helper to add a simple text item with an action.
     */
    public JDropdown addAction(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        MenuItem item = new MenuItem(text);
        item.setOnAction(action);
        getItems().add(item);
        return this;
    }

    /**
     * Helper to add custom style classes.
     */
    public JDropdown addClass(String... styleClasses) {
        getStyleClass().addAll(styleClasses);
        return this;
    }
}

