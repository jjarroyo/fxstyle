package com.jjarroyo.demo.views;

import com.jjarroyo.components.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DashboardView extends ScrollPane {

    public DashboardView() {
        getStyleClass().add("j-scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));
        
        VBox content = new VBox();
        content.setSpacing(24);
        
        // 1. Page Header
        VBox header = new VBox(8);
        Label title = new Label("Dashboard");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Overview of your business performance.");
        subtitle.getStyleClass().add("page-subtitle");
        header.getChildren().addAll(title, subtitle);
        content.getChildren().add(header);
        
        // 2. Stats Cards (Grid)
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(24);
        statsGrid.setVgap(24);
        
        statsGrid.add(createStatCard("Total Revenue", "$45,231.89", "12% more than last month", "primary"), 0, 0);
        statsGrid.add(createStatCard("Active Users", "1,294", "5% increase", "success"), 1, 0);
        statsGrid.add(createStatCard("New Orders", "854", "18% less than last week", "danger"), 2, 0);
        statsGrid.add(createStatCard("Support Tickets", "12", "All caught up!", "info"), 3, 0);
        
        // Make columns equal width
        javafx.scene.layout.ColumnConstraints cc = new javafx.scene.layout.ColumnConstraints();
        cc.setPercentWidth(25);
        statsGrid.getColumnConstraints().addAll(cc, cc, cc, cc);
        
        content.getChildren().add(statsGrid);
        
        // 3. Layout: Table vs List
        HBox mainRow = new HBox(24);
        
        // Recent Orders Table (66%)
        VBox tableSection = new VBox(16);
        Label tableTitle = new Label("Recent Orders");
        tableTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        JTable<Order> table = new JTable<>();
        table.addColumn("Order ID", "id");
        table.addColumn("Customer", "customer");
        table.addColumn("Status", "status");
        table.addColumn("Total", "total");
        
        // Dummy table data
        for (int i=1001; i<1010; i++) {
            table.addItem(new Order(
                "#" + i, 
                "Customer " + (char)('A' + (i%5)), 
                (i%2==0 ? "Completed" : "Pending"), 
                "$" + (i * 12.5)
            ));
        }
        
        tableSection.getChildren().addAll(tableTitle, table);
        HBox.setHgrow(tableSection, Priority.ALWAYS);
        
        // Activity Feed (33%)
        VBox listSection = new VBox(16);
        listSection.setMinWidth(350);
        listSection.setPrefWidth(350);
        Label listTitle = new Label("Activity Feed");
        listTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        JList<Activity> list = new JList<>();
        list.setCellFactory(lv -> new javafx.scene.control.ListCell<Activity>() {
            @Override
            protected void updateItem(Activity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    VBox box = new VBox(4);
                    Label msg = new Label(item.message);
                    msg.setStyle("-fx-font-weight: 600; -fx-text-fill: -color-slate-700;");
                    Label time = new Label(item.time);
                    time.setStyle("-fx-font-size: 11px; -fx-text-fill: -color-slate-400;");
                    box.getChildren().addAll(msg, time);
                    setGraphic(box);
                }
            }
        });
        
        list.addItem(new Activity("New user registered", "2 mins ago"));
        list.addItem(new Activity("Order #1023 placed", "15 mins ago"));
        list.addItem(new Activity("Server reboot", "1 hour ago"));
        list.addItem(new Activity("Payment received", "2 hours ago"));
        list.addItem(new Activity("New feature deployed", "5 hours ago"));
        
        listSection.getChildren().addAll(listTitle, list);
        
        mainRow.getChildren().addAll(tableSection, listSection);
        content.getChildren().add(mainRow);
        
        setContent(content);
    }
    
    private JCard createStatCard(String title, String value, String subtitle, String colorTheme) {
        JCard card = new JCard(); // Fixed constructor
        VBox body = new VBox(8);
        
        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-text-fill: -color-slate-500; -fx-font-size: 13px; -fx-font-weight: 600;");
        
        Label valueLbl = new Label(value);
        valueLbl.setStyle("-fx-text-fill: -color-slate-800; -fx-font-size: 24px; -fx-font-weight: 700;");
        
        Label subLbl = new Label(subtitle);
        // Simple heuristic for color
        String color = "-color-slate-400";
        if (colorTheme.equals("success")) color = "-color-success-600";
        if (colorTheme.equals("danger")) color = "-color-danger-600";
        if (colorTheme.equals("primary")) color = "-color-primary-600";
        subLbl.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 12px;");
        
        body.getChildren().addAll(titleLbl, valueLbl, subLbl);
        card.setBody(body);
        return card;
    }
    
    // Simple POJO for table
    public static class Order {
        private String id;
        private String customer;
        private String status;
        private String total;
        
        public Order(String id, String c, String s, String t) {
            this.id = id; this.customer = c; this.status = s; this.total = t;
        }
        
        public String getId() { return id; }
        public String getCustomer() { return customer; }
        public String getStatus() { return status; }
        public String getTotal() { return total; }
    }
    
    public static class Activity {
        public String message;
        public String time;
        public Activity(String m, String t) { message = m; time = t; }
    }
}


