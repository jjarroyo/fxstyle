package com.jjarroyo.components;

import com.jjarroyo.FxStyle;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

/**
 * JDrawer — Panel lateral deslizable para contenido contextual.
 */
public class JDrawer extends StackPane {

    public enum Position {
        LEFT, RIGHT
    }

    public enum Size {
        SMALL("j-drawer-sm", 300),
        MEDIUM("j-drawer-md", 420),
        LARGE("j-drawer-lg", 600),
        FULL("j-drawer-full", -1);

        final String styleClass;
        final double width;

        Size(String styleClass, double width) {
            this.styleClass = styleClass;
            this.width = width;
        }
    }

    private StackPane backdrop;
    private VBox drawerPanel;
    private HBox headerContainer;
    private Label titleLabel;
    private StackPane closeButton;
    private VBox bodyContainer;
    private ScrollPane bodyScroll;
    private HBox footerContainer;

    private Position position = Position.RIGHT;
    private Size size = Size.MEDIUM;
    private boolean closeOnBackdropClick = true;
    private boolean showHeader = true;
    private Runnable onClose;

    public JDrawer() {
        init();
    }

    public JDrawer(Node body) {
        init();
        setBody(body);
    }

    public JDrawer(String title, Node body) {
        init();
        setTitle(title);
        setBody(body);
    }

    private void init() {
        getStyleClass().add("j-drawer-root");
        setVisible(false);
        setPickOnBounds(true);

        // 1. Backdrop
        backdrop = new StackPane();
        backdrop.getStyleClass().add("j-drawer-backdrop");
        backdrop.setOnMouseClicked(e -> {
            if (closeOnBackdropClick) close();
        });

        // 2. Header
        headerContainer = new HBox();
        headerContainer.getStyleClass().add("j-drawer-header");
        headerContainer.setAlignment(Pos.CENTER_LEFT);

        titleLabel = new Label();
        titleLabel.getStyleClass().add("j-drawer-title");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        // Close button
        SVGPath closeIcon = new SVGPath();
        closeIcon.setContent(JIcon.CLOSE.getPath());
        closeIcon.getStyleClass().add("j-drawer-close-icon");
        closeButton = new StackPane(closeIcon);
        closeButton.getStyleClass().add("j-drawer-close-btn");
        closeButton.setCursor(Cursor.HAND);
        closeButton.setOnMouseClicked(e -> close());

        headerContainer.getChildren().addAll(titleLabel, closeButton);

        // 3. Body (with scroll)
        bodyContainer = new VBox();
        bodyContainer.getStyleClass().add("j-drawer-body");

        bodyScroll = new ScrollPane(bodyContainer);
        bodyScroll.setFitToWidth(true);
        bodyScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        bodyScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        bodyScroll.getStyleClass().add("j-drawer-scroll");
        VBox.setVgrow(bodyScroll, Priority.ALWAYS);

        // 4. Footer
        footerContainer = new HBox(12);
        footerContainer.getStyleClass().add("j-drawer-footer");
        footerContainer.setAlignment(Pos.CENTER_RIGHT);
        footerContainer.setVisible(false);
        footerContainer.setManaged(false);

        // 5. Drawer Panel
        drawerPanel = new VBox();
        drawerPanel.getStyleClass().add("j-drawer-panel");
        drawerPanel.getChildren().addAll(headerContainer, bodyScroll, footerContainer);

        getChildren().addAll(backdrop, drawerPanel);

        applyPosition();
        applySize();

        drawerPanel.prefHeightProperty().bind(heightProperty());
        drawerPanel.minHeightProperty().bind(heightProperty());
    }

    public JDrawer setTitle(String title) {
        titleLabel.setText(title);
        return this;
    }

    public JDrawer setBody(Node content) {
        bodyContainer.getChildren().clear();
        if (content != null) {
            bodyContainer.getChildren().add(content);
            if (content instanceof Region) {
                VBox.setVgrow(content, Priority.ALWAYS);
            }
        }
        return this;
    }

    public JDrawer setFooter(Node... nodes) {
        footerContainer.getChildren().clear();
        if (nodes != null && nodes.length > 0) {
            footerContainer.getChildren().addAll(nodes);
            footerContainer.setVisible(true);
            footerContainer.setManaged(true);
        } else {
            footerContainer.setVisible(false);
            footerContainer.setManaged(false);
        }
        return this;
    }

    public JDrawer setPosition(Position position) {
        this.position = position;
        applyPosition();
        return this;
    }

    public JDrawer setSize(Size size) {
        this.size = size;
        applySize();
        return this;
    }

    public JDrawer setCloseOnBackdropClick(boolean close) {
        this.closeOnBackdropClick = close;
        return this;
    }

    public JDrawer setShowHeader(boolean show) {
        this.showHeader = show;
        headerContainer.setVisible(show);
        headerContainer.setManaged(show);
        return this;
    }

    public JDrawer setOnClose(Runnable onClose) {
        this.onClose = onClose;
        return this;
    }

    public JDrawer addClass(String... styleClasses) {
        drawerPanel.getStyleClass().addAll(styleClasses);
        return this;
    }

    public VBox getBodyContainer() {
        return bodyContainer;
    }

    public HBox getFooterContainer() {
        return footerContainer;
    }

    private void applyPosition() {
        if (position == Position.LEFT) {
            StackPane.setAlignment(drawerPanel, Pos.CENTER_LEFT);
            drawerPanel.getStyleClass().remove("j-drawer-right");
            if (!drawerPanel.getStyleClass().contains("j-drawer-left")) {
                drawerPanel.getStyleClass().add("j-drawer-left");
            }
        } else {
            StackPane.setAlignment(drawerPanel, Pos.CENTER_RIGHT);
            drawerPanel.getStyleClass().remove("j-drawer-left");
            if (!drawerPanel.getStyleClass().contains("j-drawer-right")) {
                drawerPanel.getStyleClass().add("j-drawer-right");
            }
        }
    }

    private void applySize() {
        drawerPanel.getStyleClass().removeAll(
            Size.SMALL.styleClass, Size.MEDIUM.styleClass,
            Size.LARGE.styleClass, Size.FULL.styleClass
        );
        drawerPanel.getStyleClass().add(size.styleClass);

        StackPane root = FxStyle.getModalContainer();
        if (root != null) {
            applySizeBindings(root);
        }
    }

    private void applySizeBindings(StackPane root) {
        drawerPanel.prefWidthProperty().unbind();
        drawerPanel.maxWidthProperty().unbind();
        drawerPanel.minWidthProperty().unbind();

        if (this.size == Size.FULL) {
            drawerPanel.prefWidthProperty().bind(root.widthProperty().multiply(0.90));
            drawerPanel.maxWidthProperty().bind(root.widthProperty().multiply(0.90));
            drawerPanel.minWidthProperty().bind(root.widthProperty().multiply(0.90));
        } else {
            drawerPanel.setPrefWidth(size.width);
            drawerPanel.setMaxWidth(size.width);
            drawerPanel.setMinWidth(size.width);
        }
    }

    public void show() {
        StackPane root = FxStyle.getModalContainer();
        if (root == null) return;

        this.prefWidthProperty().bind(root.widthProperty());
        this.prefHeightProperty().bind(root.heightProperty());

        applySizeBindings(root);

        root.getChildren().add(this);
        this.setVisible(true);
        playEntranceAnimation();
    }

    public void close() {
        playExitAnimation(() -> {
            StackPane root = FxStyle.getModalContainer();
            if (root != null) {
                root.getChildren().remove(this);
            }
            drawerPanel.prefWidthProperty().unbind();
            drawerPanel.maxWidthProperty().unbind();
            drawerPanel.minWidthProperty().unbind();
            this.prefWidthProperty().unbind();
            this.prefHeightProperty().unbind();

            if (onClose != null) {
                onClose.run();
            }
        });
    }

    private void playEntranceAnimation() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), backdrop);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        double slideDistance = (size == Size.FULL) ? (FxStyle.getModalContainer() != null ? FxStyle.getModalContainer().getWidth() * 0.90 : 900) : size.width;
        double fromX = position == Position.RIGHT ? slideDistance : -slideDistance;

        TranslateTransition slide = new TranslateTransition(Duration.millis(300), drawerPanel);
        slide.setFromX(fromX);
        slide.setToX(0);

        FadeTransition panelFade = new FadeTransition(Duration.millis(200), drawerPanel);
        panelFade.setFromValue(0.5);
        panelFade.setToValue(1.0);

        ParallelTransition pt = new ParallelTransition(fadeIn, slide, panelFade);
        pt.play();
    }

    private void playExitAnimation(Runnable onFinished) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), backdrop);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        double slideDistance = (size == Size.FULL) ? (FxStyle.getModalContainer() != null ? FxStyle.getModalContainer().getWidth() * 0.90 : 900) : size.width;
        double toX = position == Position.RIGHT ? slideDistance : -slideDistance;

        TranslateTransition slide = new TranslateTransition(Duration.millis(250), drawerPanel);
        slide.setFromX(0);
        slide.setToX(toX);

        FadeTransition panelFade = new FadeTransition(Duration.millis(200), drawerPanel);
        panelFade.setFromValue(1.0);
        panelFade.setToValue(0.3);

        ParallelTransition pt = new ParallelTransition(fadeOut, slide, panelFade);
        pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }
}
