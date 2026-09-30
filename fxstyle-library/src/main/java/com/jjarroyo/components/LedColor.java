package com.jjarroyo.components;

import javafx.scene.paint.Color;

/**
 * Paleta de colores industriales para indicadores LED (JLed).
 * Provee colores brillantes para estado activo (ON), apagado tenue (OFF)
 * y sombras de resplandor (Glow).
 */
public enum LedColor {
    GREEN(
        "#22C55E", "#14532D", "rgba(34, 197, 94, 0.75)",
        Color.web("#22C55E"), Color.web("#14532D")
    ),
    RED(
        "#EF4444", "#7F1D1D", "rgba(239, 68, 68, 0.75)",
        Color.web("#EF4444"), Color.web("#7F1D1D")
    ),
    AMBER(
        "#F59E0B", "#78350F", "rgba(245, 158, 11, 0.75)",
        Color.web("#F59E0B"), Color.web("#78350F")
    ),
    YELLOW(
        "#EAB308", "#713F12", "rgba(234, 179, 8, 0.75)",
        Color.web("#EAB308"), Color.web("#713F12")
    ),
    BLUE(
        "#3B82F6", "#1E3A8A", "rgba(59, 130, 246, 0.75)",
        Color.web("#3B82F6"), Color.web("#1E3A8A")
    ),
    CYAN(
        "#06B6D4", "#164E63", "rgba(6, 182, 212, 0.75)",
        Color.web("#06B6D4"), Color.web("#164E63")
    ),
    PURPLE(
        "#A855F7", "#581C87", "rgba(168, 85, 247, 0.75)",
        Color.web("#A855F7"), Color.web("#581C87")
    ),
    ORANGE(
        "#F97316", "#7C2D12", "rgba(249, 115, 22, 0.75)",
        Color.web("#F97316"), Color.web("#7C2D12")
    ),
    WHITE(
        "#F8FAFC", "#475569", "rgba(248, 250, 252, 0.75)",
        Color.web("#F8FAFC"), Color.web("#475569")
    );

    private final String hexOn;
    private final String hexOff;
    private final String glowRgba;
    private final Color colorOn;
    private final Color colorOff;

    LedColor(String hexOn, String hexOff, String glowRgba, Color colorOn, Color colorOff) {
        this.hexOn = hexOn;
        this.hexOff = hexOff;
        this.glowRgba = glowRgba;
        this.colorOn = colorOn;
        this.colorOff = colorOff;
    }

    public String getHexOn() {
        return hexOn;
    }

    public String getHexOff() {
        return hexOff;
    }

    public String getGlowRgba() {
        return glowRgba;
    }

    public Color getColorOn() {
        return colorOn;
    }

    public Color getColorOff() {
        return colorOff;
    }
}
