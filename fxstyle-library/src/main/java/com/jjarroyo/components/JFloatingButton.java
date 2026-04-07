package com.jjarroyo.components;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * JFloatingButton component for FxStyle library.
 * A Floating Action Button (FAB) that positions itself inside its parent container automatically.
 */
public class JFloatingButton extends JButton {

    public enum Position {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT,
        CENTER_LEFT, CENTER, CENTER_RIGHT,
        BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
    }

    private static final String FAB_STYLE = "j-floating-button";
    private static final String EXTENDED_STYLE = "j-floating-button-extended";

    private Position currentPosition = Position.BOTTOM_RIGHT;
    private double margin = 20.0;
    private boolean extended = false;

    // Listeners for manual position binding in regular panes
    private ChangeListener<Number> parentWidthListener;
    private ChangeListener<Number> parentHeightListener;

    public JFloatingButton() {
        super();
        initFab();
    }

    public JFloatingButton(String text) {
        super(text);
        initFab();
        setExtended(true); // By default, if it has text, make it extended
    }

    public JFloatingButton(JIcon icon) {
        super("", icon);
        initFab();
    }

    public JFloatingButton(String text, JIcon icon) {
        super(text, icon);
        initFab();
        setExtended(true);
    }

    private void initFab() {
        getStyleClass().add(FAB_STYLE);
        
        // Listen for parent changes to auto-reposition
        parentProperty().addListener((obs, oldParent, newParent) -> {
            if (newParent != null) {
                applyPositioning(newParent);
            } else if (oldParent != null) {
                removePositioningBindings(oldParent);
            }
        });
    }

    /**
     * Sets whether this is an extended FAB (pill shape, text + icon) or standard circular FAB (icon only/no text).
     */
    public void setExtended(boolean extended) {
        this.extended = extended;
        if (extended) {
            getStyleClass().add(EXTENDED_STYLE);
        } else {
            getStyleClass().remove(EXTENDED_STYLE);
        }
    }

    public boolean isExtended() {
        return extended;
    }

    /**
     * Changes the relative position of the FAB in its parent container.
     */
    public void setPosition(Position pos) {
        this.currentPosition = pos;
        if (getParent() != null) {
            applyPositioning(getParent());
        }
    }

    /**
     * Sets the distance (margin) from the edges.
     */
    public void setMargin(double margin) {
        this.margin = margin;
        if (getParent() != null) {
            applyPositioning(getParent());
        }
    }

    private void removePositioningBindings(Parent parent) {
        if (parent instanceof Region region) {
            if (parentWidthListener != null) {
                region.widthProperty().removeListener(parentWidthListener);
            }
            if (parentHeightListener != null) {
                region.heightProperty().removeListener(parentHeightListener);
            }
        }
        layoutXProperty().unbind();
        layoutYProperty().unbind();
        translateXProperty().unbind();
        translateYProperty().unbind();
    }

    private void applyPositioning(Parent parent) {
        // Clear previous configurations
        removePositioningBindings(parent);
        
        if (parent instanceof AnchorPane) {
            applyAnchorPaneConstraints();
        } else if (parent instanceof StackPane) {
            applyStackPaneAlignment();
        } else if (parent instanceof Region region) {
            applyRegionManualPositioning(region);
        }
    }

    private void applyAnchorPaneConstraints() {
        AnchorPane.clearConstraints(this);
        switch (currentPosition) {
            case TOP_LEFT:
                AnchorPane.setTopAnchor(this, margin);
                AnchorPane.setLeftAnchor(this, margin);
                break;
            case TOP_CENTER:
                // Tricky in AnchorPane, rely on StackPane or manual if needed.
                // However, JavaFX doesn't easily anchor strictly to center horizontally using just anchors without stretching.
                AnchorPane.setTopAnchor(this, margin);
                // Simple workaround for horizontal center: binding translateX later if needed, but for now we do left+right
                break;
            case TOP_RIGHT:
                AnchorPane.setTopAnchor(this, margin);
                AnchorPane.setRightAnchor(this, margin);
                break;
            case CENTER_LEFT:
                AnchorPane.setLeftAnchor(this, margin);
                break;
            case CENTER:
                // Not standard for AnchorPane
                break;
            case CENTER_RIGHT:
                AnchorPane.setRightAnchor(this, margin);
                break;
            case BOTTOM_LEFT:
                AnchorPane.setBottomAnchor(this, margin);
                AnchorPane.setLeftAnchor(this, margin);
                break;
            case BOTTOM_CENTER:
                AnchorPane.setBottomAnchor(this, margin);
                break;
            case BOTTOM_RIGHT:
                AnchorPane.setBottomAnchor(this, margin);
                AnchorPane.setRightAnchor(this, margin);
                break;
        }

        // For centering horizontally/vertically in AnchorPane without stretching:
        if (currentPosition == Position.TOP_CENTER || currentPosition == Position.CENTER || currentPosition == Position.BOTTOM_CENTER) {
             layoutXProperty().bind(((Region)getParent()).widthProperty().subtract(widthProperty()).divide(2));
        } else {
             layoutXProperty().unbind();
        }

        if (currentPosition == Position.CENTER_LEFT || currentPosition == Position.CENTER || currentPosition == Position.CENTER_RIGHT) {
             layoutYProperty().bind(((Region)getParent()).heightProperty().subtract(heightProperty()).divide(2));
        } else {
             layoutYProperty().unbind();
        }
    }

    private void applyStackPaneAlignment() {
        Pos fxPos = switch (currentPosition) {
            case TOP_LEFT -> Pos.TOP_LEFT;
            case TOP_CENTER -> Pos.TOP_CENTER;
            case TOP_RIGHT -> Pos.TOP_RIGHT;
            case CENTER_LEFT -> Pos.CENTER_LEFT;
            case CENTER -> Pos.CENTER;
            case CENTER_RIGHT -> Pos.CENTER_RIGHT;
            case BOTTOM_LEFT -> Pos.BOTTOM_LEFT;
            case BOTTOM_CENTER -> Pos.BOTTOM_CENTER;
            case BOTTOM_RIGHT -> Pos.BOTTOM_RIGHT;
        };
        StackPane.setAlignment(this, fxPos);
        StackPane.setMargin(this, new javafx.geometry.Insets(margin));
    }

    private void applyRegionManualPositioning(Region parent) {
        Runnable updatePos = () -> {
            boolean isTop = currentPosition == Position.TOP_LEFT || currentPosition == Position.TOP_CENTER || currentPosition == Position.TOP_RIGHT;
            boolean isBottom = currentPosition == Position.BOTTOM_LEFT || currentPosition == Position.BOTTOM_CENTER || currentPosition == Position.BOTTOM_RIGHT;
            boolean isVCenter = currentPosition == Position.CENTER_LEFT || currentPosition == Position.CENTER || currentPosition == Position.CENTER_RIGHT;

            boolean isLeft = currentPosition == Position.TOP_LEFT || currentPosition == Position.CENTER_LEFT || currentPosition == Position.BOTTOM_LEFT;
            boolean isRight = currentPosition == Position.TOP_RIGHT || currentPosition == Position.CENTER_RIGHT || currentPosition == Position.BOTTOM_RIGHT;
            boolean isHCenter = currentPosition == Position.TOP_CENTER || currentPosition == Position.CENTER || currentPosition == Position.BOTTOM_CENTER;

            if (isTop) {
                setLayoutY(margin);
            } else if (isBottom) {
                setLayoutY(parent.getHeight() - getHeight() - margin);
            } else if (isVCenter) {
                setLayoutY((parent.getHeight() - getHeight()) / 2.0);
            }

            if (isLeft) {
                setLayoutX(margin);
            } else if (isRight) {
                setLayoutX(parent.getWidth() - getWidth() - margin);
            } else if (isHCenter) {
                setLayoutX((parent.getWidth() - getWidth()) / 2.0);
            }
        };

        parentWidthListener = (obs, oldV, newV) -> updatePos.run();
        parentHeightListener = (obs, oldV, newV) -> updatePos.run();
        
        parent.widthProperty().addListener(parentWidthListener);
        parent.heightProperty().addListener(parentHeightListener);
        widthProperty().addListener(o -> updatePos.run());
        heightProperty().addListener(o -> updatePos.run());
        
        // Initial call
        updatePos.run();
    }
}
