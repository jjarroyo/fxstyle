package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JToast;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ToastsView extends ScrollPane {

    public ToastsView() {
        getStyleClass().add("j-scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));

        VBox content = new VBox();
        content.setSpacing(24);
        
        // Header
        VBox header = new VBox(8);
        Label title = new Label("Toasts");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Floating feedback messages with various positions and styles.");
        subtitle.getStyleClass().add("page-subtitle");
        header.getChildren().addAll(title, subtitle);
        content.getChildren().add(header);

        // Demo Card
        JCard demoCard = new JCard("Toast Playground", "Test different toast configurations");
        
        VBox cardBody = new VBox(24);
        cardBody.setPadding(new Insets(24));
        
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        
        // Inputs
        TextField titleInput = new TextField("Notification");
        TextField msgInput = new TextField("Action completed successfully!");
        TextField durInput = new TextField("3000");
        
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleInput, 1, 0);
        grid.add(new Label("Message:"), 0, 1);
        grid.add(msgInput, 1, 1);
        grid.add(new Label("Duration (ms):"), 0, 2);
        grid.add(durInput, 1, 2);
        
        cardBody.getChildren().add(grid);
        
        // Buttons
        HBox btnBox1 = new HBox(12);
        btnBox1.getChildren().add(createBtn("Top Left (Success)", () -> show(titleInput, msgInput, durInput, JToast.Type.SUCCESS, JToast.Position.TOP_LEFT)));
        btnBox1.getChildren().add(createBtn("Top Center (Info)", () -> show(titleInput, msgInput, durInput, JToast.Type.INFO, JToast.Position.TOP_CENTER)));
        btnBox1.getChildren().add(createBtn("Top Right (Danger)", () -> show(titleInput, msgInput, durInput, JToast.Type.DANGER, JToast.Position.TOP_RIGHT)));
        
        HBox btnBox2 = new HBox(12);
        btnBox2.getChildren().add(createBtn("Bottom Left (Warning)", () -> show(titleInput, msgInput, durInput, JToast.Type.WARNING, JToast.Position.BOTTOM_LEFT)));
        btnBox2.getChildren().add(createBtn("Bottom Center (Default)", () -> show(titleInput, msgInput, durInput, JToast.Type.DEFAULT, JToast.Position.BOTTOM_CENTER)));
        btnBox2.getChildren().add(createBtn("Bottom Right (Success)", () -> show(titleInput, msgInput, durInput, JToast.Type.SUCCESS, JToast.Position.BOTTOM_RIGHT)));
        
        cardBody.getChildren().addAll(btnBox1, btnBox2);
        
        demoCard.setBody(cardBody);
        content.getChildren().add(demoCard);
        
        setContent(content);
    }
    
    private Button createBtn(String text, Runnable action) {
        Button btn = new Button(text);
        btn.getStyleClass().add("btn");
        btn.getStyleClass().add("btn-light");
        btn.setOnAction(e -> action.run());
        return btn;
    }
    
    private void show(TextField t, TextField m, TextField d, JToast.Type type, JToast.Position pos) {
        String title = t.getText();
        String msg = m.getText();
        int dur = 3000;
        try { dur = Integer.parseInt(d.getText()); } catch (Exception e) {}
        
        new JToast().show(getScene().getWindow(), title, msg, type, pos, Duration.millis(dur));
    }
}


