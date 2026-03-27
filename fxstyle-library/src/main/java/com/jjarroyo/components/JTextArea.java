package com.jjarroyo.components;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class JTextArea extends TextArea {

    public JTextArea() {
        super();
        init();
    }

    public JTextArea(String promptText) {
        super();
        setPromptText(promptText);
        init();
    }

    private void init() {
        getStyleClass().add("j-text-area");
        getStyleClass().add("form-input"); // Reuse common base styles if possible, but might need overrides
        setWrapText(true);
        setPrefRowCount(3); // Default height
    }

    public JTextArea addClass(String... styleClasses) {
        getStyleClass().addAll(styleClasses);
        return this;
    }

    /**
     * Creates a VBox containing a Label (with optional required mark) and this Input.
     */
    public VBox createWithLabel(String labelText, boolean required) {
        VBox container = new VBox();
        container.setSpacing(4);

        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");
        
        if (required) {
            javafx.scene.layout.HBox labelBox = new javafx.scene.layout.HBox();
            labelBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            Label req = new Label("*");
            req.getStyleClass().add("required-mark");
            labelBox.getChildren().addAll(label, req);
            container.getChildren().add(labelBox);
        } else {
            container.getChildren().add(label);
        }

        container.getChildren().add(this);
        return container;
    }

    /**
     * Sets the validation status of the input.
     * Valid values: "form-input-success", "form-input-danger", "form-input-warning", "form-input-dark"
     * Passing null or empty string removes all validation styles.
     */
    public JTextArea setStatus(String styleClass) {
        getStyleClass().removeAll("form-input-success", "form-input-danger", "form-input-warning", "form-input-dark");
        if (styleClass != null && !styleClass.isEmpty()) {
            getStyleClass().add(styleClass);
        }
        return this;
    }
}
