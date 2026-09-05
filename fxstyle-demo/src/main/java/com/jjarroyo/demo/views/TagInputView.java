package com.jjarroyo.demo.views;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JChip;
import com.jjarroyo.components.JInput;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JTagInput;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TagInputView extends ScrollPane {

    public TagInputView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));

        setFitToWidth(true);
        setContent(content);

        // Cabecera de Página
        VBox pageHeader = new VBox();
        pageHeader.setSpacing(4);
        JLabel title = new JLabel("Tag Input / JTagInput")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Componente interactivo para ingresar múltiples etiquetas de texto con autowrap.")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        // Agregar las tarjetas de ejemplos
        content.getChildren().add(new JCard("1. Ejemplo Básico", createBasicDemo()));
        content.getChildren().add(new JCard("2. Personalización de Estilo (Reactivo)", createInteractiveDemo()));
        content.getChildren().add(new JCard("3. Límite de Etiquetas y Duplicados", createLimitsDemo()));
        content.getChildren().add(new JCard("4. Integración en Formulario", createFormDemo()));
    }

    private Node createBasicDemo() {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER_LEFT);

        JLabel info = new JLabel("Escribe una etiqueta y presiona Enter o Coma (,) para agregarla. Usa Backspace para borrar.")
            .withStyle("text-sm", "text-slate-500");

        JTagInput tagInput = new JTagInput();
        tagInput.setPlaceholder("Escribe algo y pulsa Enter...");
        tagInput.getTags().addAll("JavaFX", "TailwindCSS", "ModernUI");

        box.getChildren().addAll(info, tagInput);
        return box;
    }

    private Node createInteractiveDemo() {
        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER_LEFT);

        JTagInput tagInput = new JTagInput();
        tagInput.setPlaceholder("Escribe etiquetas personalizadas...");
        tagInput.getTags().addAll("Fútbol", "Baloncesto", "Tenis");

        // Controles interactivos
        HBox colorButtons = new HBox(8);
        colorButtons.setAlignment(Pos.CENTER_LEFT);
        JLabel colorLabel = new JLabel("Color:").withStyle("text-sm", "font-semibold");
        colorButtons.getChildren().add(colorLabel);

        for (JChip.ChipColor color : JChip.ChipColor.values()) {
            JButton btn = new JButton(color.name().toLowerCase());
            btn.addClass("btn-xs", "btn-secondary");
            btn.setOnAction(e -> tagInput.setTagColor(color));
            colorButtons.getChildren().add(btn);
        }

        HBox variantButtons = new HBox(8);
        variantButtons.setAlignment(Pos.CENTER_LEFT);
        JLabel variantLabel = new JLabel("Variante:").withStyle("text-sm", "font-semibold");
        variantButtons.getChildren().add(variantLabel);

        for (JChip.Variant variant : JChip.Variant.values()) {
            JButton btn = new JButton(variant.name().toLowerCase());
            btn.addClass("btn-xs", "btn-secondary");
            btn.setOnAction(e -> tagInput.setTagVariant(variant));
            variantButtons.getChildren().add(btn);
        }

        HBox sizeButtons = new HBox(8);
        sizeButtons.setAlignment(Pos.CENTER_LEFT);
        JLabel sizeLabel = new JLabel("Tamaño:").withStyle("text-sm", "font-semibold");
        sizeButtons.getChildren().add(sizeLabel);

        for (JChip.Size size : JChip.Size.values()) {
            JButton btn = new JButton(size.name().toLowerCase());
            btn.addClass("btn-xs", "btn-secondary");
            btn.setOnAction(e -> tagInput.setTagSize(size));
            sizeButtons.getChildren().add(btn);
        }

        box.getChildren().addAll(tagInput, colorButtons, variantButtons, sizeButtons);
        return box;
    }

    private Node createLimitsDemo() {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER_LEFT);

        JLabel limitLabel = new JLabel("Límite: Máximo 4 etiquetas. No se permiten duplicados.").withStyle("text-sm", "text-slate-500");
        JLabel countLabel = new JLabel("Etiquetas añadidas: 2 / 4").withStyle("text-sm", "font-semibold", "text-primary-600");

        JTagInput tagInput = new JTagInput();
        tagInput.setPlaceholder("Intenta meter duplicados o más de 4...");
        tagInput.setMaxTags(4);
        tagInput.setDuplicateAllowed(false);
        tagInput.setTagColor(JChip.ChipColor.DANGER);
        tagInput.setTagVariant(JChip.Variant.OUTLINED);
        tagInput.getTags().addAll("Fresa", "Mango");

        tagInput.getTags().addListener((ListChangeListener<String>) change -> {
            countLabel.setText("Etiquetas añadidas: " + tagInput.getTags().size() + " / 4");
        });

        box.getChildren().addAll(limitLabel, tagInput, countLabel);
        return box;
    }

    private Node createFormDemo() {
        VBox box = new VBox(16);
        box.setPadding(new Insets(10));
        box.getStyleClass().addAll("card-bordered", "p-4", "gap-4");
        
        JLabel formTitle = new JLabel("Crear Proyecto").withStyle("text-lg", "font-bold", "text-slate-800");

        // Input tradicional
        JInput nameInput = new JInput("Mi nuevo proyecto SaaS");
        VBox nameBox = nameInput.createWithLabel("Nombre del Proyecto", true);

        // JTagInput
        JTagInput techInput = new JTagInput();
        techInput.setPlaceholder("Ej. Java, Gradle, Postgres...");
        techInput.setTagColor(JChip.ChipColor.SUCCESS);
        techInput.getTags().addAll("JavaFX", "Maven");
        
        VBox techBox = new VBox(4);
        JLabel techLabel = new JLabel("Tecnologías / Dependencias").withStyle("form-label");
        techBox.getChildren().addAll(techLabel, techInput);

        JButton saveBtn = new JButton("Guardar Proyecto");
        saveBtn.addClass("btn-primary", "btn-md");
        saveBtn.setOnAction(e -> {
            System.out.println("Proyecto: " + nameInput.getText());
            System.out.println("Tecnologías: " + String.join(", ", techInput.getTags()));
        });

        box.getChildren().addAll(formTitle, nameBox, techBox, saveBtn);
        return box;
    }
}
