package com.jjarroyo.demo.views;

import com.jjarroyo.components.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Vista de demostracion de JGauge y JGaugeCard para IoT y Dashboards.
 * Incluye tacometro de motor, temperatura, humedad, voltaje, amperaje (carga/descarga),
 * viento (anemometro), presion (manometro) y nivel de tanque vertical.
 */
public class GaugesView extends ScrollPane {

    private final List<JGaugeCard> allCards = new ArrayList<>();
    private final Button simButton;
    private boolean isSimulating = true;
    private Timeline simulationTimeline;
    private final Random random = new Random();

    public GaugesView() {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox contentBox = new VBox(24);
        contentBox.setPadding(new Insets(24));

        // --- Encabezado ---
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        Label title = new Label("Medidores Reactivos para IoT (JGauge)");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #17303A;");

        Label subtitle = new Label("Instrumentación completa para dispositivos conectados: agujas físicas suaves, arcos reactivos y simulación continua.");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #5F747B;");
        titleBox.getChildren().addAll(title, subtitle);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        simButton = new Button("Simulando datos IoT");
        simButton.setStyle("-fx-background-color: white; -fx-border-color: #D3DCDD; -fx-border-radius: 8px; " +
                           "-fx-background-radius: 8px; -fx-padding: 8px 16px; -fx-font-size: 13px; " +
                           "-fx-font-weight: 600; -fx-text-fill: #17303A; -fx-cursor: hand;");
        simButton.setOnAction(e -> toggleSimulation());

        header.getChildren().addAll(titleBox, headerSpacer, simButton);

        // --- Grid 2 columnas x 4 filas con los 8 medidores IoT ---
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        // 1. Motor de banda (RPM - Tacometro clásico con aguja deportiva)
        JGaugeCard rpmCard = new JGaugeCard("Motor de banda", "Velocidad del eje · estilo clásico", JGauge.createRpmGauge());
        grid.add(rpmCard, 0, 0);
        allCards.add(rpmCard);

        // 2. Temperatura (Semicirculo con zonas continuas de color)
        JGaugeCard tempCard = new JGaugeCard("Temperatura", "Cámara fría · semicírculo térmico", JGauge.createTemperatureGauge());
        grid.add(tempCard, 1, 0);
        allCards.add(tempCard);

        // 3. Humedad (Arco de progreso reactivo con puntero exterior)
        JGaugeCard humCard = new JGaugeCard("Humedad", "Invernadero · arco de progreso dinámico", JGauge.createHumidityGauge());
        grid.add(humCard, 0, 1);
        allCards.add(humCard);

        // 4. Bateria (Regla analogica horizontal lineal)
        JGaugeCard voltCard = new JGaugeCard("Batería", "Nodo solar 12 V · regla lineal", JGauge.createVoltageGauge());
        grid.add(voltCard, 1, 1);
        allCards.add(voltCard);

        // 5. Amperaje (Corriente de Carga y Descarga con Cero Central)
        JGaugeCard ampCard = new JGaugeCard("Amperaje Inversor", "Flujo bidireccional · carga y descarga", JGauge.createAmperageGauge());
        grid.add(ampCard, 0, 2);
        allCards.add(ampCard);

        // 6. Viento (Anemometro Meteorológico)
        JGaugeCard windCard = new JGaugeCard("Velocidad de Viento", "Estación meteorológica · anemómetro", JGauge.createWindGauge());
        grid.add(windCard, 1, 2);
        allCards.add(windCard);

        // 7. Manometro de Presion (Compresor industrial)
        JGaugeCard pressCard = new JGaugeCard("Presión de Línea", "Compresor neumático · manómetro Bar", JGauge.createPressureGauge());
        grid.add(pressCard, 0, 3);
        allCards.add(pressCard);

        // 8. Nivel de Tanque Vertical (Deposito de fluido / Combustible)
        JGaugeCard levelCard = new JGaugeCard("Nivel de Tanque", "Depósito de combustible · nivel vertical", JGauge.createTankLevelGauge());
        grid.add(levelCard, 1, 3);
        allCards.add(levelCard);

        // Columnas proporcionales 50%
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1, col2);

        // --- Bloque informativo y código de ejemplo ---
        VBox codeBox = new VBox(8);
        codeBox.setStyle("-fx-background-color: white; -fx-border-color: #D3DCDD; -fx-border-radius: 10px; " +
                         "-fx-background-radius: 10px; -fx-padding: 16px;");

        Label codeTitle = new Label("// Conexión a eventos en tiempo real (MQTT / WebSocket / Sensores IoT):");
        codeTitle.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #17303A;");

        Label codeContent = new Label(
            "// Instanciacion rápida con estilos predefinidos:\n" +
            "JGauge gauge = JGauge.createRpmGauge(); // o createAmperageGauge(), createWindGauge(), createPressureGauge()...\n\n" +
            "// Cuando el sensor IoT envia datos, la aguja viaja suavemente del punto A al nuevo punto:\n" +
            "mqttClient.subscribe(\"iot/motor/rpm\", (topic, msg) -> {\n" +
            "    double nuevoValor = Double.parseDouble(new String(msg.getPayload()));\n" +
            "    Platform.runLater(() -> gauge.setValue(nuevoValor));\n" +
            "});"
        );
        codeContent.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 12px; -fx-text-fill: #5F747B;");
        codeBox.getChildren().addAll(codeTitle, codeContent);

        contentBox.getChildren().addAll(header, grid, codeBox);
        setContent(contentBox);

        // Iniciar simulacion automatica
        setupSimulation();
        updateControlStates();
    }

    private void setupSimulation() {
        simulationTimeline = new Timeline(new KeyFrame(Duration.millis(1400), e -> {
            if (!isSimulating) return;

            for (JGaugeCard card : allCards) {
                simulateGaugeStep(card.getGauge());
            }
        }));
        simulationTimeline.setCycleCount(Timeline.INDEFINITE);
        simulationTimeline.play();
    }

    private void simulateGaugeStep(JGauge gauge) {
        double min = gauge.getMinValue();
        double max = gauge.getMaxValue();
        double span = max - min;
        double current = gauge.getValue();

        // Fluctuaciones suaves con inercia hacia el centro
        double stepRatio = 0.15;
        double centerDrift = 0.035;

        double randomDelta = (random.nextDouble() - 0.5) * span * stepRatio;
        double centerPull = ((min + span / 2.0) - current) * centerDrift;
        double next = Math.max(min, Math.min(max, current + randomDelta + centerPull));

        // Animacion suave e interpolada (Punto A -> Punto B)
        gauge.setValue(next);
    }

    private void toggleSimulation() {
        isSimulating = !isSimulating;
        if (isSimulating) {
            simButton.setText("Simulando datos IoT");
            simButton.setStyle("-fx-background-color: white; -fx-border-color: #D3DCDD; -fx-border-radius: 8px; " +
                               "-fx-background-radius: 8px; -fx-padding: 8px 16px; -fx-font-size: 13px; " +
                               "-fx-font-weight: 600; -fx-text-fill: #17303A; -fx-cursor: hand;");
            simulationTimeline.play();
        } else {
            simButton.setText("Modo Manual (Desactivado)");
            simButton.setStyle("-fx-background-color: #0E6F7C; -fx-border-color: #0E6F7C; -fx-border-radius: 8px; " +
                               "-fx-background-radius: 8px; -fx-padding: 8px 16px; -fx-font-size: 13px; " +
                               "-fx-font-weight: 600; -fx-text-fill: white; -fx-cursor: hand;");
            simulationTimeline.pause();
        }
        updateControlStates();
    }

    private void updateControlStates() {
        boolean disabled = isSimulating;
        for (JGaugeCard card : allCards) {
            card.setManualControlDisabled(disabled);
        }
    }
}
