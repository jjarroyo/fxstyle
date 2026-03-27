package com.jjarroyo.components;

import javafx.scene.control.ProgressBar;

public class JProgressBar extends ProgressBar {

    public enum ProgressStyle {
        PRIMARY("progress-primary"),
        SUCCESS("progress-success"),
        DANGER("progress-danger"),
        WARNING("progress-warning"),
        INFO("progress-info"),
        DARK("progress-dark"),
        SECONDARY("progress-secondary");

        private final String styleClass;

        ProgressStyle(String styleClass) {
            this.styleClass = styleClass;
        }

        public String getStyleClass() {
            return styleClass;
        }
    }

    public enum ProgressSize {
        SM("progress-sm"),
        MD("progress-md"), // Default
        LG("progress-lg");

        private final String styleClass;

        ProgressSize(String styleClass) {
            this.styleClass = styleClass;
        }

        public String getStyleClass() {
            return styleClass;
        }
    }

    public JProgressBar() {
        super();
        init();
    }

    public JProgressBar(double progress) {
        super(progress);
        init();
    }

    private void init() {
        getStyleClass().add("j-progress-bar");
        getStyleClass().add(ProgressStyle.PRIMARY.getStyleClass()); // Default color
        getStyleClass().add(ProgressSize.MD.getStyleClass()); // Default size
    }

    public void setProgressStyle(ProgressStyle style) {
        // Remove all style classes
        for (ProgressStyle s : ProgressStyle.values()) {
            getStyleClass().remove(s.getStyleClass());
        }
        getStyleClass().add(style.getStyleClass());
    }
    
    public void setSize(ProgressSize size) {
        for (ProgressSize s : ProgressSize.values()) {
            getStyleClass().remove(s.getStyleClass());
        }
        getStyleClass().add(size.getStyleClass());
    }
}
