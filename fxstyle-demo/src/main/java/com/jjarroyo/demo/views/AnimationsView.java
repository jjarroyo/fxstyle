package com.jjarroyo.demo.views;

import com.jjarroyo.animation.JAnimation;
import com.jjarroyo.components.JAnimatedNumber;
import com.jjarroyo.components.JAnimatedSwitcher;
import com.jjarroyo.components.JAnimatedSwitcher.SwitchAnimation;
import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JFlipCard;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JMarquee;
import com.jjarroyo.components.JRippleContainer;
import com.jjarroyo.components.JSelect;
import com.jjarroyo.demo.util.DemoCodeDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Random;

public class AnimationsView extends ScrollPane {

    private final Random random = new Random();

    public AnimationsView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));

        setFitToWidth(true);
        setContent(content);

        // Page Header
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Animaciones y Transiciones")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Motor de animaciones declarativas (JAnimation), contadores animados (JAnimatedNumber) y transiciones entre vistas (JAnimatedSwitcher)")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);

        content.getChildren().add(pageHeader);

        // Section 1: Declarative Animations (JAnimation)
        JCard card1 = new JCard("Animaciones Declarativas (JAnimation)", createDeclarativeAnimationsSection());
        card1.addToolbarItem(DemoCodeDialog.createCodeButton("JAnimation",
            "// Animaciones de Entrada / Transición:\n" +
            "JAnimation.fadeIn(node);\n" +
            "JAnimation.slideInUp(node);\n" +
            "JAnimation.slideInDown(node);\n" +
            "JAnimation.slideInLeft(node);\n" +
            "JAnimation.slideInRight(node);\n" +
            "JAnimation.zoomIn(node);\n" +
            "JAnimation.popIn(node);\n\n" +
            "// Animaciones de Feedback / Atención:\n" +
            "JAnimation.shake(node);  // Útil para errores en formularios\n" +
            "JAnimation.pulse(node);  // Útil para llamar la atención en botones\n" +
            "JAnimation.bounce(node); // Útil para notificaciones"));
        content.getChildren().add(card1);

        // Section 2: Animated Counters (JAnimatedNumber)
        JCard card2 = new JCard("Contadores Numéricos Animados (JAnimatedNumber)", createAnimatedNumberSection());
        card2.addToolbarItem(DemoCodeDialog.createCodeButton("JAnimatedNumber",
            "// Crear un contador numérico animado:\n" +
            "JAnimatedNumber revenue = new JAnimatedNumber(0, 15420.50);\n" +
            "revenue.setPrefix(\"$\");\n" +
            "revenue.setDecimalPlaces(2);\n" +
            "revenue.setStyle(\"-fx-font-size: 28px; -fx-font-weight: bold;\");\n\n" +
            "// Animar hacia un nuevo valor objetivo:\n" +
            "revenue.animateTo(28750.00);"));
        content.getChildren().add(card2);

        // Section 3: View Switcher Transitions (JAnimatedSwitcher)
        JCard card3 = new JCard("Transición de Vistas en Contenedor (JAnimatedSwitcher)", createAnimatedSwitcherSection());
        card3.addToolbarItem(DemoCodeDialog.createCodeButton("JAnimatedSwitcher",
            "// Crear contenedor de vistas con animación:\n" +
            "JAnimatedSwitcher switcher = new JAnimatedSwitcher();\n" +
            "switcher.setSwitchAnimation(SwitchAnimation.SLIDE_LEFT);\n\n" +
            "// Cambiar contenido con transición suave:\n" +
            "switcher.setContent(newView);"));
        content.getChildren().add(card3);

        // Section 4: 3D Flip Card (JFlipCard)
        JCard card4 = new JCard("Tarjeta 3D Giratoria (JFlipCard)", createFlipCardSection());
        card4.addToolbarItem(DemoCodeDialog.createCodeButton("JFlipCard",
            "// Crear tarjeta 3D con cara frontal y trasera:\n" +
            "JFlipCard card = new JFlipCard(frontView, backView);\n\n" +
            "// Rotar manualmente al hacer clic o mediante código:\n" +
            "card.flip();\n\n" +
            "// O rotar automáticamente al pasar el ratón:\n" +
            "card.setFlipOnHover(true);"));
        content.getChildren().add(card4);

        // Section 5: Ripple Wave Click Effect (JRippleContainer)
        JCard card5 = new JCard("Efecto Onda de Clic (JRippleContainer)", createRippleSection());
        card5.addToolbarItem(DemoCodeDialog.createCodeButton("JRippleContainer",
            "// Envolver cualquier nodo o botón con efecto ripple:\n" +
            "JRippleContainer ripple = new JRippleContainer(myCustomButton);\n" +
            "ripple.setRippleColor(Color.web(\"#3b82f6\", 0.3));"));
        content.getChildren().add(card5);

        // Section 6: Continuous Scrolling Marquee (JMarquee)
        JCard card6 = new JCard("Marquesina de Texto en Movimiento (JMarquee)", createMarqueeSection());
        card6.addToolbarItem(DemoCodeDialog.createCodeButton("JMarquee",
            "// Banner marquesina de movimiento continuo:\n" +
            "JMarquee marquee = new JMarquee(\"🔥 ¡OFERTA ESPECIAL! 50% de descuento en suscripciones anuales con el código FXSTYLE2026\");\n" +
            "marquee.setSpeed(75.0); // Píxeles por segundo\n" +
            "marquee.setPauseOnHover(true);"));
        content.getChildren().add(card6);
    }

    // =========================================================================
    // COPIAR Y PEGAR: Animaciones Declarativas con JAnimation
    // =========================================================================
    private Node createDeclarativeAnimationsSection() {
        VBox container = new VBox(20);

        // Preview Box to Animate
        StackPane targetBox = new StackPane();
        targetBox.setPrefSize(200, 100);
        targetBox.setMaxWidth(300);
        targetBox.setStyle("-fx-background-color: #3b82f6; -fx-background-radius: 12px; -fx-effect: dropshadow(three-pass-box, rgba(59, 130, 246, 0.3), 10, 0, 0, 4);");

        JLabel targetLabel = new JLabel("¡Objeto de Prueba!")
            .withStyle("text-white", "font-bold", "text-lg");
        targetBox.getChildren().add(targetLabel);

        // Control Buttons
        FlowPane buttonGrid = new FlowPane();
        buttonGrid.setHgap(12);
        buttonGrid.setVgap(12);

        JButton btnFadeIn = new JButton("Fade In");
        btnFadeIn.addClass("btn-secondary");
        btnFadeIn.setOnAction(e -> JAnimation.fadeIn(targetBox));

        JButton btnSlideUp = new JButton("Slide Up");
        btnSlideUp.addClass("btn-secondary");
        btnSlideUp.setOnAction(e -> JAnimation.slideInUp(targetBox));

        JButton btnSlideDown = new JButton("Slide Down");
        btnSlideDown.addClass("btn-secondary");
        btnSlideDown.setOnAction(e -> JAnimation.slideInDown(targetBox));

        JButton btnSlideLeft = new JButton("Slide Left");
        btnSlideLeft.addClass("btn-secondary");
        btnSlideLeft.setOnAction(e -> JAnimation.slideInLeft(targetBox));

        JButton btnSlideRight = new JButton("Slide Right");
        btnSlideRight.addClass("btn-secondary");
        btnSlideRight.setOnAction(e -> JAnimation.slideInRight(targetBox));

        JButton btnZoomIn = new JButton("Zoom In");
        btnZoomIn.addClass("btn-secondary");
        btnZoomIn.setOnAction(e -> JAnimation.zoomIn(targetBox));

        JButton btnPopIn = new JButton("Pop In");
        btnPopIn.addClass("btn-primary");
        btnPopIn.setOnAction(e -> JAnimation.popIn(targetBox));

        JButton btnShake = new JButton("Shake (Error)");
        btnShake.addClass("btn-danger");
        btnShake.setOnAction(e -> JAnimation.shake(targetBox));

        JButton btnPulse = new JButton("Pulse (Atención)");
        btnPulse.addClass("btn-warning");
        btnPulse.setOnAction(e -> JAnimation.pulse(targetBox));

        JButton btnBounce = new JButton("Bounce (Salto)");
        btnBounce.addClass("btn-success");
        btnBounce.setOnAction(e -> JAnimation.bounce(targetBox));

        buttonGrid.getChildren().addAll(btnFadeIn, btnSlideUp, btnSlideDown, btnSlideLeft, btnSlideRight, btnZoomIn, btnPopIn, btnShake, btnPulse, btnBounce);

        container.getChildren().addAll(targetBox, buttonGrid);
        return container;
    }

    // =========================================================================
    // COPIAR Y PEGAR: Contadores Numéricos Animados (JAnimatedNumber)
    // =========================================================================
    private Node createAnimatedNumberSection() {
        VBox container = new VBox(20);

        HBox cardsBox = new HBox(20);

        // KPI Card 1: Revenue
        VBox kpi1 = createKpiBox("Ingresos Mensuales");
        JAnimatedNumber numRevenue = new JAnimatedNumber(0, 15420.50);
        numRevenue.setPrefix("$ ");
        numRevenue.setDecimalPlaces(2);
        numRevenue.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
        kpi1.getChildren().add(numRevenue);

        // KPI Card 2: Growth Rate
        VBox kpi2 = createKpiBox("Tasa de Conversión");
        JAnimatedNumber numConversion = new JAnimatedNumber(0, 94.8);
        numConversion.setSuffix("%");
        numConversion.setDecimalPlaces(1);
        numConversion.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #16a34a;");
        kpi2.getChildren().add(numConversion);

        // KPI Card 3: Total Users
        VBox kpi3 = createKpiBox("Usuarios Activos");
        JAnimatedNumber numUsers = new JAnimatedNumber(0, 12850);
        numUsers.setSuffix(" usuarios");
        numUsers.setDecimalPlaces(0);
        numUsers.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");
        kpi3.getChildren().add(numUsers);

        cardsBox.getChildren().addAll(kpi1, kpi2, kpi3);

        // Refresh Button
        JButton btnRefresh = new JButton("Simular Nuevos Datos (Animar)");
        btnRefresh.addClass("btn-primary");
        btnRefresh.setOnAction(e -> {
            numRevenue.animateTo(10000 + random.nextDouble() * 25000);
            numConversion.animateTo(70 + random.nextDouble() * 28);
            numUsers.animateTo(5000 + random.nextInt(20000));
        });

        container.getChildren().addAll(cardsBox, btnRefresh);
        return container;
    }

    private VBox createKpiBox(String title) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(16));
        box.setPrefWidth(220);
        box.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e2e8f0; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        JLabel lblTitle = new JLabel(title).withStyle("text-xs", "font-bold", "text-slate-400");
        box.getChildren().add(lblTitle);
        return box;
    }

    // =========================================================================
    // COPIAR Y PEGAR: Transiciones en Contenedor (JAnimatedSwitcher)
    // =========================================================================
    private Node createAnimatedSwitcherSection() {
        VBox container = new VBox(16);

        // Controls
        HBox topControls = new HBox(16);
        topControls.setAlignment(Pos.CENTER_LEFT);

        JLabel selectLabel = new JLabel("Tipo de Transición:").withStyle("font-semibold", "text-slate-700");
        JSelect<SwitchAnimation> selectAnim = new JSelect<>();
        selectAnim.getItems().addAll(SwitchAnimation.values());
        selectAnim.setSelectedItem(SwitchAnimation.SLIDE_LEFT);

        topControls.getChildren().addAll(selectLabel, selectAnim);

        // Animated Switcher Instance
        JAnimatedSwitcher switcher = new JAnimatedSwitcher();
        switcher.setPrefHeight(160);
        switcher.setStyle("-fx-background-color: #f8fafc; -fx-border-color: #cbd5e1; -fx-border-radius: 12px; -fx-background-radius: 12px;");

        // Sample Step Nodes
        Node step1 = createStepCard("Paso 1: Información Personal", "Ingresa tu nombre y correo para continuar", "#3b82f6");
        Node step2 = createStepCard("Paso 2: Método de Pago", "Selecciona tu tarjeta o PayPal", "#8b5cf6");
        Node step3 = createStepCard("Paso 3: Confirmación Final", "¡Tu pedido está listo para ser procesado!", "#10b981");

        switcher.setContent(step1);

        selectAnim.selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                switcher.setSwitchAnimation(newVal);
            }
        });

        // Navigation Buttons
        HBox navButtons = new HBox(12);
        JButton btnStep1 = new JButton("Paso 1");
        btnStep1.addClass("btn-secondary");
        btnStep1.setOnAction(e -> switcher.setContent(step1));

        JButton btnStep2 = new JButton("Paso 2");
        btnStep2.addClass("btn-secondary");
        btnStep2.setOnAction(e -> switcher.setContent(step2));

        JButton btnStep3 = new JButton("Paso 3");
        btnStep3.addClass("btn-secondary");
        btnStep3.setOnAction(e -> switcher.setContent(step3));

        navButtons.getChildren().addAll(btnStep1, btnStep2, btnStep3);

        container.getChildren().addAll(topControls, switcher, navButtons);
        return container;
    }

    private Node createStepCard(String titleText, String subtitleText, String colorHex) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(24));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 10px;", colorHex));

        JLabel title = new JLabel(titleText).withStyle("text-white", "font-bold", "text-lg");
        JLabel subtitle = new JLabel(subtitleText).withStyle("text-white", "text-sm");

        card.getChildren().addAll(title, subtitle);
        return card;
    }

    // =========================================================================
    // COPIAR Y PEGAR: Tarjetas 3D Giratorias (JFlipCard)
    // =========================================================================
    private Node createFlipCardSection() {
        HBox container = new HBox(24);
        container.setAlignment(Pos.CENTER_LEFT);

        // Card 1: Click to Flip Credit Card
        VBox frontCredit = createCreditCardFace("Tarjeta Gold FX", "**** **** **** 4892", "EXPIRES: 12/28", "#1e293b");
        VBox backCredit = createCreditCardBack("SECURITY CODE", "CVV: 742", "FIRMA AUTORIZADA", "#0f172a");

        JFlipCard flipCredit = new JFlipCard(frontCredit, backCredit);
        flipCredit.setPrefSize(300, 180);

        // Card 2: Hover to Flip Product Card
        VBox frontProduct = createProductCardFace("FxStyle Pro License", "$49.99 / año", "Hover para ver detalles", "#2563eb");
        VBox backProduct = createProductCardBack("Incluye:", "• 45+ Componentes JavaFX\n• Soporte Dark Mode\n• Licencia Ilimitada", "#1d4ed8");

        JFlipCard flipProduct = new JFlipCard(frontProduct, backProduct);
        flipProduct.setPrefSize(300, 180);
        flipProduct.setFlipOnHover(true);

        container.getChildren().addAll(flipCredit, flipProduct);
        return container;
    }

    private VBox createCreditCardFace(String titleText, String numberText, String expText, String bgHex) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));
        box.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 14px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 4);", bgHex));
        JLabel title = new JLabel(titleText).withStyle("text-amber-400", "font-bold", "text-sm");
        JLabel num = new JLabel(numberText).withStyle("text-white", "font-mono", "text-lg", "font-bold");
        JLabel exp = new JLabel(expText).withStyle("text-slate-400", "text-xs");
        box.getChildren().addAll(title, num, exp);
        return box;
    }

    private VBox createCreditCardBack(String labelText, String cvvText, String signText, String bgHex) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(16));
        box.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 14px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 4);", bgHex));
        StackPane magneticStrip = new StackPane();
        magneticStrip.setPrefSize(280, 24);
        magneticStrip.setStyle("-fx-background-color: #000000;");

        JLabel sign = new JLabel(signText).withStyle("text-slate-400", "text-xs");
        JLabel cvv = new JLabel(cvvText).withStyle("text-emerald-400", "font-mono", "font-bold", "text-sm");
        box.getChildren().addAll(magneticStrip, sign, cvv);
        return box;
    }

    private VBox createProductCardFace(String name, String price, String hint, String bgHex) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);
        box.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 14px;", bgHex));
        JLabel lblName = new JLabel(name).withStyle("text-white", "font-bold", "text-lg");
        JLabel lblPrice = new JLabel(price).withStyle("text-emerald-300", "font-bold", "text-xl");
        JLabel lblHint = new JLabel(hint).withStyle("text-blue-200", "text-xs");
        box.getChildren().addAll(lblName, lblPrice, lblHint);
        return box;
    }

    private VBox createProductCardBack(String title, String details, String bgHex) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 14px;", bgHex));
        JLabel lblTitle = new JLabel(title).withStyle("text-amber-300", "font-bold", "text-sm");
        JLabel lblDetails = new JLabel(details).withStyle("text-white", "text-xs");
        box.getChildren().addAll(lblTitle, lblDetails);
        return box;
    }

    // =========================================================================
    // COPIAR Y PEGAR: Efecto Ripple al Clic (JRippleContainer)
    // =========================================================================
    private Node createRippleSection() {
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER_LEFT);

        // Ripple Button 1 (Blue Wave)
        JButton btn1 = new JButton("¡Haz Clic Aquí (Ripple Azul)!");
        btn1.addClass("btn-primary");
        JRippleContainer ripple1 = new JRippleContainer(btn1);
        ripple1.setRippleColor(Color.web("#3b82f6", 0.35));

        // Ripple Button 2 (Green Wave)
        JButton btn2 = new JButton("¡Haz Clic Aquí (Ripple Verde)!");
        btn2.addClass("btn-success");
        JRippleContainer ripple2 = new JRippleContainer(btn2);
        ripple2.setRippleColor(Color.web("#10b981", 0.35));

        // Ripple Card
        StackPane customCard = new StackPane(new JLabel("Haz Clic en Cualquier Parte de Esta Tarjeta").withStyle("text-slate-700", "font-semibold"));
        customCard.setPadding(new Insets(20));
        customCard.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cbd5e1; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        JRippleContainer rippleCard = new JRippleContainer(customCard);
        rippleCard.setRippleColor(Color.web("#6366f1", 0.25));

        container.getChildren().addAll(ripple1, ripple2, rippleCard);
        return container;
    }

    // =========================================================================
    // COPIAR Y PEGAR: Marquesina Animada (JMarquee)
    // =========================================================================
    private Node createMarqueeSection() {
        VBox container = new VBox(16);

        // Banner 1: Standard Speed News Ticker
        JMarquee marquee1 = new JMarquee("🚀 [NUEVA VERSIÓN] FxStyle v2.2.0 disponible con soporte para animaciones integradas, contadores numéricos y nuevos componentes UI.");
        marquee1.setPrefHeight(40);
        marquee1.setStyle("-fx-background-color: #0f172a; -fx-background-radius: 8px; -fx-padding: 8px 16px;");
        marquee1.getLabel().setStyle("-fx-text-fill: #38bdf8; -fx-font-weight: bold; -fx-font-size: 14px;");
        marquee1.setSpeed(70.0);

        // Banner 2: Fast Warning Banner
        JMarquee marquee2 = new JMarquee("⚠️ MANTENIMIENTO PROGRAMADO: El servidor estará fuera de servicio el Domingo 12 de Septiembre de 02:00 a 04:00 AM UTC.");
        marquee2.setPrefHeight(40);
        marquee2.setStyle("-fx-background-color: #fef2f2; -fx-border-color: #fca5a5; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 8px 16px;");
        marquee2.getLabel().setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-font-size: 13px;");
        marquee2.setSpeed(90.0);

        container.getChildren().addAll(marquee1, marquee2);
        return container;
    }
}
