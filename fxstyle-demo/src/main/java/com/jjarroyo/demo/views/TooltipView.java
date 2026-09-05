package com.jjarroyo.demo.views;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JTooltip;
import com.jjarroyo.demo.util.DemoCodeDialog;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class TooltipView extends ScrollPane {

    public TooltipView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));

        setFitToWidth(true);
        setContent(content);

        // Page Header
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("JTooltip")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Tooltips modernos estilizados con utilidades y variantes de FxStyle")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);

        content.getChildren().add(pageHeader);

        // Section: Color Variants
        JCard card1 = new JCard("Variantes de Color", createVariantTooltips());
        card1.addToolbarItem(DemoCodeDialog.createCodeButton("Variantes de JTooltip",
            "// Crear JTooltip con variantes de color:\n" +
            "JTooltip tooltipLight = new JTooltip(\"Información secundaria\", JTooltip.Variant.LIGHT);\n" +
            "JTooltip tooltipDanger = new JTooltip(\"Eliminar registro permanentemente\", JTooltip.Variant.DANGER);\n" +
            "JTooltip tooltipInfo = new JTooltip(\"Mensaje informativo\", JTooltip.Variant.INFO);\n\n" +
            "// Asignar a un botón o nodo JavaFX:\n" +
            "btn.setTooltip(tooltipDanger);"));
        content.getChildren().add(card1);

        // Section: Helper Static Installation
        JCard card2 = new JCard("Instalación Rápida (JTooltip.install)", createStaticInstallTooltips());
        card2.addToolbarItem(DemoCodeDialog.createCodeButton("JTooltip.install",
            "// Método helper estático para instalar en una sola línea:\n" +
            "JTooltip.install(buttonSave, \"Guardar los cambios actuales\");\n\n" +
            "// Instalación con variante especificada:\n" +
            "JTooltip.install(buttonDelete, \"Advertencia: No se puede deshacer\", JTooltip.Variant.DANGER);\n" +
            "JTooltip.install(iconInfo, \"Ver detalles del producto\", JTooltip.Variant.INFO);"));
        content.getChildren().add(card2);
    }

    // =========================================================================
    // COPIAR Y PEGAR: Tooltips con Variantes (JTooltip)
    // Ejemplo: node.setTooltip(new JTooltip("Mensaje", JTooltip.Variant.INFO));
    // =========================================================================
    private Node createVariantTooltips() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);

        JButton btnLight = new JButton("Light (Default)");
        btnLight.setTooltip(new JTooltip("Tooltip con estilo claro (Light)", JTooltip.Variant.LIGHT));

        JButton btnDark = new JButton("Default (Dark)");
        btnDark.addClass("btn-dark");
        btnDark.setTooltip(new JTooltip("Tooltip con estilo oscuro (Default)", JTooltip.Variant.DEFAULT));

        JButton btnInfo = new JButton("Info");
        btnInfo.addClass("btn-info");
        btnInfo.setTooltip(new JTooltip("Tooltip Informativo Azul", JTooltip.Variant.INFO));

        JButton btnSuccess = new JButton("Success");
        btnSuccess.addClass("btn-success");
        btnSuccess.setTooltip(new JTooltip("Operación completada con éxito", JTooltip.Variant.SUCCESS));

        JButton btnDanger = new JButton("Danger");
        btnDanger.addClass("btn-danger");
        btnDanger.setTooltip(new JTooltip("Advertencia: Acción destructiva", JTooltip.Variant.DANGER));

        pane.getChildren().addAll(btnLight, btnDark, btnInfo, btnSuccess, btnDanger);
        return pane;
    }

    // =========================================================================
    // COPIAR Y PEGAR: Instalación Rápida Estática (JTooltip.install)
    // Ejemplo: JTooltip.install(button, "Mensaje de ayuda", JTooltip.Variant.DANGER);
    // =========================================================================
    private Node createStaticInstallTooltips() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);

        JButton btnSave = new JButton("Guardar Cambios", JIcon.CHECK);
        btnSave.addClass("btn-success");
        JTooltip.install(btnSave, "Guarda todos los datos modificados en la base de datos");

        JButton btnDelete = new JButton("Eliminar", JIcon.CLOSE);
        btnDelete.addClass("btn-danger");
        JTooltip.install(btnDelete, "Elimina permanentemente este registro", JTooltip.Variant.DANGER);

        JLabel labelHelp = new JLabel("Pasa el mouse aquí para ayuda")
            .withStyle("text-sm", "text-blue-600", "underline");
        JTooltip.install(labelHelp, "Ayuda contextual instalada en un JLabel nativo", JTooltip.Variant.INFO);

        pane.getChildren().addAll(btnSave, btnDelete, labelHelp);
        return pane;
    }
}
