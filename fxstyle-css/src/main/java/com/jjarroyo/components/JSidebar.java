package com.jjarroyo.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList; 
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class JSidebar extends VBox {

    public enum CollapseMode {
        COMPACT, // Icons only
        HIDDEN   // Width 0
    }

    public enum SidebarVariant {
        DEFAULT, // Dark/Standard
        LIGHT    // White background
    }

    private ObservableList<Node> items = FXCollections.observableArrayList();
    private VBox itemsContainer;
    private ScrollPane scrollPane;
    private HBox footerContainer; 
    private Button toggleBtn;

    private BooleanProperty expanded = new SimpleBooleanProperty(true);
    private ObjectProperty<CollapseMode> collapseMode = new SimpleObjectProperty<>(CollapseMode.COMPACT);
    private ObjectProperty<SidebarVariant> variant = new SimpleObjectProperty<>(SidebarVariant.DEFAULT);
    
    private HBox brandContainer;
    private Label brandTitle;
    
    // Widths
    private double expandedWidth = 250;
    private double compactWidth = 70;
    private double hiddenWidth = 0;

    public JSidebar() {
        init();
    }

    private HBox topBar;
    private VBox headerContentWrapper;
    private Node headerNode;

    private void init() {
        getStyleClass().add("j-sidebar");
        setPrefWidth(expandedWidth);
        setMinWidth(Region.USE_PREF_SIZE);
        setMaxWidth(Region.USE_PREF_SIZE);

        // Header content wrapper — full width, above the toggle bar
        headerContentWrapper = new VBox();
        headerContentWrapper.getStyleClass().add("j-sidebar-header-content");

        // Top Bar (Brand + Toggle)
        topBar = new HBox();
        topBar.getStyleClass().add("j-sidebar-header");
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        brandContainer = new HBox(12);
        brandContainer.getStyleClass().add("j-sidebar-brand");
        brandContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(brandContainer, Priority.ALWAYS);

        // Toggle Button (Hamburger)
        toggleBtn = new Button();
        toggleBtn.getStyleClass().add("j-sidebar-toggle");
        toggleBtn.setGraphic(JIcon.MENU.view()); // Use Hamburger
        toggleBtn.setOnAction(e -> setExpanded(!isExpanded()));
        
        topBar.getChildren().addAll(brandContainer, toggleBtn);

        // Scrollable Items Container
        itemsContainer = new VBox();
        itemsContainer.getStyleClass().add("j-sidebar-items");
        
        scrollPane = new ScrollPane(itemsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Disable horizontal scroll
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("j-sidebar-scroll");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Footer Container
        footerContainer = new HBox();
        footerContainer.getStyleClass().add("j-sidebar-footer");
        footerContainer.setAlignment(Pos.CENTER_LEFT);
        footerContainer.setManaged(false);
        footerContainer.setVisible(false);

        getChildren().addAll(headerContentWrapper, topBar, scrollPane, footerContainer);

        // Items Listener
        items.addListener((ListChangeListener<Node>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                   itemsContainer.getChildren().addAll(c.getAddedSubList());
                }
                if (c.wasRemoved()) {
                    itemsContainer.getChildren().removeAll(c.getRemoved());
                }
                checkAutoCollapseMode();
            }
        });
        
        // Animation Listener
        expanded.addListener((obs, oldV, newV) -> animateState(newV));
    }

    private void checkAutoCollapseMode() {
        boolean hasIcons = items.stream()
            .filter(node -> node instanceof JSidebarItem)
            .map(node -> (JSidebarItem) node)
            .anyMatch(item -> item.getIcon() != null);
        
        if (hasIcons) {
             setCollapseMode(CollapseMode.COMPACT);
        } else {
             setCollapseMode(CollapseMode.HIDDEN);
        }
    }

    private void animateState(boolean isExpanded) {
        double targetWidth = isExpanded ? expandedWidth : 
                             (getCollapseMode() == CollapseMode.COMPACT ? compactWidth : hiddenWidth);
        
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(prefWidthProperty(), targetWidth);
        KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
        timeline.getKeyFrames().add(kf);
        
        // Handle content visibility
        if (!isExpanded) {
             getStyleClass().add("collapsed");
             if (headerContentWrapper != null) {
                 headerContentWrapper.setVisible(false);
                 headerContentWrapper.setManaged(false);
             }
             if (brandContainer != null) {
                 brandContainer.setVisible(false);
                 brandContainer.setManaged(false);
             }
             if (getCollapseMode() == CollapseMode.COMPACT) getStyleClass().add("compact");
             else getStyleClass().add("hidden");
        } else {
             getStyleClass().remove("collapsed");
             if (headerContentWrapper != null) {
                 headerContentWrapper.setVisible(true);
                 headerContentWrapper.setManaged(true);
             }
             if (brandContainer != null) {
                 brandContainer.setVisible(true);
                 brandContainer.setManaged(true);
             }
             getStyleClass().remove("compact");
             getStyleClass().remove("hidden");
        }

        timeline.play();
    }
    
    public void setHeader(Node header) {
        this.headerNode = header;
        headerContentWrapper.getChildren().clear();
        if (header != null) {
            headerContentWrapper.getChildren().add(header);
            headerContentWrapper.setVisible(true);
            headerContentWrapper.setManaged(true);
        } else {
            headerContentWrapper.setVisible(false);
            headerContentWrapper.setManaged(false);
        }
    }

    public void setBrand(Node logo, String title) {
        brandContainer.getChildren().clear();
        if (logo != null) {
            brandContainer.getChildren().add(logo);
        }
        if (title != null && !title.isEmpty()) {
            brandTitle = new Label(title);
            brandTitle.getStyleClass().add("j-sidebar-brand-text");
            brandContainer.getChildren().add(brandTitle);
        }
    }

    public void setFooter(Node footer) {
        footerContainer.getChildren().clear();
        if (footer != null) {
            footerContainer.getChildren().add(footer);
            footerContainer.setManaged(true);
            footerContainer.setVisible(true);
        } else {
            footerContainer.setManaged(false);
            footerContainer.setVisible(false);
        }
    }

    public void setVariant(SidebarVariant variant) {
        this.variant.set(variant);
        getStyleClass().removeAll("sidebar-default", "sidebar-light", "light");
        if (variant == SidebarVariant.LIGHT) {
            getStyleClass().add("light");
        } else {
            getStyleClass().add("sidebar-default");
        }
    }
    
    public ObservableList<Node> getItems() {
        return items;
    }
    
    public boolean isExpanded() {
        return expanded.get();
    }

    public void setExpanded(boolean expanded) {
        this.expanded.set(expanded);
    }
    
    public BooleanProperty expandedProperty() {
        return expanded;
    }
    
    public CollapseMode getCollapseMode() {
        return collapseMode.get();
    }
    
    public void setCollapseMode(CollapseMode mode) {
        this.collapseMode.set(mode);
    }
    
    public ObjectProperty<CollapseMode> collapseModeProperty() {
        return collapseMode;
    }
}

