package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JToast;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class IconsView extends ScrollPane {

    public IconsView() {
        getStyleClass().add("j-scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));

        VBox content = new VBox(24);
        
        // Header
        VBox header = new VBox(8);
        Label title = new Label("Icons");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("A complete list of all available SVG icons.");
        subtitle.getStyleClass().add("page-subtitle");
        header.getChildren().addAll(title, subtitle);
        content.getChildren().add(header);

        // Grid
        JCard gridCard = new JCard("JIcon Library", "Usage: JIcon.NAME.view()");
        
        FlowPane iconGrid = new FlowPane(16, 16);
        iconGrid.setPadding(new Insets(24));
        
        for (JIcon icon : JIcon.values()) {
            VBox item = new VBox(8);
            item.setAlignment(Pos.CENTER);
            item.setPadding(new Insets(16));
            item.setStyle("-fx-background-color: -color-slate-50; -fx-background-radius: 8px; -fx-cursor: hand;");
            item.setPrefWidth(120);
            item.setPrefHeight(100);
            
            item.getChildren().add(icon.view());
            
            Label name = new Label(icon.name());
            name.setStyle("-fx-font-size: 11px; -fx-text-fill: -color-slate-500;");
            name.setWrapText(true);
            name.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            
            item.getChildren().add(name);
            
            // Hover effect
            item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: -color-slate-100; -fx-background-radius: 8px; -fx-cursor: hand;"));
            item.setOnMouseExited(e -> item.setStyle("-fx-background-color: -color-slate-50; -fx-background-radius: 8px; -fx-cursor: hand;"));
            
            // Click to copy (Toast)
            item.setOnMouseClicked(e -> {
               String copyText = "JIcon." + icon.name() + ".view()";
               javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
               javafx.scene.input.ClipboardContent contentCopy = new javafx.scene.input.ClipboardContent();
               contentCopy.putString(copyText);
               clipboard.setContent(contentCopy);
               
               JToast.show(getScene().getWindow(), "Copied to clipboard", copyText, JToast.Type.SUCCESS, JToast.Position.BOTTOM_CENTER, 2000);
            });
            
            iconGrid.getChildren().add(item);
        }
        
        gridCard.setBody(iconGrid);
        content.getChildren().add(gridCard);

        setContent(content);
    }
}


