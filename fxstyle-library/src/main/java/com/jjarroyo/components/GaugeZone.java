package com.jjarroyo.components;

import javafx.scene.paint.Color;

/**
 * Define una zona o rango dentro de la escala del medidor (ej: zona normal, atencion o critica/redline).
 */
public class GaugeZone {
    private final double from;
    private final double to;
    private final GaugeStatus status;
    private final Color customColor;

    public GaugeZone(double from, double to, GaugeStatus status) {
        this.from = from;
        this.to = to;
        this.status = status;
        this.customColor = status != null ? Color.web(status.getDefaultHexColor()) : Color.TRANSPARENT;
    }

    public GaugeZone(double from, double to, Color customColor) {
        this.from = from;
        this.to = to;
        this.status = null;
        this.customColor = customColor;
    }

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }

    public GaugeStatus getStatus() {
        return status;
    }

    public Color getColor() {
        return customColor;
    }

    public boolean contains(double value) {
        return value >= from && value <= to;
    }
}
