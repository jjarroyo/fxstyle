package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JPopover;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PopoversView extends ScrollPane {

    public PopoversView() {
        getStyleClass().add("j-scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));
        
        VBox content = new VBox();
        content.setSpacing(24);
        
        // Header
        VBox header = new VBox();
        header.setSpacing(8);
        Label title = new Label("Popovers");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Lightweight overlays for context and confirmation.");
        subtitle.getStyleClass().add("page-subtitle");
        header.getChildren().addAll(title, subtitle);
        content.getChildren().add(header);
        
        // 1. Basic Examples (Light & Dark)
        JCard basicCard = new JCard("Basic", "Default Light and Dark theme popovers.");
        HBox basicRow = new HBox(16);
        
        Button btnLight = new Button("Light Popover");
        btnLight.getStyleClass().addAll("btn", "btn-light-primary");
        btnLight.setOnAction(e -> {
            JPopover pop = new JPopover();
            pop.setTitle("Light Theme")
               .setBody("This is a default light popover.")
               .show(btnLight);
        });
        
        Button btnDark = new Button("Dark Popover");
        btnDark.getStyleClass().addAll("btn", "btn-dark");
        btnDark.setOnAction(e -> {
            JPopover pop = new JPopover();
            pop.setTitle("Dark Theme")
               .setBody("This popover uses the dark theme variant.")
               .show(btnDark);
        });
        
        basicRow.getChildren().addAll(btnLight, btnDark);
        basicCard.setBody(basicRow);
        content.getChildren().add(basicCard);
        
        // 2. Confirmation Mode
        JCard confirmCard = new JCard("Confirmation", "Built-in confirmation mode with callbacks.");
        HBox confirmRow = new HBox(16);
        
        Button btnConfirm = new Button("Delete Item");
        btnConfirm.getStyleClass().addAll("btn", "btn-danger");
        btnConfirm.setOnAction(e -> {
            JPopover pop = new JPopover();
            pop.setTitle("Delete User?")
               .setBody("Are you sure you want to delete this user? This action cannot be undone.")
               .setConfirmationMode("Yes, Delete", "Cancel", () -> {
                   System.out.println("User confirmed deletion!");
               })
               .show(btnConfirm);
        });
        
        confirmRow.getChildren().add(btnConfirm);
        confirmCard.setBody(confirmRow);
        content.getChildren().add(confirmCard);
        
        // 3. Directions
        JCard dirCard = new JCard("Directions", "Positioning options.");
        HBox dirRow = new HBox(16);
        
        Button btnTop = new Button("Top (Default)");
        btnTop.getStyleClass().addAll("btn", "btn-secondary");
        btnTop.setOnAction(e -> {
            new JPopover().setTitle("Top").setBody("Placed above the target.").show(btnTop);
        });
        
        Button btnBottom = new Button("Bottom");
        btnBottom.getStyleClass().addAll("btn", "btn-secondary");
        btnBottom.setOnAction(e -> {
            new JPopover().setTitle("Bottom").setBody("Placed below the target.")
                .setPosition(JPopover.Position.BOTTOM)
                .show(btnBottom);
        });
        
        Button btnLeft = new Button("Left");
        btnLeft.getStyleClass().addAll("btn", "btn-secondary");
        btnLeft.setOnAction(e -> {
            new JPopover().setTitle("Left").setBody("Placed to the left.")
                .setPosition(JPopover.Position.LEFT)
                .show(btnLeft);
        });

        Button btnRight = new Button("Right");
        btnRight.getStyleClass().addAll("btn", "btn-secondary");
        btnRight.setOnAction(e -> {
            new JPopover().setTitle("Right").setBody("Placed to the right.")
                .setPosition(JPopover.Position.RIGHT)
                .show(btnRight);
        });
        
        dirRow.getChildren().addAll(btnTop, btnBottom, btnLeft, btnRight);
        dirCard.setBody(dirRow);
        content.getChildren().add(dirCard);
        
        // 4. Custom Content
        JCard customCard = new JCard("Custom Content", "Embed any node.");
        HBox customRow = new HBox(16);
        
        Button btnCustom = new Button("Login Popover");
        btnCustom.getStyleClass().addAll("btn", "btn-primary");
        btnCustom.setOnAction(e -> {
            VBox loginForm = new VBox(10);
            loginForm.setPadding(new Insets(10, 0, 0, 0));
            
            javafx.scene.control.TextField userField = new javafx.scene.control.TextField();
            userField.setPromptText("Username");
            userField.getStyleClass().add("form-input");
            
            javafx.scene.control.PasswordField passField = new javafx.scene.control.PasswordField();
            passField.setPromptText("Password");
            passField.getStyleClass().add("form-input");
            
            Button submit = new Button("Login");
            submit.getStyleClass().addAll("btn", "btn-sm", "btn-dark", "w-full");
            
            loginForm.getChildren().addAll(userField, passField, submit);
            
            JPopover pop = new JPopover();
            pop.setTitle("Login")
               .setContentNode(loginForm)
               .show(btnCustom);
               
            submit.setOnAction(ev -> {
                System.out.println("Logging in: " + userField.getText());
                pop.hide();
            });
        });
        
        customRow.getChildren().add(btnCustom);
        customCard.setBody(customRow);
        content.getChildren().add(customCard);

        setContent(content);
    }
}


