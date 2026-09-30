package com.jjarroyo.components;

import javafx.animation.*;
import javafx.beans.property.*;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

/**
 * JBattery - Widget de telemetría de batería para IoT, robótica y sistemas autónomos.
 * 
 * Características:
 * - Nivel reactivo de carga (0% a 100%) con animación de interpolación suave.
 * - Modo continuo o segmentado (bloques discretos de celdas).
 * - Detección automática de estado crítico (Rojo < 20%), preventivo (Ámbar 20-50%) y óptimo (Verde > 50%).
 * - Estado de carga activa (CHARGING) con animación de rayo pulsante.
 * - Orientación HORIZONTAL y VERTICAL.
 * - Lectura opcional de voltaje (ej: 48.2 V) y estado de salud (SOC).
 */
public class JBattery extends HBox {

    private final DoubleProperty level = new SimpleDoubleProperty(75.0);
    private final DoubleProperty animatedLevel = new SimpleDoubleProperty(75.0);
    private final DoubleProperty voltage = new SimpleDoubleProperty(0.0);
    private final BooleanProperty charging = new SimpleBooleanProperty(false);
    private final ObjectProperty<Orientation> orientation = new SimpleObjectProperty<>(Orientation.HORIZONTAL);
    private final BooleanProperty segmented = new SimpleBooleanProperty(false);
    private final IntegerProperty segmentCount = new SimpleIntegerProperty(5);
    private final BooleanProperty showPercentage = new SimpleBooleanProperty(true);
    private final BooleanProperty showVoltage = new SimpleBooleanProperty(false);
    private final BooleanProperty showStatusText = new SimpleBooleanProperty(true);
    private final ObjectProperty<Color> customColor = new SimpleObjectProperty<>(null);

    private final DoubleProperty batteryWidth = new SimpleDoubleProperty(72.0);
    private final DoubleProperty batteryHeight = new SimpleDoubleProperty(32.0);

    // Nodos gráficos internos
    private final Pane batteryGraphicPane = new Pane();
    private final Group rootGroup = new Group();
    private final VBox infoBox = new VBox(2);
    private Label percentLabel;
    private Label subLabel;
    private SVGPath boltIcon;
    private Timeline pulseTimeline;
    private Timeline fillAnimation;

    public JBattery() {
        this(75.0);
    }

    public JBattery(double initialLevel) {
        this(initialLevel, false);
    }

    public JBattery(double initialLevel, boolean isCharging) {
        this.level.set(clamp(initialLevel, 0.0, 100.0));
        this.animatedLevel.set(this.level.get());
        this.charging.set(isCharging);

        setAlignment(Pos.CENTER_LEFT);
        setSpacing(12.0);
        getStyleClass().add("j-battery");

        batteryGraphicPane.getChildren().add(rootGroup);
        getChildren().addAll(batteryGraphicPane, infoBox);

        setupListeners();
        setupChargingAnimation();
        rebuildVisuals();
    }

    private void setupListeners() {
        level.addListener((obs, oldVal, newVal) -> animateToLevel(newVal.doubleValue()));
        animatedLevel.addListener((obs, oldVal, newVal) -> updateFillLevel());
        charging.addListener((obs, oldVal, newVal) -> updateChargingState());
        orientation.addListener((obs, oldVal, newVal) -> rebuildVisuals());
        segmented.addListener((obs, oldVal, newVal) -> rebuildVisuals());
        segmentCount.addListener((obs, oldVal, newVal) -> rebuildVisuals());
        batteryWidth.addListener((obs, oldVal, newVal) -> rebuildVisuals());
        batteryHeight.addListener((obs, oldVal, newVal) -> rebuildVisuals());
        customColor.addListener((obs, oldVal, newVal) -> updateFillLevel());
        voltage.addListener((obs, oldVal, newVal) -> updateLabels());
        showPercentage.addListener((obs, oldVal, newVal) -> updateLabels());
        showVoltage.addListener((obs, oldVal, newVal) -> updateLabels());
        showStatusText.addListener((obs, oldVal, newVal) -> updateLabels());
    }

    private void setupChargingAnimation() {
        pulseTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(boltIcon != null ? boltIcon.opacityProperty() : new SimpleDoubleProperty(), 0.35)),
            new KeyFrame(Duration.millis(600), new KeyValue(boltIcon != null ? boltIcon.opacityProperty() : new SimpleDoubleProperty(), 1.0)),
            new KeyFrame(Duration.millis(1200), new KeyValue(boltIcon != null ? boltIcon.opacityProperty() : new SimpleDoubleProperty(), 0.35))
        );
        pulseTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void animateToLevel(double target) {
        double clamped = clamp(target, 0.0, 100.0);
        if (fillAnimation != null) {
            fillAnimation.stop();
        }
        fillAnimation = new Timeline(
            new KeyFrame(Duration.millis(400), new KeyValue(animatedLevel, clamped, Interpolator.EASE_BOTH))
        );
        fillAnimation.play();
    }

    public void rebuildVisuals() {
        rootGroup.getChildren().clear();

        boolean isHoriz = orientation.get() == Orientation.HORIZONTAL;
        double w = batteryWidth.get();
        double h = batteryHeight.get();

        if (!isHoriz) {
            // Invertir proporciones para vertical
            double temp = w;
            w = h;
            h = temp;
        }

        double strokeW = 2.0;
        double innerPad = 3.0;

        // 1. Carcasa principal de la batería
        Rectangle body = new Rectangle(0, 0, w, h);
        body.setArcWidth(8);
        body.setArcHeight(8);
        body.setFill(Color.web("#0F172A", 0.06));
        body.setStroke(Color.web("#475569"));
        body.setStrokeWidth(strokeW);
        rootGroup.getChildren().add(body);

        // 2. Terminal positivo (+)
        if (isHoriz) {
            double nubW = 4.5;
            double nubH = h * 0.40;
            double nubY = (h - nubH) / 2.0;
            Rectangle terminal = new Rectangle(w + 1.0, nubY, nubW, nubH);
            terminal.setArcWidth(3);
            terminal.setArcHeight(3);
            terminal.setFill(Color.web("#475569"));
            rootGroup.getChildren().add(terminal);

            batteryGraphicPane.setMinSize(w + nubW + 6.0, h);
            batteryGraphicPane.setPrefSize(w + nubW + 6.0, h);
            batteryGraphicPane.setMaxSize(w + nubW + 6.0, h);
        } else {
            double nubW = w * 0.40;
            double nubH = 4.5;
            double nubX = (w - nubW) / 2.0;
            Rectangle terminal = new Rectangle(nubX, -nubH - 1.0, nubW, nubH);
            terminal.setArcWidth(3);
            terminal.setArcHeight(3);
            terminal.setFill(Color.web("#475569"));
            rootGroup.getChildren().add(terminal);

            batteryGraphicPane.setMinSize(w, h + nubH + 6.0);
            batteryGraphicPane.setPrefSize(w, h + nubH + 6.0);
            batteryGraphicPane.setMaxSize(w, h + nubH + 6.0);
        }

        // 3. Ícono de rayo de carga centrado
        boltIcon = new SVGPath();
        boltIcon.setContent("M11 1L3 13h6l-2 10 10-12h-6l4-10z");
        boltIcon.setFill(Color.web("#F59E0B"));
        boltIcon.setStroke(Color.web("#B45309"));
        boltIcon.setStrokeWidth(0.6);
        boltIcon.setScaleX(isHoriz ? 0.75 : 0.65);
        boltIcon.setScaleY(isHoriz ? 0.75 : 0.65);
        boltIcon.setTranslateX(isHoriz ? (w / 2.0) - 8 : (w / 2.0) - 8);
        boltIcon.setTranslateY(isHoriz ? (h / 2.0) - 12 : (h / 2.0) - 12);
        boltIcon.setVisible(charging.get());
        boltIcon.setMouseTransparent(true);

        updateFillLevel();
        rootGroup.getChildren().add(boltIcon);

        updateChargingState();
        updateLabels();
    }

    private void updateFillLevel() {
        // Remover rellenos previos dejando el cuerpo, terminal y rayo
        if (rootGroup.getChildren().size() > 2) {
            rootGroup.getChildren().removeIf(node -> node.getStyleClass().contains("j-battery-fill"));
        }

        boolean isHoriz = orientation.get() == Orientation.HORIZONTAL;
        double w = batteryWidth.get();
        double h = batteryHeight.get();
        if (!isHoriz) {
            double temp = w;
            w = h;
            h = temp;
        }

        double lvl = clamp(animatedLevel.get(), 0.0, 100.0);
        Color stateColor = determineColor(lvl);

        double pad = 3.5;
        double innerW = w - (pad * 2.0);
        double innerH = h - (pad * 2.0);

        if (segmented.get()) {
            int segments = Math.max(2, segmentCount.get());
            double gap = 2.0;
            double segW = (innerW - (gap * (segments - 1))) / segments;
            int activeSegments = (int) Math.round((lvl / 100.0) * segments);

            for (int i = 0; i < segments; i++) {
                Rectangle seg = new Rectangle(pad + (i * (segW + gap)), pad, segW, innerH);
                seg.setArcWidth(2.5);
                seg.setArcHeight(2.5);
                seg.getStyleClass().add("j-battery-fill");

                if (i < activeSegments) {
                    seg.setFill(new LinearGradient(
                        0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0.0, Color.color(stateColor.getRed() * 1.15, stateColor.getGreen() * 1.15, stateColor.getBlue() * 1.15)),
                        new Stop(1.0, stateColor)
                    ));
                } else {
                    seg.setFill(Color.web("#E2E8F0", 0.4));
                }
                // Insertar antes del icono de rayo
                rootGroup.getChildren().add(rootGroup.getChildren().size() - 1, seg);
            }
        } else {
            // Llenado continuo
            if (lvl > 0.0) {
                if (isHoriz) {
                    double fillW = (lvl / 100.0) * innerW;
                    Rectangle fillRect = new Rectangle(pad, pad, fillW, innerH);
                    fillRect.setArcWidth(4);
                    fillRect.setArcHeight(4);
                    fillRect.getStyleClass().add("j-battery-fill");
                    fillRect.setFill(new LinearGradient(
                        0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0.0, Color.color(Math.min(1.0, stateColor.getRed() * 1.15), Math.min(1.0, stateColor.getGreen() * 1.15), Math.min(1.0, stateColor.getBlue() * 1.15))),
                        new Stop(1.0, stateColor)
                    ));
                    rootGroup.getChildren().add(rootGroup.getChildren().size() - 1, fillRect);
                } else {
                    double fillH = (lvl / 100.0) * innerH;
                    double fillY = pad + (innerH - fillH);
                    Rectangle fillRect = new Rectangle(pad, fillY, innerW, fillH);
                    fillRect.setArcWidth(4);
                    fillRect.setArcHeight(4);
                    fillRect.getStyleClass().add("j-battery-fill");
                    fillRect.setFill(stateColor);
                    rootGroup.getChildren().add(rootGroup.getChildren().size() - 1, fillRect);
                }
            }
        }

        updateLabels();
    }

    private Color determineColor(double lvl) {
        if (customColor.get() != null) {
            return customColor.get();
        }
        if (lvl <= 20.0) {
            return Color.web("#EF4444"); // Rojo crítico
        } else if (lvl <= 50.0) {
            return Color.web("#F59E0B"); // Ámbar preventivo
        } else {
            return Color.web("#10B981"); // Verde óptimo
        }
    }

    private void updateChargingState() {
        boolean isChg = charging.get();
        if (boltIcon != null) {
            boltIcon.setVisible(isChg);
        }

        if (pulseTimeline != null) {
            pulseTimeline.stop();
        }

        if (isChg && boltIcon != null) {
            pulseTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(boltIcon.opacityProperty(), 0.35)),
                new KeyFrame(Duration.millis(500), new KeyValue(boltIcon.opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(1000), new KeyValue(boltIcon.opacityProperty(), 0.35))
            );
            pulseTimeline.setCycleCount(Timeline.INDEFINITE);
            pulseTimeline.play();
        }
    }

    private void updateLabels() {
        infoBox.getChildren().clear();

        double lvl = level.get();
        if (showPercentage.get()) {
            if (percentLabel == null) {
                percentLabel = new Label();
                percentLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #17303A;");
            }
            percentLabel.setText(String.format("%.0f%%", lvl));
            infoBox.getChildren().add(percentLabel);
        }

        StringBuilder sb = new StringBuilder();
        if (showVoltage.get() && voltage.get() > 0.0) {
            sb.append(String.format("%.1f V", voltage.get()));
        }

        if (showStatusText.get()) {
            if (sb.length() > 0) sb.append(" • ");
            if (charging.get()) {
                sb.append("Cargando");
            } else if (lvl <= 20.0) {
                sb.append("Batería baja");
            } else if (lvl >= 95.0) {
                sb.append("Completa");
            } else {
                sb.append("Descargando");
            }
        }

        if (sb.length() > 0) {
            if (subLabel == null) {
                subLabel = new Label();
                subLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #5F747B;");
            }
            subLabel.setText(sb.toString());
            infoBox.getChildren().add(subLabel);
        }
    }

    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    // --- Fluent API Helpers ---

    public JBattery withLevel(double level) {
        setLevel(level);
        return this;
    }

    public JBattery withCharging(boolean charging) {
        setCharging(charging);
        return this;
    }

    public JBattery withVoltage(double voltage) {
        setVoltage(voltage);
        setShowVoltage(true);
        return this;
    }

    public JBattery withSegmented(boolean segmented) {
        setSegmented(segmented);
        return this;
    }

    public JBattery withOrientation(Orientation orientation) {
        setOrientation(orientation);
        return this;
    }

    // --- Getters & Setters ---

    public double getLevel() {
        return level.get();
    }

    public void setLevel(double level) {
        this.level.set(level);
    }

    public DoubleProperty levelProperty() {
        return level;
    }

    public double getVoltage() {
        return voltage.get();
    }

    public void setVoltage(double voltage) {
        this.voltage.set(voltage);
    }

    public DoubleProperty voltageProperty() {
        return voltage;
    }

    public boolean isCharging() {
        return charging.get();
    }

    public void setCharging(boolean charging) {
        this.charging.set(charging);
    }

    public BooleanProperty chargingProperty() {
        return charging;
    }

    public Orientation getOrientation() {
        return orientation.get();
    }

    public void setOrientation(Orientation orientation) {
        this.orientation.set(orientation);
    }

    public ObjectProperty<Orientation> orientationProperty() {
        return orientation;
    }

    public boolean isSegmented() {
        return segmented.get();
    }

    public void setSegmented(boolean segmented) {
        this.segmented.set(segmented);
    }

    public BooleanProperty segmentedProperty() {
        return segmented;
    }

    public int getSegmentCount() {
        return segmentCount.get();
    }

    public void setSegmentCount(int count) {
        this.segmentCount.set(count);
    }

    public IntegerProperty segmentCountProperty() {
        return segmentCount;
    }

    public boolean isShowPercentage() {
        return showPercentage.get();
    }

    public void setShowPercentage(boolean show) {
        this.showPercentage.set(show);
    }

    public BooleanProperty showPercentageProperty() {
        return showPercentage;
    }

    public boolean isShowVoltage() {
        return showVoltage.get();
    }

    public void setShowVoltage(boolean show) {
        this.showVoltage.set(show);
    }

    public BooleanProperty showVoltageProperty() {
        return showVoltage;
    }

    public boolean isShowStatusText() {
        return showStatusText.get();
    }

    public void setShowStatusText(boolean show) {
        this.showStatusText.set(show);
    }

    public BooleanProperty showStatusTextProperty() {
        return showStatusText;
    }

    public Color getCustomColor() {
        return customColor.get();
    }

    public void setCustomColor(Color color) {
        this.customColor.set(color);
    }

    public ObjectProperty<Color> customColorProperty() {
        return customColor;
    }
}
