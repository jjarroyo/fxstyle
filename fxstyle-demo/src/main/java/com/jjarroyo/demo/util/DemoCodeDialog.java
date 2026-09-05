package com.jjarroyo.demo.util;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JModal;
import com.jjarroyo.components.JModal.Size;
import com.jjarroyo.components.JToast;
import com.jjarroyo.components.JToast.Type;
import com.jjarroyo.components.JToast.Position;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DemoCodeDialog {

    public static void showCode(String title, String codeSnippet) {
        VBox content = new VBox(16);
        content.setPadding(new Insets(16));

        Label infoLabel = new Label("Fragmento de código JavaFX listo para copiar:");
        infoLabel.getStyleClass().addAll("text-sm", "text-slate-500");

        TextArea textArea = new TextArea(codeSnippet.trim());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefRowCount(10);
        textArea.setStyle(
            "-fx-font-family: 'Consolas', 'Monaco', monospace; " +
            "-fx-font-size: 13px; " +
            "-fx-control-inner-background: #0f172a; " +
            "-fx-text-fill: #e2e8f0; " +
            "-fx-highlight-fill: #3b82f6; " +
            "-fx-highlight-text-fill: #ffffff;"
        );

        content.getChildren().addAll(infoLabel, textArea);

        JModal modal = new JModal("Código JavaFX — " + title, content, Size.MEDIUM);

        JButton copyBtn = new JButton("Copiar al Portapapeles", JIcon.CHECK);
        copyBtn.addClass("btn-primary");
        copyBtn.setOnAction(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent cc = new ClipboardContent();
            cc.putString(codeSnippet.trim());
            clipboard.setContent(cc);

            JToast.show(copyBtn.getScene().getWindow(), "Portapapeles", "¡Código copiado al portapapeles!", Type.SUCCESS, Position.TOP_RIGHT, 3000);
        });

        JButton closeBtn = new JButton("Cerrar");
        closeBtn.addClass("btn-secondary");
        closeBtn.setOnAction(e -> modal.close());

        HBox footer = new HBox(12);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.getChildren().addAll(copyBtn, closeBtn);
        modal.setFooter(footer);

        modal.show();
    }

    public static JButton createCodeButton(String title, String codeSnippet) {
        JButton btn = new JButton("Ver Código", JIcon.FILE_TEXT);
        btn.addClass("btn-sm", "btn-light-primary");
        btn.setOnAction(e -> showCode(title, codeSnippet));
        return btn;
    }
}
