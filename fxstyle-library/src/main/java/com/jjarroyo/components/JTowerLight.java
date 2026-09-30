package com.jjarroyo.components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;

/**
 * JTowerLight - Torreta de señalización industrial (Andon / Stack Light) para JavaFX.
 * Utilizada en supervisión de líneas de producción, celdas robóticas, CNCs, PLC y racks de servidores.
 * 
 * Soporta:
 * - 3 o 4 etapas luminosas (Rojo, Ámbar, Verde, Azul).
 * - Módulo acústico de zumbador / sirena (Buzzer) con indicación de ondas.
 * - Modos OFF, ON y BLINK independientes para cada nivel.
 * - Textura óptica Fresnel cilíndrica realista con reflejo especular metálico.
 * - Base y poste industrial de aluminio anodizado.
 * - Presets de fábrica (Normal, Warning, Fault/Emergency, Standby, Maintenance).
 */
public class JTowerLight extends VBox {

    private final ObjectProperty<TowerModuleState> red = new SimpleObjectProperty<>(TowerModuleState.OFF);
    private final ObjectProperty<TowerModuleState> amber = new SimpleObjectProperty<>(TowerModuleState.OFF);
    private final ObjectProperty<TowerModuleState> green = new SimpleObjectProperty<>(TowerModuleState.ON);
    private final ObjectProperty<TowerModuleState> blue = new SimpleObjectProperty<>(TowerModuleState.OFF);
    private final BooleanProperty buzzer = new SimpleBooleanProperty(false);

    private final BooleanProperty hasBlueModule = new SimpleBooleanProperty(true);
    private final BooleanProperty hasBuzzerModule = new SimpleBooleanProperty(true);
    private final DoubleProperty moduleWidth = new SimpleDoubleProperty(48.0);
    private final DoubleProperty moduleHeight = new SimpleDoubleProperty(36.0);
    private final DoubleProperty poleHeight = new SimpleDoubleProperty(70.0);
    private final IntegerProperty blinkRateMs = new SimpleIntegerProperty(450);

    // Estado interno del parpadeo
    private final BooleanProperty blinkPhase = new SimpleBooleanProperty(true);
    private Timeline blinkTimeline;

    // Paneles de renderizado
    private final Pane graphicPane = new Pane();
    private final Group rootGroup = new Group();

    // Nodos gráficos de los módulos
    private Rectangle redRect;
    private Rectangle amberRect;
    private Rectangle greenRect;
    private Rectangle blueRect;
    private Shape buzzerDome;
    private Group buzzerWavesGroup;

    public JTowerLight() {
        setAlignment(Pos.CENTER);
        getStyleClass().add("j-tower-light");

        graphicPane.getChildren().add(rootGroup);
        getChildren().add(graphicPane);

        setupBlinkTimeline();
        setupListeners();
        rebuildVisuals();
    }

    private void setupBlinkTimeline() {
        blinkTimeline = new Timeline(
            new KeyFrame(Duration.millis(blinkRateMs.get()), e -> blinkPhase.set(!blinkPhase.get()))
        );
        blinkTimeline.setCycleCount(Timeline.INDEFINITE);
        blinkTimeline.play();
    }

    private void setupListeners() {
        red.addListener((o, ov, nv) -> updateModuleLight(redRect, nv, LedColor.RED));
        amber.addListener((o, ov, nv) -> updateModuleLight(amberRect, nv, LedColor.AMBER));
        green.addListener((o, ov, nv) -> updateModuleLight(greenRect, nv, LedColor.GREEN));
        blue.addListener((o, ov, nv) -> updateModuleLight(blueRect, nv, LedColor.BLUE));
        buzzer.addListener((o, ov, nv) -> updateBuzzerVisuals());

        blinkPhase.addListener((o, ov, nv) -> {
            if (red.get() == TowerModuleState.BLINK) updateModuleLight(redRect, red.get(), LedColor.RED);
            if (amber.get() == TowerModuleState.BLINK) updateModuleLight(amberRect, amber.get(), LedColor.AMBER);
            if (green.get() == TowerModuleState.BLINK) updateModuleLight(greenRect, green.get(), LedColor.GREEN);
            if (blue.get() == TowerModuleState.BLINK) updateModuleLight(blueRect, blue.get(), LedColor.BLUE);
            if (buzzer.get() && buzzerWavesGroup != null) {
                buzzerWavesGroup.setVisible(nv);
            }
        });

        blinkRateMs.addListener((o, ov, nv) -> {
            if (blinkTimeline != null) {
                blinkTimeline.stop();
                setupBlinkTimeline();
            }
        });

        hasBlueModule.addListener((o, ov, nv) -> rebuildVisuals());
        hasBuzzerModule.addListener((o, ov, nv) -> rebuildVisuals());
        moduleWidth.addListener((o, ov, nv) -> rebuildVisuals());
        moduleHeight.addListener((o, ov, nv) -> rebuildVisuals());
        poleHeight.addListener((o, ov, nv) -> rebuildVisuals());
    }

    public void rebuildVisuals() {
        rootGroup.getChildren().clear();

        double w = moduleWidth.get();
        double h = moduleHeight.get();
        double pHeight = poleHeight.get();
        double poleD = Math.max(10.0, w * 0.25);

        double currentY = 16.0; // Espacio superior para ondas acústicas si las hay
        double centerX = (w + 40.0) / 2.0;
        double startX = centerX - (w / 2.0);

        // 1. Zumbador superior (Buzzer Dome)
        if (hasBuzzerModule.get()) {
            double domeH = h * 0.55;
            Arc dome = new Arc(centerX, currentY + domeH, w / 2.0, domeH, 0, 180);
            dome.setType(ArcType.CHORD);
            LinearGradient capGrad = new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#334155")),
                new Stop(0.3, Color.web("#64748B")),
                new Stop(0.7, Color.web("#94A3B8")),
                new Stop(1.0, Color.web("#1E293B"))
            );
            dome.setFill(capGrad);
            dome.setStroke(Color.web("#0F172A"));
            dome.setStrokeWidth(1.0);
            buzzerDome = dome;
            rootGroup.getChildren().add(dome);

            // Rejillas acústicas del buzzer
            for (int i = 1; i <= 3; i++) {
                Line slot = new Line(centerX - (w * 0.22), currentY + (domeH * 0.3 * i),
                                     centerX + (w * 0.22), currentY + (domeH * 0.3 * i));
                slot.setStroke(Color.web("#0F172A", 0.8));
                slot.setStrokeWidth(1.2);
                rootGroup.getChildren().add(slot);
            }

            // Ondas acústicas animadas
            buzzerWavesGroup = new Group();
            Arc wave1 = new Arc(centerX, currentY, (w / 2.0) + 6, 8, 30, 120);
            wave1.setType(ArcType.OPEN);
            wave1.setFill(Color.TRANSPARENT);
            wave1.setStroke(Color.web("#EF4444", 0.85));
            wave1.setStrokeWidth(2.0);

            Arc wave2 = new Arc(centerX, currentY - 5, (w / 2.0) + 12, 12, 30, 120);
            wave2.setType(ArcType.OPEN);
            wave2.setFill(Color.TRANSPARENT);
            wave2.setStroke(Color.web("#EF4444", 0.6));
            wave2.setStrokeWidth(2.0);

            buzzerWavesGroup.getChildren().addAll(wave1, wave2);
            buzzerWavesGroup.setVisible(buzzer.get());
            rootGroup.getChildren().add(buzzerWavesGroup);

            currentY += domeH;
        } else {
            // Tapa plana redondeada
            double capH = 10.0;
            Rectangle cap = new Rectangle(startX, currentY, w, capH);
            cap.setArcWidth(8);
            cap.setArcHeight(8);
            cap.setFill(Color.web("#334155"));
            rootGroup.getChildren().add(cap);
            currentY += capH;
        }

        // 2. Módulo ROJO (Top)
        redRect = createModuleNode(startX, currentY, w, h);
        rootGroup.getChildren().addAll(redRect, createFresnelRibs(startX, currentY, w, h));
        currentY += h;
        addSeparatorRing(startX - 1, currentY, w + 2);
        currentY += 2;

        // 3. Módulo ÁMBAR
        amberRect = createModuleNode(startX, currentY, w, h);
        rootGroup.getChildren().addAll(amberRect, createFresnelRibs(startX, currentY, w, h));
        currentY += h;
        addSeparatorRing(startX - 1, currentY, w + 2);
        currentY += 2;

        // 4. Módulo VERDE
        greenRect = createModuleNode(startX, currentY, w, h);
        rootGroup.getChildren().addAll(greenRect, createFresnelRibs(startX, currentY, w, h));
        currentY += h;

        // 5. Módulo AZUL (Opcional)
        if (hasBlueModule.get()) {
            addSeparatorRing(startX - 1, currentY, w + 2);
            currentY += 2;
            blueRect = createModuleNode(startX, currentY, w, h);
            rootGroup.getChildren().addAll(blueRect, createFresnelRibs(startX, currentY, w, h));
            currentY += h;
        }

        // 6. Cuello inferior / Adaptador
        double collarH = 12.0;
        Rectangle collar = new Rectangle(startX + (w * 0.1), currentY, w * 0.8, collarH);
        collar.setArcWidth(4);
        collar.setArcHeight(4);
        collar.setFill(new LinearGradient(
            0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#1E293B")),
            new Stop(0.5, Color.web("#475569")),
            new Stop(1.0, Color.web("#0F172A"))
        ));
        rootGroup.getChildren().add(collar);
        currentY += collarH;

        // 7. Poste metálico de aluminio
        Rectangle pole = new Rectangle(centerX - (poleD / 2.0), currentY, poleD, pHeight);
        LinearGradient poleGrad = new LinearGradient(
            0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#64748B")),
            new Stop(0.35, Color.web("#E2E8F0")),
            new Stop(0.7, Color.web("#94A3B8")),
            new Stop(1.0, Color.web("#475569"))
        );
        pole.setFill(poleGrad);
        pole.setStroke(Color.web("#334155"));
        pole.setStrokeWidth(0.5);
        rootGroup.getChildren().add(pole);
        currentY += pHeight;

        // 8. Brida de montaje / Base con orificios de tornillo
        double baseW = w * 1.3;
        double baseH = 14.0;
        Rectangle baseFlange = new Rectangle(centerX - (baseW / 2.0), currentY, baseW, baseH);
        baseFlange.setArcWidth(6);
        baseFlange.setArcHeight(6);
        baseFlange.setFill(new LinearGradient(
            0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#334155")),
            new Stop(0.5, Color.web("#64748B")),
            new Stop(1.0, Color.web("#1E293B"))
        ));
        baseFlange.setStroke(Color.web("#0F172A"));
        baseFlange.setStrokeWidth(1.0);
        rootGroup.getChildren().add(baseFlange);

        // Tornillos de montaje de la brida
        Circle screwLeft = new Circle(centerX - (baseW * 0.38), currentY + (baseH / 2.0), 2.5, Color.web("#0F172A"));
        Circle screwRight = new Circle(centerX + (baseW * 0.38), currentY + (baseH / 2.0), 2.5, Color.web("#0F172A"));
        rootGroup.getChildren().addAll(screwLeft, screwRight);
        currentY += baseH + 6.0;

        // Dimensiones del panel gráfico
        double totalGraphicW = baseW + 30.0;
        graphicPane.setMinSize(totalGraphicW, currentY);
        graphicPane.setPrefSize(totalGraphicW, currentY);
        graphicPane.setMaxSize(totalGraphicW, currentY);

        // Actualizar estados visuales de las luces
        updateModuleLight(redRect, red.get(), LedColor.RED);
        updateModuleLight(amberRect, amber.get(), LedColor.AMBER);
        updateModuleLight(greenRect, green.get(), LedColor.GREEN);
        if (hasBlueModule.get()) {
            updateModuleLight(blueRect, blue.get(), LedColor.BLUE);
        }
    }

    private Rectangle createModuleNode(double x, double y, double w, double h) {
        Rectangle rect = new Rectangle(x, y, w, h);
        rect.setArcWidth(4);
        rect.setArcHeight(4);
        rect.setStroke(Color.web("#0F172A", 0.4));
        rect.setStrokeWidth(0.8);
        return rect;
    }

    private Group createFresnelRibs(double x, double y, double w, double h) {
        Group ribs = new Group();
        int count = (int) (h / 6.0);
        for (int i = 1; i <= count; i++) {
            Line rib = new Line(x + 1, y + (i * 6.0), x + w - 1, y + (i * 6.0));
            rib.setStroke(Color.color(1, 1, 1, 0.22));
            rib.setStrokeWidth(1.0);
            rib.setMouseTransparent(true);
            ribs.getChildren().add(rib);
        }
        return ribs;
    }

    private void addSeparatorRing(double x, double y, double w) {
        Rectangle ring = new Rectangle(x, y, w, 2.5);
        ring.setFill(Color.web("#1E293B"));
        rootGroup.getChildren().add(ring);
    }

    private void updateModuleLight(Rectangle rect, TowerModuleState state, LedColor color) {
        if (rect == null) return;

        boolean lit;
        if (state == TowerModuleState.ON) {
            lit = true;
        } else if (state == TowerModuleState.BLINK) {
            lit = blinkPhase.get();
        } else {
            lit = false;
        }

        if (lit) {
            Color onCol = color.getColorOn();
            LinearGradient litGrad = new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.color(onCol.getRed() * 0.7, onCol.getGreen() * 0.7, onCol.getBlue() * 0.7)),
                new Stop(0.35, Color.color(1, 1, 1, 0.9)), // Franja especular vertical brillante
                new Stop(0.70, onCol),
                new Stop(1.0, Color.color(onCol.getRed() * 0.6, onCol.getGreen() * 0.6, onCol.getBlue() * 0.6))
            );
            rect.setFill(litGrad);

            DropShadow glow = new DropShadow();
            glow.setColor(Color.color(onCol.getRed(), onCol.getGreen(), onCol.getBlue(), 0.85));
            glow.setRadius(14);
            glow.setSpread(0.2);
            rect.setEffect(glow);
        } else {
            Color offCol = color.getColorOff();
            LinearGradient unlitGrad = new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.color(offCol.getRed() * 0.6, offCol.getGreen() * 0.6, offCol.getBlue() * 0.6)),
                new Stop(0.4, Color.color(offCol.getRed() * 1.3, offCol.getGreen() * 1.3, offCol.getBlue() * 1.3)),
                new Stop(1.0, Color.color(offCol.getRed() * 0.4, offCol.getGreen() * 0.4, offCol.getBlue() * 0.4))
            );
            rect.setFill(unlitGrad);
            rect.setEffect(null);
        }
    }

    private void updateBuzzerVisuals() {
        if (buzzerWavesGroup != null) {
            buzzerWavesGroup.setVisible(buzzer.get());
        }
    }

    // --- Presets Industriales Estándar ---

    /**
     * Operación Normal: Verde encendido continuo, demás apagadas.
     */
    public void setPresetNormal() {
        red.set(TowerModuleState.OFF);
        amber.set(TowerModuleState.OFF);
        green.set(TowerModuleState.ON);
        blue.set(TowerModuleState.OFF);
        buzzer.set(false);
    }

    /**
     * Advertencia / Pre-alarma: Ámbar parpadeando, buzzer apagado.
     */
    public void setPresetWarning() {
        red.set(TowerModuleState.OFF);
        amber.set(TowerModuleState.BLINK);
        green.set(TowerModuleState.OFF);
        blue.set(TowerModuleState.OFF);
        buzzer.set(false);
    }

    /**
     * Falla crítica / Parada de máquina: Rojo parpadeando + Zumbador activo.
     */
    public void setPresetFault() {
        red.set(TowerModuleState.BLINK);
        amber.set(TowerModuleState.OFF);
        green.set(TowerModuleState.OFF);
        blue.set(TowerModuleState.OFF);
        buzzer.set(true);
    }

    /**
     * En espera / Standby: Ámbar fijo.
     */
    public void setPresetStandby() {
        red.set(TowerModuleState.OFF);
        amber.set(TowerModuleState.ON);
        green.set(TowerModuleState.OFF);
        blue.set(TowerModuleState.OFF);
        buzzer.set(false);
    }

    /**
     * Mantenimiento / Intervención requerida: Azul encendido.
     */
    public void setPresetMaintenance() {
        red.set(TowerModuleState.OFF);
        amber.set(TowerModuleState.OFF);
        green.set(TowerModuleState.OFF);
        blue.set(TowerModuleState.ON);
        buzzer.set(false);
    }

    /**
     * Todas apagadas.
     */
    public void setAllOff() {
        red.set(TowerModuleState.OFF);
        amber.set(TowerModuleState.OFF);
        green.set(TowerModuleState.OFF);
        blue.set(TowerModuleState.OFF);
        buzzer.set(false);
    }

    // --- Getters & Setters ---

    public TowerModuleState getRed() {
        return red.get();
    }

    public void setRed(TowerModuleState state) {
        this.red.set(state);
    }

    public ObjectProperty<TowerModuleState> redProperty() {
        return red;
    }

    public TowerModuleState getAmber() {
        return amber.get();
    }

    public void setAmber(TowerModuleState state) {
        this.amber.set(state);
    }

    public ObjectProperty<TowerModuleState> amberProperty() {
        return amber;
    }

    public TowerModuleState getGreen() {
        return green.get();
    }

    public void setGreen(TowerModuleState state) {
        this.green.set(state);
    }

    public ObjectProperty<TowerModuleState> greenProperty() {
        return green;
    }

    public TowerModuleState getBlue() {
        return blue.get();
    }

    public void setBlue(TowerModuleState state) {
        this.blue.set(state);
    }

    public ObjectProperty<TowerModuleState> blueProperty() {
        return blue;
    }

    public boolean isBuzzer() {
        return buzzer.get();
    }

    public void setBuzzer(boolean active) {
        this.buzzer.set(active);
    }

    public BooleanProperty buzzerProperty() {
        return buzzer;
    }

    public boolean isHasBlueModule() {
        return hasBlueModule.get();
    }

    public void setHasBlueModule(boolean hasBlue) {
        this.hasBlueModule.set(hasBlue);
    }

    public BooleanProperty hasBlueModuleProperty() {
        return hasBlueModule;
    }

    public boolean isHasBuzzerModule() {
        return hasBuzzerModule.get();
    }

    public void setHasBuzzerModule(boolean hasBuzzer) {
        this.hasBuzzerModule.set(hasBuzzer);
    }

    public BooleanProperty hasBuzzerModuleProperty() {
        return hasBuzzerModule;
    }

    public double getPoleHeight() {
        return poleHeight.get();
    }

    public void setPoleHeight(double height) {
        this.poleHeight.set(height);
    }

    public DoubleProperty poleHeightProperty() {
        return poleHeight;
    }
}
