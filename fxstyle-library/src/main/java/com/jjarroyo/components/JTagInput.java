package com.jjarroyo.components;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;

/**
 * JTagInput — Componente de entrada para agregar y gestionar etiquetas (tags/chips) interactivos.
 * Inspirado en Tailwind y shadcn/ui.
 *
 * <pre>
 * JTagInput tagInput = new JTagInput();
 * tagInput.setPlaceholder("Escribe y pulsa Enter...");
 * tagInput.getTags().addAll("JavaFX", "CSS");
 * </pre>
 */
public class JTagInput extends FlowPane {

    private final TextField inputField = new TextField();
    private final ObservableList<String> tags = FXCollections.observableArrayList();
    
    // Configuración del chip
    private JChip.ChipColor tagColor = JChip.ChipColor.PRIMARY;
    private JChip.Variant tagVariant = JChip.Variant.SOFT;
    private JChip.Size tagSize = JChip.Size.SM;
    
    private String placeholder = "Añadir etiqueta...";
    private int maxTags = -1; // -1 significa ilimitado
    private boolean duplicateAllowed = false;

    public JTagInput() {
        super();
        init();
    }

    private void init() {
        // Estilos del contenedor principal
        getStyleClass().addAll("j-tag-input");
        setHgap(6);
        setVgap(6);
        setAlignment(Pos.CENTER_LEFT);

        // Estilos del input interno
        inputField.getStyleClass().addAll("j-tag-input-field", "form-input-transparent");
        inputField.setPromptText(placeholder);
        inputField.setPrefWidth(120);

        // Foco visual interactivo
        inputField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                getStyleClass().add("j-tag-input-focused");
            } else {
                getStyleClass().remove("j-tag-input-focused");
            }
        });

        // Asegurar que hacer clic en el contenedor transfiera el foco al input
        this.setOnMouseClicked(e -> {
            if (e.getTarget() == this) {
                inputField.requestFocus();
            }
        });

        // Captura de eventos de teclado en el input
        inputField.setOnKeyPressed(e -> {
            String text = inputField.getText().trim();
            
            // Añadir tag al pulsar Enter o Coma
            if (e.getCode() == KeyCode.ENTER) {
                processAndAddTag(text);
                e.consume();
            } else if (e.getCode() == KeyCode.BACK_SPACE && text.isEmpty() && !tags.isEmpty()) {
                // Eliminar el último tag si el input está vacío y se pulsa Backspace
                tags.remove(tags.size() - 1);
                e.consume();
            }
        });

        // También interceptar la coma en texto tipeado
        inputField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.endsWith(",")) {
                String cleanText = newText.substring(0, newText.length() - 1).trim();
                processAndAddTag(cleanText);
            }
        });

        // Escuchar cambios en la lista de etiquetas para reconstruir el layout
        tags.addListener((ListChangeListener<String>) c -> rebuild());

        // Carga inicial
        rebuild();
    }

    private void processAndAddTag(String tagText) {
        if (tagText.isEmpty()) return;
        
        // Validar límite máximo
        if (maxTags > 0 && tags.size() >= maxTags) {
            inputField.clear();
            return;
        }

        // Validar duplicados
        if (!duplicateAllowed && tags.contains(tagText)) {
            inputField.clear();
            return;
        }

        tags.add(tagText);
        inputField.clear();
    }

    private void rebuild() {
        getChildren().clear();

        // Agregar los chips correspondientes a las etiquetas
        for (String tag : tags) {
            JChip chip = new JChip(tag);
            chip.setColor(tagColor)
                .setVariant(tagVariant)
                .setChipSize(tagSize)
                .setDismissible(true)
                .setOnDismiss(() -> tags.remove(tag));
            getChildren().add(chip);
        }

        // Agregar el campo de entrada al final de los chips
        getChildren().add(inputField);

        // Actualizar el placeholder si hay o no etiquetas
        if (tags.isEmpty()) {
            inputField.setPromptText(placeholder);
            inputField.setPrefWidth(120);
        } else {
            inputField.setPromptText("");
            inputField.setPrefWidth(80); // Reducir ancho cuando ya hay elementos
        }
    }

    // ─── API PÚBLICA ─────────────────────────────────────────────────────────────

    public ObservableList<String> getTags() {
        return tags;
    }

    public JTagInput setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (tags.isEmpty()) {
            inputField.setPromptText(placeholder);
        }
        return this;
    }

    public JTagInput setMaxTags(int maxTags) {
        this.maxTags = maxTags;
        return this;
    }

    public JTagInput setDuplicateAllowed(boolean duplicateAllowed) {
        this.duplicateAllowed = duplicateAllowed;
        return this;
    }

    public JTagInput setTagColor(JChip.ChipColor color) {
        this.tagColor = color;
        rebuild();
        return this;
    }

    public JTagInput setTagVariant(JChip.Variant variant) {
        this.tagVariant = variant;
        rebuild();
        return this;
    }

    public JTagInput setTagSize(JChip.Size size) {
        this.tagSize = size;
        rebuild();
        return this;
    }

    public void clear() {
        tags.clear();
        inputField.clear();
    }
}
