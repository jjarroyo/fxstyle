package com.jjarroyo.components;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * JSearchInput — Input de búsqueda con autocomplete/sugerencias.
 *
 * <pre>
 * JSearchInput search = new JSearchInput();
 * search.setPromptText("Buscar...");
 * search.setSuggestionProvider(query -> fetchResults(query));
 * search.setOnSearch(query -> System.out.println("Buscando: " + query));
 * </pre>
 */
public class JSearchInput extends HBox {

    private final TextField textField;
    private final javafx.scene.Node searchIcon;
    private final javafx.scene.Node clearBtn;

    private final Popup popup;
    private final VBox suggestionsBox;
    private final ScrollPane popupScroll;

    private Function<String, List<String>> suggestionProvider;
    private Consumer<String> onSearch;
    private boolean clearable = true;

    public JSearchInput() {
        getStyleClass().add("j-search-input");
        setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Search icon
        searchIcon = JIcon.SEARCH.view();
        searchIcon.getStyleClass().add("j-search-icon");

        // Text field
        textField = new TextField();
        textField.getStyleClass().add("j-search-field");
        HBox.setHgrow(textField, Priority.ALWAYS);

        // Clear button
        clearBtn = JIcon.CLOSE.view();
        clearBtn.getStyleClass().add("j-search-clear");
        clearBtn.setVisible(false);
        clearBtn.setManaged(false);
        clearBtn.setOnMouseClicked(e -> {
            textField.clear();
            textField.requestFocus();
            hidePopup();
        });

        getChildren().addAll(searchIcon, textField, clearBtn);

        // Popup for autocomplete
        popup = new Popup();
        popup.setAutoHide(true);
        
        suggestionsBox = new VBox();
        suggestionsBox.getStyleClass().add("j-search-popup-box");
        
        popupScroll = new ScrollPane(suggestionsBox);
        popupScroll.getStyleClass().add("j-search-popup-scroll");
        popupScroll.setFitToWidth(true);
        popupScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        popupScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        popup.getContent().add(popupScroll);

        // Event listeners
        textField.textProperty().addListener((obs, old, newVal) -> {
            updateClearBtn();
            handleTextChange(newVal);
        });

        textField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                commitSearch(textField.getText());
            } else if (e.getCode() == KeyCode.ESCAPE) {
                hidePopup();
            } else if (e.getCode() == KeyCode.DOWN) {
                if (popup.isShowing() && !suggestionsBox.getChildren().isEmpty()) {
                    suggestionsBox.getChildren().get(0).requestFocus();
                } else if (!popup.isShowing()) {
                    handleTextChange(textField.getText()); // try open
                }
            }
        });

        // Close popup when focus lost
        textField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused && !popup.isFocused()) {
                // Pequeño delay para permitir que el clic seleccione sugerencia
                javafx.application.Platform.runLater(() -> {
                    if (!suggestionsBox.isHover()) {
                        hidePopup();
                    }
                });
            }
        });
    }

    // ─── API ─────────────────────────────────────────────────────────────────────

    public JSearchInput setPromptText(String text) {
        textField.setPromptText(text);
        return this;
    }

    public String getText() {
        return textField.getText();
    }

    public JSearchInput setText(String text) {
        textField.setText(text);
        return this;
    }

    public JSearchInput setClearable(boolean clearable) {
        this.clearable = clearable;
        updateClearBtn();
        return this;
    }

    /** Set dynamic suggestion provider based on current text */
    public JSearchInput setSuggestionProvider(Function<String, List<String>> provider) {
        this.suggestionProvider = provider;
        return this;
    }

    /** Set static list of suggestions */
    public JSearchInput setSuggestions(List<String> staticSuggestions) {
        this.suggestionProvider = query -> {
            if (query == null || query.isBlank()) return staticSuggestions;
            String lower = query.toLowerCase();
            return staticSuggestions.stream()
                .filter(s -> s.toLowerCase().contains(lower))
                .toList();
        };
        return this;
    }

    public JSearchInput setOnSearch(Consumer<String> callback) {
        this.onSearch = callback;
        return this;
    }

    public TextField getTextField() {
        return textField;
    }

    // ─── Internal ────────────────────────────────────────────────────────────────

    private void updateClearBtn() {
        boolean show = clearable && !textField.getText().isEmpty();
        clearBtn.setVisible(show);
        clearBtn.setManaged(show);
    }

    private void handleTextChange(String text) {
        if (suggestionProvider == null) return;

        List<String> results = suggestionProvider.apply(text);
        if (results == null || results.isEmpty()) {
            hidePopup();
            return;
        }

        buildSuggestionsUI(results);
        showPopup();
    }

    private void buildSuggestionsUI(List<String> results) {
        suggestionsBox.getChildren().clear();
        for (String result : results) {
            Label item = new Label(result);
            item.getStyleClass().add("j-search-popup-item");
            item.setMaxWidth(Double.MAX_VALUE);
            item.setFocusTraversable(true);

            item.setOnMouseClicked(e -> {
                commitSearch(result);
            });
            
            item.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    commitSearch(result);
                } else if (e.getCode() == KeyCode.ESCAPE) {
                    hidePopup();
                    textField.requestFocus();
                } else if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.UP) {
                    int idx = suggestionsBox.getChildren().indexOf(item);
                    if (e.getCode() == KeyCode.DOWN && idx < suggestionsBox.getChildren().size() - 1) {
                        suggestionsBox.getChildren().get(idx + 1).requestFocus();
                    } else if (e.getCode() == KeyCode.UP && idx > 0) {
                        suggestionsBox.getChildren().get(idx - 1).requestFocus();
                    } else if (e.getCode() == KeyCode.UP && idx == 0) {
                        textField.requestFocus();
                    }
                }
            });

            suggestionsBox.getChildren().add(item);
        }
    }

    private void showPopup() {
        if (getScene() == null || getScene().getWindow() == null) return;
        
        Window window = getScene().getWindow();
        Bounds bounds = localToScreen(getBoundsInLocal());
        
        popupScroll.setPrefWidth(bounds.getWidth());
        popupScroll.setMaxHeight(250); // limit popup height
        
        if (!popup.isShowing()) {
            popup.show(window, bounds.getMinX(), bounds.getMaxY() + 4);
        }
    }

    private void hidePopup() {
        if (popup.isShowing()) {
            popup.hide();
        }
    }

    private void commitSearch(String query) {
        hidePopup();
        textField.setText(query);
        textField.positionCaret(query.length());
        if (onSearch != null) {
            onSearch.accept(query);
        }
    }
}
