package com.jjarroyo.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * JTimePicker — selector de hora compacto HH:mm con spinners de subir/bajar
 * y campos de texto editables por teclado.
 *
 * <pre>
 * // Uso basico
 * JTimePicker picker = new JTimePicker("08:30");
 * picker.valueProperty().addListener((obs, o, n) -&gt; System.out.println(n));
 *
 * // Binding bidireccional
 * picker.valueProperty().bindBidirectional(viewModel.formTimeProperty());
 * </pre>
 *
 * <p>Caracteristicas:</p>
 * <ul>
 *   <li>Escritura directa en los campos de hora y minutos</li>
 *   <li>Flechas arriba/abajo para incrementar/decrementar</li>
 *   <li>Teclas UP/DOWN del teclado cuando el campo esta enfocado</li>
 *   <li>Validacion automatica al perder el foco (clamp 0-23 / 0-59)</li>
 *   <li>Solo acepta digitos — rechaza cualquier otro caracter</li>
 * </ul>
 */
public class JTimePicker extends HBox {

    private final StringProperty value = new SimpleStringProperty("00:00");
    private int hours = 0;
    private int minutes = 0;

    /** TextField editable para la hora */
    private final TextField hourField;
    /** TextField editable para los minutos */
    private final TextField minuteField;

    // ─── Constructores ────────────────────────────────────────────────────────

    public JTimePicker() {
        this("00:00");
    }

    public JTimePicker(String initialTime) {
        getStyleClass().add("j-time-picker");
        setAlignment(Pos.CENTER);
        setSpacing(2);

        parseTime(initialTime);

        // --- Spinner de Horas ---
        hourField = createField(hours, true);
        VBox hourSpinner = createSpinner(hourField, true);

        // --- Separador ---
        Label separator = new Label(":");
        separator.getStyleClass().add("j-time-separator");

        // --- Spinner de Minutos ---
        minuteField = createField(minutes, false);
        VBox minuteSpinner = createSpinner(minuteField, false);

        getChildren().addAll(hourSpinner, separator, minuteSpinner);

        // Sincronizar value property → actualiza campos si se cambia desde fuera
        value.set(formatTime());
        value.addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(formatTime())) {
                parseTime(newVal);
                hourField.setText(String.format("%02d", hours));
                minuteField.setText(String.format("%02d", minutes));
            }
        });
    }

    // ─── Construcción de la UI ────────────────────────────────────────────────

    /**
     * Crea el TextField editable de hora o minutos.
     * Solo acepta dígitos, valida al perder foco y responde a ↑↓ del teclado.
     */
    private TextField createField(int initialValue, boolean isHour) {
        TextField field = new TextField(String.format("%02d", initialValue));
        field.getStyleClass().add("j-time-field");
        field.setPrefWidth(40);
        field.setMaxWidth(40);
        field.setAlignment(Pos.CENTER);

        // Filtrar: solo permitir dígitos (máx 2)
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d{0,2}")) {
                field.setText(oldVal);
            }
        });

        // Validar y normalizar al perder el foco
        field.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                applyFieldValue(field, isHour);
            }
        });

        // Confirmar con Enter
        field.setOnAction(e -> applyFieldValue(field, isHour));

        // ↑ incrementa, ↓ decrementa mientras el campo está activo
        field.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP) {
                step(field, isHour, +1);
                e.consume();
            } else if (e.getCode() == KeyCode.DOWN) {
                step(field, isHour, -1);
                e.consume();
            }
        });

        // Seleccionar todo al ganar foco → facilita la escritura
        field.setOnMouseClicked(e -> field.selectAll());
        field.focusedProperty().addListener((obs, o, focused) -> {
            if (focused) field.selectAll();
        });

        return field;
    }

    /**
     * Crea el VBox que contiene: botón ▲, campo editable, botón ▼.
     */
    private VBox createSpinner(TextField field, boolean isHour) {
        VBox spinner = new VBox(0);
        spinner.setAlignment(Pos.CENTER);
        spinner.getStyleClass().add("j-time-spinner");

        Button upBtn = new Button("▲");
        upBtn.getStyleClass().add("j-time-btn");
        upBtn.setOnAction(e -> step(field, isHour, +1));

        Button downBtn = new Button("▼");
        downBtn.getStyleClass().add("j-time-btn");
        downBtn.setOnAction(e -> step(field, isHour, -1));

        spinner.getChildren().addAll(upBtn, field, downBtn);
        return spinner;
    }

    // ─── Lógica interna ────────────────────────────────────────────────────────

    /**
     * Incrementa o decrementa el valor del campo en {@code delta} unidades,
     * con wrap-around (00 → max / max → 00).
     */
    private void step(TextField field, boolean isHour, int delta) {
        if (isHour) {
            hours = (hours + delta + 24) % 24;
            field.setText(String.format("%02d", hours));
        } else {
            minutes = (minutes + delta + 60) % 60;
            field.setText(String.format("%02d", minutes));
        }
        value.set(formatTime());
    }

    /**
     * Lee el texto del field, lo parsea y lo guarda en {@code hours} o
     * {@code minutes}, aplicando el rango válido. Actualiza el texto del campo
     * al formato {@code 00}.
     */
    private void applyFieldValue(TextField field, boolean isHour) {
        try {
            int parsed = Integer.parseInt(field.getText().trim());
            if (isHour) {
                hours = Math.max(0, Math.min(23, parsed));
                field.setText(String.format("%02d", hours));
            } else {
                minutes = Math.max(0, Math.min(59, parsed));
                field.setText(String.format("%02d", minutes));
            }
        } catch (NumberFormatException ex) {
            // Si el campo está vacío o inválido, restaurar el valor anterior
            field.setText(isHour
                    ? String.format("%02d", hours)
                    : String.format("%02d", minutes));
        }
        value.set(formatTime());
    }

    private void parseTime(String time) {
        try {
            if (time != null && time.contains(":")) {
                String[] parts = time.split(":");
                hours   = Math.max(0, Math.min(23, Integer.parseInt(parts[0].trim())));
                minutes = Math.max(0, Math.min(59, Integer.parseInt(parts[1].trim())));
            }
        } catch (NumberFormatException e) {
            hours = 0;
            minutes = 0;
        }
    }

    private String formatTime() {
        return String.format("%02d:%02d", hours, minutes);
    }

    // ─── API pública ──────────────────────────────────────────────────────────

    /** Retorna el valor actual como {@code "HH:mm"}. */
    public String getValue() { return value.get(); }

    /** Establece la hora como {@code "HH:mm"}. */
    public void setValue(String v) { value.set(v); }

    /** Property observable del valor {@code "HH:mm"}. */
    public StringProperty valueProperty() { return value; }

    /** Retorna la hora actual (0–23). */
    public int getHours() { return hours; }

    /** Retorna los minutos actuales (0–59). */
    public int getMinutes() { return minutes; }

    /**
     * Retorna el TextField interno de la hora. Útil para aplicar estilos extras.
     */
    public TextField getHourField() { return hourField; }

    /**
     * Retorna el TextField interno de los minutos. Útil para aplicar estilos extras.
     */
    public TextField getMinuteField() { return minuteField; }
}
