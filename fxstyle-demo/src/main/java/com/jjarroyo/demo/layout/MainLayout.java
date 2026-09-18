package com.jjarroyo.demo.layout;

import com.jjarroyo.FxStyle;
import com.jjarroyo.components.JHeader;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JNotification;
import com.jjarroyo.components.JSidebar;
import com.jjarroyo.components.JSidebarItem;
import com.jjarroyo.demo.views.AlertsView;
import com.jjarroyo.demo.views.ButtonsView;
import com.jjarroyo.demo.views.CardsView;
import com.jjarroyo.demo.views.ChecksRadiosView;
import com.jjarroyo.demo.views.FloatingButtonView;
import com.jjarroyo.demo.views.InputsView;
import com.jjarroyo.demo.views.LabelsView;
import com.jjarroyo.demo.views.SelectsView;
import com.jjarroyo.demo.views.TabsView;
import com.jjarroyo.demo.views.TitleBarView;
import java.time.LocalDateTime;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainLayout extends StackPane {
    
    private static MainLayout instance;
    private BorderPane mainContainer;
    private StackPane modalContainer;
    
    private JSidebar sidebar;
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
        modalContainer = new StackPane();
        modalContainer.setPickOnBounds(false); 
        // Register modal container with the library
        FxStyle.setModalContainer(modalContainer);
        
        // Add to StackPane
        getChildren().addAll(mainContainer, modalContainer);
        
        // Load Default View
        navigate(new com.jjarroyo.demo.views.DashboardView());
        updateActiveItem("Dashboard");
    }
    
    public static MainLayout getInstance() { return instance; }
    public StackPane getModalContainer() { return modalContainer; } // Access for JModal

    private JSidebar createSidebar() {
        JSidebar sidebar = new JSidebar();
        
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
            createItem("Dashboard", JIcon.HOME.view()),
            createItem("Buttons", JIcon.ROCKET.view()),
            createItem("Floating Buttons", JIcon.ROCKET.view()),
            createItem("Badges", JIcon.TAG.view()),
            createItem("Inputs", JIcon.EDIT.view()),
            createItem("Cards", JIcon.LAYERS.view()),
            createItem("Alerts", JIcon.BELL.view()),
            createItem("Selects", JIcon.LIST.view()),
            createItem("Sliders", JIcon.SETTINGS.view()), // Using SETTINGS icon for sliders
            createItem("Rating", JIcon.STAR.view()),
            createItem("Accordions", JIcon.LIST.view()),   // Formatting fixed
            createItem("Progress", JIcon.REFRESH.view()),  // REFRESH icon for progress/loading
            createItem("Date Pickers", JIcon.CALENDAR.view()), 
            createItem("Calendars", JIcon.CALENDAR.view()), 
            createItem("Files", JIcon.FOLDER.view()), 
            createItem("Checks & Radios", JIcon.CHECK_CIRCLE.view()),
            createItem("Tabs", JIcon.LAYOUT.view()),
            createItem("Modals", JIcon.CHAT.view()),
            createItem("Data", JIcon.LAYOUT.view()),
            createItem("Toasts", JIcon.NOTIFICATIONS.view()),
            createItem("Toast Cards", JIcon.BELL.view()),
            createItem("Tooltips", JIcon.CHAT.view()),
            createItem("Popovers", JIcon.CHAT.view()),
            createItem("Icons", JIcon.APPS.view()),
            createItem("Typography", JIcon.FILE_TEXT.view()),
            createItem("Dropdowns", JIcon.LIST.view()),
            createItem("Avatars", JIcon.PERSON.view()),
            createItem("Breadcrumbs", JIcon.ARROW_FORWARD.view()),
            createItem("Chips", JIcon.TAG.view()),
            createItem("Status Badge", JIcon.CHECK_CIRCLE.view()),
            createItem("Ribbons", JIcon.TAG.view()),
            createItem("Tag Input", JIcon.EDIT.view()),
            createItem("Number Input", JIcon.ADD.view()),
            createItem("Search Input", JIcon.SEARCH.view()),
            createItem("Skeletons", JIcon.APPS.view()),
            createItem("Steppers", JIcon.ARROW_FORWARD.view()),
            createItem("Timelines", JIcon.HOME.view()), // Using HOME temporarily as CLOCK isn't in JIcon
            createItem("StatCards", JIcon.MONEY.view()),
            createItem("TitleBar", JIcon.MONITOR.view()),
            createItem("Confirm Dialogs", JIcon.CHECK_CIRCLE.view()),
            createItem("Drawers", JIcon.LAYERS.view()),
            createItem("Charts", JIcon.BAR_CHART.view()),
            createItem("Animaciones", JIcon.ROCKET.view()),
            createItem("Avanzado", JIcon.SETTINGS.view()),
            createItem("Tree View", JIcon.FOLDER_OPEN.view())
        );
        
        return sidebar;
    }

    private JSidebarItem createItem(String text, Node icon) {
        JSidebarItem item = new JSidebarItem(text, icon);
        item.setAction(() -> {
            handleNavigation(text);
            updateActiveState(item);
        });
        return item;
    }

    private void handleNavigation(String text) {
        if (text.equals("Dashboard")) navigate(new com.jjarroyo.demo.views.DashboardView());
        else if (text.equals("Buttons")) navigate(new ButtonsView());
        else if (text.equals("Floating Buttons")) navigate(new FloatingButtonView());
        else if (text.equals("Badges") || text.equals("Labels")) navigate(new LabelsView());
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
        else if (text.equals("Toast Cards")) navigate(new com.jjarroyo.demo.views.ToastCardView());
        else if (text.equals("Tooltips")) navigate(new com.jjarroyo.demo.views.TooltipView());
        else if (text.equals("Popovers")) navigate(new com.jjarroyo.demo.views.PopoversView());
        else if (text.equals("Icons")) navigate(new com.jjarroyo.demo.views.IconsView());
        else if (text.equals("Typography")) navigate(new com.jjarroyo.demo.views.TypographyView());
        else if (text.equals("Sliders")) navigate(new com.jjarroyo.demo.views.SlidersView());
        else if (text.equals("Rating")) navigate(new com.jjarroyo.demo.views.RatingView());
        else if (text.equals("Accordions")) navigate(new com.jjarroyo.demo.views.AccordionsView());
        else if (text.equals("Progress")) navigate(new com.jjarroyo.demo.views.ProgressView());
        else if (text.equals("Date Pickers")) navigate(new com.jjarroyo.demo.views.DatePickersView());
        else if (text.equals("Calendars")) navigate(new com.jjarroyo.demo.views.CalendarView());
        else if (text.equals("Dropdowns")) navigate(new com.jjarroyo.demo.views.DropdownsView());
        else if (text.equals("Avatars")) navigate(new com.jjarroyo.demo.views.AvatarView());
        else if (text.equals("Breadcrumbs")) navigate(new com.jjarroyo.demo.views.BreadcrumbView());
        else if (text.equals("Chips")) navigate(new com.jjarroyo.demo.views.ChipView());
        else if (text.equals("Status Badge")) navigate(new com.jjarroyo.demo.views.StatusBadgeView());
        else if (text.equals("Ribbons")) navigate(new com.jjarroyo.demo.views.RibbonView());
        else if (text.equals("Tag Input")) navigate(new com.jjarroyo.demo.views.TagInputView());
        else if (text.equals("Number Input")) navigate(new com.jjarroyo.demo.views.NumberInputView());
        else if (text.equals("Search Input")) navigate(new com.jjarroyo.demo.views.SearchView());
        else if (text.equals("Skeletons")) navigate(new com.jjarroyo.demo.views.SkeletonView());
        else if (text.equals("Steppers")) navigate(new com.jjarroyo.demo.views.StepperView());
        else if (text.equals("Timelines")) navigate(new com.jjarroyo.demo.views.TimelineView());
        else if (text.equals("StatCards")) navigate(new com.jjarroyo.demo.views.StatCardView());
        else if (text.equals("TitleBar")) navigate(new TitleBarView());
        else if (text.equals("Confirm Dialogs")) navigate(new com.jjarroyo.demo.views.ConfirmDialogView());
        else if (text.equals("Drawers")) navigate(new com.jjarroyo.demo.views.DrawerView());
        else if (text.equals("Tree View")) navigate(new com.jjarroyo.demo.views.TreeViewView());
        else if (text.equals("Charts")) navigate(new com.jjarroyo.demo.views.ChartView());
        else if (text.equals("Animaciones")) navigate(new com.jjarroyo.demo.views.AnimationsView());
        else if (text.equals("Avanzado")) navigate(new com.jjarroyo.demo.views.AdvancedView());
        else {
            System.out.println("Navigating to " + text);
        }
    }
    
    private void updateActiveState(JSidebarItem activeItem) {
        for (Node node : sidebar.getItems()) {
            if (node instanceof JSidebarItem) {
                JSidebarItem item = (JSidebarItem) node;
                item.setActive(item == activeItem);
            }
        }
    }

    private void updateActiveItem(String text) {
        for (Node node : sidebar.getItems()) {
            if (node instanceof JSidebarItem) {
                JSidebarItem item = (JSidebarItem) node;
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
        VBox.setVgrow(view, Priority.ALWAYS);
    }

    private void toggleTheme() {
        if (getStyleClass().contains("dark")) {
            getStyleClass().remove("dark");
            // Update icon in header if accessible, or just let the button update itself
        } else {
            getStyleClass().add("dark");
        }
    }

    private JHeader createHeader() {
        JHeader header = new JHeader();
        
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
        themeBtn.setGraphic(JIcon.MOON.view());
        themeBtn.getStyleClass().add("j-header-menu-item");
        themeBtn.setStyle("-fx-cursor: hand; -fx-padding: 8px;");
        themeBtn.setOnMouseClicked(e -> {
            toggleTheme();
            boolean isDark = getStyleClass().contains("dark");
            themeBtn.setGraphic((isDark ? JIcon.SUN : JIcon.MOON).view());
        });
        header.addToolbarItem(themeBtn);

        // Notifications
        JNotification notifications = new JNotification();
        notifications.addNotification("Nueva Venta", "Se ha realizado una venta de $500.00", "https://avatar.iran.liara.run/public/job/operator/male", LocalDateTime.now().minusMinutes(5));
        notifications.addNotification("Error de Sistema", "El servidor de base de datos no responde adecuadamente a las peticiones.", null, LocalDateTime.now().minusHours(2));
        notifications.addNotification("Actualización Disponible", "Hay una nueva versión de FxStyle disponible para descargar.", "https://avatar.iran.liara.run/public/job/designer/female", LocalDateTime.now().minusDays(1));
        header.addToolbarItem(notifications);
        
        // Profile
        header.setUserProfile("Sean Bean", "sean@jjarroyo.com", "S");
        
        return header;
    }
}


