package com.jjarroyo.demo.views;

import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JStatCard;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class StatCardView extends VBox {

    public StatCardView() {
        setPadding(new Insets(24));
        setSpacing(24);

        Label title = new Label("JStatCard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
        
        Label description = new Label("El componente JStatCard muestra métricas o KPIs importantes de forma destacada, incluyendo una animación numérica que cuenta visualmente hasta el valor asignado.");
        description.setWrapText(true);
        description.setStyle("-fx-text-fill: #64748b; -fx-font-size: 14px;");

        // Stat Card Grid Layout
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        // Card 1: Revenue (Currency, animation 1.5s)
        JStatCard revCard = new JStatCard();
        revCard.setTitle("Total Revenue");
        revCard.setIcon(JIcon.MONEY.view(), "primary");
        revCard.setPrefix("$");
        revCard.setDecimalPlaces(2);
        revCard.setTrend("+12.5% vs last month", true);
        grid.add(revCard, 0, 0);

        // Card 2: Users (Integer, animation 2s)
        JStatCard userCard = new JStatCard();
        userCard.setTitle("Active Users");
        userCard.setIcon(JIcon.HOME.view(), "success"); // HOME is a placeholder, user can use an 'user' icon if available
        userCard.setValue(15423, 2000); 
        userCard.setTrend("+5.2% this week", true);
        grid.add(userCard, 1, 0);

        // Card 3: Bounce Rate (Percentage, suffix, red trend)
        JStatCard bounceCard = new JStatCard();
        bounceCard.setTitle("Bounce Rate");
        bounceCard.setIcon(JIcon.CLOSE.view(), "danger");
        bounceCard.setSuffix("%");
        bounceCard.setDecimalPlaces(1);
        bounceCard.setValue(42.3, 1000);
        bounceCard.setTrend("-2.1% than yesterday", false);
        grid.add(bounceCard, 2, 0);

        // Card 4: Orders (Simple, no trend)
        JStatCard orderCard = new JStatCard();
        orderCard.setTitle("Total Orders");
        orderCard.setIcon(JIcon.CHECK.view(), "warning");
        orderCard.setValue(3204);
        grid.add(orderCard, 3, 0);

        getChildren().addAll(title, description, grid);
        
        // Start animations after a tiny delay so the UI shows up first
        javafx.application.Platform.runLater(() -> {
            revCard.setValue(125430.50, 1500);
        });
    }
}
