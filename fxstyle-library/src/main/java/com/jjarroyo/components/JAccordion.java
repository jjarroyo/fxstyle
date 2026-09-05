package com.jjarroyo.components;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class JAccordion extends VBox {

    private final ObservableList<TitledPane> panes = FXCollections.observableArrayList();
    private boolean isSeparatedCards = false;
    private boolean independentPanes = false;

    public JAccordion() {
        super();
        init();
    }

    public JAccordion(TitledPane... initialPanes) {
        super();
        init();
        if (initialPanes != null) {
            panes.addAll(initialPanes);
        }
    }

    private void init() {
        getStyleClass().add("j-accordion");
        setSpacing(0);

        panes.addListener((ListChangeListener<TitledPane>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (TitledPane pane : change.getAddedSubList()) {
                        setupPaneListeners(pane);
                        if (!getChildren().contains(pane)) {
                            getChildren().add(pane);
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (TitledPane pane : change.getRemoved()) {
                        getChildren().remove(pane);
                    }
                }
            }
            if (isSeparatedCards) {
                applySeparatedCardsStyle();
            }
        });
    }

    private void setupPaneListeners(TitledPane pane) {
        if (pane instanceof JAccordionPane jPane && isSeparatedCards) {
            jPane.setSeparatedCard(true);
        }

        pane.expandedProperty().addListener((obs, oldVal, isExpanded) -> {
            if (isExpanded && !independentPanes) {
                // Modo único: colapsar otros paneles
                for (TitledPane other : panes) {
                    if (other != pane) {
                        other.setExpanded(false);
                    }
                }
            }
        });
    }

    public ObservableList<TitledPane> getPanes() {
        return panes;
    }

    public JAccordion setSeparatedCards(boolean separated) {
        this.isSeparatedCards = separated;
        if (separated) {
            if (!getStyleClass().contains("separated-cards")) {
                getStyleClass().add("separated-cards");
            }
        } else {
            getStyleClass().remove("separated-cards");
        }
        applySeparatedCardsStyle();
        return this;
    }

    public JAccordion setIndependentPanes(boolean independent) {
        this.independentPanes = independent;
        return this;
    }

    public JAccordion setMultipleExpanded(boolean allowMultiple) {
        return setIndependentPanes(allowMultiple);
    }

    public boolean isIndependentPanes() {
        return independentPanes;
    }

    public void setExpandedPane(TitledPane pane) {
        if (pane != null) {
            pane.setExpanded(true);
        }
    }

    private void applySeparatedCardsStyle() {
        for (TitledPane pane : panes) {
            if (pane instanceof JAccordionPane jPane) {
                jPane.setSeparatedCard(isSeparatedCards);
            } else if (isSeparatedCards && !pane.getStyleClass().contains("separated-card")) {
                pane.getStyleClass().add("separated-card");
            }
        }
    }
}
