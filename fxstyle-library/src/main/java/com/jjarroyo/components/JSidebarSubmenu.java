package com.jjarroyo.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class JSidebarSubmenu extends VBox {

    private HBox headerContainer;
    private Node icon;
    private Label label;
    private Node arrowIcon;
    private VBox itemsContainer;
    private JPopover activePopover;

    private BooleanProperty expanded = new SimpleBooleanProperty(false);
    private BooleanProperty active = new SimpleBooleanProperty(false);
    private ObservableList<JSidebarItem> items = FXCollections.observableArrayList();
    private Runnable action;

    public JSidebarSubmenu(String text) {
        this(text, null);
    }

    public JSidebarSubmenu(String text, Node icon) {
        this.icon = icon;
        this.label = new Label(text);
        init();
    }

    private void init() {
        getStyleClass().add("j-sidebar-submenu");

        // Header Row
        headerContainer = new HBox(12);
        headerContainer.getStyleClass().add("j-sidebar-submenu-header");
        headerContainer.setAlignment(Pos.CENTER_LEFT);

        if (icon != null) {
            headerContainer.getChildren().add(icon);
        }

        label.getStyleClass().add("j-sidebar-item-label");
        headerContainer.getChildren().add(label);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerContainer.getChildren().add(spacer);

        updateArrow();

        // Items container
        itemsContainer = new VBox(4);
        itemsContainer.getStyleClass().add("j-sidebar-submenu-items");
        itemsContainer.setVisible(false);
        itemsContainer.setManaged(false);

        getChildren().addAll(headerContainer, itemsContainer);

        // Subitems listener
        items.addListener((ListChangeListener<JSidebarItem>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (JSidebarItem item : c.getAddedSubList()) {
                        if (!item.getStyleClass().contains("j-sidebar-subitem")) {
                            item.getStyleClass().add("j-sidebar-subitem");
                        }
                        itemsContainer.getChildren().add(item);
                    }
                }
                if (c.wasRemoved()) {
                    itemsContainer.getChildren().removeAll(c.getRemoved());
                }
            }
        });

        // Header Click Event -> Toggle Submenu (in normal mode) or Show Popover (in compact mode)
        headerContainer.setOnMouseClicked(e -> {
            if (isSidebarCollapsed()) {
                showCompactPopover();
            } else {
                setExpanded(!isExpanded());
                if (action != null) {
                    action.run();
                }
            }
        });

        expanded.addListener((obs, oldV, newV) -> {
            if (!isSidebarCollapsed()) {
                itemsContainer.setVisible(newV);
                itemsContainer.setManaged(newV);
            }
            updateArrow();
            if (newV) {
                getStyleClass().add("expanded");
            } else {
                getStyleClass().remove("expanded");
            }
        });

        active.addListener((obs, oldV, newV) -> {
            if (newV) {
                if (!getStyleClass().contains("active")) {
                    getStyleClass().add("active");
                }
            } else {
                getStyleClass().remove("active");
            }
        });
    }

    private boolean isSidebarCollapsed() {
        Node current = getParent();
        while (current != null) {
            if (current instanceof JSidebar) {
                return !((JSidebar) current).isExpanded();
            }
            current = current.getParent();
        }
        return getStyleClass().contains("collapsed") || getStyleClass().contains("compact");
    }

    private void showCompactPopover() {
        if (activePopover != null && activePopover.isShowing()) {
            activePopover.hide();
            return;
        }

        activePopover = new JPopover();
        activePopover.setPosition(JPopover.Position.RIGHT);
        activePopover.setTitle(label.getText());

        VBox popoverContent = new VBox(4);
        popoverContent.setPadding(new Insets(6, 4, 6, 4));

        for (JSidebarItem originalItem : items) {
            JSidebarItem popItem = new JSidebarItem(originalItem.getText(), originalItem.getIcon());
            popItem.getStyleClass().add("j-sidebar-popover-item");
            popItem.setAction(() -> {
                if (activePopover != null) {
                    activePopover.hide();
                }
                originalItem.setActive(true);
                // Trigger item click
                if (originalItem.getOnMouseClicked() != null) {
                    originalItem.getOnMouseClicked().handle(null);
                }
            });
            popoverContent.getChildren().add(popItem);
        }

        activePopover.setContentNode(popoverContent);
        activePopover.show(headerContainer);

        Node current = getParent();
        JSidebar sidebar = null;
        while (current != null) {
            if (current instanceof JSidebar) {
                sidebar = (JSidebar) current;
                break;
            }
            current = current.getParent();
        }

        if (sidebar != null && headerContainer.getScene() != null) {
            javafx.geometry.Bounds sBounds = sidebar.localToScreen(sidebar.getBoundsInLocal());
            javafx.geometry.Bounds hBounds = headerContainer.localToScreen(headerContainer.getBoundsInLocal());
            if (sBounds != null && hBounds != null) {
                activePopover.setAnchorX(sBounds.getMaxX() + 2);
                activePopover.setAnchorY(hBounds.getMinY() + (hBounds.getHeight() / 2) - (activePopover.getHeight() / 2));
            }
        }
    }

    private void updateArrow() {
        if (headerContainer == null) return;
        if (arrowIcon != null) {
            headerContainer.getChildren().remove(arrowIcon);
        }
        JIcon arrowEnum = isExpanded() ? JIcon.EXPAND_LESS : JIcon.EXPAND_MORE;
        arrowIcon = arrowEnum.view();
        arrowIcon.getStyleClass().add("j-sidebar-submenu-arrow");
        headerContainer.getChildren().add(arrowIcon);
    }

    public void addSubItem(JSidebarItem item) {
        items.add(item);
    }

    public ObservableList<JSidebarItem> getItems() {
        return items;
    }

    public void setAction(Runnable action) {
        this.action = action;
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

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public String getText() {
        return label.getText();
    }

    public Node getIcon() {
        return icon;
    }
}
