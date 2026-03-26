package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JDatePicker;
import com.jjarroyo.components.JLabel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import java.time.LocalDate;

public class DatePickersView extends ScrollPane {

    public DatePickersView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));
        
        setFitToWidth(true);
        setContent(content);
        
        // Page Title
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Date Pickers")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Selección de fechas con estilo moderno")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        
        content.getChildren().add(pageHeader);
        
        // Basic
        content.getChildren().add(new JCard("Date Picker Básico", createBasicDatePicker()));
        
        // Pre-selected
        content.getChildren().add(new JCard("Fecha Preseleccionada", createPreselectedDatePicker()));
        
        // Disabled
        content.getChildren().add(new JCard("Deshabilitado", createDisabledDatePicker()));
    }

    private javafx.scene.Node createBasicDatePicker() {
        VBox container = new VBox(10);
        Label label = new Label("Seleccionar fecha");
        label.getStyleClass().add("form-label");
        
        JDatePicker datePicker = new JDatePicker();
        datePicker.setPromptText("dd/mm/yyyy");
        datePicker.setMaxWidth(Double.MAX_VALUE);
        
        container.getChildren().addAll(label, datePicker);
        return container;
    }

    private javafx.scene.Node createPreselectedDatePicker() {
        VBox container = new VBox(10);
        Label label = new Label("Fecha de nacimiento");
        label.getStyleClass().add("form-label");
        
        JDatePicker datePicker = new JDatePicker(LocalDate.now());
        datePicker.setMaxWidth(Double.MAX_VALUE);
        
        container.getChildren().addAll(label, datePicker);
        return container;
    }
    
    private javafx.scene.Node createDisabledDatePicker() {
        VBox container = new VBox(10);
        Label label = new Label("No editable");
        label.getStyleClass().add("form-label");
        
        JDatePicker datePicker = new JDatePicker(LocalDate.of(2025, 1, 1));
        datePicker.setDisable(true);
        datePicker.setMaxWidth(Double.MAX_VALUE);
        
        container.getChildren().addAll(label, datePicker);
        return container;
    }
}
