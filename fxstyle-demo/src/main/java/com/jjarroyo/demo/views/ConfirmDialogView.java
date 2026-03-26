package com.jjarroyo.demo.views;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JConfirmDialog;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JToast;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class ConfirmDialogView extends ScrollPane {

    public ConfirmDialogView() {
        getStyleClass().add("j-scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));

        VBox content = new VBox(24);

        // Header
        VBox header = new VBox(8);
        Label title = new Label("Confirm Dialog");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Diálogos de confirmación con variantes, iconos y callbacks.");
        subtitle.getStyleClass().add("page-subtitle");
        header.getChildren().addAll(title, subtitle);
        content.getChildren().add(header);

        // 1. Uso básico (one-liner)
        JCard card1 = new JCard("Uso Básico", "Confirmación simple con una sola línea de código.");
        FlowPane box1 = new FlowPane(12, 12);
        JButton btn1 = new JButton("Confirmar acción");
        btn1.addClass("btn-primary");
        btn1.setOnAction(e -> {
            JConfirmDialog.show("¿Estás seguro?", () -> {
                JToast.show(getScene().getWindow(), "Acción confirmada", JToast.Type.SUCCESS, JToast.Position.BOTTOM_RIGHT, 3000);
            });
        });
        box1.getChildren().add(btn1);
        card1.setBody(box1);
        content.getChildren().add(card1);

        // 2. Confirmación con mensaje
        JCard card2 = new JCard("Con Mensaje", "Diálogo con título y mensaje descriptivo.");
        FlowPane box2 = new FlowPane(12, 12);
        JButton btn2 = new JButton("Guardar cambios");
        btn2.addClass("btn-success");
        btn2.setOnAction(e -> {
            JConfirmDialog.show(
                "¿Guardar cambios?",
                "Los datos modificados se guardarán permanentemente.",
                () -> {
                    JToast.show(getScene().getWindow(), "Cambios guardados", JToast.Type.SUCCESS, JToast.Position.BOTTOM_RIGHT, 3000);
                }
            );
        });
        box2.getChildren().add(btn2);
        card2.setBody(box2);
        content.getChildren().add(card2);

        // 3. Variantes de tipo
        JCard card3 = new JCard("Variantes de Tipo", "INFO, SUCCESS, WARNING y DANGER con iconos automáticos.");
        FlowPane box3 = new FlowPane(12, 12);

        JButton btnInfo = new JButton("Info");
        btnInfo.addClass("btn-primary");
        btnInfo.setOnAction(e -> {
            JConfirmDialog.show("Información", "Se enviará una notificación a todos los usuarios.", JConfirmDialog.Type.INFO, () -> {
                JToast.show(getScene().getWindow(), "Notificación enviada", JToast.Type.INFO, JToast.Position.BOTTOM_RIGHT, 3000);
            });
        });

        JButton btnSuccess = new JButton("Success");
        btnSuccess.addClass("btn-success");
        btnSuccess.setOnAction(e -> {
            JConfirmDialog.show("Confirmar aprobación", "¿Aprobar la solicitud #1234?", JConfirmDialog.Type.SUCCESS, () -> {
                JToast.show(getScene().getWindow(), "Solicitud aprobada", JToast.Type.SUCCESS, JToast.Position.BOTTOM_RIGHT, 3000);
            });
        });

        JButton btnWarning = new JButton("Warning");
        btnWarning.addClass("btn-warning");
        btnWarning.setOnAction(e -> {
            JConfirmDialog.show("Advertencia", "Esta operación cambiará los permisos de 50 usuarios.", JConfirmDialog.Type.WARNING, () -> {
                JToast.show(getScene().getWindow(), "Permisos actualizados", JToast.Type.WARNING, JToast.Position.BOTTOM_RIGHT, 3000);
            });
        });

        JButton btnDanger = new JButton("Danger");
        btnDanger.addClass("btn-danger");
        btnDanger.setOnAction(e -> {
            JConfirmDialog.show("¿Eliminar registro?", "Esta acción no se puede deshacer.", JConfirmDialog.Type.DANGER, () -> {
                JToast.show(getScene().getWindow(), "Registro eliminado", JToast.Type.DANGER, JToast.Position.BOTTOM_RIGHT, 3000);
            });
        });

        box3.getChildren().addAll(btnInfo, btnSuccess, btnWarning, btnDanger);
        card3.setBody(box3);
        content.getChildren().add(card3);

        // 4. Shortcut deleteDialog
        JCard card4 = new JCard("Shortcut: showDelete", "Método estático pre-configurado para eliminar.");
        FlowPane box4 = new FlowPane(12, 12);
        JButton btn4 = new JButton("Eliminar usuario", new JIcon[]{JIcon.DELETE}[0]);
        btn4.addClass("btn-danger");
        btn4.setOnAction(e -> {
            JConfirmDialog.showDelete("¿Eliminar el usuario 'Jorge Arroyo'? Esta acción es irreversible.", () -> {
                JToast.show(getScene().getWindow(), "Usuario eliminado correctamente", JToast.Type.DANGER, JToast.Position.BOTTOM_RIGHT, 3000);
            });
        });
        box4.getChildren().add(btn4);
        card4.setBody(box4);
        content.getChildren().add(card4);

        // 5. Acción asíncrona con loading
        JCard card5 = new JCard("Acción Asíncrona", "El botón muestra un spinner mientras la acción se procesa en background.");
        FlowPane box5 = new FlowPane(12, 12);
        JButton btn5 = new JButton("Procesar con loading");
        btn5.addClass("btn-primary");
        btn5.setOnAction(e -> {
            new JConfirmDialog()
                .setTitle("Procesar datos")
                .setMessage("Se generará un reporte completo. Esto puede tardar unos segundos.")
                .setType(JConfirmDialog.Type.INFO)
                .setConfirmText("Generar")
                .setOnConfirmAsync(
                    () -> {
                        // Simular proceso largo
                        try { Thread.sleep(2000); } catch (InterruptedException ex) { }
                        return true;
                    },
                    success -> {
                        if (success) {
                            JToast.show(getScene().getWindow(), "Reporte generado exitosamente", JToast.Type.SUCCESS, JToast.Position.BOTTOM_RIGHT, 3000);
                        }
                    }
                )
                .show();
        });
        box5.getChildren().add(btn5);
        card5.setBody(box5);
        content.getChildren().add(card5);

        // 6. Personalización completa
        JCard card6 = new JCard("Personalización Completa", "Fluent API con textos, icono y callbacks personalizados.");
        FlowPane box6 = new FlowPane(12, 12);
        JButton btn6 = new JButton("Diálogo personalizado");
        btn6.addClass("btn-primary");
        btn6.setOnAction(e -> {
            new JConfirmDialog()
                .setTitle("Enviar reporte")
                .setMessage("El reporte mensual se enviará a gerencia@empresa.com")
                .setType(JConfirmDialog.Type.SUCCESS)
                .setIcon(JIcon.EMAIL)
                .setConfirmText("Enviar ahora")
                .setCancelText("Más tarde")
                .setOnConfirm(() -> {
                    JToast.show(getScene().getWindow(), "Reporte enviado", JToast.Type.SUCCESS, JToast.Position.BOTTOM_RIGHT, 3000);
                })
                .setOnCancel(() -> {
                    JToast.show(getScene().getWindow(), "Envío pospuesto", JToast.Type.INFO, JToast.Position.BOTTOM_RIGHT, 3000);
                })
                .show();
        });
        box6.getChildren().add(btn6);
        card6.setBody(box6);
        content.getChildren().add(card6);

        setContent(content);
    }
}
