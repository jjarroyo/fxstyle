package com.jjarroyo.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

public class JAccordionPane extends TitledPane {

    private boolean isSeparatedCard = false;
    private SVGPath rightArrow;

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

    public JAccordionPane setSeparatedCard(boolean separated) {
        this.isSeparatedCard = separated;
        if (separated) {
            if (!getStyleClass().contains("separated-card")) {
                getStyleClass().add("separated-card");
            }
            setupSeparatedCardHeader();
        } else {
            getStyleClass().remove("separated-card");
            setGraphic(null);
        }
        return this;
    }

    private void setupSeparatedCardHeader() {
        String titleText = getText();
        if (titleText != null && !titleText.isEmpty()) {
            setText(""); // Hide default text so graphic takes full width
        }

        Label titleLabel = new Label(titleText != null ? titleText : "");
        titleLabel.getStyleClass().add("separated-card-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        rightArrow = new SVGPath();
        rightArrow.setContent("M19 9l-7 7-7-7"); // Clean chevron arrow
        rightArrow.getStyleClass().add("separated-card-arrow");
        rightArrow.setStyle("-fx-stroke: #64748b; -fx-stroke-width: 2px; -fx-fill: transparent;");

        // Rotate arrow on expand / collapse
        expandedProperty().addListener((obs, oldV, isExpanded) -> {
            rightArrow.setRotate(isExpanded ? 180 : 0);
        });
        rightArrow.setRotate(isExpanded() ? 180 : 0);

        HBox header = new HBox(12, titleLabel, spacer, rightArrow);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(header, Priority.ALWAYS);

        setGraphic(header);
    }
}
