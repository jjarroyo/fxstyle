package com.jjarroyo.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * JNumberInput — Input numérico con botones + y − estilo Tailwind/Metronic.
 *
 * <pre>
 * JNumberInput input = new JNumberInput();
 * input.setValue(5);
 * input.setMin(0);
 * input.setMax(100);
 * input.setStep(1);
 * input.setOnValueChanged(val -> System.out.println("Valor: " + val));
 * </pre>
 */
public class JNumberInput extends HBox {

    private final TextField textField;
    private final Label decrementBtn;
    private final Label incrementBtn;

    private double value = 0;
    private double min = Double.NEGATIVE_INFINITY;
    private double max = Double.POSITIVE_INFINITY;
    private double step = 1;
    private boolean integerOnly = true;
    private java.util.function.Consumer<Double> onValueChanged;

    public JNumberInput() {
        getStyleClass().add("j-number-input");
        setAlignment(Pos.CENTER);

        // Decrement button
        decrementBtn = new Label("−");
        decrementBtn.getStyleClass().add("j-number-btn");
        decrementBtn.getStyleClass().add("j-number-btn-left");
        decrementBtn.setOnMouseClicked(e -> decrement());

        // Text field
        textField = new TextField(formatValue(value));
        textField.getStyleClass().add("j-number-field");
        textField.setAlignment(Pos.CENTER);
        HBox.setHgrow(textField, Priority.ALWAYS);

        // Only allow numeric input
        textField.textProperty().addListener((obs, old, newVal) -> {
            if (newVal == null || newVal.isEmpty()) return;
            String filtered = newVal.replaceAll("[^0-9.\\-]", "");
            if (!filtered.equals(newVal)) {
                textField.setText(filtered);
                return;
            }
        });

        textField.setOnAction(e -> commitText());
        textField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) commitText();
        });

        // Increment button
        incrementBtn = new Label("+");
        incrementBtn.getStyleClass().add("j-number-btn");
        incrementBtn.getStyleClass().add("j-number-btn-right");
        incrementBtn.setOnMouseClicked(e -> increment());

        getChildren().addAll(decrementBtn, textField, incrementBtn);
    }

    // ─── API ─────────────────────────────────────────────────────────────────────

    public double getValue() { return value; }

    public JNumberInput setValue(double value) {
        this.value = clamp(value);
        textField.setText(formatValue(this.value));
        return this;
    }

    public JNumberInput setMin(double min) {
        this.min = min;
        if (value < min) setValue(min);
        return this;
    }

    public JNumberInput setMax(double max) {
        this.max = max;
        if (value > max) setValue(max);
        return this;
    }

    public JNumberInput setStep(double step) {
        this.step = step;
        return this;
    }

    public JNumberInput setIntegerOnly(boolean integerOnly) {
        this.integerOnly = integerOnly;
        textField.setText(formatValue(value));
        return this;
    }

    public JNumberInput setOnValueChanged(java.util.function.Consumer<Double> callback) {
        this.onValueChanged = callback;
        return this;
    }

    public JNumberInput setPlaceholder(String placeholder) {
        textField.setPromptText(placeholder);
        return this;
    }

    public JNumberInput setNumberDisabled(boolean disabled) {
        setDisable(disabled);
        return this;
    }

    public TextField getTextField() { return textField; }

    // ─── Internal ────────────────────────────────────────────────────────────────

    private void increment() {
        double newVal = clamp(value + step);
        if (newVal != value) {
            value = newVal;
            textField.setText(formatValue(value));
            fireChanged();
        }
    }

    private void decrement() {
        double newVal = clamp(value - step);
        if (newVal != value) {
            value = newVal;
            textField.setText(formatValue(value));
            fireChanged();
        }
    }

    private void commitText() {
        try {
            double parsed = Double.parseDouble(textField.getText());
            double clamped = clamp(parsed);
            if (clamped != value) {
                value = clamped;
                fireChanged();
            }
            textField.setText(formatValue(value));
        } catch (NumberFormatException e) {
            textField.setText(formatValue(value));
        }
    }

    private double clamp(double val) {
        return Math.max(min, Math.min(max, val));
    }

    private String formatValue(double val) {
        if (integerOnly) return String.valueOf((int) val);
        return String.valueOf(val);
    }

    private void fireChanged() {
        if (onValueChanged != null) onValueChanged.accept(value);
    }
}
