package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JIcon;
import com.jjarroyo.components.JLabel;
import com.jjarroyo.components.JParagraph;
import com.jjarroyo.components.JTitleBar;
import com.jjarroyo.components.JButton;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Region;

public class TitleBarView extends ScrollPane {

    public TitleBarView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));
        setFitToWidth(true);
        setContent(content);

        // Page Header
        VBox pageHeader = new VBox(8);
        JLabel title = new JLabel("JTitleBar Component")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Barra de título personalizada con arrastre y animaciones nativas.")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        // Description Details
        JCard detailsCard = new JCard();
        VBox descBox = new VBox(12);
        JParagraph p1 = new JParagraph("JTitleBar reemplaza la barra nativa del OS. Permite arrastrar la ventana, doble clic para maximizar e integra los botones de control (minimizar, maximizar/restaurar, cerrar).");
        JParagraph p2 = new JParagraph("Incluye una animación por GPU (Scale Transition) que elimina el flash de renderizado al maximizar en JavaFX, simulando el comportamiento nativo fluído de Windows 11 DWM (Desktop Window Manager).");
        descBox.getChildren().addAll(p1, p2);
        detailsCard.setBody(descBox);
        content.getChildren().add(detailsCard);

        // Launch Demo Button
        JCard demoCard = new JCard();
        demoCard.setTitle("Demostración en Vivo");
        VBox demoContent = new VBox(16);
        
        JParagraph demoDesc = new JParagraph("Abre una instancia separada (Stage) con el JTitleBar instalado para probar el comportamiento de maximizar/restaurar con la animación en tiempo real.");
        
        JButton launchBtn = new JButton("Abrir Ventana con JTitleBar", JIcon.MONITOR);
        launchBtn.setOnAction(e -> launchDemoWindow());
        
        demoContent.getChildren().addAll(demoDesc, launchBtn);
        demoCard.setBody(demoContent);
        
        content.getChildren().add(demoCard);
    }

    private void launchDemoWindow() {
        Stage demoStage = new Stage();
        
        // 1. Content for the demo window
        VBox appContent = new VBox(24);
        appContent.setPadding(new Insets(40));
        appContent.setStyle("-fx-background-color: -color-bg-body;"); // Use theme background
        
        JLabel title = new JLabel("Ventana Demo")
            .withStyle("text-3xl", "font-bold", "text-slate-800");
            
        JParagraph desc = new JParagraph("Prueba a arrastrar esta ventana o usa el botón de maximizar en la barra de título superior. Apreciarás la animación fluída mediante Scale Transform sin distorsión de imagen.");
        
        JCard contentCard = new JCard("Contenido de Prueba", new JParagraph("El contenido se renderiza nítido a máxima resolución de forma instantánea."));
        
        appContent.getChildren().addAll(title, desc, contentCard);

        // 2. Create the Custom TitleBar
        JTitleBar titleBar = new JTitleBar()
            .setTitle("JJArroyoFX - Demo Custom Window")
            .setIcon(JIcon.MONITOR.view())
            .setOnCloseRequest(() -> demoStage.close()); // Just close the demo stage

        // 3. Install
        JTitleBar.install(demoStage, titleBar, appContent, 800, 600);
        
        demoStage.show();
    }
}
