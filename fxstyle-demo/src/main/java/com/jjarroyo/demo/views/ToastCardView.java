package com.jjarroyo.demo.views;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JToastCard;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Vista de demostración para JToastCard (Notificaciones estilo Card opaco con animación de entrada).
 */
public class ToastCardView extends ScrollPane {

    public ToastCardView() {
        getStyleClass().add("j-scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));

        VBox content = new VBox();
        content.setSpacing(24);

        // Header
        VBox header = new VBox(8);
        Label title = new Label("Toast Cards (JToastCard)");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Notificaciones emergentes estilo tarjeta con colores opacos de alto impacto, animación suave de entrada (Avast style) y salida, temporizador y botón de cierre.");
        subtitle.getStyleClass().add("page-subtitle");
        header.getChildren().addAll(title, subtitle);
        content.getChildren().add(header);

        // 1. Quick Presets Card (Avast Threat, Success, Warning, Info)
        JCard presetsCard = new JCard("Presets Rápidos (Estilo Avast)", "Demostraciones directas con casos de uso comunes");
        VBox presetsBody = new VBox(16);
        presetsBody.setPadding(new Insets(20));

        HBox presetsRow1 = new HBox(12);
        JButton btnAvastDanger = new JButton("🚨 Avast Threat (Bottom Right - Danger)");
        btnAvastDanger.setStyle("-fx-background-color: #fef2f2; -fx-text-fill: #991b1b; -fx-border-color: #fca5a5; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-font-weight: bold; -fx-padding: 7px 14px;");
        btnAvastDanger.setOnAction(e -> {
            JToastCard.show(
                getScene().getWindow(),
                "¡Se ha detectado una amenaza!",
                "Win32:Evo-gen [Susp] ha sido bloqueado y movido al baúl de virus.",
                JToastCard.Type.DANGER,
                JToastCard.Position.BOTTOM_RIGHT,
                Duration.millis(5000)
            );
        });

        JButton btnSuccess = new JButton("✔ Guardado Exitoso (Bottom Right - Success)");
        btnSuccess.setStyle("-fx-background-color: #ecfdf5; -fx-text-fill: #065f46; -fx-border-color: #a7f3d0; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-font-weight: bold; -fx-padding: 7px 14px;");
        btnSuccess.setOnAction(e -> {
            JToastCard.show(
                getScene().getWindow(),
                "Operación Completada",
                "Todos los archivos se han sincronizado con el servidor correctamente.",
                JToastCard.Type.SUCCESS,
                JToastCard.Position.BOTTOM_RIGHT,
                Duration.millis(4000)
            );
        });

        presetsRow1.getChildren().addAll(btnAvastDanger, btnSuccess);

        HBox presetsRow2 = new HBox(12);
        JButton btnTopRightWarn = new JButton("⚠ Advertencia de Memoria (Top Right - Warning)");
        btnTopRightWarn.setStyle("-fx-background-color: #fffbeb; -fx-text-fill: #92400e; -fx-border-color: #fde68a; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-font-weight: bold; -fx-padding: 7px 14px;");
        btnTopRightWarn.setOnAction(e -> {
            JToastCard.show(
                getScene().getWindow(),
                "Uso de Memoria Elevado",
                "El consumo de RAM ha superado el 85%. Se recomienda optimizar procesos.",
                JToastCard.Type.WARNING,
                JToastCard.Position.TOP_RIGHT,
                Duration.millis(4500)
            );
        });

        JButton btnTopRightInfo = new JButton("ℹ Nueva Actualización (Top Right - Info)");
        btnTopRightInfo.setStyle("-fx-background-color: #eff6ff; -fx-text-fill: #1e40af; -fx-border-color: #bfdbfe; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-font-weight: bold; -fx-padding: 7px 14px;");
        btnTopRightInfo.setOnAction(e -> {
            JToastCard.show(
                getScene().getWindow(),
                "FxStyle v2.2.0 Disponible",
                "Se ha añadido el nuevo componente JToastCard con soporte de animaciones.",
                JToastCard.Type.INFO,
                JToastCard.Position.TOP_RIGHT,
                Duration.millis(4000)
            );
        });

        presetsRow2.getChildren().addAll(btnTopRightWarn, btnTopRightInfo);

        presetsBody.getChildren().addAll(presetsRow1, presetsRow2);
        presetsCard.setBody(presetsBody);
        content.getChildren().add(presetsCard);

        // 2. Interactive Playground Card
        JCard playgroundCard = new JCard("Playground Interactivo", "Personaliza el título, mensaje, duración, tipo de tarjeta y posición");
        VBox playBody = new VBox(20);
        playBody.setPadding(new Insets(20));

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(14);

        TextField titleInput = new TextField("Se ha detectado una amenaza");
        titleInput.setPrefWidth(320);
        TextField msgInput = new TextField("El archivo sospechoso ha sido neutralizado con éxito en el sistema.");
        msgInput.setPrefWidth(320);
        TextField durInput = new TextField("4000");
        durInput.setPrefWidth(120);

        CheckBox autoCloseCheck = new CheckBox("Auto-Cierre con temporizador y barra de progreso");
        autoCloseCheck.setSelected(true);
        autoCloseCheck.setStyle("-fx-font-size: 12.5px; -fx-text-fill: #334155; -fx-font-weight: bold;");

        grid.add(new Label("Título:"), 0, 0);
        grid.add(titleInput, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1);
        grid.add(msgInput, 1, 1);
        grid.add(new Label("Duración (ms):"), 0, 2);
        grid.add(durInput, 1, 2);
        grid.add(new Label("Modo:"), 0, 3);
        grid.add(autoCloseCheck, 1, 3);

        playBody.getChildren().add(grid);

        // Position Buttons - Bottom Group (Avast Upward Slide)
        Label bottomLabel = new Label("Posiciones Inferiores (Entrada de Abajo hacia Arriba):");
        bottomLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #334155;");

        HBox bottomBtnBox = new HBox(10);
        bottomBtnBox.getChildren().add(createTriggerBtn("Bottom Right (Danger)", "#fef2f2", "#991b1b", "#fca5a5", () -> triggerToast(titleInput, msgInput, durInput, autoCloseCheck.isSelected(), JToastCard.Type.DANGER, JToastCard.Position.BOTTOM_RIGHT)));
        bottomBtnBox.getChildren().add(createTriggerBtn("Bottom Right (Warning)", "#fffbeb", "#92400e", "#fde68a", () -> triggerToast(titleInput, msgInput, durInput, autoCloseCheck.isSelected(), JToastCard.Type.WARNING, JToastCard.Position.BOTTOM_RIGHT)));
        bottomBtnBox.getChildren().add(createTriggerBtn("Bottom Right (Success)", "#ecfdf5", "#065f46", "#a7f3d0", () -> triggerToast(titleInput, msgInput, durInput, autoCloseCheck.isSelected(), JToastCard.Type.SUCCESS, JToastCard.Position.BOTTOM_RIGHT)));
        bottomBtnBox.getChildren().add(createTriggerBtn("Bottom Left (Info)", "#eff6ff", "#1e40af", "#bfdbfe", () -> triggerToast(titleInput, msgInput, durInput, autoCloseCheck.isSelected(), JToastCard.Type.INFO, JToastCard.Position.BOTTOM_LEFT)));

        // Position Buttons - Top Group (Downward Slide)
        Label topLabel = new Label("Posiciones Superiores (Entrada de Arriba hacia Abajo):");
        topLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #334155; -fx-padding: 8px 0 0 0;");

        HBox topBtnBox = new HBox(10);
        topBtnBox.getChildren().add(createTriggerBtn("Top Right (Danger)", "#fef2f2", "#991b1b", "#fca5a5", () -> triggerToast(titleInput, msgInput, durInput, autoCloseCheck.isSelected(), JToastCard.Type.DANGER, JToastCard.Position.TOP_RIGHT)));
        topBtnBox.getChildren().add(createTriggerBtn("Top Right (Warning)", "#fffbeb", "#92400e", "#fde68a", () -> triggerToast(titleInput, msgInput, durInput, autoCloseCheck.isSelected(), JToastCard.Type.WARNING, JToastCard.Position.TOP_RIGHT)));
        topBtnBox.getChildren().add(createTriggerBtn("Top Right (Success)", "#ecfdf5", "#065f46", "#a7f3d0", () -> triggerToast(titleInput, msgInput, durInput, autoCloseCheck.isSelected(), JToastCard.Type.SUCCESS, JToastCard.Position.TOP_RIGHT)));
        topBtnBox.getChildren().add(createTriggerBtn("Top Center (Default)", "#f8fafc", "#1e293b", "#cbd5e1", () -> triggerToast(titleInput, msgInput, durInput, autoCloseCheck.isSelected(), JToastCard.Type.DEFAULT, JToastCard.Position.TOP_CENTER)));

        playBody.getChildren().addAll(bottomLabel, bottomBtnBox, topLabel, topBtnBox);
        playgroundCard.setBody(playBody);
        content.getChildren().add(playgroundCard);

        setContent(content);
    }

    private JButton createTriggerBtn(String text, String bgColor, String textColor, String borderColor, Runnable action) {
        JButton btn = new JButton(text);
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; -fx-border-color: " + borderColor + "; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-padding: 6px 12px;");
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private void triggerToast(TextField t, TextField m, TextField d, boolean autoClose, JToastCard.Type type, JToastCard.Position pos) {
        String title = t.getText();
        String msg = m.getText();
        int dur = 4000;
        try {
            dur = Integer.parseInt(d.getText());
        } catch (Exception ignored) {}

        if (!autoClose) {
            JToastCard.showSticky(getScene().getWindow(), title, msg, type, pos);
        } else {
            JToastCard.show(getScene().getWindow(), title, msg, type, pos, Duration.millis(dur));
        }
    }
}
