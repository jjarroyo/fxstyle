package com.jjarroyo.components;

/**
 * Representa el estado operativo de un medidor segun las zonas configuradas.
 */
public enum GaugeStatus {
    OK("Normal", "#1F8A70", "ok"),
    WARN("Atención", "#E29A12", "warn"),
    BAD("Crítico", "#C8442F", "bad");

    private final String label;
    private final String defaultHexColor;
    private final String cssClass;

    GaugeStatus(String label, String defaultHexColor, String cssClass) {
        this.label = label;
        this.defaultHexColor = defaultHexColor;
        this.cssClass = cssClass;
    }

    public String getLabel() {
        return label;
    }

    public String getDefaultHexColor() {
        return defaultHexColor;
    }

    public String getCssClass() {
        return cssClass;
    }
}
