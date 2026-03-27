package com.jjarroyo.components;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Custom Title Bar component that replaces the native Windows title bar.
 * Supports light and dark mode via the theme's CSS variables.
 * 
 * <p>Features:</p>
 * <ul>
 *   <li>Window dragging via the title bar</li>
 *   <li>Double-click to maximize/restore</li>
 *   <li>Minimize, maximize/restore, and close buttons with SVG icons</li>
 *   <li>Smooth scale+translate animation for maximize/restore</li>
 *   <li>Fluent builder API</li>
 * </ul>
 * 
 * <p>Usage:</p>
 * <pre>{@code
 * JTitleBar titleBar = new JTitleBar()
 *     .setTitle("My Application")
 *     .setIcon(new JIcon("feather-home"));
 * 
 * JTitleBar.install(primaryStage, titleBar, myContentNode);
 * }</pre>
 */
public class JTitleBar extends HBox {

    // ── SVG paths for window control buttons (Windows 11 style) ──
    private static final String SVG_MINIMIZE = "M0 5h10v1H0z";
    private static final String SVG_MAXIMIZE = "M0 0h10v10H0V0zm1 1v8h8V1H1z";
    private static final String SVG_RESTORE = "M2 0h8v8h-2v2H0V2h2V0zm1 1v1h5v5h1V1H3zM1 3v6h6V3H1z";
    private static final String SVG_CLOSE = "M1 0L5 4L9 0L10 1L6 5L10 9L9 10L5 6L1 10L0 9L4 5L0 1z";

    // ── Internal structure ──
    private final HBox leftBox;
    private final HBox rightBox;
    private final HBox windowButtonsBox;
    private final Label titleLabel;
    private final SVGPath maximizeIcon;

    // ── Window state ──
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isMaximized = false;
    private double prevX, prevY, prevWidth, prevHeight;
    private Timeline transitionTimeline;
    private boolean isAnimating = false;
    private boolean animated = true;
    private Runnable onCloseRequest;
    private VBox rootContainer; // The wrapper VBox (set by install())
    private double cornerRadius = 10;

    /**
     * Creates a new JTitleBar with default settings.
     */
    public JTitleBar() {
        getStyleClass().add("j-titlebar");
        setAlignment(Pos.CENTER_LEFT);

        // ── Left: icon + title ──
        leftBox = new HBox(8);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.getStyleClass().add("j-titlebar-left");

        titleLabel = new Label();
        titleLabel.getStyleClass().add("j-titlebar-title");
        leftBox.getChildren().add(titleLabel);

        // ── Spacer ──
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // ── Right: custom nodes ──
        rightBox = new HBox(8);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.getStyleClass().add("j-titlebar-right");

        // ── Window control buttons ──
        windowButtonsBox = new HBox(0);
        windowButtonsBox.setAlignment(Pos.CENTER_RIGHT);
        windowButtonsBox.getStyleClass().add("j-titlebar-buttons");

        maximizeIcon = new SVGPath();
        maximizeIcon.setContent(SVG_MAXIMIZE);
        maximizeIcon.getStyleClass().add("j-titlebar-btn-icon");

        windowButtonsBox.getChildren().addAll(
            createWindowButton(SVG_MINIMIZE, "j-titlebar-btn-minimize", this::handleMinimize),
            createWindowButton(maximizeIcon, "j-titlebar-btn-maximize", this::handleMaximize),
            createWindowButton(SVG_CLOSE, "j-titlebar-btn-close", this::handleClose)
        );

        getChildren().addAll(leftBox, spacer, rightBox, windowButtonsBox);

        // ── Dragging + double click ──
        setupDragging();
    }

    // ═══════════════════════════════════════════════════════════════════
    // PUBLIC API (Fluent Builder)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Sets the title text displayed in the title bar.
     * @param title The title text.
     * @return this for chaining.
     */
    public JTitleBar setTitle(String title) {
        titleLabel.setText(title);
        return this;
    }

    /**
     * Sets an icon node at the start of the title bar (before the title text).
     * @param icon Any Node (JIcon, ImageView, SVGPath, etc.)
     * @return this for chaining.
     */
    public JTitleBar setIcon(Node icon) {
        // Remove previous icon if any
        leftBox.getChildren().removeIf(n -> n.getStyleClass().contains("j-titlebar-icon"));
        if (icon != null) {
            icon.getStyleClass().add("j-titlebar-icon");
            leftBox.getChildren().add(0, icon);
        }
        return this;
    }

    /**
     * Adds a custom node to the right side of the title bar (before window buttons).
     * Use this for search bars, user profiles, notification icons, etc.
     * @param node The node to add.
     * @return this for chaining.
     */
    public JTitleBar addRightNode(Node node) {
        if (node != null) {
            rightBox.getChildren().add(node);
        }
        return this;
    }

    /**
     * Sets the action to execute when the close button is clicked.
     * Default is {@code Platform.exit()}.
     * @param action The close action.
     * @return this for chaining.
     */
    public JTitleBar setOnCloseRequest(Runnable action) {
        this.onCloseRequest = action;
        return this;
    }

    /**
     * Enables or disables animations for maximize/restore/minimize/close.
     * @param animated true to enable animations (default), false for instant transitions.
     * @return this for chaining.
     */
    public JTitleBar setAnimated(boolean animated) {
        this.animated = animated;
        return this;
    }

    /**
     * Returns whether the window is currently maximized.
     */
    public boolean isMaximized() {
        return isMaximized;
    }

    // ═══════════════════════════════════════════════════════════════════
    // STATIC INSTALLER
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Installs the JTitleBar on a Stage. This method:
     * <ul>
     *   <li>Sets the stage style to TRANSPARENT</li>
     *   <li>Creates a VBox wrapper with the title bar + content</li>
     *   <li>Applies rounded corners, border, and shadow</li>
     *   <li>Configures the scene with transparent fill</li>
     * </ul>
     *
     * @param stage    The primary stage (must NOT have been shown yet, or have TRANSPARENT style).
     * @param titleBar The JTitleBar instance.
     * @param content  The main content node of the application.
     */
    public static void install(Stage stage, JTitleBar titleBar, Node content) {
        install(stage, titleBar, content, 800, 600);
    }

    /**
     * Installs the JTitleBar with custom initial dimensions.
     *
     * @param stage    The primary stage.
     * @param titleBar The JTitleBar instance.
     * @param content  The main content node.
     * @param width    Initial window width.
     * @param height   Initial window height.
     */
    public static void install(Stage stage, JTitleBar titleBar, Node content, double width, double height) {
        // Root container
        VBox root = new VBox();
        root.getStyleClass().add("j-titlebar-container");
        
        // Content grows to fill
        if (content instanceof Region) {
            VBox.setVgrow(content, Priority.ALWAYS);
        }
        root.getChildren().addAll(titleBar, content);

        // Link the root container for animations
        titleBar.rootContainer = root;

        // Scene
        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.TRANSPARENT);

        // Stage
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);

        // Apply theme if available
        try {
            String themeUrl = JTitleBar.class.getResource("/fxstyle.css").toExternalForm();
            if (!scene.getStylesheets().contains(themeUrl)) {
                scene.getStylesheets().add(themeUrl);
            }
        } catch (Exception ignored) {
            // Theme CSS not found, continue without it
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // WINDOW CONTROL HANDLERS
    // ═══════════════════════════════════════════════════════════════════

    private void handleMinimize() {
        Stage stage = getStage();
        if (stage == null) return;

        if (animated && rootContainer != null) {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), rootContainer);
            st.setToX(0.85);
            st.setToY(0.85);
            st.setInterpolator(Interpolator.EASE_BOTH);
            st.setOnFinished(e -> {
                stage.setIconified(true);
                rootContainer.setScaleX(1.0);
                rootContainer.setScaleY(1.0);
            });
            st.play();
        } else {
            stage.setIconified(true);
        }
    }

    private void handleMaximize() {
        Stage stage = getStage();
        if (stage == null || isAnimating) return;

        double startX = stage.getX();
        double startY = stage.getY();
        double startW = stage.getWidth();
        double startH = stage.getHeight();

        double targetX, targetY, targetW, targetH;

        if (isMaximized) {
            // Restore
            targetX = prevX;
            targetY = prevY;
            targetW = prevWidth;
            targetH = prevHeight;
            isMaximized = false;
            maximizeIcon.setContent(SVG_MAXIMIZE);
        } else {
            // Maximize
            prevX = stage.getX();
            prevY = stage.getY();
            prevWidth = stage.getWidth();
            prevHeight = stage.getHeight();

            ObservableList<Screen> screens = Screen.getScreensForRectangle(prevX, prevY, prevWidth, prevHeight);
            Screen screen = screens.isEmpty() ? Screen.getPrimary() : screens.get(0);
            Rectangle2D bounds = screen.getVisualBounds();
            targetX = bounds.getMinX();
            targetY = bounds.getMinY();
            targetW = bounds.getWidth();
            targetH = bounds.getHeight();
            isMaximized = true;
            maximizeIcon.setContent(SVG_RESTORE);
        }

        // Update border radius
        updateContainerRadius();

        if (animated && rootContainer != null) {
            animateMaximize(stage, startX, startY, startW, startH, targetX, targetY, targetW, targetH);
        } else {
            stage.setX(targetX);
            stage.setY(targetY);
            stage.setWidth(targetW);
            stage.setHeight(targetH);
        }
    }

    private void handleClose() {
        if (onCloseRequest != null) {
            onCloseRequest.run();
            return;
        }

        Stage stage = getStage();
        if (stage == null) {
            Platform.exit();
            return;
        }

        if (animated && rootContainer != null) {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), rootContainer);
            st.setToX(0.96);
            st.setToY(0.96);
            st.setInterpolator(Interpolator.LINEAR);

            Timeline fade = new Timeline(
                new KeyFrame(Duration.millis(120),
                    new KeyValue(rootContainer.opacityProperty(), 0)
                )
            );
            fade.setOnFinished(e -> Platform.exit());

            st.play();
            fade.play();
        } else {
            Platform.exit();
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // MAXIMIZE ANIMATION (Scale + Translate approach)
    // ═══════════════════════════════════════════════════════════════════

    private void animateMaximize(Stage stage,
                                 double sX, double sY, double sW, double sH,
                                 double tX, double tY, double tW, double tH) {

        Interpolator easing = new Interpolator() {
            @Override
            protected double curve(double t) {
                return 1.0 - Math.pow(1.0 - t, 3); // Ease Out Cubic
            }
        };

        isAnimating = true;
        transitionTimeline = new Timeline();

        if (isMaximized) {
            // ── MAXIMIZING ──
            stage.setX(tX);
            stage.setY(tY);
            stage.setWidth(tW);
            stage.setHeight(tH);

            double ratioX = sW / tW;
            double ratioY = sH / tH;
            double offsetX = (sX + sW / 2.0) - (tX + tW / 2.0);
            double offsetY = (sY + sH / 2.0) - (tY + tH / 2.0);

            rootContainer.setScaleX(ratioX);
            rootContainer.setScaleY(ratioY);
            rootContainer.setTranslateX(offsetX);
            rootContainer.setTranslateY(offsetY);

            transitionTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(250),
                    new KeyValue(rootContainer.scaleXProperty(), 1.0, easing),
                    new KeyValue(rootContainer.scaleYProperty(), 1.0, easing),
                    new KeyValue(rootContainer.translateXProperty(), 0, easing),
                    new KeyValue(rootContainer.translateYProperty(), 0, easing)
                )
            );
        } else {
            // ── RESTORING ──
            double ratioX = tW / sW;
            double ratioY = tH / sH;
            double offsetX = (tX + tW / 2.0) - (sX + sW / 2.0);
            double offsetY = (tY + tH / 2.0) - (sY + sH / 2.0);

            transitionTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(250),
                    new KeyValue(rootContainer.scaleXProperty(), ratioX, easing),
                    new KeyValue(rootContainer.scaleYProperty(), ratioY, easing),
                    new KeyValue(rootContainer.translateXProperty(), offsetX, easing),
                    new KeyValue(rootContainer.translateYProperty(), offsetY, easing)
                )
            );
        }

        transitionTimeline.setOnFinished(e -> {
            if (!isMaximized) {
                stage.setX(tX);
                stage.setY(tY);
                stage.setWidth(tW);
                stage.setHeight(tH);
            }
            rootContainer.setScaleX(1.0);
            rootContainer.setScaleY(1.0);
            rootContainer.setTranslateX(0);
            rootContainer.setTranslateY(0);
            isAnimating = false;
        });

        transitionTimeline.play();
    }

    // ═══════════════════════════════════════════════════════════════════
    // DRAGGING
    // ═══════════════════════════════════════════════════════════════════

    private void setupDragging() {
        setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        setOnMouseDragged(event -> {
            Stage stage = getStage();
            if (stage == null) return;

            if (isMaximized) {
                // Restore while dragging, reposition relative to mouse
                double relativeX = xOffset / prevWidth;
                handleMaximize();
                // Adjust xOffset so window follows mouse proportionally
                xOffset = stage.getWidth() * relativeX;
            }
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleMaximize();
            }
        });
    }

    // ═══════════════════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════════════════

    private Stage getStage() {
        if (getScene() != null && getScene().getWindow() instanceof Stage) {
            return (Stage) getScene().getWindow();
        }
        return null;
    }

    private void updateContainerRadius() {
        if (rootContainer == null) return;
        if (isMaximized) {
            rootContainer.setStyle(
                "-fx-background-radius: 0; -fx-border-radius: 0;"
            );
            rootContainer.getStyleClass().add("j-titlebar-maximized");
        } else {
            rootContainer.setStyle(
                "-fx-background-radius: " + cornerRadius + "; -fx-border-radius: " + cornerRadius + ";"
            );
            rootContainer.getStyleClass().remove("j-titlebar-maximized");
        }
    }

    private HBox createWindowButton(String svgContent, String styleClass, Runnable action) {
        SVGPath svg = new SVGPath();
        svg.setContent(svgContent);
        svg.getStyleClass().add("j-titlebar-btn-icon");
        return createWindowButton(svg, styleClass, action);
    }

    private HBox createWindowButton(SVGPath svg, String styleClass, Runnable action) {
        HBox btn = new HBox();
        btn.setAlignment(Pos.CENTER);
        btn.getStyleClass().addAll("j-titlebar-btn", styleClass);
        btn.getChildren().add(svg);
        btn.setOnMouseClicked(e -> {
            e.consume();
            action.run();
        });
        return btn;
    }
}
