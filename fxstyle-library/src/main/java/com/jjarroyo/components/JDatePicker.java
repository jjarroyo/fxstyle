package com.jjarroyo.components;

import javafx.scene.control.DatePicker;
import java.time.LocalDate;

public class JDatePicker extends DatePicker {

    public JDatePicker() {
        super();
        init();
    }

    public JDatePicker(LocalDate localDate) {
        super(localDate);
        init();
    }

    private void init() {
        getStyleClass().add("j-date-picker");
        // Ensure the editor (TextField inside) looks like a JInput
        getEditor().getStyleClass().add("form-input"); 
    }
}
