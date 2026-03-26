package com.jjarroyo.components;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.core.FormatConfig;
import com.github.vertical_blank.sqlformatter.languages.Dialect;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.ParseException;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.time.Duration;
import java.util.*;
import java.util.regex.*;


public class JSqlEditor extends VBox {

    // ─── SQL Token Pattern ────────────────────────────────────────────────────────
    private static final Set<String> KEYWORDS = Set.of(
        "SELECT", "FROM", "WHERE", "GROUP", "BY", "ORDER", "HAVING", "LIMIT", "OFFSET",
        "INSERT", "INTO", "VALUES", "UPDATE", "SET", "DELETE", "TRUNCATE",
        "CREATE", "TABLE", "DROP", "ALTER", "DATABASE", "SCHEMA", "INDEX", "VIEW",
        "PRIMARY", "KEY", "FOREIGN", "REFERENCES", "CONSTRAINT", "DEFAULT", "CHECK",
        "NULL", "UNIQUE", "NOT", "AND", "OR", "IN", "IS", "LIKE", "BETWEEN",
        "EXISTS", "AS", "JOIN", "ON", "INNER", "LEFT", "RIGHT", "OUTER", "FULL",
        "UNION", "ALL", "DISTINCT", "CASE", "WHEN", "THEN", "ELSE", "END",
        "WITH", "RECURSIVE", "RETURNING", "REPLACE", "SHOW", "USE", "DESCRIBE",
        "COMMIT", "ROLLBACK", "BEGIN", "TRANSACTION", "SAVEPOINT", "GRANT", "REVOKE",
        "IF", "CROSS", "NATURAL", "LATERAL", "OVER", "PARTITION", "ROWS", "RANGE",
        "UNBOUNDED", "PRECEDING", "FOLLOWING", "CURRENT", "ROW", "FILTER",
        "EXCEPT", "INTERSECT", "MINUS", "FETCH", "FIRST", "NEXT", "ONLY", "TIES",
        "LOCK", "UNLOCK", "NOWAIT", "SKIP", "LOCKED", "TRUE", "FALSE", "UNKNOWN"
    );

    private static final Set<String> TYPES = Set.of(
        "SERIAL", "BIGSERIAL", "SMALLSERIAL", "VARCHAR", "CHARACTER", "VARYING",
        "INTEGER", "INT", "INT2", "INT4", "INT8", "BIGINT", "SMALLINT", "TINYINT",
        "TEXT", "CLOB", "NVARCHAR", "NCHAR", "NTEXT",
        "BOOLEAN", "BOOL", "BIT",
        "DATE", "TIME", "TIMESTAMP", "TIMESTAMPTZ", "TIMETZ", "DATETIME", "DATETIME2",
        "NUMERIC", "DECIMAL", "FLOAT", "FLOAT4", "FLOAT8", "DOUBLE", "PRECISION", "REAL", "MONEY",
        "CHAR", "BYTEA", "BLOB", "BINARY", "VARBINARY",
        "JSON", "JSONB", "XML", "UUID", "ARRAY",
        "HSTORE", "INTERVAL", "OID", "INET", "CIDR", "MACADDR", "POINT", "LINE",
        "GEOMETRY", "GEOGRAPHY", "ROWID", "NUMBER", "RAW", "LONG", "BFILE"
    );

    private static final Set<String> FUNCTIONS = Set.of(
        "COUNT", "SUM", "AVG", "MIN", "MAX", "COALESCE", "NULLIF", "CAST", "CONVERT",
        "NOW", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "SYSDATE", "GETDATE",
        "LENGTH", "LEN", "LOWER", "UPPER", "TRIM", "LTRIM", "RTRIM", "LPAD", "RPAD",
        "REPLACE", "SUBSTRING", "SUBSTR", "POSITION", "CHARINDEX", "PATINDEX",
        "EXTRACT", "TO_DATE", "TO_CHAR", "TO_NUMBER", "TO_TIMESTAMP", "DATEADD", "DATEDIFF",
        "ROUND", "CEIL", "CEILING", "FLOOR", "ABS", "MOD", "POWER", "SQRT", "RANDOM", "RAND",
        "ROW_NUMBER", "RANK", "DENSE_RANK", "NTILE", "PERCENT_RANK", "CUME_DIST",
        "LEAD", "LAG", "FIRST_VALUE", "LAST_VALUE", "NTH_VALUE",
        "STRING_AGG", "GROUP_CONCAT", "ARRAY_AGG", "JSON_AGG", "JSONB_AGG",
        "GENERATE_SERIES", "UNNEST", "ARRAY_LENGTH", "CARDINALITY",
        "CONCAT", "CONCAT_WS", "FORMAT", "SPLIT_PART", "REGEXP_MATCHES", "REGEXP_REPLACE",
        "DATE_TRUNC", "DATE_PART", "AGE", "MAKE_DATE", "MAKE_INTERVAL",
        "GREATEST", "LEAST", "IIF", "DECODE", "NVL", "NVL2", "IFNULL",
        "ENCODE", "MD5", "SHA1", "SHA256", "CRYPT", "GEN_SALT",
        "ST_DISTANCE", "ST_CONTAINS", "ST_INTERSECTS", "ST_AREA", "ST_LENGTH",
        "ISNULL", "ISNOTNULL", "TYPEOF", "OID", "TABLEOID", "CTID"
    );

    private static final String TOKEN_REGEX =
        "(?s)(/\\*.*?\\*/)"                   // 1: block comment
        + "|(--[^\n]*)"                        // 2: line comment
        + "|('(?:[^'\\\\]|\\\\.)*')"          // 3: single-quoted string
        + "|(\"(?:[^\"\\\\]|\\\\.)*\")"        // 4: double-quoted identifier
        + "|(\\b\\d+\\.?\\d*(?:[eE][+-]?\\d+)?\\b)" // 5: numbers
        + "|(\\b[A-Za-z_][A-Za-z0-9_]*\\b)"  // 6: identifier/keyword
        + "|([=<>!+\\-*/|&^~%])"             // 7: operators
        + "|([(\\[{)}\\];,.])"               // 8: punctuation
        ;

    private static final Pattern TOKEN_PATTERN = Pattern.compile(TOKEN_REGEX);

    // ─── Types ───────────────────────────────────────────────────────────────────
    public record SqlError(int line, int startCol, int endCol, String message) {}

    // ─── UI ──────────────────────────────────────────────────────────────────────
    private final CodeArea codeArea = new CodeArea();
    private final VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(codeArea);

    private final HBox statusBar = new HBox();
    private final Label lblPosition = new Label("Ln 1, Col 1");
    private final Label lblErrors = new Label("✓ Sin errores");
    private final Label lblTheme = new Label("☀ Claro");

    // Autocomplete popup
    private final Popup autocompletePopup = new Popup();
    private final ListView<String> autocompleteList = new ListView<>();

    // ─── Properties ──────────────────────────────────────────────────────────────
    private final BooleanProperty readOnly = new SimpleBooleanProperty(false);
    private final BooleanProperty darkMode = new SimpleBooleanProperty(false);
    private final ObservableList<SqlError> errors = FXCollections.observableArrayList();
    private Subscription highlightSubscription;
    private Subscription validateSubscription;
    private static final double THUMB_MIN_SIZE = 40.0; // px — siempre visible
    private static final double SCROLLBAR_WIDTH = 12.0; // px — grosor del track

    // ─── Init ────────────────────────────────────────────────────────────────────
    public JSqlEditor() { init(); }

    private void init() {
        getStyleClass().add("j-sql-editor");
        setFillWidth(true);
        applyThemeStyle(false);

        setupCodeArea();
        setupScrollPane();
        setupStatusBar();
        setupAutocomplete();
        setupSubscriptions();

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        getChildren().addAll(scrollPane, statusBar);
        sceneProperty().addListener((obs, ov, scene) -> {
            if (scene != null) {
                // Esperar un pulse para que el ScrollPane construya su skin
                javafx.application.Platform.runLater(this::fixScrollBarThumb);
            }
        });

    }

    // ─── CodeArea Setup ───────────────────────────────────────────────────────────
    private void setupCodeArea() {
        // Native line numbers — RichTextFX built-in, always correct
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.setWrapText(false);
        codeArea.getStyleClass().add("j-sql-code-area");

        // Key bindings
        codeArea.setOnKeyPressed(this::handleKeyPressed);
        codeArea.setOnKeyTyped(this::handleKeyTyped);

        // Position label
        codeArea.caretPositionProperty().addListener((obs, ov, nv) -> {
            int para = codeArea.getCurrentParagraph();
            int col = codeArea.getCaretColumn();
            lblPosition.setText("Ln " + (para + 1) + ", Col " + (col + 1));
        });

        // Read-only binding
        readOnly.addListener((obs, ov, nv) -> codeArea.setEditable(!nv));
    }

    private void setupScrollPane() {
        scrollPane.getStyleClass().add("j-sql-scroll");
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    // ─── Status Bar ───────────────────────────────────────────────────────────────
    private void setupStatusBar() {
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setPadding(new Insets(0, 12, 0, 12));
        statusBar.setSpacing(12);
        statusBar.setPrefHeight(26);
        statusBar.setMinHeight(26);
        statusBar.setMaxHeight(26);
        statusBar.getStyleClass().add("j-sql-status-bar");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Theme toggle button
        lblTheme.getStyleClass().add("j-sql-status-label");
        lblTheme.setCursor(javafx.scene.Cursor.HAND);
        lblTheme.setOnMouseClicked(e -> toggleDarkMode());

        lblPosition.getStyleClass().add("j-sql-status-label");
        lblErrors.getStyleClass().addAll("j-sql-status-label", "j-sql-status-ok");
        lblErrors.setCursor(javafx.scene.Cursor.HAND);
        lblErrors.setOnMouseClicked(e -> navigateToFirstError());

        statusBar.getChildren().addAll(lblPosition, spacer, lblErrors, new Separator(Orientation.VERTICAL), lblTheme);
    }

    // ─── Autocomplete Popup ───────────────────────────────────────────────────────
    private void setupAutocomplete() {
        autocompleteList.setMaxHeight(180);
        autocompleteList.setPrefWidth(220);
        autocompleteList.getStyleClass().add("j-sql-autocomplete-list");
        autocompletePopup.getContent().add(autocompleteList);
        autocompletePopup.setAutoHide(true);

        autocompleteList.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB) {
                acceptAutocomplete();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                autocompletePopup.hide();
            }
        });
        autocompleteList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) acceptAutocomplete();
        });
    }

    private void showAutocomplete(String prefix) {
        String upper = prefix.toUpperCase();
        List<String> all = new ArrayList<>();
        KEYWORDS.stream().filter(k -> k.startsWith(upper)).sorted().forEach(all::add);
        TYPES.stream().filter(t -> t.startsWith(upper)).sorted().forEach(all::add);
        FUNCTIONS.stream().filter(f -> f.startsWith(upper)).sorted().forEach(all::add);

        if (all.isEmpty()) {
            autocompletePopup.hide();
            return;
        }

        autocompleteList.getItems().setAll(all);
        autocompleteList.getSelectionModel().selectFirst();

        // Position popup at caret
        codeArea.getCaretBounds().ifPresent(bounds -> {
            autocompletePopup.show(codeArea,
                bounds.getMaxX(),
                bounds.getMaxY() + 4);
        });
    }

    private void acceptAutocomplete() {
        String selected = autocompleteList.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        autocompletePopup.hide();

        // Replace the current word prefix with the completion
        int caretPos = codeArea.getCaretPosition();
        String text = codeArea.getText();
        int start = caretPos;
        while (start > 0 && Character.isLetterOrDigit(text.charAt(start - 1))) start--;
        codeArea.replaceText(start, caretPos, selected);
    }

    // ─── Reactive Subscriptions ───────────────────────────────────────────────────
    private void setupSubscriptions() {
        // Syntax highlighting — realtime, muy rápido (solo regex, no parsing)
        highlightSubscription = codeArea.multiPlainChanges()
            .successionEnds(Duration.ofMillis(30))
            .subscribe(changes -> highlightSyntax());

        // Validation — debounced 400ms (JSqlParser es más lento)
        validateSubscription = codeArea.multiPlainChanges()
            .successionEnds(Duration.ofMillis(400))
            .subscribe(changes -> validateSql(codeArea.getText()));
    }

    // ─── Keyboard Handling ────────────────────────────────────────────────────────
    private void handleKeyPressed(KeyEvent e) {
        KeyCode code = e.getCode();
        boolean ctrl = e.isControlDown() || e.isMetaDown();
        boolean shift = e.isShiftDown();

        if (autocompletePopup.isShowing()) {
            if (code == KeyCode.DOWN) {
                autocompleteList.requestFocus();
                return;
            }
            if (code == KeyCode.ESCAPE) {
                autocompletePopup.hide();
                return;
            }
        }

        switch (code) {
            case TAB -> {
                e.consume();
                if (autocompletePopup.isShowing()) {
                    acceptAutocomplete();
                } else {
                    codeArea.insertText(codeArea.getCaretPosition(), "    ");
                }
            }
            case ENTER -> {
                e.consume();
                handleSmartEnter();
            }
            case BACK_SPACE -> {
                // Smart backspace: borra 4 espacios si el cursor está precedido de 4 espacios
                int pos = codeArea.getCaretPosition();
                String text = codeArea.getText();
                if (pos >= 4 && text.substring(pos - 4, pos).equals("    ")
                        && codeArea.getSelectedText().isEmpty()) {
                    e.consume();
                    codeArea.replaceText(pos - 4, pos, "");
                }
            }
            case SLASH -> {
                if (ctrl) { e.consume(); toggleLineComment(); }
            }
            case D -> {
                if (ctrl && !shift) { e.consume(); duplicateLine(); }
            }
            case K -> {
                if (ctrl && shift) { e.consume(); deleteLine(); }
            }
            case F -> {
                if (ctrl && shift) { e.consume(); formatSql(); }
            }
            case SPACE -> {
                if (ctrl) {
                    e.consume();
                    triggerAutocomplete();
                }
            }
            default -> {}
        }
    }

    private void handleKeyTyped(KeyEvent e) {
        String ch = e.getCharacter();
        // Auto-close pairs
        if (!readOnly.get()) {
            switch (ch) {
                case "(" -> { e.consume(); insertPair("(", ")"); }
                case "[" -> { e.consume(); insertPair("[", "]"); }
                case "'" -> { e.consume(); handleSingleQuote(); }
            }
        }
        // Trigger autocomplete on alphanumeric typing
        if (ch.matches("[A-Za-z_]")) {
            String prefix = getCurrentWordPrefix();
            if (prefix.length() >= 2) {
                showAutocomplete(prefix);
            } else {
                autocompletePopup.hide();
            }
        } else if (ch.equals(" ") || ch.equals(";") || ch.equals("\n") || ch.equals("\r")) {
            autocompletePopup.hide();
            // Auto-uppercase keyword
            javafx.application.Platform.runLater(this::autoUppercaseLastWord);
        }
    }

    private void handleSmartEnter() {
        int pos = codeArea.getCaretPosition();
        String text = codeArea.getText();
        int lineStart = text.lastIndexOf('\n', pos - 1) + 1;
        String currentLine = text.substring(lineStart, pos);

        // Calculate leading indent
        StringBuilder indent = new StringBuilder("\n");
        for (char c : currentLine.toCharArray()) {
            if (c == ' ' || c == '\t') indent.append(c); else break;
        }
        // Extra indent after open paren
        if (pos > 0 && text.charAt(pos - 1) == '(') {
            indent.append("    ");
        }

        codeArea.insertText(pos, indent.toString());
    }

    private void insertPair(String open, String close) {
        int pos = codeArea.getCaretPosition();
        codeArea.insertText(pos, open + close);
        codeArea.moveTo(pos + 1);
    }

    private void handleSingleQuote() {
        int pos = codeArea.getCaretPosition();
        String text = codeArea.getText();
        // If next char is already a quote, just move past it
        if (pos < text.length() && text.charAt(pos) == '\'') {
            codeArea.moveTo(pos + 1);
            return;
        }
        // Count quotes before this position to determine open/close state
        long count = 0;
        for (int i = 0; i < pos; i++) {
            if (text.charAt(i) == '\'' && (i == 0 || text.charAt(i - 1) != '\\')) count++;
        }
        if (count % 2 == 0) {
            codeArea.insertText(pos, "''");
            codeArea.moveTo(pos + 1);
        } else {
            codeArea.insertText(pos, "'");
        }
    }

    private String getCurrentWordPrefix() {
        int pos = codeArea.getCaretPosition();
        String text = codeArea.getText();
        int start = pos;
        while (start > 0 && Character.isLetterOrDigit(text.charAt(start - 1))) start--;
        return text.substring(start, pos);
    }

    private void triggerAutocomplete() {
        String prefix = getCurrentWordPrefix();
        if (!prefix.isEmpty()) {
            showAutocomplete(prefix);
        } else {
            // Show all keywords
            autocompleteList.getItems().setAll(KEYWORDS.stream().sorted().toList());
            autocompleteList.getSelectionModel().selectFirst();
            codeArea.getCaretBounds().ifPresent(b ->
                autocompletePopup.show(codeArea, b.getMaxX(), b.getMaxY() + 4));
        }
    }

    private void autoUppercaseLastWord() {
        int pos = codeArea.getCaretPosition();
        String text = codeArea.getText();
        if (pos <= 1) return;
        // Word ends one char before caret (the space/semicolon was just typed)
        int end = pos - 1;
        int start = end;
        while (start > 0 && Character.isLetterOrDigit(text.charAt(start - 1))) start--;
        if (start >= end) return;
        String word = text.substring(start, end);
        String upper = word.toUpperCase();
        if ((KEYWORDS.contains(upper) || TYPES.contains(upper) || FUNCTIONS.contains(upper)) && !word.equals(upper)) {
            codeArea.replaceText(start, end, upper);
        }
    }

    // ─── Line Operations ─────────────────────────────────────────────────────────
    private void toggleLineComment() {
        int para = codeArea.getCurrentParagraph();
        String line = codeArea.getParagraph(para).getText();
        String stripped = line.stripLeading();
        if (stripped.startsWith("-- ")) {
            codeArea.replaceText(para, 0, para, line.length(),
                line.replaceFirst("-- ", ""));
        } else if (stripped.startsWith("--")) {
            codeArea.replaceText(para, 0, para, line.length(),
                line.replaceFirst("--", ""));
        } else {
            codeArea.replaceText(para, 0, para, 0, "-- ");
        }
    }

    private void duplicateLine() {
        int para = codeArea.getCurrentParagraph();
        String line = codeArea.getParagraph(para).getText();
        int end = codeArea.getAbsolutePosition(para, line.length());
        codeArea.insertText(end, "\n" + line);
    }

    private void deleteLine() {
        int para = codeArea.getCurrentParagraph();
        int paragraphs = codeArea.getParagraphs().size();
        if (paragraphs == 1) {
            codeArea.replaceText("");
            return;
        }
        int lineStart = codeArea.getAbsolutePosition(para, 0);
        int lineEnd = codeArea.getAbsolutePosition(para, codeArea.getParagraph(para).length());
        if (para < paragraphs - 1) {
            codeArea.deleteText(lineStart, lineEnd + 1); // +1 for the newline
        } else {
            codeArea.deleteText(lineStart - 1, lineEnd); // remove preceding newline
        }
    }

    // ─── SQL Formatter ────────────────────────────────────────────────────────────
    public void formatSql() {
        String sql = codeArea.getText().trim();
        if (sql.isEmpty()) return;
        try {
            String formatted = SqlFormatter.of(Dialect.StandardSql)
                .format(sql, FormatConfig.builder()
                    .indent("    ")
                    .uppercase(true)
                    .linesBetweenQueries(1)
                    .maxColumnLength(80)
                    .build());
            codeArea.replaceText(formatted);
        } catch (Exception ex) {
            // Si el formatter falla (SQL inválido), no hacer nada
        }
    }

    // ─── Syntax Highlighting (RichTextFX StyleSpans) ──────────────────────────────
    private void highlightSyntax() {
        String text = codeArea.getText();
        if (text.isEmpty()) return;
        try {
            StyleSpans<Collection<String>> spans = computeHighlighting(text);
            codeArea.setStyleSpans(0, spans);
        } catch (Exception ignored) {}
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        Matcher matcher = TOKEN_PATTERN.matcher(text);
        int lastEnd = 0;

        while (matcher.find()) {
            // Gap before this token
            if (matcher.start() > lastEnd) {
                spansBuilder.add(Collections.emptyList(), matcher.start() - lastEnd);
            }

            List<String> styleClasses;

            if (matcher.group(1) != null) {
                styleClasses = List.of("sql-comment", "sql-block-comment");
            } else if (matcher.group(2) != null) {
                styleClasses = List.of("sql-comment");
            } else if (matcher.group(3) != null) {
                styleClasses = List.of("sql-string");
            } else if (matcher.group(4) != null) {
                styleClasses = List.of("sql-quoted-ident");
            } else if (matcher.group(5) != null) {
                styleClasses = List.of("sql-number");
            } else if (matcher.group(6) != null) {
                String word = matcher.group(6).toUpperCase();
                if (KEYWORDS.contains(word)) {
                    styleClasses = List.of("sql-keyword");
                } else if (TYPES.contains(word)) {
                    styleClasses = List.of("sql-type");
                } else if (FUNCTIONS.contains(word)) {
                    styleClasses = List.of("sql-function");
                } else {
                    styleClasses = Collections.emptyList();
                }
            } else if (matcher.group(7) != null) {
                styleClasses = List.of("sql-operator");
            } else if (matcher.group(8) != null) {
                styleClasses = List.of("sql-punctuation");
            } else {
                styleClasses = Collections.emptyList();
            }

            spansBuilder.add(styleClasses, matcher.end() - matcher.start());
            lastEnd = matcher.end();
        }

        if (lastEnd < text.length()) {
            spansBuilder.add(Collections.emptyList(), text.length() - lastEnd);
        }

        return spansBuilder.create();
    }

    // ─── Validation ───────────────────────────────────────────────────────────────
    private void validateSql(String text) {
        List<SqlError> found = new ArrayList<>();

        if (!text.trim().isEmpty()) {
            try {
                CCJSqlParserUtil.parseStatements(text);
            } catch (JSQLParserException e) {
                Throwable cause = e.getCause();
                if (cause instanceof ParseException pe && pe.currentToken != null) {
                    var token = pe.currentToken.next;
                    found.add(new SqlError(token.beginLine, token.beginColumn - 1,
                        token.endColumn, "Error de sintaxis cerca de '" + token.image + "'"));
                } else {
                    Matcher m = Pattern.compile("(?i)at line (\\d+)").matcher(
                        e.getMessage() != null ? e.getMessage() : "");
                    if (m.find()) {
                        found.add(new SqlError(Integer.parseInt(m.group(1)), 0, 1,
                            "Error de sintaxis SQL"));
                    }
                }
            }
            checkParenBalance(text, found);
        }

        errors.setAll(found);
        javafx.application.Platform.runLater(this::updateErrorIndicators);
    }

    private void checkParenBalance(String text, List<SqlError> out) {
        int depth = 0, line = 1;
        for (char c : text.toCharArray()) {
            if (c == '\n') line++;
            else if (c == '(') depth++;
            else if (c == ')' && --depth < 0) {
                out.add(new SqlError(line, 0, 1, "Paréntesis de cierre inesperado"));
                depth = 0;
            }
        }
        if (depth > 0) {
            out.add(new SqlError(codeArea.getParagraphs().size(), 0, 1,
                depth + " paréntesis sin cerrar"));
        }
    }

    private void updateErrorIndicators() {
        // Apply error paragraph styles (highlight error lines in red)
        Set<Integer> errorLines = new HashSet<>();
        for (SqlError e : errors) errorLines.add(e.line() - 1);

        for (int i = 0; i < codeArea.getParagraphs().size(); i++) {
            if (errorLines.contains(i)) {
                codeArea.setParagraphStyle(i, List.of("sql-error-line"));
            } else {
                codeArea.setParagraphStyle(i, Collections.emptyList());
            }
        }

        // Update status bar
        if (errors.isEmpty()) {
            lblErrors.setText("✓ Sin errores");
            lblErrors.getStyleClass().removeAll("j-sql-status-error");
            if (!lblErrors.getStyleClass().contains("j-sql-status-ok"))
                lblErrors.getStyleClass().add("j-sql-status-ok");
        } else {
            int n = errors.size();
            lblErrors.setText("✕ " + n + " error" + (n > 1 ? "es" : ""));
            lblErrors.getStyleClass().removeAll("j-sql-status-ok");
            if (!lblErrors.getStyleClass().contains("j-sql-status-error"))
                lblErrors.getStyleClass().add("j-sql-status-error");
        }
    }

    // ─── Dark / Light Theme ───────────────────────────────────────────────────────
    public void toggleDarkMode() {
        darkMode.set(!darkMode.get());
        applyThemeStyle(darkMode.get());
        lblTheme.setText(darkMode.get() ? "☾ Oscuro" : "☀ Claro");
    }

    private void applyThemeStyle(boolean dark) {
        if (dark) {
            setStyle(
                "-fx-background-color: #0f172a;" +
                "-fx-border-color: #334155;" +
                "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0, 0, 3);"
            );
            // Load dark stylesheet
            getStylesheets().removeIf(s -> s.contains("sql-editor-light"));
            if (!getStylesheets().stream().anyMatch(s -> s.contains("sql-editor-dark"))) {
                getStylesheets().add(getClass().getResource("/css/sql-editor-dark.css").toExternalForm());
            }
        } else {
            setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #e2e8f0;" +
                "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2);"
            );
            getStylesheets().removeIf(s -> s.contains("sql-editor-dark"));
            if (!getStylesheets().stream().anyMatch(s -> s.contains("sql-editor-light"))) {
                getStylesheets().add(getClass().getResource("/css/sql-editor-light.css").toExternalForm());
            }
        }
    }

    private void navigateToFirstError() {
        if (errors.isEmpty()) return;

        SqlError first = errors.get(0);
        int targetLine = first.line() - 1;  // RichTextFX es 0-indexed
        int totalParagraphs = codeArea.getParagraphs().size();

        if (targetLine < 0 || targetLine >= totalParagraphs) return;

        // Mover el caret al inicio de la línea del error
        int col = Math.min(first.startCol(), codeArea.getParagraph(targetLine).length());
        codeArea.moveTo(targetLine, col);

        // Hacer visible la línea (scroll hacia ella)
        codeArea.requestFollowCaret();

        // Seleccionar el rango del error para que se vea claramente
        int endCol = Math.min(first.endCol(), codeArea.getParagraph(targetLine).length());
        if (endCol > col) {
            codeArea.selectRange(targetLine, col, targetLine, endCol);
        }

        // Focus al editor para que la selección sea visible
        codeArea.requestFocus();
    }

    private void fixScrollBarThumb() { 
        scrollPane.lookupAll(".scroll-bar").forEach(node -> {
            if (node instanceof ScrollBar sb) {
                sb.setPrefWidth(SCROLLBAR_WIDTH);
                sb.setMinWidth(SCROLLBAR_WIDTH); 
                sb.visibleAmountProperty().addListener((obs, ov, nv) -> {
                    double range = sb.getMax() - sb.getMin();
                    if (range <= 0) return;
                    double trackLength = (sb.getOrientation() == Orientation.VERTICAL)
                        ? sb.getHeight()
                        : sb.getWidth();

                    if (trackLength <= 0) return;

                    double minRatio = THUMB_MIN_SIZE / trackLength;
                    double currentRatio = nv.doubleValue() / range;

                    if (currentRatio < minRatio) {
                        sb.setVisibleAmount(minRatio * range);
                    }
                });
                javafx.application.Platform.runLater(() -> {
                    javafx.scene.Node thumb = sb.lookup(".thumb");
                    if (thumb != null) {
                        if (sb.getOrientation() == Orientation.VERTICAL) {
                            ((Region) thumb).setMinHeight(THUMB_MIN_SIZE);
                        } else {
                            ((Region) thumb).setMinWidth(THUMB_MIN_SIZE);
                        }
                    }
                });
            }
        });
    }

    // ─── Cleanup ──────────────────────────────────────────────────────────────────
    public void dispose() {
        if (highlightSubscription != null) highlightSubscription.unsubscribe();
        if (validateSubscription != null) validateSubscription.unsubscribe();
    }

    // ─── Public API ───────────────────────────────────────────────────────────────
    public String getSql() { return codeArea.getText(); }
    public void setSql(String sql) { codeArea.replaceText(sql); }
    public String getText() { return codeArea.getText(); }
    public void setText(String text) { codeArea.replaceText(text); }
    public void clear() { codeArea.clear(); }
    public void focusEditor() { codeArea.requestFocus(); }
    public void selectAll() { codeArea.selectAll(); }
    public void scrollToTop() { codeArea.scrollYToPixel(0); codeArea.scrollXToPixel(0); }

    public ObservableList<SqlError> getSqlErrors() { return errors; }

    public boolean isReadOnly() { return readOnly.get(); }
    public void setReadOnly(boolean v) { readOnly.set(v); }
    public BooleanProperty readOnlyProperty() { return readOnly; }

    public boolean isDarkMode() { return darkMode.get(); }
    public void setDarkMode(boolean v) { darkMode.set(v); applyThemeStyle(v); }
    public BooleanProperty darkModeProperty() { return darkMode; }

    public void copyToClipboard() {
        ClipboardContent cc = new ClipboardContent();
        cc.putString(codeArea.getText());
        Clipboard.getSystemClipboard().setContent(cc);
    }

    /** Expone el CodeArea interno por si necesitas acceder a API avanzada de RichTextFX */
    public CodeArea getCodeArea() { return codeArea; }
}