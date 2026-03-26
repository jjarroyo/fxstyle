package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JCircularProgress;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JProgressBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;

public class ProgressView extends ScrollPane {

    public ProgressView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));
        
        setFitToWidth(true);
        setContent(content);
        
        // Page Title
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Progress")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Barras de progreso lineales y circulares")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        
        content.getChildren().add(pageHeader);
        
        // Basic Progress
        content.getChildren().add(new JCard("Progreso Básico", createBasicProgress()));
        
        // Indeterminate
        content.getChildren().add(new JCard("Indeterminado", createIndeterminateProgress()));
        
        // Colors
        content.getChildren().add(new JCard("Colores", createColorProgress()));
        
        // Sizes
        content.getChildren().add(new JCard("Tamaños", createSizeProgress()));
        
        // Circular Progress
        content.getChildren().add(new JCard("Progreso Circular", createCircularProgress()));
        
        // Circular Progress Colors
        content.getChildren().add(new JCard("Circular - Colores", createCircularColors()));
    }

    private javafx.scene.Node createBasicProgress() {
        VBox container = new VBox(20);
        
        // 50%
        Label l1 = new Label("50%");
        JProgressBar p1 = new JProgressBar(0.5);
        p1.setMaxWidth(Double.MAX_VALUE);
        
        // 75%
        Label l2 = new Label("75%");
        JProgressBar p2 = new JProgressBar(0.75);
        p2.setMaxWidth(Double.MAX_VALUE);
        
        container.getChildren().addAll(l1, p1, l2, p2);
        return container;
    }

    private javafx.scene.Node createIndeterminateProgress() {
        VBox container = new VBox(20);
        
        Label l1 = new Label("Cargando...");
        JProgressBar p1 = new JProgressBar(); // Indeterminate by default if no progress set or set to -1
        p1.setProgress(-1);
        p1.setMaxWidth(Double.MAX_VALUE);
        
        container.getChildren().addAll(l1, p1);
        return container;
    }
    
    private javafx.scene.Node createColorProgress() {
        VBox container = new VBox(20);
        
        for (JProgressBar.ProgressStyle style : JProgressBar.ProgressStyle.values()) {
            Label label = new Label(style.name());
            JProgressBar pb = new JProgressBar(0.6);
            pb.setProgressStyle(style);
            pb.setMaxWidth(Double.MAX_VALUE);
            container.getChildren().addAll(label, pb);
        }
        
        return container;
    }
    
    private javafx.scene.Node createSizeProgress() {
        VBox container = new VBox(20);
        
        Label l1 = new Label("Small");
        JProgressBar sm = new JProgressBar(0.4);
        sm.setSize(JProgressBar.ProgressSize.SM);
        sm.setMaxWidth(Double.MAX_VALUE);
        
        Label l2 = new Label("Default (Medium)");
        JProgressBar md = new JProgressBar(0.6);
        md.setSize(JProgressBar.ProgressSize.MD);
        md.setMaxWidth(Double.MAX_VALUE);
        
        Label l3 = new Label("Large");
        JProgressBar lg = new JProgressBar(0.8);
        lg.setSize(JProgressBar.ProgressSize.LG);
        lg.setMaxWidth(Double.MAX_VALUE);
        
        container.getChildren().addAll(l1, sm, l2, md, l3, lg);
        return container;
    }

    private javafx.scene.Node createCircularProgress() {
        FlowPane container = new FlowPane(40, 20);
        container.setAlignment(Pos.CENTER_LEFT);
        
        // 25%
        VBox box1 = new VBox(8);
        box1.setAlignment(Pos.CENTER);
        JCircularProgress cp1 = new JCircularProgress(40, 10);
        cp1.setProgress(0.25);
        box1.getChildren().addAll(cp1, new Label("25%"));
        
        // 50%
        VBox box2 = new VBox(8);
        box2.setAlignment(Pos.CENTER);
        JCircularProgress cp2 = new JCircularProgress(50, 14);
        cp2.setProgress(0.50);
        box2.getChildren().addAll(cp2, new Label("50%"));
        
        // 75%
        VBox box3 = new VBox(8);
        box3.setAlignment(Pos.CENTER);
        JCircularProgress cp3 = new JCircularProgress(60, 16);
        cp3.setProgress(0.75);
        box3.getChildren().addAll(cp3, new Label("75%"));
        
        // 100%
        VBox box4 = new VBox(8);
        box4.setAlignment(Pos.CENTER);
        JCircularProgress cp4 = new JCircularProgress(70, 20);
        cp4.setProgress(1.0);
        box4.getChildren().addAll(cp4, new Label("100%"));
        
        container.getChildren().addAll(box1, box2, box3, box4);
        return container;
    }

    private javafx.scene.Node createCircularColors() {
        FlowPane container = new FlowPane(40, 20);
        container.setAlignment(Pos.CENTER_LEFT);
        
        // Primary
        VBox box1 = new VBox(8);
        box1.setAlignment(Pos.CENTER);
        JCircularProgress cp1 = new JCircularProgress(50, 12);
        cp1.setProgress(0.65);
        cp1.setColor("#6366f1");
        box1.getChildren().addAll(cp1, new Label("Primary"));
        
        // Success
        VBox box2 = new VBox(8);
        box2.setAlignment(Pos.CENTER);
        JCircularProgress cp2 = new JCircularProgress(50, 12);
        cp2.setProgress(0.80);
        cp2.setColor("#22c55e");
        box2.getChildren().addAll(cp2, new Label("Success"));
        
        // Danger
        VBox box3 = new VBox(8);
        box3.setAlignment(Pos.CENTER);
        JCircularProgress cp3 = new JCircularProgress(50, 12);
        cp3.setProgress(0.40);
        cp3.setColor("#ef4444");
        box3.getChildren().addAll(cp3, new Label("Danger"));
        
        // Warning
        VBox box4 = new VBox(8);
        box4.setAlignment(Pos.CENTER);
        JCircularProgress cp4 = new JCircularProgress(50, 12);
        cp4.setProgress(0.55);
        cp4.setColor("#f59e0b");
        box4.getChildren().addAll(cp4, new Label("Warning"));
        
        container.getChildren().addAll(box1, box2, box3, box4);
        return container;
    }
}
