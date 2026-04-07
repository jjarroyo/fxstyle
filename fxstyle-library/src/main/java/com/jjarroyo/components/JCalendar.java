package com.jjarroyo.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.Consumer;

/**
 * JCalendar — Calendario mensual interactivo e inline.
 *
 * <p>Muestra una cuadrícula de un mes con navegación ◀/▶, resalta el día de hoy,
 * permite seleccionar una fecha y opcionalmente resaltar múltiples fechas.</p>
 *
 * <pre>
 * // Uso básico
 * JCalendar calendar = new JCalendar();
 * calendar.selectedDateProperty().addListener((obs, o, n) -&gt; System.out.println(n));
 *
 * // Con fecha inicial
 * JCalendar calendar = new JCalendar(LocalDate.of(2026, 6, 15));
 *
 * // Escuchar selección
 * calendar.setOnDateSelected(date -&gt; System.out.println("Seleccionado: " + date));
 *
 * // Resaltar fechas (eventos, citas, etc.)
 * calendar.setHighlightedDates(Set.of(
 *     LocalDate.of(2026, 4, 10),
 *     LocalDate.of(2026, 4, 15)
 * ));
 * </pre>
 *
 * <p>Clases CSS:</p>
 * <ul>
 *   <li>{@code j-calendar} — contenedor raíz (VBox)</li>
 *   <li>{@code j-calendar-header} — barra de navegación mes/año</li>
 *   <li>{@code j-calendar-nav-btn} — botones ◀ ▶</li>
 *   <li>{@code j-calendar-title} — label del mes y año</li>
 *   <li>{@code j-calendar-grid} — cuadrícula de días</li>
 *   <li>{@code j-calendar-day-header} — encabezados L, M, M, J, V, S, D</li>
 *   <li>{@code j-calendar-day} — celda de día normal</li>
 *   <li>{@code j-calendar-day-today} — día actual</li>
 *   <li>{@code j-calendar-day-selected} — día seleccionado</li>
 *   <li>{@code j-calendar-day-other-month} — día fuera del mes visible</li>
 *   <li>{@code j-calendar-day-highlighted} — día resaltado (evento)</li>
 *   <li>{@code j-calendar-day-weekend} — sábado / domingo</li>
 * </ul>
 */
public class JCalendar extends VBox {

    private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();
    private YearMonth currentMonth;
    private final Label monthYearLabel;
    private final GridPane grid;
    private Consumer<LocalDate> onDateSelected;
    private Set<LocalDate> highlightedDates = new HashSet<>();

    // ─── Constructores ──────────────────────────────────────────────────────

    public JCalendar() {
        this(LocalDate.now());
    }

    public JCalendar(LocalDate initialDate) {
        getStyleClass().add("j-calendar");
        setSpacing(0);
        setAlignment(Pos.TOP_CENTER);

        currentMonth = YearMonth.from(initialDate);
        selectedDate.set(initialDate);

        // ── Header: ◀  Abril 2026  ▶ ──
        monthYearLabel = new Label();
        monthYearLabel.getStyleClass().add("j-calendar-title");

        Button prevBtn = new Button("◀");
        prevBtn.getStyleClass().add("j-calendar-nav-btn");
        prevBtn.setOnAction(e -> navigateMonth(-1));

        Button nextBtn = new Button("▶");
        nextBtn.getStyleClass().add("j-calendar-nav-btn");
        nextBtn.setOnAction(e -> navigateMonth(1));

        Button todayBtn = new Button("Hoy");
        todayBtn.getStyleClass().addAll("j-calendar-nav-btn", "j-calendar-today-btn");
        todayBtn.setOnAction(e -> goToToday());

        Region spacerLeft = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        Region spacerRight = new Region();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        HBox header = new HBox(8, prevBtn, spacerLeft, monthYearLabel, spacerRight, todayBtn, nextBtn);
        header.getStyleClass().add("j-calendar-header");
        header.setAlignment(Pos.CENTER);

        // ── Grid ──
        grid = new GridPane();
        grid.getStyleClass().add("j-calendar-grid");
        grid.setHgap(0);
        grid.setVgap(0);

        getChildren().addAll(header, grid);

        renderMonth();

        // Re-render when selectedDate changes programmatically
        selectedDate.addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !YearMonth.from(newVal).equals(currentMonth)) {
                currentMonth = YearMonth.from(newVal);
            }
            renderMonth();
        });
    }

    // ─── Navegación ─────────────────────────────────────────────────────────

    private void navigateMonth(int delta) {
        currentMonth = currentMonth.plusMonths(delta);
        renderMonth();
    }

    private void goToToday() {
        LocalDate today = LocalDate.now();
        currentMonth = YearMonth.from(today);
        selectedDate.set(today);
        if (onDateSelected != null) {
            onDateSelected.accept(today);
        }
        renderMonth();
    }

    // ─── Renderizado del mes ────────────────────────────────────────────────

    private void renderMonth() {
        grid.getChildren().clear();

        // Actualizar título
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
        monthYearLabel.setText(monthName + " " + currentMonth.getYear());

        // Encabezados de día: L, M, M, J, V, S, D
        DayOfWeek[] days = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};
        for (int col = 0; col < 7; col++) {
            String dayName = days[col].getDisplayName(TextStyle.SHORT, Locale.getDefault());
            dayName = dayName.substring(0, 1).toUpperCase() + dayName.substring(1);
            if (dayName.length() > 3) dayName = dayName.substring(0, 3);

            Label dayLabel = new Label(dayName);
            dayLabel.getStyleClass().add("j-calendar-day-header");
            if (days[col] == DayOfWeek.SATURDAY || days[col] == DayOfWeek.SUNDAY) {
                dayLabel.getStyleClass().add("j-calendar-day-weekend");
            }
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setAlignment(Pos.CENTER);
            GridPane.setHgrow(dayLabel, Priority.ALWAYS);
            grid.add(dayLabel, col, 0);
        }

        // Primer día del mes
        LocalDate firstOfMonth = currentMonth.atDay(1);
        // ¿Qué día de la semana cae? (Lunes = 1)
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1=Mon, 7=Sun
        // Calcular la fecha de inicio de la cuadrícula (lunes de la primera semana)
        LocalDate gridStart = firstOfMonth.minusDays(startDayOfWeek - 1);

        LocalDate today = LocalDate.now();
        LocalDate selected = selectedDate.get();

        // Llenar 6 semanas (42 días)
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                LocalDate date = gridStart.plusDays(row * 7L + col);
                boolean isCurrentMonth = YearMonth.from(date).equals(currentMonth);

                StackPane cell = new StackPane();
                cell.getStyleClass().add("j-calendar-day");

                Label dayNumber = new Label(String.valueOf(date.getDayOfMonth()));
                dayNumber.getStyleClass().add("j-calendar-day-label");

                if (!isCurrentMonth) {
                    cell.getStyleClass().add("j-calendar-day-other-month");
                }
                if (date.equals(today)) {
                    cell.getStyleClass().add("j-calendar-day-today");
                }
                if (selected != null && date.equals(selected)) {
                    cell.getStyleClass().add("j-calendar-day-selected");
                }
                if (highlightedDates.contains(date)) {
                    cell.getStyleClass().add("j-calendar-day-highlighted");
                }
                if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    cell.getStyleClass().add("j-calendar-day-weekend");
                }

                cell.getChildren().add(dayNumber);
                cell.setMaxWidth(Double.MAX_VALUE);
                cell.setMaxHeight(Double.MAX_VALUE);
                GridPane.setHgrow(cell, Priority.ALWAYS);
                GridPane.setVgrow(cell, Priority.ALWAYS);

                // Click handler
                final LocalDate clickedDate = date;
                cell.setOnMouseClicked(e -> {
                    selectedDate.set(clickedDate);
                    if (!YearMonth.from(clickedDate).equals(currentMonth)) {
                        currentMonth = YearMonth.from(clickedDate);
                    }
                    if (onDateSelected != null) {
                        onDateSelected.accept(clickedDate);
                    }
                    renderMonth();
                });

                grid.add(cell, col, row + 1);
            }
        }
    }

    // ─── API pública ────────────────────────────────────────────────────────

    /** Property observable de la fecha seleccionada. */
    public ObjectProperty<LocalDate> selectedDateProperty() { return selectedDate; }

    /** Retorna la fecha seleccionada actual. */
    public LocalDate getSelectedDate() { return selectedDate.get(); }

    /** Establece la fecha seleccionada y navega al mes correspondiente. */
    public void setSelectedDate(LocalDate date) { selectedDate.set(date); }

    /** Retorna el YearMonth actualmente mostrado. */
    public YearMonth getCurrentMonth() { return currentMonth; }

    /** Navega a un mes/año específico sin cambiar la selección. */
    public void setCurrentMonth(YearMonth yearMonth) {
        this.currentMonth = yearMonth;
        renderMonth();
    }

    /** Callback cuando se selecciona una fecha con clic. */
    public void setOnDateSelected(Consumer<LocalDate> handler) {
        this.onDateSelected = handler;
    }

    /** Establece un conjunto de fechas para resaltar (eventos, citas, etc.). */
    public void setHighlightedDates(Set<LocalDate> dates) {
        this.highlightedDates = dates != null ? new HashSet<>(dates) : new HashSet<>();
        renderMonth();
    }

    /** Agrega una fecha al conjunto de fechas resaltadas. */
    public void addHighlightedDate(LocalDate date) {
        highlightedDates.add(date);
        renderMonth();
    }

    /** Elimina una fecha del conjunto de fechas resaltadas. */
    public void removeHighlightedDate(LocalDate date) {
        highlightedDates.remove(date);
        renderMonth();
    }

    /** Limpia todas las fechas resaltadas. */
    public void clearHighlightedDates() {
        highlightedDates.clear();
        renderMonth();
    }

    /** Retorna las fechas resaltadas actuales. */
    public Set<LocalDate> getHighlightedDates() {
        return Collections.unmodifiableSet(highlightedDates);
    }
}
