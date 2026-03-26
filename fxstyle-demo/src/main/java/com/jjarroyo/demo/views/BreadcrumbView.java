package com.jjarroyo.demo.views;

import com.jjarroyo.components.JBreadcrumb;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JToast;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class BreadcrumbView extends ScrollPane {

    public BreadcrumbView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));

        setFitToWidth(true);
        setContent(content);

        // Page Title
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Breadcrumbs")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Navegación tipo migas de pan para indicar ubicación")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        // Basic
        content.getChildren().add(new JCard("Básico", createBasic()));

        // With actions
        content.getChildren().add(new JCard("Con Acciones", createWithActions()));

        // Separators
        content.getChildren().add(new JCard("Separadores Personalizados", createSeparators()));

        // Colors
        content.getChildren().add(new JCard("Colores", createColors()));

        // With Icons
        content.getChildren().add(new JCard("Con Íconos", createWithIcons()));
    }

    private javafx.scene.Node createBasic() {
        VBox container = new VBox(16);

        JBreadcrumb bc1 = new JBreadcrumb();
        bc1.addItem("Inicio")
           .addItem("Usuarios")
           .addItem("Perfil");

        JBreadcrumb bc2 = new JBreadcrumb();
        bc2.setPath("Dashboard", "Configuración", "Seguridad", "Contraseña");

        container.getChildren().addAll(bc1, bc2);
        return container;
    }

    private javafx.scene.Node createWithActions() {
        VBox container = new VBox(16);

        JBreadcrumb bc = new JBreadcrumb();
        bc.addItem("Inicio", () -> showToast("Navegando a Inicio"))
          .addItem("Productos", () -> showToast("Navegando a Productos"))
          .addItem("Categorías", () -> showToast("Navegando a Categorías"))
          .addItem("Electrónicos");

        javafx.scene.control.Label hint = new javafx.scene.control.Label("Haz clic en los items para ver la acción");
        hint.setStyle("-fx-font-size: 11px; -fx-text-fill: -color-slate-400;");

        container.getChildren().addAll(bc, hint);
        return container;
    }

    private javafx.scene.Node createSeparators() {
        VBox container = new VBox(16);

        JBreadcrumb bc1 = new JBreadcrumb();
        bc1.setPath("Home", "Settings", "Profile");
        bc1.setSeparator("/");

        JBreadcrumb bc2 = new JBreadcrumb();
        bc2.setPath("Home", "Settings", "Profile");
        bc2.setSeparator("›");

        JBreadcrumb bc3 = new JBreadcrumb();
        bc3.setPath("Home", "Settings", "Profile");
        bc3.setSeparator("→");

        JBreadcrumb bc4 = new JBreadcrumb();
        bc4.setPath("Home", "Settings", "Profile");
        bc4.setSeparator("•");

        container.getChildren().addAll(bc1, bc2, bc3, bc4);
        return container;
    }

    private javafx.scene.Node createColors() {
        VBox container = new VBox(16);

        String[] colors = {"primary", "success", "danger", "warning", "info", "slate"};
        for (String color : colors) {
            JBreadcrumb bc = new JBreadcrumb();
            bc.setPath("Inicio", "Sección", color)
              .setActiveColor(color);
            container.getChildren().add(bc);
        }

        return container;
    }

    private javafx.scene.Node createWithIcons() {
        VBox container = new VBox(16);

        JBreadcrumb bc = new JBreadcrumb();
        bc.addItem("Dashboard", JIcon.DASHBOARD.view(), () -> showToast("Dashboard"))
          .addItem("Usuarios", JIcon.PERSON.view(), () -> showToast("Usuarios"))
          .addItem("Jorge Arroyo", JIcon.USER.view());

        container.getChildren().add(bc);
        return container;
    }

    private void showToast(String message) {
        if (getScene() != null && getScene().getWindow() != null) {
            JToast.show(getScene().getWindow(), message, JToast.Type.INFO, JToast.Position.BOTTOM_RIGHT, 2000);
        }
    }
}
