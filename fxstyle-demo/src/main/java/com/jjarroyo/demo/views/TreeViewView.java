package com.jjarroyo.demo.views;

import com.jjarroyo.components.*;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class TreeViewView extends VBox {

    public TreeViewView() {
        setSpacing(24);
        setPadding(new Insets(32));

        // Título de la vista
        VBox header = new VBox(
            new JLabel("Tree View").withStyle("text-2xl", "font-bold", "text-slate-800"),
            new JLabel("Componente avanzado para mostrar datos jerárquicos.").withStyle("text-base", "text-slate-500")
        );

        // 1. Basic Tree
        JTreeView<String> basicTree = new JTreeView<>();
        
        JTreeItem<String> root = new JTreeItem<>("Proyectos", JIcon.FOLDER);
        root.setExpanded(true);
        
        JTreeItem<String> proj1 = new JTreeItem<>("App Móvil", JIcon.FOLDER);
        proj1.getChildren().add(new JTreeItem<>("AndroidManifest.xml", JIcon.FILE));
        proj1.getChildren().add(new JTreeItem<>("MainActivity.java", JIcon.FILE));
        proj1.getChildren().add(new JTreeItem<>("styles.xml", JIcon.FILE));
        
        JTreeItem<String> proj2 = new JTreeItem<>("Backend API", JIcon.FOLDER);
        proj2.getChildren().add(new JTreeItem<>("application.yml", JIcon.FILE));
        proj2.getChildren().add(new JTreeItem<>("UserController.java", JIcon.FILE));
        proj2.getChildren().add(new JTreeItem<>("README.md", JIcon.FILE_TEXT));
        
        root.getChildren().add(proj1);
        root.getChildren().add(proj2);
        basicTree.setRoot(root);
        
        JCard basicCard = new JCard("Ejemplo Básico", basicTree);

        // 2. Tree with Checkboxes
        JTreeView<String> checkTree = new JTreeView<>();
        checkTree.setShowCheckboxes(true);
        
        JTreeItem<String> checkRoot = new JTreeItem<>("Permisos de Sistema", JIcon.SETTINGS);
        checkRoot.setExpanded(true);
        
        JTreeItem<String> userMgmt = new JTreeItem<>("Gestión de Usuarios", JIcon.GROUP);
        userMgmt.setExpanded(true);
        userMgmt.getChildren().add(new JTreeItem<>("Crear Usuarios", JIcon.PERSON));
        userMgmt.getChildren().add(new JTreeItem<>("Editar Usuarios", JIcon.EDIT));
        userMgmt.getChildren().add(new JTreeItem<>("Eliminar Usuarios", JIcon.DELETE));
        
        JTreeItem<String> sysConfig = new JTreeItem<>("Configuración del Sistema", JIcon.BUILD);
        sysConfig.getChildren().add(new JTreeItem<>("Variables de entorno", JIcon.CODE));
        sysConfig.getChildren().add(new JTreeItem<>("Logs", JIcon.BUG_REPORT));
        
        checkRoot.getChildren().add(userMgmt);
        checkRoot.getChildren().add(sysConfig);
        checkTree.setRoot(checkRoot);
        
        // Listener for checkbox clicks
        JParagraph selectionText = new JParagraph("Seleccionado: Nada");
        selectionText.getStyleClass().add("j-text-primary-600");
        checkTree.setOnCheckAction(item -> {
            selectionText.setText(item.getValue() + " -> " + (item.isChecked() ? "Marcado" : "Desmarcado"));
        });
        
        VBox checkContainer = new VBox(16, checkTree, selectionText);
        JCard checkCard = new JCard("Árbol con Selección (Checkboxes)", checkContainer);

        getChildren().addAll(header, basicCard, checkCard);
    }
}
