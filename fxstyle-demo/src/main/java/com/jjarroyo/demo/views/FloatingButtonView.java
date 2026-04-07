package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JFloatingButton;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JLabel;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class FloatingButtonView extends StackPane {

    public FloatingButtonView() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(24));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        // Important: remove border from scrollpane to avoid double borders
        scrollPane.setStyle("-fx-background-color:transparent;-fx-padding:0;");

        // Add ScrollPane as base layer
        getChildren().add(scrollPane);

        // Page Header
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Floating Action Buttons").withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Responsive, auto-positioning FABs").withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        // Information Card
        content.getChildren().add(new JCard("About Floating Buttons", 
            new JLabel("Look at the corners of this view! There are several floating buttons attached relatively " +
                        "to this view's structural boundaries. Resize the window and see how they adapt. " +
                        "You can configure Top, Center, Bottom and Left, Center, Right positions automagically.")));
        
        // Large blank space to simulate scrolling
        VBox space = new VBox();
        space.setMinHeight(800);
        content.getChildren().add(space);

        // Floating Buttons

        // 1. Bottom Right (Standard Primary)
        JFloatingButton fabBR = new JFloatingButton(JIcon.ADD);
        fabBR.setPosition(JFloatingButton.Position.BOTTOM_RIGHT);
        fabBR.addClass("btn-primary"); 

        // 2. Bottom Left (Dark color)
        JFloatingButton fabBL = new JFloatingButton(JIcon.SETTINGS);
        fabBL.setPosition(JFloatingButton.Position.BOTTOM_LEFT);
        fabBL.addClass("btn-dark");

        // 3. Top Right (Extended, Info)
        JFloatingButton fabTR = new JFloatingButton("Help", JIcon.STAR);
        fabTR.setPosition(JFloatingButton.Position.TOP_RIGHT);
        fabTR.addClass("btn-info");

        // 4. Bottom Center (Extended, Gradient)
        JFloatingButton fabBC = new JFloatingButton("Create New", JIcon.CHECK_CIRCLE);
        fabBC.setPosition(JFloatingButton.Position.BOTTOM_CENTER);
        fabBC.addClass("btn-gradient-blue");

        // 5. Center Right (Success)
        JFloatingButton fabCR = new JFloatingButton(JIcon.SHARE);
        fabCR.setPosition(JFloatingButton.Position.CENTER_RIGHT);
        fabCR.addClass("btn-success");
        fabCR.setExtended(false); // Make sure it's round
        
        // 6. Center Left (Outline Danger)
        JFloatingButton fabCL = new JFloatingButton(JIcon.BELL);
        fabCL.setPosition(JFloatingButton.Position.CENTER_LEFT);
        fabCL.addClass("btn-outline-danger");
        fabCL.setStyle("-fx-background-color: white;"); // Add solid background to avoid transparency conflicts
        fabCL.setExtended(false);

        // Note: we add them directly to this StackPane. They will auto position!
        getChildren().addAll(fabBR, fabBL, fabTR, fabBC, fabCR, fabCL);
    }
}
