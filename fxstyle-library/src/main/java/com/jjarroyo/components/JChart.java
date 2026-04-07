package com.jjarroyo.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.text.DecimalFormat;

/**
 * JChart - Beautiful, animated chart component for FxStyle.
 * <p>
 * Supports: BAR, LINE, PIE, DONUT, AREA, SCATTER, HORIZONTAL_BAR
 * <p>
 * Usage is dead simple:
 * <pre>
 * JChart chart = JChart.bar("Ventas Mensuales",
 *     new String[]{"Ene", "Feb", "Mar"},
 *     new double[]{120, 200, 150});
 * </pre>
 */
public class JChart extends VBox {

    // ═══════════════════════════════════════════════════════════════════
    // TYPES
    // ═══════════════════════════════════════════════════════════════════

    public enum ChartType {
        BAR, LINE, PIE, DONUT, AREA, SCATTER, HORIZONTAL_BAR
    }

    // ═══════════════════════════════════════════════════════════════════
    // PREMIUM COLOR PALETTES
    // ═══════════════════════════════════════════════════════════════════

    /** Vibrant modern palette */
    public static final Color[] PALETTE_VIBRANT = {
        Color.web("#3b82f6"), Color.web("#10b981"), Color.web("#f59e0b"),
        Color.web("#ef4444"), Color.web("#8b5cf6"), Color.web("#ec4899"),
        Color.web("#06b6d4"), Color.web("#f97316"), Color.web("#14b8a6"),
        Color.web("#6366f1")
    };

    /** Ocean blues palette */
    public static final Color[] PALETTE_OCEAN = {
        Color.web("#0ea5e9"), Color.web("#06b6d4"), Color.web("#14b8a6"),
        Color.web("#3b82f6"), Color.web("#6366f1"), Color.web("#0284c7"),
        Color.web("#0891b2"), Color.web("#0d9488"), Color.web("#2563eb"),
        Color.web("#4f46e5")
    };

    /** Sunset warm palette */
    public static final Color[] PALETTE_SUNSET = {
        Color.web("#f97316"), Color.web("#ef4444"), Color.web("#ec4899"),
        Color.web("#f59e0b"), Color.web("#e11d48"), Color.web("#f43f5e"),
        Color.web("#fb923c"), Color.web("#fbbf24"), Color.web("#a855f7"),
        Color.web("#d946ef")
    };

    /** Forest greens palette */
    public static final Color[] PALETTE_FOREST = {
        Color.web("#10b981"), Color.web("#059669"), Color.web("#14b8a6"),
        Color.web("#0d9488"), Color.web("#22c55e"), Color.web("#16a34a"),
        Color.web("#34d399"), Color.web("#2dd4bf"), Color.web("#4ade80"),
        Color.web("#a3e635")
    };

    /** Monochromatic slate palette */
    public static final Color[] PALETTE_SLATE = {
        Color.web("#334155"), Color.web("#475569"), Color.web("#64748b"),
        Color.web("#94a3b8"), Color.web("#cbd5e1"), Color.web("#1e293b"),
        Color.web("#0f172a"), Color.web("#e2e8f0"), Color.web("#f1f5f9"),
        Color.web("#f8fafc")
    };

    // ═══════════════════════════════════════════════════════════════════
    // STATE
    // ═══════════════════════════════════════════════════════════════════

    private ChartType chartType;
    private String title;
    private String[] labels;
    private double[] values;
    private double[][] multiValues; // For multi-series
    private String[] seriesNames;
    private Color[] palette = PALETTE_VIBRANT;
    private boolean showLegend = true;
    private boolean showValues = true;
    private boolean showGrid = true;
    private boolean animated = true;
    private int canvasWidth = 500;
    private int canvasHeight = 300;

    private Canvas canvas;
    private final DoubleProperty animProgress = new SimpleDoubleProperty(0);
    private Label tooltipLabel;
    private StackPane canvasContainer;

    // ═══════════════════════════════════════════════════════════════════
    // CONSTRUCTORS
    // ═══════════════════════════════════════════════════════════════════

    public JChart(ChartType type, String title, String[] labels, double[] values) {
        this.chartType = type;
        this.title = title;
        this.labels = labels;
        this.values = values;
        init();
    }

    public JChart(ChartType type, String title, String[] labels, double[][] multiValues, String[] seriesNames) {
        this.chartType = type;
        this.title = title;
        this.labels = labels;
        this.multiValues = multiValues;
        this.seriesNames = seriesNames;
        init();
    }

    // ═══════════════════════════════════════════════════════════════════
    // STATIC FACTORY METHODS - Super easy API
    // ═══════════════════════════════════════════════════════════════════

    /** Creates a bar chart. */
    public static JChart bar(String title, String[] labels, double[] values) {
        return new JChart(ChartType.BAR, title, labels, values);
    }

    /** Creates a grouped bar chart with multiple series. */
    public static JChart bar(String title, String[] labels, double[][] multiValues, String[] seriesNames) {
        return new JChart(ChartType.BAR, title, labels, multiValues, seriesNames);
    }

    /** Creates a horizontal bar chart. */
    public static JChart horizontalBar(String title, String[] labels, double[] values) {
        return new JChart(ChartType.HORIZONTAL_BAR, title, labels, values);
    }

    /** Creates a line chart. */
    public static JChart line(String title, String[] labels, double[] values) {
        return new JChart(ChartType.LINE, title, labels, values);
    }

    /** Creates a multi-line chart. */
    public static JChart line(String title, String[] labels, double[][] multiValues, String[] seriesNames) {
        return new JChart(ChartType.LINE, title, labels, multiValues, seriesNames);
    }

    /** Creates a pie chart. */
    public static JChart pie(String title, String[] labels, double[] values) {
        return new JChart(ChartType.PIE, title, labels, values);
    }

    /** Creates a donut chart. */
    public static JChart donut(String title, String[] labels, double[] values) {
        return new JChart(ChartType.DONUT, title, labels, values);
    }

    /** Creates an area chart. */
    public static JChart area(String title, String[] labels, double[] values) {
        return new JChart(ChartType.AREA, title, labels, values);
    }

    /** Creates a multi-area chart. */
    public static JChart area(String title, String[] labels, double[][] multiValues, String[] seriesNames) {
        return new JChart(ChartType.AREA, title, labels, multiValues, seriesNames);
    }

    /** Creates a scatter chart. */
    public static JChart scatter(String title, String[] labels, double[] values) {
        return new JChart(ChartType.SCATTER, title, labels, values);
    }

    /** Creates a scatter chart with multiple series. */
    public static JChart scatter(String title, String[] labels, double[][] multiValues, String[] seriesNames) {
        return new JChart(ChartType.SCATTER, title, labels, multiValues, seriesNames);
    }

    // ═══════════════════════════════════════════════════════════════════
    // BUILDER SETTERS (Fluent API)
    // ═══════════════════════════════════════════════════════════════════

    public JChart withPalette(Color[] palette) {
        this.palette = palette;
        redraw();
        return this;
    }

    public JChart withSize(int width, int height) {
        this.canvasWidth = width;
        this.canvasHeight = height;
        canvas.setWidth(width);
        canvas.setHeight(height);
        redraw();
        return this;
    }

    public JChart withLegend(boolean show) {
        this.showLegend = show;
        rebuildLayout();
        return this;
    }

    public JChart withValues(boolean show) {
        this.showValues = show;
        redraw();
        return this;
    }

    public JChart withGrid(boolean show) {
        this.showGrid = show;
        redraw();
        return this;
    }

    public JChart withAnimation(boolean animated) {
        this.animated = animated;
        return this;
    }

    // ═══════════════════════════════════════════════════════════════════
    // INITIALIZATION
    // ═══════════════════════════════════════════════════════════════════

    private void init() {
        getStyleClass().add("j-chart");
        setSpacing(12);
        setPadding(new Insets(20));

        // Card styling
        setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12px;" +
            "-fx-border-color: #e2e8f0;" +
            "-fx-border-radius: 12px;" +
            "-fx-border-width: 1px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.06), 8, 0, 0, 3);"
        );

        // Canvas
        canvas = new Canvas(canvasWidth, canvasHeight);

        // Canvas Container
        canvasContainer = new StackPane(canvas);
        canvasContainer.setAlignment(Pos.CENTER);

        // Tooltip
        tooltipLabel = new Label();
        tooltipLabel.setStyle(
            "-fx-background-color: rgba(15,23,42,0.9);" +
            "-fx-text-fill: white;" +
            "-fx-padding: 6 12;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;"
        );
        tooltipLabel.setVisible(false);
        tooltipLabel.setManaged(false);

        canvasContainer.getChildren().add(tooltipLabel);

        // Listen for animation
        animProgress.addListener((obs, old, val) -> drawChart());

        rebuildLayout();

        // Animate
        if (animated) {
            playAnimation();
        } else {
            animProgress.set(1.0);
        }
    }

    private void rebuildLayout() {
        getChildren().clear();

        // Title
        if (title != null && !title.isEmpty()) {
            Label titleLabel = new Label(title);
            titleLabel.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: #1e293b;"
            );
            getChildren().add(titleLabel);
        }

        getChildren().add(canvasContainer);

        // Legend
        if (showLegend) {
            getChildren().add(buildLegend());
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // ANIMATION
    // ═══════════════════════════════════════════════════════════════════

    private void playAnimation() {
        animProgress.set(0);
        Timeline tl = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(animProgress, 0)),
            new KeyFrame(Duration.millis(800), new KeyValue(animProgress, 1.0))
        );
        tl.play();
    }

    public void refresh() {
        if (animated) {
            playAnimation();
        } else {
            animProgress.set(1.0);
            drawChart();
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // DATA UPDATE
    // ═══════════════════════════════════════════════════════════════════

    public void setData(String[] labels, double[] values) {
        this.labels = labels;
        this.values = values;
        this.multiValues = null;
        rebuildLayout();
        refresh();
    }

    public void setData(String[] labels, double[][] multiValues, String[] seriesNames) {
        this.labels = labels;
        this.multiValues = multiValues;
        this.seriesNames = seriesNames;
        this.values = null;
        rebuildLayout();
        refresh();
    }

    private void redraw() {
        rebuildLayout();
        drawChart();
    }

    // ═══════════════════════════════════════════════════════════════════
    // DRAWING ENGINE
    // ═══════════════════════════════════════════════════════════════════

    private void drawChart() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double progress = animProgress.get();

        switch (chartType) {
            case BAR -> drawBarChart(gc, progress);
            case HORIZONTAL_BAR -> drawHorizontalBarChart(gc, progress);
            case LINE -> drawLineChart(gc, progress);
            case PIE -> drawPieChart(gc, progress);
            case DONUT -> drawDonutChart(gc, progress);
            case AREA -> drawAreaChart(gc, progress);
            case SCATTER -> drawScatterChart(gc, progress);
        }
    }

    // ─── HELPERS ────────────────────────────────────────────────

    private double[] getEffectiveValues() {
        if (values != null) return values;
        if (multiValues != null && multiValues.length > 0) return multiValues[0];
        return new double[0];
    }

    private double getMaxValue() {
        double max = 0;
        if (multiValues != null) {
            for (double[] series : multiValues) {
                for (double v : series) max = Math.max(max, v);
            }
        } else if (values != null) {
            for (double v : values) max = Math.max(max, v);
        }
        return max == 0 ? 1 : max;
    }

    private double getTotal() {
        double total = 0;
        double[] vals = getEffectiveValues();
        for (double v : vals) total += v;
        return total == 0 ? 1 : total;
    }

    private Color getColor(int index) {
        return palette[index % palette.length];
    }

    private String formatValue(double val) {
        if (val == (long) val) return String.valueOf((long) val);
        return new DecimalFormat("#,##0.#").format(val);
    }

    // ─── GRID & AXES ────────────────────────────────────────────

    private void drawGrid(GraphicsContext gc, double left, double top, double right, double bottom, double maxVal) {
        if (!showGrid) return;

        int gridLines = 5;
        gc.setStroke(Color.web("#f1f5f9"));
        gc.setLineWidth(1);
        gc.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 11));
        gc.setFill(Color.web("#94a3b8"));

        for (int i = 0; i <= gridLines; i++) {
            double y = bottom - (i * (bottom - top) / gridLines);
            gc.strokeLine(left, y, right, y);

            double val = (maxVal / gridLines) * i;
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.fillText(formatValue(val), left - 8, y + 4);
        }
    }

    private void drawXLabels(GraphicsContext gc, double left, double right, double bottom, String[] labs) {
        gc.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 11));
        gc.setFill(Color.web("#64748b"));
        gc.setTextAlign(TextAlignment.CENTER);

        double step = (right - left) / labs.length;
        for (int i = 0; i < labs.length; i++) {
            double x = left + step * i + step / 2;
            gc.fillText(labs[i], x, bottom + 18);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // BAR CHART
    // ═══════════════════════════════════════════════════════════════════

    private void drawBarChart(GraphicsContext gc, double progress) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double left = 60, top = 20, right = w - 20, bottom = h - 40;
        double maxVal = getMaxValue();

        drawGrid(gc, left, top, right, bottom, maxVal);

        if (multiValues != null) {
            // Grouped bars
            int cats = labels.length;
            int series = multiValues.length;
            double catWidth = (right - left) / cats;
            double barWidth = (catWidth * 0.7) / series;
            double gap = catWidth * 0.15;

            for (int s = 0; s < series; s++) {
                Color c = getColor(s);
                for (int i = 0; i < cats; i++) {
                    double val = multiValues[s][i] * progress;
                    double barH = (val / maxVal) * (bottom - top);
                    double x = left + catWidth * i + gap + barWidth * s;
                    double y = bottom - barH;

                    // Gradient fill
                    gc.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, c.brighter()),
                        new Stop(1, c)
                    ));
                    gc.fillRoundRect(x, y, barWidth - 2, barH, 4, 4);

                    // Value label
                    if (showValues && progress > 0.9) {
                        gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
                        gc.setFill(Color.web("#475569"));
                        gc.setTextAlign(TextAlignment.CENTER);
                        gc.fillText(formatValue(multiValues[s][i]), x + barWidth / 2, y - 6);
                    }
                }
            }
        } else {
            // Single series
            double barAreaWidth = (right - left) / labels.length;
            double barWidth = barAreaWidth * 0.6;

            for (int i = 0; i < values.length; i++) {
                Color c = getColor(i);
                double val = values[i] * progress;
                double barH = (val / maxVal) * (bottom - top);
                double x = left + barAreaWidth * i + (barAreaWidth - barWidth) / 2;
                double y = bottom - barH;

                // Gradient fill
                gc.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, c.deriveColor(0, 1, 1.2, 1)),
                    new Stop(1, c)
                ));
                gc.fillRoundRect(x, y, barWidth, barH, 6, 6);

                // Value label
                if (showValues && progress > 0.9) {
                    gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
                    gc.setFill(Color.web("#1e293b"));
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.fillText(formatValue(values[i]), x + barWidth / 2, y - 8);
                }
            }
        }

        drawXLabels(gc, left, right, bottom, labels);
    }

    // ═══════════════════════════════════════════════════════════════════
    // HORIZONTAL BAR CHART
    // ═══════════════════════════════════════════════════════════════════

    private void drawHorizontalBarChart(GraphicsContext gc, double progress) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double left = 100, top = 10, right = w - 40, bottom = h - 20;
        double maxVal = getMaxValue();

        double barAreaHeight = (bottom - top) / labels.length;
        double barHeight = barAreaHeight * 0.6;

        // Grid lines (vertical)
        if (showGrid) {
            int gridLines = 5;
            gc.setStroke(Color.web("#f1f5f9"));
            gc.setLineWidth(1);
            for (int i = 0; i <= gridLines; i++) {
                double x = left + (i * (right - left) / gridLines);
                gc.strokeLine(x, top, x, bottom);
            }
        }

        for (int i = 0; i < values.length; i++) {
            Color c = getColor(i);
            double val = values[i] * progress;
            double barW = (val / maxVal) * (right - left);
            double y = top + barAreaHeight * i + (barAreaHeight - barHeight) / 2;

            // Gradient fill
            gc.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, c),
                new Stop(1, c.deriveColor(0, 1, 1.2, 1))
            ));
            gc.fillRoundRect(left, y, barW, barHeight, 6, 6);

            // Category label
            gc.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
            gc.setFill(Color.web("#475569"));
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.fillText(labels[i], left - 10, y + barHeight / 2 + 4);

            // Value label
            if (showValues && progress > 0.9) {
                gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
                gc.setFill(Color.web("#1e293b"));
                gc.setTextAlign(TextAlignment.LEFT);
                gc.fillText(formatValue(values[i]), left + barW + 8, y + barHeight / 2 + 4);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // LINE CHART
    // ═══════════════════════════════════════════════════════════════════

    private void drawLineChart(GraphicsContext gc, double progress) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double left = 60, top = 20, right = w - 20, bottom = h - 40;
        double maxVal = getMaxValue();

        drawGrid(gc, left, top, right, bottom, maxVal);

        int seriesCount = multiValues != null ? multiValues.length : 1;

        for (int s = 0; s < seriesCount; s++) {
            double[] data = multiValues != null ? multiValues[s] : values;
            Color c = getColor(s);
            int pointsToShow = (int) Math.ceil(data.length * progress);

            // Draw line segments
            gc.setStroke(c);
            gc.setLineWidth(2.5);

            double step = (right - left) / (data.length - 1);

            for (int i = 0; i < pointsToShow - 1; i++) {
                double x1 = left + step * i;
                double y1 = bottom - (data[i] / maxVal) * (bottom - top);
                double x2 = left + step * (i + 1);
                double y2 = bottom - (data[i + 1] / maxVal) * (bottom - top);

                // Partial progress on last segment
                if (i == pointsToShow - 2) {
                    double frac = (data.length * progress) - (pointsToShow - 1);
                    x2 = x1 + (x2 - x1) * frac;
                    y2 = y1 + (y2 - y1) * frac;
                }

                gc.strokeLine(x1, y1, x2, y2);
            }

            // Draw dots
            for (int i = 0; i < pointsToShow; i++) {
                double x = left + step * i;
                double y = bottom - (data[i] / maxVal) * (bottom - top);

                // Outer glow
                gc.setFill(c.deriveColor(0, 1, 1, 0.2));
                gc.fillOval(x - 7, y - 7, 14, 14);
                // White ring
                gc.setFill(Color.WHITE);
                gc.fillOval(x - 5, y - 5, 10, 10);
                // Inner dot
                gc.setFill(c);
                gc.fillOval(x - 3.5, y - 3.5, 7, 7);

                // Value label
                if (showValues && progress > 0.9) {
                    gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
                    gc.setFill(Color.web("#1e293b"));
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.fillText(formatValue(data[i]), x, y - 14);
                }
            }
        }

        drawXLabels(gc, left, right, bottom, labels);
    }

    // ═══════════════════════════════════════════════════════════════════
    // AREA CHART
    // ═══════════════════════════════════════════════════════════════════

    private void drawAreaChart(GraphicsContext gc, double progress) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double left = 60, top = 20, right = w - 20, bottom = h - 40;
        double maxVal = getMaxValue();

        drawGrid(gc, left, top, right, bottom, maxVal);

        int seriesCount = multiValues != null ? multiValues.length : 1;

        for (int s = seriesCount - 1; s >= 0; s--) { // Draw back to front
            double[] data = multiValues != null ? multiValues[s] : values;
            Color c = getColor(s);
            double step = (right - left) / (data.length - 1);

            // Fill area
            gc.beginPath();
            gc.moveTo(left, bottom);
            for (int i = 0; i < data.length; i++) {
                double x = left + step * i;
                double y = bottom - (data[i] * progress / maxVal) * (bottom - top);
                gc.lineTo(x, y);
            }
            gc.lineTo(left + step * (data.length - 1), bottom);
            gc.closePath();

            gc.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, c.deriveColor(0, 1, 1, 0.4)),
                new Stop(1, c.deriveColor(0, 1, 1, 0.05))
            ));
            gc.fill();

            // Line
            gc.setStroke(c);
            gc.setLineWidth(2.5);
            gc.beginPath();
            for (int i = 0; i < data.length; i++) {
                double x = left + step * i;
                double y = bottom - (data[i] * progress / maxVal) * (bottom - top);
                if (i == 0) gc.moveTo(x, y);
                else gc.lineTo(x, y);
            }
            gc.stroke();

            // Dots
            for (int i = 0; i < data.length; i++) {
                double x = left + step * i;
                double y = bottom - (data[i] * progress / maxVal) * (bottom - top);
                gc.setFill(Color.WHITE);
                gc.fillOval(x - 4, y - 4, 8, 8);
                gc.setFill(c);
                gc.fillOval(x - 3, y - 3, 6, 6);
            }
        }

        drawXLabels(gc, left, right, bottom, labels);
    }

    // ═══════════════════════════════════════════════════════════════════
    // PIE CHART
    // ═══════════════════════════════════════════════════════════════════

    private void drawPieChart(GraphicsContext gc, double progress) {
        drawPieOrDonut(gc, progress, false);
    }

    private void drawDonutChart(GraphicsContext gc, double progress) {
        drawPieOrDonut(gc, progress, true);
    }

    private void drawPieOrDonut(GraphicsContext gc, double progress, boolean isDonut) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double centerX = w / 2;
        double centerY = h / 2;
        double radius = Math.min(w, h) / 2 - 30;

        double total = getTotal();
        double[] vals = getEffectiveValues();
        double startAngle = 90;

        for (int i = 0; i < vals.length; i++) {
            double sweep = (vals[i] / total) * 360 * progress;
            Color c = getColor(i);

            gc.setFill(c);
            gc.fillArc(centerX - radius, centerY - radius,
                radius * 2, radius * 2,
                startAngle, -sweep, ArcType.ROUND);

            // Draw percentage label
            if (showValues && progress > 0.9) {
                double midAngle = Math.toRadians(startAngle - sweep / 2);
                double labelR = radius * 0.7;
                double lx = centerX + Math.cos(midAngle) * labelR;
                double ly = centerY - Math.sin(midAngle) * labelR;

                double pct = (vals[i] / total) * 100;
                if (pct > 4) { // Only show label if slice is big enough
                    gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
                    gc.setFill(Color.WHITE);
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.fillText(String.format("%.0f%%", pct), lx, ly + 4);
                }
            }

            startAngle -= sweep;
        }

        // Donut hole
        if (isDonut) {
            double innerRadius = radius * 0.55;
            gc.setFill(Color.WHITE);
            gc.fillOval(centerX - innerRadius, centerY - innerRadius,
                innerRadius * 2, innerRadius * 2);

            // Center total
            if (progress > 0.9) {
                gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
                gc.setFill(Color.web("#1e293b"));
                gc.setTextAlign(TextAlignment.CENTER);
                gc.fillText(formatValue(getTotal()), centerX, centerY - 2);

                gc.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 11));
                gc.setFill(Color.web("#94a3b8"));
                gc.fillText("Total", centerX, centerY + 16);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // SCATTER CHART
    // ═══════════════════════════════════════════════════════════════════

    private void drawScatterChart(GraphicsContext gc, double progress) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double left = 60, top = 20, right = w - 20, bottom = h - 40;
        double maxVal = getMaxValue();

        drawGrid(gc, left, top, right, bottom, maxVal);

        int seriesCount = multiValues != null ? multiValues.length : 1;

        for (int s = 0; s < seriesCount; s++) {
            double[] data = multiValues != null ? multiValues[s] : values;
            Color c = getColor(s);

            double step = (right - left) / (data.length - 1);
            int pointsToShow = (int) Math.ceil(data.length * progress);

            for (int i = 0; i < pointsToShow; i++) {
                double x = left + step * i;
                double y = bottom - (data[i] / maxVal) * (bottom - top);
                double dotSize = 6 + (data[i] / maxVal) * 6; // Size based on value

                // Glow
                gc.setFill(c.deriveColor(0, 1, 1, 0.15));
                gc.fillOval(x - dotSize - 3, y - dotSize - 3, (dotSize + 3) * 2, (dotSize + 3) * 2);
                // Dot
                gc.setFill(c);
                gc.fillOval(x - dotSize, y - dotSize, dotSize * 2, dotSize * 2);
                // White center
                gc.setFill(c.deriveColor(0, 0.5, 1.3, 1));
                gc.fillOval(x - dotSize * 0.4, y - dotSize * 0.4, dotSize * 0.8, dotSize * 0.8);
            }
        }

        drawXLabels(gc, left, right, bottom, labels);
    }

    // ═══════════════════════════════════════════════════════════════════
    // LEGEND
    // ═══════════════════════════════════════════════════════════════════

    private javafx.scene.Node buildLegend() {
        FlowPane legend = new FlowPane(16, 8);
        legend.setAlignment(Pos.CENTER);
        legend.setPadding(new Insets(8, 0, 0, 0));

        String[] legendLabels;
        if (seriesNames != null && multiValues != null) {
            legendLabels = seriesNames;
        } else {
            legendLabels = labels;
        }

        if (legendLabels == null) return legend;

        for (int i = 0; i < legendLabels.length; i++) {
            Color c = getColor(i);

            HBox item = new HBox(6);
            item.setAlignment(Pos.CENTER_LEFT);

            Region dot = new Region();
            dot.setMinSize(10, 10);
            dot.setMaxSize(10, 10);
            dot.setStyle(
                "-fx-background-color: " + toHex(c) + ";" +
                "-fx-background-radius: 3px;"
            );

            Label label = new Label(legendLabels[i]);
            label.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #64748b;" +
                "-fx-font-weight: 500;"
            );

            item.getChildren().addAll(dot, label);
            legend.getChildren().add(item);
        }

        return legend;
    }

    private String toHex(Color c) {
        return String.format("#%02x%02x%02x",
            (int) (c.getRed() * 255),
            (int) (c.getGreen() * 255),
            (int) (c.getBlue() * 255));
    }
}
