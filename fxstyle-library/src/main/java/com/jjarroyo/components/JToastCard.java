package com.jjarroyo.components;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * JToastCard — Componente de notificación flotante estilo tarjeta con colores suaves/claros (pastel/light),
 * icono temático, botón de cierre '✕' superior derecho, temporizador con barra de progreso y animación fluida (estilo Avast).
 */
public class JToastCard extends Popup {

    public enum Type {
        DEFAULT("j-toast-card-default", "M12 22C6.477 22 2 17.523 2 12S6.477 2 12 2s10 4.477 10 10-4.477 10-10 10z M12 16v-4 M12 8h.01"),
        SUCCESS("j-toast-card-success", "M22 11.08V12a10 10 0 1 1-5.93-9.14 M22 4L12 14.01l-3-3"),
        DANGER("j-toast-card-danger", "M12 2L3 7v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V7l-9-5zm-1 6h2v6h-2V8zm0 8h2v2h-2v-2z"),
        WARNING("j-toast-card-warning", "M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z M12 9v4 M12 17h.01"),
        INFO("j-toast-card-info", "M12 22C6.477 22 2 17.523 2 12S6.477 2 12 2s10 4.477 10 10-4.477 10-10 10z M12 16v-4 M12 8h.01");

        private final String styleClass;
        private final String iconPath;

        Type(String styleClass, String iconPath) {
            this.styleClass = styleClass;
            this.iconPath = iconPath;
        }

        public String getStyleClass() {
            return styleClass;
        }

        public String getIconPath() {
            return iconPath;
        }
    }

    public enum Position {
        BOTTOM_RIGHT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        TOP_LEFT,
        BOTTOM_CENTER,
        TOP_CENTER
    }

    private final StackPane wrapper;
    private final VBox cardRoot;
    private final StackPane bodyStack;
    private final HBox mainRow;
    private final StackPane iconBadge;
    private final SVGPath iconSvg;
    private final VBox textBox;
    private final Label titleLabel;
    private final Label messageLabel;
    private final Label closeBtn;
    private final StackPane progressContainer;
    private final Region progressBar;

    private PauseTransition autoCloseTimer;
    private Timeline progressTimeline;
    private Position currentPosition = Position.BOTTOM_RIGHT;
    private boolean isExiting = false;

    // Fluent builder pending state
    private String pendingTitle;
    private String pendingMessage;
    private Type pendingType = Type.DEFAULT;
    private Position pendingPosition = Position.BOTTOM_RIGHT;
    private Duration pendingDuration = Duration.millis(4000);
    private boolean pendingProgressBar = true;

    public JToastCard() {
        wrapper = new StackPane();
        wrapper.getStyleClass().add("j-toast-card-wrapper");

        cardRoot = new VBox();
        cardRoot.getStyleClass().addAll("j-toast-card", "j-toast-card-default");

        bodyStack = new StackPane();

        mainRow = new HBox(12);
        mainRow.setAlignment(Pos.TOP_LEFT);

        // Icon Badge
        iconBadge = new StackPane();
        iconBadge.getStyleClass().add("j-toast-card-icon-badge");
        iconBadge.setMinSize(36, 36);
        iconBadge.setPrefSize(36, 36);
        iconBadge.setMaxSize(36, 36);

        iconSvg = new SVGPath();
        iconSvg.getStyleClass().add("j-toast-card-icon");
        iconSvg.setScaleX(0.85);
        iconSvg.setScaleY(0.85);
        iconBadge.getChildren().add(iconSvg);

        // Text Box
        textBox = new VBox(3);
        textBox.getStyleClass().add("j-toast-card-content");
        textBox.setPadding(new Insets(0, 24, 0, 0)); // Clearance for top-right close button
        HBox.setHgrow(textBox, Priority.ALWAYS);

        titleLabel = new Label();
        titleLabel.getStyleClass().add("j-toast-card-title");
        titleLabel.setWrapText(true);

        messageLabel = new Label();
        messageLabel.getStyleClass().add("j-toast-card-message");
        messageLabel.setWrapText(true);

        textBox.getChildren().addAll(titleLabel, messageLabel);

        mainRow.getChildren().addAll(iconBadge, textBox);

        // Close Button - Pinned to Top-Right
        closeBtn = new Label("✕");
        closeBtn.getStyleClass().add("j-toast-card-close");
        closeBtn.setMinSize(22, 22);
        closeBtn.setPrefSize(22, 22);
        closeBtn.setMaxSize(22, 22);
        closeBtn.setAlignment(Pos.CENTER);
        closeBtn.setTextOverrun(javafx.scene.control.OverrunStyle.CLIP);
        StackPane.setAlignment(closeBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(closeBtn, new Insets(-2, -4, 0, 0));
        closeBtn.setOnMouseClicked(e -> dismissWithAnimation());

        bodyStack.getChildren().addAll(mainRow, closeBtn);

        // Progress bar container (countdown line at the bottom)
        progressContainer = new StackPane();
        progressContainer.setAlignment(Pos.CENTER);
        progressContainer.setMinHeight(3);
        progressContainer.setPrefHeight(3);
        progressContainer.setMaxHeight(3);

        progressBar = new Region();
        progressBar.getStyleClass().add("j-toast-card-progress");
        progressBar.setMinHeight(3);
        progressBar.setPrefHeight(3);
        progressBar.setMaxHeight(3);

        progressContainer.getChildren().add(progressBar);
        VBox.setMargin(progressContainer, new Insets(8, -16, -10, -16)); // Flush to bottom

        cardRoot.getChildren().addAll(bodyStack, progressContainer);
        wrapper.getChildren().add(cardRoot);

        getContent().add(wrapper);
        setAutoHide(false);
    }

    /**
     * Muestra la notificación card con parámetros completos.
     */
    public void display(Window owner, String title, String message, Type type, Position position, Duration duration, boolean showProgressBar) {
        if (isExiting) return;

        this.currentPosition = position != null ? position : Position.BOTTOM_RIGHT;
        Type actualType = type != null ? type : Type.DEFAULT;

        // Reset & apply type styles
        cardRoot.getStyleClass().setAll("j-toast-card", actualType.getStyleClass());
        iconSvg.setContent(actualType.getIconPath());

        // Set Title
        if (title != null && !title.trim().isEmpty()) {
            titleLabel.setText(title);
            titleLabel.setVisible(true);
            titleLabel.setManaged(true);
        } else {
            titleLabel.setVisible(false);
            titleLabel.setManaged(false);
        }

        // Set Message
        if (message != null && !message.trim().isEmpty()) {
            messageLabel.setText(message);
            messageLabel.setVisible(true);
            messageLabel.setManaged(true);
        } else {
            messageLabel.setVisible(false);
            messageLabel.setManaged(false);
        }

        // Pre-show and compute dimensions
        if (!isShowing()) {
            super.show(owner);
        }

        wrapper.applyCss();
        wrapper.layout();
        double width = wrapper.getBoundsInLocal().getWidth();
        double height = wrapper.getBoundsInLocal().getHeight();
        if (width <= 0) width = 360;
        if (height <= 0) height = 80;

        double screenX = owner != null ? owner.getX() : 0;
        double screenY = owner != null ? owner.getY() : 0;
        double screenW = owner != null ? owner.getWidth() : Screen.getPrimary().getVisualBounds().getWidth();
        double screenH = owner != null ? owner.getHeight() : Screen.getPrimary().getVisualBounds().getHeight();

        double margin = 20;
        double x = 0;
        double y = 0;

        switch (currentPosition) {
            case BOTTOM_RIGHT:
                x = screenX + screenW - width - margin;
                y = screenY + screenH - height - margin;
                break;
            case TOP_RIGHT:
                x = screenX + screenW - width - margin;
                y = screenY + margin + 30; // 30px offset for native titlebar clearance
                break;
            case BOTTOM_LEFT:
                x = screenX + margin;
                y = screenY + screenH - height - margin;
                break;
            case TOP_LEFT:
                x = screenX + margin;
                y = screenY + margin + 30;
                break;
            case BOTTOM_CENTER:
                x = screenX + (screenW - width) / 2;
                y = screenY + screenH - height - margin;
                break;
            case TOP_CENTER:
                x = screenX + (screenW - width) / 2;
                y = screenY + margin + 30;
                break;
        }

        setX(x);
        setY(y);

        // Entrance animation
        playEntranceAnimation();

        // Progress bar & auto-close timer logic
        if (duration != null && duration.greaterThan(Duration.ZERO)) {
            if (autoCloseTimer != null) autoCloseTimer.stop();
            autoCloseTimer = new PauseTransition(duration);
            autoCloseTimer.setOnFinished(e -> dismissWithAnimation());
            autoCloseTimer.play();

            if (showProgressBar) {
                progressContainer.setVisible(true);
                progressContainer.setManaged(true);
                progressBar.setVisible(true);

                if (progressTimeline != null) progressTimeline.stop();
                progressBar.setScaleX(1.0);
                progressTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(progressBar.scaleXProperty(), 1.0)),
                    new KeyFrame(duration, new KeyValue(progressBar.scaleXProperty(), 0.0, Interpolator.LINEAR))
                );
                progressTimeline.play();
            } else {
                progressContainer.setVisible(false);
                progressContainer.setManaged(false);
            }
        } else {
            // Persistent notification: No auto-close, No progress bar
            progressContainer.setVisible(false);
            progressContainer.setManaged(false);
            if (autoCloseTimer != null) autoCloseTimer.stop();
            if (progressTimeline != null) progressTimeline.stop();
        }
    }

    public void display(Window owner, String title, String message, Type type, Position position, Duration duration) {
        display(owner, title, message, type, position, duration, true);
    }

    public void display(Window owner, String title, String message, Type type, Position position, boolean autoClose) {
        display(owner, title, message, type, position, autoClose ? Duration.millis(4000) : null, autoClose);
    }

    private void playEntranceAnimation() {
        wrapper.setOpacity(0.0);

        double translateYDistance;
        if (currentPosition == Position.BOTTOM_RIGHT || currentPosition == Position.BOTTOM_LEFT || currentPosition == Position.BOTTOM_CENTER) {
            // Deslizamiento clásico desde abajo hacia arriba (Avast-style)
            translateYDistance = 45.0;
        } else {
            // Deslizamiento desde arriba hacia abajo
            translateYDistance = -45.0;
        }

        wrapper.setTranslateY(translateYDistance);

        TranslateTransition translate = new TranslateTransition(Duration.millis(350), wrapper);
        translate.setFromY(translateYDistance);
        translate.setToY(0);
        translate.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fade = new FadeTransition(Duration.millis(300), wrapper);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition parallel = new ParallelTransition(translate, fade);
        parallel.play();
    }

    /**
     * Cierra el card con una animación de salida suave.
     */
    public void dismissWithAnimation() {
        if (isExiting) return;
        isExiting = true;

        if (autoCloseTimer != null) autoCloseTimer.stop();
        if (progressTimeline != null) progressTimeline.stop();

        double exitTranslateY = (currentPosition == Position.BOTTOM_RIGHT || currentPosition == Position.BOTTOM_LEFT || currentPosition == Position.BOTTOM_CENTER)
                ? 30.0 : -30.0;

        TranslateTransition translate = new TranslateTransition(Duration.millis(250), wrapper);
        translate.setToY(exitTranslateY);
        translate.setInterpolator(Interpolator.EASE_IN);

        FadeTransition fade = new FadeTransition(Duration.millis(250), wrapper);
        fade.setFromValue(wrapper.getOpacity());
        fade.setToValue(0.0);
        fade.setInterpolator(Interpolator.EASE_IN);

        ParallelTransition parallel = new ParallelTransition(translate, fade);
        parallel.setOnFinished(e -> {
            hide();
            isExiting = false;
        });
        parallel.play();
    }

    // ── Static Helper Methods ───────────────────────────────────────────────

    public static void show(Window owner, String title, String message, Type type, Position pos, Duration duration) {
        new JToastCard().display(owner, title, message, type, pos, duration, true);
    }

    public static void show(Window owner, String title, String message, Type type, Position pos, int durationMillis) {
        new JToastCard().display(owner, title, message, type, pos, durationMillis > 0 ? Duration.millis(durationMillis) : null, durationMillis > 0);
    }

    public static void show(Window owner, String title, String message, Type type, Position pos, boolean autoClose) {
        new JToastCard().display(owner, title, message, type, pos, autoClose ? Duration.millis(4000) : null, autoClose);
    }

    public static void show(Window owner, String title, String message, Type type, boolean autoClose) {
        new JToastCard().display(owner, title, message, type, Position.BOTTOM_RIGHT, autoClose ? Duration.millis(4000) : null, autoClose);
    }

    public static void show(Window owner, String title, String message, Type type, Position pos) {
        new JToastCard().display(owner, title, message, type, pos, Duration.millis(4000), true);
    }

    public static void show(Window owner, String title, String message, Type type) {
        new JToastCard().display(owner, title, message, type, Position.BOTTOM_RIGHT, Duration.millis(4000), true);
    }

    public static void showDanger(Window owner, String title, String message) {
        new JToastCard().display(owner, title, message, Type.DANGER, Position.BOTTOM_RIGHT, Duration.millis(4500), true);
    }

    public static void showDanger(Window owner, String title, String message, boolean autoClose) {
        new JToastCard().display(owner, title, message, Type.DANGER, Position.BOTTOM_RIGHT, autoClose ? Duration.millis(4500) : null, autoClose);
    }

    public static void showWarning(Window owner, String title, String message) {
        new JToastCard().display(owner, title, message, Type.WARNING, Position.BOTTOM_RIGHT, Duration.millis(4000), true);
    }

    public static void showWarning(Window owner, String title, String message, boolean autoClose) {
        new JToastCard().display(owner, title, message, Type.WARNING, Position.BOTTOM_RIGHT, autoClose ? Duration.millis(4000) : null, autoClose);
    }

    public static void showSuccess(Window owner, String title, String message) {
        new JToastCard().display(owner, title, message, Type.SUCCESS, Position.BOTTOM_RIGHT, Duration.millis(3500), true);
    }

    public static void showSuccess(Window owner, String title, String message, boolean autoClose) {
        new JToastCard().display(owner, title, message, Type.SUCCESS, Position.BOTTOM_RIGHT, autoClose ? Duration.millis(3500) : null, autoClose);
    }

    public static void showInfo(Window owner, String title, String message) {
        new JToastCard().display(owner, title, message, Type.INFO, Position.BOTTOM_RIGHT, Duration.millis(3500), true);
    }

    public static void showInfo(Window owner, String title, String message, boolean autoClose) {
        new JToastCard().display(owner, title, message, Type.INFO, Position.BOTTOM_RIGHT, autoClose ? Duration.millis(3500) : null, autoClose);
    }

    public static void showSticky(Window owner, String title, String message, Type type, Position pos) {
        new JToastCard().display(owner, title, message, type, pos, null, false);
    }

    public static void showSticky(Window owner, String title, String message, Type type) {
        new JToastCard().display(owner, title, message, type, Position.BOTTOM_RIGHT, null, false);
    }

    // ── Fluent Builder API ──────────────────────────────────────────────────

    public static JToastCard make(String title, String message) {
        JToastCard card = new JToastCard();
        card.pendingTitle = title;
        card.pendingMessage = message;
        return card;
    }

    public JToastCard type(Type type) {
        this.pendingType = type;
        return this;
    }

    public JToastCard position(Position position) {
        this.pendingPosition = position;
        return this;
    }

    public JToastCard duration(Duration duration) {
        this.pendingDuration = duration;
        if (duration == null || duration.lessThanOrEqualTo(Duration.ZERO)) {
            this.pendingProgressBar = false;
        }
        return this;
    }

    public JToastCard duration(int millis) {
        if (millis <= 0) {
            this.pendingDuration = null;
            this.pendingProgressBar = false;
        } else {
            this.pendingDuration = Duration.millis(millis);
        }
        return this;
    }

    public JToastCard autoClose(boolean autoClose) {
        if (!autoClose) {
            this.pendingDuration = null;
            this.pendingProgressBar = false;
        } else if (this.pendingDuration == null) {
            this.pendingDuration = Duration.millis(4000);
            this.pendingProgressBar = true;
        }
        return this;
    }

    public JToastCard showProgressBar(boolean show) {
        this.pendingProgressBar = show;
        return this;
    }

    public JToastCard persistent() {
        return autoClose(false);
    }

    public JToastCard sticky() {
        return autoClose(false);
    }

    public JToastCard customIcon(String svgPath) {
        if (svgPath != null) {
            this.iconSvg.setContent(svgPath);
        }
        return this;
    }

    public void show(Window owner) {
        display(owner, pendingTitle, pendingMessage, pendingType, pendingPosition, pendingDuration, pendingProgressBar);
    }

    public void display(Window owner) {
        show(owner);
    }
}
