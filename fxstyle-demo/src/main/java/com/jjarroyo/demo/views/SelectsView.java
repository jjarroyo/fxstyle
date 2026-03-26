package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JSelect;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.util.Callback;

public class SelectsView extends ScrollPane {

    public SelectsView() {
        getStyleClass().add("scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));

        VBox container = new VBox(24);
        container.setMaxWidth(Double.MAX_VALUE);
        container.getStyleClass().add("container");

        container.getChildren().add(
            new JLabel("Selects").addClass("text-2xl", "text-slate-800", "font-bold")
        );

        // 1. Simple Select
        JSelect<String> simpleSelect = new JSelect<>();
        simpleSelect.getItems().addAll("Option 1", "Option 2", "Option 3", "Option 4");
        simpleSelect.setPlaceholder("Choose an option");
        
        JCard simpleCard = new JCard("Simple Select", new VBox(16,
            new JLabel("Basic dropdown functionality."),
            simpleSelect
        ));

        // 2. Searchable Select
        JSelect<String> searchSelect = new JSelect<>();
        searchSelect.getItems().addAll("Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grape");
        searchSelect.setSearchable(true);
        searchSelect.setPlaceholder("Pick a fruit...");
        
        JCard searchCard = new JCard("Searchable Select", new VBox(16,
            new JLabel("Dropdown with filter search box."),
            searchSelect
        ));

        // 3. Multiple Select
        JSelect<String> multiSelect = new JSelect<>();
        multiSelect.getItems().addAll("React", "Angular", "Vue", "Svelte", "JavaFX", "Swing");
        multiSelect.setMultiple(true);
        multiSelect.setPlaceholder("Select frameworks");
        
        JCard multiCard = new JCard("Multiple Select", new VBox(16,
            new JLabel("Multiple selection with badges."),
            multiSelect
        ));

        // 4. Icon Select (Custom Cell Factory)
        JSelect<JIcon> iconSelect = new JSelect<>();
        iconSelect.getItems().addAll(JIcon.values());
        // Use only a subset to keep it clean if needed, but let's try mostly all or 10-20
        // Optimizing list:
        iconSelect.getItems().clear();
        iconSelect.getItems().addAll(
            JIcon.HOME, JIcon.SETTINGS, JIcon.PERSON, JIcon.STAR, 
            JIcon.HEART, JIcon.WARNING, JIcon.CHECK, JIcon.INFO
        );
        iconSelect.setPlaceholder("Select an icon");
        
        // Custom Cell Factory for Icons
        iconSelect.setCellFactory(param -> new ListCell<JIcon>() {
            private final HBox container = new HBox(12);
            private final SVGPath icon = new SVGPath();
            private final Label label = new Label();
            
            {
                container.setAlignment(Pos.CENTER_LEFT);
                icon.setScaleX(0.8);
                icon.setScaleY(0.8);
                container.getChildren().addAll(icon, label);
            }
            
            @Override
            protected void updateItem(JIcon item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    icon.setContent(item.getPath());
                    // Style icon based on selection or defaults
                    icon.setStyle("-fx-fill: -color-slate-600;");
                    
                    label.setText(item.name());
                    label.setStyle("-fx-text-fill: -color-slate-700; -fx-font-weight: 600;");
                    
                    setGraphic(container);
                }
            }
        });
        
        JCard iconCard = new JCard("Icon Select", new VBox(16,
            new JLabel("Dropdown with custom icon rendering."),
            iconSelect
        ));

        container.getChildren().addAll(simpleCard, searchCard, multiCard, iconCard);
        setContent(container);
    }
}


