package com.jjarroyo.components;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.scene.control.ListCell;

public class JList<T> extends VBox {

    private final ListView<T> listView;
    private final JPagination pagination;
    private Runnable onScrollBottom;
    private boolean infiniteScrollEnabled = false;

    public JList() {
        getStyleClass().add("j-list-wrapper");
        
        // ListView
        listView = new ListView<>();
        listView.getStyleClass().add("j-list-view");
        VBox.setVgrow(listView, Priority.ALWAYS);

        // Pagination
        pagination = new JPagination();
        
        getChildren().addAll(listView, pagination);
        
        // Listen for scroll
        listView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                // Find vertical scroll bar
                listView.lookupAll(".scroll-bar").stream()
                    .filter(node -> node instanceof ScrollBar)
                    .map(node -> (ScrollBar) node)
                    .filter(sb -> sb.getOrientation() == Orientation.VERTICAL)
                    .findFirst()
                    .ifPresent(sb -> {
                        sb.valueProperty().addListener((o, oldVal, newVal) -> {
                            // Trigger when near bottom (90% scroll)
                            if (infiniteScrollEnabled && newVal.doubleValue() >= sb.getMax() * 0.9) {
                                if (onScrollBottom != null) onScrollBottom.run();
                            }
                        });
                    });
            }
        });
    }
    
    // Set cell factory wrapper to inject style class
    public void setCellFactory(Callback<ListView<T>, ListCell<T>> value) {
        listView.setCellFactory(param -> {
            ListCell<T> cell = value.call(param);
            cell.getStyleClass().add("j-list-card-cell");
            return cell;
        });
    }
    
    public ListView<T> getListView() { return listView; }
    public JPagination getPagination() { return pagination; }
    
    public void setItems(ObservableList<T> items) {
        listView.setItems(items);
    }
    
    public void setOnScrollBottom(Runnable action) {
        this.onScrollBottom = action;
        this.infiniteScrollEnabled = true;
    }
    
    public void setPaginationVisible(boolean visible) {
        pagination.setVisible(visible);
        pagination.setManaged(visible);
    }
    
    public void addItem(T item) {
        listView.getItems().add(item);
    }
}

