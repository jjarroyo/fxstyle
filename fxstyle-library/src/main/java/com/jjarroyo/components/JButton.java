package com.jjarroyo.components;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

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

    public JButton(String text, JIcon icon, String hexColor) {
        super(text);
        init();
        setIcon(icon, hexColor);
    }

    private void init() {
        getStyleClass().addAll("btn", DEFAULT_STYLE);
        
        getStyleClass().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (String style : change.getAddedSubList()) {
                        if (style != null && style.startsWith("btn-") && !style.equals("btn")
                                && !style.equals(DEFAULT_STYLE)
                                && !style.equals("btn-sm") && !style.equals("btn-lg")
                                && !style.equals("btn-md") && !style.equals("btn-rounded")
                                && !style.equals("btn-has-icon") && !style.equals("btn-icon")
                                && !style.equals("btn-icon-emerald") && !style.equals("btn-link-emerald")) {
                            Platform.runLater(() -> getStyleClass().remove(DEFAULT_STYLE));
                        }
                    }
                }
            }
        });
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
        setIcon(icon, null);
    }

    public void setIcon(JIcon icon, String hexColor) {
        if (icon != null) {
            SVGPath svg = new SVGPath();
            svg.setContent(icon.getPath());
            svg.getStyleClass().add("icon-svg");
            
            // Icono inline/outline más grande y nítido
            svg.setScaleX(1.15);
            svg.setScaleY(1.15);
            
            Color color;
            if (hexColor != null && !hexColor.isBlank()) {
                color = Color.web(hexColor);
            } else if (getStyleClass().contains("btn-ghost") || getStyleClass().contains("btn-outline")) {
                color = Color.web("#475569");
            } else {
                color = Color.WHITE;
            }

            if (icon == JIcon.BARCODE) {
                svg.setFill(color);
                svg.setStroke(Color.TRANSPARENT);
                svg.setStrokeWidth(0.0);
            } else {
                svg.setStrokeWidth(2.0);
                svg.setStrokeLineCap(StrokeLineCap.ROUND);
                svg.setStrokeLineJoin(StrokeLineJoin.ROUND);
                svg.setFill(Color.TRANSPARENT);
                svg.setStroke(color);
            }

            setGraphic(svg);
            if (getText() != null && !getText().isBlank()) {
                setGraphicTextGap(8);
            }
            getStyleClass().add("btn-has-icon");
        } else {
            setGraphic(null);
            getStyleClass().remove("btn-has-icon");
        }
    }

    public void setLoading(boolean loading) {
        if (this.isLoading == loading) return;
        this.isLoading = loading;

        Platform.runLater(() -> {
            if (loading) {
                this.originalText = getText();
                this.originalGraphic = getGraphic();

                String loadingText = (originalText != null && !originalText.isBlank()) ? "Ingresando..." : "";
                setText(loadingText);

                ProgressIndicator pi = new ProgressIndicator();
                pi.setPrefSize(16, 16);
                pi.setMaxSize(16, 16);
                pi.setMinSize(16, 16);
                pi.setStyle("-fx-progress-color: white;");

                setGraphic(pi);
                setGraphicTextGap(10);
                setDisable(true);
            } else {
                setText(originalText);
                setGraphic(originalGraphic);
                setDisable(false);
            }
        });
    }

    public void setOnAsyncAction(Consumer<Runnable> action) {
        setOnAction(e -> {
            setLoading(true);
            new Thread(() -> {
                try {
                    action.accept(() -> Platform.runLater(() -> setLoading(false)));
                } catch (Exception ex) {
                    Platform.runLater(() -> setLoading(false));
                }
            }).start();
        });
    }
}
