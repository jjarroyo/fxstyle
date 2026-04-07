package com.jjarroyo.demo.views;

import com.jjarroyo.components.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class DataView extends ScrollPane {

    // ─── Data Model ──────────────────────────────────────────────────────────────
    public static class User {
        private final SimpleStringProperty name;
        private final SimpleStringProperty email;
        private final SimpleStringProperty role;
        private final SimpleStringProperty status;
        private final SimpleStringProperty department;

        public User(String name, String email, String role, String status, String department) {
            this.name = new SimpleStringProperty(name);
            this.email = new SimpleStringProperty(email);
            this.role = new SimpleStringProperty(role);
            this.status = new SimpleStringProperty(status);
            this.department = new SimpleStringProperty(department);
        }

        // Convenience constructor for basic tables
        public User(String name, String email, String role) {
            this(name, email, role, "Activo", "General");
        }

        public String getName() { return name.get(); }
        public void setName(String v) { name.set(v); }
        public SimpleStringProperty nameProperty() { return name; }

        public String getEmail() { return email.get(); }
        public void setEmail(String v) { email.set(v); }
        public SimpleStringProperty emailProperty() { return email; }

        public String getRole() { return role.get(); }
        public void setRole(String v) { role.set(v); }
        public SimpleStringProperty roleProperty() { return role; }

        public String getStatus() { return status.get(); }
        public void setStatus(String v) { status.set(v); }
        public SimpleStringProperty statusProperty() { return status; }

        public String getDepartment() { return department.get(); }
        public void setDepartment(String v) { department.set(v); }
        public SimpleStringProperty departmentProperty() { return department; }

        @Override
        public String toString() {
            return name.get() + " | " + email.get() + " | " + role.get();
        }
    }

    private ObservableList<User> data;

    public DataView() {
        getStyleClass().add("j-scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));

        VBox content = new VBox();
        content.setSpacing(32);

        // Header
        VBox header = new VBox(8);
        Label title = new Label("Data Tables & Lists");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Advanced data presentation with sorting, pagination, checkboxes, search, row actions, and more.");
        subtitle.getStyleClass().add("page-subtitle");
        header.getChildren().addAll(title, subtitle);
        content.getChildren().add(header);

        // Generate rich dummy data
        data = generateData();

        // ═══════════════════════════════════════════════════════════════════
        // 1. TABLA BÁSICA
        // ═══════════════════════════════════════════════════════════════════
        content.getChildren().add(createBasicTable());

        // ═══════════════════════════════════════════════════════════════════
        // 2. TABLA CON CHECKBOXES
        // ═══════════════════════════════════════════════════════════════════
        content.getChildren().add(createCheckableTable());

        // ═══════════════════════════════════════════════════════════════════
        // 3. TABLA CON BÚSQUEDA
        // ═══════════════════════════════════════════════════════════════════
        content.getChildren().add(createSearchableTable());

        // ═══════════════════════════════════════════════════════════════════
        // 4. TABLA CON ACCIONES Y CUSTOM RENDERERS
        // ═══════════════════════════════════════════════════════════════════
        content.getChildren().add(createActionsTable());

        // ═══════════════════════════════════════════════════════════════════
        // 5. TABLA DENSE + STRIPED
        // ═══════════════════════════════════════════════════════════════════
        content.getChildren().add(createDenseStripedTable());

        // ═══════════════════════════════════════════════════════════════════
        // 6. TABLA PRO — ALL FEATURES
        // ═══════════════════════════════════════════════════════════════════
        content.getChildren().add(createProTable());

        // ═══════════════════════════════════════════════════════════════════
        // 7. EDITABLE TABLE (DataGrid Mode)
        // ═══════════════════════════════════════════════════════════════════
        content.getChildren().add(createEditableTable());

        // ═══════════════════════════════════════════════════════════════════
        // 8. EDITABLE TABLE WITH COMBOS
        // ═══════════════════════════════════════════════════════════════════
        content.getChildren().add(createEditableComboTable());

        // ═══════════════════════════════════════════════════════════════════
        // 9. JLIST EXAMPLE (existing)
        // ═══════════════════════════════════════════════════════════════════
        content.getChildren().add(createListExample());

        // ═══════════════════════════════════════════════════════════════════
        // 10. STANDALONE PAGINATION
        // ═══════════════════════════════════════════════════════════════════
        content.getChildren().add(createPaginationExample());

        setContent(content);
    }

    // ─── Data Generator ──────────────────────────────────────────────────────────

    private ObservableList<User> generateData() {
        ObservableList<User> list = FXCollections.observableArrayList();
        String[] names = {"Carlos García", "María López", "Juan Rodríguez", "Ana Martínez",
            "Pedro Sánchez", "Laura Torres", "Diego Rivera", "Sofía Hernández",
            "Miguel Flores", "Valentina Díaz", "Andrés Morales", "Camila Vargas",
            "Roberto Castro", "Isabella Ramos", "Fernando Ortiz", "Lucía Jiménez",
            "Gabriel Mendoza", "Daniela Guerrero", "Alejandro Peña", "Natalia Cruz",
            "Ricardo Reyes", "Paula Romero", "Sebastián Ruiz", "Mariana Medina",
            "Jorge Herrera", "Carolina Aguilar", "David Navarro", "Andrea Molina",
            "Héctor Domínguez", "Valeria Vega"};
        String[] roles = {"Admin", "User", "Editor", "Viewer", "Moderator"};
        String[] statuses = {"Activo", "Inactivo", "Pendiente", "Suspendido"};
        String[] departments = {"Ingeniería", "Marketing", "Ventas", "Soporte", "RRHH", "Finanzas"};

        for (int i = 0; i < names.length; i++) {
            String email = names[i].toLowerCase().replace(" ", ".").replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o").replace("ú", "u") + "@empresa.com";
            list.add(new User(
                names[i], email,
                roles[i % roles.length],
                statuses[i % statuses.length],
                departments[i % departments.length]
            ));
        }
        return list;
    }

    // ─── 1. Basic Table ──────────────────────────────────────────────────────────

    private JCard createBasicTable() {
        JCard card = new JCard("Tabla Básica", "Tabla simple con columnas sortables y paginación.");

        JTable<User> table = new JTable<>();
        table.addColumn("Nombre", "name");
        table.addColumn("Email", "email");
        table.addColumn("Rol", "role");
        table.setItems(FXCollections.observableArrayList(data));
        table.setItemsPerPage(5);

        card.setBody(table);
        return card;
    }

    // ─── 2. Checkable Table ──────────────────────────────────────────────────────

    private JCard createCheckableTable() {
        JCard card = new JCard("Tabla con Checkboxes", "Selecciona filas con checkbox. Select All en el header.");

        VBox body = new VBox(12);

        JTable<User> table = new JTable<>();
        table.setCheckable(true);
        table.addColumn("Nombre", "name");
        table.addColumn("Email", "email");
        table.addColumn("Rol", "role");
        table.setItems(FXCollections.observableArrayList(data));
        table.setItemsPerPage(5);

        // Info bar
        HBox infoBar = new HBox(12);
        infoBar.setAlignment(Pos.CENTER_LEFT);
        
        Label selectedLabel = new Label("Seleccionados: 0");
        selectedLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: -color-slate-600;");

        Button showBtn = new Button("Mostrar seleccionados");
        showBtn.getStyleClass().addAll("j-btn", "btn-sm", "btn-primary");
        showBtn.setOnAction(e -> {
            List<User> checked = table.getCheckedItems();
            StringBuilder sb = new StringBuilder();
            for (User u : checked) {
                sb.append("• ").append(u.getName()).append("\n");
            }
            if (sb.length() == 0) sb.append("Ninguno seleccionado");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Seleccionados");
            alert.setHeaderText(checked.size() + " usuario(s) seleccionado(s)");
            alert.setContentText(sb.toString());
            alert.showAndWait();
        });

        Button clearBtn = new Button("Limpiar selección");
        clearBtn.getStyleClass().addAll("j-btn", "btn-sm", "btn-secondary");
        clearBtn.setOnAction(e -> table.clearChecks());

        infoBar.getChildren().addAll(selectedLabel, showBtn, clearBtn);

        table.setOnCheckChange(items -> {
            selectedLabel.setText("Seleccionados: " + items.size());
        });

        body.getChildren().addAll(infoBar, table);
        card.setBody(body);
        return card;
    }

    // ─── 3. Searchable Table ─────────────────────────────────────────────────────

    private JCard createSearchableTable() {
        JCard card = new JCard("Tabla con Búsqueda", "Filtra en tiempo real escribiendo en el campo de búsqueda.");

        JTable<User> table = new JTable<>();
        table.setSearchable(true);
        table.setSearchPlaceholder("Buscar usuarios por nombre, email o rol...");
        table.addColumn("Nombre", "name");
        table.addColumn("Email", "email");
        table.addColumn("Rol", "role");
        table.addColumn("Departamento", "department");
        table.setStatusBarEnabled(true);
        table.setItems(FXCollections.observableArrayList(data));
        table.setItemsPerPage(5);

        card.setBody(table);
        return card;
    }

    // ─── 4. Actions + Custom Renderers ───────────────────────────────────────────

    private JCard createActionsTable() {
        JCard card = new JCard("Tabla con Acciones y Renderers", 
            "Botones de acción por fila y chips para roles/estados.");

        ObservableList<User> actionData = FXCollections.observableArrayList(data);

        JTable<User> table = new JTable<>();
        table.addColumn("Nombre", "name");
        table.addColumn("Email", "email");

        // Custom chip renderer for Role
        table.addColumn("Rol", "role", user -> {
            JChip chip = new JChip(user.getRole());
            switch (user.getRole()) {
                case "Admin": chip.setColor(JChip.ChipColor.DANGER); break;
                case "Editor": chip.setColor(JChip.ChipColor.WARNING); break;
                case "Moderator": chip.setColor(JChip.ChipColor.INFO); break;
                default: chip.setColor(JChip.ChipColor.PRIMARY); break;
            }
            chip.setChipSize(JChip.Size.SM);
            return chip;
        });

        // Custom chip renderer for Status
        table.addColumn("Estado", "status", user -> {
            JChip chip = new JChip(user.getStatus());
            switch (user.getStatus()) {
                case "Activo": chip.setColor(JChip.ChipColor.SUCCESS); break;
                case "Inactivo": chip.setColor(JChip.ChipColor.SLATE); break;
                case "Pendiente": chip.setColor(JChip.ChipColor.WARNING); break;
                case "Suspendido": chip.setColor(JChip.ChipColor.DANGER); break;
                default: chip.setColor(JChip.ChipColor.PRIMARY); break;
            }
            chip.setChipSize(JChip.Size.SM);
            return chip;
        });

        // Row actions
        table.setRowActions((user, box) -> {
            Button editBtn = new Button("✏");
            editBtn.getStyleClass().addAll("j-btn", "btn-sm", "btn-info");
            editBtn.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Editar");
                alert.setHeaderText("Editando: " + user.getName());
                alert.setContentText("Email: " + user.getEmail());
                alert.showAndWait();
            });

            Button deleteBtn = new Button("🗑");
            deleteBtn.getStyleClass().addAll("j-btn", "btn-sm", "btn-danger");
            deleteBtn.setOnAction(e -> {
                actionData.remove(user);
                table.refresh();
            });

            box.getChildren().addAll(editBtn, deleteBtn);
        });

        table.setItems(actionData);
        table.setItemsPerPage(5);

        // Row click handler
        table.setOnRowClick(user -> {
            System.out.println("Clicked: " + user.getName());
        });

        card.setBody(table);
        return card;
    }

    // ─── 5. Dense + Striped ──────────────────────────────────────────────────────

    private JCard createDenseStripedTable() {
        JCard card = new JCard("Tabla Dense + Striped", 
            "Modo compacto con filas zebra. Ideal para datasets grandes.");

        JTable<User> table = new JTable<>();
        table.setDense(true);
        table.setStriped(true);
        table.addColumn("Nombre", "name");
        table.addColumn("Email", "email");
        table.addColumn("Rol", "role");
        table.addColumn("Departamento", "department");
        table.addColumn("Estado", "status");
        table.setItems(FXCollections.observableArrayList(data));
        table.setItemsPerPage(8);
        table.setStatusBarEnabled(true);

        card.setBody(table);
        return card;
    }

    // ─── 6. Pro Table — Full Featured ────────────────────────────────────────────

    private JCard createProTable() {
        JCard card = new JCard("Tabla Pro ⚡", 
            "Todas las funciones: checkboxes + búsqueda + acciones masivas + custom renderers + status bar.");

        ObservableList<User> proData = FXCollections.observableArrayList(data);

        JTable<User> table = new JTable<>();
        table.setCheckable(true);
        table.setSearchable(true);
        table.setSearchPlaceholder("🔍 Buscar en tabla pro...");
        table.setStriped(true);
        table.setStatusBarEnabled(true);

        // Columns
        table.addColumn("Nombre", "name");
        table.addColumn("Email", "email");

        // Custom renderer for Role
        table.addColumn("Rol", "role", user -> {
            JChip chip = new JChip(user.getRole());
            switch (user.getRole()) {
                case "Admin": chip.setColor(JChip.ChipColor.DANGER); break;
                case "Editor": chip.setColor(JChip.ChipColor.WARNING); break;
                case "Moderator": chip.setColor(JChip.ChipColor.INFO); break;
                default: chip.setColor(JChip.ChipColor.PRIMARY); break;
            }
            chip.setChipSize(JChip.Size.SM);
            return chip;
        });

        table.addColumn("Departamento", "department");

        // Custom renderer for Status
        table.addColumn("Estado", "status", user -> {
            JChip chip = new JChip(user.getStatus());
            switch (user.getStatus()) {
                case "Activo": chip.setColor(JChip.ChipColor.SUCCESS); break;
                case "Inactivo": chip.setColor(JChip.ChipColor.SLATE); break;
                case "Pendiente": chip.setColor(JChip.ChipColor.WARNING); break;
                case "Suspendido": chip.setColor(JChip.ChipColor.DANGER); break;
                default: chip.setColor(JChip.ChipColor.PRIMARY); break;
            }
            chip.setChipSize(JChip.Size.SM);
            return chip;
        });

        // Bulk delete
        table.setOnBulkDelete(items -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Eliminar en lote");
            confirm.setHeaderText("¿Eliminar " + items.size() + " usuario(s)?");
            confirm.setContentText("Esta acción no se puede deshacer.");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    proData.removeAll(items);
                    table.clearChecks();
                    table.refresh();
                }
            });
        });

        // Row actions
        table.setRowActions((user, box) -> {
            Button viewBtn = new Button("👁");
            viewBtn.getStyleClass().addAll("j-btn", "btn-sm", "btn-secondary");
            viewBtn.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Detalle");
                alert.setHeaderText(user.getName());
                alert.setContentText(
                    "Email: " + user.getEmail() + "\n" +
                    "Rol: " + user.getRole() + "\n" +
                    "Departamento: " + user.getDepartment() + "\n" +
                    "Estado: " + user.getStatus()
                );
                alert.showAndWait();
            });

            Button deleteBtn = new Button("🗑");
            deleteBtn.getStyleClass().addAll("j-btn", "btn-sm", "btn-danger");
            deleteBtn.setOnAction(e -> {
                proData.remove(user);
                table.refresh();
            });

            box.getChildren().addAll(viewBtn, deleteBtn);
        });

        table.setItems(proData);
        table.setItemsPerPage(5);
        table.setEmptyText("No se encontraron usuarios");

        card.setBody(table);
        return card;
    }

    // ─── 7. Editable Table ───────────────────────────────────────────────────────

    private JCard createEditableTable() {
        JCard card = new JCard("Tabla Editable ✏️",
            "Doble clic en cualquier celda para editar. Enter para confirmar, Escape para cancelar, Tab para siguiente.");

        VBox body = new VBox(12);

        ObservableList<User> editableData = FXCollections.observableArrayList(
            new User("Carlos García", "carlos@empresa.com", "Admin", "Activo", "Ingeniería"),
            new User("María López", "maria@empresa.com", "Editor", "Activo", "Marketing"),
            new User("Juan Rodríguez", "juan@empresa.com", "User", "Pendiente", "Ventas"),
            new User("Ana Martínez", "ana@empresa.com", "Viewer", "Inactivo", "Soporte"),
            new User("Pedro Sánchez", "pedro@empresa.com", "Admin", "Activo", "Finanzas")
        );

        JTable<User> table = new JTable<>();
        table.setEditable(true);
        table.setStriped(true);

        // Editable columns
        table.addEditableColumn("Nombre", "name",
            (user, val) -> user.setName(val));

        table.addEditableColumn("Email", "email",
            (user, val) -> user.setEmail(val));

        table.addEditableColumn("Departamento", "department",
            (user, val) -> user.setDepartment(val));

        // Non-editable column
        table.addColumn("Rol", "role");

        // Edit callback
        Label lastEditLabel = new Label("Último cambio: ninguno");
        lastEditLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: -color-slate-500;");

        table.setOnCellEdit((user, prop) -> {
            lastEditLabel.setText("✅ Editado: " + user.getName() + " → campo '" + prop + "'");
        });

        table.setItems(editableData);
        table.setItemsPerPage(5);

        body.getChildren().addAll(lastEditLabel, table);
        card.setBody(body);
        return card;
    }

    // ─── 8. Editable Table with ComboBox ─────────────────────────────────────────

    private JCard createEditableComboTable() {
        JCard card = new JCard("Tabla Editable con Combo ⚡",
            "Columnas de texto libre y columnas con opciones predefinidas (ComboBox).");

        VBox body = new VBox(12);

        ObservableList<User> comboData = FXCollections.observableArrayList(
            new User("Roberto Castro", "roberto@empresa.com", "Admin", "Activo", "Ingeniería"),
            new User("Isabella Ramos", "isabella@empresa.com", "Editor", "Pendiente", "Marketing"),
            new User("Fernando Ortiz", "fernando@empresa.com", "User", "Inactivo", "Ventas"),
            new User("Lucía Jiménez", "lucia@empresa.com", "Moderator", "Activo", "RRHH"),
            new User("Gabriel Mendoza", "gabriel@empresa.com", "Viewer", "Suspendido", "Finanzas")
        );

        JTable<User> table = new JTable<>();
        table.setEditable(true);
        table.setSearchable(true);
        table.setSearchPlaceholder("Buscar en tabla editable...");
        table.setStatusBarEnabled(true);

        // Text editable
        table.addEditableColumn("Nombre", "name",
            (user, val) -> user.setName(val));

        table.addEditableColumn("Email", "email",
            (user, val) -> user.setEmail(val));

        // ComboBox editable - Role
        table.addEditableColumn("Rol", "role",
            (user, val) -> user.setRole(val),
            "Admin", "Editor", "User", "Viewer", "Moderator");

        // ComboBox editable - Status
        table.addEditableColumn("Estado", "status",
            (user, val) -> user.setStatus(val),
            "Activo", "Inactivo", "Pendiente", "Suspendido");

        // ComboBox editable - Department
        table.addEditableColumn("Departamento", "department",
            (user, val) -> user.setDepartment(val),
            "Ingeniería", "Marketing", "Ventas", "Soporte", "RRHH", "Finanzas");

        // Edit change log
        VBox logBox = new VBox(4);
        logBox.setStyle("-fx-padding: 8; -fx-background-color: -color-bg-subtle; -fx-background-radius: 6;");
        Label logTitle = new Label("📝 Log de cambios:");
        logTitle.setStyle("-fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: -color-slate-700;");
        Label logContent = new Label("Sin cambios todavía...");
        logContent.setStyle("-fx-font-size: 12px; -fx-text-fill: -color-slate-500;");
        logBox.getChildren().addAll(logTitle, logContent);

        final int[] editCount = {0};
        table.setOnCellEdit((user, prop) -> {
            editCount[0]++;
            String value = "";
            switch (prop) {
                case "name": value = user.getName(); break;
                case "email": value = user.getEmail(); break;
                case "role": value = user.getRole(); break;
                case "status": value = user.getStatus(); break;
                case "department": value = user.getDepartment(); break;
            }
            logContent.setText("[" + editCount[0] + "] " + prop + " → \"" + value + "\"  (" + user.getName() + ")");
        });

        table.setItems(comboData);
        table.setItemsPerPage(5);

        body.getChildren().addAll(logBox, table);
        card.setBody(body);
        return card;
    }

    // ─── 9. JList Example ────────────────────────────────────────────────────────

    private JCard createListExample() {
        JCard card = new JCard("Users List", "Card-style rows with infinite scroll.");

        JList<User> list = new JList<>();
        list.setItems(data);

        list.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    VBox cardBox = new VBox(4);
                    cardBox.getStyleClass().add("j-list-card");

                    Label nameLbl = new Label(item.getName());
                    nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                    Label emailLbl = new Label(item.getEmail());
                    emailLbl.setStyle("-fx-text-fill: -color-slate-500; -fx-font-size: 13px;");

                    HBox row = new HBox(10);
                    Label roleBadge = new Label(item.getRole());
                    roleBadge.setStyle("-fx-background-color: -color-primary-100; -fx-text-fill: -color-primary-700; -fx-padding: 2px 8px; -fx-background-radius: 4px; -fx-font-size: 11px;");

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    row.getChildren().addAll(nameLbl, spacer, roleBadge);
                    cardBox.getChildren().addAll(row, emailLbl);

                    setGraphic(cardBox);
                    setStyle("-fx-background-color: transparent; -fx-padding: 5px 0;");
                }
            }
        });

        list.setOnScrollBottom(() -> {
            System.out.println("Scrolled to bottom! Loading more...");
            int start = data.size() + 1;
            for (int i = start; i < start + 5; i++) {
                data.add(new User("Nuevo Usuario " + i, "nuevo" + i + "@empresa.com", "Guest"));
            }
        });

        card.setBody(list);
        return card;
    }

    // ─── 10. Standalone Pagination ────────────────────────────────────────────────

    private JCard createPaginationExample() {
        JCard card = new JCard("Paginación Standalone", "Componente JPagination independiente.");

        VBox body = new VBox(16);
        body.setPadding(new Insets(16));

        Label pageLabel = new Label("Página actual: 1 de 10");
        pageLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 600; -fx-text-fill: -color-slate-700;");

        JPagination pagination = new JPagination();
        pagination.totalPagesProperty().set(10);
        pagination.currentPageProperty().addListener((obs, old, newVal) -> {
            pageLabel.setText("Página actual: " + newVal + " de 10");
        });

        body.getChildren().addAll(pageLabel, pagination);
        card.setBody(body);
        return card;
    }
}
