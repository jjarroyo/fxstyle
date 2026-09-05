package com.jjarroyo.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class JInput extends TextField {

    private boolean isProgrammaticAutocompleteChange = false;
    private ContextMenu autocompleteContextMenu;

    public JInput() {
        super();
        init();
    }

    public JInput(String promptText) {
        super();
        setPromptText(promptText);
        init();
    }

    private void init() {
        getStyleClass().add("form-input");
    }

    public JInput addClass(String... styleClasses) {
        getStyleClass().addAll(styleClasses);
        return this;
    }

    /**
     * Embeds a right-side node (like a link button or icon) inside the input container,
     * maintaining full modern height (48px) and fluid HBox growth.
     */
    public StackPane createWithRightNode(Node rightNode, double rightOffsetPadding) {
        StackPane container = new StackPane();
        container.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(container, Priority.ALWAYS);
        container.setMinHeight(48);
        container.setPrefHeight(48);

        this.setMinHeight(48);
        this.setPrefHeight(48);
        this.setMaxWidth(Double.MAX_VALUE);

        // Padding para que el texto no solape el botón/icono derecho, asegurando 48px de alto
        this.setStyle("-fx-padding: 0 " + (int) rightOffsetPadding + "px 0 16px; -fx-min-height: 48px; -fx-pref-height: 48px;");

        StackPane.setAlignment(rightNode, Pos.CENTER_RIGHT);
        StackPane.setMargin(rightNode, new Insets(0, 12, 0, 0));

        container.getChildren().addAll(this, rightNode);
        return container;
    }

    public StackPane createWithRightAction(Node actionNode) {
        return createWithRightNode(actionNode, 80);
    }

    public StackPane createWithRightIcon(JIcon icon) {
        Node iconView = icon.view("#94a3b8");
        iconView.setMouseTransparent(true);
        return createWithRightNode(iconView, 40);
    }

    /**
     * Creates a VBox containing a Label (with optional required mark) and this Input.
     */
    public VBox createWithLabel(String labelText, boolean required) {
        VBox container = new VBox();
        container.setSpacing(4);

        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");
        
        if (required) {
            Label req = new Label("*");
            req.getStyleClass().add("required-mark");
            HBox labelBox = new HBox(label, req);
            labelBox.setAlignment(Pos.CENTER_LEFT);
            container.getChildren().add(labelBox);
        } else {
            container.getChildren().add(label);
        }

        container.getChildren().add(this);
        return container;
    }

    /**
     * Sets the validation status of the input.
     * Valid values: "form-input-success", "form-input-danger", "form-input-warning", "form-input-dark"
     * Passing null or empty string removes all validation styles.
     */
    public JInput setStatus(String styleClass) {
        getStyleClass().removeAll("form-input-success", "form-input-danger", "form-input-warning", "form-input-dark");
        if (styleClass != null && !styleClass.isEmpty()) {
            getStyleClass().add(styleClass);
        }
        return this;
    }

    /**
     * Applies the modern style with more padding and rounded corners.
     */
    public JInput setModern(boolean modern) {
        if (modern) {
            if (!getStyleClass().contains("form-input-modern")) {
                getStyleClass().add("form-input-modern");
            }
        } else {
            getStyleClass().remove("form-input-modern");
        }
        return this;
    }

    /**
     * Habilita el comportamiento de Autocomplete dinámico en JInput.
     * Se activa cuando la longitud del texto tipeado es >= minQueryLength (por defecto 3).
     *
     * @param minQueryLength Cantidad mínima de caracteres para realizar la búsqueda (ej. 3)
     * @param searchProvider Función lambda que realiza la búsqueda en base de datos / Eloquent (ej. query -> repository.search(query))
     * @param displayMapper Función lambda opcional para dar formato al texto mostrado del elemento
     * @param onSelected Callback invocado cuando el usuario selecciona un elemento del menú desplegable
     */
    public <T> void setAutocomplete(
            int minQueryLength,
            Function<String, List<T>> searchProvider,
            Function<T, String> displayMapper,
            Consumer<T> onSelected
    ) {
        if (autocompleteContextMenu == null) {
            autocompleteContextMenu = new ContextMenu();
            autocompleteContextMenu.getStyleClass().add("j-autocomplete-popup");
        }

        this.textProperty().addListener((obs, oldVal, newVal) -> {
            if (isProgrammaticAutocompleteChange) {
                return;
            }

            if (newVal == null || newVal.trim().length() < minQueryLength) {
                autocompleteContextMenu.hide();
                return;
            }

            String query = newVal.trim();
            List<T> results;
            try {
                results = searchProvider.apply(query);
            } catch (Exception e) {
                results = Collections.emptyList();
            }

            autocompleteContextMenu.getItems().clear();

            if (results == null || results.isEmpty()) {
                MenuItem noResultItem = new MenuItem("No se encontraron resultados para: \"" + query + "\"");
                noResultItem.setDisable(true);
                autocompleteContextMenu.getItems().add(noResultItem);
            } else {
                int limit = Math.min(results.size(), 15);
                for (int i = 0; i < limit; i++) {
                    T item = results.get(i);
                    String displayText = (displayMapper != null) ? displayMapper.apply(item) : String.valueOf(item);

                    MenuItem menuItem = new MenuItem(displayText);
                    menuItem.setOnAction(evt -> {
                        isProgrammaticAutocompleteChange = true;
                        this.setText(displayText);
                        this.positionCaret(displayText.length());
                        isProgrammaticAutocompleteChange = false;
                        autocompleteContextMenu.hide();
                        if (onSelected != null) {
                            onSelected.accept(item);
                        }
                    });
                    autocompleteContextMenu.getItems().add(menuItem);
                }
            }

            if (!autocompleteContextMenu.isShowing() && getScene() != null && getScene().getWindow() != null) {
                autocompleteContextMenu.show(this, Side.BOTTOM, 0, 0);
            }
        });
    }

    public <T> void setAutocomplete(
            Function<String, List<T>> searchProvider,
            Function<T, String> displayMapper,
            Consumer<T> onSelected
    ) {
        setAutocomplete(3, searchProvider, displayMapper, onSelected);
    }
}
