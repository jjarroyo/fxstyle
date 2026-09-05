package com.jjarroyo.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * JTimePicker — selector de hora compacto HH:mm con spinners de subir/bajar,
 * estilo moderno, campos editables por teclado y validación de hora mínima.
 */
public class JTimePicker extends HBox {

    private final StringProperty value = new SimpleStringProperty("00:00");
    private int hours = 0;
    private int minutes = 0;

    private final TextField hourField;
    private final TextField minuteField;

    private JDatePicker parentDatePicker = null;
    private boolean disablePastTime = false;

    public JTimePicker() {
        this("00:00");
    }

    public JTimePicker(String initialTime) {
        getStyleClass().add("j-time-picker");
        setAlignment(Pos.CENTER);
        setSpacing(2);

        parseTime(initialTime);

        hourField = createField(hours, true);
        VBox hourSpinner = createSpinner(hourField, true);

        Label separator = new Label(":");
        separator.getStyleClass().add("j-time-separator");

        minuteField = createField(minutes, false);
        VBox minuteSpinner = createSpinner(minuteField, false);

        getChildren().addAll(hourSpinner, separator, minuteSpinner);

        value.set(formatTime());
        value.addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(formatTime())) {
                parseTime(newVal);
                validateMinTime();
                hourField.setText(String.format("%02d", hours));
                minuteField.setText(String.format("%02d", minutes));
            }
        });
    }

    public JTimePicker setModern(boolean modern) {
        if (modern) {
            if (!getStyleClass().contains("j-time-picker-modern")) {
                getStyleClass().add("j-time-picker-modern");
            }
            if (!hourField.getStyleClass().contains("form-input-modern")) {
                hourField.getStyleClass().add("form-input-modern");
            }
            if (!minuteField.getStyleClass().contains("form-input-modern")) {
                minuteField.getStyleClass().add("form-input-modern");
            }
        } else {
            getStyleClass().remove("j-time-picker-modern");
            hourField.getStyleClass().remove("form-input-modern");
            minuteField.getStyleClass().remove("form-input-modern");
        }
        return this;
    }

    public JTimePicker setParentDatePicker(JDatePicker parent) {
        this.parentDatePicker = parent;
        if (parent != null) {
            parent.valueProperty().addListener((obs, oldDate, newDate) -> validateMinTime());
        }
        validateMinTime();
        return this;
    }

    public JTimePicker setDisablePastTime(boolean disable) {
        this.disablePastTime = disable;
        validateMinTime();
        return this;
    }

    public JTimePicker setDisablePastTime(boolean disable, JDatePicker parent) {
        this.disablePastTime = disable;
        setParentDatePicker(parent);
        return this;
    }

    private void validateMinTime() {
        if (!disablePastTime) return;

        LocalDate targetDate = (parentDatePicker != null && parentDatePicker.getValue() != null)
                ? parentDatePicker.getValue() : LocalDate.now();

        if (targetDate.isEqual(LocalDate.now()) || targetDate.isBefore(LocalDate.now())) {
            LocalTime now = LocalTime.now();
            LocalTime current = LocalTime.of(hours, minutes);
            if (current.isBefore(now)) {
                hours = now.getHour();
                minutes = now.getMinute();
                hourField.setText(String.format("%02d", hours));
                minuteField.setText(String.format("%02d", minutes));
                value.set(formatTime());
            }
        }
    }

    private TextField createField(int initialValue, boolean isHour) {
        TextField field = new TextField(String.format("%02d", initialValue));
        field.getStyleClass().add("j-time-field");
        field.setPrefWidth(40);
        field.setMaxWidth(40);
        field.setAlignment(Pos.CENTER);

        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d{0,2}")) {
                field.setText(oldVal);
            }
        });

        field.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                applyFieldValue(field, isHour);
            }
        });

        field.setOnAction(e -> applyFieldValue(field, isHour));

        field.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP) {
                step(field, isHour, +1);
                e.consume();
            } else if (e.getCode() == KeyCode.DOWN) {
                step(field, isHour, -1);
                e.consume();
            }
        });

        field.setOnMouseClicked(e -> field.selectAll());
        field.focusedProperty().addListener((obs, o, focused) -> {
            if (focused) field.selectAll();
        });

        return field;
    }

    private VBox createSpinner(TextField field, boolean isHour) {
        VBox spinner = new VBox(0);
        spinner.setAlignment(Pos.CENTER);
        spinner.getStyleClass().add("j-time-spinner");

        Button upBtn = new Button("▲");
        upBtn.getStyleClass().add("j-time-btn");
        upBtn.setOnAction(e -> step(field, isHour, +1));

        Button downBtn = new Button("▼");
        downBtn.getStyleClass().add("j-time-btn");
        downBtn.setOnAction(e -> step(field, isHour, -1));

        spinner.getChildren().addAll(upBtn, field, downBtn);
        return spinner;
    }

    private void step(TextField field, boolean isHour, int delta) {
        int newHours = hours;
        int newMinutes = minutes;

        if (isHour) {
            newHours = (hours + delta + 24) % 24;
        } else {
            newMinutes = (minutes + delta + 60) % 60;
        }

        if (disablePastTime) {
            LocalDate targetDate = (parentDatePicker != null && parentDatePicker.getValue() != null)
                    ? parentDatePicker.getValue() : LocalDate.now();
            if (targetDate.isEqual(LocalDate.now()) || targetDate.isBefore(LocalDate.now())) {
                LocalTime minTime = LocalTime.now();
                LocalTime targetTime = LocalTime.of(newHours, newMinutes);
                if (targetTime.isBefore(minTime)) {
                    newHours = minTime.getHour();
                    newMinutes = minTime.getMinute();
                }
            }
        }

        hours = newHours;
        minutes = newMinutes;
        hourField.setText(String.format("%02d", hours));
        minuteField.setText(String.format("%02d", minutes));
        value.set(formatTime());
    }

    private void applyFieldValue(TextField field, boolean isHour) {
        try {
            int parsed = Integer.parseInt(field.getText().trim());
            int newHours = hours;
            int newMinutes = minutes;

            if (isHour) {
                newHours = Math.max(0, Math.min(23, parsed));
            } else {
                newMinutes = Math.max(0, Math.min(59, parsed));
            }

            if (disablePastTime) {
                LocalDate targetDate = (parentDatePicker != null && parentDatePicker.getValue() != null)
                        ? parentDatePicker.getValue() : LocalDate.now();
                if (targetDate.isEqual(LocalDate.now()) || targetDate.isBefore(LocalDate.now())) {
                    LocalTime minTime = LocalTime.now();
                    LocalTime targetTime = LocalTime.of(newHours, newMinutes);
                    if (targetTime.isBefore(minTime)) {
                        newHours = minTime.getHour();
                        newMinutes = minTime.getMinute();
                    }
                }
            }

            hours = newHours;
            minutes = newMinutes;
        } catch (NumberFormatException ex) {
            // Restaurar valor previo si es inválido
        }
        hourField.setText(String.format("%02d", hours));
        minuteField.setText(String.format("%02d", minutes));
        value.set(formatTime());
    }

    private void parseTime(String time) {
        try {
            if (time != null && time.contains(":")) {
                String[] parts = time.split(":");
                hours   = Math.max(0, Math.min(23, Integer.parseInt(parts[0].trim())));
                minutes = Math.max(0, Math.min(59, Integer.parseInt(parts[1].trim())));
            }
        } catch (NumberFormatException e) {
            hours = 0;
            minutes = 0;
        }
    }

    private String formatTime() {
        return String.format("%02d:%02d", hours, minutes);
    }

    public String getValue() { return value.get(); }
    public void setValue(String v) { value.set(v); }
    public StringProperty valueProperty() { return value; }
    public int getHours() { return hours; }
    public int getMinutes() { return minutes; }
    public TextField getHourField() { return hourField; }
    public TextField getMinuteField() { return minuteField; }
}
