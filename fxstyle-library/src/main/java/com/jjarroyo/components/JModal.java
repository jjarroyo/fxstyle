package com.jjarroyo.components;

import com.jjarroyo.FxStyle;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * JModal - Componente modal estilizado para FxStyle.
 * Soporte completo para Size.SMALL, Size.MEDIUM, Size.LARGE y Size.FULL.
 *
 * El alto del modal siempre respeta el espacio disponible de la pantalla
 * (dejando margen arriba/abajo). Si el body es más grande que ese límite,
 * aparece scroll solo dentro del body; si es más chico, el modal se ajusta
 * a su contenido sin estirarse innecesariamente.
 */
public class JModal extends StackPane {

    public enum Size {
        SMALL("modal-sm"),
        MEDIUM("modal-md"),
        LARGE("modal-lg"),
        FULL("modal-full");

        final String styleClass;
        Size(String styleClass) { this.styleClass = styleClass; }
    }

    // Margen vertical total (arriba + abajo) que siempre se respeta,
    // sin importar el tamaño de pantalla.
    private static final double VERTICAL_MARGIN = 64;
    // Alto mínimo de seguridad para pantallas muy pequeñas.
    private static final double MIN_DIALOG_HEIGHT = 200;

    private StackPane backdrop;
    private VBox dialogContainer;
    private VBox headerContainer;
    private StackPane bodyContainer;
    private ScrollPane bodyScroll;
    private HBox footerContainer;
    private Size size = Size.MEDIUM;
    private boolean closeOnBackdropClick = true;

    public JModal() {
        init();
    }

    public JModal(String title, Node body, Size size) {
        this.size = size;
        init();
        setTitle(title);
        setBody(body);
    }

    public JModal(Node body) {
        init();
        setBody(body);
    }
    
    public JModal(Node body, Size size) {
        this.size = size;
        init();
        setBody(body);
    }

    private void init() {
        getStyleClass().add("j-modal-root");
        setVisible(false);

        // 1. Backdrop
        backdrop = new StackPane();
        backdrop.getStyleClass().add("j-modal-backdrop");
        backdrop.setOnMouseClicked(e -> {
            if (closeOnBackdropClick) close();
        });

        // 2. Containers for Structure
        headerContainer = new VBox();
        headerContainer.getStyleClass().add("modal-header");
        headerContainer.setVisible(false);
        headerContainer.setManaged(false);

        bodyContainer = new StackPane();
        bodyContainer.getStyleClass().add("modal-body");

        // El body vive dentro de un ScrollPane. Este es el que realmente
        // absorbe el "sobrante" cuando el contenido no entra en el alto
        // máximo permitido: en vez de desbordar la pantalla, aparece scroll.
        bodyScroll = new ScrollPane(bodyContainer);
        bodyScroll.getStyleClass().add("modal-body-scroll");
        bodyScroll.setFitToWidth(true);
        bodyScroll.setFitToHeight(false);
        bodyScroll.setPannable(false);
        VBox.setVgrow(bodyScroll, Priority.ALWAYS);

        footerContainer = new HBox();
        footerContainer.getStyleClass().add("modal-footer");
        footerContainer.setVisible(false);
        footerContainer.setManaged(false);

        // 3. Dialog Container
        dialogContainer = new VBox();
        dialogContainer.getStyleClass().add("j-modal-dialog");
        dialogContainer.getChildren().addAll(headerContainer, bodyScroll, footerContainer);

        // Clip so children respect rounded corners
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
        clip.setArcWidth(24);
        clip.setArcHeight(24);
        clip.widthProperty().bind(dialogContainer.widthProperty());
        clip.heightProperty().bind(dialogContainer.heightProperty());
        dialogContainer.setClip(clip);

        // Add children
        getChildren().addAll(backdrop, dialogContainer);
        StackPane.setAlignment(dialogContainer, Pos.CENTER);
        setAlignment(Pos.CENTER);
        
        // Apply initial size
        setSize(this.size);
    }

    public JModal setTitle(String title) {
        if (title != null && !title.isBlank()) {
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().addAll("text-lg", "font-bold", "text-slate-800");
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            JButton btnClose = new JButton("", JIcon.CLOSE, "#64748b");
            btnClose.getStyleClass().addAll("btn-ghost", "btn-sm");
            btnClose.setOnAction(e -> close());
            
            HBox headerBox = new HBox(12, titleLabel, spacer, btnClose);
            headerBox.setAlignment(Pos.CENTER_LEFT);
            setHeader(headerBox);
        }
        return this;
    }

    public JModal setHeader(Node... nodes) {
        headerContainer.getChildren().setAll(nodes);
        boolean hasContent = nodes.length > 0;
        headerContainer.setVisible(hasContent);
        headerContainer.setManaged(hasContent);
        return this;
    }

    public JModal setBody(Node node) {
        bodyContainer.getChildren().setAll(node);
        return this;
    }

    public JModal setFooter(Node... nodes) {
        footerContainer.getChildren().setAll(nodes);
        boolean hasContent = nodes.length > 0;
        footerContainer.setVisible(hasContent);
        footerContainer.setManaged(hasContent);
        return this;
    }

    public void show() {
        StackPane root = FxStyle.getModalContainer();
        if (root == null) return;
        
        this.prefWidthProperty().bind(root.widthProperty());
        this.prefHeightProperty().bind(root.heightProperty());

        applySizeBindings(root);

        StackPane.setAlignment(this, Pos.CENTER);
        StackPane.setAlignment(dialogContainer, Pos.CENTER);

        root.getChildren().add(this);
        this.setVisible(true);
        
        // Animations
        playEntranceAnimation();
    }

    private void applySizeBindings(StackPane root) {
        dialogContainer.prefWidthProperty().unbind();
        dialogContainer.maxWidthProperty().unbind();
        dialogContainer.prefHeightProperty().unbind();
        dialogContainer.maxHeightProperty().unbind();
        bodyScroll.maxHeightProperty().unbind();

        if (this.size == Size.FULL) {
            dialogContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.85));
            dialogContainer.maxWidthProperty().bind(root.widthProperty().multiply(0.85));
        }

        // IMPORTANTE: dialogContainer NUNCA debe tener un maxHeight "grande"
        // basado en la pantalla, porque StackPane estira a sus hijos hasta
        // su maxHeight sin importar si el contenido es chico. Por eso el
        // tope de pantalla NO va aquí, sino en el ScrollPane del body (abajo).
        // Esto restaura el comportamiento natural: el modal mide lo que su
        // contenido necesita, ni un pixel más.
        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        dialogContainer.setMaxHeight(Region.USE_PREF_SIZE);

        // El límite de pantalla vive en el ScrollPane: es lo único que se
        // "topa" contra el espacio disponible (alto de pantalla - margen -
        // header - footer). Si el body cabe, el ScrollPane toma su alto
        // natural (sin scroll, sin estirar el modal). Si no cabe, se frena
        // aquí y el scroll aparece SOLO dentro del body.
        DoubleBinding availableForBody = Bindings.createDoubleBinding(
            () -> Math.max(
                root.getHeight() - VERTICAL_MARGIN
                        - headerContainer.getHeight()
                        - footerContainer.getHeight(),
                MIN_DIALOG_HEIGHT
            ),
            root.heightProperty(),
            headerContainer.heightProperty(),
            footerContainer.heightProperty()
        );
        bodyScroll.maxHeightProperty().bind(availableForBody);
    }

    public void close() {
        playExitAnimation(() -> {
            StackPane root = FxStyle.getModalContainer();
            if (root != null) {
                root.getChildren().remove(this);
            }
            dialogContainer.prefWidthProperty().unbind();
            dialogContainer.maxWidthProperty().unbind();
            dialogContainer.prefHeightProperty().unbind();
            dialogContainer.maxHeightProperty().unbind();
            bodyScroll.maxHeightProperty().unbind();
        });
    }

    private void playEntranceAnimation() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), backdrop);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(250), dialogContainer);
        scale.setFromX(0.85);
        scale.setFromY(0.85);
        scale.setToX(1.0);
        scale.setToY(1.0);
        
        FadeTransition dialogFade = new FadeTransition(Duration.millis(250), dialogContainer);
        dialogFade.setFromValue(0);
        dialogFade.setToValue(1);
        
        ParallelTransition pt = new ParallelTransition(fadeIn, scale, dialogFade);
        pt.play();
    }

    private void playExitAnimation(Runnable onFinished) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), backdrop);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(200), dialogContainer);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(0.9);
        scale.setToY(0.9);
        
        FadeTransition dialogFade = new FadeTransition(Duration.millis(200), dialogContainer);
        dialogFade.setFromValue(1);
        dialogFade.setToValue(0);

        ParallelTransition pt = new ParallelTransition(fadeOut, scale, dialogFade);
        pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }
    
    // Fluent API
    public JModal setCloseOnBackdropClick(boolean close) {
        this.closeOnBackdropClick = close;
        return this;
    }
    
    public JModal setSize(Size size) {
        this.size = size;
        dialogContainer.getStyleClass().removeAll(Size.SMALL.styleClass, Size.MEDIUM.styleClass, Size.LARGE.styleClass, Size.FULL.styleClass);
        dialogContainer.getStyleClass().add(size.styleClass);
        
        StackPane root = FxStyle.getModalContainer();
        if (root != null) {
            applySizeBindings(root);
        }
        
        return this;
    }

    public VBox getDialogContainer() { return dialogContainer; }
    public VBox getHeaderContainer() { return headerContainer; }
    public StackPane getBodyContainer() { return bodyContainer; }
    public ScrollPane getBodyScroll() { return bodyScroll; }
    public HBox getFooterContainer() { return footerContainer; }
    public StackPane getBackdrop() { return backdrop; }
}