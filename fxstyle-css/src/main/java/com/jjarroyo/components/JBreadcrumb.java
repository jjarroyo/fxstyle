package com.jjarroyo.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

/**
 * JBreadcrumb — Navegación tipo breadcrumb (migas de pan).
 *
 * <pre>
 * JBreadcrumb bc = new JBreadcrumb();
 * bc.addItem("Inicio", () -> navigate("home"));
 * bc.addItem("Usuarios", () -> navigate("users"));
 * bc.addItem("Perfil"); // último = activo (sin acción)
 * </pre>
 */
public class JBreadcrumb extends HBox {

    private final List<BreadcrumbItem> items = new ArrayList<>();
    private String separator = "/";
    private String activeColor = "primary"; // primary, success, danger, etc.

    public JBreadcrumb() {
        getStyleClass().add("j-breadcrumb");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(0);
    }

    // ─── API ─────────────────────────────────────────────────────────────────────

    /** Agrega un item clickeable */
    public JBreadcrumb addItem(String text, Runnable action) {
        items.add(new BreadcrumbItem(text, action, null));
        rebuild();
        return this;
    }

    /** Agrega un item clickeable con ícono */
    public JBreadcrumb addItem(String text, Node icon, Runnable action) {
        items.add(new BreadcrumbItem(text, action, icon));
        rebuild();
        return this;
    }

    /** Agrega el item actual (último, sin acción) */
    public JBreadcrumb addItem(String text) {
        items.add(new BreadcrumbItem(text, null, null));
        rebuild();
        return this;
    }

    /** Agrega item actual con ícono */
    public JBreadcrumb addItem(String text, Node icon) {
        items.add(new BreadcrumbItem(text, null, icon));
        rebuild();
        return this;
    }

    /** Cambia el separador (default: "/") */
    public JBreadcrumb setSeparator(String sep) {
        this.separator = sep;
        rebuild();
        return this;
    }

    /** Cambia el color del item activo: primary, success, danger, warning, info */
    public JBreadcrumb setActiveColor(String color) {
        this.activeColor = color;
        rebuild();
        return this;
    }

    /** Limpia todos los items */
    public JBreadcrumb clear() {
        items.clear();
        getChildren().clear();
        return this;
    }

    /** Establece los items de una vez (los anteriores al último son navegables) */
    public JBreadcrumb setPath(String... paths) {
        items.clear();
        for (String p : paths) {
            items.add(new BreadcrumbItem(p, null, null));
        }
        rebuild();
        return this;
    }

    // ─── Build ───────────────────────────────────────────────────────────────────

    private void rebuild() {
        getChildren().clear();

        for (int i = 0; i < items.size(); i++) {
            BreadcrumbItem item = items.get(i);
            boolean isLast = (i == items.size() - 1);

            // Separator
            if (i > 0) {
                Label sep = new Label(separator);
                sep.getStyleClass().add("j-breadcrumb-separator");
                getChildren().add(sep);
            }

            // Item
            HBox itemBox = new HBox(4);
            itemBox.setAlignment(Pos.CENTER_LEFT);

            if (item.icon != null) {
                itemBox.getChildren().add(item.icon);
            }

            Label label = new Label(item.text);

            if (isLast) {
                // Active item
                label.getStyleClass().add("j-breadcrumb-active");
                itemBox.getStyleClass().add("j-breadcrumb-item-active");
                // Apply color variant
                getStyleClass().removeIf(c -> c.startsWith("j-breadcrumb-color-"));
                getStyleClass().add("j-breadcrumb-color-" + activeColor);
            } else {
                // Normal item (clickable)
                label.getStyleClass().add("j-breadcrumb-link");
                itemBox.getStyleClass().add("j-breadcrumb-item");
                if (item.action != null) {
                    itemBox.setOnMouseClicked(e -> item.action.run());
                }
            }

            itemBox.getChildren().add(label);
            getChildren().add(itemBox);
        }
    }

    // ─── Inner ───────────────────────────────────────────────────────────────────

    private static class BreadcrumbItem {
        final String text;
        final Runnable action;
        final Node icon;

        BreadcrumbItem(String text, Runnable action, Node icon) {
            this.text = text;
            this.action = action;
            this.icon = icon;
        }
    }
}
