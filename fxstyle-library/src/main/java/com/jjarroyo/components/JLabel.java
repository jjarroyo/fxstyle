package com.jjarroyo.components;

import javafx.scene.control.Label;

public class JLabel extends Label {

    public JLabel() {
        super();
        init();
    }

    public JLabel(String text) {
        super(text);
        init();
    }
    
    public JLabel(JIcon icon) {
        super();
        init();
        setIcon(icon);
    }
    
    public JLabel(String text, JIcon icon) {
        super(text);
        init();
        setIcon(icon);
    }

    private void init() {
        getStyleClass().add("j-label");
    }
    
    public JLabel setIcon(JIcon icon) {
        if (icon != null) {
            setGraphic(icon.view());
        } else {
            setGraphic(null);
        }
        return this;
    }
    
    public JLabel setIconColor(String cssColor) {
        if (getGraphic() != null) {
            getGraphic().setStyle("-fx-fill: " + cssColor + ";");
        }
        return this;
    }
    // Helper to add style classes easily
    public JLabel withStyle(String... styleClasses) {
        getStyleClass().addAll(styleClasses);
        return this;
    }
    
    // Alias for withStyle (consistent with other controls)
    public JLabel addClass(String... styleClasses) {
        return withStyle(styleClasses);
    }
    
    public JLabel setMultiline(boolean multiline) {
        setWrapText(multiline);
        return this;
    }
}
