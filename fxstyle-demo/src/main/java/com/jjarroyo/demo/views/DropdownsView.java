package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JDropdown;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JToast;
import com.jjarroyo.components.JZoom;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DropdownsView extends ScrollPane {

    public DropdownsView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));
        
        setFitToWidth(true);
        setContent(content);
        
        // Page Title
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Dropdowns & Zoom")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Menús desplegables y controles de zoom")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        // Basic Dropdown
        content.getChildren().add(new JCard("Dropdown Básico", createBasicDropdowns()));

        // Dropdown with Actions
        content.getChildren().add(new JCard("Dropdown con Acciones", createActionDropdowns()));
        
        // Dropdown Styles
        content.getChildren().add(new JCard("Estilos de Dropdown", createStyledDropdowns()));

        // Zoom Controls
        content.getChildren().add(new JCard("Controles de Zoom (JZoom)", createZoomDemo()));
    }

    private javafx.scene.Node createBasicDropdowns() {
        FlowPane container = new FlowPane(20, 16);
        
        JDropdown dd1 = new JDropdown("Opciones");
        dd1.addAction("Opción 1", e -> {});
        dd1.addAction("Opción 2", e -> {});
        dd1.addAction("Opción 3", e -> {});
        
        JDropdown dd2 = new JDropdown("Con Ícono", JIcon.SETTINGS.view());
        dd2.addAction("Configuración", e -> {});
        dd2.addAction("Preferencias", e -> {});
        dd2.addAction("Avanzado", e -> {});
        
        container.getChildren().addAll(dd1, dd2);
        return container;
    }

    private javafx.scene.Node createActionDropdowns() {
        FlowPane container = new FlowPane(20, 16);
        
        JDropdown dd = new JDropdown("Acciones", JIcon.LIST.view());
        dd.addAction("Nuevo Proyecto", e -> 
            JToast.show(getScene().getWindow(), "Nuevo proyecto creado", JToast.Type.SUCCESS, JToast.Position.BOTTOM_RIGHT, 3000));
        dd.addAction("Abrir Archivo", e -> 
            JToast.show(getScene().getWindow(), "Abrir archivo...", JToast.Type.INFO, JToast.Position.BOTTOM_RIGHT, 3000));
        dd.getItems().add(new SeparatorMenuItem());
        dd.addAction("Eliminar", e -> 
            JToast.show(getScene().getWindow(), "Elemento eliminado", JToast.Type.DANGER, JToast.Position.BOTTOM_RIGHT, 3000));
        
        container.getChildren().add(dd);
        return container;
    }

    private javafx.scene.Node createStyledDropdowns() {
        FlowPane container = new FlowPane(20, 16);
        
        JDropdown primary = new JDropdown("Primary");
        primary.addClass("btn-primary");
        primary.addAction("Acción 1", e -> {});
        primary.addAction("Acción 2", e -> {});
        
        JDropdown success = new JDropdown("Success");
        success.addClass("btn-success");
        success.addAction("Acción 1", e -> {});
        success.addAction("Acción 2", e -> {});
        
        JDropdown danger = new JDropdown("Danger");
        danger.addClass("btn-danger");
        danger.addAction("Acción 1", e -> {});
        danger.addAction("Acción 2", e -> {});
        
        JDropdown info = new JDropdown("Info");
        info.addClass("btn-info");
        info.addAction("Acción 1", e -> {});
        info.addAction("Acción 2", e -> {});
        
        JDropdown outline = new JDropdown("Outline");
        outline.addClass("btn-outline-primary");
        outline.addAction("Acción 1", e -> {});
        outline.addAction("Acción 2", e -> {});
        
        container.getChildren().addAll(primary, success, danger, info, outline);
        return container;
    }

    private javafx.scene.Node createZoomDemo() {
        VBox container = new VBox(20);
        
        Label description = new Label("JZoom provee controles de +/- para zoom. Conecta callbacks para implementar la lógica.");
        description.setWrapText(true);
        description.getStyleClass().addAll("text-base", "text-slate-500");
        
        // Zoom label
        Label zoomLabel = new Label("Zoom: 100%");
        zoomLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: -color-slate-700;");
        
        final double[] zoomLevel = {100.0};
        
        JZoom zoom = new JZoom();
        zoom.setOnZoomIn(() -> {
            zoomLevel[0] = Math.min(200, zoomLevel[0] + 10);
            zoomLabel.setText(String.format("Zoom: %.0f%%", zoomLevel[0]));
            zoomLabel.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-font-weight: bold; -fx-text-fill: -color-slate-700;",
                18.0 * zoomLevel[0] / 100.0));
        });
        zoom.setOnZoomOut(() -> {
            zoomLevel[0] = Math.max(50, zoomLevel[0] - 10);
            zoomLabel.setText(String.format("Zoom: %.0f%%", zoomLevel[0]));
            zoomLabel.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-font-weight: bold; -fx-text-fill: -color-slate-700;",
                18.0 * zoomLevel[0] / 100.0));
        });
        
        HBox zoomRow = new HBox(16);
        zoomRow.setAlignment(Pos.CENTER_LEFT);
        zoomRow.getChildren().addAll(zoom, zoomLabel);
        
        container.getChildren().addAll(description, zoomRow);
        return container;
    }
}
