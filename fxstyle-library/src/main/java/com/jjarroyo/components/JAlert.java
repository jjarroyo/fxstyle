package com.jjarroyo.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class JAlert extends HBox {

    private Node icon;
    private Label titleLabel;
    private Label descriptionLabel;
    private StackPane iconContainer;
    private VBox contentContainer;
    private StackPane dismissContainer;
    private StackPane dismissButton;

    public JAlert() {
        this("", null);
        // Sin texto, oculto por defecto
        setVisible(false);
        setManaged(false);
    }

    public JAlert(String title) {
        this(title, null);
    }

    public JAlert(String title, String description) {
        getStyleClass().add("alert");
        setAlignment(Pos.CENTER_LEFT);
        
        // 1. Icon Container (Initialized empty)
        iconContainer = new StackPane();
        iconContainer.getStyleClass().add("alert-icon-container");
        // Only visible if icon is set
        iconContainer.setVisible(false);
        iconContainer.setManaged(false);

        // 2. Content Container
        titleLabel = new Label(title);
        titleLabel.getStyleClass().add("alert-title");
        
        contentContainer = new VBox(4);
        contentContainer.setAlignment(Pos.CENTER_LEFT);
        contentContainer.getChildren().add(titleLabel);
        
        if (description != null) {
            descriptionLabel = new Label(description);
            descriptionLabel.getStyleClass().add("alert-description");
            descriptionLabel.setWrapText(true);
            contentContainer.getChildren().add(descriptionLabel);
        }
        
        HBox.setHgrow(contentContainer, Priority.ALWAYS);

        // 3. Dismiss Container (Initialized empty)
        dismissContainer = new StackPane();
        dismissContainer.getStyleClass().add("alert-dismiss-container");
        dismissContainer.setVisible(false);
        dismissContainer.setManaged(false);

        getChildren().addAll(iconContainer, contentContainer, dismissContainer);
    }

    public JAlert setIcon(Node icon) {
        this.icon = icon;
        iconContainer.getChildren().clear();
        if (icon != null) {
            iconContainer.getChildren().add(icon);
            iconContainer.setVisible(true);
            iconContainer.setManaged(true);
            icon.getStyleClass().add("alert-icon");
        } else {
            iconContainer.setVisible(false);
            iconContainer.setManaged(false);
        }
        return this;
    }

    public JAlert setIcon(JIcon iconEnum) {
        if (iconEnum != null) {
            SVGPath svg = new SVGPath();
            svg.setContent(iconEnum.getPath());
            svg.setScaleX(0.8); 
            svg.setScaleY(0.8);
            // Actually, let's rely on CSS for size, but set content here
            return setIcon(svg);
        }
        return this;
    }

    public JAlert setDismissible(boolean dismissible) {
        dismissContainer.getChildren().clear();
        if (dismissible) {
            SVGPath closeIcon = new SVGPath();
            closeIcon.setContent("M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z");
            closeIcon.getStyleClass().add("alert-close-icon");
            
            dismissButton = new StackPane(closeIcon);
            dismissButton.getStyleClass().add("alert-dismiss-btn");
            dismissButton.setCursor(javafx.scene.Cursor.HAND);
            dismissButton.setOnMouseClicked(e -> {
                setVisible(false);
                setManaged(false);
            });
            
            dismissContainer.getChildren().add(dismissButton);
            dismissContainer.setVisible(true);
            dismissContainer.setManaged(true);
        } else {
            dismissContainer.setVisible(false);
            dismissContainer.setManaged(false);
        }
        return this;
    }

    /**
     * Establece el texto del alert y lo hace visible.
     */
    public JAlert setText(String text) {
        titleLabel.setText(text);
        if (text != null && !text.isEmpty()) {
            setVisible(true);
            setManaged(true);
        } else {
            setVisible(false);
            setManaged(false);
        }
        return this;
    }

    public JAlert addClass(String... styleClasses) {
        getStyleClass().addAll(styleClasses);
        return this;
    }
}

