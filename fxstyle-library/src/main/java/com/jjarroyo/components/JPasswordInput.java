package com.jjarroyo.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class JPasswordInput extends StackPane {

    private PasswordField passwordField;
    private TextField textField;
    private Button toggleButton;
    private boolean isVisible = false;

    public JPasswordInput() {
        this("");
    }

    public JPasswordInput(String promptText) {
        getStyleClass().add("input-wrapper");
        
        passwordField = new PasswordField();
        passwordField.setPromptText(promptText);
        passwordField.getStyleClass().add("form-input");
        passwordField.setStyle("-fx-background-color: transparent; -fx-border-width: 0;"); // Override default input style to fit wrapper
        
        textField = new TextField();
        textField.setPromptText(promptText);
        textField.getStyleClass().add("form-input");
        textField.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        textField.setVisible(false);
        textField.setManaged(false);

        // Bind text properties
        textField.textProperty().bindBidirectional(passwordField.textProperty());

        toggleButton = new Button();
        toggleButton.getStyleClass().add("password-toggle-btn");
        updateIcon();
        
        toggleButton.setOnAction(e -> toggleVisibility());

        // Layout
        // we use a StackPane for the inputs to overlay them, but we need the button on the right.
        // Better structure: StackPane ( InputLayer, ButtonLayer(RightAligned) )
        // OR: HBox ( Input(Grow), Button ) inside the wrapper.
        
        HBox contentString = new HBox();
        contentString.setAlignment(Pos.CENTER_LEFT);
        
        // Wrap inputs in a StackPane so they occupy same space
        StackPane inputStack = new StackPane(textField, passwordField);
        HBox.setHgrow(inputStack, javafx.scene.layout.Priority.ALWAYS);
        
        contentString.getChildren().addAll(inputStack, toggleButton);
        
        getChildren().add(contentString);

        // JavaFX no soporta :focused-within en CSS.
        // Usamos listeners para toggle la clase 'input-focused' en el wrapper.
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> updateFocusStyle());
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> updateFocusStyle());
    }

    private void updateFocusStyle() {
        if (passwordField.isFocused() || textField.isFocused()) {
            if (!getStyleClass().contains("input-focused")) {
                getStyleClass().add("input-focused");
            }
        } else {
            getStyleClass().remove("input-focused");
        }
    }
    
    private void toggleVisibility() {
        isVisible = !isVisible;
        if (isVisible) {
            textField.setVisible(true);
            textField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
        } else {
            textField.setVisible(false);
            textField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
        }
        updateIcon();
    }

    private void updateIcon() {
        SVGPath icon = new SVGPath();
        if (isVisible) {
             // Eye Off Icon
             icon.setContent("M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"); 
        } else {
             // Eye Icon
             icon.setContent("M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"); 
             // Wait, I reused the same path. Let me fix the Eye Off path.
             // Actually, for simplicity let's just use the same "Eye" for now or use JIcon if available. 
             // I'll grab the paths from JIcon if they exist, or define them here properly.
        }
        // Let's use a "slash" for off
        if (!isVisible) {
             icon.setContent("M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z");
        } else {
             // Slash icon (Eye Off) - simplified
             icon.setContent("M11.83 9L15 12.17V12c0-1.66-1.34-3-3-3h-.17zm-4.3.4l1.55 1.55c-.05.21-.08.43-.08.65 0 1.66 1.34 3 3 3 .22 0 .44-.03.65-.08l1.55 1.55c-.67.33-1.41.53-2.2.53-2.76 0-5-2.24-5-5 0-.79.2-1.53.53-2.2zm-2.53-1.14L3 10.25c1.1 2.2 3.45 3.8 6.25 4.55l-2.75-2.75zM12 4.5c5 0 9.27 3.11 11 7.5-1.1 2.8-3.48 4.88-6.42 5.96L14.7 16.08c1.62-.27 3.14-1 4.34-2.12-1.4-1.91-3.69-3.23-6.29-3.42-2.34-.17-4.46.72-5.96 2.15l-1.92-1.92C6.4 10.05 8.97 9 12 9c0-.18.02-.36.04-.54C10.6 8.78 9.32 9.4 8.27 10.25L5.7 7.68C7.4 6.13 9.61 5.09 12 4.5z");
        }
        
        icon.getStyleClass().add("icon-svg");
        icon.setScaleX(0.8);
        icon.setScaleY(0.8);
        icon.setStyle("-fx-fill: -color-slate-400;");
        toggleButton.setGraphic(icon);
    }
    
    public VBox createWithLabel(String labelText, boolean required) {
         VBox container = new VBox();
        container.setSpacing(4);
        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");
        
        if (required) {
            Label req = new Label("*");
            req.getStyleClass().add("required-mark");
            HBox labelBox = new HBox(label, req);
            labelBox.setAlignment(Pos.CENTER_LEFT);
            container.getChildren().add(labelBox);
        } else {
            container.getChildren().add(label);
        }
        container.getChildren().add(this);
        return container;
    }

    /**
     * Applies the modern style to internal fields.
     */
    public JPasswordInput setModern(boolean modern) {
        if (modern) {
            if (!getStyleClass().contains("form-input-modern")) {
                getStyleClass().add("form-input-modern");
            }
            passwordField.getStyleClass().add("form-input-modern");
            textField.getStyleClass().add("form-input-modern");
        } else {
            getStyleClass().remove("form-input-modern");
            passwordField.getStyleClass().remove("form-input-modern");
            textField.getStyleClass().remove("form-input-modern");
        }
        return this;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }
}

