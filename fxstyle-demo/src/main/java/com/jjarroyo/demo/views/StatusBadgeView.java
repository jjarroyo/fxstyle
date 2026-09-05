package com.jjarroyo.demo.views;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JStatusBadge;
import com.jjarroyo.components.JStatusBadge.Status;
import com.jjarroyo.demo.util.DemoCodeDialog;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StatusBadgeView extends ScrollPane {

    public StatusBadgeView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));

        setFitToWidth(true);
        setContent(content);

        // Page Header
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("JStatusBadge")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Insignia de estado con indicador luminoso (dot) y animación de pulso en vivo")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);

        content.getChildren().add(pageHeader);

        // Section 1: Standard Status Badges
        JCard card1 = new JCard("Estados Estándar", createStandardStatusBadges());
        card1.addToolbarItem(DemoCodeDialog.createCodeButton("Estados Estándar", 
            "// Crear insignias de estado con punto de color:\n" +
            "JStatusBadge online = new JStatusBadge(\"Online\", Status.ONLINE);\n" +
            "JStatusBadge offline = new JStatusBadge(\"Offline\", Status.OFFLINE);\n" +
            "JStatusBadge away = new JStatusBadge(\"Ausente\", Status.AWAY);\n" +
            "JStatusBadge processing = new JStatusBadge(\"Procesando\", Status.PROCESSING);\n" +
            "JStatusBadge idle = new JStatusBadge(\"En Espera\", Status.IDLE);"));
        content.getChildren().add(card1);

        // Section 2: Live Pulse Animation
        JCard card2 = new JCard("Animación de Pulso en Vivo (Live Pulse)", createPulseStatusBadges());
        card2.addToolbarItem(DemoCodeDialog.createCodeButton("Live Pulse",
            "// Pasar 'true' en el 3er parámetro activa el efecto de pulso:\n" +
            "JStatusBadge liveServer = new JStatusBadge(\"Servidor Activo (Live)\", Status.ONLINE, true);\n" +
            "JStatusBadge liveStream = new JStatusBadge(\"Transmisión en Vivo\", Status.DANGER, true);\n" +
            "JStatusBadge syncing = new JStatusBadge(\"Sincronizando...\", Status.PROCESSING, true);"));
        content.getChildren().add(card2);

        // Section 3: Dynamic Interaction Toggle
        JCard card3 = new JCard("Control Dinámico de Animación", createDynamicToggleSection());
        card3.addToolbarItem(DemoCodeDialog.createCodeButton("Control Dinámico",
            "// Encender o apagar el pulso en tiempo de ejecución:\n" +
            "badge.setPulse(true);  // Inicia la animación\n" +
            "badge.setPulse(false); // Detiene la animación\n\n" +
            "// Cambiar texto y estado en vivo:\n" +
            "badge.setStatus(Status.OFFLINE).setText(\"Servidor Caído\");"));
        content.getChildren().add(card3);
    }

    // =========================================================================
    // COPIAR Y PEGAR: Insignias de Estado Estándar (JStatusBadge)
    // Ejemplo: JStatusBadge badge = new JStatusBadge("Online", Status.ONLINE);
    // =========================================================================
    private Node createStandardStatusBadges() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);

        JStatusBadge online = new JStatusBadge("Online", Status.ONLINE);
        JStatusBadge offline = new JStatusBadge("Offline", Status.OFFLINE);
        JStatusBadge away = new JStatusBadge("Ausente", Status.AWAY);
        JStatusBadge processing = new JStatusBadge("Procesando", Status.PROCESSING);
        JStatusBadge idle = new JStatusBadge("En Espera", Status.IDLE);
        JStatusBadge purple = new JStatusBadge("VIP Status", Status.PURPLE);

        pane.getChildren().addAll(online, offline, away, processing, idle, purple);
        return pane;
    }

    // =========================================================================
    // COPIAR Y PEGAR: Insignias con Animación de Pulso en Vivo (Live Pulse)
    // Ejemplo: JStatusBadge liveBadge = new JStatusBadge("En Vivo", Status.DANGER, true);
    // =========================================================================
    private Node createPulseStatusBadges() {
        FlowPane pane = new FlowPane();
        pane.setHgap(16);
        pane.setVgap(16);

        JStatusBadge liveServer = new JStatusBadge("Servidor Activo (Live)", Status.ONLINE, true);
        JStatusBadge liveStream = new JStatusBadge("Transmisión en Vivo", Status.DANGER, true);
        JStatusBadge syncing = new JStatusBadge("Sincronizando...", Status.PROCESSING, true);
        JStatusBadge warning = new JStatusBadge("Carga Elevada", Status.WARNING, true);

        pane.getChildren().addAll(liveServer, liveStream, syncing, warning);
        return pane;
    }

    // =========================================================================
    // COPIAR Y PEGAR: Control Dinámico de Animación y Cambio de Estado
    // Ejemplo: badge.setPulse(false); badge.setStatus(Status.OFFLINE);
    // =========================================================================
    private Node createDynamicToggleSection() {
        VBox container = new VBox(16);

        JStatusBadge dynamicBadge = new JStatusBadge("Servidor Principal", Status.ONLINE, true);

        HBox controls = new HBox(12);
        JButton togglePulseBtn = new JButton("Alternar Pulso (Pausa / Play)");
        togglePulseBtn.addClass("btn-secondary");
        togglePulseBtn.setOnAction(e -> {
            boolean currentPulse = dynamicBadge.isPulse();
            dynamicBadge.setPulse(!currentPulse);
        });

        JButton changeStatusBtn = new JButton("Cambiar a Error / Offline");
        changeStatusBtn.addClass("btn-danger");
        changeStatusBtn.setOnAction(e -> {
            if (dynamicBadge.getStatus() == Status.ONLINE) {
                dynamicBadge.setStatus(Status.OFFLINE).setText("Servidor Caído");
                changeStatusBtn.setText("Restablecer a Online");
            } else {
                dynamicBadge.setStatus(Status.ONLINE).setText("Servidor Principal");
                changeStatusBtn.setText("Cambiar a Error / Offline");
            }
        });

        controls.getChildren().addAll(togglePulseBtn, changeStatusBtn);
        container.getChildren().addAll(dynamicBadge, controls);

        return container;
    }
}
