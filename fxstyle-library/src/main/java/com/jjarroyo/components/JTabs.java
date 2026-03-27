package com.jjarroyo.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.HashMap;
import java.util.Map;

public class JTabs extends BorderPane {

    public enum TabStyle {
        LINE("tabs-line"),
        PILLS("tabs-pills");

        private final String styleClass;
        TabStyle(String styleClass) { this.styleClass = styleClass; }
        public String getStyleClass() { return styleClass; }
    }

    private final ObservableList<JTab> tabs = FXCollections.observableArrayList();
    private final ObjectProperty<JTab> selectedTab = new SimpleObjectProperty<>();
    private final ObjectProperty<Side> side = new SimpleObjectProperty<>(Side.TOP);
    private final ObjectProperty<TabStyle> tabStyle = new SimpleObjectProperty<>(TabStyle.LINE);
    private final BooleanProperty headerVisible = new SimpleBooleanProperty(true);

    private Pane headerContainer;
    private final StackPane contentContainer;
    private final Map<JTab, Node> tabHeaderMap = new HashMap<>();

    public JTabs() {
        getStyleClass().add("j-tabs");

        contentContainer = new StackPane();
        contentContainer.getStyleClass().add("j-tabs-content");
        setCenter(contentContainer);

        // Initial Layout
        updateLayout(Side.TOP);

        setupListeners();
        updateStyle(TabStyle.LINE);
    }

    private void setupListeners() {
        tabs.addListener((ListChangeListener<JTab>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (JTab tab : c.getAddedSubList()) {
                        addTabHeader(tab);
                    }
                }
                if (c.wasRemoved()) {
                    for (JTab tab : c.getRemoved()) {
                        removeTabHeader(tab);
                    }
                }
            }
            if (selectedTab.get() == null && !tabs.isEmpty()) {
                selectTab(tabs.get(0));
            }
        });

        selectedTab.addListener((obs, oldTab, newTab) -> {
            if (oldTab != null) {
                Node oldHeader = tabHeaderMap.get(oldTab);
                if (oldHeader != null) oldHeader.getStyleClass().remove("selected");
                if (oldTab.getContent() != null) oldTab.getContent().setVisible(false);
            }
            if (newTab != null) {
                Node newHeader = tabHeaderMap.get(newTab);
                if (newHeader != null) newHeader.getStyleClass().add("selected");
                
                contentContainer.getChildren().clear();
                if (newTab.getContent() != null) {
                    newTab.getContent().setVisible(true);
                    contentContainer.getChildren().add(newTab.getContent());
                }
            }
        });

        side.addListener((obs, oldPos, newPos) -> updateLayout(newPos));
        tabStyle.addListener((obs, oldStyle, newStyle) -> updateStyle(newStyle));
    }

    private void updateStyle(TabStyle style) {
        getStyleClass().removeAll("tabs-line", "tabs-pills");
        getStyleClass().add(style.getStyleClass());
        
        if (headerContainer != null) {
            headerContainer.getStyleClass().removeAll("tabs-line", "tabs-pills");
            headerContainer.getStyleClass().add(style.getStyleClass());
        }
    }

    public BooleanProperty headerVisibleProperty() { return headerVisible; }
    public boolean isHeaderVisible() { return headerVisible.get(); }
    public void setHeaderVisible(boolean visible) { 
        this.headerVisible.set(visible);
        updateLayout(getSide());
    }

    public Node getHeaderNode() {
        return headerContainer;
    }

    private void updateLayout(Side side) {
        // Remove existing header container from layout
        if (headerContainer != null) {
            setTop(null); setBottom(null); setLeft(null); setRight(null);
            setCenter(contentContainer);
        }
        
        // Create new container
        if (side == Side.TOP || side == Side.BOTTOM) {
            headerContainer = new HBox();
            ((HBox)headerContainer).setAlignment(Pos.CENTER_LEFT);
        } else {
            headerContainer = new VBox();
            ((VBox)headerContainer).setAlignment(Pos.TOP_LEFT);
        }
        
        headerContainer.getStyleClass().add("j-tabs-header");
        headerContainer.getStyleClass().add(getTabStyle().getStyleClass());
        
        // Re-create all headers
        tabHeaderMap.clear();
        for (JTab tab : tabs) {
            Node header = createHeaderNode(tab, side);
            tabHeaderMap.put(tab, header);
            headerContainer.getChildren().add(header);
            
            if (tab == selectedTab.get()) {
                header.getStyleClass().add("selected");
            }
        }

        // For TOP/BOTTOM, wrap in a ScrollPane for horizontal scrolling
        if (side == Side.TOP || side == Side.BOTTOM) {
            ScrollPane scroll = new ScrollPane(headerContainer);
            scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroll.setFitToHeight(true);
            scroll.setFitToWidth(true);
            scroll.setPannable(true);
            scroll.setStyle("-fx-background-color:transparent;-fx-background:transparent;-fx-padding:0;");
            scroll.setMinHeight(38);
            scroll.setPrefHeight(38);

            if (isHeaderVisible()) {
                switch (side) {
                    case TOP    -> setTop(scroll);
                    case BOTTOM -> setBottom(scroll);
                    default -> {}
                }
            }
        } else {
            if (isHeaderVisible()) {
                switch (side) {
                    case LEFT  -> setLeft(headerContainer);
                    case RIGHT -> setRight(headerContainer);
                    default -> {}
                }
            }
        }
    }

    private void addTabHeader(JTab tab) {
        if (headerContainer == null) return;
        Node header = createHeaderNode(tab, side.get());
        tabHeaderMap.put(tab, header);
        headerContainer.getChildren().add(header);
    }
    
    private void removeTabHeader(JTab tab) {
        Node header = tabHeaderMap.remove(tab);
        if (headerContainer != null) headerContainer.getChildren().remove(header);
    }

    private Node createHeaderNode(JTab tab, Side side) {
        VBox textContainer = new VBox(2);
        
        if (side == Side.LEFT || side == Side.RIGHT) {
             textContainer.setAlignment(Pos.CENTER_LEFT);
        } else {
             textContainer.setAlignment(Pos.CENTER);
        }
        
        Label title = new Label();
        title.textProperty().bind(tab.titleProperty());
        title.getStyleClass().add("tab-title");
        textContainer.getChildren().add(title);
        
        Label desc = new Label();
        desc.textProperty().bind(tab.descriptionProperty());
        desc.getStyleClass().add("tab-description");
        // Bind visibility to managed state
        desc.visibleProperty().bind(tab.descriptionProperty().isNotEmpty());
        desc.managedProperty().bind(tab.descriptionProperty().isNotEmpty());
        textContainer.getChildren().add(desc);

        // Wrapper
        Pane wrapper;
        if (side == Side.LEFT || side == Side.RIGHT) {
            wrapper = new HBox(textContainer);
            ((HBox)wrapper).setAlignment(Pos.CENTER_LEFT);
            ((HBox)wrapper).setSpacing(10);
        } else {
            HBox hWrapper = new HBox(6);
            hWrapper.setAlignment(Pos.CENTER);
            // Add graphic (icon) if present
            if (tab.getGraphic() != null) {
                hWrapper.getChildren().add(tab.getGraphic());
            }
            hWrapper.getChildren().add(textContainer);
            wrapper = hWrapper;
        }
        
        wrapper.getStyleClass().add("j-tab");
        
        wrapper.setOnMouseClicked(e -> selectTab(tab));
        
        return wrapper;
    }

    public void selectTab(JTab tab) {
        if (tab != null && !tab.isDisable()) {
            selectedTab.set(tab);
        }
    }

    public ObservableList<JTab> getTabs() { return tabs; }
    
    public ObjectProperty<Side> sideProperty() { return side; }
    public Side getSide() { return side.get(); }
    public void setSide(Side s) { this.side.set(s); }
    
    public ObjectProperty<TabStyle> tabStyleProperty() { return tabStyle; }
    public TabStyle getTabStyle() { return tabStyle.get(); }
    public void setTabStyle(TabStyle s) { this.tabStyle.set(s); }
}

