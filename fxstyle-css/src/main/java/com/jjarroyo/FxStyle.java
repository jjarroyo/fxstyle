package com.jjarroyo;

import javafx.scene.Scene;
import java.util.Objects;

/**
 * Main entry point for the FxStyle Design System.
 * Use this class to initialize the theme and other global settings.
 */
public class FxStyle {

    /**
     * Initializes the FxStyle theme for the given Scene.
     * This loads the main stylesheets required for the components to look correct.
     *
     * @param scene The JavaFX Scene to apply the theme to.
     */
    public static void init(Scene scene) {
        if (scene == null) {
            throw new IllegalArgumentException("Scene cannot be null");
        }
        
        String styles = Objects.requireNonNull(FxStyle.class.getResource("/fxstyle.css")).toExternalForm();
        
        if (!scene.getStylesheets().contains(styles)) {
            scene.getStylesheets().add(styles);
        }
    }

    private static javafx.scene.layout.StackPane modalContainer;

    /**
     * Sets the container where modals will be added.
     * @param container The StackPane to use as the modal root.
     */
    public static void setModalContainer(javafx.scene.layout.StackPane container) {
        modalContainer = container;
    }

    /**
     * Gets the registered modal container.
     * @return The modal container, or null if not set.
     */
    public static javafx.scene.layout.StackPane getModalContainer() {
        return modalContainer;
    }
}
