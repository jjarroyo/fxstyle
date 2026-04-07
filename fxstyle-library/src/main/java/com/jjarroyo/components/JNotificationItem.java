package com.jjarroyo.components;

import java.time.LocalDateTime;

/**
 * Representa una notificación individual dentro del componente JNotification.
 */
public class JNotificationItem {
    private String title;
    private String description;
    private String imageUrl;
    private LocalDateTime timestamp;
    private boolean read;

    public JNotificationItem(String title, String description, String imageUrl, LocalDateTime timestamp) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.read = false;
    }

    // Getters y Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}
