package com.jjarroyo.components;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class JPagination extends HBox {

    private final IntegerProperty currentPage = new SimpleIntegerProperty(1);
    private final IntegerProperty totalPages = new SimpleIntegerProperty(1);
    private Runnable onPageChange;

    public JPagination() {
        getStyleClass().add("j-pagination");
        updateButtons();
        
        currentPage.addListener((obs, old, newVal) -> updateButtons());
        totalPages.addListener((obs, old, newVal) -> updateButtons());
    }

    private void updateButtons() {
        getChildren().clear();
        
        int current = currentPage.get();
        int total = totalPages.get();
        
        // Prev Button
        Button prev = createButton("<");
        prev.setDisable(current <= 1);
        prev.setOnAction(e -> {
            if (current > 1) {
                currentPage.set(current - 1);
                triggerChange();
            }
        });
        getChildren().add(prev);

        // Page Numbers (Simple range for demo: 1 ... current ... total)
        // A robust implementation would handle ranges like: 1 2 ... 5 6 7 ... 10
        
        if (total <= 7) {
            for (int i = 1; i <= total; i++) {
                addPageButton(i, current);
            }
        } else {
             // Always show first
             addPageButton(1, current);
             
             if (current > 3) {
                 getChildren().add(createEllipsis());
             }
             
             int start = Math.max(2, current - 1);
             int end = Math.min(total - 1, current + 1);
             
             for (int i = start; i <= end; i++) {
                 addPageButton(i, current);
             }
             
             if (current < total - 2) {
                 getChildren().add(createEllipsis());
             }
             
             // Always show last
             addPageButton(total, current);
        }

        // Next Button
        Button next = createButton(">");
        next.setDisable(current >= total);
        next.setOnAction(e -> {
            if (current < total) {
                currentPage.set(current + 1);
                triggerChange();
            }
        });
        getChildren().add(next);
    }
    
    private void addPageButton(int pageNum, int current) {
        Button btn = createButton(String.valueOf(pageNum));
        if (pageNum == current) {
            btn.getStyleClass().add("active");
        }
        btn.setOnAction(e -> {
            if (pageNum != current) {
                currentPage.set(pageNum);
                triggerChange();
            }
        });
        getChildren().add(btn);
    }
    
    private Button createEllipsis() {
        Button btn = createButton("...");
        btn.setDisable(true);
        btn.getStyleClass().add("disabled");
        return btn;
    }

    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("j-pagination-btn");
        return btn;
    }

    private void triggerChange() {
        if (onPageChange != null) onPageChange.run();
    }

    public IntegerProperty currentPageProperty() { return currentPage; }
    public IntegerProperty totalPagesProperty() { return totalPages; }
    public void setOnPageChange(Runnable action) { this.onPageChange = action; }
}

