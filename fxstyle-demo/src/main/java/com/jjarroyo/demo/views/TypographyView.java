package com.jjarroyo.demo.views;

import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JParagraph;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class TypographyView extends ScrollPane {

    public TypographyView() {
        getStyleClass().add("j-scroll-pane");
        setFitToWidth(true);
        setPadding(new Insets(24));

        VBox content = new VBox(24);
        
        // Header
        VBox header = new VBox(8);
        Label title = new Label("Typography");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Headings, body text, paragraphs, and more.");
        subtitle.getStyleClass().add("page-subtitle");
        header.getChildren().addAll(title, subtitle);
        content.getChildren().add(header);

        // Headings Card
        JCard headingCard = new JCard("Headings", "Standard HTML styled headings");
        VBox headingBody = new VBox(16);
        headingBody.setPadding(new Insets(24));
        
        headingBody.getChildren().add(createHeading("H1 - Heading 1", "text-3xl font-bold text-slate-800"));
        headingBody.getChildren().add(createHeading("H2 - Heading 2", "text-2xl font-bold text-slate-800"));
        headingBody.getChildren().add(createHeading("H3 - Heading 3", "text-xl font-bold text-slate-800"));
        headingBody.getChildren().add(createHeading("H4 - Heading 4", "text-lg font-bold text-slate-800"));
        headingBody.getChildren().add(createHeading("H5 - Heading 5", "text-base font-bold text-slate-800"));
        headingBody.getChildren().add(createHeading("H6 - Heading 6", "text-sm font-bold text-slate-800"));
        
        headingCard.setBody(headingBody);
        content.getChildren().add(headingCard);
        
        // Body Text Card
        JCard textCard = new JCard("Body Text", "Paragraphs and colors");
        VBox textBody = new VBox(16);
        textBody.setPadding(new Insets(24));
        
        Label p1 = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam in dui mauris. Vivamus hendrerit arcu sed erat molestie vehicula. Sed auctor neque eu tellus rhoncus ut eleifend nibh porttitor.");
        p1.setWrapText(true);
        p1.getStyleClass().add("text-base");
        p1.getStyleClass().add("text-slate-500");
        
        Label p2 = new Label("Ut in nulla enim. Phasellus molestie magna non est bibendum non venenatis nisl tempor. Suspendisse dictum feugiat nisl ut dapibus.");
        p2.setWrapText(true);
        p2.getStyleClass().addAll("text-sm", "text-slate-500");
        
        textBody.getChildren().addAll(p1, p2);
        
        // Colors
        VBox colorBox = new VBox(8);
        colorBox.getChildren().add(createStyledLabel("Primary Text", "text-primary font-bold"));
        colorBox.getChildren().add(createStyledLabel("Success Text", "text-success font-bold"));
        colorBox.getChildren().add(createStyledLabel("Danger Text", "text-danger font-bold"));
        colorBox.getChildren().add(createStyledLabel("Warning Text", "text-warning font-bold"));
        colorBox.getChildren().add(createStyledLabel("Info Text", "text-info font-bold"));
        colorBox.getChildren().add(createStyledLabel("Muted Text", "text-slate-500"));
        
        textBody.getChildren().add(colorBox);
        
        textCard.setBody(textBody);
        content.getChildren().add(textCard);
        
        // JParagraph Card
        JCard paragraphCard = new JCard("JParagraph", "Componente de párrafo con alineación y estilos");
        VBox paragraphBody = new VBox(20);
        paragraphBody.setPadding(new Insets(24));
        
        // Left aligned
        Label lblLeft = new Label("Alineación Izquierda");
        lblLeft.getStyleClass().addAll("text-sm", "font-bold", "text-slate-600");
        JParagraph pLeft = new JParagraph("Este párrafo está alineado a la izquierda. JParagraph extiende Label con soporte para wrap automático, alineación y estilos fluidos.", true);
        pLeft.setAlignLeft();
        pLeft.withStyle("text-base", "text-slate-500");
        
        // Center aligned
        Label lblCenter = new Label("Alineación Centro");
        lblCenter.getStyleClass().addAll("text-sm", "font-bold", "text-slate-600");
        JParagraph pCenter = new JParagraph("Este párrafo está centrado. Ideal para títulos descriptivos y textos de presentación que necesitan enfoque visual.", true);
        pCenter.setAlignCenter();
        pCenter.withStyle("text-base", "text-slate-500");
        
        // Right aligned
        Label lblRight = new Label("Alineación Derecha");
        lblRight.getStyleClass().addAll("text-sm", "font-bold", "text-slate-600");
        JParagraph pRight = new JParagraph("Este párrafo está alineado a la derecha. Útil para firmas, fechas o contenido secundario.", true);
        pRight.setAlignRight();
        pRight.withStyle("text-base", "text-slate-500");
        
        // Justified
        Label lblJustify = new Label("Justificado");
        lblJustify.getStyleClass().addAll("text-sm", "font-bold", "text-slate-600");
        JParagraph pJustify = new JParagraph("Este párrafo está justificado. El texto se distribuye uniformemente entre los márgenes izquierdo y derecho, creando un aspecto limpio y profesional similar al de un periódico o revista.", true);
        pJustify.setJustify();
        pJustify.withStyle("text-base", "text-slate-500");
        
        // Styled
        Label lblStyled = new Label("Con Estilos");
        lblStyled.getStyleClass().addAll("text-sm", "font-bold", "text-slate-600");
        JParagraph pStyled = new JParagraph("Este párrafo usa la API fluida withStyle() para aplicar múltiples clases CSS de forma encadenada.", true);
        pStyled.setAlignLeft();
        pStyled.withStyle("text-lg", "text-primary", "font-bold");
        
        paragraphBody.getChildren().addAll(
            lblLeft, pLeft,
            lblCenter, pCenter,
            lblRight, pRight,
            lblJustify, pJustify,
            lblStyled, pStyled
        );
        
        paragraphCard.setBody(paragraphBody);
        content.getChildren().add(paragraphCard);
        
        setContent(content);
    }
    
    private Label createHeading(String text, String classes) {
        Label lbl = new Label(text);
        lbl.getStyleClass().addAll(classes.split(" "));
        return lbl;
    }
    
    private Label createStyledLabel(String text, String classes) {
        Label lbl = new Label(text);
        lbl.getStyleClass().addAll(classes.split(" "));
        return lbl;
    }
}
