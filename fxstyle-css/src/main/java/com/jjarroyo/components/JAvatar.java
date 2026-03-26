package com.jjarroyo.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * JAvatar — Componente de avatar para mostrar identidad de usuario.
 * Soporta iniciales, imagen, tamaños, colores y badges de estado.
 *
 * <pre>
 * // Iniciales
 * JAvatar avatar = new JAvatar("JA");
 * JAvatar avatar = new JAvatar("Jorge", "Arroyo"); // auto "JA"
 *
 * // Imagen
 * avatar.setImage(new Image("photo.jpg"));
 *
 * // Tamaño y color
 * avatar.setSize(JAvatar.Size.LG);
 * avatar.setColor("primary"); // success, danger, warning, info
 *
 * // Estado
 * avatar.setStatus(JAvatar.Status.ONLINE);
 * </pre>
 */
public class JAvatar extends StackPane {

    // ─── Enums ───────────────────────────────────────────────────────────────────

    public enum Size {
        XS(24, 10, 8),
        SM(32, 12, 10),
        MD(40, 14, 12),
        LG(48, 16, 14),
        XL(64, 20, 16);

        final double px;
        final double fontSize;
        final double badgeSize;

        Size(double px, double fontSize, double badgeSize) {
            this.px = px;
            this.fontSize = fontSize;
            this.badgeSize = badgeSize;
        }
    }

    public enum Status {
        NONE,
        ONLINE,
        AWAY,
        BUSY,
        OFFLINE
    }

    // ─── State ───────────────────────────────────────────────────────────────────

    private final ObjectProperty<Size> size = new SimpleObjectProperty<>(Size.MD);
    private final ObjectProperty<Status> status = new SimpleObjectProperty<>(Status.NONE);

    private String initials;
    private Image image;
    private String colorVariant = "primary";

    // UI
    private final Circle bgCircle = new Circle();
    private final Circle imgCircle = new Circle();
    private final Label initialsLabel = new Label();
    private final Circle badgeCircle = new Circle();
    private final Circle badgeBorder = new Circle();

    // ─── Constructors ────────────────────────────────────────────────────────────

    /** Avatar con iniciales directas, e.g. "JA" */
    public JAvatar(String initials) {
        this.initials = initials != null ? initials.toUpperCase() : "?";
        init();
    }

    /** Avatar auto-generando iniciales de nombre y apellido */
    public JAvatar(String firstName, String lastName) {
        this.initials = extractInitials(firstName, lastName);
        init();
    }

    /** Avatar vacío (placeholder) */
    public JAvatar() {
        this.initials = "?";
        init();
    }

    // ─── Init ────────────────────────────────────────────────────────────────────

    private void init() {
        getStyleClass().add("j-avatar");
        setAlignment(Pos.CENTER);

        // Background circle
        bgCircle.getStyleClass().add("j-avatar-bg");

        // Image circle (overlay — solo visible si hay imagen)
        imgCircle.setFill(Color.TRANSPARENT);
        imgCircle.setVisible(false);

        // Initials label
        initialsLabel.getStyleClass().add("j-avatar-text");
        initialsLabel.setText(initials);

        // Badge (status indicator)
        badgeBorder.setFill(Color.WHITE);
        badgeBorder.setVisible(false);
        badgeCircle.getStyleClass().add("j-avatar-badge");
        badgeCircle.setVisible(false);

        getChildren().addAll(bgCircle, imgCircle, initialsLabel, badgeBorder, badgeCircle);

        // Listeners
        size.addListener((obs, ov, nv) -> applySize());
        status.addListener((obs, ov, nv) -> applyStatus());

        applySize();
        applyColor();
    }

    // ─── Layout ──────────────────────────────────────────────────────────────────

    private void applySize() {
        Size s = size.get();
        double r = s.px / 2.0;

        bgCircle.setRadius(r);
        imgCircle.setRadius(r);

        initialsLabel.setStyle(
            "-fx-font-size: " + s.fontSize + "px; -fx-font-weight: bold;"
        );

        // Badge position: bottom-right
        double badgeR = s.badgeSize / 2.0;
        double borderR = badgeR + 2;
        badgeCircle.setRadius(badgeR);
        badgeBorder.setRadius(borderR);

        // Position badge at bottom-right of the circle
        double offset = r * 0.7;
        badgeCircle.setTranslateX(offset);
        badgeCircle.setTranslateY(offset);
        badgeBorder.setTranslateX(offset);
        badgeBorder.setTranslateY(offset);

        // Component sizing
        double total = s.px + 4; // +4 for badge overflow
        setMinSize(total, total);
        setPrefSize(total, total);
        setMaxSize(total, total);

        // Update size style classes
        getStyleClass().removeIf(c -> c.startsWith("j-avatar-size-"));
        getStyleClass().add("j-avatar-size-" + s.name().toLowerCase());
    }

    private void applyColor() {
        // Remove old color classes
        getStyleClass().removeIf(c -> c.startsWith("j-avatar-color-"));
        getStyleClass().add("j-avatar-color-" + colorVariant);

        // Apply via inline style for the circles (CSS backup)
        String[] colors = getColorPair(colorVariant);
        bgCircle.setStyle("-fx-fill: " + colors[0] + ";");
        initialsLabel.setTextFill(Color.web(colors[1]));
    }

    private void applyStatus() {
        Status st = status.get();
        boolean visible = st != Status.NONE;
        badgeCircle.setVisible(visible);
        badgeBorder.setVisible(visible);

        if (visible) {
            String color = switch (st) {
                case ONLINE -> "#22c55e";
                case AWAY -> "#f59e0b";
                case BUSY -> "#ef4444";
                case OFFLINE -> "#94a3b8";
                default -> "transparent";
            };
            badgeCircle.setStyle("-fx-fill: " + color + ";");
        }
    }

    // ─── Public API ──────────────────────────────────────────────────────────────

    /** Establece una imagen (cubre las iniciales) */
    public JAvatar setImage(Image img) {
        this.image = img;
        if (img != null) {
            imgCircle.setFill(new ImagePattern(img));
            imgCircle.setVisible(true);
            initialsLabel.setVisible(false);
        } else {
            imgCircle.setFill(Color.TRANSPARENT);
            imgCircle.setVisible(false);
            initialsLabel.setVisible(true);
        }
        return this;
    }

    /** Establece las iniciales */
    public JAvatar setInitials(String initials) {
        this.initials = initials != null ? initials.toUpperCase() : "?";
        initialsLabel.setText(this.initials);
        return this;
    }

    /** Establece iniciales desde nombre y apellido */
    public JAvatar setName(String firstName, String lastName) {
        this.initials = extractInitials(firstName, lastName);
        initialsLabel.setText(this.initials);
        return this;
    }

    /** Establece el tamaño */
    public JAvatar setSize(Size size) {
        this.size.set(size);
        return this;
    }

    public Size getSize() {
        return size.get();
    }

    public ObjectProperty<Size> sizeProperty() {
        return size;
    }

    /** Establece el color: primary, success, danger, warning, info, slate */
    public JAvatar setColor(String color) {
        this.colorVariant = color != null ? color : "primary";
        applyColor();
        return this;
    }

    /** Establece el estado (badge) */
    public JAvatar setStatus(Status status) {
        this.status.set(status);
        return this;
    }

    public Status getStatus() {
        return status.get();
    }

    public ObjectProperty<Status> statusProperty() {
        return status;
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────────

    private static String extractInitials(String first, String last) {
        StringBuilder sb = new StringBuilder();
        if (first != null && !first.isEmpty()) sb.append(first.charAt(0));
        if (last != null && !last.isEmpty()) sb.append(last.charAt(0));
        return sb.length() > 0 ? sb.toString().toUpperCase() : "?";
    }

    private static String[] getColorPair(String variant) {
        return switch (variant) {
            case "success" -> new String[]{"#dcfce7", "#16a34a"};
            case "danger"  -> new String[]{"#fee2e2", "#dc2626"};
            case "warning" -> new String[]{"#fef3c7", "#d97706"};
            case "info"    -> new String[]{"#dbeafe", "#2563eb"};
            case "slate"   -> new String[]{"#f1f5f9", "#475569"};
            default        -> new String[]{"#e0e7ff", "#4f46e5"}; // primary
        };
    }

    // ═════════════════════════════════════════════════════════════════════════════
    // Inner class: Group — Avatares apilados con solapamiento
    // ═════════════════════════════════════════════════════════════════════════════

    public static class Group extends HBox {

        private int maxVisible = Integer.MAX_VALUE;

        public Group(JAvatar... avatars) {
            getStyleClass().add("j-avatar-group");
            setAlignment(Pos.CENTER_LEFT);
            for (JAvatar a : avatars) addAvatar(a);
        }

        /** Máximo de avatares visibles; extra se muestra como "+N" */
        public Group setMax(int max) {
            this.maxVisible = max;
            rebuild();
            return this;
        }

        public void addAvatar(JAvatar avatar) {
            // Negative margin for overlap
            if (!getChildren().isEmpty()) {
                HBox.setMargin(avatar, new Insets(0, 0, 0, -10));
            }
            // Borde blanco para dar efecto de separación
            avatar.setStyle(
                avatar.getStyle() + 
                "-fx-effect: dropshadow(gaussian, rgba(255,255,255,1), 0, 0, 0, 0);"
            );
            getChildren().add(avatar);
            rebuild();
        }

        private void rebuild() {
            // Remove old overflow label
            getChildren().removeIf(n -> n.getStyleClass().contains("j-avatar-overflow"));

            int total = (int) getChildren().stream()
                .filter(n -> n instanceof JAvatar).count();

            if (total > maxVisible) {
                int overflow = total - maxVisible;
                // Hide extra avatars
                int count = 0;
                for (int i = getChildren().size() - 1; i >= 0; i--) {
                    if (getChildren().get(i) instanceof JAvatar) {
                        if (count >= maxVisible) {
                            getChildren().get(i).setVisible(false);
                            getChildren().get(i).setManaged(false);
                        }
                        count++;
                    }
                }

                // Overflow indicator
                JAvatar overflowAvatar = new JAvatar("+" + overflow);
                overflowAvatar.setColor("slate");
                overflowAvatar.getStyleClass().add("j-avatar-overflow");
                // Match size of existing avatars
                if (!getChildren().isEmpty() && getChildren().get(0) instanceof JAvatar first) {
                    overflowAvatar.setSize(first.getSize());
                }
                HBox.setMargin(overflowAvatar, new Insets(0, 0, 0, -10));
                getChildren().add(overflowAvatar);
            }
        }
    }
}
