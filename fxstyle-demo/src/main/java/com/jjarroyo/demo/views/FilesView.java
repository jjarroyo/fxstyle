package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JFile;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.FlowPane;

public class FilesView extends ScrollPane {

    public FilesView() {
        getStyleClass().add("j-scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));
        
        VBox content = new VBox();
        content.setSpacing(24);
        
        // Header
        VBox header = new VBox();
        header.setSpacing(8);
        Label title = new Label("File Uploads");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Advanced file upload controls with previews and validation.");
        subtitle.getStyleClass().add("page-subtitle");
        header.getChildren().addAll(title, subtitle);
        content.getChildren().add(header);
        
        // 1. Basic Examples
        HBox row1 = new HBox();
        row1.setSpacing(24);
        
        JCard cardWeb = createCard("Basic Upload", "Simple button mode.");
        JFile fileBtn = new JFile(JFile.Mode.BUTTON);
        fileBtn.getValidExtensions().addAll(".png", ".jpg");
        cardWeb.setBody(fileBtn);
        HBox.setHgrow(cardWeb, Priority.ALWAYS);
        
        JCard cardAvatar = createCard("Avatar Upload", "Circular preview with edit controls.");
        JFile fileAvatar = new JFile(JFile.Mode.AVATAR);
        fileAvatar.setShowEdit(true);
        fileAvatar.setShowRemove(true);
        cardAvatar.setBody(fileAvatar);
        HBox.setHgrow(cardAvatar, Priority.ALWAYS);
        
        JCard cardSquare = createCard("Square Preview", "Rounded square preview.");
        JFile fileSquare = new JFile(JFile.Mode.SQUARE);
        fileSquare.setShowRemove(true);
        cardSquare.setBody(fileSquare);
        HBox.setHgrow(cardSquare, Priority.ALWAYS);

        row1.getChildren().addAll(cardWeb, cardAvatar, cardSquare);
        content.getChildren().add(row1);
        
        // 2. Drop Zone
        JCard cardDrop = createCard("Drop Zone", "Drag and drop area with custom content.");
        JFile fileDrop = new JFile(JFile.Mode.DROP_ZONE);
        fileDrop.setMaxFileCount(5);
        fileDrop.getValidExtensions().addAll(".png", ".jpg", ".pdf");
        cardDrop.setBody(fileDrop);
        content.getChildren().add(cardDrop);
        
        setContent(content);
    }
    
    private JCard createCard(String title, String description) {
        JCard card = new JCard();
        card.setTitle(title);
        card.setSubtitle(description);
        return card;
    }
}


