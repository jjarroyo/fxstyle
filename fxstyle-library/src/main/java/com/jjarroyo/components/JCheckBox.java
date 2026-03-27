package com.jjarroyo.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckBox;

public class JCheckBox extends CheckBox {

    public enum CheckBoxStyle {
        PRIMARY("checkbox-primary"),
        SUCCESS("checkbox-success"),
        DANGER("checkbox-danger"),
        WARNING("checkbox-warning"),
        INFO("checkbox-info"),
        DARK("checkbox-dark"),
        SECONDARY("checkbox-secondary");

        private final String styleClass;

        CheckBoxStyle(String styleClass) {
            this.styleClass = styleClass;
        }

        public String getStyleClass() {
            return styleClass;
        }
    }
    
    public enum CheckBoxSize {
        SMALL("checkbox-sm"),
        DEFAULT("checkbox-md"),
        LARGE("checkbox-lg");
        
        private final String styleClass;
        
        CheckBoxSize(String styleClass) {
            this.styleClass = styleClass;
        }
        
        public String getStyleClass() {
            return styleClass;
        }
    }

    private final ObjectProperty<CheckBoxStyle> colorStyle = new SimpleObjectProperty<>(CheckBoxStyle.PRIMARY);
    private final ObjectProperty<CheckBoxSize> size = new SimpleObjectProperty<>(CheckBoxSize.DEFAULT);

    public JCheckBox() {
        super();
        init();
    }

    public JCheckBox(String text) {
        super(text);
        init();
    }

    private void init() {
        getStyleClass().add("j-checkbox");
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

    public ObjectProperty<CheckBoxStyle> colorStyleProperty() { return colorStyle; }
    public CheckBoxStyle getColorStyle() { return colorStyle.get(); }
    public void setColorStyle(CheckBoxStyle style) { this.colorStyle.set(style); }
    
    public ObjectProperty<CheckBoxSize> sizeProperty() { return size; }
    public CheckBoxSize getSize() { return size.get(); }
    public void setSize(CheckBoxSize size) { this.size.set(size); }
}

