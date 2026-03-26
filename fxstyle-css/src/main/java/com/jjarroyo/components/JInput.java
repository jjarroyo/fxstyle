package com.jjarroyo.components;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class JInput extends TextField {

    public JInput() {
        super();
        init();
    }

    public JInput(String promptText) {
        super();
        setPromptText(promptText);
        init();
    }

    private void init() {
        getStyleClass().add("form-input");
    }

    public JInput addClass(String... styleClasses) {
        getStyleClass().addAll(styleClasses);
        return this;
    }

    /**
     * Creates a VBox containing a Label (with optional required mark) and this Input.
     */
    public VBox createWithLabel(String labelText, boolean required) {
        VBox container = new VBox();
        container.setSpacing(4); // Adjust spacing as needed

        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");
        
        if (required) {
            Label req = new Label("*");
            req.getStyleClass().add("required-mark");
            // Use a FlowPane or HBox if you want the * on the same line right after text
            // For simplicity here, appending * to text or using a graphic might be easier, 
            // but styling parts of text in JavaFX Label is tricky without TextFlow.
            // Let's use a TextFlow approach or simple concatenation if CSS handles it.
            // Actually, simplest is HBox for the label line.
            javafx.scene.layout.HBox labelBox = new javafx.scene.layout.HBox(label, req);
            labelBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
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
    public JInput setStatus(String styleClass) {
        getStyleClass().removeAll("form-input-success", "form-input-danger", "form-input-warning", "form-input-dark");
        if (styleClass != null && !styleClass.isEmpty()) {
            getStyleClass().add(styleClass);
        }
        return this;
    }

    /**
     * Applies the modern style with more padding and rounded corners.
     */
    public JInput setModern(boolean modern) {
        if (modern) {
            if (!getStyleClass().contains("form-input-modern")) {
                getStyleClass().add("form-input-modern");
            }
        } else {
            getStyleClass().remove("form-input-modern");
        }
        return this;
    }
}

