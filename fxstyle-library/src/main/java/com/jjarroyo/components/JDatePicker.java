package com.jjarroyo.components;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;

public class JDatePicker extends DatePicker {

    private boolean disablePastDates = false;

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
        getEditor().getStyleClass().add("form-input");
    }

    public JDatePicker setModern(boolean modern) {
        if (modern) {
            if (!getStyleClass().contains("j-date-picker-modern")) {
                getStyleClass().add("j-date-picker-modern");
            }
            if (!getEditor().getStyleClass().contains("form-input-modern")) {
                getEditor().getStyleClass().add("form-input-modern");
            }
        } else {
            getStyleClass().remove("j-date-picker-modern");
            getEditor().getStyleClass().remove("form-input-modern");
        }
        return this;
    }

    public boolean isDisablePastDates() {
        return disablePastDates;
    }

    public JDatePicker setDisablePastDates(boolean disable) {
        this.disablePastDates = disable;
        if (disable) {
            setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (date != null && date.isBefore(LocalDate.now())) {
                        setDisable(true);
                        setStyle("-fx-background-color: #f1f5f9; -fx-text-fill: #94a3b8;");
                    }
                }
            });
            if (getValue() != null && getValue().isBefore(LocalDate.now())) {
                setValue(LocalDate.now());
            }
        } else {
            setDayCellFactory(null);
        }
        return this;
    }

    public JDatePicker setDisablePast(boolean disable) {
        return setDisablePastDates(disable);
    }
}
