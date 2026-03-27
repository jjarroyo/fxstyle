package com.jjarroyo.components;

import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

public class JAccordion extends Accordion {

    public JAccordion() {
        super();
        init();
    }

    public JAccordion(TitledPane... panes) {
        super(panes);
        init();
    }

    private void init() {
        getStyleClass().add("j-accordion");
    }
}
