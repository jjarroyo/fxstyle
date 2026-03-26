package com.jjarroyo.demo.views;

import com.jjarroyo.components.JChip;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JToast;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class ChipView extends ScrollPane {

    public ChipView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));

        setFitToWidth(true);
        setContent(content);

        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Chips / Tags")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Etiquetas para categorías, estados, filtros y tags")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        content.getChildren().add(new JCard("Variante Soft (default)", createSoftVariant()));
        content.getChildren().add(new JCard("Variante Filled", createFilledVariant()));
        content.getChildren().add(new JCard("Variante Outlined", createOutlinedVariant()));
        content.getChildren().add(new JCard("Tamaños", createSizes()));
        content.getChildren().add(new JCard("Con Íconos", createWithIcons()));
        content.getChildren().add(new JCard("Dismissible (Cerrables)", createDismissible()));
    }

    private javafx.scene.Node createSoftVariant() {
        FlowPane flow = new FlowPane(8, 8);
        JChip.ChipColor[] colors = JChip.ChipColor.values();
        for (JChip.ChipColor color : colors) {
            flow.getChildren().add(new JChip(color.name().toLowerCase()).setColor(color));
        }
        return flow;
    }

    private javafx.scene.Node createFilledVariant() {
        FlowPane flow = new FlowPane(8, 8);
        JChip.ChipColor[] colors = JChip.ChipColor.values();
        for (JChip.ChipColor color : colors) {
            flow.getChildren().add(
                new JChip(color.name().toLowerCase()).setColor(color).setVariant(JChip.Variant.FILLED)
            );
        }
        return flow;
    }

    private javafx.scene.Node createOutlinedVariant() {
        FlowPane flow = new FlowPane(8, 8);
        JChip.ChipColor[] colors = JChip.ChipColor.values();
        for (JChip.ChipColor color : colors) {
            flow.getChildren().add(
                new JChip(color.name().toLowerCase()).setColor(color).setVariant(JChip.Variant.OUTLINED)
            );
        }
        return flow;
    }

    private javafx.scene.Node createSizes() {
        FlowPane flow = new FlowPane(8, 8);
        flow.getChildren().addAll(
            new JChip("Small").setChipSize(JChip.Size.SM).setColor(JChip.ChipColor.INFO),
            new JChip("Medium").setChipSize(JChip.Size.MD).setColor(JChip.ChipColor.INFO),
            new JChip("Large").setChipSize(JChip.Size.LG).setColor(JChip.ChipColor.INFO)
        );
        return flow;
    }

    private javafx.scene.Node createWithIcons() {
        FlowPane flow = new FlowPane(8, 8);
        flow.getChildren().addAll(
            new JChip("Activo", JIcon.CHECK.view()).setColor(JChip.ChipColor.SUCCESS),
            new JChip("Error", JIcon.ERROR.view()).setColor(JChip.ChipColor.DANGER),
            new JChip("Info", JIcon.INFO.view()).setColor(JChip.ChipColor.INFO),
            new JChip("Alerta", JIcon.WARNING.view()).setColor(JChip.ChipColor.WARNING)
        );
        return flow;
    }

    private javafx.scene.Node createDismissible() {
        FlowPane flow = new FlowPane(8, 8);
        String[] tags = {"JavaScript", "Java", "Python", "TypeScript", "Rust"};
        JChip.ChipColor[] colors = {JChip.ChipColor.PRIMARY, JChip.ChipColor.SUCCESS, JChip.ChipColor.DANGER, JChip.ChipColor.WARNING, JChip.ChipColor.INFO};
        for (int i = 0; i < tags.length; i++) {
            final JChip chip = new JChip(tags[i]);
            chip.setColor(colors[i])
                .setDismissible(true)
                .setOnDismiss(() -> {
                    flow.getChildren().remove(chip);
                    if (getScene() != null && getScene().getWindow() != null) {
                        JToast.show(getScene().getWindow(),
                            "Chip eliminado: " + chip.getText(),
                            JToast.Type.INFO, JToast.Position.BOTTOM_RIGHT, 2000);
                    }
                });
            flow.getChildren().add(chip);
        }
        return flow;
    }
}
