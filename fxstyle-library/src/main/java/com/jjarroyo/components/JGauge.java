package com.jjarroyo.components;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * JGauge - Componente reactivo de medidor tipo tacometro / velocimetro para JavaFX.
 * Inspirado en dashboards industriales y dispositivos IoT en tiempo real.
 * 
 * Soporta estilos avanzados de IoT:
 * - RADIAL_CLASSIC: Tacometro deportivo 270 grados (RPM, velocidad, etc.)
 * - SEMI_CIRCLE: Medidor semicircular 180 grados con zonas continuas (Temperatura, etc.)
 * - ARC_PROGRESS: Arco de progreso grueso con puntero exterior y valor gigante (Humedad, SOC de bateria, etc.)
 * - LINEAR: Regla analogica horizontal con cursor deslizante (Voltaje de bateria, etc.)
 * - AMPERAGE: Amperimetro con cero central (-30A a +30A) para carga/descarga solar y baterias.
 * - WIND_SPEED: Anemometro de viento con escala meteorologica (km/h, m/s).
 * - PRESSURE: Manometro industrial de presion con escala de seguridad (Bar, PSI).
 * - LEVEL_VERTICAL: Tanque / deposito de fluido vertical reactivo (Nivel de agua, combustible, etc.).
 */
public class JGauge extends StackPane {

    private final GaugeType type;
    private final DoubleProperty value = new SimpleDoubleProperty(0.0);
    private final DoubleProperty animatedValue = new SimpleDoubleProperty(0.0);
    private final DoubleProperty minValue = new SimpleDoubleProperty(0.0);
    private final DoubleProperty maxValue = new SimpleDoubleProperty(100.0);
    private final DoubleProperty majorTickUnit = new SimpleDoubleProperty(10.0);
    private final DoubleProperty minorTickUnit = new SimpleDoubleProperty(2.5);

    private final StringProperty unit = new SimpleStringProperty("");
    private final StringProperty title = new SimpleStringProperty("");
    private final StringProperty subtitle = new SimpleStringProperty("");
    private final StringProperty multiplierLabel = new SimpleStringProperty("");

    private final IntegerProperty decimalPlaces = new SimpleIntegerProperty(0);
    private final BooleanProperty animated = new SimpleBooleanProperty(true);
    private final IntegerProperty animationDurationMs = new SimpleIntegerProperty(800);

    private final ObjectProperty<GaugeStatus> currentStatus = new SimpleObjectProperty<>(GaugeStatus.OK);
    private final List<GaugeZone> zones = new ArrayList<>();
    private Function<Double, String> tickLabelFormatter = null;

    // Panes graficos internos
    private final Group rootGraphicGroup = new Group();
    private final Pane dynamicPane = new Pane();
    private final Pane staticBackgroundPane = new Pane();

    // Referencias a elementos dinamicos
    private Node needleNode;
    private Rotate needleRotate;
    private Translate needleTranslate;
    private Path progressArcPath;
    private Rectangle tankFillRect;
    private Text valueText;
    private Text unitText;
    private Text statusDescText;

    private Timeline timeline;
    private DecimalFormat decimalFormat = new DecimalFormat("#,##0");

    // Constantes de estilo de color segun el diseno IoT
    public static final String COLOR_OK = "#1F8A70";
    public static final String COLOR_WARN = "#E29A12";
    public static final String COLOR_BAD = "#C8442F";
    public static final String COLOR_ACC = "#0E6F7C";
    public static final String COLOR_INK = "#17303A";
    public static final String COLOR_MUTE = "#5F747B";
    public static final String COLOR_LINE = "#D3DCDD";
    public static final String COLOR_BLUE = "#2563EB";

    public JGauge() {
        this(GaugeType.RADIAL_CLASSIC);
    }

    public JGauge(GaugeType type) {
        this.type = type != null ? type : GaugeType.RADIAL_CLASSIC;
        getStyleClass().addAll("j-gauge", "gauge-" + this.type.name().toLowerCase().replace('_', '-'));
        setAlignment(Pos.CENTER);

        // Ajustar valores por defecto segun el tipo
        applyDefaultConfigForType(this.type);

        // Estructura visual
        dynamicPane.setPickOnBounds(false);
        staticBackgroundPane.setPickOnBounds(false);
        rootGraphicGroup.getChildren().addAll(staticBackgroundPane, dynamicPane);
        getChildren().add(rootGraphicGroup);

        // Listener reactivo sobre el valor animado: actualiza aguja, arcos y texto en cada tick
        animatedValue.addListener((obs, oldV, newV) -> {
            updateVisuals(newV.doubleValue());
        });

        // Al cambiar value externamente, iniciar animacion suave
        value.addListener((obs, oldV, newV) -> {
            applyValueChange(newV.doubleValue());
        });

        decimalPlaces.addListener((obs, oldV, newV) -> {
            updateFormatter();
            updateVisuals(animatedValue.get());
        });

        // Reconstruir visualizacion inicial
        rebuildUI();
    }

    private void applyDefaultConfigForType(GaugeType t) {
        switch (t) {
            case RADIAL_CLASSIC -> {
                minValue.set(0);
                maxValue.set(8000);
                majorTickUnit.set(1000);
                minorTickUnit.set(250);
                unit.set("rpm");
                multiplierLabel.set("×1000");
                decimalPlaces.set(0);
                setTickLabelFormatter(v -> String.valueOf((int) (v / 1000)));
                addZone(6500, 8000, GaugeStatus.BAD);
                setValueImmediate(2400);
            }
            case SEMI_CIRCLE -> {
                minValue.set(0);
                maxValue.set(100);
                majorTickUnit.set(20);
                minorTickUnit.set(5);
                unit.set("°C");
                decimalPlaces.set(1);
                addZone(0, 60, GaugeStatus.OK);
                addZone(60, 80, GaugeStatus.WARN);
                addZone(80, 100, GaugeStatus.BAD);
                setValueImmediate(42.0);
            }
            case ARC_PROGRESS -> {
                minValue.set(0);
                maxValue.set(100);
                majorTickUnit.set(20);
                minorTickUnit.set(5);
                unit.set("% humedad relativa");
                decimalPlaces.set(0);
                addZone(0, 20, GaugeStatus.BAD);
                addZone(20, 30, GaugeStatus.WARN);
                addZone(30, 70, GaugeStatus.OK);
                addZone(70, 85, GaugeStatus.WARN);
                addZone(85, 100, GaugeStatus.BAD);
                setValueImmediate(55.0);
            }
            case LINEAR -> {
                minValue.set(10);
                maxValue.set(15);
                majorTickUnit.set(1.0);
                minorTickUnit.set(0.5);
                unit.set("V");
                decimalPlaces.set(2);
                addZone(10, 11.5, GaugeStatus.BAD);
                addZone(11.5, 12.0, GaugeStatus.WARN);
                addZone(12.0, 15.0, GaugeStatus.OK);
                setValueImmediate(12.6);
            }
            case AMPERAGE -> {
                minValue.set(-30);
                maxValue.set(30);
                majorTickUnit.set(10);
                minorTickUnit.set(5);
                unit.set("A");
                decimalPlaces.set(1);
                addZone(-30, -5, GaugeStatus.WARN); // Descarga
                addZone(-5, 5, GaugeStatus.OK);     // Reposo
                addZone(5, 30, GaugeStatus.OK);     // Carga
                setValueImmediate(12.5);
            }
            case WIND_SPEED -> {
                minValue.set(0);
                maxValue.set(120);
                majorTickUnit.set(20);
                minorTickUnit.set(5);
                unit.set("km/h");
                decimalPlaces.set(0);
                addZone(0, 20, GaugeStatus.OK);      // Calma / Brisa suave
                addZone(20, 50, GaugeStatus.OK);     // Moderado
                addZone(50, 75, GaugeStatus.WARN);   // Fuerte
                addZone(75, 120, GaugeStatus.BAD);   // Temporal / Tormenta
                setValueImmediate(34.0);
            }
            case PRESSURE -> {
                minValue.set(0);
                maxValue.set(10);
                majorTickUnit.set(2);
                minorTickUnit.set(0.5);
                unit.set("Bar");
                decimalPlaces.set(1);
                addZone(0, 2, Color.web(COLOR_ACC));
                addZone(2, 7, GaugeStatus.OK);
                addZone(7, 8.5, GaugeStatus.WARN);
                addZone(8.5, 10, GaugeStatus.BAD);
                setValueImmediate(5.4);
            }
            case LEVEL_VERTICAL -> {
                minValue.set(0);
                maxValue.set(100);
                majorTickUnit.set(25);
                minorTickUnit.set(5);
                unit.set("%");
                decimalPlaces.set(0);
                addZone(0, 15, GaugeStatus.BAD);    // Reserva crítica
                addZone(15, 30, GaugeStatus.WARN);  // Nivel bajo
                addZone(30, 100, GaugeStatus.OK);   // Nivel óptimo
                setValueImmediate(68.0);
            }
        }
    }

    private void updateFormatter() {
        int places = decimalPlaces.get();
        if (places > 0) {
            StringBuilder sb = new StringBuilder("#,##0.");
            for (int i = 0; i < places; i++) {
                sb.append("0");
            }
            decimalFormat = new DecimalFormat(sb.toString());
        } else {
            decimalFormat = new DecimalFormat("#,##0");
        }
    }

    /**
     * Reconstruye los graficos estaticos y dinamicos segun el GaugeType.
     */
    public void rebuildUI() {
        staticBackgroundPane.getChildren().clear();
        dynamicPane.getChildren().clear();

        updateFormatter();

        switch (type) {
            case RADIAL_CLASSIC -> buildRadialClassic();
            case SEMI_CIRCLE -> buildSemiCircle();
            case ARC_PROGRESS -> buildArcProgress();
            case LINEAR -> buildLinear();
            case AMPERAGE -> buildAmperage();
            case WIND_SPEED -> buildWindSpeed();
            case PRESSURE -> buildPressure();
            case LEVEL_VERTICAL -> buildLevelVertical();
        }

        updateVisuals(animatedValue.get());
    }

    // =========================================================================
    // 1. RADIAL CLASSIC (Tacometro clasico con aguja deportiva, 270 grados)
    // =========================================================================
    private void buildRadialClassic() {
        double cx = 120, cy = 120;
        double a0 = -135, a1 = 135;
        double r = 100;

        Path track = createArcPath(r, a0, a1, cx, cy);
        track.setFill(Color.TRANSPARENT);
        track.setStroke(Color.web(COLOR_LINE));
        track.setStrokeWidth(5);
        track.setStrokeLineCap(StrokeLineCap.ROUND);
        staticBackgroundPane.getChildren().add(track);

        for (GaugeZone zone : zones) {
            double za0 = valueToAngle(zone.getFrom(), a0, a1);
            double za1 = valueToAngle(zone.getTo(), a0, a1);
            Path zPath = createArcPath(r, za0, za1, cx, cy);
            zPath.setFill(Color.TRANSPARENT);
            zPath.setStroke(zone.getColor());
            zPath.setStrokeWidth(5);
            staticBackgroundPane.getChildren().add(zPath);
        }

        buildTicksAndLabels(a0, a1, 92, 86, 78, 62, cx, cy, 12, staticBackgroundPane);

        if (multiplierLabel.get() != null && !multiplierLabel.get().isEmpty()) {
            Text mult = new Text(multiplierLabel.get());
            mult.setFont(Font.font("System", FontWeight.NORMAL, 10));
            mult.setFill(Color.web(COLOR_MUTE));
            mult.setTextAlignment(TextAlignment.CENTER);
            mult.setX(cx - 15);
            mult.setY(88);
            staticBackgroundPane.getChildren().add(mult);
        }

        valueText = new Text("0");
        valueText.setFont(Font.font("System", FontWeight.BOLD, 30));
        valueText.setFill(Color.web(COLOR_INK));
        valueText.setTextAlignment(TextAlignment.CENTER);
        valueText.setX(cx - 35);
        valueText.setY(182);

        unitText = new Text(unit.get());
        unitText.setFont(Font.font("System", FontWeight.NORMAL, 12));
        unitText.setFill(Color.web(COLOR_MUTE));
        unitText.setTextAlignment(TextAlignment.CENTER);
        unitText.setX(cx - 15);
        unitText.setY(202);

        dynamicPane.getChildren().addAll(valueText, unitText);

        Polygon needle = new Polygon(
            116.5, 134,
            123.5, 134,
            121.0, 30,
            119.0, 30
        );
        needle.setFill(Color.web(COLOR_BAD));

        needleRotate = new Rotate(a0, cx, cy);
        needle.getTransforms().add(needleRotate);
        needleNode = needle;

        Circle outerPivot = new Circle(cx, cy, 10, Color.web(COLOR_INK));
        Circle innerPivot = new Circle(cx, cy, 3.5, Color.WHITE);

        dynamicPane.getChildren().addAll(needle, outerPivot, innerPivot);

        rootGraphicGroup.prefWidth(240);
        rootGraphicGroup.prefHeight(230);
        setPrefSize(260, 240);
    }

    // =========================================================================
    // 2. SEMI CIRCLE (Semicirculo de 180 grados con zonas continuas)
    // =========================================================================
    private void buildSemiCircle() {
        double cx = 120, cy = 125;
        double a0 = -90, a1 = 90;
        double r = 94;

        if (zones.isEmpty()) {
            Path track = createArcPath(r, a0, a1, cx, cy);
            track.setFill(Color.TRANSPARENT);
            track.setStroke(Color.web(COLOR_OK));
            track.setStrokeWidth(14);
            track.setStrokeLineCap(StrokeLineCap.ROUND);
            staticBackgroundPane.getChildren().add(track);
        } else {
            for (GaugeZone zone : zones) {
                double za0 = valueToAngle(zone.getFrom(), a0, a1);
                double za1 = valueToAngle(zone.getTo(), a0, a1);
                Path zPath = createArcPath(r, za0, za1, cx, cy);
                zPath.setFill(Color.TRANSPARENT);
                zPath.setStroke(zone.getColor());
                zPath.setStrokeWidth(14);
                staticBackgroundPane.getChildren().add(zPath);
            }
        }

        buildTicksAndLabels(a0, a1, 80, 74, 68, 54, cx, cy, 11, staticBackgroundPane);

        Group needleGroup = new Group();
        Line needleLine = new Line(cx, cy, cx, 44);
        needleLine.setStroke(Color.web(COLOR_INK));
        needleLine.setStrokeWidth(3);
        needleLine.setStrokeLineCap(StrokeLineCap.ROUND);

        Circle needleHead = new Circle(cx, 44, 4, Color.web(COLOR_INK));
        needleGroup.getChildren().addAll(needleLine, needleHead);

        needleRotate = new Rotate(a0, cx, cy);
        needleGroup.getTransforms().add(needleRotate);
        needleNode = needleGroup;

        Circle pivot = new Circle(cx, cy, 8, Color.web(COLOR_INK));

        valueText = new Text("0");
        valueText.setFont(Font.font("System", FontWeight.BOLD, 28));
        valueText.setFill(Color.web(COLOR_INK));
        valueText.setX(cx - 30);
        valueText.setY(162);

        unitText = new Text(unit.get());
        unitText.setFont(Font.font("System", FontWeight.NORMAL, 13));
        unitText.setFill(Color.web(COLOR_MUTE));
        unitText.setX(cx + 25);
        unitText.setY(162);

        dynamicPane.getChildren().addAll(needleGroup, pivot, valueText, unitText);

        rootGraphicGroup.prefWidth(240);
        rootGraphicGroup.prefHeight(175);
        setPrefSize(260, 190);
    }

    // =========================================================================
    // 3. ARC PROGRESS (Arco grueso reactivo con puntero exterior)
    // =========================================================================
    private void buildArcProgress() {
        double cx = 120, cy = 115;
        double a0 = -120, a1 = 120;
        double r = 88;

        Path track = createArcPath(r, a0, a1, cx, cy);
        track.setFill(Color.TRANSPARENT);
        track.setStroke(Color.web(COLOR_LINE));
        track.setStrokeWidth(16);
        track.setStrokeLineCap(StrokeLineCap.ROUND);
        staticBackgroundPane.getChildren().add(track);

        progressArcPath = new Path();
        progressArcPath.setFill(Color.TRANSPARENT);
        progressArcPath.setStroke(Color.web(COLOR_OK));
        progressArcPath.setStrokeWidth(16);
        progressArcPath.setStrokeLineCap(StrokeLineCap.ROUND);
        dynamicPane.getChildren().add(progressArcPath);

        Polygon pointer = new Polygon(
            120, 19,
            113, 6,
            127, 6
        );
        pointer.setFill(Color.web(COLOR_INK));

        needleRotate = new Rotate(a0, cx, cy);
        pointer.getTransforms().add(needleRotate);
        needleNode = pointer;
        dynamicPane.getChildren().add(pointer);

        valueText = new Text("0");
        valueText.setFont(Font.font("System", FontWeight.BOLD, 50));
        valueText.setFill(Color.web(COLOR_INK));
        valueText.setTextAlignment(TextAlignment.CENTER);
        valueText.setX(cx - 30);
        valueText.setY(130);

        unitText = new Text(unit.get());
        unitText.setFont(Font.font("System", FontWeight.NORMAL, 13));
        unitText.setFill(Color.web(COLOR_MUTE));
        unitText.setTextAlignment(TextAlignment.CENTER);
        unitText.setX(cx - 50);
        unitText.setY(153);

        dynamicPane.getChildren().addAll(valueText, unitText);

        double[] ptMin = polarPoint(r, a0, cx, cy);
        double[] ptMax = polarPoint(r, a1, cx, cy);

        Text minLbl = new Text(String.valueOf((int) minValue.get()));
        minLbl.setFont(Font.font("System", FontWeight.NORMAL, 11));
        minLbl.setFill(Color.web(COLOR_MUTE));
        minLbl.setX(ptMin[0] - 10);
        minLbl.setY(184);

        Text maxLbl = new Text(String.valueOf((int) maxValue.get()));
        maxLbl.setFont(Font.font("System", FontWeight.NORMAL, 11));
        maxLbl.setFill(Color.web(COLOR_MUTE));
        maxLbl.setX(ptMax[0] - 10);
        maxLbl.setY(184);

        staticBackgroundPane.getChildren().addAll(minLbl, maxLbl);

        rootGraphicGroup.prefWidth(240);
        rootGraphicGroup.prefHeight(200);
        setPrefSize(260, 215);
    }

    // =========================================================================
    // 4. LINEAR (Medidor horizontal analógico)
    // =========================================================================
    private void buildLinear() {
        double min = minValue.get();
        double max = maxValue.get();
        double span = max - min;

        valueText = new Text("0");
        valueText.setFont(Font.font("System", FontWeight.BOLD, 32));
        valueText.setFill(Color.web(COLOR_INK));
        valueText.setX(20);
        valueText.setY(34);

        unitText = new Text(unit.get());
        unitText.setFont(Font.font("System", FontWeight.NORMAL, 14));
        unitText.setFill(Color.web(COLOR_MUTE));
        unitText.setX(95);
        unitText.setY(34);

        dynamicPane.getChildren().addAll(valueText, unitText);

        for (GaugeZone zone : zones) {
            double x1 = 20 + ((zone.getFrom() - min) / span) * 200;
            double x2 = 20 + ((zone.getTo() - min) / span) * 200;
            Rectangle rect = new Rectangle(x1, 66, Math.max(1, x2 - x1), 10);
            rect.setFill(zone.getColor());
            staticBackgroundPane.getChildren().add(rect);
        }

        double minor = minorTickUnit.get() > 0 ? minorTickUnit.get() : 0.5;
        double major = majorTickUnit.get() > 0 ? majorTickUnit.get() : 1.0;

        for (double v = min; v <= max + 1e-9; v += minor) {
            double q = (v - min) / major;
            boolean isMajor = Math.abs(q - Math.round(q)) < 1e-6;
            double vx = 20 + ((v - min) / span) * 200;

            Line tick = new Line(vx, 80, vx, isMajor ? 90 : 85);
            tick.setStroke(Color.web(COLOR_INK));
            tick.setStrokeWidth(isMajor ? 2 : 1);
            staticBackgroundPane.getChildren().add(tick);

            if (isMajor) {
                String lbl = tickLabelFormatter != null ? tickLabelFormatter.apply(v) : formatTickNumber(v);
                Text num = new Text(lbl);
                num.setFont(Font.font("System", FontWeight.NORMAL, 11));
                num.setFill(Color.web(COLOR_MUTE));
                num.setX(vx - 6);
                num.setY(104);
                staticBackgroundPane.getChildren().add(num);
            }
        }

        Group cursorGroup = new Group();
        Polygon arrow = new Polygon(
            0, 64,
            -6, 48,
            6, 48
        );
        arrow.setFill(Color.web(COLOR_INK));

        Line guide = new Line(0, 64, 0, 78);
        guide.setStroke(Color.web(COLOR_INK));
        guide.setStrokeWidth(2);

        cursorGroup.getChildren().addAll(arrow, guide);

        needleTranslate = new Translate(20, 0);
        cursorGroup.getTransforms().add(needleTranslate);
        needleNode = cursorGroup;
        dynamicPane.getChildren().add(cursorGroup);

        rootGraphicGroup.prefWidth(240);
        rootGraphicGroup.prefHeight(120);
        setPrefSize(260, 135);
    }

    // =========================================================================
    // 5. AMPERAGE (Amperímetro con cero central para carga/descarga)
    // =========================================================================
    private void buildAmperage() {
        double cx = 120, cy = 135;
        double a0 = -75, a1 = 75; // 0A en el centro exacto (a las 12:00)
        double r = 95;

        // Pista semicircular superior
        Path track = createArcPath(r, a0, a1, cx, cy);
        track.setFill(Color.TRANSPARENT);
        track.setStroke(Color.web(COLOR_LINE));
        track.setStrokeWidth(6);
        track.setStrokeLineCap(StrokeLineCap.ROUND);
        staticBackgroundPane.getChildren().add(track);

        // Zona Izquierda: Descarga (ambar)
        Path descPath = createArcPath(r, a0, 0, cx, cy);
        descPath.setFill(Color.TRANSPARENT);
        descPath.setStroke(Color.web(COLOR_WARN));
        descPath.setStrokeWidth(6);
        staticBackgroundPane.getChildren().add(descPath);

        // Zona Derecha: Carga (verde)
        Path cargaPath = createArcPath(r, 0, a1, cx, cy);
        cargaPath.setFill(Color.TRANSPARENT);
        cargaPath.setStroke(Color.web(COLOR_OK));
        cargaPath.setStrokeWidth(6);
        staticBackgroundPane.getChildren().add(cargaPath);

        // Ticks y numeros
        buildTicksAndLabels(a0, a1, 85, 78, 70, 56, cx, cy, 11, staticBackgroundPane);

        // Rotulos "DESCARGA" y "CARGA"
        Text descLabel = new Text("DESCARGA");
        descLabel.setFont(Font.font("System", FontWeight.BOLD, 9));
        descLabel.setFill(Color.web(COLOR_WARN));
        descLabel.setX(24);
        descLabel.setY(138);

        Text cargaLabel = new Text("CARGA");
        cargaLabel.setFont(Font.font("System", FontWeight.BOLD, 9));
        cargaLabel.setFill(Color.web(COLOR_OK));
        cargaLabel.setX(176);
        cargaLabel.setY(138);

        staticBackgroundPane.getChildren().addAll(descLabel, cargaLabel);

        // Display Digital central inferior
        valueText = new Text("0.0");
        valueText.setFont(Font.font("System", FontWeight.BOLD, 30));
        valueText.setFill(Color.web(COLOR_INK));
        valueText.setX(cx - 30);
        valueText.setY(185);

        unitText = new Text(unit.get());
        unitText.setFont(Font.font("System", FontWeight.NORMAL, 13));
        unitText.setFill(Color.web(COLOR_MUTE));
        unitText.setX(cx + 25);
        unitText.setY(185);

        statusDescText = new Text("Flujo de corriente");
        statusDescText.setFont(Font.font("System", FontWeight.NORMAL, 11));
        statusDescText.setFill(Color.web(COLOR_MUTE));
        statusDescText.setX(cx - 45);
        statusDescText.setY(205);

        dynamicPane.getChildren().addAll(valueText, unitText, statusDescText);

        // Aguja estilizada de amperimetro
        Group needleGroup = new Group();
        Line needleLine = new Line(cx, cy, cx, 48);
        needleLine.setStroke(Color.web(COLOR_INK));
        needleLine.setStrokeWidth(2.5);
        needleLine.setStrokeLineCap(StrokeLineCap.ROUND);

        Circle tip = new Circle(cx, 48, 3.5, Color.web(COLOR_BAD));
        needleGroup.getChildren().addAll(needleLine, tip);

        needleRotate = new Rotate(0, cx, cy);
        needleGroup.getTransforms().add(needleRotate);
        needleNode = needleGroup;

        Circle pivot = new Circle(cx, cy, 7, Color.web(COLOR_INK));
        dynamicPane.getChildren().addAll(needleGroup, pivot);

        rootGraphicGroup.prefWidth(240);
        rootGraphicGroup.prefHeight(215);
        setPrefSize(260, 225);
    }

    // =========================================================================
    // 6. WIND SPEED (Anemómetro de Viento con Escala Meteorológica)
    // =========================================================================
    private void buildWindSpeed() {
        double cx = 120, cy = 120;
        double a0 = -120, a1 = 120;
        double r = 96;

        // Pista de fondo
        Path track = createArcPath(r, a0, a1, cx, cy);
        track.setFill(Color.TRANSPARENT);
        track.setStroke(Color.web(COLOR_LINE));
        track.setStrokeWidth(8);
        track.setStrokeLineCap(StrokeLineCap.ROUND);
        staticBackgroundPane.getChildren().add(track);

        // Franjas de zonas de viento (Brisa, Fuerte, Tormenta)
        for (GaugeZone zone : zones) {
            double za0 = valueToAngle(zone.getFrom(), a0, a1);
            double za1 = valueToAngle(zone.getTo(), a0, a1);
            Path zPath = createArcPath(r, za0, za1, cx, cy);
            zPath.setFill(Color.TRANSPARENT);
            zPath.setStroke(zone.getColor());
            zPath.setStrokeWidth(8);
            staticBackgroundPane.getChildren().add(zPath);
        }

        buildTicksAndLabels(a0, a1, 88, 82, 74, 60, cx, cy, 11, staticBackgroundPane);

        // Display Central
        valueText = new Text("0");
        valueText.setFont(Font.font("System", FontWeight.BOLD, 38));
        valueText.setFill(Color.web(COLOR_INK));
        valueText.setTextAlignment(TextAlignment.CENTER);
        valueText.setX(cx - 20);
        valueText.setY(155);

        unitText = new Text(unit.get());
        unitText.setFont(Font.font("System", FontWeight.NORMAL, 13));
        unitText.setFill(Color.web(COLOR_MUTE));
        unitText.setX(cx - 15);
        unitText.setY(175);

        statusDescText = new Text("Viento");
        statusDescText.setFont(Font.font("System", FontWeight.BOLD, 12));
        statusDescText.setFill(Color.web(COLOR_ACC));
        statusDescText.setX(cx - 20);
        statusDescText.setY(95);

        dynamicPane.getChildren().addAll(valueText, unitText, statusDescText);

        // Aguja estilo flecha/veleta de viento con aleta
        Polygon arrowNeedle = new Polygon(
            cx - 3, cy + 16,
            cx + 3, cy + 16,
            cx + 1.5, cy - 65,
            cx + 6, cy - 65,
            cx, cy - 80,
            cx - 6, cy - 65,
            cx - 1.5, cy - 65
        );
        arrowNeedle.setFill(Color.web(COLOR_ACC));

        needleRotate = new Rotate(a0, cx, cy);
        arrowNeedle.getTransforms().add(needleRotate);
        needleNode = arrowNeedle;

        Circle pivot = new Circle(cx, cy, 8, Color.web(COLOR_INK));
        Circle pivotDot = new Circle(cx, cy, 3, Color.WHITE);

        dynamicPane.getChildren().addAll(arrowNeedle, pivot, pivotDot);

        rootGraphicGroup.prefWidth(240);
        rootGraphicGroup.prefHeight(200);
        setPrefSize(260, 215);
    }

    // =========================================================================
    // 7. PRESSURE (Manómetro Industrial de Presión Bar / PSI)
    // =========================================================================
    private void buildPressure() {
        double cx = 120, cy = 120;
        double a0 = -135, a1 = 135;
        double r = 98;

        // Dial con borde doble industrial
        Circle dialBg = new Circle(cx, cy, r + 6);
        dialBg.setFill(Color.TRANSPARENT);
        dialBg.setStroke(Color.web(COLOR_LINE));
        dialBg.setStrokeWidth(1.5);
        staticBackgroundPane.getChildren().add(dialBg);

        Path track = createArcPath(r, a0, a1, cx, cy);
        track.setFill(Color.TRANSPARENT);
        track.setStroke(Color.web(COLOR_LINE));
        track.setStrokeWidth(5);
        track.setStrokeLineCap(StrokeLineCap.ROUND);
        staticBackgroundPane.getChildren().add(track);

        // Franjas de presion segura vs sobrepresion
        for (GaugeZone zone : zones) {
            double za0 = valueToAngle(zone.getFrom(), a0, a1);
            double za1 = valueToAngle(zone.getTo(), a0, a1);
            Path zPath = createArcPath(r, za0, za1, cx, cy);
            zPath.setFill(Color.TRANSPARENT);
            zPath.setStroke(zone.getColor());
            zPath.setStrokeWidth(5);
            staticBackgroundPane.getChildren().add(zPath);
        }

        buildTicksAndLabels(a0, a1, 90, 84, 76, 60, cx, cy, 11, staticBackgroundPane);

        Text dialLabel = new Text("PRESSURE");
        dialLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
        dialLabel.setFill(Color.web(COLOR_MUTE));
        dialLabel.setX(cx - 28);
        dialLabel.setY(88);
        staticBackgroundPane.getChildren().add(dialLabel);

        // Display
        valueText = new Text("0.0");
        valueText.setFont(Font.font("System", FontWeight.BOLD, 28));
        valueText.setFill(Color.web(COLOR_INK));
        valueText.setX(cx - 22);
        valueText.setY(165);

        unitText = new Text(unit.get());
        unitText.setFont(Font.font("System", FontWeight.BOLD, 12));
        unitText.setFill(Color.web(COLOR_MUTE));
        unitText.setX(cx - 10);
        unitText.setY(182);

        dynamicPane.getChildren().addAll(valueText, unitText);

        // Aguja fina con contrapeso circular trasero clasico de manometro
        Group needleGroup = new Group();
        Line needleLine = new Line(cx, cy + 20, cx, cy - 78);
        needleLine.setStroke(Color.web(COLOR_BAD));
        needleLine.setStrokeWidth(2);

        Circle counterWeight = new Circle(cx, cy + 15, 4.5, Color.web(COLOR_BAD));
        needleGroup.getChildren().addAll(needleLine, counterWeight);

        needleRotate = new Rotate(a0, cx, cy);
        needleGroup.getTransforms().add(needleRotate);
        needleNode = needleGroup;

        Circle pivot = new Circle(cx, cy, 7, Color.web(COLOR_INK));
        dynamicPane.getChildren().addAll(needleGroup, pivot);

        rootGraphicGroup.prefWidth(240);
        rootGraphicGroup.prefHeight(210);
        setPrefSize(260, 225);
    }

    // =========================================================================
    // 8. LEVEL VERTICAL (Tanque / Nivel de líquido vertical IoT)
    // =========================================================================
    private void buildLevelVertical() {
        double tankW = 70;
        double tankH = 150;
        double tankX = 50;
        double tankY = 30;

        // Contorno de tanque con esquinas redondeadas
        Rectangle tankTrack = new Rectangle(tankX, tankY, tankW, tankH);
        tankTrack.setArcWidth(20);
        tankTrack.setArcHeight(20);
        tankTrack.setFill(Color.web("#F1F5F9"));
        tankTrack.setStroke(Color.web(COLOR_LINE));
        tankTrack.setStrokeWidth(2);
        staticBackgroundPane.getChildren().add(tankTrack);

        // Relleno dinámico de nivel de líquido
        tankFillRect = new Rectangle(tankX, tankY + tankH, tankW, 0);
        tankFillRect.setArcWidth(18);
        tankFillRect.setArcHeight(18);
        tankFillRect.setFill(Color.web(COLOR_ACC));

        // Clip para respetar esquinas del tanque
        Rectangle clip = new Rectangle(tankX, tankY, tankW, tankH);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        tankFillRect.setClip(clip);
        dynamicPane.getChildren().add(tankFillRect);

        // Regla de marcas laterales en el costado derecho (0%, 25%, 50%, 75%, 100%)
        double min = minValue.get();
        double max = maxValue.get();
        double span = max - min;

        for (int step = 0; step <= 4; step++) {
            double ratio = step / 4.0;
            double vy = tankY + tankH - (ratio * tankH);
            Line tick = new Line(tankX + tankW + 6, vy, tankX + tankW + 14, vy);
            tick.setStroke(Color.web(COLOR_MUTE));
            tick.setStrokeWidth(1.5);

            int pct = (int) Math.round(min + ratio * span);
            Text num = new Text(pct + "%");
            num.setFont(Font.font("System", FontWeight.NORMAL, 10));
            num.setFill(Color.web(COLOR_MUTE));
            num.setX(tankX + tankW + 18);
            num.setY(vy + 3);

            staticBackgroundPane.getChildren().addAll(tick, num);
        }

        // Display Central dentro del tanque
        valueText = new Text("0%");
        valueText.setFont(Font.font("System", FontWeight.BOLD, 26));
        valueText.setFill(Color.web(COLOR_INK));
        valueText.setX(tankX + 14);
        valueText.setY(tankY + tankH / 2.0);

        unitText = new Text("Nivel de tanque");
        unitText.setFont(Font.font("System", FontWeight.NORMAL, 11));
        unitText.setFill(Color.web(COLOR_MUTE));
        unitText.setX(tankX - 10);
        unitText.setY(tankY + tankH + 24);

        dynamicPane.getChildren().addAll(valueText, unitText);

        rootGraphicGroup.prefWidth(220);
        rootGraphicGroup.prefHeight(220);
        setPrefSize(240, 235);
    }

    // =========================================================================
    // Actualizacion Dinámica (Aguja, Arcos, Texto y Zonas)
    // =========================================================================
    private void updateVisuals(double currentVal) {
        double min = minValue.get();
        double max = maxValue.get();
        double clamped = Math.max(min, Math.min(max, currentVal));

        if (valueText != null) {
            if (type == GaugeType.AMPERAGE) {
                String sign = clamped > 0 ? "+" : "";
                valueText.setText(sign + decimalFormat.format(clamped));
            } else if (type == GaugeType.LEVEL_VERTICAL) {
                valueText.setText(decimalFormat.format(clamped) + "%");
            } else {
                valueText.setText(decimalFormat.format(clamped));
            }
            adjustTextPosition(clamped);
        }

        GaugeStatus status = resolveStatus(clamped);
        if (currentStatus.get() != status) {
            currentStatus.set(status);
        }

        switch (type) {
            case RADIAL_CLASSIC -> {
                double a0 = -135, a1 = 135;
                double angle = valueToAngle(clamped, a0, a1);
                if (needleRotate != null) needleRotate.setAngle(angle);
            }
            case SEMI_CIRCLE -> {
                double a0 = -90, a1 = 90;
                double angle = valueToAngle(clamped, a0, a1);
                if (needleRotate != null) needleRotate.setAngle(angle);
            }
            case ARC_PROGRESS -> {
                double a0 = -120, a1 = 120;
                double angle = valueToAngle(clamped, a0, a1);
                if (needleRotate != null) needleRotate.setAngle(angle);

                double cx = 120, cy = 115, r = 88;
                if (progressArcPath != null) {
                    progressArcPath.getElements().clear();
                    if (clamped > min) {
                        Path newArc = createArcPath(r, a0, Math.max(a0 + 0.1, angle), cx, cy);
                        progressArcPath.getElements().addAll(newArc.getElements());
                        progressArcPath.setStroke(Color.web(status.getDefaultHexColor()));
                    }
                }
            }
            case LINEAR -> {
                double span = max - min;
                double x = 20 + ((clamped - min) / span) * 200;
                if (needleTranslate != null) needleTranslate.setX(x);
            }
            case AMPERAGE -> {
                double a0 = -75, a1 = 75;
                double angle = valueToAngle(clamped, a0, a1);
                if (needleRotate != null) needleRotate.setAngle(angle);
                if (statusDescText != null) {
                    if (clamped > 1.0) statusDescText.setText("Cargando batería");
                    else if (clamped < -1.0) statusDescText.setText("Consumiendo carga");
                    else statusDescText.setText("Reposo / Standby");
                }
            }
            case WIND_SPEED -> {
                double a0 = -120, a1 = 120;
                double angle = valueToAngle(clamped, a0, a1);
                if (needleRotate != null) needleRotate.setAngle(angle);
                if (statusDescText != null) {
                    if (clamped < 20) statusDescText.setText("Brisa suave");
                    else if (clamped < 50) statusDescText.setText("Viento moderado");
                    else if (clamped < 75) statusDescText.setText("Viento fuerte");
                    else statusDescText.setText("Temporal crítico");
                }
            }
            case PRESSURE -> {
                double a0 = -135, a1 = 135;
                double angle = valueToAngle(clamped, a0, a1);
                if (needleRotate != null) needleRotate.setAngle(angle);
            }
            case LEVEL_VERTICAL -> {
                double tankW = 70;
                double tankH = 150;
                double tankX = 50;
                double tankY = 30;
                double ratio = (clamped - min) / (max - min);
                double fillHeight = tankH * Math.max(0, Math.min(1.0, ratio));

                if (tankFillRect != null) {
                    tankFillRect.setY(tankY + tankH - fillHeight);
                    tankFillRect.setHeight(fillHeight);
                    tankFillRect.setFill(Color.web(status.getDefaultHexColor()));
                }
            }
        }
    }

    private void adjustTextPosition(double val) {
        if (valueText == null) return;

        double valW = valueText.getLayoutBounds().getWidth();
        double unitW = (unitText != null && unitText.getText() != null && !unitText.getText().isEmpty())
                ? unitText.getLayoutBounds().getWidth() : 0.0;
        double cx = 120;

        switch (type) {
            case RADIAL_CLASSIC -> {
                valueText.setX(cx - (valW / 2.0));
                if (unitText != null) unitText.setX(cx - (unitW / 2.0));
            }
            case SEMI_CIRCLE -> {
                double gap = 5.0;
                double totalW = valW + (unitW > 0 ? (gap + unitW) : 0.0);
                double startX = cx - (totalW / 2.0);
                valueText.setX(startX);
                if (unitText != null) {
                    unitText.setX(startX + valW + gap);
                }
            }
            case ARC_PROGRESS -> {
                valueText.setX(cx - (valW / 2.0));
                if (unitText != null) unitText.setX(cx - (unitW / 2.0));
            }
            case LINEAR -> {
                valueText.setX(20);
                if (unitText != null) {
                    unitText.setX(20 + valW + 6.0);
                }
            }
            case AMPERAGE -> {
                double gap = 5.0;
                double totalW = valW + (unitW > 0 ? (gap + unitW) : 0.0);
                double startX = cx - (totalW / 2.0);
                valueText.setX(startX);
                if (unitText != null) {
                    unitText.setX(startX + valW + gap);
                }
                if (statusDescText != null) {
                    double descW = statusDescText.getLayoutBounds().getWidth();
                    statusDescText.setX(cx - (descW / 2.0));
                }
            }
            case WIND_SPEED -> {
                valueText.setX(cx - (valW / 2.0));
                if (unitText != null) unitText.setX(cx - (unitW / 2.0));
                if (statusDescText != null) {
                    double descW = statusDescText.getLayoutBounds().getWidth();
                    statusDescText.setX(cx - (descW / 2.0));
                }
            }
            case PRESSURE -> {
                valueText.setX(cx - (valW / 2.0));
                if (unitText != null) unitText.setX(cx - (unitW / 2.0));
            }
            case LEVEL_VERTICAL -> {
                double tankW = 70;
                double tankX = 50;
                valueText.setX(tankX + (tankW - valW) / 2.0);
                if (unitText != null) {
                    unitText.setX(tankX + (tankW - unitW) / 2.0);
                }
            }
        }
    }

    private GaugeStatus resolveStatus(double v) {
        for (GaugeZone z : zones) {
            if (z.contains(v)) {
                if (z.getStatus() != null) return z.getStatus();
                return GaugeStatus.OK;
            }
        }
        return GaugeStatus.OK;
    }

    // =========================================================================
    // Control de Animacion Reactiva Fluida (Punto A -> Punto B en tiempo real)
    // =========================================================================
    private void applyValueChange(double targetValue) {
        if (!animated.get()) {
            animatedValue.set(targetValue);
            return;
        }

        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            timeline.stop();
        }

        double startVal = animatedValue.get();
        int duration = animationDurationMs.get();

        // Curva suave con aceleración y desaceleración natural (ease-in-out)
        Interpolator interpolator = Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0);

        timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(animatedValue, startVal)),
            new KeyFrame(Duration.millis(duration), new KeyValue(animatedValue, targetValue, interpolator))
        );
        timeline.play();
    }

    public void setValue(double targetValue) {
        this.value.set(targetValue);
    }

    public void setValue(double targetValue, int durationMs) {
        setAnimationDurationMs(durationMs);
        this.value.set(targetValue);
    }

    public void setValueImmediate(double targetValue) {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            timeline.stop();
        }
        this.value.set(targetValue);
        this.animatedValue.set(targetValue);
        updateVisuals(targetValue);
    }

    // =========================================================================
    // Utilidades Matematicas y Geometria Polar
    // =========================================================================
    private double valueToAngle(double val, double a0, double a1) {
        double min = minValue.get();
        double max = maxValue.get();
        if (max <= min) return a0;
        return a0 + ((val - min) / (max - min)) * (a1 - a0);
    }

    private double[] polarPoint(double r, double aDeg, double cx, double cy) {
        double rad = Math.toRadians(aDeg);
        return new double[]{
            cx + r * Math.sin(rad),
            cy - r * Math.cos(rad)
        };
    }

    private Path createArcPath(double r, double a0, double a1, double cx, double cy) {
        double[] s = polarPoint(r, a0, cx, cy);
        double[] e = polarPoint(r, a1, cx, cy);

        Path path = new Path();
        path.getElements().add(new MoveTo(s[0], s[1]));

        ArcTo arcTo = new ArcTo();
        arcTo.setX(e[0]);
        arcTo.setY(e[1]);
        arcTo.setRadiusX(r);
        arcTo.setRadiusY(r);
        arcTo.setLargeArcFlag((a1 - a0) > 180);
        arcTo.setSweepFlag(true);

        path.getElements().add(arcTo);
        return path;
    }

    private void buildTicksAndLabels(double a0, double a1, double ro, double rm, double rM, double rl,
                                     double cx, double cy, double fs, Pane targetPane) {
        double min = minValue.get();
        double max = maxValue.get();
        double minor = minorTickUnit.get() > 0 ? minorTickUnit.get() : 1.0;
        double major = majorTickUnit.get() > 0 ? majorTickUnit.get() : 10.0;
        int n = (int) Math.round((max - min) / minor);

        for (int i = 0; i <= n; i++) {
            double v = min + i * minor;
            double q = (v - min) / major;
            boolean isMajor = Math.abs(q - Math.round(q)) < 1e-6;
            double angle = valueToAngle(v, a0, a1);

            double[] p = polarPoint(ro, angle, cx, cy);
            double[] e = polarPoint(isMajor ? rM : rm, angle, cx, cy);

            Line tick = new Line(p[0], p[1], e[0], e[1]);
            tick.setStroke(Color.web(COLOR_INK));
            tick.setStrokeWidth(isMajor ? 2 : 1);
            targetPane.getChildren().add(tick);

            if (isMajor) {
                double[] l = polarPoint(rl, angle, cx, cy);
                String labelText = tickLabelFormatter != null ? tickLabelFormatter.apply(v) : formatTickNumber(v);
                Text text = new Text(labelText);
                text.setFont(Font.font("System", FontWeight.NORMAL, fs));
                text.setFill(Color.web(COLOR_MUTE));
                text.setTextAlignment(TextAlignment.CENTER);
                text.setX(l[0] - (labelText.length() * 3.2));
                text.setY(l[1] + 4);
                targetPane.getChildren().add(text);
            }
        }
    }

    private String formatTickNumber(double v) {
        if (Math.abs(v - Math.round(v)) < 1e-6) {
            return String.valueOf((int) Math.round(v));
        }
        return String.valueOf(v);
    }

    // =========================================================================
    // Metodos Factory para IoT
    // =========================================================================
    public static JGauge createRpmGauge() {
        return new JGauge(GaugeType.RADIAL_CLASSIC);
    }

    public static JGauge createTemperatureGauge() {
        return new JGauge(GaugeType.SEMI_CIRCLE);
    }

    public static JGauge createHumidityGauge() {
        return new JGauge(GaugeType.ARC_PROGRESS);
    }

    public static JGauge createVoltageGauge() {
        return new JGauge(GaugeType.LINEAR);
    }

    public static JGauge createAmperageGauge() {
        return new JGauge(GaugeType.AMPERAGE);
    }

    public static JGauge createWindGauge() {
        return new JGauge(GaugeType.WIND_SPEED);
    }

    public static JGauge createPressureGauge() {
        return new JGauge(GaugeType.PRESSURE);
    }

    public static JGauge createTankLevelGauge() {
        return new JGauge(GaugeType.LEVEL_VERTICAL);
    }

    // =========================================================================
    // Getters, Setters y Propiedades
    // =========================================================================
    public GaugeType getType() {
        return type;
    }

    public double getValue() {
        return value.get();
    }

    public DoubleProperty valueProperty() {
        return value;
    }

    public double getAnimatedValue() {
        return animatedValue.get();
    }

    public DoubleProperty animatedValueProperty() {
        return animatedValue;
    }

    public double getMinValue() {
        return minValue.get();
    }

    public void setMinValue(double min) {
        this.minValue.set(min);
        rebuildUI();
    }

    public DoubleProperty minValueProperty() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue.get();
    }

    public void setMaxValue(double max) {
        this.maxValue.set(max);
        rebuildUI();
    }

    public DoubleProperty maxValueProperty() {
        return maxValue;
    }

    public double getMajorTickUnit() {
        return majorTickUnit.get();
    }

    public void setMajorTickUnit(double unit) {
        this.majorTickUnit.set(unit);
        rebuildUI();
    }

    public DoubleProperty majorTickUnitProperty() {
        return majorTickUnit;
    }

    public double getMinorTickUnit() {
        return minorTickUnit.get();
    }

    public void setMinorTickUnit(double unit) {
        this.minorTickUnit.set(unit);
        rebuildUI();
    }

    public DoubleProperty minorTickUnitProperty() {
        return minorTickUnit;
    }

    public String getUnit() {
        return unit.get();
    }

    public void setUnit(String unit) {
        this.unit.set(unit != null ? unit : "");
        if (unitText != null) {
            unitText.setText(this.unit.get());
            adjustTextPosition(animatedValue.get());
        }
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getSubtitle() {
        return subtitle.get();
    }

    public void setSubtitle(String subtitle) {
        this.subtitle.set(subtitle);
    }

    public StringProperty subtitleProperty() {
        return subtitle;
    }

    public String getMultiplierLabel() {
        return multiplierLabel.get();
    }

    public void setMultiplierLabel(String mult) {
        this.multiplierLabel.set(mult);
        rebuildUI();
    }

    public StringProperty multiplierLabelProperty() {
        return multiplierLabel;
    }

    public int getDecimalPlaces() {
        return decimalPlaces.get();
    }

    public void setDecimalPlaces(int places) {
        this.decimalPlaces.set(places);
    }

    public IntegerProperty decimalPlacesProperty() {
        return decimalPlaces;
    }

    public boolean isAnimated() {
        return animated.get();
    }

    public void setAnimated(boolean anim) {
        this.animated.set(anim);
    }

    public BooleanProperty animatedProperty() {
        return animated;
    }

    public int getAnimationDurationMs() {
        return animationDurationMs.get();
    }

    public void setAnimationDurationMs(int ms) {
        this.animationDurationMs.set(ms);
    }

    public IntegerProperty animationDurationMsProperty() {
        return animationDurationMs;
    }

    public GaugeStatus getCurrentStatus() {
        return currentStatus.get();
    }

    public ReadOnlyObjectProperty<GaugeStatus> statusProperty() {
        return currentStatus;
    }

    public void addZone(double from, double to, GaugeStatus status) {
        zones.add(new GaugeZone(from, to, status));
    }

    public void addZone(double from, double to, Color color) {
        zones.add(new GaugeZone(from, to, color));
    }

    public void clearZones() {
        zones.clear();
    }

    public List<GaugeZone> getZones() {
        return zones;
    }

    public void setTickLabelFormatter(Function<Double, String> formatter) {
        this.tickLabelFormatter = formatter;
        rebuildUI();
    }
}
