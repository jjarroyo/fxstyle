package com.jjarroyo.demo.views;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JSplitButton;
import com.jjarroyo.components.JSplitButton.Variant;
import com.jjarroyo.demo.util.DemoCodeDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ButtonsView extends ScrollPane {

    public ButtonsView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24)); 
        
        setFitToWidth(true);
        setContent(content);
        
        // Page Title
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Buttons")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Button styles inspired by Tailwind/JJArroyo")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        
        content.getChildren().add(pageHeader);
        
        // Section: Colors
        JCard c1 = new JCard("Solid Colors", createSolidButtons());
        c1.addToolbarItem(DemoCodeDialog.createCodeButton("Botones Sólidos",
            "JButton btnPrimary = new JButton(\"Primary\");\n" +
            "JButton btnSuccess = new JButton(\"Success\").addClass(\"btn-success\");\n" +
            "JButton btnDanger = new JButton(\"Danger\").addClass(\"btn-danger\");"));
        content.getChildren().add(c1);

        // Section: Outline
        JCard c2 = new JCard("Outline Style", createOutlineButtons());
        c2.addToolbarItem(DemoCodeDialog.createCodeButton("Botones Outline",
            "JButton btnOutline = new JButton(\"Outline Primary\");\n" +
            "btnOutline.addClass(\"btn-outline-primary\");"));
        content.getChildren().add(c2);

        // Section: Sizes
        JCard c3 = new JCard("Sizes", createSizeButtons());
        c3.addToolbarItem(DemoCodeDialog.createCodeButton("Tamaños de Botón",
            "JButton btnSm = new JButton(\"Small\").addClass(\"btn-sm\");\n" +
            "JButton btnMd = new JButton(\"Default\");\n" +
            "JButton btnLg = new JButton(\"Large\").addClass(\"btn-lg\");"));
        content.getChildren().add(c3);

        // Section: Light Style
        JCard c4 = new JCard("Light Style", createLightButtons());
        c4.addToolbarItem(DemoCodeDialog.createCodeButton("Botones Light",
            "JButton btnLight = new JButton(\"Primary Light\").addClass(\"btn-light-primary\");"));
        content.getChildren().add(c4);

        // Section: Dashed Style
        JCard c5 = new JCard("Dashed Style", createDashedButtons());
        content.getChildren().add(c5);

        // Section: Icon Text Color Style
        JCard c6 = new JCard("Icon Buttons", createIconButtons());
        c6.addToolbarItem(DemoCodeDialog.createCodeButton("Botones con Icono",
            "JButton btnStar = new JButton(\"Favoritos\", JIcon.HEART);\n" +
            "btnStar.addClass(\"btn-accent-info\");"));
        content.getChildren().add(c6);

        // Section: Loading Style
        JCard c7 = new JCard("Loading Style", createLoadingButtons());
        content.getChildren().add(c7);

        // Section: JSplitButton (Split Action Dropdown)
        JCard c8 = new JCard("Botones Divididos con Menú Desplegable (JSplitButton)", createSplitButtons());
        c8.addToolbarItem(DemoCodeDialog.createCodeButton("JSplitButton",
            "// Crear un botón dividido con acción principal e ítems de menú:\n" +
            "JSplitButton saveBtn = new JSplitButton(\"Guardar Cambios\", () -> save());\n" +
            "saveBtn.addMenuItem(\"Guardar y Cerrar\", () -> saveAndClose());\n" +
            "saveBtn.addMenuItem(\"Guardar como Borrador\", () -> saveDraft());\n" +
            "saveBtn.addSeparator();\n" +
            "saveBtn.addMenuItem(\"Descartar\", () -> discard());"));
        content.getChildren().add(c8);
    }

        // =========================================================================
    // COPIAR Y PEGAR: Botones Sólidos (Primary, Success, Danger, Warning, Info, Dark)
    // Ejemplo: JButton btn = new JButton("Texto").addClass("btn-success");
    // =========================================================================
private Node createSolidButtons() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        // Default is Primary, no need to add class explicitly
        JButton btnPrimary = new JButton("Primary");
        // btnPrimary.addClass("btn-primary"); // Default

        JButton btnSuccess = new JButton("Success");
        btnSuccess.addClass("btn-success");

        JButton btnDanger = new JButton("Danger");
        btnDanger.addClass("btn-danger");
        
        JButton btnWarning = new JButton("Warning");
        btnWarning.addClass("btn-warning");

        JButton btnInfo = new JButton("Info");
        btnInfo.addClass("btn-info");

        JButton btnSecondary = new JButton("Secondary");
        btnSecondary.addClass("btn-secondary");

        JButton btnDark = new JButton("Dark");
        btnDark.addClass("btn-dark");

        pane.getChildren().addAll(btnPrimary, btnSuccess, btnDanger, btnWarning, btnInfo, btnSecondary, btnDark);
        return pane;
    }

        // =========================================================================
    // COPIAR Y PEGAR: Botones con Borde (Outline)
    // Ejemplo: JButton btn = new JButton("Outline").addClass("btn-outline-primary");
    // =========================================================================
private Node createOutlineButtons() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        JButton btnOutline = new JButton("Outline Primary");
        btnOutline.addClass("btn-outline-primary");

        pane.getChildren().addAll(btnOutline);
        return pane;
    }
    
        // =========================================================================
    // COPIAR Y PEGAR: Tamaños de Botón (btn-sm, Normal, btn-lg)
    // Ejemplo: JButton btn = new JButton("Pequeno").addClass("btn-sm");
    // =========================================================================
private Node createSizeButtons() {
        HBox box = new HBox(16);
        box.setAlignment(Pos.CENTER_LEFT);
        
        JButton btnSm = new JButton("Small Button");
        btnSm.addClass("btn-sm"); // Should keep default primary
        
        JButton btnMd = new JButton("Default Button");
        // Default primary
        
        JButton btnLg = new JButton("Large Button");
        btnLg.addClass("btn-lg"); // Should keep default primary

        box.getChildren().addAll(btnSm, btnMd, btnLg);
        return box;
    }

        // =========================================================================
    // COPIAR Y PEGAR: Botones con Fondo Suave (Light Style)
    // Ejemplo: JButton btn = new JButton("Suave").addClass("btn-light-primary");
    // =========================================================================
private Node createLightButtons() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        JButton btnLightPrimary = new JButton("Primary Light");
        btnLightPrimary.addClass("btn-light-primary");

        JButton btnLightSuccess = new JButton("Success Light");
        btnLightSuccess.addClass("btn-light-success");

        JButton btnLightDanger = new JButton("Danger Light");
        btnLightDanger.addClass("btn-light-danger");

        pane.getChildren().addAll(btnLightPrimary, btnLightSuccess, btnLightDanger);
        return pane;
    }

    private Node createDashedButtons() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        JButton btnDashed = new JButton("Add New");
        btnDashed.addClass("btn-dashed");

        pane.getChildren().addAll(btnDashed);
        return pane;
    }

        // =========================================================================
    // COPIAR Y PEGAR: Botones con Icono (JIcon)
    // Ejemplo: JButton btn = new JButton("Favoritos", JIcon.HEART);
    // =========================================================================
private Node createIconButtons() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        // Using JIcon now!
        JButton btnIconPrimary = new JButton("Star Button", JIcon.STAR);
        btnIconPrimary.addClass("btn-accent-primary");

        JButton btnIconInfo = new JButton("Favorites", JIcon.HEART);
        btnIconInfo.addClass("btn-accent-info");
        
        JButton btnSettings = new JButton("Settings", JIcon.SETTINGS);
        btnSettings.addClass("btn-secondary");

        pane.getChildren().addAll(btnIconPrimary, btnIconInfo, btnSettings);
        return pane;
    }

        // =========================================================================
    // COPIAR Y PEGAR: Botones con Estado de Carga (ProgressIndicator)
    // Ejemplo: btn.setGraphic(new ProgressIndicator());
    // =========================================================================
private Node createLoadingButtons() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);
        
        // Standard Loading (Default is primary)
        JButton btnLoading1 = new JButton("Please wait...");
        // btnLoading1.addClass("btn-primary"); // Default
        ProgressIndicator spinner1 = new ProgressIndicator();
        spinner1.setMaxSize(20, 20);
        spinner1.setStyle("-fx-progress-color: white;");
        btnLoading1.setGraphic(spinner1);

        // Icon only Loading
        JButton btnLoading2 = new JButton();
        // btnLoading2.addClass("btn-primary"); // Default
        ProgressIndicator spinner2 = new ProgressIndicator();
        spinner2.setMaxSize(20, 20);
        spinner2.setStyle("-fx-progress-color: white;");
        btnLoading2.setGraphic(spinner2);

        // Right side loading
        JButton btnLoading3 = new JButton("Loading");
        btnLoading3.addClass("btn-outline-primary");
        btnLoading3.setContentDisplay(ContentDisplay.RIGHT);
        ProgressIndicator spinner3 = new ProgressIndicator();
        spinner3.setMaxSize(20, 20);
        btnLoading3.setGraphic(spinner3);
        
        // Secondary Loading
        JButton btnLoading4 = new JButton("Processing");
        btnLoading4.addClass("btn-secondary");
        ProgressIndicator spinner4 = new ProgressIndicator();
        spinner4.setMaxSize(20, 20);
        btnLoading4.setGraphic(spinner4);

        pane.getChildren().addAll(btnLoading1, btnLoading2, btnLoading3, btnLoading4);
        return pane;
    }

    private Node createSplitButtons() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);

        JSplitButton btnPrimary = new JSplitButton("Guardar Cambios")
                .addMenuItem("Guardar y Salir", () -> {})
                .addMenuItem("Guardar como Borrador", () -> {})
                .addSeparator()
                .addMenuItem("Descartar", () -> {});
        btnPrimary.setVariant(Variant.PRIMARY);

        JSplitButton btnSuccess = new JSplitButton("Publicar Documento")
                .addMenuItem("Publicar Ahora", () -> {})
                .addMenuItem("Programar Publicación", () -> {})
                .addSeparator()
                .addMenuItem("Guardar Borrador", () -> {});
        btnSuccess.setVariant(Variant.SUCCESS);

        JSplitButton btnDanger = new JSplitButton("Eliminar Registro")
                .addMenuItem("Eliminar permanentemente", () -> {})
                .addMenuItem("Mover a la Papelera", () -> {});
        btnDanger.setVariant(Variant.DANGER);

        JSplitButton btnOutline = new JSplitButton("Exportar Datos")
                .addMenuItem("Exportar a PDF", () -> {})
                .addMenuItem("Exportar a Excel", () -> {})
                .addMenuItem("Exportar a CSV", () -> {});
        btnOutline.setVariant(Variant.OUTLINE);

        pane.getChildren().addAll(btnPrimary, btnSuccess, btnDanger, btnOutline);
        return pane;
    }
}



