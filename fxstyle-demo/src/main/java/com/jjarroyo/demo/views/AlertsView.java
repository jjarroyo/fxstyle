package com.jjarroyo.demo.views;

import com.jjarroyo.components.JAlert;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JLabel;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class AlertsView extends ScrollPane {

    public AlertsView() {
        getStyleClass().add("scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));

        VBox container = new VBox(24);
        container.setMaxWidth(Double.MAX_VALUE);
        container.getStyleClass().add("container");

        container.getChildren().add(
            new JLabel("Alerts").addClass("text-2xl", "text-slate-800", "font-bold")
        );

        // Section 1: Basic Colors (Light Variant)
        JCard basicCard = new JCard("Basic Alerts", new VBox(16,
            createAlert("Primary Alert", "This is a primary alert.", "alert-primary", "alert-light", JIcon.INFO),
            createAlert("Success Alert", "This is a success alert.", "alert-success", "alert-light", JIcon.CHECK),
            createAlert("Warning Alert", "This is a warning alert.", "alert-warning", "alert-light", JIcon.WARNING_CIRCLE),
            createAlert("Danger Alert", "This is a danger alert.", "alert-danger", "alert-light", JIcon.CROSS_CIRCLE),
            createAlert("Info Alert", "This is an info alert.", "alert-info", "alert-light", JIcon.INFO)
        ));
        
        // Section 2: Bordered Alerts
        JCard borderedCard = new JCard("Bordered Alerts", new VBox(16,
             createAlert("Bordered Primary", "Has a solid border.", "alert-primary", "alert-light", "alert-bordered", JIcon.INFO),
             createAlert("Bordered Danger", "Has a solid border.", "alert-danger", "alert-light", "alert-bordered", JIcon.CROSS_CIRCLE),
             createAlert("Bordered Success", "Has a solid border.", "alert-success", "alert-light", "alert-bordered", JIcon.CHECK)
        ));

        // Section 3: Dashed Alerts
        JCard dashedCard = new JCard("Dashed Alerts", new VBox(16,
             createAlert("Dashed Warning", "Has a dashed border style.", "alert-warning", "alert-light", "alert-dashed", JIcon.WARNING_CIRCLE),
             createAlert("Dashed Info", "Has a dashed border style.", "alert-info", "alert-light", "alert-dashed", JIcon.INFO)
        ));

        // Section 4: Dismissible Alerts
        JAlert dismissAlert = new JAlert("Dismiss Me", "Click the X on the right.");
        dismissAlert.addClass("alert-primary", "alert-light");
        dismissAlert.setIcon(com.jjarroyo.components.JIcon.INFO);
        dismissAlert.setDismissible(true);

        JCard dismissCard = new JCard("Dismissible Alert", dismissAlert);

        container.getChildren().addAll(basicCard, borderedCard, dashedCard, dismissCard);
        
        setContent(container);
    }

    private JAlert createAlert(String title, String desc, String colorClass, String variantClass, JIcon iconCode) {
        JAlert alert = new JAlert(title, desc);
        alert.addClass(colorClass, variantClass);
        if (iconCode != null) {
            alert.setIcon(iconCode);
        }
        return alert;
    }
    
    private JAlert createAlert(String title, String desc, String colorClass, String variantClass, String borderClass, JIcon iconCode) {
         JAlert alert = createAlert(title, desc, colorClass, variantClass, iconCode);
         alert.addClass(borderClass);
         return alert;
    }
}


