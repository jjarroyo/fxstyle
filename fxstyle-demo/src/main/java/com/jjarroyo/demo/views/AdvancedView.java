package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JDesignCanvas;
import com.jjarroyo.components.JDraggableField;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JSqlEditor;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AdvancedView extends ScrollPane {

    public AdvancedView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));
        
        setFitToWidth(true);
        setContent(content);
        
        // Page Title
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Componentes Avanzados")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Editor SQL, Canvas de diseño y campos arrastrables")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        // SQL Editor
        content.getChildren().add(createSqlEditorCard());

        // Design Canvas + Draggable Fields
        content.getChildren().add(createDesignCanvasCard());
    }

    private javafx.scene.Node createSqlEditorCard() {
        JCard card = new JCard("SQL Editor", "Editor con resaltado de sintaxis, autocompletado y validación en tiempo real");
        
        VBox body = new VBox(12);
        body.setPadding(new Insets(16));
        
        Label hint = new Label("Ctrl+Shift+F: Formatear SQL  •  Ctrl+Space: Autocompletar  •  Ctrl+/: Comentar");
        hint.setWrapText(true);
        hint.setStyle("-fx-font-size: 12px; -fx-text-fill: -color-slate-400;");
        
        JSqlEditor editor = new JSqlEditor();
        editor.setPrefHeight(300);
        editor.setMinHeight(250);
        editor.setSql(
            "-- Ejemplo de SQL\n" +
            "CREATE TABLE users (\n" +
            "    id SERIAL PRIMARY KEY,\n" +
            "    name VARCHAR(100) NOT NULL,\n" +
            "    email VARCHAR(255) UNIQUE,\n" +
            "    role VARCHAR(50) DEFAULT 'user',\n" +
            "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
            ");\n\n" +
            "SELECT u.name, u.email, COUNT(o.id) AS total_orders\n" +
            "FROM users u\n" +
            "LEFT JOIN orders o ON o.user_id = u.id\n" +
            "WHERE u.role = 'admin'\n" +
            "GROUP BY u.name, u.email\n" +
            "HAVING COUNT(o.id) > 5\n" +
            "ORDER BY total_orders DESC\n" +
            "LIMIT 10;"
        );
        
        VBox.setVgrow(editor, Priority.ALWAYS);
        body.getChildren().addAll(hint, editor);
        card.setBody(body);
        return card;
    }

    private javafx.scene.Node createDesignCanvasCard() {
        JCard card = new JCard("Design Canvas", "Canvas con drag & drop y campos arrastrables");
        
        VBox body = new VBox(12);
        body.setPadding(new Insets(16));
        
        Label description = new Label("Arrastra los campos de la izquierda hacia las áreas del canvas para asignarlos.");
        description.setWrapText(true);
        description.getStyleClass().addAll("text-sm", "text-slate-500");
        
        HBox layout = new HBox(16);
        
        // Draggable fields panel
        VBox fieldsPanel = new VBox(8);
        fieldsPanel.setPadding(new Insets(12));
        fieldsPanel.setStyle(
            "-fx-background-color: -color-slate-50;" +
            "-fx-border-color: -color-slate-200;" +
            "-fx-border-radius: 8; -fx-background-radius: 8;"
        );
        fieldsPanel.setMinWidth(180);
        fieldsPanel.setPrefWidth(200);
        
        Label fieldsTitle = new Label("Campos Disponibles");
        fieldsTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: -color-slate-700;");
        fieldsPanel.getChildren().add(fieldsTitle);
        
        fieldsPanel.getChildren().addAll(
            new JDraggableField("nombre", "Nombre", JIcon.USER),
            new JDraggableField("email", "Email", JIcon.CHAT),
            new JDraggableField("telefono", "Teléfono", JIcon.PHONE),
            new JDraggableField("direccion", "Dirección", JIcon.LOCATION_ON),
            new JDraggableField("fecha", "Fecha", JIcon.CALENDAR),
            new JDraggableField("codigo", "Código", JIcon.TAG)
        );
        
        // Design canvas
        JDesignCanvas canvas = new JDesignCanvas();
        canvas.setSupportedLines(4); // Create 4 drop slots
        canvas.setPrefHeight(350);
        canvas.setMinHeight(300);
        HBox.setHgrow(canvas, Priority.ALWAYS);
        
        layout.getChildren().addAll(fieldsPanel, canvas);
        
        body.getChildren().addAll(description, layout);
        card.setBody(body);
        return card;
    }
}
