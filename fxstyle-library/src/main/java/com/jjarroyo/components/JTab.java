package com.jjarroyo.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public class JTab {

    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final ObjectProperty<Node> content = new SimpleObjectProperty<>();
    private final ObjectProperty<Node> graphic = new SimpleObjectProperty<>();
    private final ObjectProperty<Boolean> disable = new SimpleObjectProperty<>(false);

    public JTab() {
    }

    public JTab(String title, Node content) {
        setTitle(title);
        setContent(content);
    }
    
    public JTab(String title, Node graphic, Node content) {
        setTitle(title);
        setGraphic(graphic);
        setContent(content);
    }
    
    public JTab(String title, String description, Node content) {
        setTitle(title);
        setDescription(description);
        setContent(content);
    }

    public StringProperty titleProperty() { return title; }
    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }

    public StringProperty descriptionProperty() { return description; }
    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public ObjectProperty<Node> contentProperty() { return content; }
    public Node getContent() { return content.get(); }
    public void setContent(Node content) { this.content.set(content); }

    public ObjectProperty<Node> graphicProperty() { return graphic; }
    public Node getGraphic() { return graphic.get(); }
    public void setGraphic(Node graphic) { this.graphic.set(graphic); }
    
    public ObjectProperty<Boolean> disableProperty() { return disable; }
    public Boolean isDisable() { return disable.get(); }
    public void setDisable(Boolean disable) { this.disable.set(disable); }
}

