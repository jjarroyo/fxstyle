package com.jjarroyo.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * JChip — Etiqueta/chip estilo Tailwind para categorías, tags, filtros.
 *
 * <pre>
 * JChip chip = new JChip("JavaFX");
 * chip.setColor("primary");
 * chip.setDismissible(true);
 * chip.setOnDismiss(() -> container.getChildren().remove(chip));
 * </pre>
 */
public class JChip extends HBox {

    public enum Variant { FILLED, OUTLINED, SOFT }
    public enum Size { SM, MD, LG }
    public enum ChipColor { PRIMARY, SUCCESS, DANGER, WARNING, INFO, SLATE }
    private final Label textLabel;
    private Label dismissBtn;
    private ChipColor color = ChipColor.PRIMARY;
    private Variant variant = Variant.SOFT;
    private Size size = Size.MD;
    private Node icon;
    private Runnable onDismiss;
    private boolean dismissible = false;

    public JChip(String text) {
        this.textLabel = new Label(text);
        getStyleClass().add("j-chip");
        setAlignment(Pos.CENTER);
        rebuild();
    }

    public JChip(String text, Node icon) {
        this.textLabel = new Label(text);
        this.icon = icon;
        getStyleClass().add("j-chip");
        setAlignment(Pos.CENTER);
        rebuild();
    }

    // ─── API ─────────────────────────────────────────────────────────────────────

    /** Color: primary, success, danger, warning, info, slate */
    public JChip setColor(ChipColor color) {
        this.color = color;
        applyStyles();
        return this;
    }

    /** Variante visual */
    public JChip setVariant(Variant variant) {
        this.variant = variant;
        applyStyles();
        return this;
    }

    /** Tamaño */
    public JChip setChipSize(Size size) {
        this.size = size;
        applyStyles();
        return this;
    }

    /** Texto */
    public JChip setText(String text) {
        this.textLabel.setText(text);
        return this;
    }

    public String getText() {
        return this.textLabel.getText();
    }

    /** Ícono a la izquierda */
    public JChip setIcon(Node icon) {
        this.icon = icon;
        rebuild();
        return this;
    }

    /** Habilita botón de cerrar (×) */
    public JChip setDismissible(boolean dismissible) {
        this.dismissible = dismissible;
        rebuild();
        return this;
    }

    /** Callback al cerrar */
    public JChip setOnDismiss(Runnable onDismiss) {
        this.onDismiss = onDismiss;
        return this;
    }

    // ─── Build ───────────────────────────────────────────────────────────────────

    private void rebuild() {
        getChildren().clear();

        if (icon != null) {
            getChildren().add(icon);
        }

        textLabel.getStyleClass().setAll("j-chip-text");
        getChildren().add(textLabel);

        if (dismissible) {
            dismissBtn = new Label("✕");
            dismissBtn.getStyleClass().add("j-chip-dismiss");
            dismissBtn.setOnMouseClicked(e -> {
                if (onDismiss != null) onDismiss.run();
            });
            getChildren().add(dismissBtn);
        }

        applyStyles();
    }

    private void applyStyles() {
        getStyleClass().removeIf(c ->
            c.startsWith("j-chip-color-") ||
            c.startsWith("j-chip-variant-") ||
            c.startsWith("j-chip-size-")
        );
        getStyleClass().addAll(
            "j-chip-color-" + color.name().toLowerCase(),
            "j-chip-variant-" + variant.name().toLowerCase(),
            "j-chip-size-" + size.name().toLowerCase()
        );
    }
}
