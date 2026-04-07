package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JChart;
import com.jjarroyo.components.JLabel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChartView extends ScrollPane {

    public ChartView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));

        setFitToWidth(true);
        setContent(content);

        // Page Header
        VBox pageHeader = new VBox(4);
        JLabel title = new JLabel("Charts")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Gráficos animados y profesionales para dashboards y reportes")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        // ═══════════════════════════════════════════════════════════════
        // 1. BAR CHARTS
        // ═══════════════════════════════════════════════════════════════
        content.getChildren().add(sectionTitle("📊 Gráficos de Barras"));

        // 1.1 Simple Bar
        content.getChildren().add(new JCard("Barras Simples", createSimpleBar()));

        // 1.2 Colorful Sales Bar
        content.getChildren().add(new JCard("Ventas por Producto", createSalesBar()));

        // 1.3 Grouped Bar (Multi-Series)
        content.getChildren().add(new JCard("Barras Agrupadas - Ingresos vs Gastos", createGroupedBar()));

        // 1.4 Many categories bar
        content.getChildren().add(new JCard("Barras - Temperatura por Mes", createMonthlyBar()));

        // ═══════════════════════════════════════════════════════════════
        // 2. HORIZONTAL BAR CHARTS  
        // ═══════════════════════════════════════════════════════════════
        content.getChildren().add(sectionTitle("📏 Barras Horizontales"));

        // 2.1 Simple Horizontal
        content.getChildren().add(new JCard("Lenguajes de Programación - Popularidad", createHorizontalBar()));

        // 2.2 Countries
        content.getChildren().add(new JCard("Top Países por Cosecha (toneladas)", createCountriesHBar()));

        // ═══════════════════════════════════════════════════════════════
        // 3. LINE CHARTS
        // ═══════════════════════════════════════════════════════════════
        content.getChildren().add(sectionTitle("📈 Gráficos de Línea"));

        // 3.1 Simple line
        content.getChildren().add(new JCard("Tráfico Web - Últimos 7 Días", createSimpleLine()));

        // 3.2 Multi-line
        content.getChildren().add(new JCard("Comparativa de Ventas - 3 Productos", createMultiLine()));

        // 3.3 Stock price line
        content.getChildren().add(new JCard("Precio de Acción (USD) - 12 Meses", createStockLine()));

        // ═══════════════════════════════════════════════════════════════
        // 4. AREA CHARTS
        // ═══════════════════════════════════════════════════════════════
        content.getChildren().add(sectionTitle("🏔️ Gráficos de Área"));

        // 4.1 Simple area
        content.getChildren().add(new JCard("Uso de CPU (%)", createSimpleArea()));

        // 4.2 Multi-area
        content.getChildren().add(new JCard("Tráfico por Canal", createMultiArea()));

        // 4.3 Revenue area
        content.getChildren().add(new JCard("Ingresos Acumulados ($)", createRevenueArea()));

        // ═══════════════════════════════════════════════════════════════
        // 5. PIE CHARTS
        // ═══════════════════════════════════════════════════════════════
        content.getChildren().add(sectionTitle("🥧 Gráficos de Pastel"));

        // 5.1 Simple pie
        FlowPane pieRow = new FlowPane(24, 24);
        pieRow.getChildren().addAll(
            wrapInCard("Distribución de Presupuesto", createSimplePie()),
            wrapInCard("Cuota de Mercado", createMarketPie())
        );
        content.getChildren().add(pieRow);

        // 5.2 Another pie
        content.getChildren().add(new JCard("Ventas por Región", createRegionPie()));

        // ═══════════════════════════════════════════════════════════════
        // 6. DONUT CHARTS
        // ═══════════════════════════════════════════════════════════════
        content.getChildren().add(sectionTitle("🍩 Gráficos de Dona"));

        FlowPane donutRow = new FlowPane(24, 24);
        donutRow.getChildren().addAll(
            wrapInCard("Estado de Tareas", createTaskDonut()),
            wrapInCard("Uso de Almacenamiento", createStorageDonut())
        );
        content.getChildren().add(donutRow);

        // 6.3 Large donut
        content.getChildren().add(new JCard("Composición de Portafolio", createPortfolioDonut()));

        // ═══════════════════════════════════════════════════════════════
        // 7. SCATTER CHARTS
        // ═══════════════════════════════════════════════════════════════
        content.getChildren().add(sectionTitle("✨ Gráficos de Dispersión"));

        content.getChildren().add(new JCard("Puntos de venta por zona", createSimpleScatter()));
        content.getChildren().add(new JCard("Correlación Multi-Serie", createMultiScatter()));

        // ═══════════════════════════════════════════════════════════════
        // 8. PALETTES
        // ═══════════════════════════════════════════════════════════════
        content.getChildren().add(sectionTitle("🎨 Paletas de Colores"));

        content.getChildren().add(new JCard("Paleta Ocean", createOceanPalette()));
        content.getChildren().add(new JCard("Paleta Sunset", createSunsetPalette()));
        content.getChildren().add(new JCard("Paleta Forest", createForestPalette()));
        content.getChildren().add(new JCard("Paleta Slate", createSlatePalette()));

        // ═══════════════════════════════════════════════════════════════
        // 9. SIZES
        // ═══════════════════════════════════════════════════════════════
        content.getChildren().add(sectionTitle("📐 Tamaños Personalizados"));

        FlowPane sizeRow = new FlowPane(24, 24);
        sizeRow.getChildren().addAll(
            wrapInCard("Pequeño (250x180)", createSmallChart()),
            wrapInCard("Compacto (300x200)", createCompactChart())
        );
        content.getChildren().add(sizeRow);

        content.getChildren().add(new JCard("Grande (700x350)", createLargeChart()));

        // ═══════════════════════════════════════════════════════════════
        // 10. DASHBOARD COMBO
        // ═══════════════════════════════════════════════════════════════
        content.getChildren().add(sectionTitle("🎯 Combo Dashboard"));

        content.getChildren().add(new JCard("Dashboard Agrícola Completo", createDashboardCombo()));
    }

    // ─────────────────────────────────────────────────────────────────
    // SECTION HELPERS
    // ─────────────────────────────────────────────────────────────────

    private VBox sectionTitle(String text) {
        VBox box = new VBox();
        box.setPadding(new Insets(16, 0, 0, 0));
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: 800; -fx-text-fill: #0f172a;");
        box.getChildren().add(label);
        return box;
    }

    private JCard wrapInCard(String title, javafx.scene.Node content) {
        return new JCard(title, content);
    }

    // ═══════════════════════════════════════════════════════════════════
    // 1. BAR CHART DEMOS
    // ═══════════════════════════════════════════════════════════════════

    private javafx.scene.Node createSimpleBar() {
        VBox container = new VBox(12);
        Label desc = new Label("Gráfico de barras simple con una sola línea de código.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.bar("Ventas Mensuales",
            new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun"},
            new double[]{120, 200, 150, 80, 250, 180});

        container.getChildren().addAll(desc, chart);
        return container;
    }

    private javafx.scene.Node createSalesBar() {
        VBox container = new VBox(12);
        Label desc = new Label("Barras con colores automáticos para cada categoría.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.bar("Ventas por Producto ($)",
            new String[]{"Laptop", "Monitor", "Teclado", "Mouse", "Auriculares"},
            new double[]{4500, 2800, 650, 320, 1200});
        chart.withSize(550, 320);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    private javafx.scene.Node createGroupedBar() {
        VBox container = new VBox(12);
        Label desc = new Label("Barras agrupadas para comparar múltiples series de datos.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.bar("Ingresos vs Gastos ($K)",
            new String[]{"Q1", "Q2", "Q3", "Q4"},
            new double[][]{
                {120, 180, 220, 160},  // Ingresos
                {90, 140, 180, 130},   // Gastos
                {30, 40, 40, 30}       // Utilidad
            },
            new String[]{"Ingresos", "Gastos", "Utilidad"});

        container.getChildren().addAll(desc, chart);
        return container;
    }

    private javafx.scene.Node createMonthlyBar() {
        VBox container = new VBox(12);
        Label desc = new Label("12 categorías para datos mensuales.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.bar("Temperatura Promedio (°C)",
            new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"},
            new double[]{12, 14, 18, 22, 27, 32, 35, 34, 29, 23, 17, 13});
        chart.withSize(600, 300);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    // ═══════════════════════════════════════════════════════════════════
    // 2. HORIZONTAL BAR DEMOS
    // ═══════════════════════════════════════════════════════════════════

    private javafx.scene.Node createHorizontalBar() {
        VBox container = new VBox(12);
        Label desc = new Label("Barras horizontales, ideales para categorías con nombres largos.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.horizontalBar("Popularidad de Lenguajes (%)",
            new String[]{"Python", "JavaScript", "Java", "C#", "Go", "Rust"},
            new double[]{92, 88, 75, 63, 48, 42});
        chart.withSize(500, 280);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    private javafx.scene.Node createCountriesHBar() {
        VBox container = new VBox(12);
        Label desc = new Label("Rankings con barras horizontales.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.horizontalBar("Producción Agrícola (K toneladas)",
            new String[]{"Brasil", "EE.UU.", "India", "China", "Argentina"},
            new double[]{350, 290, 240, 210, 180});
        chart.withPalette(JChart.PALETTE_FOREST);
        chart.withSize(500, 260);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    // ═══════════════════════════════════════════════════════════════════
    // 3. LINE CHART DEMOS
    // ═══════════════════════════════════════════════════════════════════

    private javafx.scene.Node createSimpleLine() {
        VBox container = new VBox(12);
        Label desc = new Label("Línea con puntos decorativos y valores.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.line("Visitas Diarias (K)",
            new String[]{"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"},
            new double[]{12, 19, 15, 25, 22, 30, 28});

        container.getChildren().addAll(desc, chart);
        return container;
    }

    private javafx.scene.Node createMultiLine() {
        VBox container = new VBox(12);
        Label desc = new Label("Múltiples líneas para comparar tendencias de diferentes productos.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.line("Comparativa de Ventas (unidades)",
            new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun"},
            new double[][]{
                {30, 45, 35, 50, 60, 55},   // Producto A
                {20, 30, 40, 35, 45, 50},   // Producto B
                {10, 15, 25, 20, 30, 40}    // Producto C
            },
            new String[]{"Producto A", "Producto B", "Producto C"});
        chart.withSize(550, 300);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    private javafx.scene.Node createStockLine() {
        VBox container = new VBox(12);
        Label desc = new Label("Evolución de precio con paleta oceánica.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.line("Precio USD",
            new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"},
            new double[]{142, 148, 155, 149, 160, 172, 168, 175, 182, 178, 190, 195});
        chart.withPalette(JChart.PALETTE_OCEAN);
        chart.withSize(600, 280);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    // ═══════════════════════════════════════════════════════════════════
    // 4. AREA CHART DEMOS
    // ═══════════════════════════════════════════════════════════════════

    private javafx.scene.Node createSimpleArea() {
        VBox container = new VBox(12);
        Label desc = new Label("Área con degradado semi-transparente bajo la línea.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.area("Uso de CPU (%)",
            new String[]{"10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00"},
            new double[]{25, 42, 38, 65, 55, 72, 48});
        chart.withPalette(new javafx.scene.paint.Color[]{javafx.scene.paint.Color.web("#8b5cf6")});

        container.getChildren().addAll(desc, chart);
        return container;
    }

    private javafx.scene.Node createMultiArea() {
        VBox container = new VBox(12);
        Label desc = new Label("Áreas superpuestas con transparencia para comparar canales.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.area("Visitas por Canal",
            new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun"},
            new double[][]{
                {500, 700, 600, 850, 900, 1100},   // Orgánico
                {300, 400, 350, 500, 450, 600},    // Social
                {100, 150, 200, 250, 300, 350}     // Email
            },
            new String[]{"Orgánico", "Social Media", "Email"});
        chart.withSize(550, 300);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    private javafx.scene.Node createRevenueArea() {
        VBox container = new VBox(12);
        Label desc = new Label("Ingresos acumulados con paleta sunset.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.area("Ingresos Acumulados ($K)",
            new String[]{"Q1", "Q2", "Q3", "Q4"},
            new double[]{85, 195, 340, 520});
        chart.withPalette(JChart.PALETTE_SUNSET);
        chart.withSize(500, 260);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    // ═══════════════════════════════════════════════════════════════════
    // 5. PIE CHART DEMOS
    // ═══════════════════════════════════════════════════════════════════

    private javafx.scene.Node createSimplePie() {
        VBox container = new VBox(12);

        JChart chart = JChart.pie("Presupuesto",
            new String[]{"Marketing", "Dev", "Operaciones", "RRHH", "Otros"},
            new double[]{35, 25, 20, 12, 8});
        chart.withSize(300, 250);

        container.getChildren().add(chart);
        return container;
    }

    private javafx.scene.Node createMarketPie() {
        VBox container = new VBox(12);

        JChart chart = JChart.pie("Cuota de Mercado",
            new String[]{"Chrome", "Safari", "Firefox", "Edge", "Otros"},
            new double[]{65, 18, 8, 5, 4});
        chart.withSize(300, 250);
        chart.withPalette(JChart.PALETTE_OCEAN);

        container.getChildren().add(chart);
        return container;
    }

    private javafx.scene.Node createRegionPie() {
        VBox container = new VBox(12);
        Label desc = new Label("Distribución de ventas por zona geográfica.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.pie("Ventas por Región",
            new String[]{"Norteamérica", "Europa", "Asia", "Latam", "África", "Oceanía"},
            new double[]{40, 25, 18, 10, 4, 3});
        chart.withSize(400, 300);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    // ═══════════════════════════════════════════════════════════════════
    // 6. DONUT CHART DEMOS
    // ═══════════════════════════════════════════════════════════════════

    private javafx.scene.Node createTaskDonut() {
        VBox container = new VBox(12);

        JChart chart = JChart.donut("Estado de Tareas",
            new String[]{"Completadas", "En Progreso", "Pendientes", "Canceladas"},
            new double[]{45, 25, 20, 10});
        chart.withSize(300, 250);

        container.getChildren().add(chart);
        return container;
    }

    private javafx.scene.Node createStorageDonut() {
        VBox container = new VBox(12);

        JChart chart = JChart.donut("Uso de Almacenamiento",
            new String[]{"Documentos", "Imágenes", "Videos", "Libre"},
            new double[]{35, 25, 15, 25});
        chart.withSize(300, 250);
        chart.withPalette(JChart.PALETTE_SUNSET);

        container.getChildren().add(chart);
        return container;
    }

    private javafx.scene.Node createPortfolioDonut() {
        VBox container = new VBox(12);
        Label desc = new Label("Dona grande con centro informativo mostrando el total.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.donut("Composición de Portafolio ($)",
            new String[]{"Acciones", "Bonos", "Bienes Raíces", "Cripto", "Efectivo"},
            new double[]{45000, 25000, 18000, 8000, 4000});
        chart.withSize(450, 320);
        chart.withPalette(JChart.PALETTE_OCEAN);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    // ═══════════════════════════════════════════════════════════════════
    // 7. SCATTER CHART DEMOS
    // ═══════════════════════════════════════════════════════════════════

    private javafx.scene.Node createSimpleScatter() {
        VBox container = new VBox(12);
        Label desc = new Label("Puntos de dispersión con tamaño proporcional al valor.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.scatter("Puntos de Venta",
            new String[]{"Zona A", "Zona B", "Zona C", "Zona D", "Zona E", "Zona F", "Zona G", "Zona H"},
            new double[]{45, 82, 60, 30, 95, 55, 70, 40});

        container.getChildren().addAll(desc, chart);
        return container;
    }

    private javafx.scene.Node createMultiScatter() {
        VBox container = new VBox(12);
        Label desc = new Label("Dispersión con múltiples series para correlación de datos.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.scatter("Correlación Rendimiento",
            new String[]{"P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8"},
            new double[][]{
                {30, 50, 70, 45, 85, 60, 90, 40},  // Grupo A
                {60, 35, 55, 80, 40, 70, 50, 75}   // Grupo B
            },
            new String[]{"Grupo A", "Grupo B"});
        chart.withSize(550, 300);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    // ═══════════════════════════════════════════════════════════════════
    // 8. PALETTE DEMOS
    // ═══════════════════════════════════════════════════════════════════

    private javafx.scene.Node createOceanPalette() {
        VBox container = new VBox(12);
        Label desc = new Label("Paleta de tonos azules y cian para reportes corporativos.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.bar("Ventas con Paleta Oceánica",
            new String[]{"Web", "Mobile", "Desktop", "API", "IoT"},
            new double[]{420, 380, 290, 200, 150});
        chart.withPalette(JChart.PALETTE_OCEAN);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    private javafx.scene.Node createSunsetPalette() {
        VBox container = new VBox(12);
        Label desc = new Label("Paleta cálida con naranjas, rojos y rosas.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.donut("Distribución con Sunset",
            new String[]{"Salud", "Educación", "Transporte", "Energía", "Tech"},
            new double[]{30, 25, 20, 15, 10});
        chart.withPalette(JChart.PALETTE_SUNSET);
        chart.withSize(400, 280);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    private javafx.scene.Node createForestPalette() {
        VBox container = new VBox(12);
        Label desc = new Label("Paleta de verdes ideal para datos ambientales o agrícolas.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.line("Cultivos con Paleta Forest",
            new String[]{"Sem 1", "Sem 2", "Sem 3", "Sem 4", "Sem 5", "Sem 6"},
            new double[][]{
                {10, 25, 45, 60, 72, 85},  // Maíz
                {5, 15, 30, 50, 65, 78}    // Soya
            },
            new String[]{"Maíz", "Soya"});
        chart.withPalette(JChart.PALETTE_FOREST);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    private javafx.scene.Node createSlatePalette() {
        VBox container = new VBox(12);
        Label desc = new Label("Paleta monocromática elegante y profesional.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.pie("Distribución con Slate",
            new String[]{"Tipo A", "Tipo B", "Tipo C", "Tipo D"},
            new double[]{40, 30, 20, 10});
        chart.withPalette(JChart.PALETTE_SLATE);
        chart.withSize(350, 280);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    // ═══════════════════════════════════════════════════════════════════
    // 9. SIZE DEMOS
    // ═══════════════════════════════════════════════════════════════════

    private javafx.scene.Node createSmallChart() {
        VBox container = new VBox(8);

        JChart chart = JChart.pie("Mini Pie",
            new String[]{"A", "B", "C"},
            new double[]{50, 30, 20});
        chart.withSize(250, 180);
        chart.withValues(false);

        container.getChildren().add(chart);
        return container;
    }

    private javafx.scene.Node createCompactChart() {
        VBox container = new VBox(8);

        JChart chart = JChart.bar("Compacto",
            new String[]{"A", "B", "C", "D"},
            new double[]{40, 70, 55, 90});
        chart.withSize(300, 200);

        container.getChildren().add(chart);
        return container;
    }

    private javafx.scene.Node createLargeChart() {
        VBox container = new VBox(12);
        Label desc = new Label("Gráfico grande de 700x350px para pantallas amplias.");
        desc.getStyleClass().add("text-slate-500");

        JChart chart = JChart.area("Rendimiento Anual Detallado (%)",
            new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"},
            new double[][]{
                {12, 18, 25, 22, 30, 35, 42, 38, 44, 40, 48, 55},
                {8, 12, 15, 18, 22, 28, 32, 35, 38, 42, 45, 50}
            },
            new String[]{"Producto Premium", "Producto Standard"});
        chart.withSize(700, 350);

        container.getChildren().addAll(desc, chart);
        return container;
    }

    // ═══════════════════════════════════════════════════════════════════
    // 10. DASHBOARD COMBO
    // ═══════════════════════════════════════════════════════════════════

    private javafx.scene.Node createDashboardCombo() {
        VBox container = new VBox(20);
        Label desc = new Label("Combinación de múltiples gráficos en un layout tipo dashboard agrícola.");
        desc.getStyleClass().add("text-slate-500");
        container.getChildren().add(desc);

        // Row 1: Donut + Bars
        HBox row1 = new HBox(20);

        JChart cropDonut = JChart.donut("Distribución de Cultivos",
            new String[]{"Café", "Caña", "Maíz", "Aguacate", "Otros"},
            new double[]{35, 25, 20, 12, 8});
        cropDonut.withSize(280, 240);
        cropDonut.withPalette(JChart.PALETTE_FOREST);

        JChart harvestBars = JChart.bar("Cosecha por Trimestre (ton)",
            new String[]{"Q1", "Q2", "Q3", "Q4"},
            new double[][]{
                {220, 340, 280, 150},  // 2024
                {190, 310, 320, 180}   // 2025
            },
            new String[]{"2024", "2025"});
        harvestBars.withSize(380, 240);

        row1.getChildren().addAll(cropDonut, harvestBars);
        container.getChildren().add(row1);

        // Row 2: Line + Area
        HBox row2 = new HBox(20);

        JChart rainLine = JChart.line("Precipitación (mm)",
            new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun"},
            new double[]{120, 80, 60, 140, 200, 180});
        rainLine.withSize(340, 220);
        rainLine.withPalette(JChart.PALETTE_OCEAN);

        JChart growthArea = JChart.area("Crecimiento del Cultivo (cm)",
            new String[]{"Sem1", "Sem2", "Sem3", "Sem4", "Sem5", "Sem6"},
            new double[]{5, 18, 35, 55, 72, 88});
        growthArea.withSize(340, 220);
        growthArea.withPalette(JChart.PALETTE_FOREST);

        row2.getChildren().addAll(rainLine, growthArea);
        container.getChildren().add(row2);

        return container;
    }
}
