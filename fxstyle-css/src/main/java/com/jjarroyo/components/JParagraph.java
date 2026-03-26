package com.jjarroyo.components;

import javafx.scene.control.Label;

public class JParagraph extends Label {

    public JParagraph() {
        super();
        init();
    }

    public JParagraph(String text) {
        super(text);
        init();
    }
    
    public JParagraph(String text, boolean wrap) {
        super(text);
        init();
        setWrapText(wrap);
    }

    private void init() {
        setWrapText(true);
        setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
        setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
    }
    
    public JParagraph text(String text) {
        setText(text);
        return this;
    }
    
    public JParagraph width(double width) {
        setPrefWidth(width);
        setMinWidth(width);
        setMaxWidth(width);
        return this;
    }
    
    public JParagraph setHtmlText(String htmlText) {
        setText(convertHtmlToText(htmlText));
        return this;
    }
    
    public JParagraph setAlignCenter() {
        setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        return this;
    }
    
    public JParagraph setAlignRight() {
        setTextAlignment(javafx.scene.text.TextAlignment.RIGHT);
        return this;
    }
    
    public JParagraph setAlignLeft() {
        setTextAlignment(javafx.scene.text.TextAlignment.LEFT);
        return this;
    }
    
    public JParagraph setJustify() {
        setTextAlignment(javafx.scene.text.TextAlignment.JUSTIFY);
        return this;
    }
    
    private String convertHtmlToText(String html) {
        if (html == null) return "";
        return html.replaceAll("<br\\s*/?>", "\n")
                   .replaceAll("<p[^>]*>", "\n")
                   .replaceAll("</p>", "\n")
                   .replaceAll("<[^>]+>", "")
                   .replaceAll("&nbsp;", " ")
                   .replaceAll("&amp;", "&")
                   .replaceAll("&lt;", "<")
                   .replaceAll("&gt;", ">")
                   .trim();
    }
    
    public JParagraph withStyle(String... styleClasses) {
        getStyleClass().addAll(styleClasses);
        return this;
    }
    
    public JParagraph addClass(String... styleClasses) {
        return withStyle(styleClasses);
    }
}
