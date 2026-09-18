package com.jjarroyo.demo.views;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JRibbon;
import com.jjarroyo.components.JRibbon.Position;
import com.jjarroyo.components.JRibbon.Variant;
import com.jjarroyo.demo.util.DemoCodeDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RibbonView extends ScrollPane {

    public RibbonView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));

        setFitToWidth(true);
        setContent(content);

        // Page Header
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("JRibbon")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Cintas diagonales esquineras para destacar productos, planes de precios o estados especiales")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);

        content.getChildren().add(pageHeader);

        // Section 1: Pricing Cards
        JCard card1 = new JCard("Tarjetas de Planes de Precios", createPricingCardsSection());
        card1.addToolbarItem(DemoCodeDialog.createCodeButton("Tarjetas de Precios",
            "// Envolver una JCard con una cinta promocional:\n" +
            "JCard proCard = createCard(\"Plan Pro\", \"$19 / mes\");\n" +
            "Node decoratedCard = JRibbon.wrap(proCard, \"POPULAR\", Variant.PURPLE);"));
        content.getChildren().add(card1);

        // Section 2: Color Variants
        JCard card2 = new JCard("Variantes de Color (Variant)", createColorVariantsSection());
        card2.addToolbarItem(DemoCodeDialog.createCodeButton("Variantes de Color",
            "// Cintas en variantes de color contextuales:\n" +
            "JRibbon.wrap(card1, \"NUEVO\", Variant.PRIMARY);\n" +
            "JRibbon.wrap(card2, \"-30% OFF\", Variant.SUCCESS);\n" +
            "JRibbon.wrap(card3, \"AGOTADO\", Variant.DANGER);\n" +
            "JRibbon.wrap(card4, \"BETA\", Variant.WARNING);\n" +
            "JRibbon.wrap(card5, \"VIP\", Variant.PURPLE);\n" +
            "JRibbon.wrap(card6, \"PRÓXIMO\", Variant.DARK);"));
        content.getChildren().add(card2);

        // Section 3: Position Left / Right
        JCard card3 = new JCard("Posicionamiento Esquina Izquierda vs Derecha", createPositionSection());
        card3.addToolbarItem(DemoCodeDialog.createCodeButton("Posicionamiento",
            "// Cambiar la esquina de posicionamiento (TOP_RIGHT / TOP_LEFT):\n" +
            "JRibbon ribbon = new JRibbon(\"BETA\", Variant.WARNING, Position.TOP_LEFT);\n" +
            "Node cardLeft = new JRibbon.JRibbonContainer(card, ribbon);"));
        content.getChildren().add(card3);
    }

    // =========================================================================
    // COPIAR Y PEGAR: Tarjetas de Planes con Cintas Promocionales
    // =========================================================================
    private Node createPricingCardsSection() {
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER_LEFT);

        // Free Plan
        JCard freeCard = createSamplePlanCard("Plan Básico", "$0 / mes", "Ideal para proyectos personales", "Comenzar Gratis");
        freeCard.setPrefWidth(240);

        // Pro Plan (Wrapped with PURPLE Ribbon)
        JCard proCard = createSamplePlanCard("Plan Pro", "$19 / mes", "Para desarrolladores activos", "Obtener Pro");
        proCard.setPrefWidth(240);
        Node wrappedPro = JRibbon.wrap(proCard, "POPULAR", Variant.PURPLE);

        // Enterprise Plan (Wrapped with PRIMARY Ribbon)
        JCard enterpriseCard = createSamplePlanCard("Plan Enterprise", "$49 / mes", "Para equipos y empresas", "Contactar Ventas");
        enterpriseCard.setPrefWidth(240);
        Node wrappedEnterprise = JRibbon.wrap(enterpriseCard, "RECOMENDADO", Variant.PRIMARY);

        container.getChildren().addAll(freeCard, wrappedPro, wrappedEnterprise);
        return container;
    }

    private JCard createSamplePlanCard(String name, String price, String desc, String btnText) {
        JCard card = new JCard();
        VBox body = new VBox(12);
        body.setPadding(new Insets(12));
        body.setAlignment(Pos.CENTER);

        JLabel lblName = new JLabel(name).withStyle("text-slate-800", "font-bold", "text-lg");
        JLabel lblPrice = new JLabel(price).withStyle("text-blue-600", "font-bold", "text-2xl");
        JLabel lblDesc = new JLabel(desc).withStyle("text-slate-500", "text-xs");

        JButton btn = new JButton(btnText);
        btn.addClass("btn-primary");

        body.getChildren().addAll(lblName, lblPrice, lblDesc, btn);
        card.setBody(body);
        return card;
    }

    // =========================================================================
    // COPIAR Y PEGAR: Variantes de Color
    // =========================================================================
    private Node createColorVariantsSection() {
        FlowPane grid = new FlowPane();
        grid.setHgap(16);
        grid.setVgap(16);

        grid.getChildren().addAll(
            JRibbon.wrap(createDemoBox("Primary"), "NUEVO", Variant.PRIMARY),
            JRibbon.wrap(createDemoBox("Success"), "-30% OFF", Variant.SUCCESS),
            JRibbon.wrap(createDemoBox("Danger"), "AGOTADO", Variant.DANGER),
            JRibbon.wrap(createDemoBox("Warning"), "BETA", Variant.WARNING),
            JRibbon.wrap(createDemoBox("Purple"), "VIP", Variant.PURPLE),
            JRibbon.wrap(createDemoBox("Dark"), "PRÓXIMO", Variant.DARK)
        );

        return grid;
    }

    private VBox createDemoBox(String titleText) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(20));
        box.setPrefSize(180, 100);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e2e8f0; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        JLabel label = new JLabel(titleText).withStyle("text-slate-700", "font-bold");
        box.getChildren().add(label);
        return box;
    }

    // =========================================================================
    // COPIAR Y PEGAR: Posición Izquierda / Derecha
    // =========================================================================
    private Node createPositionSection() {
        HBox container = new HBox(24);

        VBox card1 = createDemoBox("Esquina Derecha");
        Node rightWrapped = JRibbon.wrap(card1, "TOP_RIGHT", Variant.PRIMARY, Position.TOP_RIGHT);

        VBox card2 = createDemoBox("Esquina Izquierda");
        Node leftWrapped = JRibbon.wrap(card2, "TOP_LEFT", Variant.DANGER, Position.TOP_LEFT);

        container.getChildren().addAll(rightWrapped, leftWrapped);
        return container;
    }
}
