package com.jjarroyo.components;

import javafx.scene.control.Label;

public class JBadge extends Label {

    private static final String DEFAULT_STYLE = "badge-primary";

    public JBadge() {
        super();
        init();
    }

    public JBadge(String text) {
        super(text);
        init();
    }

    public JBadge(String text, JIcon icon) {
        super(text);
        init();
        setIcon(icon);
    }

    public JBadge(String text, String... styleClasses) {
        super(text);
        init();
        addClass(styleClasses);
    }

    private void init() {
        // Add base class and default variant
        getStyleClass().addAll("badge", DEFAULT_STYLE);
    }

    public JBadge addClass(String... styleClasses) {
        for (String style : styleClasses) {
            // Intelligent replacement: 
            // If the user adds a new badge variant (starts with "badge-" but isn't just "badge"),
            // remove the default primary style.
            // EXCEPTION: Do not remove default if it's just a size or shape modifier.
            if (style != null && style.startsWith("badge-") && !style.equals("badge")
                    && !style.equals("badge-sm") && !style.equals("badge-lg") && !style.equals("badge-circle")) {
                getStyleClass().remove(DEFAULT_STYLE);
            }
        }
        getStyleClass().addAll(styleClasses);
        return this;
    }

    public void setIcon(JIcon icon) {
        if (icon != null) {
            javafx.scene.shape.SVGPath svg = new javafx.scene.shape.SVGPath();
            svg.setContent(icon.getPath());
            svg.getStyleClass().add("icon-svg");
            svg.setScaleX(0.8);
            svg.setScaleY(0.8);
            svg.setStyle("-fx-fill: currentColor;"); 
            
            setGraphic(svg);
            setGraphicTextGap(6);
            getStyleClass().add("badge-has-icon");
        } else {
            setGraphic(null);
            getStyleClass().remove("badge-has-icon");
        }
    }
}

