package com.jjarroyo.components;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public class JToast extends Popup {

    public enum Type {
        DEFAULT("j-toast"),
        SUCCESS("j-toast-success"),
        DANGER("j-toast-danger"),
        WARNING("j-toast-warning"),
        INFO("j-toast-info");

        private final String styleClass;
        Type(String styleClass) { this.styleClass = styleClass; }
    }

    public enum Position {
        TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_CENTER, BOTTOM_CENTER
    }

    private final HBox container;
    private final VBox contentBox;
    private final Label titleLabel;
    private final Label messageLabel;
    private PauseTransition autoCloseTimer;

    public JToast() {
        container = new HBox(12);
        container.getStyleClass().add("j-toast");
        container.setAlignment(Pos.CENTER_LEFT);

        contentBox = new VBox(2);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        
        titleLabel = new Label();
        titleLabel.getStyleClass().add("j-toast-title");
        titleLabel.setManaged(false);
        titleLabel.setVisible(false);

        messageLabel = new Label();
        messageLabel.getStyleClass().add("j-toast-message");
        messageLabel.setWrapText(true);

        contentBox.getChildren().addAll(titleLabel, messageLabel);

        // Close Button
        Label closeBtn = new Label("✕");
        closeBtn.getStyleClass().add("j-toast-close");
        closeBtn.setOnMouseClicked(e -> hide());
        
        // Layout: Content expands, Close stays right
        HBox.setHgrow(contentBox, javafx.scene.layout.Priority.ALWAYS);
        container.getChildren().addAll(contentBox, closeBtn);

        getContent().add(container);
        setAutoHide(false); // Manually handle closing
    }

    public void show(Window owner, String title, String message, Type type, Position position, Duration duration) {
        // Reset styles
        container.getStyleClass().setAll("j-toast");
        if (type != Type.DEFAULT) {
            container.getStyleClass().add(type.styleClass);
        }

        // Set Content
        if (title != null && !title.isEmpty()) {
            titleLabel.setText(title);
            titleLabel.setManaged(true);
            titleLabel.setVisible(true);
        } else {
            titleLabel.setManaged(false);
            titleLabel.setVisible(false);
        }
        messageLabel.setText(message);

        // Show to calculate size
        // Show to calculate size
        if (!isShowing()) super.show(owner);

        // Position Logic
        double x = 0, y = 0;
        double margin = 20;
        double width = container.getWidth();
        double height = container.getHeight();
        
        // If width/height are 0 (first time), try pref size or verify after show.
        // Actually Popup size might need delay or forced layout.
        container.applyCss();
        container.layout();
        width = container.getLayoutBounds().getWidth();
        height = container.getLayoutBounds().getHeight();

        double screenX = owner.getX();
        double screenY = owner.getY();
        double screenW = owner.getWidth();
        double screenH = owner.getHeight();

        switch (position) {
            case TOP_RIGHT:
                x = screenX + screenW - width - margin;
                y = screenY + margin;
                break;
            case TOP_LEFT:
                x = screenX + margin;
                y = screenY + margin;
                break;
            case BOTTOM_RIGHT:
                x = screenX + screenW - width - margin;
                y = screenY + screenH - height - margin;
                break;
            case BOTTOM_LEFT:
                x = screenX + margin;
                y = screenY + screenH - height - margin;
                break;
            case TOP_CENTER:
                x = screenX + (screenW - width) / 2;
                y = screenY + margin;
                break;
            case BOTTOM_CENTER:
                x = screenX + (screenW - width) / 2;
                y = screenY + screenH - height - margin;
                break;
        }

        setX(x);
        setY(y);

        // Auto Close
        if (duration != null && duration.greaterThan(Duration.ZERO)) {
            if (autoCloseTimer != null) autoCloseTimer.stop();
            autoCloseTimer = new PauseTransition(duration);
            autoCloseTimer.setOnFinished(e -> hide());
            autoCloseTimer.play();
        }
    }
    
    // Static helper for quick usage
    public static void show(Window owner, String message, Type type, Position pos, int durationMillis) {
        new JToast().show(owner, null, message, type, pos, Duration.millis(durationMillis));
    }
    
    // Static helper overload with title
    public static void show(Window owner, String title, String message, Type type, Position pos, int durationMillis) {
        new JToast().show(owner, title, message, type, pos, Duration.millis(durationMillis));
    }
    // Factory method for chaining
    public static JToast make(String title, String message, Type type, int durationMillis) {
        JToast toast = new JToast();
        // Store config to show later? Or just return initialized toast.
        // The show(Window) method takes proper args.
        // If we use chaining .show(window), we need to store these values.
        toast.pendingTitle = title;
        toast.pendingMessage = message;
        toast.pendingType = type;
        toast.pendingDuration = Duration.millis(durationMillis);
        return toast;
    }

    private String pendingTitle;
    private String pendingMessage;
    private Type pendingType = Type.DEFAULT;
    private Duration pendingDuration;
    
    // Overload show to use pending values if available
    public void show(Window owner) {
        show(owner, pendingTitle, pendingMessage, pendingType, Position.BOTTOM_RIGHT, pendingDuration); // Default position
    }
}

