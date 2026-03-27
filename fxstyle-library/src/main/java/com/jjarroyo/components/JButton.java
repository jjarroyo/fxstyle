package com.jjarroyo.components;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.shape.SVGPath;

import java.util.function.Consumer;

public class JButton extends Button {

    private static final String DEFAULT_STYLE = "btn-primary";
    private String originalText;
    private Node originalGraphic;
    private boolean isLoading = false;

    public JButton() {
        super();
        init();
    }

    public JButton(String text) {
        super(text);
        init();
    }

    public JButton(String text, JIcon icon) {
        super(text);
        init();
        setIcon(icon);
    }

    private void init() {
        getStyleClass().addAll("btn", DEFAULT_STYLE);
    }

    public JButton addClass(String... styleClasses) {
        for (String style : styleClasses) {
            if (style != null && style.startsWith("btn-") && !style.equals("btn") 
                    && !style.equals("btn-sm") && !style.equals("btn-lg")) {
                getStyleClass().remove(DEFAULT_STYLE);
            }
        }
        getStyleClass().addAll(styleClasses);
        return this;
    }

    public void setIcon(JIcon icon) {
        if (icon != null) {
            SVGPath svg = new SVGPath();
            svg.setContent(icon.getPath());
            svg.getStyleClass().add("icon-svg");
            svg.setScaleX(0.8);
            svg.setScaleY(0.8);
            svg.setStyle("-fx-fill: -fx-text-fill;"); 
            
            setGraphic(svg);
            setGraphicTextGap(8);
            getStyleClass().add("btn-has-icon");
        } else {
            setGraphic(null);
            getStyleClass().remove("btn-has-icon");
        }
    }

    /**
     * Activa/desactiva el estado de loading con un spinner animado.
     */
    public void setLoading(boolean loading) {
        if (loading && !isLoading) {
            isLoading = true;
            originalText = getText();
            originalGraphic = getGraphic();

            // Spinner nativo de JavaFX
            javafx.scene.control.ProgressIndicator spinner = new javafx.scene.control.ProgressIndicator();
            spinner.setMaxSize(18, 18);
            spinner.setPrefSize(18, 18);
            spinner.getStyleClass().add("btn-spinner");

            setText("Cargando...");
            setGraphic(spinner);
            setDisable(true);
            if (!getStyleClass().contains("btn-loading")) {
                getStyleClass().add("btn-loading");
            }
        } else if (!loading && isLoading) {
            isLoading = false;
            setText(originalText);
            setGraphic(originalGraphic);
            setDisable(false);
            getStyleClass().remove("btn-loading");
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    /**
     * Ejecuta una acción en un hilo separado con loading automático.
     * El Runnable 'action' se ejecuta en background.
     * El {@code Consumer<Boolean>} 'callback' recibe true (éxito) o false (fallo) en el hilo de JavaFX.
     *
     * Ejemplo:
     * {@code
     *   btn.setOnActionAsync(
     *       () -> viewModel.login(),
     *       success -> { if (success) navegarDashboard(); }
     *   );
     * }
     */
    public void setOnActionAsync(java.util.function.Supplier<Boolean> action, Consumer<Boolean> callback) {
        setOnAction(e -> {
            if (isLoading) return;
            setLoading(true);

            Thread thread = new Thread(() -> {
                boolean result = false;
                try {
                    result = action.get();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                final boolean finalResult = result;
                Platform.runLater(() -> {
                    setLoading(false);
                    if (callback != null) {
                        callback.accept(finalResult);
                    }
                });
            });
            thread.setDaemon(true);
            thread.start();
        });
    }
}
