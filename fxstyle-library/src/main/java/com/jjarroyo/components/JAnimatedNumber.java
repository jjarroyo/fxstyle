package com.jjarroyo.components;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * A Label control that smoothly animates numeric transitions (CountUp/CountDown).
 * Ideal for KPI cards, financial values, and dashboard metrics.
 */
public class JAnimatedNumber extends Label {

    private final DoubleProperty currentValue = new SimpleDoubleProperty(0.0);
    private final StringProperty prefix = new SimpleStringProperty("");
    private final StringProperty suffix = new SimpleStringProperty("");
    private final IntegerProperty decimalPlaces = new SimpleIntegerProperty(0);
    private final BooleanProperty useGroupSeparators = new SimpleBooleanProperty(true);
    private final ObjectProperty<Duration> duration = new SimpleObjectProperty<>(Duration.millis(1000));

    private Timeline activeTimeline;

    public JAnimatedNumber() {
        this(0.0);
    }

    public JAnimatedNumber(double initialValue) {
        getStyleClass().add("j-animated-number");
        currentValue.addListener((obs, oldVal, newVal) -> updateText(newVal.doubleValue()));
        prefix.addListener((obs, o, n) -> updateText(currentValue.get()));
        suffix.addListener((obs, o, n) -> updateText(currentValue.get()));
        decimalPlaces.addListener((obs, o, n) -> updateText(currentValue.get()));
        useGroupSeparators.addListener((obs, o, n) -> updateText(currentValue.get()));

        setValue(initialValue);
    }

    public JAnimatedNumber(double startValue, double targetValue) {
        this(startValue);
        animateTo(targetValue);
    }

    public void setValue(double value) {
        if (activeTimeline != null) {
            activeTimeline.stop();
        }
        currentValue.set(value);
    }

    public double getValue() {
        return currentValue.get();
    }

    public DoubleProperty valueProperty() {
        return currentValue;
    }

    public void animateTo(double targetValue) {
        animateTo(targetValue, getDuration());
    }

    public void animateTo(double targetValue, Duration customDuration) {
        if (activeTimeline != null) {
            activeTimeline.stop();
        }

        activeTimeline = new Timeline(
            new KeyFrame(
                customDuration,
                new KeyValue(currentValue, targetValue, Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0)) // Smooth easeOutCubic
            )
        );
        activeTimeline.play();
    }

    private void updateText(double value) {
        StringBuilder pattern = new StringBuilder();
        if (getUseGroupSeparators()) {
            pattern.append("#,##0");
        } else {
            pattern.append("0");
        }

        int decimals = getDecimalPlaces();
        if (decimals > 0) {
            pattern.append(".");
            for (int i = 0; i < decimals; i++) {
                pattern.append("0");
            }
        }

        DecimalFormat df = new DecimalFormat(pattern.toString(), new DecimalFormatSymbols(Locale.US));
        String formattedNumber = df.format(value);

        setText(getPrefix() + formattedNumber + getSuffix());
    }

    // ── GETTERS & SETTERS ────────────────────────────────────────────────

    public String getPrefix() {
        return prefix.get();
    }

    public void setPrefix(String val) {
        prefix.set(val != null ? val : "");
    }

    public StringProperty prefixProperty() {
        return prefix;
    }

    public String getSuffix() {
        return suffix.get();
    }

    public void setSuffix(String val) {
        suffix.set(val != null ? val : "");
    }

    public StringProperty suffixProperty() {
        return suffix;
    }

    public int getDecimalPlaces() {
        return decimalPlaces.get();
    }

    public void setDecimalPlaces(int val) {
        decimalPlaces.set(Math.max(0, val));
    }

    public IntegerProperty decimalPlacesProperty() {
        return decimalPlaces;
    }

    public boolean getUseGroupSeparators() {
        return useGroupSeparators.get();
    }

    public void setUseGroupSeparators(boolean val) {
        useGroupSeparators.set(val);
    }

    public BooleanProperty useGroupSeparatorsProperty() {
        return useGroupSeparators;
    }

    public Duration getDuration() {
        return duration.get();
    }

    public void setDuration(Duration val) {
        duration.set(val != null ? val : Duration.millis(1000));
    }

    public ObjectProperty<Duration> durationProperty() {
        return duration;
    }
}
