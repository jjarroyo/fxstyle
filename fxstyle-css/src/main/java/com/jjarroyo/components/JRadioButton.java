package com.jjarroyo.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.RadioButton;

public class JRadioButton extends RadioButton {

    public enum RadioStyle {
        PRIMARY("radio-primary"),
        SUCCESS("radio-success"),
        DANGER("radio-danger"),
        WARNING("radio-warning"),
        INFO("radio-info"),
        DARK("radio-dark"),
        SECONDARY("radio-secondary");

        private final String styleClass;

        RadioStyle(String styleClass) {
            this.styleClass = styleClass;
        }

        public String getStyleClass() {
            return styleClass;
        }
    }

    public enum RadioSize {
        SMALL("radio-sm"),
        DEFAULT("radio-md"),
        LARGE("radio-lg");
        
        private final String styleClass;
        
        RadioSize(String styleClass) {
            this.styleClass = styleClass;
        }
        
        public String getStyleClass() {
            return styleClass;
        }
    }

    private final ObjectProperty<RadioStyle> colorStyle = new SimpleObjectProperty<>(RadioStyle.PRIMARY);
    private final ObjectProperty<RadioSize> size = new SimpleObjectProperty<>(RadioSize.DEFAULT);

    public JRadioButton() {
        super();
        init();
    }

    public JRadioButton(String text) {
        super(text);
        init();
    }

    private void init() {
        getStyleClass().add("j-radio");
        updateStyle();
        updateSize();

        colorStyle.addListener((obs, old, newVal) -> {
            getStyleClass().removeAll(old.getStyleClass());
            getStyleClass().add(newVal.getStyleClass());
        });
        
        size.addListener((obs, old, newVal) -> {
            getStyleClass().removeAll(old.getStyleClass());
            getStyleClass().add(newVal.getStyleClass());
        });
    }

    private void updateStyle() {
        getStyleClass().add(colorStyle.get().getStyleClass());
    }

    private void updateSize() {
        getStyleClass().add(size.get().getStyleClass());
    }

    public ObjectProperty<RadioStyle> colorStyleProperty() { return colorStyle; }
    public RadioStyle getColorStyle() { return colorStyle.get(); }
    public void setColorStyle(RadioStyle style) { this.colorStyle.set(style); }
    
    public ObjectProperty<RadioSize> sizeProperty() { return size; }
    public RadioSize getSize() { return size.get(); }
    public void setSize(RadioSize size) { this.size.set(size); }
}

