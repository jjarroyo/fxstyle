package com.jjarroyo.demo.views;

import com.jjarroyo.components.JButton;
import com.jjarroyo.components.JCalendar;
import com.jjarroyo.components.JCard;
import com.jjarroyo.components.JLabel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class CalendarView extends ScrollPane {

    public CalendarView() {
        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(24));

        setFitToWidth(true);
        setContent(content);

        // Page Header
        VBox pageHeader = new VBox();
        JLabel title = new JLabel("Calendar")
            .withStyle("text-2xl", "font-bold", "text-slate-800");
        JLabel subtitle = new JLabel("Calendario mensual interactivo con navegación, selección y resaltado de fechas")
            .withStyle("text-base", "text-slate-500");
        pageHeader.getChildren().addAll(title, subtitle);
        content.getChildren().add(pageHeader);

        // 1. Basic Calendar
        content.getChildren().add(new JCard("Calendario Básico", createBasicCalendar()));

        // 2. Calendar with Live Selection
        content.getChildren().add(new JCard("Selección en Vivo", createLiveSelectionCalendar()));

        // 3. Calendar with Highlighted Dates
        content.getChildren().add(new JCard("Fechas Resaltadas (Eventos)", createHighlightedCalendar()));

        // 4. Calendar with Programmatic Navigation
        content.getChildren().add(new JCard("Navegación Programática", createProgrammaticCalendar()));

        // 5. Multiple Calendars Side by Side
        content.getChildren().add(new JCard("Calendarios Múltiples", createMultipleCalendars()));
    }

    // ─── 1. Basic Calendar ───────────────────────────────────────────

    private javafx.scene.Node createBasicCalendar() {
        VBox container = new VBox(12);

        Label desc = new Label("Calendario del mes actual. Haz clic en cualquier día para seleccionarlo. Usa los botones ◀ ▶ para navegar entre meses.");
        desc.getStyleClass().add("text-slate-500");
        desc.setWrapText(true);

        JCalendar calendar = new JCalendar();

        container.getChildren().addAll(desc, calendar);
        return container;
    }

    // ─── 2. Live Selection ───────────────────────────────────────────

    private javafx.scene.Node createLiveSelectionCalendar() {
        VBox container = new VBox(12);

        Label desc = new Label("El label de abajo se actualiza en tiempo real al seleccionar una fecha.");
        desc.getStyleClass().add("text-slate-500");
        desc.setWrapText(true);

        JCalendar calendar = new JCalendar();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy");

        Label selectedLabel = new Label("Fecha seleccionada: " + LocalDate.now().format(fmt));
        selectedLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 600; -fx-text-fill: -color-primary-600;");
        selectedLabel.setWrapText(true);

        calendar.setOnDateSelected(date -> {
            selectedLabel.setText("Fecha seleccionada: " + date.format(fmt));
        });

        container.getChildren().addAll(desc, calendar, selectedLabel);
        return container;
    }

    // ─── 3. Highlighted Dates ────────────────────────────────────────

    private javafx.scene.Node createHighlightedCalendar() {
        VBox container = new VBox(12);

        Label desc = new Label("Las fechas verdes representan eventos o citas. Puedes usar setHighlightedDates() para marcarlas.");
        desc.getStyleClass().add("text-slate-500");
        desc.setWrapText(true);

        LocalDate now = LocalDate.now();
        JCalendar calendar = new JCalendar(now);

        // Highlight some dates in the current month
        calendar.setHighlightedDates(Set.of(
            now.withDayOfMonth(5),
            now.withDayOfMonth(10),
            now.withDayOfMonth(15),
            now.withDayOfMonth(20),
            now.withDayOfMonth(25)
        ));

        Label info = new Label("💡 Los días 5, 10, 15, 20 y 25 están resaltados como eventos.");
        info.setStyle("-fx-font-size: 12px; -fx-text-fill: -color-success-600; -fx-font-weight: 500;");
        info.setWrapText(true);

        container.getChildren().addAll(desc, calendar, info);
        return container;
    }

    // ─── 4. Programmatic Navigation ──────────────────────────────────

    private javafx.scene.Node createProgrammaticCalendar() {
        VBox container = new VBox(12);

        Label desc = new Label("Controla el calendario desde código: ir a una fecha específica, avanzar o retroceder meses.");
        desc.getStyleClass().add("text-slate-500");
        desc.setWrapText(true);

        JCalendar calendar = new JCalendar();

        HBox buttons = new HBox(8);
        buttons.setAlignment(Pos.CENTER_LEFT);

        JButton goToChristmas = new JButton("🎄 Ir a Navidad");
        goToChristmas.addClass("btn-outline-primary");
        goToChristmas.addClass("btn-sm");
        goToChristmas.setOnAction(e -> calendar.setSelectedDate(LocalDate.of(2026, 12, 25)));

        JButton goToNewYear = new JButton("🎆 Año Nuevo");
        goToNewYear.addClass("btn-outline-primary");
        goToNewYear.addClass("btn-sm");
        goToNewYear.setOnAction(e -> calendar.setSelectedDate(LocalDate.of(2027, 1, 1)));

        JButton goToBirthday = new JButton("🎂 Mi Cumpleaños");
        goToBirthday.addClass("btn-outline-success");
        goToBirthday.addClass("btn-sm");
        goToBirthday.setOnAction(e -> calendar.setSelectedDate(LocalDate.of(2026, 7, 15)));

        JButton resetToday = new JButton("📅 Hoy");
        resetToday.addClass("btn-outline-danger");
        resetToday.addClass("btn-sm");
        resetToday.setOnAction(e -> calendar.setSelectedDate(LocalDate.now()));

        buttons.getChildren().addAll(goToChristmas, goToNewYear, goToBirthday, resetToday);

        container.getChildren().addAll(desc, calendar, buttons);
        return container;
    }

    // ─── 5. Multiple Calendars ───────────────────────────────────────

    private javafx.scene.Node createMultipleCalendars() {
        VBox container = new VBox(12);

        Label desc = new Label("Dos calendarios independientes mostrando meses consecutivos.");
        desc.getStyleClass().add("text-slate-500");
        desc.setWrapText(true);

        FlowPane flow = new FlowPane(24, 16);

        LocalDate now = LocalDate.now();

        VBox leftBox = new VBox(6);
        Label leftLabel = new Label("Este mes");
        leftLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: -color-slate-600;");
        JCalendar leftCalendar = new JCalendar(now);
        leftBox.getChildren().addAll(leftLabel, leftCalendar);

        VBox rightBox = new VBox(6);
        Label rightLabel = new Label("Próximo mes");
        rightLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: -color-slate-600;");
        JCalendar rightCalendar = new JCalendar(now.plusMonths(1));
        rightBox.getChildren().addAll(rightLabel, rightCalendar);

        flow.getChildren().addAll(leftBox, rightBox);

        container.getChildren().addAll(desc, flow);
        return container;
    }
}
