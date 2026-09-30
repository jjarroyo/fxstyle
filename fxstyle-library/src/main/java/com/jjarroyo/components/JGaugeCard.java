package com.jjarroyo.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Tarjeta contenedora lista para dashboard y dispositivos IoT.
 * Incluye cabecera con titulo, subtitulo, indicador LED reactivo de estado (Normal, Atencion, Critico),
 * el medidor JGauge centrado y un control deslizante opcional para pruebas interactivas.
 */
public class JGaugeCard extends VBox {

    private final Label titleLabel;
    private final Label subtitleLabel;
    private final Circle statusDot;
    private final Label statusLabel;
    private final JGauge gauge;
    private final HBox headerBox;
    private final HBox manualControlBox;
    private final Slider manualSlider;
    private boolean isUpdatingFromGauge = false;
    private boolean isUpdatingFromSlider = false;

    public JGaugeCard(String title, String subtitle, JGauge gauge) {
        this.gauge = gauge != null ? gauge : new JGauge();

        getStyleClass().add("j-gauge-card");
        setPadding(new Insets(18));
        setSpacing(14);
        setAlignment(Pos.TOP_CENTER);

        // --- Encabezado ---
        VBox titleBox = new VBox(2);
        titleLabel = new Label(title);
        titleLabel.getStyleClass().add("j-gauge-card-title");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: 600; -fx-text-fill: #17303A;");

        subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyleClass().add("j-gauge-card-subtitle");
        subtitleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #5F747B;");
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Badge de estado reactivo
        HBox statusBadge = new HBox(6);
        statusBadge.setAlignment(Pos.CENTER_RIGHT);

        statusDot = new Circle(4.5);
        statusDot.setFill(Color.web(this.gauge.getCurrentStatus().getDefaultHexColor()));

        statusLabel = new Label(this.gauge.getCurrentStatus().getLabel());
        statusLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #17303A;");

        statusBadge.getChildren().addAll(statusDot, statusLabel);

        headerBox = new HBox(10);
        headerBox.setAlignment(Pos.TOP_LEFT);
        headerBox.getChildren().addAll(titleBox, spacer, statusBadge);

        // --- Contenedor del medidor ---
        VBox gaugeContainer = new VBox(this.gauge);
        gaugeContainer.setAlignment(Pos.CENTER);
        VBox.setVgrow(gaugeContainer, Priority.ALWAYS);

        // --- Control manual inferior ---
        manualControlBox = new HBox(10);
        manualControlBox.setAlignment(Pos.CENTER_LEFT);

        Label manualLabel = new Label("Manual");
        manualLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #5F747B;");

        manualSlider = new Slider(this.gauge.getMinValue(), this.gauge.getMaxValue(), this.gauge.getValue());
        manualSlider.setPrefWidth(160);
        HBox.setHgrow(manualSlider, Priority.ALWAYS);
        manualSlider.getStyleClass().add("j-gauge-slider");

        manualControlBox.getChildren().addAll(manualLabel, manualSlider);

        getChildren().addAll(headerBox, gaugeContainer, manualControlBox);

        // --- Enlaces y reactividad ---
        // Sincronizar el badge de estado cuando el medidor cambie de zona
        this.gauge.statusProperty().addListener((obs, oldS, newS) -> {
            if (newS != null) {
                statusDot.setFill(Color.web(newS.getDefaultHexColor()));
                statusLabel.setText(newS.getLabel());
            }
        });

        // Sincronizar slider cuando cambia el valor animado del medidor
        this.gauge.animatedValueProperty().addListener((obs, oldV, newV) -> {
            if (!isUpdatingFromSlider && !manualSlider.isValueChanging()) {
                isUpdatingFromGauge = true;
                manualSlider.setValue(newV.doubleValue());
                isUpdatingFromGauge = false;
            }
        });

        // Al mover el slider manual, enviar valor al medidor
        manualSlider.valueProperty().addListener((obs, oldV, newV) -> {
            if (!isUpdatingFromGauge) {
                isUpdatingFromSlider = true;
                this.gauge.setValue(newV.doubleValue(), 200); // respuesta agil para slider manual
                isUpdatingFromSlider = false;
            }
        });
    }

    public JGauge getGauge() {
        return gauge;
    }

    public void setTitle(String text) {
        titleLabel.setText(text);
    }

    public void setSubtitle(String text) {
        subtitleLabel.setText(text);
    }

    public void setManualControlVisible(boolean visible) {
        manualControlBox.setVisible(visible);
        manualControlBox.setManaged(visible);
    }

    public void setManualControlDisabled(boolean disabled) {
        manualSlider.setDisable(disabled);
    }

    public Slider getManualSlider() {
        return manualSlider;
    }
}
