package com.jjarroyo.components;

import com.jjarroyo.FxStyle;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * JConfirmDialog — Diálogo de confirmación reutilizable con variantes, iconos y callbacks.
 *
 * <p><b>Uso básico (una línea):</b></p>
 * <pre>
 * JConfirmDialog.show("¿Eliminar registro?", () -&gt; delete());
 * </pre>
 *
 * <p><b>Uso con variantes:</b></p>
 * <pre>
 * JConfirmDialog.show("¿Eliminar?", "Esta acción no se puede deshacer.", Type.DANGER, () -&gt; delete());
 * </pre>
 *
 * <p><b>Uso avanzado (Fluent API):</b></p>
 * <pre>
 * new JConfirmDialog()
 *     .setTitle("Confirmar envío")
 *     .setMessage("Se enviará el reporte al servidor.")
 *     .setType(Type.INFO)
 *     .setConfirmText("Enviar")
 *     .setCancelText("Cancelar")
 *     .setOnConfirm(() -&gt; enviarReporte())
 *     .setOnCancel(() -&gt; System.out.println("Cancelado"))
 *     .show();
 * </pre>
 *
 * <p><b>Uso con acción asíncrona (loading state):</b></p>
 * <pre>
 * new JConfirmDialog()
 *     .setTitle("¿Eliminar usuario?")
 *     .setType(Type.DANGER)
 *     .setOnConfirmAsync(() -&gt; userService.delete(userId), success -&gt; {
 *         if (success) refreshTable();
 *     })
 *     .show();
 * </pre>
 */
public class JConfirmDialog extends StackPane {

    // ═══════════════════════════════════════════════════════════════════════════════
    // ENUMS
    // ═══════════════════════════════════════════════════════════════════════════════

    public enum Type {
        INFO("j-confirm-info", JIcon.INFO,
             "btn-primary", "btn-light-primary"),
        SUCCESS("j-confirm-success", JIcon.CHECK_CIRCLE,
                "btn-success", "btn-light-success"),
        WARNING("j-confirm-warning", JIcon.WARNING,
                "btn-warning", "btn-light-warning"),
        DANGER("j-confirm-danger", JIcon.DELETE,
               "btn-danger", "btn-light-danger");

        final String styleClass;
        final JIcon defaultIcon;
        final String confirmBtnClass;
        final String cancelBtnClass;

        Type(String styleClass, JIcon defaultIcon, String confirmBtnClass, String cancelBtnClass) {
            this.styleClass = styleClass;
            this.defaultIcon = defaultIcon;
            this.confirmBtnClass = confirmBtnClass;
            this.cancelBtnClass = cancelBtnClass;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // FIELDS
    // ═══════════════════════════════════════════════════════════════════════════════

    private StackPane backdrop;
    private VBox dialogContainer;
    private StackPane iconContainer;
    private Label titleLabel;
    private Label messageLabel;
    private Button confirmBtn;
    private Button cancelBtn;
    private HBox footerContainer;

    private Type type = Type.INFO;
    private JIcon customIcon = null;
    private String title = "¿Estás seguro?";
    private String message = "";
    private String confirmText = "Confirmar";
    private String cancelText = "Cancelar";
    private boolean closeOnBackdropClick = false;
    private boolean showCancelButton = true;

    private Runnable onConfirm;
    private Runnable onCancel;

    // Async support
    private Supplier<Boolean> asyncAction;
    private Consumer<Boolean> asyncCallback;

    // ═══════════════════════════════════════════════════════════════════════════════
    // CONSTRUCTORS
    // ═══════════════════════════════════════════════════════════════════════════════

    public JConfirmDialog() {
        init();
    }

    public JConfirmDialog(String title) {
        this.title = title;
        init();
    }

    public JConfirmDialog(String title, String message) {
        this.title = title;
        this.message = message;
        init();
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // INIT
    // ═══════════════════════════════════════════════════════════════════════════════

    private void init() {
        getStyleClass().add("j-confirm-root");
        setVisible(false);

        // 1. Backdrop
        backdrop = new StackPane();
        backdrop.getStyleClass().add("j-confirm-backdrop");
        backdrop.setOnMouseClicked(e -> {
            if (closeOnBackdropClick) close();
        });

        // 2. Icon container
        iconContainer = new StackPane();
        iconContainer.getStyleClass().add("j-confirm-icon-container");

        // 3. Title
        titleLabel = new Label(title);
        titleLabel.getStyleClass().add("j-confirm-title");
        titleLabel.setWrapText(true);

        // 4. Message
        messageLabel = new Label(message);
        messageLabel.getStyleClass().add("j-confirm-message");
        messageLabel.setWrapText(true);
        if (message == null || message.isEmpty()) {
            messageLabel.setVisible(false);
            messageLabel.setManaged(false);
        }

        // 5. Buttons
        confirmBtn = new Button(confirmText);
        confirmBtn.getStyleClass().addAll("btn", "btn-primary");
        confirmBtn.setOnAction(e -> handleConfirm());

        cancelBtn = new Button(cancelText);
        cancelBtn.getStyleClass().addAll("btn", "btn-light-primary");
        cancelBtn.setOnAction(e -> handleCancel());

        footerContainer = new HBox(12);
        footerContainer.getStyleClass().add("j-confirm-footer");
        footerContainer.setAlignment(Pos.CENTER);
        footerContainer.getChildren().addAll(cancelBtn, confirmBtn);

        // 6. Body container
        VBox bodyContent = new VBox(8);
        bodyContent.getStyleClass().add("j-confirm-body");
        bodyContent.setAlignment(Pos.CENTER);
        bodyContent.getChildren().addAll(iconContainer, titleLabel, messageLabel, footerContainer);

        // 7. Dialog container
        dialogContainer = new VBox();
        dialogContainer.getStyleClass().add("j-confirm-dialog");
        dialogContainer.setAlignment(Pos.CENTER);
        dialogContainer.setMaxHeight(Region.USE_PREF_SIZE);
        dialogContainer.setMaxWidth(Region.USE_PREF_SIZE);
        dialogContainer.getChildren().add(bodyContent);

        getChildren().addAll(backdrop, dialogContainer);
        setAlignment(Pos.CENTER);
        // Ensure backdrop fills fully
        StackPane.setAlignment(backdrop, Pos.CENTER);

        // Apply type
        applyType();
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // FLUENT API
    // ═══════════════════════════════════════════════════════════════════════════════

    public JConfirmDialog setTitle(String title) {
        this.title = title;
        titleLabel.setText(title);
        return this;
    }

    public JConfirmDialog setMessage(String message) {
        this.message = message;
        messageLabel.setText(message);
        boolean hasMessage = message != null && !message.isEmpty();
        messageLabel.setVisible(hasMessage);
        messageLabel.setManaged(hasMessage);
        return this;
    }

    public JConfirmDialog setType(Type type) {
        this.type = type;
        applyType();
        return this;
    }

    public JConfirmDialog setIcon(JIcon icon) {
        this.customIcon = icon;
        applyType();
        return this;
    }

    public JConfirmDialog setConfirmText(String text) {
        this.confirmText = text;
        confirmBtn.setText(text);
        return this;
    }

    public JConfirmDialog setCancelText(String text) {
        this.cancelText = text;
        cancelBtn.setText(text);
        return this;
    }

    public JConfirmDialog setShowCancelButton(boolean show) {
        this.showCancelButton = show;
        cancelBtn.setVisible(show);
        cancelBtn.setManaged(show);
        return this;
    }

    public JConfirmDialog setCloseOnBackdropClick(boolean close) {
        this.closeOnBackdropClick = close;
        return this;
    }

    public JConfirmDialog setOnConfirm(Runnable onConfirm) {
        this.onConfirm = onConfirm;
        this.asyncAction = null;
        this.asyncCallback = null;
        return this;
    }

    public JConfirmDialog setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    /**
     * Configura una acción asíncrona para el botón confirmar.
     * El botón mostrará un spinner mientras la acción se ejecuta en background.
     *
     * @param action   Supplier que retorna true (éxito) o false (fallo). Se ejecuta en hilo background.
     * @param callback Consumer que recibe el resultado. Se ejecuta en hilo de JavaFX.
     */
    public JConfirmDialog setOnConfirmAsync(Supplier<Boolean> action, Consumer<Boolean> callback) {
        this.asyncAction = action;
        this.asyncCallback = callback;
        this.onConfirm = null;
        return this;
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // INTERNAL HANDLERS
    // ═══════════════════════════════════════════════════════════════════════════════

    private void handleConfirm() {
        if (asyncAction != null) {
            // Async mode: show loading and run in background
            setLoadingState(true);
            Thread thread = new Thread(() -> {
                boolean result = false;
                try {
                    result = asyncAction.get();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                final boolean finalResult = result;
                Platform.runLater(() -> {
                    setLoadingState(false);
                    close();
                    if (asyncCallback != null) {
                        asyncCallback.accept(finalResult);
                    }
                });
            });
            thread.setDaemon(true);
            thread.start();
        } else {
            close();
            if (onConfirm != null) {
                onConfirm.run();
            }
        }
    }

    private void handleCancel() {
        close();
        if (onCancel != null) {
            onCancel.run();
        }
    }

    private String originalConfirmText;

    private void setLoadingState(boolean loading) {
        if (loading) {
            originalConfirmText = confirmBtn.getText();
            ProgressIndicator spinner = new ProgressIndicator();
            spinner.setMaxSize(16, 16);
            spinner.setPrefSize(16, 16);
            spinner.getStyleClass().add("btn-spinner");
            confirmBtn.setGraphic(spinner);
            confirmBtn.setText("Procesando...");
            confirmBtn.setDisable(true);
            cancelBtn.setDisable(true);
        } else {
            confirmBtn.setGraphic(null);
            confirmBtn.setText(originalConfirmText != null ? originalConfirmText : confirmText);
            confirmBtn.setDisable(false);
            cancelBtn.setDisable(false);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // TYPE STYLING
    // ═══════════════════════════════════════════════════════════════════════════════

    private void applyType() {
        // Remove old type classes
        dialogContainer.getStyleClass().removeAll(
            Type.INFO.styleClass, Type.SUCCESS.styleClass,
            Type.WARNING.styleClass, Type.DANGER.styleClass
        );
        dialogContainer.getStyleClass().add(type.styleClass);

        // Apply icon
        iconContainer.getChildren().clear();
        JIcon iconToUse = customIcon != null ? customIcon : type.defaultIcon;
        SVGPath svg = new SVGPath();
        svg.setContent(iconToUse.getPath());
        svg.getStyleClass().add("j-confirm-icon");
        iconContainer.getChildren().add(svg);

        // Apply button styles
        confirmBtn.getStyleClass().removeAll(
            "btn-primary", "btn-success", "btn-warning", "btn-danger",
            "btn-light-primary", "btn-light-success", "btn-light-warning", "btn-light-danger"
        );
        cancelBtn.getStyleClass().removeAll(
            "btn-primary", "btn-success", "btn-warning", "btn-danger",
            "btn-light-primary", "btn-light-success", "btn-light-warning", "btn-light-danger"
        );
        confirmBtn.getStyleClass().add(type.confirmBtnClass);
        cancelBtn.getStyleClass().add(type.cancelBtnClass);
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // SHOW / CLOSE
    // ═══════════════════════════════════════════════════════════════════════════════

    public void show() {
        StackPane root = FxStyle.getModalContainer();
        if (root == null) return;

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
        });
    }

    private void playEntranceAnimation() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), backdrop);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(250), dialogContainer);
        scale.setFromX(0.7);
        scale.setFromY(0.7);
        scale.setToX(1.0);
        scale.setToY(1.0);

        FadeTransition dialogFade = new FadeTransition(Duration.millis(250), dialogContainer);
        dialogFade.setFromValue(0);
        dialogFade.setToValue(1);

        ParallelTransition pt = new ParallelTransition(fadeIn, scale, dialogFade);
        pt.play();
    }

    private void playExitAnimation(Runnable onFinished) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), backdrop);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(150), dialogContainer);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(0.85);
        scale.setToY(0.85);

        FadeTransition dialogFade = new FadeTransition(Duration.millis(150), dialogContainer);
        dialogFade.setFromValue(1);
        dialogFade.setToValue(0);

        ParallelTransition pt = new ParallelTransition(fadeOut, scale, dialogFade);
        pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // STATIC SHORTCUTS
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * Muestra una confirmación básica con un callback.
     *
     * <pre>
     * JConfirmDialog.show("¿Eliminar este registro?", () -&gt; deleteRecord());
     * </pre>
     */
    public static void show(String title, Runnable onConfirm) {
        new JConfirmDialog(title)
            .setOnConfirm(onConfirm)
            .show();
    }

    /**
     * Muestra una confirmación con mensaje y callback.
     */
    public static void show(String title, String message, Runnable onConfirm) {
        new JConfirmDialog(title, message)
            .setOnConfirm(onConfirm)
            .show();
    }

    /**
     * Muestra una confirmación con tipo, mensaje y callback.
     *
     * <pre>
     * JConfirmDialog.show("¿Eliminar?", "No se puede deshacer.", Type.DANGER, () -&gt; delete());
     * </pre>
     */
    public static void show(String title, String message, Type type, Runnable onConfirm) {
        new JConfirmDialog(title, message)
            .setType(type)
            .setOnConfirm(onConfirm)
            .show();
    }

    /**
     * Muestra un diálogo de eliminación pre-configurado (DANGER).
     *
     * <pre>
     * JConfirmDialog.showDelete("¿Eliminar el usuario 'Juan'?", () -&gt; deleteUser());
     * </pre>
     */
    public static void showDelete(String message, Runnable onConfirm) {
        new JConfirmDialog("Eliminar", message)
            .setType(Type.DANGER)
            .setConfirmText("Eliminar")
            .setIcon(JIcon.DELETE_FOREVER)
            .setOnConfirm(onConfirm)
            .show();
    }
}
