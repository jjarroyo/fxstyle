package com.jjarroyo.demo.views;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JInput;
import com.jjarroyo.components.JLabel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class CardsView extends ScrollPane {

    public CardsView() {
        getStyleClass().add("scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));

        VBox container = new VBox(24);
        container.setMaxWidth(Double.MAX_VALUE);
        container.getStyleClass().add("container");

        // Header
        container.getChildren().add(
            new JLabel("Advanced Cards").addClass("text-2xl", "text-slate-800", "font-bold")
        );

        // 1. Simple Card
        JCard simpleCard = new JCard("Simple Card", new JLabel("Content of a basic card. Just title and body."));
        simpleCard.setSubtitle("Description text here");
        
        // 2. Card with Separators (Bordered Header/Footer)
        // We can achieve this by adding specific styles or ensuring the default .card-header has border.
        // In our CSS, .card-header HAS a bottom border 1px solid -color-slate-200.
        // So checking if it is visible.
        JCard separatorCard = new JCard("Card with Separators", new VBox(10, 
            new JLabel("This card has visible separators for header and footer."),
            new JInput("Example Input")
        ));
        separatorCard.setFooter(new JButton("Save Changes", JIcon.CHECK).addClass("btn-primary"));
        
        // 3. Card with Toolbar (Dropdowns, Buttons)
        JCard toolbarCard = new JCard("Toolbar Actions", new JLabel("Header contains buttons, icons, and dropdowns."));
        
        // Icon Button
        JButton iconBtn = new JButton(null, JIcon.SETTINGS).addClass("btn-icon", "btn-light-primary", "btn-sm");
        
        // Dropdown (JDropdown)
        com.jjarroyo.components.JDropdown dropdown = new com.jjarroyo.components.JDropdown("Export");
        dropdown.addClass("btn-light-primary");
        dropdown.addAction("Excel", e -> System.out.println("Export Excel"));
        dropdown.addAction("PDF", e -> System.out.println("Export PDF"));
        dropdown.addAction("CSV", e -> System.out.println("Export CSV"));
        
        toolbarCard.addToolbarItem(iconBtn);
        toolbarCard.addToolbarItem(dropdown);
        
        // 4. Collapsible Card
        JCard collapsibleCard = new JCard("Collapsible Card", new VBox(10,
            new JLabel("Click the chevron in the top right to toggle visibility."),
            new JLabel("Content line 1..."),
            new JLabel("Content line 2...")
        ));
        collapsibleCard.setCollapsible(true);
        collapsibleCard.setFooter(new JLabel("Footer is also toggled."));

        // 5. Scrollable Card (Fixed Height)
        VBox longContent = new VBox(8);
        for(int i=0; i<20; i++) {
            longContent.getChildren().add(new JLabel("Scrollable Row item " + (i+1)));
        }
        JCard scrollCard = new JCard("Scrollable Content", longContent);
        scrollCard.setPrefHeight(300); // Fixed height to force scroll
        scrollCard.makeScrollable();

        // Add all to container
        container.getChildren().addAll(simpleCard, separatorCard, toolbarCard, collapsibleCard, scrollCard);
        
        setContent(container);
    }
}


