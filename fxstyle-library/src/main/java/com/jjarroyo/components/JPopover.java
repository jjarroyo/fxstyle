package com.jjarroyo.components;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.util.Duration;

public class JPopover extends Popup {

    public enum Position { TOP, RIGHT, BOTTOM, LEFT }

    private final VBox container;
    private final Polygon arrow;
    private final VBox contentBox;
    
    private Position position = Position.TOP;
    
    // Content Parts
    private final Label titleLabel;
    private final Label bodyLabel;
    private final HBox footerBox;
    
    public JPopover() {
        // Root Container (Bubbles + Arrow)
        container = new VBox();  
        container.getStyleClass().add("j-popover");
        container.setMouseTransparent(false);
        container.setAlignment(javafx.geometry.Pos.CENTER); // Fix: Center arrow relative to content
        
        // Content Box (The visual card)
        contentBox = new VBox();
        contentBox.getStyleClass().add("j-popover-content");
        
        // Arrow
        arrow = new Polygon(0, 0, 10, 10, 20, 0); // Down pointing arrow shape logic varies by orientation
        arrow.getStyleClass().add("j-popover-arrow");
        
        // Assemble initial structure (Default: Arrow Top or Bottom? JPopover structure changes based on pos)
        // We'll dynamic assemble in show() or configure()
        
        // Init Content Parts
        titleLabel = new Label();
        titleLabel.getStyleClass().add("j-popover-title");
        titleLabel.setWrapText(true);
        titleLabel.setManaged(false);
        titleLabel.setVisible(false);
        
        bodyLabel = new Label();
        bodyLabel.getStyleClass().add("j-popover-text");
        bodyLabel.setWrapText(true);
        
        footerBox = new HBox();
        footerBox.getStyleClass().add("j-popover-footer");
        footerBox.setManaged(false);
        footerBox.setVisible(false);
        
        contentBox.getChildren().addAll(titleLabel, bodyLabel, footerBox);
        getContent().add(container);
        setAutoHide(true);
    }
    
    public JPopover setTitle(String title) {
        titleLabel.setText(title);
        boolean hasText = title != null && !title.isEmpty();
        titleLabel.setVisible(hasText);
        titleLabel.setManaged(hasText);
        return this;
    }
    
    public JPopover setBody(String text) {
        bodyLabel.setText(text);
        return this;
    }
    
    public JPopover setContentNode(Node content) {
        contentBox.getChildren().clear();
        contentBox.getChildren().add(content);
        return this;
    }
    
    
    // Confirmation Mode Helper
    public JPopover setConfirmationMode(String yesText, String noText, Runnable onYes) {
        footerBox.getChildren().clear();
        
        Button btnNo = new Button(noText);
        btnNo.getStyleClass().addAll("btn", "btn-sm", "btn-light-danger"); // Or light-secondary
        btnNo.setOnAction(e -> hide());
        
        Button btnYes = new Button(yesText);
        btnYes.getStyleClass().addAll("btn", "btn-sm", "btn-primary");
        btnYes.setOnAction(e -> {
            hide();
            if (onYes != null) onYes.run();
        });
        
        footerBox.getChildren().addAll(btnNo, btnYes);
        footerBox.setVisible(true);
        footerBox.setManaged(true);
        return this;
    }
    
    public JPopover setPosition(Position pos) {
        this.position = pos;
        return this;
    }

    public void show(Node target) {
        // 1. Re-assemble container based on orientation
        // We need the correct container type (VBox for Vertical, HBox for Horizontal)
        javafx.scene.layout.Pane activeContainer;
        
        arrow.getPoints().clear();
        
        // Auto-detect dark mode from target context
        boolean isDark = false;
        Node current = target;
        while (current != null) {
            if (current.getStyleClass().contains("dark")) {
                isDark = true;
                break;
            }
            current = current.getParent();
        }
        if (!isDark && target != null && target.getScene() != null && target.getScene().getRoot() != null) {
            if (target.getScene().getRoot().getStyleClass().contains("dark")) {
                isDark = true;
            }
        }

        if (position == Position.TOP || position == Position.BOTTOM) {
            VBox vBox = new VBox();            
            vBox.getStyleClass().add("j-popover");
            vBox.setStyle("-fx-background-color: transparent;"); // Force transparency to avoid flash
            vBox.setMouseTransparent(false);
            vBox.setAlignment(javafx.geometry.Pos.CENTER);
            
            if (isDark) {
                vBox.getStyleClass().add("j-popover-dark");
                vBox.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 8px;"); // Direct hex to be sure
            }
            
            if (position == Position.TOP) {
                arrow.getPoints().addAll(0d, 0d, 10d, 10d, 20d, 0d); // Point Down
                vBox.getChildren().addAll(contentBox, arrow);
            } else {
                arrow.getPoints().addAll(0d, 10d, 10d, 0d, 20d, 10d); // Point Up
                vBox.getChildren().addAll(arrow, contentBox);
            }
            activeContainer = vBox;
        } else {
            HBox hBox = new HBox();          
            hBox.getStyleClass().add("j-popover");
            hBox.setStyle("-fx-background-color: transparent;"); // Force transparency to avoid flash
            hBox.setMouseTransparent(false);
            hBox.setAlignment(javafx.geometry.Pos.CENTER);
            
            if (isDark) {
                hBox.getStyleClass().add("j-popover-dark");
                hBox.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 8px;"); // Direct hex to be sure
            }
            
            if (position == Position.LEFT) {
                arrow.getPoints().addAll(0d, 0d, 10d, 10d, 0d, 20d); // Point Right
                hBox.getChildren().addAll(contentBox, arrow);
            } else { // RIGHT
                arrow.getPoints().addAll(10d, 0d, 0d, 10d, 10d, 20d); // Point Left
                hBox.getChildren().addAll(arrow, contentBox);
            }
            activeContainer = hBox;
        }
        
        // 2. Prepare style context BEFORE adding to scene
        if (target != null && target.getScene() != null) {
            activeContainer.getStylesheets().setAll(target.getScene().getStylesheets());
        }
        
        // Ensure container is transparent at Java level
        activeContainer.setBackground(javafx.scene.layout.Background.EMPTY);
        activeContainer.setStyle("-fx-background-color: transparent !important;");
        activeContainer.setOpacity(0);
        activeContainer.applyCss();
        activeContainer.layout();

        getContent().clear();
        getContent().add(activeContainer);
        
        // 3. Calculate and Set Initial Position
        Window window = target.getScene().getWindow();
        Bounds bounds = target.localToScreen(target.getBoundsInLocal());
        
        // Temporarily show with 0 opacity to get dimensions
        setOpacity(0);
        super.show(window, 0, 0); // Show at 0,0 first
        
        // Now we have dimensions
        double width = activeContainer.getWidth();
        double height = activeContainer.getHeight();
        
        // Aggressive transparency fix (standard JavaFX remedy for Popup flash)
        javafx.application.Platform.runLater(() -> {
            if (getScene() != null) {
                getScene().setFill(javafx.scene.paint.Color.TRANSPARENT);
                if (getScene().getRoot() != null) {
                    getScene().getRoot().setStyle("-fx-background-color: transparent !important;");
                }
            }
        });
        
        double x = 0;
        double y = 0;
        
        if (position == Position.TOP) {
            x = bounds.getMinX() + (bounds.getWidth() / 2) - (width / 2);
            y = bounds.getMinY() - height - 10;
        } else if (position == Position.BOTTOM) {
            x = bounds.getMinX() + (bounds.getWidth() / 2) - (width / 2);
            y = bounds.getMaxY() + 10;
        } else if (position == Position.LEFT) {
            x = bounds.getMinX() - width - 10;
            y = bounds.getMinY() + (bounds.getHeight() / 2) - (height / 2);
        } else if (position == Position.RIGHT) {
            x = bounds.getMaxX() + 10;
            y = bounds.getMinY() + (bounds.getHeight() / 2) - (height / 2);
        }
        
        setAnchorX(x);
        setAnchorY(y);
        
        // Animation
        playEntranceAnimation(activeContainer);
    }
    
    private void playEntranceAnimation(Node node) {
        node.setOpacity(0); // Ensure it starts at 0
        FadeTransition fade = new FadeTransition(Duration.millis(200), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), node);
        scale.setFromX(0.95);
        scale.setFromY(0.95);
        scale.setToX(1.0);
        scale.setToY(1.0);
        
        ParallelTransition pt = new ParallelTransition(fade, scale);
        pt.setOnFinished(e -> setOpacity(1)); // Make popup itself opaque only after animation starts
        pt.play();
    }
}

