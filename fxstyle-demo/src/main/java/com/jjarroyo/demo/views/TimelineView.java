package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JTimeline;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class TimelineView extends ScrollPane {

    public TimelineView() {
        VBox content = new VBox();
        content.setPadding(new Insets(24));
        content.setSpacing(24);
        
        setFitToWidth(true);
        setContent(content);

        Label title = new Label("Timelines");
        title.getStyleClass().add("j-text-h2");
        
        Label subtitle = new Label("Show historical events or step-by-step processes.");
        subtitle.getStyleClass().add("j-text-body");

        content.getChildren().addAll(title, subtitle, createBasicTimeline(), createIconsTimeline(), createHorizontalTimeline());
    }

    private JCard createBasicTimeline() {
        JCard card = new JCard();
        card.setTitle("Basic Timeline");
        card.setSubtitle("Vertical timeline with default styling and colors");

        JTimeline timeline = new JTimeline();
        
        JTimeline.Event e1 = new JTimeline.Event("System Update", "The system was updated to version 2.1.0.", "10:00 AM");
        e1.setColorClass("primary");
        
        JTimeline.Event e2 = new JTimeline.Event("Database Backup", "Automated backup completed without errors.", "11:30 AM");
        e2.setColorClass("success");
        
        JTimeline.Event e3 = new JTimeline.Event("High Traffic Alert", "CPU usage spiked over 95%.", "02:15 PM");
        e3.setColorClass("warning");
        
        JTimeline.Event e4 = new JTimeline.Event("Server Crashed", "Node 1 went offline due to memory limits.", "03:45 PM");
        e4.setColorClass("danger");
        
        timeline.addEvent(e1);
        timeline.addEvent(e2);
        timeline.addEvent(e3);
        timeline.addEvent(e4);

        card.setBody(timeline);
        return card;
    }

    private JCard createIconsTimeline() {
        JCard card = new JCard();
        card.setTitle("Icon Timeline");
        card.setSubtitle("Vertical timeline using custom icons for events");

        JTimeline timeline = new JTimeline();
        
        JTimeline.Event e1 = new JTimeline.Event("User Signed Up", "John Doe created a new account.", "Yesterday");
        e1.setIcon(JIcon.USER.view());
        e1.setColorClass("info");
        
        JTimeline.Event e2 = new JTimeline.Event("Email Verification", "Verification email sent to john@example.com.", "Yesterday");
        e2.setIcon(JIcon.INFO.view());
        e2.setColorClass("slate");
        
        JTimeline.Event e3 = new JTimeline.Event("First Purchase", "Bought Premium Subscription.", "Today");
        e3.setIcon(JIcon.STAR.view());
        e3.setColorClass("success");
        
        // Custom content example
        VBox receipt = new VBox(new Label("Invoice #1024 - $49.99 paid."));
        receipt.setStyle("-fx-background-color: #f8fafc; -fx-padding: 8; -fx-background-radius: 4; -fx-border-color: #e2e8f0; -fx-border-radius: 4;");
        e3.setCustomContent(receipt);
        
        timeline.addEvent(e1);
        timeline.addEvent(e2);
        timeline.addEvent(e3);

        card.setBody(timeline);
        return card;
    }
    
    private JCard createHorizontalTimeline() {
        JCard card = new JCard();
        card.setTitle("Horizontal Timeline");
        card.setSubtitle("A horizontal orientation for processes covering periods");

        JTimeline timeline = new JTimeline();
        timeline.setOrientation(Orientation.HORIZONTAL);
        
        JTimeline.Event e1 = new JTimeline.Event("Phase 1", "Planning", "Jan 2024");
        e1.setColorClass("slate");
        
        JTimeline.Event e2 = new JTimeline.Event("Phase 2", "Development", "Feb 2024");
        e2.setColorClass("primary");
        
        JTimeline.Event e3 = new JTimeline.Event("Phase 3", "Testing", "Apr 2024");
        e3.setColorClass("warning");
        
        JTimeline.Event e4 = new JTimeline.Event("Launch", "Production Release", "Jun 2024");
        e4.setIcon(JIcon.ROCKET.view());
        e4.setColorClass("success");
        
        timeline.addEvent(e1);
        timeline.addEvent(e2);
        timeline.addEvent(e3);
        timeline.addEvent(e4);

        card.setBody(timeline);
        // add scroll pane for horizontal if necessary, though card scroll is handled usually
        card.makeScrollable();
        
        return card;
    }
}
