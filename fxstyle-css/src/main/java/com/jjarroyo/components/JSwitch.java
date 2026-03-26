package com.jjarroyo.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckBox;

public class JSwitch extends JCheckBox {

    public JSwitch() {
        super();
        initSwitch();
    }

    public JSwitch(String text) {
        super(text);
        initSwitch();
    }

    private void initSwitch() {
        getStyleClass().add("j-switch");
    }
}
