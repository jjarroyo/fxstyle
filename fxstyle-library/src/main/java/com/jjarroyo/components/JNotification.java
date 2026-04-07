package com.jjarroyo.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JNotification — Componente de notificaciones avanzado con campana, badge y lista detallada.
 */
public class JNotification extends StackPane {

    private final List<JNotificationItem> notifications = new ArrayList<>();
    private final JBadge badge;
    private final SVGPath bellIcon;
    private final JPopover popover;
    private final VBox listContainer;

    public JNotification() {
        getStyleClass().add("j-notification-trigger");
        setCursor(javafx.scene.Cursor.HAND);

        // 1. Icono de Campana
        bellIcon = new SVGPath();
        bellIcon.setContent(JIcon.BELL.getPath());
        bellIcon.getStyleClass().add("j-notification-bell");
        bellIcon.setScaleX(1.2);
        bellIcon.setScaleY(1.2);

        // 2. Badge de Notificaciones
        badge = new JBadge("0");
        badge.getStyleClass().addAll("badge-danger", "badge-circle", "j-notification-badge");
        badge.setVisible(false);
        badge.setManaged(false);

        // Posicionamiento del badge en el StackPane
        StackPane.setAlignment(badge, Pos.TOP_RIGHT);
        StackPane.setMargin(badge, new Insets(-6, -12, 0, 0));

        getChildren().addAll(bellIcon, badge);

        // 3. Popover y Contenedor de Lista
        popover = new JPopover();
        popover.setPosition(JPopover.Position.BOTTOM);
        popover.getStyleClass().add("j-notification-popover");
        
        listContainer = new VBox();
        listContainer.getStyleClass().add("j-notification-list");
        
        setupPopoverContent();

        // Evento Click
        setOnMouseClicked(e -> showNotifications());
    }

    private void setupPopoverContent() {
        VBox root = new VBox();
        root.setMinWidth(350); // Ancho "gordito"
        root.setMaxWidth(350);
        root.getStyleClass().add("j-notification-popup");

        // Header
        HBox header = new HBox();
        header.getStyleClass().add("j-notification-header");
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("Notificaciones");
        title.getStyleClass().add("j-notification-title");
        
        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label markAllRead = new Label("Marcar todo como leído");
        markAllRead.getStyleClass().add("j-notification-action");
        markAllRead.setOnMouseClicked(e -> markAllAsRead());

        header.getChildren().addAll(title, spacer, markAllRead);

        // Body (Scrollable)
        ScrollPane scrollPane = new ScrollPane(listContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("j-notification-scroll");

        // Footer
        HBox footer = new HBox();
        footer.getStyleClass().add("j-notification-footer");
        footer.setAlignment(Pos.CENTER);
        
        Label viewAll = new Label("Ver todas las notificaciones");
        viewAll.getStyleClass().add("j-notification-footer-action");
        footer.getChildren().add(viewAll);

        root.setMaxHeight(500); // Cabecera + Body (~400) + Footer
        root.getChildren().addAll(header, scrollPane, footer);
        popover.setContentNode(root);
    }

    public void addNotification(String title, String description, String imageUrl, LocalDateTime time) {
        JNotificationItem item = new JNotificationItem(title, description, imageUrl, time);
        notifications.add(0, item); // Insertar al inicio
        updateBadge();
        rebuildList();
    }

    private void updateBadge() {
        long unreadCount = notifications.stream().filter(n -> !n.isRead()).count();
        if (unreadCount > 0) {
            badge.setText(unreadCount > 99 ? "99+" : String.valueOf(unreadCount));
            badge.setVisible(true);
            badge.setManaged(true);
        } else {
            badge.setVisible(false);
            badge.setManaged(false);
        }
    }

    private void rebuildList() {
        listContainer.getChildren().clear();
        if (notifications.isEmpty()) {
            Label empty = new Label("No tienes notificaciones");
            empty.getStyleClass().add("j-notification-empty");
            listContainer.getChildren().add(empty);
            return;
        }

        for (JNotificationItem item : notifications) {
            listContainer.getChildren().add(createItemNode(item));
        }
    }

    private Node createItemNode(JNotificationItem item) {
        HBox row = new HBox(12);
        row.getStyleClass().add("j-notification-item");
        if (!item.isRead()) row.getStyleClass().add("unread");
        row.setPadding(new Insets(12));
        row.setAlignment(Pos.TOP_LEFT);

        // Avatar
        JAvatar avatar = new JAvatar();
        avatar.setSize(JAvatar.Size.SM);
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            try {
                avatar.setImage(new javafx.scene.image.Image(item.getImageUrl(), true));
            } catch (Exception e) {
                avatar.setInitials(item.getTitle().substring(0, 1));
            }
        } else {
            avatar.setInitials(item.getTitle().substring(0, 1));
        }

        // Content
        VBox content = new VBox(2);
        HBox.setHgrow(content, Priority.ALWAYS);
        
        Label titleLbl = new Label(item.getTitle());
        titleLbl.getStyleClass().add("item-title");
        
        Label descLbl = new Label(item.getDescription());
        descLbl.getStyleClass().add("item-description");
        descLbl.setWrapText(true);
        descLbl.setMaxHeight(40); // Aproximadamente 2 líneas

        content.getChildren().addAll(titleLbl, descLbl);

        // Time
        Label timeLbl = new Label(getTimeAgo(item.getTimestamp()));
        timeLbl.getStyleClass().add("item-time");

        row.getChildren().addAll(avatar, content, timeLbl);
        
        row.setOnMouseClicked(e -> {
            item.setRead(true);
            updateBadge();
            row.getStyleClass().remove("unread");
        });

        return row;
    }

    private String getTimeAgo(LocalDateTime dateTime) {
        Duration duration = Duration.between(dateTime, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) return "Ahora";
        if (seconds < 3600) return "Hace " + (seconds / 60) + "m";
        if (seconds < 86400) return "Hace " + (seconds / 3600) + "h";
        if (seconds < 2592000) return "Hace " + (seconds / 86400) + "d";
        return "Hace 1m+"; // Simplificado para meses
    }

    private void markAllAsRead() {
        notifications.forEach(n -> n.setRead(true));
        updateBadge();
        rebuildList();
    }

    private void showNotifications() {
        popover.show(this);
    }
}
