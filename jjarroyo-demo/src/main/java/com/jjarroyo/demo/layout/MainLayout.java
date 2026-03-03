package com.jjarroyo.demo.layout;

import com.jjarroyo.demo.views.ButtonsView;
import com.jjarroyo.demo.views.LabelsView;
import com.jjarroyo.demo.views.AlertsView;
import com.jjarroyo.demo.views.SelectsView;
import com.jjarroyo.demo.views.ChecksRadiosView;
import com.jjarroyo.demo.views.InputsView;
import com.jjarroyo.demo.views.CardsView;
import com.jjarroyo.demo.views.TabsView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.jjarroyo.components.JHeader;
import com.jjarroyo.components.JSidebar;
import com.jjarroyo.components.JSidebarItem;
import com.jjarroyo.components.JIcon;

public class MainLayout extends javafx.scene.layout.StackPane {
    
    private static MainLayout instance;
    private BorderPane mainContainer;
    private javafx.scene.layout.StackPane modalContainer;
    
    private com.jjarroyo.components.JSidebar sidebar;
    private VBox contentArea;

    public MainLayout() {
        instance = this;
        getStyleClass().add("root");
        
        // 1. Main Layout (BorderPane)
        mainContainer = new BorderPane();
        
        // Setup Regions
        JHeader header = createHeader();
        mainContainer.setTop(header);
        
        sidebar = createSidebar();
        mainContainer.setLeft(sidebar);
        
        contentArea = createContentArea();
        mainContainer.setCenter(contentArea);
        
        // 2. Modal Container (Overlay)
        modalContainer = new javafx.scene.layout.StackPane();
        modalContainer.setPickOnBounds(false); 
        // Register modal container with the library
        com.jjarroyo.JJArroyo.setModalContainer(modalContainer);
        
        // Add to StackPane
        getChildren().addAll(mainContainer, modalContainer);
        
        // Load Default View
        navigate(new com.jjarroyo.demo.views.DashboardView());
        updateActiveItem("Dashboard");
    }
    
    public static MainLayout getInstance() { return instance; }
    public javafx.scene.layout.StackPane getModalContainer() { return modalContainer; } // Access for JModal

    private com.jjarroyo.components.JSidebar createSidebar() {
        com.jjarroyo.components.JSidebar sidebar = new com.jjarroyo.components.JSidebar();
        
        // Header
        VBox header = new VBox();
        // header.getStyleClass().add("j-sidebar-header"); // Removed to avoid double padding
        Label title = new Label("JJARROYO");
        title.getStyleClass().add("sidebar-title"); // Keep font style
        // style removed, handled by CSS class .sidebar-title
        Label subtitle = new Label("JavaFX Edition");
        subtitle.getStyleClass().addAll("text-sm", "text-slate-500");
        header.getChildren().addAll(title, subtitle);
        sidebar.setHeader(header);
        
        // Items
        sidebar.getItems().addAll(
            createItem("Dashboard", com.jjarroyo.components.JIcon.HOME.view()),
            createItem("Buttons", com.jjarroyo.components.JIcon.ROCKET.view()),
            createItem("Labels", com.jjarroyo.components.JIcon.FLAG.view()),
            createItem("Inputs", com.jjarroyo.components.JIcon.EDIT.view()),
            createItem("Cards", com.jjarroyo.components.JIcon.LAYERS.view()),
            createItem("Alerts", com.jjarroyo.components.JIcon.BELL.view()),
            createItem("Selects", com.jjarroyo.components.JIcon.LIST.view()),
            createItem("Sliders", com.jjarroyo.components.JIcon.SETTINGS.view()), // Using SETTINGS icon for sliders
            createItem("Accordions", com.jjarroyo.components.JIcon.LIST.view()),   // Formatting fixed
            createItem("Progress", com.jjarroyo.components.JIcon.REFRESH.view()),  // REFRESH icon for progress/loading
            createItem("Date Pickers", com.jjarroyo.components.JIcon.CALENDAR.view()), 
            createItem("Files", com.jjarroyo.components.JIcon.FOLDER.view()), 
            createItem("Checks & Radios", com.jjarroyo.components.JIcon.CHECK_CIRCLE.view()),
            createItem("Tabs", com.jjarroyo.components.JIcon.LAYOUT.view()),
            createItem("Modals", com.jjarroyo.components.JIcon.CHAT.view()),
            createItem("Data", com.jjarroyo.components.JIcon.LAYOUT.view()),
            createItem("Toasts", com.jjarroyo.components.JIcon.NOTIFICATIONS.view()),
            createItem("Popovers", com.jjarroyo.components.JIcon.CHAT.view()),
            createItem("Icons", com.jjarroyo.components.JIcon.APPS.view()),
            createItem("Typography", com.jjarroyo.components.JIcon.FILE_TEXT.view()),
            createItem("Dropdowns", com.jjarroyo.components.JIcon.LIST.view()),
            createItem("Avatars", com.jjarroyo.components.JIcon.PERSON.view()),
            createItem("Breadcrumbs", com.jjarroyo.components.JIcon.ARROW_FORWARD.view()),
            createItem("Chips", com.jjarroyo.components.JIcon.TAG.view()),
            createItem("Number Input", com.jjarroyo.components.JIcon.ADD.view()),
            createItem("Search Input", com.jjarroyo.components.JIcon.SEARCH.view()),
            createItem("Skeletons", com.jjarroyo.components.JIcon.APPS.view()),
            createItem("Steppers", com.jjarroyo.components.JIcon.ARROW_FORWARD.view()),
            createItem("Timelines", com.jjarroyo.components.JIcon.HOME.view()), // Using HOME temporarily as CLOCK isn't in JIcon
            createItem("StatCards", com.jjarroyo.components.JIcon.MONEY.view()),
            createItem("Avanzado", com.jjarroyo.components.JIcon.SETTINGS.view())
        );
        
        return sidebar;
    }

    private com.jjarroyo.components.JSidebarItem createItem(String text, Node icon) {
        com.jjarroyo.components.JSidebarItem item = new com.jjarroyo.components.JSidebarItem(text, icon);
        item.setAction(() -> {
            handleNavigation(text);
            updateActiveState(item);
        });
        return item;
    }

    private void handleNavigation(String text) {
        if (text.equals("Dashboard")) navigate(new com.jjarroyo.demo.views.DashboardView());
        else if (text.equals("Buttons")) navigate(new ButtonsView());
        else if (text.equals("Labels")) navigate(new LabelsView());
        else if (text.equals("Inputs")) navigate(new InputsView());
        else if (text.equals("Cards")) navigate(new CardsView());
        else if (text.equals("Alerts")) navigate(new AlertsView());
        else if (text.equals("Selects")) navigate(new SelectsView());
        else if (text.equals("Checks & Radios")) navigate(new ChecksRadiosView());
        else if (text.equals("Tabs")) navigate(new TabsView());
        else if (text.equals("Files")) navigate(new com.jjarroyo.demo.views.FilesView());
        else if (text.equals("Modals")) navigate(new com.jjarroyo.demo.views.ModalsView());
        else if (text.equals("Data")) navigate(new com.jjarroyo.demo.views.DataView());
        else if (text.equals("Toasts")) navigate(new com.jjarroyo.demo.views.ToastsView());
        else if (text.equals("Popovers")) navigate(new com.jjarroyo.demo.views.PopoversView());
        else if (text.equals("Icons")) navigate(new com.jjarroyo.demo.views.IconsView());
        else if (text.equals("Typography")) navigate(new com.jjarroyo.demo.views.TypographyView());
        else if (text.equals("Sliders")) navigate(new com.jjarroyo.demo.views.SlidersView());
        else if (text.equals("Accordions")) navigate(new com.jjarroyo.demo.views.AccordionsView());
        else if (text.equals("Progress")) navigate(new com.jjarroyo.demo.views.ProgressView());
        else if (text.equals("Date Pickers")) navigate(new com.jjarroyo.demo.views.DatePickersView());
        else if (text.equals("Dropdowns")) navigate(new com.jjarroyo.demo.views.DropdownsView());
        else if (text.equals("Avatars")) navigate(new com.jjarroyo.demo.views.AvatarView());
        else if (text.equals("Breadcrumbs")) navigate(new com.jjarroyo.demo.views.BreadcrumbView());
        else if (text.equals("Chips")) navigate(new com.jjarroyo.demo.views.ChipView());
        else if (text.equals("Number Input")) navigate(new com.jjarroyo.demo.views.NumberInputView());
        else if (text.equals("Search Input")) navigate(new com.jjarroyo.demo.views.SearchView());
        else if (text.equals("Skeletons")) navigate(new com.jjarroyo.demo.views.SkeletonView());
        else if (text.equals("Steppers")) navigate(new com.jjarroyo.demo.views.StepperView());
        else if (text.equals("Timelines")) navigate(new com.jjarroyo.demo.views.TimelineView());
        else if (text.equals("StatCards")) navigate(new com.jjarroyo.demo.views.StatCardView());
        else if (text.equals("Avanzado")) navigate(new com.jjarroyo.demo.views.AdvancedView());
        else {
            System.out.println("Navigating to " + text);
        }
    }
    
    private void updateActiveState(com.jjarroyo.components.JSidebarItem activeItem) {
        for (Node node : sidebar.getItems()) {
            if (node instanceof com.jjarroyo.components.JSidebarItem) {
                com.jjarroyo.components.JSidebarItem item = (com.jjarroyo.components.JSidebarItem) node;
                item.setActive(item == activeItem);
            }
        }
    }

    private void updateActiveItem(String text) {
        for (Node node : sidebar.getItems()) {
            if (node instanceof com.jjarroyo.components.JSidebarItem) {
                com.jjarroyo.components.JSidebarItem item = (com.jjarroyo.components.JSidebarItem) node;
                if (item.getText().equals(text)) {
                    item.setActive(true);
                } else {
                    item.setActive(false);
                }
            }
        }
    }

    private VBox createContentArea() {
        contentArea = new VBox();
        contentArea.getStyleClass().add("j-content-area"); 
        contentArea.setPadding(new Insets(30)); 
        contentArea.setSpacing(30);
        return contentArea;
    }

    public void navigate(Node view) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(view);
        VBox.setVgrow(view, javafx.scene.layout.Priority.ALWAYS);
    }

    private void toggleTheme() {
        if (getStyleClass().contains("dark")) {
            getStyleClass().remove("dark");
            // Update icon in header if accessible, or just let the button update itself
        } else {
            getStyleClass().add("dark");
        }
    }

    private com.jjarroyo.components.JHeader createHeader() {
        com.jjarroyo.components.JHeader header = new com.jjarroyo.components.JHeader();
        
        // Brand
        header.setBrand(null, "JJArroyoFX");
        
        // Menu
        header.addMenuItem("Dashboard", () -> navigate(new com.jjarroyo.demo.views.DashboardView()));
        header.addMenuItem("Reports", () -> System.out.println("Reports clicked"));
        
        java.util.Map<String, Runnable> appsMenu = new java.util.HashMap<>();
        appsMenu.put("Project Manager", () -> System.out.println("Project Manager"));
        appsMenu.put("eCommerce", () -> System.out.println("eCommerce"));
        appsMenu.put("CRM", () -> System.out.println("CRM"));
        header.addMenuDropdown("Apps", appsMenu);
        
        // Theme Toggle
        Label themeBtn = new Label();
        themeBtn.setGraphic(com.jjarroyo.components.JIcon.MOON.view());
        themeBtn.getStyleClass().add("j-header-menu-item");
        themeBtn.setStyle("-fx-cursor: hand; -fx-padding: 8px;");
        themeBtn.setOnMouseClicked(e -> {
            toggleTheme();
            boolean isDark = getStyleClass().contains("dark");
            themeBtn.setGraphic((isDark ? com.jjarroyo.components.JIcon.SUN : com.jjarroyo.components.JIcon.MOON).view());
        });
        header.addToolbarItem(themeBtn);
        
        // Profile
        header.setUserProfile("Sean Bean", "sean@jjarroyo.com", "S");
        
        return header;
    }
}


