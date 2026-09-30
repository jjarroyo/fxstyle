package com.jjarroyo.demo.views;

import com.jjarroyo.components.*;
import com.jjarroyo.demo.util.DemoCodeDialog;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.Random;

/**
 * Vista de demostración de componentes IoT e Industriales de FxStyle:
 * - JLed: Indicadores LED con bisel metálico, glow y parpadeo.
 * - JTowerLight: Torreta industrial (Andon / Stack Light) con presets y zumbador.
 * - JBattery: Widget de telemetría de batería y nivel de carga.
 */
public class IotIndustrialView extends ScrollPane {

    public IotIndustrialView() {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox content = new VBox(24);
        content.setPadding(new Insets(24));
        setContent(content);

        // --- Encabezado ---
        VBox pageHeader = new VBox(4);
        Label title = new Label("IoT & Automatización Industrial");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #17303A;");
        Label subtitle = new Label("Componentes de señalización, alarmas y telemetría para SCADA, robótica y sistemas embebidos.");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #5F747B;");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        // --- Sección 1: JLed ---
        content.getChildren().add(createLedSection());

        // --- Sección 2: JTowerLight ---
        content.getChildren().add(createTowerLightSection());

        // --- Sección 3: JBattery ---
        content.getChildren().add(createBatterySection());
    }

    private JCard createLedSection() {
        VBox container = new VBox(20);

        // Fila 1: Colores industriales redondos
        Label row1Title = new Label("1. Paleta de Colores y Resplandor (Round Lens)");
        row1Title.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #17303A;");

        FlowPane colorsPane = new FlowPane(16, 12);
        colorsPane.getChildren().addAll(
            new JLed("RUN", LedColor.GREEN, true),
            new JLed("ALARM", LedColor.RED, true),
            new JLed("WARN", LedColor.AMBER, true),
            new JLed("STATUS", LedColor.BLUE, true),
            new JLed("SYNC", LedColor.CYAN, true),
            new JLed("AUX", LedColor.PURPLE, true),
            new JLed("ALERT", LedColor.ORANGE, true),
            new JLed("PWR", LedColor.WHITE, true),
            new JLed("STANDBY", LedColor.GREEN, false)
        );

        // Fila 2: Formas cuadradas y bisel plano
        Label row2Title = new Label("2. Lentes Cuadrados (Square Lens) para Racks Industriales");
        row2Title.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #17303A;");

        FlowPane squarePane = new FlowPane(16, 12);
        JLed sqGreen = new JLed("CH 01", LedColor.GREEN, true).withShape(LedShape.SQUARE).withSize(16);
        JLed sqRed = new JLed("CH 02", LedColor.RED, true).withShape(LedShape.SQUARE).withSize(16);
        JLed sqAmber = new JLed("CH 03", LedColor.AMBER, true).withShape(LedShape.SQUARE).withSize(16);
        JLed sqBlue = new JLed("CH 04", LedColor.BLUE, true).withShape(LedShape.SQUARE).withSize(16);
        JLed sqOff = new JLed("CH 05", LedColor.CYAN, false).withShape(LedShape.SQUARE).withSize(16);
        squarePane.getChildren().addAll(sqGreen, sqRed, sqAmber, sqBlue, sqOff);

        // Fila 3: Parpadeo en tiempo real (Heartbeat, TX/RX) e Interacción
        Label row3Title = new Label("3. Modos Dinámicos: Parpadeo (Blink) e Interactivos (Click para alternar)");
        row3Title.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #17303A;");

        HBox dynamicRow = new HBox(24);
        dynamicRow.setAlignment(Pos.CENTER_LEFT);

        JLed txLed = new JLed("TX (Comm)", LedColor.CYAN, true).blink(180);
        JLed rxLed = new JLed("RX (Packet)", LedColor.GREEN, true).blink(250);
        JLed alarmBlink = new JLed("FALLA CRÍTICA", LedColor.RED, true).blink(400);

        JLed clickLed = new JLed("Clickeable (Haz clic)", LedColor.PURPLE, false).withSize(22);
        clickLed.setInteractive(true);

        dynamicRow.getChildren().addAll(txLed, rxLed, alarmBlink, clickLed);

        container.getChildren().addAll(row1Title, colorsPane, row2Title, squarePane, row3Title, dynamicRow);

        JCard card = new JCard("Indicadores Luminosos (JLed)", container);
        card.addToolbarItem(DemoCodeDialog.createCodeButton("JLed",
            "// Crear LED con color y estado:\n" +
            "JLed runLed = new JLed(\"RUN\", LedColor.GREEN, true);\n\n" +
            "// Parpadeo automático (heartbeat / transmisión):\n" +
            "JLed txLed = new JLed(\"TX\", LedColor.CYAN, true).blink(200); // 200 ms\n\n" +
            "// Lente cuadrado para montaje en rack:\n" +
            "JLed rackLed = new JLed(\"CH 1\", LedColor.BLUE, true)\n" +
            "    .withShape(LedShape.SQUARE)\n" +
            "    .withSize(20.0);\n\n" +
            "// Modo interactivo (toggle con clic):\n" +
            "runLed.setInteractive(true);"));
        return card;
    }

    private JCard createTowerLightSection() {
        HBox layout = new HBox(36);
        layout.setAlignment(Pos.CENTER_LEFT);

        // Torreta interactiva
        JTowerLight tower = new JTowerLight();

        // Panel de control lateral
        VBox controls = new VBox(16);
        controls.setAlignment(Pos.CENTER_LEFT);

        Label desc = new Label("Selecciona un estado operativo industrial para ver la respuesta visual en vivo de la columna:");
        desc.setStyle("-fx-font-size: 13px; -fx-text-fill: #5F747B;");
        desc.setWrapText(true);

        // Presets
        HBox presetsRow1 = new HBox(8);
        Button btnNormal = createActionButton("Normal (Verde)", "#10B981", () -> tower.setPresetNormal());
        Button btnWarning = createActionButton("Advertencia (Ámbar)", "#F59E0B", () -> tower.setPresetWarning());
        Button btnFault = createActionButton("Falla / Parada (Rojo + Buzzer)", "#EF4444", () -> tower.setPresetFault());
        presetsRow1.getChildren().addAll(btnNormal, btnWarning, btnFault);

        HBox presetsRow2 = new HBox(8);
        Button btnMaint = createActionButton("Mantenimiento (Azul)", "#3B82F6", () -> tower.setPresetMaintenance());
        Button btnStandby = createActionButton("En Espera (Ámbar ON)", "#64748B", () -> tower.setPresetStandby());
        Button btnOff = createActionButton("Apagar Todo", "#334155", () -> tower.setAllOff());
        presetsRow2.getChildren().addAll(btnMaint, btnStandby, btnOff);

        // Controles directos de niveles
        Label manualLabel = new Label("Control individual de módulos:");
        manualLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #17303A; -fx-padding: 8px 0 0 0;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        addModuleController(grid, 0, "Rojo", tower.redProperty());
        addModuleController(grid, 1, "Ámbar", tower.amberProperty());
        addModuleController(grid, 2, "Verde", tower.greenProperty());
        addModuleController(grid, 3, "Azul", tower.blueProperty());

        Button btnToggleBuzzer = new Button("Alternar Zumbador / Sirena");
        btnToggleBuzzer.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #1E293B; -fx-font-weight: 600; -fx-border-color: #CBD5E1; -fx-border-radius: 6px; -fx-padding: 6px 12px; -fx-cursor: hand;");
        btnToggleBuzzer.setOnAction(e -> tower.setBuzzer(!tower.isBuzzer()));

        controls.getChildren().addAll(desc, presetsRow1, presetsRow2, manualLabel, grid, btnToggleBuzzer);

        layout.getChildren().addAll(tower, controls);

        JCard card = new JCard("Torreta Industrial / Andon (JTowerLight)", layout);
        card.addToolbarItem(DemoCodeDialog.createCodeButton("JTowerLight",
            "// Crear torreta industrial de 4 niveles con zumbador acústico:\n" +
            "JTowerLight tower = new JTowerLight();\n\n" +
            "// Aplicar estados estándar de industria:\n" +
            "tower.setPresetNormal();      // Verde continuo\n" +
            "tower.setPresetWarning();     // Ámbar parpadeante\n" +
            "tower.setPresetFault();       // Rojo parpadeante + sirena acústica\n" +
            "tower.setPresetMaintenance(); // Azul continuo\n\n" +
            "// Control manual de cada módulo (OFF, ON, BLINK):\n" +
            "tower.setRed(TowerModuleState.BLINK);\n" +
            "tower.setGreen(TowerModuleState.OFF);\n" +
            "tower.setBuzzer(true);"));
        return card;
    }

    private void addModuleController(GridPane grid, int row, String name, javafx.beans.property.ObjectProperty<TowerModuleState> prop) {
        Label lbl = new Label(name + ":");
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #334155; -fx-font-weight: 500;");

        HBox btnGroup = new HBox(4);
        Button bOff = createSmallButton("OFF", () -> prop.set(TowerModuleState.OFF));
        Button bOn = createSmallButton("ON", () -> prop.set(TowerModuleState.ON));
        Button bBlink = createSmallButton("BLINK", () -> prop.set(TowerModuleState.BLINK));
        btnGroup.getChildren().addAll(bOff, bOn, bBlink);

        grid.add(lbl, 0, row);
        grid.add(btnGroup, 1, row);
    }

    private Button createSmallButton(String text, Runnable action) {
        Button b = new Button(text);
        b.setStyle("-fx-font-size: 11px; -fx-padding: 3px 8px; -fx-background-color: white; -fx-border-color: #CBD5E1; -fx-border-radius: 4px; -fx-cursor: hand;");
        b.setOnAction(e -> action.run());
        return b;
    }

    private Button createActionButton(String text, String colorHex, Runnable action) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-font-weight: bold; " +
                     "-fx-font-size: 12px; -fx-padding: 8px 14px; -fx-background-radius: 6px; -fx-cursor: hand;");
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private JCard createBatterySection() {
        VBox container = new VBox(20);

        // Baterías reactivas
        JBattery mainBattery = new JBattery(82.0, false).withVoltage(48.4);
        JBattery segmentedBattery = new JBattery(60.0, false).withSegmented(true).withVoltage(24.0);
        JBattery lowBattery = new JBattery(15.0, false).withVoltage(11.2);
        JBattery verticalBattery = new JBattery(90.0, true).withOrientation(Orientation.VERTICAL).withVoltage(3.7);

        HBox batteriesRow = new HBox(32);
        batteriesRow.setAlignment(Pos.CENTER_LEFT);

        VBox hBox1 = new VBox(6, new Label("Continuo (Óptimo)"), mainBattery);
        VBox hBox2 = new VBox(6, new Label("Segmentado (5 Celdas)"), segmentedBattery);
        VBox hBox3 = new VBox(6, new Label("Alerta Crítica (< 20%)"), lowBattery);
        VBox hBox4 = new VBox(6, new Label("Vertical (Cargando)"), verticalBattery);

        for (VBox b : new VBox[]{hBox1, hBox2, hBox3, hBox4}) {
            ((Label) b.getChildren().get(0)).setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #475569;");
        }

        batteriesRow.getChildren().addAll(hBox1, hBox2, hBox3, hBox4);

        // Control interactivo
        VBox interactiveControls = new VBox(10);
        interactiveControls.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 16px; -fx-border-color: #E2E8F0; -fx-border-radius: 8px;");

        Label sliderTitle = new Label("Control Interactivo de Nivel de Batería Principal:");
        sliderTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #17303A;");

        HBox sliderRow = new HBox(16);
        sliderRow.setAlignment(Pos.CENTER_LEFT);

        Slider slider = new Slider(0, 100, 82);
        slider.setPrefWidth(260);
        slider.valueProperty().addListener((obs, ov, nv) -> {
            mainBattery.setLevel(nv.doubleValue());
            segmentedBattery.setLevel(nv.doubleValue());
            mainBattery.setVoltage(40.0 + (nv.doubleValue() * 0.12));
            segmentedBattery.setVoltage(20.0 + (nv.doubleValue() * 0.05));
        });

        Button btnToggleCharging = new Button("Alternar Estado de Carga (Solar / AC)");
        btnToggleCharging.setStyle("-fx-background-color: #0E6F7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 6px 12px; -fx-background-radius: 6px; -fx-cursor: hand;");
        btnToggleCharging.setOnAction(e -> {
            boolean newState = !mainBattery.isCharging();
            mainBattery.setCharging(newState);
            segmentedBattery.setCharging(newState);
            btnToggleCharging.setText(newState ? "Desconectar Cargador" : "Conectar Cargador (Solar / AC)");
        });

        sliderRow.getChildren().addAll(slider, btnToggleCharging);
        interactiveControls.getChildren().addAll(sliderTitle, sliderRow);

        container.getChildren().addAll(batteriesRow, interactiveControls);

        JCard card = new JCard("Telemetría de Baterías (JBattery)", container);
        card.addToolbarItem(DemoCodeDialog.createCodeButton("JBattery",
            "// Crear batería con porcentaje y voltaje:\n" +
            "JBattery battery = new JBattery(82.0)\n" +
            "    .withVoltage(48.4)\n" +
            "    .withCharging(true); // Activa icono de rayo pulsante\n\n" +
            "// Batería en modo celdas segmentadas:\n" +
            "JBattery segmented = new JBattery(60.0)\n" +
            "    .withSegmented(true)\n" +
            "    .withOrientation(Orientation.VERTICAL);\n\n" +
            "// Modificar nivel de forma reactiva (con animación suave):\n" +
            "battery.setLevel(25.0); // Cambia automáticamente a ámbar"));
        return card;
    }
}
