package com.jjarroyo.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDesignCanvas extends StackPane {

    private final Pane labelSurface;
    private final VBox labelMedia;
    private final Map<Integer, StackPane> slots = new HashMap<>();
    private final StackPane barcodeArea = new StackPane();
    private double zoomScale = 1.0;
    private final Scale scaleTransform = new Scale(1, 1, 0, 0);

    public JDesignCanvas() {
        init();
        
        labelSurface = new Pane();
        labelSurface.getStyleClass().add("j-design-label-surface");
        
        // Create a visual "Label" that looks like physical media
        labelMedia = new VBox(15); 
        labelMedia.getStyleClass().add("label-media");
        labelMedia.setAlignment(Pos.TOP_CENTER);
        labelMedia.setPrefSize(420, 320);
        labelMedia.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        labelMedia.setPadding(new Insets(25));
        
        // Premium look: Pure white with soft border and specific shadow
        labelMedia.setStyle("-fx-background-color: white; " +
                           "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 30, 0, 0, 15); " +
                           "-fx-background-radius: 16; " +
                           "-fx-border-color: #e2e8f0; " +
                           "-fx-border-width: 1; " +
                           "-fx-border-radius: 16;");
        
        // Setup Barcode Area
        barcodeArea.setPrefHeight(100); 
        barcodeArea.getStyleClass().add("barcode-drop-zone");
        barcodeArea.getChildren().add(createPlaceholder("BARCODE PLACEMENT", "Drop info field here"));
        setupReordering(barcodeArea);

        // Center labelMedia and apply scale
        StackPane canvasWrapper = new StackPane(labelMedia);
        canvasWrapper.setAlignment(Pos.CENTER);
        labelMedia.getTransforms().add(scaleTransform);
        
        // Pivot to center
        scaleTransform.pivotXProperty().bind(labelMedia.widthProperty().divide(2));
        scaleTransform.pivotYProperty().bind(labelMedia.heightProperty().divide(2));

        getChildren().add(canvasWrapper);
        
        setupDropHandling();
    }

    public void setSupportedLines(int count) {
        labelMedia.getChildren().clear();
        slots.clear();
        
        for (int i = 0; i < count; i++) {
            StackPane slot = createSlot("TEXT LINE " + (i + 1), "Drop info field here");
            slots.put(i, slot);
            labelMedia.getChildren().add(slot);
        }
        
        // Always add barcode area at the end by default
        labelMedia.getChildren().add(barcodeArea);
    }

    private StackPane createSlot(String title, String subtitle) {
        StackPane slot = new StackPane();
        slot.setPrefHeight(60);
        slot.getStyleClass().add("canvas-slot");
        slot.setStyle("-fx-border-color: #cbd5e1; -fx-border-style: dashed; -fx-border-width: 1.5; -fx-border-radius: 10;");
        
        slot.getChildren().add(createPlaceholder(title, subtitle));
        setupReordering(slot);
        return slot;
    }

    private VBox createPlaceholder(String title, String subtitle) {
        VBox text = new VBox(2);
        text.setAlignment(Pos.CENTER);
        JLabel t = new JLabel(title);
        t.addClass("text-xs", "font-bold", "text-slate-400");
        JLabel s = new JLabel(subtitle);
        s.addClass("text-2xs", "text-slate-300");
        text.getChildren().addAll(t, s);
        return text;
    }

    private void init() {
        getStyleClass().add("j-design-canvas");
        setPadding(new Insets(40));
        
        // Background: "Studio" look with subtle grid or plain light gray
        setStyle("-fx-background-color: #f8fafc;");
    }

    private void setupDropHandling() {
        setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasString()) {
                // If it's a reorder operation, don't accept it here (handled individually)
                if (event.getDragboard().hasString() && event.getDragboard().getString().startsWith("_REORDER_")) {
                    return;
                }
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        setOnDragEntered(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasString()) {
                getStyleClass().add("drag-over");
            }
            event.consume();
        });

        setOnDragExited(event -> {
            getStyleClass().remove("drag-over");
            event.consume();
        });

        setOnDragDropped(event -> {
            boolean success = false;
            if (event.getDragboard().hasString()) {
                String fieldId = event.getDragboard().getString();
                if (fieldId.startsWith("_REORDER_")) return;

                // Check if mouse is over barcodeArea
                javafx.geometry.Point2D localPoint = labelMedia.sceneToLocal(event.getSceneX(), event.getSceneY());
                
                if (barcodeArea.getBoundsInParent().contains(localPoint)) {
                    updateBarcodeArea(fieldId);
                    success = true;
                } else {
                    for (StackPane slot : slots.values()) {
                        if (slot.getBoundsInParent().contains(localPoint)) {
                             updateSlot(slot, fieldId);
                             success = true;
                             break;
                        }
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setupReordering(StackPane slot) {
        slot.setOnDragDetected(event -> {
            Dragboard db = slot.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("_REORDER_" + labelMedia.getChildren().indexOf(slot));
            db.setContent(content);
            slot.setOpacity(0.5);
            event.consume();
        });

        slot.setOnDragOver(event -> {
            if (event.getGestureSource() != slot && event.getDragboard().hasString()) {
                String data = event.getDragboard().getString();
                if (data.startsWith("_REORDER_")) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                } else {
                    // Accept field drops from sidebar
                    event.acceptTransferModes(TransferMode.COPY);
                    event.consume();
                }
            }
        });

        slot.setOnDragEntered(event -> {
            if (event.getGestureSource() != slot && event.getDragboard().hasString()) {
                slot.setStyle(slot.getStyle() + "-fx-border-color: -color-primary; -fx-background-color: rgba(59, 130, 246, 0.05);");
                event.consume();
            }
        });

        slot.setOnDragExited(event -> {
            // Restore styles
            if (slot == barcodeArea && barcodeArea.getChildren().get(0) instanceof VBox && !(barcodeArea.getChildren().get(0) instanceof JLabel)) {
                 slot.setStyle("-fx-border-color: #cbd5e1; -fx-border-style: dashed; -fx-border-width: 1.5; -fx-border-radius: 10;");
            } else if (slot != barcodeArea && slot.getChildren().size() > 0 && !(slot.getChildren().get(0) instanceof VBox)) {
                 slot.setStyle("-fx-border-color: transparent; -fx-background-color: rgba(59, 130, 246, 0.05); -fx-background-radius: 10;");
            } else {
                 slot.setStyle("-fx-border-color: #cbd5e1; -fx-border-style: dashed; -fx-border-width: 1.5; -fx-border-radius: 10;");
            }
            event.consume();
        });

        slot.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                String data = db.getString();
                if (data.startsWith("_REORDER_")) {
                    int sourceIndex = Integer.parseInt(data.replace("_REORDER_", ""));
                    int targetIndex = labelMedia.getChildren().indexOf(slot);
                    
                    List<javafx.scene.Node> nodes = new ArrayList<>(labelMedia.getChildren());
                    javafx.scene.Node sourceNode = nodes.get(sourceIndex);
                    nodes.set(sourceIndex, nodes.get(targetIndex));
                    nodes.set(targetIndex, sourceNode);
                    
                    labelMedia.getChildren().setAll(nodes);
                    event.setDropCompleted(true);
                } else {
                    // Handle field drop directly on slot
                    if (slot == barcodeArea) {
                        updateBarcodeArea(data);
                    } else {
                        updateSlot(slot, data);
                    }
                    event.setDropCompleted(true);
                }
            }
            event.consume();
        });

        slot.setOnDragDone(event -> {
            slot.setOpacity(1.0);
            event.consume();
        });
    }

    private void updateSlot(StackPane slot, String fieldId) {
        slot.getChildren().clear();
        slot.setStyle("-fx-border-color: transparent; -fx-background-color: rgba(59, 130, 246, 0.05); -fx-background-radius: 10;");
        
        JLabel fieldLabel = new JLabel(fieldId.toUpperCase());
        fieldLabel.addClass("text-lg", "font-bold", "text-slate-800");
        slot.getChildren().add(fieldLabel);
    }

    private void updateBarcodeArea(String fieldId) {
        barcodeArea.getChildren().clear();
        barcodeArea.setStyle("-fx-background-color: transparent;");
        
        VBox visualBarcode = new VBox(5);
        visualBarcode.setAlignment(Pos.CENTER);
        visualBarcode.setPadding(new Insets(10));
        visualBarcode.setStyle("-fx-background-color: white; -fx-border-color: -color-border-default; -fx-border-radius: 4; -fx-padding: 10;");
        
        // Visual representation of a barcode
        HBox lines = new HBox(2);
        lines.setAlignment(Pos.CENTER);
        for(int i=0; i<30; i++) {
            Region line = new Region();
            line.setPrefWidth(Math.random() > 0.5 ? 2 : 4);
            line.setPrefHeight(40);
            line.setStyle("-fx-background-color: black;");
            lines.getChildren().add(line);
        }
        
        JLabel sub = new JLabel(fieldId.toUpperCase());
        sub.addClass("text-2xs", "text-slate-500", "font-bold");
        
        visualBarcode.getChildren().addAll(lines, sub);
        barcodeArea.getChildren().add(visualBarcode);
    }
    
    public void zoomIn() {
        zoomScale += 0.1;
        if (zoomScale > 2.0) zoomScale = 2.0;
        updateScale();
    }

    public void zoomOut() {
        zoomScale -= 0.1;
        if (zoomScale < 0.5) zoomScale = 0.5;
        updateScale();
    }

    private void updateScale() {
        scaleTransform.setX(zoomScale);
        scaleTransform.setY(zoomScale);
    }

    public Map<String, Object> getDesignData() {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, String>> slotsConfig = new ArrayList<>();
        
        for (javafx.scene.Node node : labelMedia.getChildren()) {
            if (node instanceof StackPane) {
                StackPane slot = (StackPane) node;
                Map<String, String> config = new HashMap<>();
                
                if (slot == barcodeArea) {
                    config.put("type", "barcode");
                    // Check if assigned
                    if (barcodeArea.getChildren().get(0) instanceof VBox && ((VBox)barcodeArea.getChildren().get(0)).getChildren().get(1) instanceof JLabel) {
                        JLabel sub = (JLabel) ((VBox)barcodeArea.getChildren().get(0)).getChildren().get(1);
                        config.put("fieldId", sub.getText().toLowerCase());
                    }
                } else {
                    config.put("type", "text");
                    // Check if assigned (updateSlot puts a JLabel directly)
                    if (slot.getChildren().size() > 0 && slot.getChildren().get(0) instanceof JLabel) {
                        JLabel label = (JLabel) slot.getChildren().get(0);
                        config.put("fieldId", label.getText().toLowerCase());
                    }
                }
                slotsConfig.add(config);
            }
        }
        
        data.put("slots", slotsConfig);
        return data;
    }

    public void reset() {
        labelMedia.getChildren().clear();
        barcodeArea.getChildren().clear();
        barcodeArea.getChildren().add(createPlaceholder("BARCODE PLACEMENT", "Drop info field here"));
        // restore default state? usually depends on the current type, but we can just clear visuals
        slots.clear();
        zoomScale = 1.0;
        updateScale();
    }
}
