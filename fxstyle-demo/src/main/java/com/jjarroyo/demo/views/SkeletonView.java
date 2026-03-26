package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JSkeleton;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SkeletonView extends ScrollPane {

    public SkeletonView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));
        setFitToWidth(true);
        setContent(content);

        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Skeletons")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Placeholders animados para estados de carga")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        content.getChildren().add(new JCard("Líneas de Texto", createTextLines()));
        content.getChildren().add(new JCard("Avatar + Texto", createUserRows()));
        content.getChildren().add(new JCard("Cards", createCards()));
        content.getChildren().add(new JCard("Rectángulos", createRects()));
        content.getChildren().add(new JCard("Tabla", createTable()));
    }

    private javafx.scene.Node createTextLines() {
        VBox container = new VBox(20);

        VBox section1 = new VBox(4);
        javafx.scene.control.Label l1 = new javafx.scene.control.Label("1 línea:");
        l1.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-text-fill: -color-slate-500;");
        section1.getChildren().addAll(l1, JSkeleton.text(1));

        VBox section2 = new VBox(4);
        javafx.scene.control.Label l2 = new javafx.scene.control.Label("3 líneas:");
        l2.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-text-fill: -color-slate-500;");
        section2.getChildren().addAll(l2, JSkeleton.text(3));

        VBox section3 = new VBox(4);
        javafx.scene.control.Label l3 = new javafx.scene.control.Label("5 líneas:");
        l3.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-text-fill: -color-slate-500;");
        section3.getChildren().addAll(l3, JSkeleton.text(5));

        container.getChildren().addAll(section1, section2, section3);
        return container;
    }

    private javafx.scene.Node createUserRows() {
        VBox container = new VBox(16);
        container.getChildren().addAll(
            JSkeleton.userRow(),
            JSkeleton.userRow(),
            JSkeleton.userRow()
        );
        return container;
    }

    private javafx.scene.Node createCards() {
        HBox container = new HBox(16);
        container.getChildren().addAll(
            JSkeleton.card(),
            JSkeleton.card()
        );
        return container;
    }

    private javafx.scene.Node createRects() {
        HBox container = new HBox(16);
        javafx.geometry.Pos alignment = javafx.geometry.Pos.BOTTOM_LEFT;
        container.setAlignment(alignment);
        container.getChildren().addAll(
            JSkeleton.rect(80, 80),
            JSkeleton.rect(120, 40),
            JSkeleton.rect(200, 24),
            JSkeleton.avatar(48),
            JSkeleton.avatar(32)
        );
        return container;
    }

    private javafx.scene.Node createTable() {
        return JSkeleton.table(5, 4);
    }
}
