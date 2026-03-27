package com.jjarroyo.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;

import java.util.function.Consumer;

/**
 * JTreeView — Componente de árbol jerárquico.
 * Implementa nodos personalizables con iconos, checkboxes opcionales y diseño moderno.
 *
 * <p><b>Uso básico:</b></p>
 * <pre>
 * JTreeView&lt;String&gt; tree = new JTreeView&lt;&gt;();
 * JTreeItem&lt;String&gt; root = new JTreeItem&lt;&gt;("Raíz", JIcon.FOLDER);
 * JTreeItem&lt;String&gt; child = new JTreeItem&lt;&gt;("Documento.txt", JIcon.FILE_TEXT);
 * root.getChildren().add(child);
 * tree.setRoot(root);
 * </pre>
 */
public class JTreeView<T> extends TreeView<T> {

    private final BooleanProperty showCheckboxes = new SimpleBooleanProperty(false);
    
    // Callback para cuando se selecciona un checkbox
    private Consumer<JTreeItem<T>> onCheckAction;

    public JTreeView() {
        super();
        init();
    }

    public JTreeView(TreeItem<T> root) {
        super(root);
        init();
    }

    private void init() {
        getStyleClass().add("j-tree-view");
        setShowRoot(true);
        
        // Custom Cell Factory
        setCellFactory(tv -> new JTreeCell());
    }

    public boolean isShowCheckboxes() {
        return showCheckboxes.get();
    }

    public BooleanProperty showCheckboxesProperty() {
        return showCheckboxes;
    }

    public void setShowCheckboxes(boolean showCheckboxes) {
        this.showCheckboxes.set(showCheckboxes);
    }

    public void setOnCheckAction(Consumer<JTreeItem<T>> onCheckAction) {
        this.onCheckAction = onCheckAction;
    }

    public Consumer<JTreeItem<T>> getOnCheckAction() {
        return onCheckAction;
    }

    // =========================================================================
    // INNER CLASS: JTreeCell
    // =========================================================================

    private class JTreeCell extends TreeCell<T> {

        private final HBox container;
        private final StackPane expandIconWrapper;
        private final SVGPath expandIcon;
        private final StackPane nodeIconWrapper;
        private final SVGPath nodeIcon;
        private final JCheckBox checkBox;
        private final JLabel label;

        private static final PseudoClass EXPANDED_PSEUDO_CLASS = PseudoClass.getPseudoClass("expanded");

        public JTreeCell() {
            getStyleClass().add("j-tree-cell-root");

            container = new HBox(8);
            container.setAlignment(Pos.CENTER_LEFT);
            container.getStyleClass().add("j-tree-cell-content");

            // Expand / Collapse chevron
            expandIcon = new SVGPath();
            expandIcon.setContent(JIcon.CHEVRON_RIGHT.getPath());
            expandIcon.getStyleClass().add("j-tree-expand-icon");
            
            expandIconWrapper = new StackPane(expandIcon);
            expandIconWrapper.getStyleClass().add("j-tree-expand-wrapper");
            expandIconWrapper.setCursor(Cursor.HAND);
            expandIconWrapper.setOnMouseClicked(e -> {
                TreeItem<T> item = getTreeItem();
                if (item != null && !item.isLeaf()) {
                    item.setExpanded(!item.isExpanded());
                    e.consume(); // Prevent selection
                }
            });

            // Node Icon (Folder, File, etc.)
            nodeIcon = new SVGPath();
            nodeIcon.getStyleClass().add("j-tree-node-icon");
            nodeIconWrapper = new StackPane(nodeIcon);
            nodeIconWrapper.getStyleClass().add("j-tree-icon-wrapper");

            // CheckBox
            checkBox = new JCheckBox();
            checkBox.getStyleClass().add("j-tree-checkbox");
            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                TreeItem<T> item = getTreeItem();
                if (item instanceof JTreeItem) {
                    JTreeItem<T> jItem = (JTreeItem<T>) item;
                    jItem.setChecked(newVal);
                    if (onCheckAction != null) {
                        onCheckAction.accept(jItem);
                    }
                }
            });

            // Text Label
            label = new JLabel("");
            label.getStyleClass().add("j-tree-label");
            HBox.setHgrow(label, Priority.ALWAYS);

            container.getChildren().addAll(expandIconWrapper, checkBox, nodeIconWrapper, label);
            
            // Sync checkbox visibility with TreeView property
            checkBox.managedProperty().bind(showCheckboxes);
            checkBox.visibleProperty().bind(showCheckboxes);
        }

        private BooleanProperty boundProperty = null;

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                setText(null);
                pseudoClassStateChanged(EXPANDED_PSEUDO_CLASS, false);
            } else {
                TreeItem<T> treeItem = getTreeItem();
                
                // Set text
                label.setText(item.toString());
                
                // Configure Expand Icon
                if (treeItem.isLeaf()) {
                    expandIconWrapper.setVisible(false);
                } else {
                    expandIconWrapper.setVisible(true);
                    pseudoClassStateChanged(EXPANDED_PSEUDO_CLASS, treeItem.isExpanded());
                }

                // Configure Node Icon
                if (treeItem instanceof JTreeItem) {
                    JTreeItem<T> jItem = (JTreeItem<T>) treeItem;
                    if (jItem.getIcon() != null) {
                        nodeIcon.setContent(jItem.getIcon().getPath());
                        nodeIconWrapper.setVisible(true);
                        nodeIconWrapper.setManaged(true);
                    } else {
                        nodeIconWrapper.setVisible(false);
                        nodeIconWrapper.setManaged(false);
                    }

                    // Properly manage binding to avoid memory leaks
                    if (boundProperty != null) {
                        checkBox.selectedProperty().unbindBidirectional(boundProperty);
                    }
                    boundProperty = jItem.checkedProperty();
                    checkBox.selectedProperty().bindBidirectional(boundProperty);
                } else {
                    nodeIconWrapper.setVisible(false);
                    nodeIconWrapper.setManaged(false);
                    if (boundProperty != null) {
                        checkBox.selectedProperty().unbindBidirectional(boundProperty);
                        boundProperty = null;
                    }
                }

                setGraphic(container);
                setText(null); // We use our own label inside the graphic
            }
        }
    }
}
