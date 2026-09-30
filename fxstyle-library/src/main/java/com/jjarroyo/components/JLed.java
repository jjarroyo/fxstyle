package com.jjarroyo.components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 * JLed - Indicador LED industrial y de telemetría para JavaFX.
 * Diseñado para dashboards SCADA, IoT, paneles de control y estado de hardware.
 * 
 * Características:
 * - Lente 3D realista con reflejo especular de cristal.
 * - Bisel metálico o plano industrial.
 * - Resplandor dinámico (DropShadow Glow).
 * - Modos ON, OFF y BLINK (frecuencia configurable).
 * - Formas ROUND y SQUARE.
 * - Soporte para texto descriptivo integrado y modo interactivo con clic.
 */
public class JLed extends HBox {

    private final BooleanProperty on = new SimpleBooleanProperty(false);
    private final BooleanProperty blinking = new SimpleBooleanProperty(false);
    private final IntegerProperty blinkRateMs = new SimpleIntegerProperty(500);
    private final ObjectProperty<LedColor> color = new SimpleObjectProperty<>(LedColor.GREEN);
    private final ObjectProperty<LedShape> ledShape = new SimpleObjectProperty<>(LedShape.ROUND);
    private final DoubleProperty ledSize = new SimpleDoubleProperty(18.0);
    private final BooleanProperty bezel = new SimpleBooleanProperty(true);
    private final BooleanProperty glow = new SimpleBooleanProperty(true);
    private final StringProperty text = new SimpleStringProperty("");
    private final BooleanProperty interactive = new SimpleBooleanProperty(false);

    private final StackPane ledContainer = new StackPane();
    private Shape bezelShape;
    private Shape lensShape;
    private Shape specularHighlight;
    private Label labelNode;
    private Timeline blinkTimeline;

    public JLed() {
        this("LED", LedColor.GREEN, false);
    }

    public JLed(LedColor color) {
        this("", color, false);
    }

    public JLed(LedColor color, boolean isOn) {
        this("", color, isOn);
    }

    public JLed(String text, LedColor color, boolean isOn) {
        this.color.set(color != null ? color : LedColor.GREEN);
        this.on.set(isOn);
        this.text.set(text != null ? text : "");

        setAlignment(Pos.CENTER_LEFT);
        setSpacing(8.0);
        getStyleClass().add("j-led");

        initView();
        setupListeners();
        updateVisuals();
    }

    private void initView() {
        ledContainer.setAlignment(Pos.CENTER);
        getChildren().add(ledContainer);

        if (text.get() != null && !text.get().isEmpty()) {
            labelNode = new Label(text.get());
            labelNode.setStyle("-fx-font-size: 13px; -fx-text-fill: #17303A; -fx-font-weight: 500;");
            getChildren().add(labelNode);
        }

        setOnMouseClicked(e -> {
            if (interactive.get()) {
                toggle();
            }
        });
    }

    private void setupListeners() {
        on.addListener((obs, oldVal, newVal) -> updateVisuals());
        color.addListener((obs, oldVal, newVal) -> updateVisuals());
        ledShape.addListener((obs, oldVal, newVal) -> rebuildShapes());
        ledSize.addListener((obs, oldVal, newVal) -> rebuildShapes());
        bezel.addListener((obs, oldVal, newVal) -> rebuildShapes());
        glow.addListener((obs, oldVal, newVal) -> updateVisuals());
        interactive.addListener((obs, oldVal, newVal) -> {
            setStyle(newVal ? "-fx-cursor: hand;" : "-fx-cursor: default;");
        });

        text.addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                if (labelNode != null) {
                    getChildren().remove(labelNode);
                    labelNode = null;
                }
            } else {
                if (labelNode == null) {
                    labelNode = new Label(newVal);
                    labelNode.setStyle("-fx-font-size: 13px; -fx-text-fill: #17303A; -fx-font-weight: 500;");
                    getChildren().add(labelNode);
                } else {
                    labelNode.setText(newVal);
                }
            }
        });

        blinking.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                startBlinking();
            } else {
                stopBlinking();
            }
        });

        blinkRateMs.addListener((obs, oldVal, newVal) -> {
            if (blinking.get()) {
                startBlinking();
            }
        });
    }

    private void rebuildShapes() {
        ledContainer.getChildren().clear();
        double size = ledSize.get();
        double bezelThickness = bezel.get() ? Math.max(2.0, size * 0.16) : 0.0;
        double totalSize = size + (bezelThickness * 2.0);

        ledContainer.setMinSize(totalSize, totalSize);
        ledContainer.setPrefSize(totalSize, totalSize);
        ledContainer.setMaxSize(totalSize, totalSize);

        if (ledShape.get() == LedShape.ROUND) {
            // Bezel metálico exterior
            if (bezel.get()) {
                Circle bCircle = new Circle(totalSize / 2.0);
                LinearGradient bezelGrad = new LinearGradient(
                    0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.web("#94A3B8")),
                    new Stop(0.5, Color.web("#475569")),
                    new Stop(1.0, Color.web("#1E293B"))
                );
                bCircle.setFill(bezelGrad);
                bCircle.setStroke(Color.web("#0F172A", 0.6));
                bCircle.setStrokeWidth(0.8);
                bezelShape = bCircle;
                ledContainer.getChildren().add(bezelShape);
            }

            // Lente de cristal
            Circle lCircle = new Circle(size / 2.0);
            lensShape = lCircle;
            ledContainer.getChildren().add(lensShape);

            // Reflejo especular superior para profundidad óptica 3D
            Ellipse highlight = new Ellipse(size * 0.28, size * 0.14);
            highlight.setTranslateY(-size * 0.22);
            LinearGradient specGrad = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.color(1, 1, 1, 0.70)),
                new Stop(1.0, Color.color(1, 1, 1, 0.0))
            );
            highlight.setFill(specGrad);
            highlight.setMouseTransparent(true);
            specularHighlight = highlight;
            ledContainer.getChildren().add(specularHighlight);

        } else {
            // SQUARE SHAPE
            if (bezel.get()) {
                Rectangle bRect = new Rectangle(totalSize, totalSize);
                bRect.setArcWidth(4.0);
                bRect.setArcHeight(4.0);
                LinearGradient bezelGrad = new LinearGradient(
                    0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.web("#94A3B8")),
                    new Stop(0.5, Color.web("#475569")),
                    new Stop(1.0, Color.web("#1E293B"))
                );
                bRect.setFill(bezelGrad);
                bRect.setStroke(Color.web("#0F172A", 0.6));
                bRect.setStrokeWidth(0.8);
                bezelShape = bRect;
                ledContainer.getChildren().add(bezelShape);
            }

            Rectangle lRect = new Rectangle(size, size);
            lRect.setArcWidth(3.0);
            lRect.setArcHeight(3.0);
            lensShape = lRect;
            ledContainer.getChildren().add(lensShape);

            Rectangle highlight = new Rectangle(size * 0.7, size * 0.28);
            highlight.setArcWidth(2.0);
            highlight.setArcHeight(2.0);
            highlight.setTranslateY(-size * 0.22);
            LinearGradient specGrad = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.color(1, 1, 1, 0.65)),
                new Stop(1.0, Color.color(1, 1, 1, 0.0))
            );
            highlight.setFill(specGrad);
            highlight.setMouseTransparent(true);
            specularHighlight = highlight;
            ledContainer.getChildren().add(specularHighlight);
        }

        updateVisuals();
    }

    private void updateVisuals() {
        if (lensShape == null) {
            rebuildShapes();
            return;
        }

        boolean isOn = on.get();
        LedColor ledCol = color.get() != null ? color.get() : LedColor.GREEN;

        if (isOn) {
            // Emisión de luz brillante y núcleo radiante
            Color base = ledCol.getColorOn();
            Color brightCenter = Color.color(
                Math.min(1.0, base.getRed() * 0.5 + 0.5),
                Math.min(1.0, base.getGreen() * 0.5 + 0.5),
                Math.min(1.0, base.getBlue() * 0.5 + 0.5)
            );

            RadialGradient lensGrad = new RadialGradient(
                0, 0, 0.45, 0.40, 0.65, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, brightCenter),
                new Stop(0.65, base),
                new Stop(1.0, Color.color(base.getRed() * 0.7, base.getGreen() * 0.7, base.getBlue() * 0.7))
            );
            lensShape.setFill(lensGrad);

            // Resplandor (Glow)
            if (glow.get()) {
                double glowRadius = Math.max(6.0, ledSize.get() * 0.65);
                DropShadow glowEffect = new DropShadow();
                glowEffect.setColor(Color.color(base.getRed(), base.getGreen(), base.getBlue(), 0.75));
                glowEffect.setRadius(glowRadius);
                glowEffect.setSpread(0.25);
                lensShape.setEffect(glowEffect);
            } else {
                lensShape.setEffect(null);
            }

            if (specularHighlight != null) {
                specularHighlight.setOpacity(0.8);
            }
        } else {
            // Estado OFF: color apagado tenue y oscuro
            Color offBase = ledCol.getColorOff();
            RadialGradient lensGrad = new RadialGradient(
                0, 0, 0.45, 0.40, 0.65, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.color(offBase.getRed() * 1.3, offBase.getGreen() * 1.3, offBase.getBlue() * 1.3)),
                new Stop(0.85, offBase),
                new Stop(1.0, Color.color(offBase.getRed() * 0.6, offBase.getGreen() * 0.6, offBase.getBlue() * 0.6))
            );
            lensShape.setFill(lensGrad);
            lensShape.setEffect(null);

            if (specularHighlight != null) {
                specularHighlight.setOpacity(0.35);
            }
        }
    }

    private void startBlinking() {
        stopBlinking();
        blinkTimeline = new Timeline(
            new KeyFrame(Duration.millis(blinkRateMs.get()), e -> on.set(!on.get()))
        );
        blinkTimeline.setCycleCount(Timeline.INDEFINITE);
        blinkTimeline.play();
    }

    private void stopBlinking() {
        if (blinkTimeline != null) {
            blinkTimeline.stop();
            blinkTimeline = null;
        }
    }

    // --- Fluent API Helpers ---

    public JLed on() {
        setOn(true);
        return this;
    }

    public JLed off() {
        setOn(false);
        setBlinking(false);
        return this;
    }

    public JLed toggle() {
        setOn(!isOn());
        return this;
    }

    public JLed withColor(LedColor color) {
        setColor(color);
        return this;
    }

    public JLed withSize(double size) {
        setLedSize(size);
        return this;
    }

    public JLed withShape(LedShape shape) {
        setLedShape(shape);
        return this;
    }

    public JLed withBezel(boolean hasBezel) {
        setBezel(hasBezel);
        return this;
    }

    public JLed withText(String text) {
        setText(text);
        return this;
    }

    public JLed blink(boolean enable) {
        setBlinking(enable);
        return this;
    }

    public JLed blink(int rateMs) {
        setBlinkRateMs(rateMs);
        setBlinking(true);
        return this;
    }

    // --- Getters & Setters ---

    public boolean isOn() {
        return on.get();
    }

    public void setOn(boolean value) {
        this.on.set(value);
    }

    public BooleanProperty onProperty() {
        return on;
    }

    public boolean isBlinking() {
        return blinking.get();
    }

    public void setBlinking(boolean value) {
        this.blinking.set(value);
    }

    public BooleanProperty blinkingProperty() {
        return blinking;
    }

    public int getBlinkRateMs() {
        return blinkRateMs.get();
    }

    public void setBlinkRateMs(int rateMs) {
        this.blinkRateMs.set(rateMs);
    }

    public IntegerProperty blinkRateMsProperty() {
        return blinkRateMs;
    }

    public LedColor getColor() {
        return color.get();
    }

    public void setColor(LedColor color) {
        this.color.set(color);
    }

    public ObjectProperty<LedColor> colorProperty() {
        return color;
    }

    public LedShape getLedShape() {
        return ledShape.get();
    }

    public void setLedShape(LedShape shape) {
        this.ledShape.set(shape);
    }

    public ObjectProperty<LedShape> ledShapeProperty() {
        return ledShape;
    }

    public double getLedSize() {
        return ledSize.get();
    }

    public void setLedSize(double size) {
        this.ledSize.set(size);
    }

    public DoubleProperty ledSizeProperty() {
        return ledSize;
    }

    public boolean hasBezel() {
        return bezel.get();
    }

    public void setBezel(boolean hasBezel) {
        this.bezel.set(hasBezel);
    }

    public BooleanProperty bezelProperty() {
        return bezel;
    }

    public boolean hasGlow() {
        return glow.get();
    }

    public void setGlow(boolean hasGlow) {
        this.glow.set(hasGlow);
    }

    public BooleanProperty glowProperty() {
        return glow;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public StringProperty textProperty() {
        return text;
    }

    public boolean isInteractive() {
        return interactive.get();
    }

    public void setInteractive(boolean interactive) {
        this.interactive.set(interactive);
    }

    public BooleanProperty interactiveProperty() {
        return interactive;
    }
}
