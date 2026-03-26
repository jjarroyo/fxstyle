package com.jjarroyo.components;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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
import javafx.geometry.Orientation;

import java.util.function.BiConsumer;

public class JStepper extends VBox {

    private final ObservableList<Step> steps = FXCollections.observableArrayList();
    private final IntegerProperty currentStepIndex = new SimpleIntegerProperty(0);
    private Orientation orientation = Orientation.HORIZONTAL;
    
    private Pane headerBox;
    private final StackPane contentPane;
    private final HBox footerBox;
    
    private final JButton backButton;
    private final JButton nextButton;
    private final JButton finishButton;

    private BiConsumer<Integer, Integer> onStepChangeCallback;
    private Runnable onFinishCallback;

    public JStepper() {
        getStyleClass().add("j-stepper");
        setSpacing(24);

        // Initialize default horizontal layout
        headerBox = new HBox();
        headerBox.getStyleClass().add("j-stepper-header");
        ((HBox) headerBox).setAlignment(Pos.CENTER);
        ((HBox) headerBox).setSpacing(8);

        contentPane = new StackPane();
        contentPane.getStyleClass().add("j-stepper-content");
        VBox.setVgrow(contentPane, Priority.ALWAYS);

        footerBox = new HBox();
        footerBox.getStyleClass().add("j-stepper-footer");
        footerBox.setAlignment(Pos.CENTER_RIGHT);
        footerBox.setSpacing(12);

        backButton = new JButton("Back");
        backButton.addClass("btn-secondary");
        backButton.setOnAction(e -> previousStep());

        nextButton = new JButton("Next");
        // default primary
        nextButton.setOnAction(e -> nextStep());

        finishButton = new JButton("Finish");
        finishButton.addClass("btn-success");
        finishButton.setOnAction(e -> finish());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        footerBox.getChildren().addAll(backButton, spacer, nextButton, finishButton);

        rebuildLayout();

        steps.addListener((ListChangeListener<Step>) c -> {
            renderHeader();
            updateContent();
            updateButtons();
        });

        currentStepIndex.addListener((obs, oldVal, newVal) -> {
            renderHeader();
            updateContent();
            updateButtons();
            if (onStepChangeCallback != null && oldVal != null && newVal != null) {
                onStepChangeCallback.accept(oldVal.intValue(), newVal.intValue());
            }
        });
    }

    public void addStep(String title, String subtitle, Node content) {
        steps.add(new Step(title, subtitle, content));
    }

    public void addStep(Step step) {
        steps.add(step);
    }

    public void setSteps(Iterable<Step> newSteps) {
        steps.clear();
        for (Step s : newSteps) {
            steps.add(s);
        }
    }

    public void setOrientation(Orientation orientation) {
        if (this.orientation != orientation) {
            this.orientation = orientation;
            rebuildLayout();
            renderHeader(); // Re-render to adjust elements for the new orientation
        }
    }

    public Orientation getOrientation() {
        return orientation;
    }

    private void rebuildLayout() {
        getChildren().clear();
        
        if (orientation == Orientation.HORIZONTAL) {
            headerBox = new HBox();
            headerBox.getStyleClass().add("j-stepper-header");
            ((HBox) headerBox).setAlignment(Pos.CENTER);
            ((HBox) headerBox).setSpacing(8);
            
            getChildren().addAll(headerBox, contentPane, footerBox);
        } else {
            headerBox = new VBox();
            headerBox.getStyleClass().add("j-stepper-header-vertical");
            ((VBox) headerBox).setAlignment(Pos.TOP_LEFT);
            ((VBox) headerBox).setSpacing(8);
            
            VBox rightSide = new VBox(24);
            rightSide.getChildren().addAll(contentPane, footerBox);
            VBox.setVgrow(contentPane, Priority.ALWAYS);
            HBox.setHgrow(rightSide, Priority.ALWAYS);
            
            HBox mainLayout = new HBox(32);
            mainLayout.getChildren().addAll(headerBox, rightSide);
            getChildren().add(mainLayout);
        }
    }

    public ObservableList<Step> getSteps() {
        return steps;
    }

    public int getCurrentStepIndex() {
        return currentStepIndex.get();
    }

    public void setCurrentStepIndex(int index) {
        if (index >= 0 && index < steps.size()) {
            currentStepIndex.set(index);
        }
    }

    public void nextStep() {
        if (currentStepIndex.get() < steps.size() - 1) {
            currentStepIndex.set(currentStepIndex.get() + 1);
        }
    }

    public void previousStep() {
        if (currentStepIndex.get() > 0) {
            currentStepIndex.set(currentStepIndex.get() - 1);
        }
    }

    public void finish() {
        if (onFinishCallback != null) {
            onFinishCallback.run();
        }
    }

    // Callbacks
    public void setOnStepChange(BiConsumer<Integer, Integer> callback) {
        this.onStepChangeCallback = callback;
    }

    public void setOnFinish(Runnable callback) {
        this.onFinishCallback = callback;
    }

    // Buttons Customization
    public JButton getBackButton() { return backButton; }
    public JButton getNextButton() { return nextButton; }
    public JButton getFinishButton() { return finishButton; }
    
    // Areas access
    public Pane getHeaderBox() { return headerBox; }
    public StackPane getContentPane() { return contentPane; }
    public HBox getFooterBox() { return footerBox; }

    private void renderHeader() {
        headerBox.getChildren().clear();
        int total = steps.size();
        for (int i = 0; i < total; i++) {
            Step step = steps.get(i);
            boolean isCompleted = i < currentStepIndex.get();
            boolean isActive = i == currentStepIndex.get();

            Pane stepBox;
            if (orientation == Orientation.HORIZONTAL) {
                stepBox = new VBox();
                ((VBox) stepBox).setAlignment(Pos.CENTER);
                ((VBox) stepBox).setSpacing(8);
            } else {
                stepBox = new HBox();
                ((HBox) stepBox).setAlignment(Pos.CENTER_LEFT);
                ((HBox) stepBox).setSpacing(16);
            }
            stepBox.getStyleClass().add("j-stepper-step-box");

            StackPane iconPane = new StackPane();
            iconPane.getStyleClass().add("j-stepper-icon-pane");
            
            Circle circle = new Circle(16);
            circle.getStyleClass().add("j-stepper-circle");
            
            Label numberOrIcon = new Label();
            numberOrIcon.getStyleClass().add("j-stepper-number");

            if (isCompleted) {
                stepBox.getStyleClass().add("completed");
                iconPane.getStyleClass().add("completed");
                circle.getStyleClass().add("completed");
                numberOrIcon.setGraphic(JIcon.CHECK.view());
                numberOrIcon.getStyleClass().add("completed");
            } else if (isActive) {
                stepBox.getStyleClass().add("active");
                iconPane.getStyleClass().add("active");
                circle.getStyleClass().add("active");
                numberOrIcon.setText(String.valueOf(i + 1));
                numberOrIcon.getStyleClass().add("active");
            } else {
                stepBox.getStyleClass().add("pending");
                iconPane.getStyleClass().add("pending");
                circle.getStyleClass().add("pending");
                numberOrIcon.setText(String.valueOf(i + 1));
                numberOrIcon.getStyleClass().add("pending");
            }

            iconPane.getChildren().addAll(circle, numberOrIcon);

            Label titleLabel = new Label(step.getTitle());
            titleLabel.getStyleClass().add("j-stepper-title");
            
            if (isActive) titleLabel.getStyleClass().add("active");
            else if (isCompleted) titleLabel.getStyleClass().add("completed");
            else titleLabel.getStyleClass().add("pending");

            VBox textsBox = new VBox(4);
            textsBox.setAlignment(orientation == Orientation.HORIZONTAL ? Pos.CENTER : Pos.CENTER_LEFT);
            textsBox.getChildren().add(titleLabel);
            
            if (step.getSubtitle() != null && !step.getSubtitle().isEmpty()) {
                Label subtitleLabel = new Label(step.getSubtitle());
                subtitleLabel.getStyleClass().add("j-stepper-subtitle");
                textsBox.getChildren().add(subtitleLabel);
            }
            
            stepBox.getChildren().addAll(iconPane, textsBox);

            headerBox.getChildren().add(stepBox);

            if (i < total - 1) {
                Region line = new Region();
                line.getStyleClass().add("j-stepper-line");
                if (isCompleted) {
                    line.getStyleClass().add("completed");
                } else {
                    line.getStyleClass().add("pending");
                }
                
                if (orientation == Orientation.HORIZONTAL) {
                    HBox.setHgrow(line, Priority.ALWAYS);
                    VBox lineContainer = new VBox(line);
                    lineContainer.setAlignment(Pos.TOP_CENTER);
                    lineContainer.setPadding(new Insets(16, 0, 0, 0));
                    HBox.setHgrow(lineContainer, Priority.ALWAYS);
                    headerBox.getChildren().add(lineContainer);
                } else {
                    VBox.setVgrow(line, Priority.ALWAYS);
                    line.setMinHeight(24);
                    HBox lineContainer = new HBox(line);
                    lineContainer.setAlignment(Pos.CENTER_LEFT);
                    lineContainer.setPadding(new Insets(4, 0, 4, 15)); // Align vertically under the circle center (16 radius = width 32)
                    headerBox.getChildren().add(lineContainer);
                }
            }
        }
    }

    private void updateContent() {
        contentPane.getChildren().clear();
        if (currentStepIndex.get() >= 0 && currentStepIndex.get() < steps.size()) {
            Node content = steps.get(currentStepIndex.get()).getContent();
            if (content != null) {
                contentPane.getChildren().add(content);
            }
        }
    }

    private void updateButtons() {
        int idx = currentStepIndex.get();
        int size = steps.size();

        backButton.setDisable(idx <= 0);
        
        if (idx >= size - 1) {
            nextButton.setVisible(false);
            nextButton.setManaged(false);
            finishButton.setVisible(true);
            finishButton.setManaged(true);
        } else {
            nextButton.setVisible(true);
            nextButton.setManaged(true);
            finishButton.setVisible(false);
            finishButton.setManaged(false);
        }
    }

    public static class Step {
        private String title;
        private String subtitle;
        private Node content;

        public Step(String title, String subtitle, Node content) {
            this.title = title;
            this.subtitle = subtitle;
            this.content = content;
        }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getSubtitle() { return subtitle; }
        public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

        public Node getContent() { return content; }
        public void setContent(Node content) { this.content = content; }
    }
}
