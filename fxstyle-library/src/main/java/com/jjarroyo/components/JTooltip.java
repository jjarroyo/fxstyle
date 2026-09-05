package com.jjarroyo.components;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

/**
 * Componente de Tooltip moderno estilizado con utilidades de fxstyle (Tailwind CSS).
 * Reemplaza el tooltip oscuro por defecto de JavaFX por un diseño elegante y personalizable.
 */
public class JTooltip extends Tooltip {

    public enum Variant {
        DEFAULT("tooltip"),
        LIGHT("tooltip-light"),
        INFO("tooltip-info"),
        SUCCESS("tooltip-success"),
        DANGER("tooltip-danger");

        private final String styleClass;

        Variant(String styleClass) {
            this.styleClass = styleClass;
        }

        public String getStyleClass() {
            return styleClass;
        }
    }

    public JTooltip() {
        setVariant(Variant.LIGHT);
    }

    public JTooltip(String text) {
        super(text);
        setVariant(Variant.LIGHT);
    }

    public JTooltip(String text, Variant variant) {
        super(text);
        setVariant(variant);
    }

    public JTooltip setVariant(Variant variant) {
        if (variant == null) {
            variant = Variant.LIGHT;
        }
        getStyleClass().clear();
        getStyleClass().add(variant.getStyleClass());
        setShowDelay(Duration.millis(150));
        setShowDuration(Duration.seconds(10));
        return this;
    }

    /**
     * Instala un JTooltip en un nodo de JavaFX con estilo claro por defecto.
     *
     * @param node El nodo al que se adjuntará el tooltip
     * @param text El contenido textual del tooltip
     * @return Instancia del JTooltip instalado
     */
    public static JTooltip install(Node node, String text) {
        return install(node, text, Variant.LIGHT);
    }

    /**
     * Instala un JTooltip en un nodo de JavaFX con la variante seleccionada.
     *
     * @param node    El nodo al que se adjuntará el tooltip
     * @param text    El contenido textual del tooltip
     * @param variant La variante de color (LIGHT, DEFAULT, INFO, SUCCESS, DANGER)
     * @return Instancia del JTooltip instalado
     */
    public static JTooltip install(Node node, String text, Variant variant) {
        if (node == null || text == null || text.isBlank()) {
            return null;
        }
        JTooltip tooltip = new JTooltip(text, variant);
        Tooltip.install(node, tooltip);
        return tooltip;
    }
}
