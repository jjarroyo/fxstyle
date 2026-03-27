package com.jjarroyo.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class JZoom extends HBox {

    private final JButton minusBtn;
    private final JButton plusBtn;
    private Runnable onZoomIn;
    private Runnable onZoomOut;

    public JZoom() {
        getStyleClass().add("j-zoom");
        setSpacing(6);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(0, 12, 0, 12));
        setPrefHeight(32);
        setMinHeight(32);
        setMaxHeight(32);

        minusBtn = createRoundButton(JIcon.MINUS);
        plusBtn = createRoundButton(JIcon.PLUS);

        minusBtn.setOnAction(e -> {
            if (onZoomOut != null) onZoomOut.run();
        });

        plusBtn.setOnAction(e -> {
            if (onZoomIn != null) onZoomIn.run();
        });

        getChildren().addAll(minusBtn, plusBtn);
    }

    private JButton createRoundButton(JIcon icon) {
        JButton btn = new JButton(null, icon);
        btn.setPrefSize(24, 24);
        btn.setMinSize(24, 24);
        btn.setMaxSize(24, 24);
        
        // Always highlighted round style
        btn.setStyle("-fx-background-radius: 50; -fx-padding: 0; -fx-background-color: rgba(203, 213, 225, 0.4);");
        
        // Hover deepened effect
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-radius: 50; -fx-padding: 0; -fx-background-color: rgba(203, 213, 225, 0.7);"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-radius: 50; -fx-padding: 0; -fx-background-color: rgba(203, 213, 225, 0.4);"));
        
        return btn;
    }

    public void setOnZoomIn(Runnable onZoomIn) {
        this.onZoomIn = onZoomIn;
    }

    public void setOnZoomOut(Runnable onZoomOut) {
        this.onZoomOut = onZoomOut;
    }
}
