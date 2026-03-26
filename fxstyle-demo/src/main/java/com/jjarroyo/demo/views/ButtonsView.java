package com.jjarroyo.demo.views;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JLabel;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ButtonsView extends ScrollPane {

    public ButtonsView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24)); 
        
        setFitToWidth(true);
        setContent(content);
        
        // Page Title
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Buttons")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Button styles inspired by Tailwind/JJArroyo")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        
        content.getChildren().add(pageHeader);
        
        // Section: Colors
        content.getChildren().add(new JCard("Solid Colors", createSolidButtons()));

        // Section: Outline
        content.getChildren().add(new JCard("Outline Style", createOutlineButtons()));
        
        // Section: Sizes
        content.getChildren().add(new JCard("Sizes", createSizeButtons()));

        // Section: Light Style
        content.getChildren().add(new JCard("Light Style", createLightButtons()));

        // Section: Dashed Style
        content.getChildren().add(new JCard("Dashed Style", createDashedButtons()));

        // Section: Icon Text Color Style
        content.getChildren().add(new JCard("Icon Buttons", createIconButtons()));

        // Section: Loading Style
        content.getChildren().add(new JCard("Loading Style", createLoadingButtons()));
    }

    private javafx.scene.Node createSolidButtons() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        // Default is Primary, no need to add class explicitly
        JButton btnPrimary = new JButton("Primary");
        // btnPrimary.addClass("btn-primary"); // Default

        JButton btnSuccess = new JButton("Success");
        btnSuccess.addClass("btn-success");

        JButton btnDanger = new JButton("Danger");
        btnDanger.addClass("btn-danger");
        
        JButton btnWarning = new JButton("Warning");
        btnWarning.addClass("btn-warning");

        JButton btnInfo = new JButton("Info");
        btnInfo.addClass("btn-info");

        JButton btnSecondary = new JButton("Secondary");
        btnSecondary.addClass("btn-secondary");

        JButton btnDark = new JButton("Dark");
        btnDark.addClass("btn-dark");

        pane.getChildren().addAll(btnPrimary, btnSuccess, btnDanger, btnWarning, btnInfo, btnSecondary, btnDark);
        return pane;
    }

    private javafx.scene.Node createOutlineButtons() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        JButton btnOutline = new JButton("Outline Primary");
        btnOutline.addClass("btn-outline-primary");

        pane.getChildren().addAll(btnOutline);
        return pane;
    }
    
    private javafx.scene.Node createSizeButtons() {
        HBox box = new HBox(16);
        box.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        JButton btnSm = new JButton("Small Button");
        btnSm.addClass("btn-sm"); // Should keep default primary
        
        JButton btnMd = new JButton("Default Button");
        // Default primary
        
        JButton btnLg = new JButton("Large Button");
        btnLg.addClass("btn-lg"); // Should keep default primary

        box.getChildren().addAll(btnSm, btnMd, btnLg);
        return box;
    }

    private javafx.scene.Node createLightButtons() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        JButton btnLightPrimary = new JButton("Primary Light");
        btnLightPrimary.addClass("btn-light-primary");

        JButton btnLightSuccess = new JButton("Success Light");
        btnLightSuccess.addClass("btn-light-success");

        JButton btnLightDanger = new JButton("Danger Light");
        btnLightDanger.addClass("btn-light-danger");

        pane.getChildren().addAll(btnLightPrimary, btnLightSuccess, btnLightDanger);
        return pane;
    }

    private javafx.scene.Node createDashedButtons() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        JButton btnDashed = new JButton("Add New");
        btnDashed.addClass("btn-dashed");

        pane.getChildren().addAll(btnDashed);
        return pane;
    }

    private javafx.scene.Node createIconButtons() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        // Using JIcon now!
        JButton btnIconPrimary = new JButton("Star Button", JIcon.STAR);
        btnIconPrimary.addClass("btn-accent-primary");

        JButton btnIconInfo = new JButton("Favorites", JIcon.HEART);
        btnIconInfo.addClass("btn-accent-info");
        
        JButton btnSettings = new JButton("Settings", JIcon.SETTINGS);
        btnSettings.addClass("btn-secondary");

        pane.getChildren().addAll(btnIconPrimary, btnIconInfo, btnSettings);
        return pane;
    }

    private javafx.scene.Node createLoadingButtons() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        // Standard Loading (Default is primary)
        JButton btnLoading1 = new JButton("Please wait...");
        // btnLoading1.addClass("btn-primary"); // Default
        javafx.scene.control.ProgressIndicator spinner1 = new javafx.scene.control.ProgressIndicator();
        spinner1.setMaxSize(20, 20);
        spinner1.setStyle("-fx-progress-color: white;");
        btnLoading1.setGraphic(spinner1);

        // Icon only Loading
        JButton btnLoading2 = new JButton();
        // btnLoading2.addClass("btn-primary"); // Default
        javafx.scene.control.ProgressIndicator spinner2 = new javafx.scene.control.ProgressIndicator();
        spinner2.setMaxSize(20, 20);
        spinner2.setStyle("-fx-progress-color: white;");
        btnLoading2.setGraphic(spinner2);

        // Right side loading
        JButton btnLoading3 = new JButton("Loading");
        btnLoading3.addClass("btn-outline-primary");
        btnLoading3.setContentDisplay(javafx.scene.control.ContentDisplay.RIGHT);
        javafx.scene.control.ProgressIndicator spinner3 = new javafx.scene.control.ProgressIndicator();
        spinner3.setMaxSize(20, 20);
        btnLoading3.setGraphic(spinner3);
        
        // Secondary Loading
        JButton btnLoading4 = new JButton("Processing");
        btnLoading4.addClass("btn-secondary");
        javafx.scene.control.ProgressIndicator spinner4 = new javafx.scene.control.ProgressIndicator();
        spinner4.setMaxSize(20, 20);
        btnLoading4.setGraphic(spinner4);

        pane.getChildren().addAll(btnLoading1, btnLoading2, btnLoading3, btnLoading4);
        return pane;
    }
}


