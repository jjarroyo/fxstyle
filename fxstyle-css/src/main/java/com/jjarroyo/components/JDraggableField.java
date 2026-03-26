package com.jjarroyo.components;

import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class JDraggableField extends JSidebarItem {

    private final String fieldId;
    private final String fieldName;
    private final JIcon icon;

    public JDraggableField(String fieldId, String fieldName, JIcon icon) {
        super(fieldName, icon != null ? icon.view() : null);
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.icon = icon;

        initDraggable();
    }

    private void initDraggable() {
        getStyleClass().add("j-draggable-field");
        
        // Drag Handle (JSidebarItem already has a spacer at the end, so we just add the handle)

        // Drag and Drop Logic
        setOnDragDetected(event -> {
            Dragboard db = startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(fieldId);
            db.setContent(content);
            
            db.setDragView(this.snapshot(null, null));
            
            event.consume();
        });
    }

    public String getFieldId() {
        return fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }
}
