package com.jjarroyo.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Popup;
import javafx.util.Callback;

import java.util.function.Function;

public class JSelect<T> extends StackPane {

    private final ObservableList<T> items = FXCollections.observableArrayList();
    private final FilteredList<T> filteredItems;
    private final ObjectProperty<T> selectedItem = new SimpleObjectProperty<>();
    private final ObservableList<T> selectedItems = FXCollections.observableArrayList();
    
    private final BooleanProperty multiple = new SimpleBooleanProperty(false);
    private final BooleanProperty searchable = new SimpleBooleanProperty(false);
    
    private Popup popup;
    private ListView<T> listView;
    private TextField searchField;
    private Node trigger;
    private Label promptLabel;
    private HBox tagsContainer;
    
    private Function<T, String> converter = Object::toString;
    
    // Default placeholder
    private String placeholder = "Select an option";

    public JSelect() {
        getStyleClass().add("j-select");
        filteredItems = new FilteredList<>(items, p -> true);
        initGraphics();
        initPopup();
        setupEventHandlers();
    }

    private void initGraphics() {
        // Trigger area (looks like JInput)
        HBox container = new HBox();
        container.getStyleClass().add("select-trigger");
        container.setAlignment(Pos.CENTER_LEFT);
        container.setMaxWidth(Double.MAX_VALUE);
        
        // Content area (Label or Tags)
        StackPane contentPane = new StackPane();
        HBox.setHgrow(contentPane, Priority.ALWAYS);
        contentPane.setAlignment(Pos.CENTER_LEFT);
        
        promptLabel = new Label(placeholder);
        promptLabel.getStyleClass().add("select-prompt");
        
        tagsContainer = new HBox(4); // Spacing for tags
        tagsContainer.setAlignment(Pos.CENTER_LEFT);
        tagsContainer.setVisible(false);
        
        contentPane.getChildren().addAll(promptLabel, tagsContainer);
        
        // Arrow Icon
        StackPane arrowParams = new StackPane();
        SVGPath arrow = new SVGPath();
        arrow.setContent("M7 10l5 5 5-5z");
        arrow.getStyleClass().add("select-arrow");
        arrowParams.getChildren().add(arrow);
        
        container.getChildren().addAll(contentPane, arrowParams);
        trigger = container;
        getChildren().add(trigger);
    }

    private void initPopup() {
        popup = new Popup();
        popup.setAutoHide(true);
        
        VBox popupContent = new VBox();
        popupContent.getStyleClass().add("select-popup");
        
        // Search Field
        StackPane searchContainer = new StackPane();
        searchContainer.getStyleClass().add("select-search-container");
        searchContainer.setPadding(new Insets(8));
        
        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.getStyleClass().add("form-input");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterItems(newVal));
        
        searchContainer.getChildren().add(searchField);
        // Only managed if searchable
        searchContainer.managedProperty().bind(searchable);
        searchContainer.visibleProperty().bind(searchable);
        
        popupContent.getChildren().add(searchContainer);
        
        // ListView
        listView = new ListView<>(filteredItems);
        listView.getStyleClass().add("select-list");
        listView.setMaxHeight(200); // Max height constraints
        listView.setCellFactory(param -> new SelectListCell());
        
        listView.setOnMouseClicked(e -> {
            if (listView.getSelectionModel().getSelectedItem() != null) {
                handleSelection(listView.getSelectionModel().getSelectedItem());
                if (!isMultiple()) {
                    popup.hide();
                }
            }
        });
        
        popupContent.getChildren().add(listView);
        
        popup.getContent().add(popupContent);
    }

    private void setupEventHandlers() {
        trigger.setOnMouseClicked(this::togglePopup);
        
        selectedItem.addListener((obs, old, newVal) -> updateTrigger());
        selectedItems.addListener((javafx.collections.ListChangeListener.Change<? extends T> c) -> updateTrigger());
    }

    private static final javafx.css.PseudoClass SHOWING_PSEUDO_CLASS = javafx.css.PseudoClass.getPseudoClass("showing");

    private void togglePopup(MouseEvent e) {
        if (popup.isShowing()) {
            popup.hide();
        } else {
            Bounds bounds = trigger.localToScreen(trigger.getBoundsInLocal());
            
            // Sync Stylesheets
            if (getScene() != null) {
                popup.getScene().getStylesheets().setAll(getScene().getStylesheets());
                
                // Sync Dark Mode
                Node root = getScene().getRoot();
                if (root != null && root.getStyleClass().contains("dark")) {
                    if (!popup.getScene().getRoot().getStyleClass().contains("dark")) {
                        popup.getScene().getRoot().getStyleClass().add("dark");
                    }
                } else {
                    popup.getScene().getRoot().getStyleClass().remove("dark");
                }
            }

            // Match width
            // Need to set min width of popup content
            if (!popup.getContent().isEmpty()) {
                VBox content = (VBox) popup.getContent().get(0);
                content.applyCss();
                content.layout();
                
                // Extraemos el espacio (padding/insets) asignado por el CSS para la sombra
                double leftInset = content.getInsets().getLeft();
                double rightInset = content.getInsets().getRight();
                double topInset = content.getInsets().getTop();
                
                // Ajustamos el tamaño total para que el fondo blanco coincida exactamente con el iniciador
                content.setMinWidth(bounds.getWidth() + leftInset + rightInset);
                content.setPrefWidth(bounds.getWidth() + leftInset + rightInset);
                content.setMaxWidth(bounds.getWidth() + leftInset + rightInset);

                // Update pseudo-class state
                popup.setOnShown(event -> pseudoClassStateChanged(SHOWING_PSEUDO_CLASS, true));
                popup.setOnHidden(event -> pseudoClassStateChanged(SHOWING_PSEUDO_CLASS, false));

                // Restamos el inset izquierdo y superior para que quede perfectamente alineado
                // Restamos 1 adicional en Y para superponer ligeramente y anular espacios dobles de borde
                popup.show(trigger, bounds.getMinX() - leftInset-15, bounds.getMaxY() - topInset - 1);
            } else {
                popup.setOnShown(event -> pseudoClassStateChanged(SHOWING_PSEUDO_CLASS, true));
                popup.setOnHidden(event -> pseudoClassStateChanged(SHOWING_PSEUDO_CLASS, false));
                popup.show(trigger, bounds.getMinX(), bounds.getMaxY());
            }
            if (isSearchable()) {
                searchField.requestFocus();
            }
        }
    }

    private void handleSelection(T item) {
        if (isMultiple()) {
            if (selectedItems.contains(item)) {
                selectedItems.remove(item);
            } else {
                selectedItems.add(item);
            }
            // Use cell factory refesh or bindings to update checks
            listView.refresh(); 
        } else {
            setSelectedItem(item);
        }
    }

    private void updateTrigger() {
        if (isMultiple()) {
            if (selectedItems.isEmpty()) {
                promptLabel.setVisible(true);
                tagsContainer.setVisible(false);
                promptLabel.setText(placeholder);
            } else {
                promptLabel.setVisible(false);
                tagsContainer.setVisible(true);
                tagsContainer.getChildren().clear();
                
                // Show count or tags? Let's show first X tags + count
                for (T item : selectedItems) {
                    Label tag = new Label(converter.apply(item));
                    tag.getStyleClass().add("select-tag");
                    tagsContainer.getChildren().add(tag);
                }
            }
        } else {
            T item = getSelectedItem();
            tagsContainer.setVisible(false);
            promptLabel.setVisible(true);
            if (item != null) {
                promptLabel.setText(converter.apply(item));
                promptLabel.getStyleClass().remove("select-prompt"); // Make it look like value
                promptLabel.getStyleClass().add("select-value");
            } else {
                promptLabel.setText(placeholder);
                if (!promptLabel.getStyleClass().contains("select-prompt")) {
                    promptLabel.getStyleClass().add("select-prompt");
                }
                promptLabel.getStyleClass().remove("select-value");
            }
        }
    }

    private void filterItems(String query) {
        filteredItems.setPredicate(item -> {
            if (query == null || query.isEmpty()) return true;
            return converter.apply(item).toLowerCase().contains(query.toLowerCase());
        });
    }

    // --- Inner Class: Custom List Cell ---
    private class SelectListCell extends ListCell<T> {
        private final CheckBox checkBox;
        private final HBox container;
        private final Label label;

        public SelectListCell() {
            container = new HBox(8);
            container.setAlignment(Pos.CENTER_LEFT);
            
            checkBox = new CheckBox();
            checkBox.managedProperty().bind(multiple);
            checkBox.visibleProperty().bind(multiple);
            checkBox.setMouseTransparent(true); // Let cell handle clicks
            
            label = new Label();
            container.getChildren().addAll(checkBox, label);
        }

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setText(null);
            } else {
                label.setText(converter.apply(item));
                
                // Custom Icon Support check
                if (item instanceof Node) {
                     // If item itself is node, might need special handling, but usually we use cell factory for that
                     // For now, assume T is data object
                }
                
                if (isMultiple()) {
                    checkBox.setSelected(selectedItems.contains(item));
                }
                
                setGraphic(container);
            }
        }
    }

    // --- Properties ---

    public ObservableList<T> getItems() { return items; }
    
    public ObjectProperty<T> selectedItemProperty() { return selectedItem; }
    public T getSelectedItem() { return selectedItem.get(); }
    public void setSelectedItem(T item) { selectedItem.set(item); }
    
    public ObservableList<T> getSelectedItems() { return selectedItems; }

    public BooleanProperty multipleProperty() { return multiple; }
    public boolean isMultiple() { return multiple.get(); }
    public void setMultiple(boolean multiple) { this.multiple.set(multiple); }

    public BooleanProperty searchableProperty() { return searchable; }
    public boolean isSearchable() { return searchable.get(); }
    public void setSearchable(boolean searchable) { this.searchable.set(searchable); }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        updateTrigger();
    }
    
    public void setConverter(Function<T, String> converter) {
        this.converter = converter;
        updateTrigger();
    }
    
    // Allow custom cell factory for icons
    public void setCellFactory(Callback<ListView<T>, ListCell<T>> factory) {
        listView.setCellFactory(factory);
    }
}

