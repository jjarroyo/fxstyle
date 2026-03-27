package com.jjarroyo.components;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class JTimeline extends VBox {

    private final ObservableList<Event> events = FXCollections.observableArrayList();
    private Orientation orientation = Orientation.VERTICAL;
    private Pane container;

    public JTimeline() {
        getStyleClass().add("j-timeline");
        rebuildLayout();

        events.addListener((ListChangeListener<Event>) c -> renderEvents());
    }

    public void setOrientation(Orientation orientation) {
        if (this.orientation != orientation) {
            this.orientation = orientation;
            rebuildLayout();
            renderEvents();
        }
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void addEvent(String title, String description, String time) {
        events.add(new Event(title, description, time));
    }

    public ObservableList<Event> getEvents() {
        return events;
    }

    private void rebuildLayout() {
        getChildren().clear();
        if (orientation == Orientation.VERTICAL) {
            container = new VBox();
            container.getStyleClass().add("j-timeline-vertical");
        } else {
            container = new HBox();
            container.getStyleClass().add("j-timeline-horizontal");
            ((HBox) container).setAlignment(Pos.TOP_LEFT);
        }
        getChildren().add(container);
    }

    private void renderEvents() {
        container.getChildren().clear();
        int total = events.size();

        for (int i = 0; i < total; i++) {
            Event event = events.get(i);
            boolean isLast = (i == total - 1);

            Pane eventBox;
            if (orientation == Orientation.VERTICAL) {
                eventBox = new HBox();
                ((HBox) eventBox).setAlignment(Pos.TOP_LEFT);
            } else {
                eventBox = new VBox();
                ((VBox) eventBox).setAlignment(Pos.TOP_LEFT);
                HBox.setHgrow(eventBox, Priority.ALWAYS); // Equal width in horizontal
            }
            eventBox.getStyleClass().add("j-timeline-event");

            // --- Indicator & Line Block ---
            Pane indicatorBlock;
            if (orientation == Orientation.VERTICAL) {
                indicatorBlock = new VBox();
                ((VBox) indicatorBlock).setAlignment(Pos.TOP_CENTER);
            } else {
                indicatorBlock = new HBox();
                ((HBox) indicatorBlock).setAlignment(Pos.CENTER_LEFT);
            }
            indicatorBlock.getStyleClass().add("j-timeline-indicator-block");

            // Indicator Icon/Circle
            StackPane iconPane = new StackPane();
            iconPane.getStyleClass().add("j-timeline-icon-pane");
            
            if (event.getColorClass() != null && !event.getColorClass().isEmpty()) {
                iconPane.getStyleClass().add(event.getColorClass());
            }

            if (event.getIcon() != null) {
                iconPane.getChildren().add(event.getIcon());
                iconPane.getStyleClass().add("has-icon");
            } else {
                Circle circle = new Circle(6);
                circle.getStyleClass().add("j-timeline-circle");
                if (event.getColorClass() != null && !event.getColorClass().isEmpty()) {
                    circle.getStyleClass().add(event.getColorClass());
                }
                iconPane.getChildren().add(circle);
            }

            indicatorBlock.getChildren().add(iconPane);

            // Connective Line
            if (!isLast) {
                Region line = new Region();
                line.getStyleClass().add("j-timeline-line");
                if (event.getColorClass() != null && !event.getColorClass().isEmpty()) {
                    line.getStyleClass().add(event.getColorClass());
                }

                if (orientation == Orientation.VERTICAL) {
                    VBox.setVgrow(line, Priority.ALWAYS);
                    // Slight padding to detach the line from the icon
                    VBox lineContainer = new VBox(line);
                    lineContainer.setPadding(new Insets(4, 0, 4, 0));
                    VBox.setVgrow(lineContainer, Priority.ALWAYS);
                    indicatorBlock.getChildren().add(lineContainer);
                } else {
                    HBox.setHgrow(line, Priority.ALWAYS);
                    // Slight padding to detach the line from the icon
                    HBox lineContainer = new HBox(line);
                    lineContainer.setPadding(new Insets(0, 4, 0, 4));
                    HBox.setHgrow(lineContainer, Priority.ALWAYS);
                    indicatorBlock.getChildren().add(lineContainer);
                }
            } else {
                // For layout consistency in the last item, particularly in horizontal
                if (orientation == Orientation.HORIZONTAL) {
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    indicatorBlock.getChildren().add(spacer);
                }
            }

            // --- Content Block ---
            VBox contentBlock = new VBox(4);
            contentBlock.getStyleClass().add("j-timeline-content-block");
            
            if (orientation == Orientation.VERTICAL) {
                contentBlock.setPadding(new Insets(0, 0, 24, 16));
            } else {
                contentBlock.setPadding(new Insets(16, 24, 0, 0));
            }

            Label titleLabel = new Label(event.getTitle());
            titleLabel.getStyleClass().add("j-timeline-title");

            HBox titleTimeBox = new HBox(8);
            titleTimeBox.setAlignment(Pos.CENTER_LEFT);
            titleTimeBox.getChildren().add(titleLabel);

            if (event.getTime() != null && !event.getTime().isEmpty()) {
                Label timeLabel = new Label(event.getTime());
                timeLabel.getStyleClass().add("j-timeline-time");
                
                if (orientation == Orientation.VERTICAL) {
                    // Time beside title in vertical
                    titleTimeBox.getChildren().add(timeLabel);
                    contentBlock.getChildren().add(titleTimeBox);
                } else {
                    // Time above title in horizontal? Or below title. Below looks better usually.
                    // Actually let's put it beside in horizontal too, or separate. Let's do separate.
                    contentBlock.getChildren().add(titleLabel);
                    contentBlock.getChildren().add(timeLabel);
                }
            } else {
                contentBlock.getChildren().add(titleTimeBox);
            }

            if (event.getDescription() != null && !event.getDescription().isEmpty()) {
                Label descLabel = new Label(event.getDescription());
                descLabel.getStyleClass().add("j-timeline-desc");
                descLabel.setWrapText(true);
                contentBlock.getChildren().add(descLabel);
            }

            if (event.getCustomContent() != null) {
                VBox customBox = new VBox(event.getCustomContent());
                customBox.setPadding(new Insets(8, 0, 0, 0));
                contentBlock.getChildren().add(customBox);
            }

            // Combine
            eventBox.getChildren().addAll(indicatorBlock, contentBlock);
            container.getChildren().add(eventBox);
        }
    }

    public static class Event {
        private String title;
        private String description;
        private String time;
        private Node icon;
        private String colorClass; // 'primary', 'success', 'danger', etc.
        private Node customContent;

        public Event(String title, String description, String time) {
            this.title = title;
            this.description = description;
            this.time = time;
        }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }

        public Node getIcon() { return icon; }
        public void setIcon(Node icon) { this.icon = icon; }

        public String getColorClass() { return colorClass; }
        public void setColorClass(String colorClass) { this.colorClass = colorClass; }

        public Node getCustomContent() { return customContent; }
        public void setCustomContent(Node customContent) { this.customContent = customContent; }
    }
}
