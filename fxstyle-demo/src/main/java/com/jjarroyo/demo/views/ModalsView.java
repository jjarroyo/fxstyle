package com.jjarroyo.demo.views;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JModal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class ModalsView extends ScrollPane {

    public ModalsView() {
        getStyleClass().add("j-scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));

        VBox content = new VBox(24);

        // Header
        VBox header = new VBox(8);
        Label title = new Label("Modals");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Diálogos overlay con transiciones y diferentes tamaños.");
        subtitle.getStyleClass().add("page-subtitle");
        header.getChildren().addAll(title, subtitle);
        content.getChildren().add(header);

        // 1. Tamaños
        JCard cardSizes = new JCard("Tamaños de Modal", "Small, Medium, Large y Full Screen.");
        FlowPane sizesBox = new FlowPane(12, 12);
        sizesBox.getChildren().addAll(
            createModalTrigger("Small", JModal.Size.SMALL),
            createModalTrigger("Medium", JModal.Size.MEDIUM),
            createModalTrigger("Large", JModal.Size.LARGE),
            createModalTrigger("Full Screen", JModal.Size.FULL)
        );
        cardSizes.setBody(sizesBox);
        content.getChildren().add(cardSizes);

        // 2. Modal con Header, Body y Footer estructurados
        JCard cardStructured = new JCard("Modal Estructurado", "Usando setHeader(), setBody() y setFooter() para una estructura limpia.");
        FlowPane structBox = new FlowPane(12, 12);
        JButton btnStruct = new JButton("Modal Estructurado");
        btnStruct.addClass("btn-primary");
        btnStruct.setOnAction(e -> {
            JModal modal = new JModal();
            modal.setSize(JModal.Size.MEDIUM);

            // Header with title + close button
            modal.setHeader(createModalHeader("Editar Perfil", modal));

            // Body
            VBox body = new VBox(16);
            body.getChildren().addAll(
                new Label("Nombre: Jorge Arroyo"),
                new Label("Email: jorge@example.com"),
                new Label("Rol: Administrador"),
                new Label("Aquí podrías colocar un formulario completo de edición.")
            );
            modal.setBody(body);

            // Footer
            JButton saveBtn = new JButton("Guardar");
            saveBtn.addClass("btn-success");
            saveBtn.setOnAction(ev -> modal.close());

            JButton cancelBtn = new JButton("Cancelar");
            cancelBtn.addClass("btn-light-danger");
            cancelBtn.setOnAction(ev -> modal.close());

            modal.setFooter(cancelBtn, saveBtn);
            modal.show();
        });
        structBox.getChildren().add(btnStruct);
        cardStructured.setBody(structBox);
        content.getChildren().add(cardStructured);

        // 3. Modal sin cerrar con backdrop
        JCard cardNoClose = new JCard("Sin Cierre por Backdrop", "El modal solo se cierra con la X o los botones internos.");
        FlowPane noCloseBox = new FlowPane(12, 12);
        JButton btnNoClose = new JButton("Modal Protegido");
        btnNoClose.addClass("btn-warning");
        btnNoClose.setOnAction(e -> {
            JModal modal = new JModal();
            modal.setSize(JModal.Size.SMALL);
            modal.setCloseOnBackdropClick(false);

            // Header with X
            modal.setHeader(createModalHeader("Acción Requerida", modal));

            VBox body = new VBox(12);
            body.getChildren().add(new Label("Debes completar esta acción antes de continuar.\nEste modal no se cierra al hacer clic afuera."));
            modal.setBody(body);

            JButton closeBtn = new JButton("Entendido");
            closeBtn.addClass("btn-primary");
            closeBtn.setOnAction(ev -> modal.close());
            modal.setFooter(closeBtn);

            modal.show();
        });
        noCloseBox.getChildren().add(btnNoClose);
        cardNoClose.setBody(noCloseBox);
        content.getChildren().add(cardNoClose);

        setContent(content);
    }

    /**
     * Creates a modal header HBox with title label + X close button.
     */
    private HBox createModalHeader(String title, JModal modal) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-padding: 0;");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("modal-title");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        SVGPath closeIcon = new SVGPath();
        closeIcon.setContent(JIcon.CLOSE.getPath());
        closeIcon.setStyle("-fx-fill: -color-text-secondary;");

        StackPane closeBtn = new StackPane(closeIcon);
        closeBtn.setCursor(Cursor.HAND);
        closeBtn.setStyle("-fx-padding: 6px; -fx-background-radius: 6px;");
        closeBtn.setOnMouseClicked(e -> modal.close());
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle("-fx-padding: 6px; -fx-background-radius: 6px; -fx-background-color: rgba(0,0,0,0.06);"));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle("-fx-padding: 6px; -fx-background-radius: 6px;"));

        header.getChildren().addAll(titleLabel, closeBtn);
        return header;
    }

    private JButton createModalTrigger(String text, JModal.Size size) {
        JButton btn = new JButton(text);
        btn.addClass("btn-primary");
        btn.setOnAction(e -> {
            JModal modal = new JModal();
            modal.setSize(size);

            // Header with title + X close button
            modal.setHeader(createModalHeader("Modal " + text, modal));

            // Body
            VBox body = new VBox(12);
            body.getChildren().addAll(
                new Label("Este es un modal de tamaño " + size.name().toLowerCase() + "."),
                new Label("Incluye animaciones de entrada y salida suaves.")
            );
            modal.setBody(body);

            // Footer
            JButton closeBtn = new JButton("Cerrar");
            closeBtn.addClass("btn-light-danger");
            closeBtn.setOnAction(ev -> modal.close());
            modal.setFooter(closeBtn);

            modal.show();
        });
        return btn;
    }
}
