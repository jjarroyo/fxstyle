package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JNumberInput;
import com.jjarroyo.components.JToast;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NumberInputView extends ScrollPane {

    public NumberInputView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));
        setFitToWidth(true);
        setContent(content);

        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Number Input")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Input numérico con botones + y − para incrementar/decrementar")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        content.getChildren().add(new JCard("Básico", createBasic()));
        content.getChildren().add(new JCard("Con Límites (Min/Max)", createMinMax()));
        content.getChildren().add(new JCard("Step Personalizado", createStep()));
        content.getChildren().add(new JCard("Decimales", createDecimals()));
        content.getChildren().add(new JCard("Con Callback", createCallback()));
    }

    private javafx.scene.Node createBasic() {
        VBox container = new VBox(12);
        JNumberInput input = new JNumberInput();
        input.setValue(0);

        javafx.scene.control.Label hint = new javafx.scene.control.Label("Usa los botones + / − o escribe directamente");
        hint.setStyle("-fx-font-size: 11px; -fx-text-fill: -color-slate-400;");

        container.getChildren().addAll(input, hint);
        return container;
    }

    private javafx.scene.Node createMinMax() {
        VBox container = new VBox(12);

        HBox row = new HBox(16);

        VBox col1 = new VBox(4);
        javafx.scene.control.Label l1 = new javafx.scene.control.Label("Cantidad (0-10)");
        l1.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: -color-slate-600;");
        JNumberInput input1 = new JNumberInput();
        input1.setValue(5).setMin(0).setMax(10);
        col1.getChildren().addAll(l1, input1);

        VBox col2 = new VBox(4);
        javafx.scene.control.Label l2 = new javafx.scene.control.Label("Edad (18-99)");
        l2.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: -color-slate-600;");
        JNumberInput input2 = new JNumberInput();
        input2.setValue(25).setMin(18).setMax(99);
        col2.getChildren().addAll(l2, input2);

        row.getChildren().addAll(col1, col2);
        container.getChildren().add(row);
        return container;
    }

    private javafx.scene.Node createStep() {
        VBox container = new VBox(12);
        HBox row = new HBox(16);

        VBox col1 = new VBox(4);
        javafx.scene.control.Label l1 = new javafx.scene.control.Label("Step: 5");
        l1.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: -color-slate-600;");
        JNumberInput input1 = new JNumberInput();
        input1.setValue(0).setStep(5).setMin(0).setMax(100);
        col1.getChildren().addAll(l1, input1);

        VBox col2 = new VBox(4);
        javafx.scene.control.Label l2 = new javafx.scene.control.Label("Step: 10");
        l2.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: -color-slate-600;");
        JNumberInput input2 = new JNumberInput();
        input2.setValue(50).setStep(10).setMin(0).setMax(100);
        col2.getChildren().addAll(l2, input2);

        row.getChildren().addAll(col1, col2);
        container.getChildren().add(row);
        return container;
    }

    private javafx.scene.Node createDecimals() {
        VBox container = new VBox(12);

        javafx.scene.control.Label label = new javafx.scene.control.Label("Precio (step 0.5)");
        label.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: -color-slate-600;");
        JNumberInput input = new JNumberInput();
        input.setValue(9.5).setStep(0.5).setMin(0).setMax(999).setIntegerOnly(false);

        container.getChildren().addAll(label, input);
        return container;
    }

    private javafx.scene.Node createCallback() {
        VBox container = new VBox(12);

        javafx.scene.control.Label label = new javafx.scene.control.Label("Cambia el valor:");
        label.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: -color-slate-600;");

        javafx.scene.control.Label display = new javafx.scene.control.Label("Valor actual: 0");
        display.setStyle("-fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: -color-primary-600;");

        JNumberInput input = new JNumberInput();
        input.setValue(0).setMin(-50).setMax(50);
        input.setOnValueChanged(val -> {
            display.setText("Valor actual: " + val.intValue());
        });

        container.getChildren().addAll(label, input, display);
        return container;
    }
}
