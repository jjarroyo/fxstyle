package com.jjarroyo.components;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;

public class JAccordionPane extends TitledPane {

    public JAccordionPane() {
        super();
        init();
    }

    public JAccordionPane(String title, Node content) {
        super(title, content);
        init();
    }

    private void init() {
        getStyleClass().add("j-accordion-pane");
        setAnimated(true); // Smooth animation by default
    }
}
