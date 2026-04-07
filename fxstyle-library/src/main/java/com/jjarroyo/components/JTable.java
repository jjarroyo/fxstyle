package com.jjarroyo.components;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import java.util.stream.Collectors;

/**
 * JTable — Enterprise-grade table component with checkboxes, search/filter,
 * row actions, custom cell renderers, bulk actions, CSV export, inline editing and more.
 *
 * <pre>
 * JTable&lt;User&gt; table = new JTable&lt;&gt;();
 * table.setCheckable(true);
 * table.setSearchable(true);
 * table.setStriped(true);
 * table.addColumn("Name", "name");
 * table.addColumn("Role", "role", user -&gt; new JChip(user.getRole()));
 * table.setRowActions((user, box) -&gt; { ... });
 * table.setItems(data);
 *
 * // Editable mode (DataGrid):
 * table.setEditable(true);
 * table.addEditableColumn("Name", "name", (user, val) -&gt; user.nameProperty().set(val));
 * </pre>
 */
public class JTable<T> extends VBox {

    // ─── Core ────────────────────────────────────────────────────────────────────
    private final TableView<T> tableView;
    private final JPagination pagination;
    private Runnable onScrollBottom;
    private boolean infiniteScrollEnabled = false;

    private ObservableList<T> allItems = FXCollections.observableArrayList();
    private ObservableList<T> filteredItems = FXCollections.observableArrayList();
    private final IntegerProperty itemsPerPage = new SimpleIntegerProperty(10);

    // ─── Checkable ───────────────────────────────────────────────────────────────
    private boolean checkable = false;
    private final ObservableList<T> checkedItems = FXCollections.observableArrayList();
    private Consumer<List<T>> onCheckChange;
    private CheckBox headerCheckBox;
    private TableColumn<T, Boolean> checkColumn;
    private boolean updatingHeaderCheckBox = false;

    // ─── Search ──────────────────────────────────────────────────────────────────
    private boolean searchable = false;
    private TextField searchField;
    private HBox searchBar;
    private HBox filterBar;
    private String searchPlaceholder = "Buscar...";
    private final List<String> searchProperties = new ArrayList<>();

    // ─── Row Actions ─────────────────────────────────────────────────────────────
    private BiConsumer<T, HBox> rowActionsFactory;
    private TableColumn<T, Void> actionsColumn;

    // ─── Row Click ───────────────────────────────────────────────────────────────
    private Consumer<T> onRowClick;

    // ─── Bulk Actions ────────────────────────────────────────────────────────────
    private HBox bulkBar;
    private Label bulkLabel;
    private Consumer<List<T>> onBulkDelete;
    private boolean bulkActionsEnabled = false;

    // ─── Empty State ─────────────────────────────────────────────────────────────
    private String emptyText = "No hay datos disponibles";
    private Node emptyGraphic;

    // ─── Status Bar ──────────────────────────────────────────────────────────────
    private boolean statusBarEnabled = false;
    private Label statusLabel;
    private HBox statusBar;

    // ─── Editable Mode (DataGrid) ────────────────────────────────────────────────
    private boolean editable = false;
    private BiConsumer<T, String> onCellEdit;

    // ─── Visual Modes ────────────────────────────────────────────────────────────
    private boolean striped = false;
    private boolean dense = false;

    // ─── Column info for search & export ─────────────────────────────────────────
    private final List<ColumnInfo> columnInfos = new ArrayList<>();

    private static class ColumnInfo {
        String title;
        String property;
        ColumnInfo(String title, String property) {
            this.title = title;
            this.property = property;
        }
    }

    // ─── Items listener ──────────────────────────────────────────────────────────
    private final ListChangeListener<T> itemsListener = c -> {
        applyFilter();
    };

    // ═══════════════════════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════════════════════

    public JTable() {
        getStyleClass().add("j-table-wrapper");

        // TableView
        tableView = new TableView<>();
        tableView.getStyleClass().add("j-table-view");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // Empty state placeholder
        updateEmptyState();

        // Pagination
        pagination = new JPagination();
        pagination.setOnPageChange(this::updateTableData);

        getChildren().addAll(tableView, pagination);

        // Row click handler
        tableView.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && onRowClick != null) {
                    onRowClick.accept(row.getItem());
                }
            });
            return row;
        });

        // Scroll bottom listener
        tableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                tableView.lookupAll(".scroll-bar").stream()
                    .filter(node -> node instanceof ScrollBar)
                    .map(node -> (ScrollBar) node)
                    .filter(sb -> sb.getOrientation() == Orientation.VERTICAL)
                    .findFirst()
                    .ifPresent(sb -> {
                        sb.valueProperty().addListener((o, oldVal, newVal) -> {
                            if (infiniteScrollEnabled && newVal.doubleValue() == sb.getMax()) {
                                if (onScrollBottom != null) onScrollBottom.run();
                            }
                        });
                    });
            }
        });
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // CORE ACCESSORS
    // ═══════════════════════════════════════════════════════════════════════════════

    public TableView<T> getTableView() { return tableView; }

    /** @deprecated Use getTableView() instead */
    @Deprecated
    public TableView<T> getLastTable() { return tableView; }

    public JPagination getPagination() { return pagination; }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 1. CHECKABLE MODE
    // ═══════════════════════════════════════════════════════════════════════════════

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
        if (checkable) {
            setupCheckColumn();
        } else {
            removeCheckColumn();
        }
    }

    public boolean isCheckable() { return checkable; }

    private void setupCheckColumn() {
        if (checkColumn != null) return;

        checkColumn = new TableColumn<>();
        checkColumn.setSortable(false);
        checkColumn.setResizable(false);
        checkColumn.setMinWidth(45);
        checkColumn.setMaxWidth(45);
        checkColumn.setPrefWidth(45);
        checkColumn.getStyleClass().add("j-table-checkbox-col");

        // Header checkbox
        headerCheckBox = new JCheckBox();
        headerCheckBox.setOnAction(e -> {
            if (updatingHeaderCheckBox) return;
            boolean selected = headerCheckBox.isSelected();
            ObservableList<T> currentPageItems = tableView.getItems();
            if (selected) {
                for (T item : currentPageItems) {
                    if (!checkedItems.contains(item)) {
                        checkedItems.add(item);
                    }
                }
            } else {
                checkedItems.removeAll(currentPageItems);
            }
            tableView.refresh();
            notifyCheckChange();
            updateBulkBar();
        });
        checkColumn.setGraphic(headerCheckBox);

        // Cell factory
        checkColumn.setCellFactory(col -> new TableCell<T, Boolean>() {
            private final JCheckBox cb = new JCheckBox();
            {
                cb.setOnAction(e -> {
                    T item = getTableRow() != null ? getTableRow().getItem() : null;
                    if (item == null) return;
                    if (cb.isSelected()) {
                        if (!checkedItems.contains(item)) {
                            checkedItems.add(item);
                        }
                    } else {
                        checkedItems.remove(item);
                    }
                    updateHeaderCheckBoxState();
                    notifyCheckChange();
                    updateBulkBar();
                });
            }

            @Override
            protected void updateItem(Boolean val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    cb.setSelected(checkedItems.contains(getTableRow().getItem()));
                    setGraphic(cb);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        tableView.getColumns().add(0, checkColumn);
        setupBulkBar();
    }

    private void removeCheckColumn() {
        if (checkColumn != null) {
            tableView.getColumns().remove(checkColumn);
            checkColumn = null;
            headerCheckBox = null;
            checkedItems.clear();
            removeBulkBar();
        }
    }

    private void updateHeaderCheckBoxState() {
        if (headerCheckBox == null) return;
        updatingHeaderCheckBox = true;
        ObservableList<T> currentPageItems = tableView.getItems();
        if (currentPageItems.isEmpty()) {
            headerCheckBox.setSelected(false);
            headerCheckBox.setIndeterminate(false);
        } else {
            boolean allChecked = checkedItems.containsAll(currentPageItems);
            boolean noneChecked = currentPageItems.stream().noneMatch(checkedItems::contains);
            if (allChecked) {
                headerCheckBox.setIndeterminate(false);
                headerCheckBox.setSelected(true);
            } else if (noneChecked) {
                headerCheckBox.setIndeterminate(false);
                headerCheckBox.setSelected(false);
            } else {
                headerCheckBox.setIndeterminate(true);
            }
        }
        updatingHeaderCheckBox = false;
    }

    private void notifyCheckChange() {
        if (onCheckChange != null) {
            onCheckChange.accept(new ArrayList<>(checkedItems));
        }
    }

    public ObservableList<T> getCheckedItems() { return checkedItems; }

    public List<Integer> getCheckedIndices() {
        return checkedItems.stream()
            .map(allItems::indexOf)
            .filter(i -> i >= 0)
            .collect(Collectors.toList());
    }

    public void clearChecks() {
        checkedItems.clear();
        if (headerCheckBox != null) {
            headerCheckBox.setSelected(false);
            headerCheckBox.setIndeterminate(false);
        }
        tableView.refresh();
        updateBulkBar();
    }

    public void setOnCheckChange(Consumer<List<T>> callback) {
        this.onCheckChange = callback;
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 2. SORTING CONTROL
    // ═══════════════════════════════════════════════════════════════════════════════

    /** Add a column with title and property name. Sortable by default. */
    public <V> TableColumn<T, V> addColumn(String title, String property) {
        return addColumn(title, property, true);
    }

    /** Add a column with control over sortability. */
    public <V> TableColumn<T, V> addColumn(String title, String property, boolean sortable) {
        TableColumn<T, V> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setSortable(sortable);
        tableView.getColumns().add(col);
        searchProperties.add(property);
        columnInfos.add(new ColumnInfo(title, property));
        return col;
    }

    /** Add a column with a custom cell renderer. */
    public <V> TableColumn<T, V> addColumn(String title, String property, Function<T, Node> renderer) {
        return addColumn(title, property, true, renderer);
    }

    /** Add a column with custom renderer and sortability control. */
    public <V> TableColumn<T, V> addColumn(String title, String property, boolean sortable, Function<T, Node> renderer) {
        TableColumn<T, V> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setSortable(sortable);

        col.setCellFactory(column -> new TableCell<T, V>() {
            @Override
            protected void updateItem(V item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    T rowItem = getTableRow().getItem();
                    Node node = renderer.apply(rowItem);
                    setGraphic(node);
                    setText(null);
                }
            }
        });

        tableView.getColumns().add(col);
        searchProperties.add(property);
        columnInfos.add(new ColumnInfo(title, property));
        return col;
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 2b. EDITABLE COLUMNS (DataGrid Mode)
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * Enable or disable inline cell editing (DataGrid mode).
     * When enabled, columns added via addEditableColumn() will allow double-click editing.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
        tableView.setEditable(editable);
        if (editable) {
            tableView.getStyleClass().add("j-table-editable");
        } else {
            tableView.getStyleClass().remove("j-table-editable");
        }
    }

    public boolean isEditable() { return editable; }

    /**
     * Callback fired when any cell is edited. Receives the row item and the column property name.
     */
    public void setOnCellEdit(BiConsumer<T, String> handler) {
        this.onCellEdit = handler;
    }

    /**
     * Add an editable text column. Double-click to edit, Enter to commit, Escape to cancel.
     *
     * @param title    Column header text
     * @param property JavaFX bean property name (for PropertyValueFactory)
     * @param setter   Lambda to write the new value back to the model: (item, newValue) -> ...
     */
    public TableColumn<T, String> addEditableColumn(String title, String property, BiConsumer<T, String> setter) {
        return addEditableColumn(title, property, setter, true);
    }

    /**
     * Add an editable text column with sortability control.
     */
    public TableColumn<T, String> addEditableColumn(String title, String property, BiConsumer<T, String> setter, boolean sortable) {
        TableColumn<T, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setSortable(sortable);
        col.setEditable(true);

        col.setCellFactory(column -> new EditableTextCell<>(setter, property, this));

        tableView.getColumns().add(col);
        searchProperties.add(property);
        columnInfos.add(new ColumnInfo(title, property));
        return col;
    }

    /**
     * Add an editable column with a dropdown (ComboBox) for predefined options.
     *
     * @param title    Column header text
     * @param property JavaFX bean property name
     * @param setter   Lambda to write the new value back to the model
     * @param options  Available options for the dropdown
     */
    public TableColumn<T, String> addEditableColumn(String title, String property, BiConsumer<T, String> setter, String... options) {
        TableColumn<T, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setSortable(true);
        col.setEditable(true);

        col.setCellFactory(column -> new EditableComboCell<>(setter, property, this, options));

        tableView.getColumns().add(col);
        searchProperties.add(property);
        columnInfos.add(new ColumnInfo(title, property));
        return col;
    }

    // ─── Editable Text Cell ───────────────────────────────────────────────────────

    private static class EditableTextCell<T> extends TableCell<T, String> {
        private TextField textField;
        private final BiConsumer<T, String> setter;
        private final String property;
        private final JTable<T> table;

        EditableTextCell(BiConsumer<T, String> setter, String property, JTable<T> table) {
            this.setter = setter;
            this.property = property;
            this.table = table;
            getStyleClass().add("j-table-editable-cell");
        }

        @Override
        public void startEdit() {
            if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) return;
            super.startEdit();

            if (textField == null) {
                textField = new TextField(getItem() != null ? getItem() : "");
                textField.getStyleClass().add("j-table-edit-field");

                // Commit on Enter
                textField.setOnAction(e -> commitEdit(textField.getText()));

                // Cancel on Escape
                textField.setOnKeyPressed(e -> {
                    if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                    // Tab to next cell
                    if (e.getCode() == javafx.scene.input.KeyCode.TAB) {
                        commitEdit(textField.getText());
                        // Move focus to next column
                        TableColumn<T, ?> nextCol = getNextColumn(!e.isShiftDown());
                        if (nextCol != null) {
                            getTableView().edit(getTableRow().getIndex(), nextCol);
                        }
                    }
                });

                // Cancel on focus lost
                textField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                    if (!isFocused && isEditing()) {
                        commitEdit(textField.getText());
                    }
                });
            }

            textField.setText(getItem() != null ? getItem() : "");
            setText(null);
            setGraphic(textField);
            textField.selectAll();
            textField.requestFocus();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem());
            setGraphic(null);
        }

        @Override
        public void commitEdit(String newValue) {
            super.commitEdit(newValue);
            T item = getTableRow() != null ? getTableRow().getItem() : null;
            if (item != null && setter != null) {
                setter.accept(item, newValue);
                if (table.onCellEdit != null) {
                    table.onCellEdit.accept(item, property);
                }
            }
            setText(newValue);
            setGraphic(null);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else if (isEditing()) {
                if (textField != null) {
                    textField.setText(item != null ? item : "");
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(item);
                setGraphic(null);
            }
        }

        @SuppressWarnings("unchecked")
        private TableColumn<T, ?> getNextColumn(boolean forward) {
            var columns = getTableView().getVisibleLeafColumns();
            if (columns.size() < 2) return null;
            int currentIndex = columns.indexOf(getTableColumn());
            int nextIndex = currentIndex + (forward ? 1 : -1);
            if (nextIndex < 0 || nextIndex >= columns.size()) return null;
            return (TableColumn<T, ?>) columns.get(nextIndex);
        }
    }

    // ─── Editable ComboBox Cell ───────────────────────────────────────────────────

    private static class EditableComboCell<T> extends TableCell<T, String> {
        private ComboBox<String> comboBox;
        private final BiConsumer<T, String> setter;
        private final String property;
        private final JTable<T> table;
        private final String[] options;

        EditableComboCell(BiConsumer<T, String> setter, String property, JTable<T> table, String... options) {
            this.setter = setter;
            this.property = property;
            this.table = table;
            this.options = options;
            getStyleClass().add("j-table-editable-cell");
        }

        @Override
        public void startEdit() {
            if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) return;
            super.startEdit();

            if (comboBox == null) {
                comboBox = new ComboBox<>(FXCollections.observableArrayList(options));
                comboBox.getStyleClass().add("j-table-edit-combo");
                comboBox.setMaxWidth(Double.MAX_VALUE);

                comboBox.setOnAction(e -> {
                    String selected = comboBox.getValue();
                    if (selected != null) {
                        commitEdit(selected);
                    }
                });

                comboBox.setOnKeyPressed(e -> {
                    if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                });
            }

            comboBox.setValue(getItem());
            setText(null);
            setGraphic(comboBox);
            comboBox.show();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem());
            setGraphic(null);
        }

        @Override
        public void commitEdit(String newValue) {
            super.commitEdit(newValue);
            T item = getTableRow() != null ? getTableRow().getItem() : null;
            if (item != null && setter != null) {
                setter.accept(item, newValue);
                if (table.onCellEdit != null) {
                    table.onCellEdit.accept(item, property);
                }
            }
            setText(newValue);
            setGraphic(null);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else if (isEditing()) {
                if (comboBox != null) {
                    comboBox.setValue(item);
                }
                setText(null);
                setGraphic(comboBox);
            } else {
                setText(item);
                setGraphic(null);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 3. SEARCH / FILTER BAR
    // ═══════════════════════════════════════════════════════════════════════════════

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
        if (searchable) {
            setupSearchBar();
        } else {
            removeSearchBar();
        }
    }

    public boolean isSearchable() { return searchable; }

    public void setSearchPlaceholder(String placeholder) {
        this.searchPlaceholder = placeholder;
        if (searchField != null) {
            searchField.setPromptText(placeholder);
        }
    }

    private void setupSearchBar() {
        if (searchBar != null) return;

        searchField = new TextField();
        searchField.setPromptText(searchPlaceholder);
        searchField.getStyleClass().add("j-table-search-field");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        // Icono de búsqueda
        Label searchIcon = new Label("\uD83D\uDD0D");
        searchIcon.getStyleClass().add("j-table-search-icon");

        searchBar = new HBox(8);
        searchBar.getStyleClass().add("j-table-search");
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.getChildren().addAll(searchIcon, searchField);
        searchBar.setMaxWidth(Double.MAX_VALUE);
        searchBar.setPadding(new Insets(8, 0, 0, 0));

        // Filter bar for custom buttons on the right
        filterBar = new HBox(8);
        filterBar.setAlignment(Pos.CENTER_RIGHT);
        filterBar.getStyleClass().add("j-table-filter-bar");

        // Wrapper: search (left 50%) + filter bar (right 50%)
        HBox searchWrapper = new HBox(12);
        searchWrapper.getStyleClass().add("j-table-search-wrapper");
        HBox.setHgrow(searchBar, Priority.ALWAYS);
        HBox.setHgrow(filterBar, Priority.ALWAYS);
       // searchWrapper.setPadding(new Insets(8, 0, 0, 0));
        searchWrapper.getChildren().addAll(searchBar, filterBar);

        // Filter on text change
        searchField.textProperty().addListener((obs, old, newVal) -> {
            applyFilter();
        });

        getChildren().add(0, searchWrapper);
    }

    /** Returns the filter bar HBox where custom filter buttons can be added. */
    public HBox getFilterBar() {
        return filterBar;
    }

    private void removeSearchBar() {
        if (searchBar != null) {
            // Remove the wrapper (parent of searchBar)
            getChildren().removeIf(n -> n.getStyleClass().contains("j-table-search-wrapper"));
            searchBar = null;
            searchField = null;
            filterBar = null;
            applyFilter();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 4. ROW ACTIONS COLUMN
    // ═══════════════════════════════════════════════════════════════════════════════

    public void setRowActions(BiConsumer<T, HBox> actionsFactory) {
        this.rowActionsFactory = actionsFactory;
        setupActionsColumn();
    }

    private void setupActionsColumn() {
        if (actionsColumn != null) {
            tableView.getColumns().remove(actionsColumn);
        }

        actionsColumn = new TableColumn<>("Acciones");
        actionsColumn.setSortable(false);
        actionsColumn.setMinWidth(120);
        actionsColumn.getStyleClass().add("j-table-actions-col");

        actionsColumn.setCellFactory(col -> new TableCell<T, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(6);
                    box.setAlignment(Pos.CENTER);
                    rowActionsFactory.accept(getTableRow().getItem(), box);
                    setGraphic(box);
                }
            }
        });

        tableView.getColumns().add(actionsColumn);
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 5. EMPTY STATE
    // ═══════════════════════════════════════════════════════════════════════════════

    public void setEmptyText(String text) {
        this.emptyText = text;
        updateEmptyState();
    }

    public void setEmptyGraphic(Node graphic) {
        this.emptyGraphic = graphic;
        updateEmptyState();
    }

    private void updateEmptyState() {
        VBox emptyBox = new VBox(12);
        emptyBox.setAlignment(Pos.CENTER);
        emptyBox.getStyleClass().add("j-table-empty-state");

        if (emptyGraphic != null) {
            emptyBox.getChildren().add(emptyGraphic);
        } else {
            Label icon = new Label("📋");
            icon.setStyle("-fx-font-size: 36px;");
            emptyBox.getChildren().add(icon);
        }

        Label label = new Label(emptyText);
        label.getStyleClass().add("j-table-empty-text");
        emptyBox.getChildren().add(label);

        tableView.setPlaceholder(emptyBox);
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 6. VISUAL MODES
    // ═══════════════════════════════════════════════════════════════════════════════

    public void setStriped(boolean striped) {
        this.striped = striped;
        if (striped) {
            tableView.getStyleClass().add("j-table-striped");
        } else {
            tableView.getStyleClass().remove("j-table-striped");
        }
    }

    public boolean isStriped() { return striped; }

    public void setDense(boolean dense) {
        this.dense = dense;
        if (dense) {
            tableView.getStyleClass().add("j-table-dense");
        } else {
            tableView.getStyleClass().remove("j-table-dense");
        }
    }

    public boolean isDense() { return dense; }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 7. ROW CLICK
    // ═══════════════════════════════════════════════════════════════════════════════

    public void setOnRowClick(Consumer<T> handler) {
        this.onRowClick = handler;
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 8. BULK ACTIONS TOOLBAR
    // ═══════════════════════════════════════════════════════════════════════════════

    public void setBulkActionsEnabled(boolean enabled) {
        this.bulkActionsEnabled = enabled;
        if (enabled && checkable) {
            setupBulkBar();
        } else {
            removeBulkBar();
        }
    }

    public void setOnBulkDelete(Consumer<List<T>> handler) {
        this.onBulkDelete = handler;
        this.bulkActionsEnabled = true;
        if (checkable) setupBulkBar();
    }

    private void setupBulkBar() {
        if (bulkBar != null) return;

        bulkBar = new HBox(12);
        bulkBar.getStyleClass().add("j-table-bulk-bar");
        bulkBar.setAlignment(Pos.CENTER_LEFT);
        bulkBar.setVisible(false);
        bulkBar.setManaged(false);

        bulkLabel = new Label("0 seleccionados");
        bulkLabel.getStyleClass().add("j-table-bulk-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button deselectBtn = new Button("Deseleccionar todo");
        deselectBtn.getStyleClass().addAll("j-btn", "btn-sm", "btn-secondary");
        deselectBtn.setOnAction(e -> clearChecks());

        bulkBar.getChildren().addAll(bulkLabel, spacer, deselectBtn);

        if (onBulkDelete != null) {
            Button deleteBtn = new Button("🗑 Eliminar");
            deleteBtn.getStyleClass().addAll("j-btn", "btn-sm", "btn-danger");
            deleteBtn.setOnAction(e -> {
                if (onBulkDelete != null) {
                    onBulkDelete.accept(new ArrayList<>(checkedItems));
                }
            });
            bulkBar.getChildren().add(deleteBtn);
        }

        // Insert before tableView
        int tableIdx = getChildren().indexOf(tableView);
        getChildren().add(tableIdx, bulkBar);
    }

    private void removeBulkBar() {
        if (bulkBar != null) {
            getChildren().remove(bulkBar);
            bulkBar = null;
            bulkLabel = null;
        }
    }

    private void updateBulkBar() {
        if (bulkBar == null || !bulkActionsEnabled) return;
        int count = checkedItems.size();
        boolean visible = count > 0;
        bulkBar.setVisible(visible);
        bulkBar.setManaged(visible);
        if (bulkLabel != null) {
            bulkLabel.setText(count + " seleccionado" + (count != 1 ? "s" : ""));
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 9. STATUS BAR
    // ═══════════════════════════════════════════════════════════════════════════════

    public void setStatusBarEnabled(boolean enabled) {
        this.statusBarEnabled = enabled;
        if (enabled) {
            setupStatusBar();
        } else {
            removeStatusBar();
        }
    }

    private void setupStatusBar() {
        if (statusBar != null) return;

        statusBar = new HBox();
        statusBar.getStyleClass().add("j-table-status-bar");
        statusBar.setAlignment(Pos.CENTER_LEFT);

        statusLabel = new Label();
        statusLabel.getStyleClass().add("j-table-status-label");

        statusBar.getChildren().add(statusLabel);

        // Insert before pagination
        int paginIdx = getChildren().indexOf(pagination);
        getChildren().add(paginIdx, statusBar);

        updateStatusBar();
    }

    private void removeStatusBar() {
        if (statusBar != null) {
            getChildren().remove(statusBar);
            statusBar = null;
            statusLabel = null;
        }
    }

    private void updateStatusBar() {
        if (statusLabel == null) return;
        int page = pagination.currentPageProperty().get();
        int perPage = itemsPerPage.get();
        int total = filteredItems.size();
        int from = Math.min((page - 1) * perPage + 1, total);
        int to = Math.min(page * perPage, total);

        if (total == 0) {
            statusLabel.setText("No hay resultados");
        } else {
            statusLabel.setText("Mostrando " + from + " a " + to + " de " + total + " resultados");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 10. EXPORT CSV
    // ═══════════════════════════════════════════════════════════════════════════════

    public void exportToCSV(File file) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            // Header
            List<String> headers = columnInfos.stream()
                .map(ci -> ci.title)
                .collect(Collectors.toList());
            pw.println(String.join(",", headers));

            // Data
            for (T item : allItems) {
                List<String> values = new ArrayList<>();
                for (ColumnInfo ci : columnInfos) {
                    values.add(escapeCSV(getPropertyValue(item, ci.property)));
                }
                pw.println(String.join(",", values));
            }
        }
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String getPropertyValue(T item, String property) {
        try {
            String getter = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
            Method method = item.getClass().getMethod(getter);
            Object val = method.invoke(item);
            return val != null ? val.toString() : "";
        } catch (Exception e) {
            return "";
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // DATA MANAGEMENT
    // ═══════════════════════════════════════════════════════════════════════════════

    public void setItems(ObservableList<T> items) {
        if (this.allItems != null) {
            this.allItems.removeListener(itemsListener);
        }
        this.allItems = items != null ? items : FXCollections.observableArrayList();
        this.allItems.addListener(itemsListener);

        applyFilter();
    }

    public ObservableList<T> getItems() { return allItems; }

    public void setItemsPerPage(int items) {
        this.itemsPerPage.set(items);
        updatePagination();
        updateTableData();
    }

    public void addItem(T item) {
        if (allItems != null) {
            allItems.add(item);
        } else {
            tableView.getItems().add(item);
        }
    }

    public void removeItem(T item) {
        allItems.remove(item);
        checkedItems.remove(item);
    }

    public void removeItems(List<T> items) {
        allItems.removeAll(items);
        checkedItems.removeAll(items);
    }

    // ─── Filter Logic ────────────────────────────────────────────────────────────

    private void applyFilter() {
        String query = (searchField != null) ? searchField.getText() : null;

        if (query == null || query.trim().isEmpty()) {
            filteredItems.setAll(allItems);
        } else {
            String lower = query.toLowerCase().trim();
            List<T> matched = allItems.stream()
                .filter(item -> matchesSearch(item, lower))
                .collect(Collectors.toList());
            filteredItems.setAll(matched);
        }

        updatePagination();
        updateTableData();
    }

    private boolean matchesSearch(T item, String query) {
        for (String prop : searchProperties) {
            String val = getPropertyValue(item, prop);
            if (val != null && val.toLowerCase().contains(query)) {
                return true;
            }
        }
        // Fallback: try toString
        String str = item.toString().toLowerCase();
        return str.contains(query);
    }

    private void updatePagination() {
        int total = filteredItems.size();
        int pages = (int) Math.ceil((double) total / itemsPerPage.get());
        if (pages == 0) pages = 1;
        pagination.totalPagesProperty().set(pages);
    }

    private void updateTableData() {
        int page = pagination.currentPageProperty().get();
        int totalPages = pagination.totalPagesProperty().get();

        if (page > totalPages) {
            page = totalPages;
            if (page < 1) page = 1;
            pagination.currentPageProperty().set(page);
        }

        int fromIndex = (page - 1) * itemsPerPage.get();
        int toIndex = Math.min(fromIndex + itemsPerPage.get(), filteredItems.size());

        if (fromIndex <= toIndex && fromIndex < filteredItems.size() && fromIndex >= 0) {
            tableView.setItems(FXCollections.observableArrayList(filteredItems.subList(fromIndex, toIndex)));
        } else {
            tableView.setItems(FXCollections.observableArrayList());
        }

        updateHeaderCheckBoxState();
        updateStatusBar();
    }

    // ─── Scroll & Pagination ─────────────────────────────────────────────────────

    public void setOnScrollBottom(Runnable action) {
        this.onScrollBottom = action;
        this.infiniteScrollEnabled = true;
    }

    public void setPaginationVisible(boolean visible) {
        pagination.setVisible(visible);
        pagination.setManaged(visible);
    }

    // ─── Refresh ─────────────────────────────────────────────────────────────────

    public void refresh() {
        applyFilter();
        tableView.refresh();
    }
}
