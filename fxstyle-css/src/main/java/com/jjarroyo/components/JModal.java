package com.jjarroyo.components;

import com.jjarroyo.FxStyle;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class JModal extends StackPane {

    public enum Size {
        SMALL("modal-sm"),
        MEDIUM("modal-md"),
        LARGE("modal-lg"),
        FULL("modal-full");

        final String styleClass;
        Size(String styleClass) { this.styleClass = styleClass; }
    }

    private StackPane backdrop;
    private VBox dialogContainer;
    private VBox headerContainer;
    private VBox bodyContainer;
    private HBox footerContainer;
    private Size size = Size.MEDIUM;
    private boolean closeOnBackdropClick = true;

    public JModal() {
        init();
    }

    public JModal(Node body) {
        init();
        setBody(body);
    }
    
    public JModal(Node body, Size size) {
        this.size = size;
        init();
        setBody(body);
    }

    private void init() {
        getStyleClass().add("j-modal-root");
        setVisible(false);

        // 1. Backdrop
        backdrop = new StackPane();
        backdrop.getStyleClass().add("j-modal-backdrop");
        backdrop.setOnMouseClicked(e -> {
            if (closeOnBackdropClick) close();
        });

        // 2. Containers for Structure
        headerContainer = new VBox();
        headerContainer.getStyleClass().add("modal-header");
        headerContainer.setVisible(false);
        headerContainer.setManaged(false);

        bodyContainer = new VBox();
        bodyContainer.getStyleClass().add("modal-body");
        VBox.setVgrow(bodyContainer, javafx.scene.layout.Priority.ALWAYS);

        footerContainer = new HBox();
        footerContainer.getStyleClass().add("modal-footer");
        footerContainer.setVisible(false);
        footerContainer.setManaged(false);

        // 2. Dialog Container
        dialogContainer = new VBox();
        dialogContainer.getStyleClass().add("j-modal-dialog");
        dialogContainer.getChildren().addAll(headerContainer, bodyContainer, footerContainer);

        // Clip so children respect rounded corners
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
        clip.setArcWidth(24);
        clip.setArcHeight(24);
        clip.widthProperty().bind(dialogContainer.widthProperty());
        clip.heightProperty().bind(dialogContainer.heightProperty());
        dialogContainer.setClip(clip);

        // Add children
        getChildren().addAll(backdrop, dialogContainer);
        setAlignment(Pos.CENTER);
        
        // Apply initial size
        setSize(this.size);
    }

    public JModal setHeader(Node... nodes) {
        headerContainer.getChildren().setAll(nodes);
        boolean hasContent = nodes.length > 0;
        headerContainer.setVisible(hasContent);
        headerContainer.setManaged(hasContent);
        return this;
    }

    public JModal setBody(Node node) {
        bodyContainer.getChildren().setAll(node);
        if (node instanceof javafx.scene.layout.Region) {
            VBox.setVgrow(node, javafx.scene.layout.Priority.ALWAYS);
        }
        return this;
    }

    public JModal setFooter(Node... nodes) {
        footerContainer.getChildren().setAll(nodes);
        boolean hasContent = nodes.length > 0;
        footerContainer.setVisible(hasContent);
        footerContainer.setManaged(hasContent);
        return this;
    }

    public void show() {
        StackPane root = FxStyle.getModalContainer();
        if (root == null) return;
        
        root.getChildren().add(this);
        this.setVisible(true);
        
        // Animations
        playEntranceAnimation();
    }

    public void close() {
        playExitAnimation(() -> {
            StackPane root = FxStyle.getModalContainer();
            if (root != null) {
                root.getChildren().remove(this);
            }
        });
    }

    private void playEntranceAnimation() {
        // Backdrop Fade In
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), backdrop);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Dialog Scale + Fade
        ScaleTransition scale = new ScaleTransition(Duration.millis(300), dialogContainer);
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1.0);
        scale.setToY(1.0);
        
        FadeTransition dialogFade = new FadeTransition(Duration.millis(300), dialogContainer);
        dialogFade.setFromValue(0);
        dialogFade.setToValue(1);
        
        ParallelTransition pt = new ParallelTransition(fadeIn, scale, dialogFade);
        pt.play();
    }

    private void playExitAnimation(Runnable onFinished) {
        // Backdrop Fade Out
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), backdrop);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        // Dialog Scale + Fade
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), dialogContainer);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(0.9);
        scale.setToY(0.9);
        
        FadeTransition dialogFade = new FadeTransition(Duration.millis(200), dialogContainer);
        dialogFade.setFromValue(1);
        dialogFade.setToValue(0);

        ParallelTransition pt = new ParallelTransition(fadeOut, scale, dialogFade);
        pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }
    
    // Fluent API
    public JModal setCloseOnBackdropClick(boolean close) {
        this.closeOnBackdropClick = close;
        return this;
    }
    
    public JModal setSize(Size size) {
        this.size = size;
        dialogContainer.getStyleClass().removeAll(Size.SMALL.styleClass, Size.MEDIUM.styleClass, Size.LARGE.styleClass, Size.FULL.styleClass);
        dialogContainer.getStyleClass().add(size.styleClass);
        
        // Handle Full Screen Sizing logic (CSS percentages are unreliable in JavaFX)
        if (size == Size.FULL) {
            dialogContainer.prefWidthProperty().bind(widthProperty().multiply(0.95));
            dialogContainer.prefHeightProperty().bind(heightProperty().multiply(0.95));
        } else {
            dialogContainer.prefWidthProperty().unbind();
            dialogContainer.prefHeightProperty().unbind();
            dialogContainer.setPrefWidth(javafx.scene.layout.Region.USE_COMPUTED_SIZE);
            dialogContainer.setPrefHeight(javafx.scene.layout.Region.USE_COMPUTED_SIZE);
        }
        
        return this;
    }
}

