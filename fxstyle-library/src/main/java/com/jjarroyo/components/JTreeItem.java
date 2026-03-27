package com.jjarroyo.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TreeItem;

/**
 * JTreeItem — Elemento para el JTreeView.
 * Permite asignar un ícono y manejar el estado de un checkbox interno.
 */
public class JTreeItem<T> extends TreeItem<T> {

    private JIcon icon;
    private final BooleanProperty checked = new SimpleBooleanProperty(false);

    public JTreeItem(T value) {
        super(value);
    }

    public JTreeItem(T value, JIcon icon) {
        super(value);
        this.icon = icon;
    }

    public JIcon getIcon() {
        return icon;
    }

    public void setIcon(JIcon icon) {
        this.icon = icon;
    }

    public boolean isChecked() {
        return checked.get();
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    public BooleanProperty checkedProperty() {
        return checked;
    }
}
