package com.jjarroyo.demo.views;

import com.jjarroyo.components.JAccordion;
import com.jjarroyo.components.JAccordionPane;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JLabel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AccordionsView extends ScrollPane {

    public AccordionsView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));
        
        setFitToWidth(true);
        setContent(content);
        
        // Page Title
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Accordions")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Paneles colapsables para organizar contenido")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        
        content.getChildren().add(pageHeader);
        
        // Basic Accordion
        content.getChildren().add(new JCard("Acordeón Básico", createBasicAccordion()));
        
        // Multiple Expanded (using independent panes or configured accordion)
        // JavaFX Accordion only allows one expanded at a time roughly speaking unless configured otherwise?
        // Actually Accordion only allows one. If we want multiple, we just use VBox of TitledPanes.
        // Let's stick to standard Accordion behavior for JAccordion.
    }

    private javafx.scene.Node createBasicAccordion() {
        JAccordion accordion = new JAccordion();
        
        // Item 1
        VBox content1 = new VBox(10);
        content1.getChildren().add(new Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        JAccordionPane pane1 = new JAccordionPane("¿Cómo funciona el sistema?", content1);
        
        // Item 2
        VBox content2 = new VBox(10);
        content2.getChildren().add(new Text("Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."));
        JAccordionPane pane2 = new JAccordionPane("¿Puedo personalizar los colores?", content2);
        
        // Item 3
        VBox content3 = new VBox(10);
        content3.getChildren().add(new Text("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."));
        JAccordionPane pane3 = new JAccordionPane("Información de Facturación", content3);
        
        accordion.getPanes().addAll(pane1, pane2, pane3);
        accordion.setExpandedPane(pane1); // Expand first one
        
        return accordion;
    }
}
