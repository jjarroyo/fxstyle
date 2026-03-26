package com.jjarroyo.demo.views;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JDrawer;
import com.jjarroyo.components.JInput;
import com.jjarroyo.components.JTextArea;
import com.jjarroyo.components.JToast;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class DrawerView extends ScrollPane {

    public DrawerView() {
        getStyleClass().add("j-scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));

        VBox content = new VBox(24);

        // Header
        VBox header = new VBox(8);
        Label title = new Label("Drawer");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Panel lateral deslizable para contenido contextual, formularios y detalles.");
        subtitle.getStyleClass().add("page-subtitle");
        header.getChildren().addAll(title, subtitle);
        content.getChildren().add(header);

        // 1. Drawer básico (derecha)
        JCard card1 = new JCard("Drawer Básico", "Panel que se desliza desde la derecha con contenido simple.");
        FlowPane box1 = new FlowPane(12, 12);
        JButton btn1 = new JButton("Abrir Drawer");
        btn1.addClass("btn-primary");
        btn1.setOnAction(e -> {
            VBox drawerContent = new VBox(12);
            drawerContent.getChildren().addAll(
                new Label("Este es un drawer básico con contenido simple."),
                new Label("Haz clic en la X o en el fondo oscuro para cerrarlo.")
            );
            new JDrawer()
                .setTitle("Drawer Básico")
                .setBody(drawerContent)
                .show();
        });
        box1.getChildren().add(btn1);
        card1.setBody(box1);
        content.getChildren().add(card1);

        // 2. Drawer Left
        JCard card2 = new JCard("Drawer Izquierdo", "Panel que se desliza desde la izquierda.");
        FlowPane box2 = new FlowPane(12, 12);
        JButton btn2 = new JButton("Abrir desde Izquierda");
        btn2.addClass("btn-primary");
        btn2.setOnAction(e -> {
            VBox drawerContent = new VBox(12);
            drawerContent.getChildren().addAll(
                new Label("Este drawer se abre desde el lado izquierdo."),
                new Label("Ideal para menús de navegación o filtros.")
            );
            new JDrawer()
                .setTitle("Panel Izquierdo")
                .setBody(drawerContent)
                .setPosition(JDrawer.Position.LEFT)
                .show();
        });
        box2.getChildren().add(btn2);
        card2.setBody(box2);
        content.getChildren().add(card2);

        // 3. Sizes
        JCard card3 = new JCard("Tamaños", "Small (300px), Medium (420px), Large (600px) y Full.");
        FlowPane box3 = new FlowPane(12, 12);

        for (JDrawer.Size size : JDrawer.Size.values()) {
            JButton btn = new JButton(size.name());
            btn.addClass("btn-primary");
            btn.setOnAction(e -> {
                VBox drawerContent = new VBox(12);
                drawerContent.getChildren().addAll(
                    new Label("Tamaño: " + size.name()),
                    new Label("Este drawer usa el tamaño " + size.name() + ".")
                );
                new JDrawer()
                    .setTitle("Drawer " + size.name())
                    .setBody(drawerContent)
                    .setSize(size)
                    .show();
            });
            box3.getChildren().add(btn);
        }
        card3.setBody(box3);
        content.getChildren().add(card3);

        // 4. Con Header, Body y Footer
        JCard card4 = new JCard("Con Footer", "Drawer con header, cuerpo y footer con botones de acción.");
        FlowPane box4 = new FlowPane(12, 12);
        JButton btn4 = new JButton("Drawer con Footer");
        btn4.addClass("btn-primary");
        btn4.setOnAction(e -> {
            VBox drawerContent = new VBox(16);
            drawerContent.getChildren().addAll(
                new Label("Información del registro"),
                new Label("ID: 1234"),
                new Label("Nombre: Jorge Arroyo"),
                new Label("Email: jorge@example.com"),
                new Label("Rol: Administrador"),
                new Label("Fecha de creación: 2024-01-15"),
                new Label("Último acceso: 2024-03-20")
            );

            JButton saveBtn = new JButton("Guardar");
            saveBtn.addClass("btn-success");

            JButton cancelBtn = new JButton("Cancelar");
            cancelBtn.addClass("btn-light-danger");

            JDrawer drawer = new JDrawer()
                .setTitle("Detalle de Usuario")
                .setBody(drawerContent)
                .setSize(JDrawer.Size.MEDIUM);

            saveBtn.setOnAction(ev -> {
                drawer.close();
                JToast.show(getScene().getWindow(), "Datos guardados", JToast.Type.SUCCESS, JToast.Position.BOTTOM_RIGHT, 3000);
            });
            cancelBtn.setOnAction(ev -> drawer.close());

            drawer.setFooter(cancelBtn, saveBtn);
            drawer.show();
        });
        box4.getChildren().add(btn4);
        card4.setBody(box4);
        content.getChildren().add(card4);

        // 5. Formulario de edición
        JCard card5 = new JCard("Formulario en Drawer", "Drawer con formulario completo de edición.");
        FlowPane box5 = new FlowPane(12, 12);
        JButton btn5 = new JButton("Editar Producto");
        btn5.addClass("btn-primary");
        btn5.setOnAction(e -> {
            VBox form = new VBox(16);

            JInput nameInput = new JInput();
            nameInput.setPromptText("Nombre del producto");
            nameInput.setText("Laptop Pro 15\"");

            JInput priceInput = new JInput();
            priceInput.setPromptText("Precio");
            priceInput.setText("1,299.99");

            JInput skuInput = new JInput();
            skuInput.setPromptText("SKU");
            skuInput.setText("LP-PRO-15-2024");

            JTextArea descInput = new JTextArea();
            descInput.setPromptText("Descripción del producto");
            descInput.setText("Laptop de alto rendimiento con pantalla de 15 pulgadas, procesador de última generación y 16GB de RAM.");

            form.getChildren().addAll(
                createFormField("Nombre", nameInput),
                createFormField("Precio", priceInput),
                createFormField("SKU", skuInput),
                createFormField("Descripción", descInput)
            );

            JButton saveBtn = new JButton("Guardar Cambios");
            saveBtn.addClass("btn-success");

            JButton cancelBtn = new JButton("Cancelar");
            cancelBtn.addClass("btn-light-danger");

            JDrawer drawer = new JDrawer()
                .setTitle("Editar Producto")
                .setBody(form)
                .setSize(JDrawer.Size.LARGE);

            saveBtn.setOnAction(ev -> {
                drawer.close();
                JToast.show(getScene().getWindow(), "Producto actualizado", JToast.Type.SUCCESS, JToast.Position.BOTTOM_RIGHT, 3000);
            });
            cancelBtn.setOnAction(ev -> drawer.close());

            drawer.setFooter(cancelBtn, saveBtn);
            drawer.show();
        });
        box5.getChildren().add(btn5);
        card5.setBody(box5);
        content.getChildren().add(card5);

        // 6. No backdrop click
        JCard card6 = new JCard("Sin Cierre por Backdrop", "El drawer solo se cierra con el botón X.");
        FlowPane box6 = new FlowPane(12, 12);
        JButton btn6 = new JButton("Drawer protegido");
        btn6.addClass("btn-warning");
        btn6.setOnAction(e -> {
            VBox drawerContent = new VBox(12);
            drawerContent.getChildren().addAll(
                new Label("Este drawer NO se cierra al hacer clic en el fondo."),
                new Label("Solo se puede cerrar con el botón X o el botón de abajo.")
            );

            JButton closeBtn = new JButton("Cerrar Drawer");
            closeBtn.addClass("btn-danger");

            JDrawer drawer = new JDrawer()
                .setTitle("Drawer Protegido")
                .setBody(drawerContent)
                .setCloseOnBackdropClick(false);

            closeBtn.setOnAction(ev -> drawer.close());
            drawer.setFooter(closeBtn);
            drawer.show();
        });
        box6.getChildren().add(btn6);
        card6.setBody(box6);
        content.getChildren().add(card6);

        setContent(content);
    }

    private VBox createFormField(String labelText, javafx.scene.Node input) {
        VBox field = new VBox(4);
        Label label = new Label(labelText);
        label.getStyleClass().add("j-label");
        label.setStyle("-fx-font-weight: 600; -fx-font-size: 13px;");
        field.getChildren().addAll(label, input);
        return field;
    }
}
