package com.jjarroyo.components;

import javafx.scene.layout.VBox;
import javafx.scene.Node;

public class JCard extends VBox {

    private javafx.scene.layout.HBox header;
    private javafx.scene.layout.VBox titleBox;
    private javafx.scene.layout.HBox toolbar;
    private javafx.scene.layout.VBox body;
    private javafx.scene.layout.HBox footer;
    private javafx.scene.control.ScrollPane scrollPane;

    public JCard() {
        super();
        init();
    }
    
    // Convenience constructor
    public JCard(String titleText, Node content) {
        this();
        setTitle(titleText);
        setBody(content);
    }

    // Convenience constructor with subtitle
    public JCard(String titleText, String subtitleText) {
        this();
        setTitle(titleText);
        setSubtitle(subtitleText);
    }

    private void init() {
        getStyleClass().add("card");
        // Use fill width
        setFillWidth(true);
    }

    private void ensureHeader() {
        if (header == null) {
            header = new javafx.scene.layout.HBox();
            header.getStyleClass().add("card-header");
            
            // Spacer to push toolbar to right
            javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
            javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
            
            titleBox = new javafx.scene.layout.VBox();
            titleBox.getStyleClass().add("card-title-box");
            
            toolbar = new javafx.scene.layout.HBox();
            toolbar.getStyleClass().add("card-toolbar");
            
            header.getChildren().addAll(titleBox, spacer, toolbar);
            
            // Add header at the top
            getChildren().add(0, header);
        }
    }

    public JCard setTitle(String titleText) {
        ensureHeader();
        // Clear existing title if any (simple implementation)
        titleBox.getChildren().clear();
        
        JLabel title = new JLabel(titleText);
        title.getStyleClass().add("card-title");
        
        titleBox.getChildren().add(title);
        return this;
    }

    public JCard setSubtitle(String subtitleText) {
        ensureHeader();
        // Assume title is first, subtitle second. 
        // This is a simple append, realistic usage sets both or one.
        JLabel subtitle = new JLabel(subtitleText);
        subtitle.getStyleClass().add("card-subtitle");
        titleBox.getChildren().add(subtitle);
        return this;
    }

    public JCard addToolbarItem(Node node) {
        ensureHeader();
        toolbar.getChildren().add(node);
        return this;
    }

    public void setLine(boolean show) {
        ensureHeader();
        if (show) {
            if (!header.getStyleClass().contains("card-header-line")) {
                header.getStyleClass().add("card-header-line");
            }
        } else {
            header.getStyleClass().remove("card-header-line");
        }
    }

    public JCard setBody(Node content) {
        if (body == null) {
            body = new javafx.scene.layout.VBox();
            body.getStyleClass().add("card-body");
            // If header exists, add after it. If footer, before it.
            // Simplest: remove all except header/footer and re-add?
            // Or just keep track of indices.
            // VBox order: Header, Body, Footer.
            updateChildrenOrder();
        }
        body.getChildren().clear();
        if (content != null) {
            body.getChildren().add(content);
        }
        return this;
    }

    public JCard setFooter(Node content) {
        if (footer == null) {
            footer = new javafx.scene.layout.HBox();
            footer.getStyleClass().add("card-footer");
            updateChildrenOrder();
        }
        footer.getChildren().clear();
        if (content != null) {
            footer.getChildren().add(content);
        }
        return this;
    }
    
    private void updateChildrenOrder() {
        getChildren().clear();
        if (header != null) getChildren().add(header);
        if (scrollPane != null) {
             getChildren().add(scrollPane);
        } else if (body != null) {
             getChildren().add(body);
        }
        if (footer != null) getChildren().add(footer);
    }

    public JCard makeScrollable() {
        if (body != null && scrollPane == null) {
            scrollPane = new javafx.scene.control.ScrollPane(body);
            scrollPane.setFitToWidth(true);
            scrollPane.getStyleClass().add("card-scroll");
            scrollPane.setStyle("-fx-background-color:transparent; -fx-background: transparent;");
            updateChildrenOrder();
        }
        return this;
    }
    
    private boolean collapsible = false;
    private boolean expanded = true;
    private javafx.scene.shape.SVGPath chevron;

    public JCard setCollapsible(boolean collapsible) {
        this.collapsible = collapsible;
        ensureHeader();
        updateToolbar();
        
        if (collapsible) {
            header.setCursor(javafx.scene.Cursor.HAND);
            header.setOnMouseClicked(e -> toggleCollapse());
        } else {
            header.setCursor(javafx.scene.Cursor.DEFAULT);
            header.setOnMouseClicked(null);
        }
        return this;
    }

    private void updateToolbar() {
        if (toolbar == null) return;
        
        // Remove existing chevron if any
        toolbar.getChildren().remove(chevron);
        
        if (collapsible) {
            if (chevron == null) {
                chevron = new javafx.scene.shape.SVGPath();
                chevron.setContent("M12 8l-6 6 1.41 1.41L12 10.83l4.59 4.58L18 14z"); // Chevron Up
                chevron.getStyleClass().add("card-chevron");
                
                javafx.scene.layout.StackPane chevronBtn = new javafx.scene.layout.StackPane(chevron);
                // Click is handled by the header
                toolbar.getChildren().add(chevronBtn);
            } else {
                 javafx.scene.layout.StackPane chevronBtn = new javafx.scene.layout.StackPane(chevron);
                 // Click is handled by the header
                 toolbar.getChildren().add(chevronBtn);
            }
        }
    }
    
    public JCard setExpanded(boolean expanded) {
        if (this.expanded != expanded) {
            toggleCollapse();
        }
        return this;
    }
    
    private void toggleCollapse() {
        expanded = !expanded;
        if (body != null) {
            body.setVisible(expanded);
            body.setManaged(expanded);
        }
        if (scrollPane != null) {
            scrollPane.setVisible(expanded);
            scrollPane.setManaged(expanded);
        }
        if (footer != null) {
            footer.setVisible(expanded);
            footer.setManaged(expanded);
        }
        
        // Rotate chevron
        if (chevron != null) {
            chevron.setRotate(expanded ? 0 : 180);
        }
    }

    public JCard addClass(String... styleClasses) {
        getStyleClass().addAll(styleClasses);
        return this;
    }
}

