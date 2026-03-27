package com.jjarroyo.components;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class JInputGroup extends HBox {

    public JInputGroup() {
        getStyleClass().add("input-group");
        setSpacing(0); // Borders must touch
        
        // Listen to children changes to update classes
        getChildren().addListener((ListChangeListener<Node>) c -> updateStyles());
    }

    public void add(Node... nodes) {
        getChildren().addAll(nodes);
    }

    private void updateStyles() {
        int size = getChildren().size();
        for (int i = 0; i < size; i++) {
            Node node = getChildren().get(i);
            
            // Remove previous group classes
            node.getStyleClass().removeAll("input-group-left", "input-group-center", "input-group-right");
            
            // Standardize styles for Buttons and Labels in groups if needed
            // For now, assume user passes styled JInput or JButton.
            // But we need to ensure borders merge correctly.
            // CSS handles border-radius and border-width based on these classes.
            
            if (i == 0) {
                node.getStyleClass().add("input-group-left");
            } else if (i == size - 1) {
                node.getStyleClass().add("input-group-right");
            } else {
                node.getStyleClass().add("input-group-center");
            }
        }
    }
}

