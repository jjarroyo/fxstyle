package com.jjarroyo.demo.views;

import com.jjarroyo.components.JBadge;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JLabel;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class LabelsView extends ScrollPane {

    public LabelsView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));
        
        setFitToWidth(true);
        setContent(content);
        
        // Page Title
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Labels (Badges)")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Label and badge styles")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        
        content.getChildren().add(pageHeader);
        
        // Section: Basic Badges
        content.getChildren().add(new JCard("Solid Badges", createSolidBadges()));

        // Section: Light Badges
        content.getChildren().add(new JCard("Light Badges", createLightBadges()));
        
        // Section: Sizes
        content.getChildren().add(new JCard("Sizes", createSizeBadges()));
    }

    private javafx.scene.Node createSolidBadges() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        pane.getChildren().addAll(
            new JBadge("Primary"), // Default is primary
            new JBadge("Success", "badge-success"),
            new JBadge("Danger", "badge-danger"),
            new JBadge("Warning", "badge-warning"),
            new JBadge("Info", "badge-info"),
            new JBadge("Dark", "badge-dark"),
            new JBadge("Secondary", "badge-secondary")
        );
        return pane;
    }

    private javafx.scene.Node createLightBadges() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        pane.getChildren().addAll(
            new JBadge("Primary Light", "badge-light-primary"),
            new JBadge("Success Light", "badge-light-success"),
            new JBadge("Danger Light", "badge-light-danger"),
            new JBadge("Warning Light", "badge-light-warning"),
            new JBadge("Info Light", "badge-light-info"),
            new JBadge("Dark Light", "badge-light-dark")
        );
        return pane;
    }

    private javafx.scene.Node createSizeBadges() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        pane.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        pane.getChildren().addAll(
            new JBadge("Small Badge", "badge-sm"), // Default primary + small
            new JBadge("Default Badge"),           // Default primary
            new JBadge("Large Badge", "badge-lg")  // Default primary + large
        );
        return pane;
    }
}


