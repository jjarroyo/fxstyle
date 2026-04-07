package com.jjarroyo.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import java.util.function.Consumer;

/**
 * JRating - Interactive star rating component for FxStyle.
 * Supports configurable star count, half-star increments, hover preview,
 * read-only mode, multiple sizes and color variants.
 */
public class JRating extends HBox {

    private static final String STYLE_CLASS = "j-rating";
    private static final String STAR_STYLE = "j-rating-star";
    private static final String STAR_FILLED_STYLE = "j-rating-star-filled";
    private static final String STAR_HALF_STYLE = "j-rating-star-half";
    private static final String STAR_EMPTY_STYLE = "j-rating-star-empty";
    private static final String STAR_HOVER_STYLE = "j-rating-star-hover";

    // SVG paths for stars
    private static final String STAR_FILLED_PATH = "M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z";
    private static final String STAR_EMPTY_PATH = "M22 9.24l-7.19-.62L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27 18.18 21l-1.63-7.03L22 9.24zM12 15.4l-3.76 2.27 1-4.28-3.32-2.88 4.38-.38L12 6.1l1.71 4.01 4.38.38-3.32 2.88 1 4.28L12 15.4z";
    private static final String STAR_HALF_PATH = "M22 9.24l-7.19-.62L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27 18.18 21l-1.63-7.03L22 9.24zM12 15.4V6.1l1.71 4.04 4.38.38-3.32 2.88 1 4.28L12 15.4z";

    private final IntegerProperty maxStars = new SimpleIntegerProperty(5);
    private final DoubleProperty rating = new SimpleDoubleProperty(0);
    private final BooleanProperty readOnly = new SimpleBooleanProperty(false);
    private final BooleanProperty allowHalf = new SimpleBooleanProperty(false);

    private Consumer<Double> onRatingChanged;
    private double hoverValue = -1;

    // ─── Constructors ─────────────────────────────────────────────────

    public JRating() {
        this(5, 0);
    }

    public JRating(int maxStars) {
        this(maxStars, 0);
    }

    public JRating(int maxStars, double initialRating) {
        super();
        this.maxStars.set(maxStars);
        this.rating.set(initialRating);
        init();
    }

    // ─── Initialization ───────────────────────────────────────────────

    private void init() {
        getStyleClass().add(STYLE_CLASS);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(2);
        setCursor(Cursor.HAND);

        // Build initial stars
        buildStars();

        // Listen for property changes
        rating.addListener((obs, oldVal, newVal) -> updateStarVisuals());
        maxStars.addListener((obs, oldVal, newVal) -> buildStars());
        readOnly.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                getStyleClass().add("rating-readonly");
                setCursor(Cursor.DEFAULT);
            } else {
                getStyleClass().remove("rating-readonly");
                setCursor(Cursor.HAND);
            }
        });

        // Mouse exit from container -> clear hover
        setOnMouseExited(e -> {
            if (!readOnly.get() && !isDisabled()) {
                hoverValue = -1;
                updateStarVisuals();
            }
        });
    }

    private void buildStars() {
        getChildren().clear();
        int count = maxStars.get();

        for (int i = 1; i <= count; i++) {
            final int starIndex = i;

            // Wrapper region to contain the SVG and handle clipping/sizing
            Region starWrapper = new Region();
            starWrapper.getStyleClass().add(STAR_STYLE);
            starWrapper.setMinSize(24, 24);
            starWrapper.setPrefSize(24, 24);
            starWrapper.setMaxSize(24, 24);

            SVGPath svg = new SVGPath();
            svg.setContent(STAR_FILLED_PATH);
            svg.getStyleClass().add("j-rating-svg");
            starWrapper.setShape(svg);
            starWrapper.setScaleShape(true);

            // Mouse events for interactivity
            starWrapper.setOnMouseEntered(e -> {
                if (!readOnly.get() && !isDisabled()) {
                    hoverValue = starIndex;
                    updateStarVisuals();
                }
            });

            starWrapper.setOnMouseMoved(e -> {
                if (!readOnly.get() && !isDisabled() && allowHalf.get()) {
                    double relX = e.getX();
                    double halfWidth = starWrapper.getWidth() / 2.0;
                    hoverValue = relX <= halfWidth ? starIndex - 0.5 : starIndex;
                    updateStarVisuals();
                }
            });

            starWrapper.setOnMouseClicked(e -> {
                if (!readOnly.get() && !isDisabled()) {
                    double newRating;
                    if (allowHalf.get()) {
                        double relX = e.getX();
                        double halfWidth = starWrapper.getWidth() / 2.0;
                        newRating = relX <= halfWidth ? starIndex - 0.5 : starIndex;
                    } else {
                        newRating = starIndex;
                    }

                    // Toggle: if clicking same value, reset to 0
                    if (newRating == rating.get()) {
                        newRating = 0;
                    }

                    rating.set(newRating);
                    if (onRatingChanged != null) {
                        onRatingChanged.accept(rating.get());
                    }
                }
            });

            getChildren().add(starWrapper);
        }

        updateStarVisuals();
    }

    private void updateStarVisuals() {
        double displayValue = hoverValue >= 0 ? hoverValue : rating.get();

        for (int i = 0; i < getChildren().size(); i++) {
            if (getChildren().get(i) instanceof Region star) {
                int starNum = i + 1;

                // Clear previous state classes
                star.getStyleClass().removeAll(STAR_FILLED_STYLE, STAR_HALF_STYLE, STAR_EMPTY_STYLE, STAR_HOVER_STYLE);

                // Determine visual state
                SVGPath svg = new SVGPath();

                if (displayValue >= starNum) {
                    // Full star
                    svg.setContent(STAR_FILLED_PATH);
                    star.getStyleClass().add(STAR_FILLED_STYLE);
                } else if (displayValue >= starNum - 0.5 && allowHalf.get()) {
                    // Half star
                    svg.setContent(STAR_HALF_PATH);
                    star.getStyleClass().add(STAR_HALF_STYLE);
                } else {
                    // Empty star
                    svg.setContent(STAR_EMPTY_PATH);
                    star.getStyleClass().add(STAR_EMPTY_STYLE);
                }

                star.setShape(svg);
                star.setScaleShape(true);

                // Add hover highlight
                if (hoverValue >= 0 && starNum <= Math.ceil(hoverValue)) {
                    star.getStyleClass().add(STAR_HOVER_STYLE);
                }
            }
        }
    }

    // ─── Properties ───────────────────────────────────────────────────

    public DoubleProperty ratingProperty() {
        return rating;
    }

    public double getRating() {
        return rating.get();
    }

    public void setRating(double value) {
        rating.set(value);
    }

    public IntegerProperty maxStarsProperty() {
        return maxStars;
    }

    public int getMaxStars() {
        return maxStars.get();
    }

    public void setMaxStars(int value) {
        maxStars.set(value);
    }

    public BooleanProperty readOnlyProperty() {
        return readOnly;
    }

    public boolean isReadOnly() {
        return readOnly.get();
    }

    public void setReadOnly(boolean value) {
        readOnly.set(value);
    }

    public BooleanProperty allowHalfProperty() {
        return allowHalf;
    }

    public boolean isAllowHalf() {
        return allowHalf.get();
    }

    public void setAllowHalf(boolean value) {
        allowHalf.set(value);
    }

    public void setOnRatingChanged(Consumer<Double> handler) {
        this.onRatingChanged = handler;
    }

    // ─── Style helpers ────────────────────────────────────────────────

    /**
     * Adds one or more CSS style classes.
     */
    public JRating withStyle(String... classes) {
        getStyleClass().addAll(classes);
        return this;
    }

    /**
     * Sets the size variant: "rating-sm", "rating-md", "rating-lg"
     */
    public JRating withSize(String sizeClass) {
        getStyleClass().removeAll("rating-sm", "rating-md", "rating-lg");
        getStyleClass().add(sizeClass);
        return this;
    }

    /**
     * Sets the color variant.
     */
    public JRating withColor(String colorClass) {
        getStyleClass().removeAll(
            "rating-primary", "rating-warning", "rating-danger",
            "rating-success", "rating-info", "rating-dark"
        );
        getStyleClass().add(colorClass);
        return this;
    }
}
