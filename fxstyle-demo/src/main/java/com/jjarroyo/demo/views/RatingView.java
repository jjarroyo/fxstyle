package com.jjarroyo.demo.views;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JRating;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RatingView extends ScrollPane {

    public RatingView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));

        setFitToWidth(true);
        setContent(content);

        // Page Header
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Rating")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Componente interactivo de calificación con estrellas")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        // 1. Basic Rating
        content.getChildren().add(new JCard("Rating Básico", createBasicRating()));

        // 2. Rating with Initial Value
        content.getChildren().add(new JCard("Rating con Valor Inicial", createInitialValueRating()));

        // 3. Rating with Live Value Label
        content.getChildren().add(new JCard("Rating con Valor en Vivo", createLiveValueRating()));

        // 4. Sizes
        content.getChildren().add(new JCard("Tamaños", createSizesRating()));

        // 5. Color Variants
        content.getChildren().add(new JCard("Variantes de Color", createColorVariants()));

        // 6. Read Only
        content.getChildren().add(new JCard("Solo Lectura", createReadOnlyRating()));

        // 7. Custom Star Count
        content.getChildren().add(new JCard("Cantidad Personalizada", createCustomStarCount()));

        // 8. Disabled
        content.getChildren().add(new JCard("Estado Deshabilitado", createDisabledRating()));

        // 9. Feedback Labels
        content.getChildren().add(new JCard("Rating con Feedback", createFeedbackRating()));

        // 10. Interactive with Reset
        content.getChildren().add(new JCard("Rating Interactivo", createInteractiveRating()));
    }

    // ─── 1. Basic Rating ──────────────────────────────────────────────

    private javafx.scene.Node createBasicRating() {
        VBox container = new VBox(12);

        Label desc = new Label("Rating de 5 estrellas por defecto. Haz clic para calificar.");
        desc.getStyleClass().add("text-slate-500");

        JRating rating = new JRating();

        container.getChildren().addAll(desc, rating);
        return container;
    }

    // ─── 2. Initial Value Rating ──────────────────────────────────────

    private javafx.scene.Node createInitialValueRating() {
        VBox container = new VBox(12);

        Label desc = new Label("Rating pre-configurado con un valor inicial de 3 estrellas.");
        desc.getStyleClass().add("text-slate-500");

        JRating rating = new JRating(5, 3);

        container.getChildren().addAll(desc, rating);
        return container;
    }

    // ─── 3. Live Value Rating ─────────────────────────────────────────

    private javafx.scene.Node createLiveValueRating() {
        VBox container = new VBox(12);

        Label desc = new Label("El valor numérico se actualiza en tiempo real al hacer clic.");
        desc.getStyleClass().add("text-slate-500");

        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);

        JRating rating = new JRating(5, 0);

        Label valueLabel = new Label("0.0 / 5.0");
        valueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: -color-warning-500;");

        rating.ratingProperty().addListener((obs, old, val) -> {
            valueLabel.setText(String.format("%.1f / 5.0", val.doubleValue()));
        });

        row.getChildren().addAll(rating, valueLabel);
        container.getChildren().addAll(desc, row);
        return container;
    }

    // ─── 4. Sizes ─────────────────────────────────────────────────────

    private javafx.scene.Node createSizesRating() {
        VBox container = new VBox(16);

        Label desc = new Label("Tres tamaños disponibles: pequeño (sm), mediano (md) y grande (lg).");
        desc.getStyleClass().add("text-slate-500");

        HBox smRow = new HBox(12);
        smRow.setAlignment(Pos.CENTER_LEFT);
        Label smLabel = new Label("Small:");
        smLabel.getStyleClass().add("form-label");
        smLabel.setMinWidth(80);
        JRating smRating = new JRating(5, 3);
        smRating.withSize("rating-sm");
        smRow.getChildren().addAll(smLabel, smRating);

        HBox mdRow = new HBox(12);
        mdRow.setAlignment(Pos.CENTER_LEFT);
        Label mdLabel = new Label("Medium:");
        mdLabel.getStyleClass().add("form-label");
        mdLabel.setMinWidth(80);
        JRating mdRating = new JRating(5, 3);
        mdRow.getChildren().addAll(mdLabel, mdRating);

        HBox lgRow = new HBox(12);
        lgRow.setAlignment(Pos.CENTER_LEFT);
        Label lgLabel = new Label("Large:");
        lgLabel.getStyleClass().add("form-label");
        lgLabel.setMinWidth(80);
        JRating lgRating = new JRating(5, 3);
        lgRating.withSize("rating-lg");
        lgRow.getChildren().addAll(lgLabel, lgRating);

        container.getChildren().addAll(desc, smRow, mdRow, lgRow);
        return container;
    }

    // ─── 5. Color Variants ────────────────────────────────────────────

    private javafx.scene.Node createColorVariants() {
        VBox container = new VBox(16);

        Label desc = new Label("Múltiples variantes de color para diferentes contextos.");
        desc.getStyleClass().add("text-slate-500");

        FlowPane flow = new FlowPane(24, 16);

        // Default (Warning/Gold)
        VBox defaultBox = createColorItem("Default (Gold)", new JRating(5, 4));

        // Primary
        JRating primaryRating = new JRating(5, 4);
        primaryRating.withColor("rating-primary");
        VBox primaryBox = createColorItem("Primary", primaryRating);

        // Danger
        JRating dangerRating = new JRating(5, 4);
        dangerRating.withColor("rating-danger");
        VBox dangerBox = createColorItem("Danger", dangerRating);

        // Success
        JRating successRating = new JRating(5, 4);
        successRating.withColor("rating-success");
        VBox successBox = createColorItem("Success", successRating);

        // Info
        JRating infoRating = new JRating(5, 4);
        infoRating.withColor("rating-info");
        VBox infoBox = createColorItem("Info", infoRating);

        // Dark
        JRating darkRating = new JRating(5, 4);
        darkRating.withColor("rating-dark");
        VBox darkBox = createColorItem("Dark", darkRating);

        flow.getChildren().addAll(defaultBox, primaryBox, dangerBox, successBox, infoBox, darkBox);
        container.getChildren().addAll(desc, flow);
        return container;
    }

    private VBox createColorItem(String labelText, JRating rating) {
        VBox box = new VBox(4);
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: -color-slate-500;");
        box.getChildren().addAll(label, rating);
        return box;
    }

    // ─── 6. Read Only ─────────────────────────────────────────────────

    private javafx.scene.Node createReadOnlyRating() {
        VBox container = new VBox(16);

        Label desc = new Label("Ratings de solo lectura, ideales para mostrar calificaciones de productos o reseñas.");
        desc.getStyleClass().add("text-slate-500");

        // Product review simulation
        VBox review1 = createReviewItem("Laptop Pro X1", 4, 128);
        VBox review2 = createReviewItem("Auriculares Bluetooth", 5, 2340);
        VBox review3 = createReviewItem("Mouse Ergonómico", 3, 56);

        container.getChildren().addAll(desc, review1, review2, review3);
        return container;
    }

    private VBox createReviewItem(String productName, double rating, int reviewCount) {
        VBox item = new VBox(4);
        item.setPadding(new Insets(8, 0, 8, 0));

        Label name = new Label(productName);
        name.setStyle("-fx-font-weight: 600; -fx-font-size: 14px;");

        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        JRating ratingComp = new JRating(5, rating);
        ratingComp.setReadOnly(true);

        Label count = new Label(String.format("%.1f (%d reseñas)", rating, reviewCount));
        count.setStyle("-fx-font-size: 13px; -fx-text-fill: -color-slate-500;");

        row.getChildren().addAll(ratingComp, count);
        item.getChildren().addAll(name, row);
        return item;
    }

    // ─── 7. Custom Star Count ─────────────────────────────────────────

    private javafx.scene.Node createCustomStarCount() {
        VBox container = new VBox(16);

        Label desc = new Label("Configurable con diferente cantidad de estrellas.");
        desc.getStyleClass().add("text-slate-500");

        HBox row3 = new HBox(12);
        row3.setAlignment(Pos.CENTER_LEFT);
        Label label3 = new Label("3 estrellas:");
        label3.getStyleClass().add("form-label");
        label3.setMinWidth(100);
        JRating rating3 = new JRating(3, 2);
        row3.getChildren().addAll(label3, rating3);

        HBox row7 = new HBox(12);
        row7.setAlignment(Pos.CENTER_LEFT);
        Label label7 = new Label("7 estrellas:");
        label7.getStyleClass().add("form-label");
        label7.setMinWidth(100);
        JRating rating7 = new JRating(7, 5);
        row7.getChildren().addAll(label7, rating7);

        HBox row10 = new HBox(12);
        row10.setAlignment(Pos.CENTER_LEFT);
        Label label10 = new Label("10 estrellas:");
        label10.getStyleClass().add("form-label");
        label10.setMinWidth(100);
        JRating rating10 = new JRating(10, 7);
        rating10.withSize("rating-sm");
        row10.getChildren().addAll(label10, rating10);

        container.getChildren().addAll(desc, row3, row7, row10);
        return container;
    }

    // ─── 8. Disabled ──────────────────────────────────────────────────

    private javafx.scene.Node createDisabledRating() {
        VBox container = new VBox(12);

        Label desc = new Label("Rating en estado deshabilitado. No permite interacción.");
        desc.getStyleClass().add("text-slate-500");

        JRating rating = new JRating(5, 3);
        rating.setDisable(true);

        container.getChildren().addAll(desc, rating);
        return container;
    }

    // ─── 9. Feedback Labels ───────────────────────────────────────────

    private javafx.scene.Node createFeedbackRating() {
        VBox container = new VBox(12);

        Label desc = new Label("El texto de feedback cambia dinámicamente según la calificación.");
        desc.getStyleClass().add("text-slate-500");

        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);

        JRating rating = new JRating(5, 0);
        rating.withSize("rating-lg");

        Label feedbackLabel = new Label("Selecciona una calificación");
        feedbackLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: 600; -fx-text-fill: -color-slate-400;");

        String[] feedbacks = {"", "Terrible 😡", "Malo 😞", "Regular 😐", "Bueno 😊", "Excelente 🤩"};
        String[] colors = {"", "-color-danger-500", "-color-danger-500", "-color-warning-500", "-color-success-500", "-color-success-500"};

        rating.setOnRatingChanged(value -> {
            int index = (int) Math.ceil(value);
            if (index >= 0 && index < feedbacks.length) {
                feedbackLabel.setText(feedbacks[index]);
                feedbackLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: 600; -fx-text-fill: " + colors[index] + ";");
            }
            if (value == 0) {
                feedbackLabel.setText("Selecciona una calificación");
                feedbackLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: 600; -fx-text-fill: -color-slate-400;");
            }
        });

        row.getChildren().addAll(rating, feedbackLabel);
        container.getChildren().addAll(desc, row);
        return container;
    }

    // ─── 10. Interactive with Reset ───────────────────────────────────

    private javafx.scene.Node createInteractiveRating() {
        VBox container = new VBox(16);

        Label desc = new Label("Rating interactivo con contador de cambios y botón de reset. Haz clic en la misma estrella para deseleccionar.");
        desc.getStyleClass().add("text-slate-500");

        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);

        JRating rating = new JRating(5, 0);
        rating.withColor("rating-primary");
        rating.withSize("rating-lg");

        Label changesLabel = new Label("Cambios: 0");
        changesLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 600; -fx-text-fill: -color-primary-500;");

        Label currentLabel = new Label("Valor: 0.0");
        currentLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: -color-slate-600;");

        final int[] changeCount = {0};

        rating.setOnRatingChanged(value -> {
            changeCount[0]++;
            changesLabel.setText("Cambios: " + changeCount[0]);
            currentLabel.setText("Valor: " + String.format("%.1f", value));
        });

        JButton resetBtn = new JButton("Reset");
        resetBtn.addClass("btn-outline-danger");
        resetBtn.addClass("btn-sm");
        resetBtn.setOnAction(e -> {
            rating.setRating(0);
            changeCount[0] = 0;
            changesLabel.setText("Cambios: 0");
            currentLabel.setText("Valor: 0.0");
        });

        row.getChildren().addAll(rating, currentLabel, changesLabel, resetBtn);
        container.getChildren().addAll(desc, row);
        return container;
    }
}
