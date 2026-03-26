package com.jjarroyo.demo.views;

import com.jjarroyo.components.JAvatar;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JLabel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AvatarView extends ScrollPane {

    public AvatarView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));

        setFitToWidth(true);
        setContent(content);

        // Page Title
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Avatars")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Componente de avatar con iniciales, imágenes, estados y grupos")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        // Sizes
        content.getChildren().add(new JCard("Tamaños", createSizes()));

        // Colors
        content.getChildren().add(new JCard("Colores", createColors()));

        // Names
        content.getChildren().add(new JCard("Desde Nombres", createFromNames()));

        // Status
        content.getChildren().add(new JCard("Estado (Badge)", createStatus()));

        // Group
        content.getChildren().add(new JCard("Grupo de Avatares", createGroup()));
    }

    private javafx.scene.Node createSizes() {
        FlowPane container = new FlowPane(24, 16);
        container.setAlignment(Pos.CENTER_LEFT);

        for (JAvatar.Size s : JAvatar.Size.values()) {
            VBox box = new VBox(8);
            box.setAlignment(Pos.CENTER);
            JAvatar avatar = new JAvatar("JA").setSize(s);
            Label label = new Label(s.name());
            label.setStyle("-fx-font-size: 11px; -fx-text-fill: -color-slate-500;");
            box.getChildren().addAll(avatar, label);
            container.getChildren().add(box);
        }

        return container;
    }

    private javafx.scene.Node createColors() {
        FlowPane container = new FlowPane(24, 16);
        container.setAlignment(Pos.CENTER_LEFT);

        String[] colors = {"primary", "success", "danger", "warning", "info", "slate"};
        String[] initials = {"AB", "CD", "EF", "GH", "IJ", "KL"};

        for (int i = 0; i < colors.length; i++) {
            VBox box = new VBox(8);
            box.setAlignment(Pos.CENTER);
            JAvatar avatar = new JAvatar(initials[i])
                .setSize(JAvatar.Size.LG)
                .setColor(colors[i]);
            Label label = new Label(colors[i]);
            label.setStyle("-fx-font-size: 11px; -fx-text-fill: -color-slate-500;");
            box.getChildren().addAll(avatar, label);
            container.getChildren().add(box);
        }

        return container;
    }

    private javafx.scene.Node createFromNames() {
        FlowPane container = new FlowPane(24, 16);
        container.setAlignment(Pos.CENTER_LEFT);

        String[][] names = {
            {"Jorge", "Arroyo"}, {"María", "López"}, {"Carlos", "García"},
            {"Ana", "Rodríguez"}, {"Pedro", "Sánchez"}
        };
        String[] colors = {"primary", "success", "danger", "warning", "info"};

        for (int i = 0; i < names.length; i++) {
            VBox box = new VBox(8);
            box.setAlignment(Pos.CENTER);
            JAvatar avatar = new JAvatar(names[i][0], names[i][1])
                .setSize(JAvatar.Size.LG)
                .setColor(colors[i]);
            Label label = new Label(names[i][0] + " " + names[i][1]);
            label.setStyle("-fx-font-size: 11px; -fx-text-fill: -color-slate-500;");
            box.getChildren().addAll(avatar, label);
            container.getChildren().add(box);
        }

        return container;
    }

    private javafx.scene.Node createStatus() {
        FlowPane container = new FlowPane(32, 16);
        container.setAlignment(Pos.CENTER_LEFT);

        JAvatar.Status[] statuses = {
            JAvatar.Status.ONLINE, JAvatar.Status.AWAY,
            JAvatar.Status.BUSY, JAvatar.Status.OFFLINE
        };
        String[] labels = {"Online", "Away", "Busy", "Offline"};

        for (int i = 0; i < statuses.length; i++) {
            VBox box = new VBox(8);
            box.setAlignment(Pos.CENTER);
            JAvatar avatar = new JAvatar("U" + (i + 1))
                .setSize(JAvatar.Size.LG)
                .setStatus(statuses[i]);
            Label label = new Label(labels[i]);
            label.setStyle("-fx-font-size: 11px; -fx-text-fill: -color-slate-500;");
            box.getChildren().addAll(avatar, label);
            container.getChildren().add(box);
        }

        return container;
    }

    private javafx.scene.Node createGroup() {
        VBox container = new VBox(20);

        // Basic group
        Label lbl1 = new Label("Grupo básico");
        lbl1.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: -color-slate-700;");

        JAvatar.Group group1 = new JAvatar.Group(
            new JAvatar("JA").setColor("primary"),
            new JAvatar("ML").setColor("success"),
            new JAvatar("CG").setColor("danger"),
            new JAvatar("AR").setColor("warning")
        );

        // Group with max
        Label lbl2 = new Label("Grupo con máximo (3 de 6)");
        lbl2.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: -color-slate-700;");

        JAvatar.Group group2 = new JAvatar.Group(
            new JAvatar("A").setColor("primary"),
            new JAvatar("B").setColor("success"),
            new JAvatar("C").setColor("danger"),
            new JAvatar("D").setColor("warning"),
            new JAvatar("E").setColor("info"),
            new JAvatar("F").setColor("slate")
        );
        group2.setMax(3);

        // Larger group
        Label lbl3 = new Label("Grupo grande (LG)");
        lbl3.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: -color-slate-700;");

        JAvatar.Group group3 = new JAvatar.Group(
            new JAvatar("Jorge", "Arroyo").setSize(JAvatar.Size.LG).setColor("primary"),
            new JAvatar("María", "López").setSize(JAvatar.Size.LG).setColor("success"),
            new JAvatar("Carlos", "García").setSize(JAvatar.Size.LG).setColor("danger")
        );

        container.getChildren().addAll(lbl1, group1, lbl2, group2, lbl3, group3);
        return container;
    }
}
