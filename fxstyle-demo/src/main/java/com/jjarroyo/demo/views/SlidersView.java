package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JSlider;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;

public class SlidersView extends ScrollPane {

    public SlidersView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));
        
        setFitToWidth(true);
        setContent(content);
        
        // Page Title
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Sliders")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Rango de valores con estilo personalizado")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        
        content.getChildren().add(pageHeader);
        
        // Basic Slider
        content.getChildren().add(new JCard("Slider Básico", createBasicSlider()));
        
        // Slider with Value
        content.getChildren().add(new JCard("Slider con Valor", createSliderWithValue()));
        
        // Disabled Slider
        content.getChildren().add(new JCard("Estado Deshabilitado", createDisabledSlider()));
    }

    private javafx.scene.Node createBasicSlider() {
        VBox container = new VBox(20);
        
        Label label = new Label("Volumen");
        label.getStyleClass().add("form-label");
        
        JSlider slider = new JSlider(0, 100, 50);
        
        container.getChildren().addAll(label, slider);
        return container;
    }

    private javafx.scene.Node createSliderWithValue() {
        VBox container = new VBox(20);
        
        HBox header = new HBox(10);
        Label label = new Label("Progreso");
        label.getStyleClass().add("form-label");
        
        Label valueLabel = new Label("30%");
        valueLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -color-primary-500;");
        
        header.getChildren().addAll(label, valueLabel);
        
        JSlider slider = new JSlider(0, 100, 30);
        slider.valueProperty().addListener((obs, old, val) -> {
            valueLabel.setText(String.format("%.0f%%", val.doubleValue()));
        });
        
        container.getChildren().addAll(header, slider);
        return container;
    }
    
    private javafx.scene.Node createDisabledSlider() {
        VBox container = new VBox(20);
        
        Label label = new Label("Deshabilitado");
        label.getStyleClass().add("form-label");
        
        JSlider slider = new JSlider(0, 100, 75);
        slider.setDisable(true);
        
        container.getChildren().addAll(label, slider);
        return container;
    }
}
