# COMPONENTS.md — Catálogo Rápido de Componentes FxStyle para Agentes de IA

> **Propósito de este archivo**: Este documento es la guía de referencia instantánea para Agentes de Inteligencia Artificial (Claude, Gemini, ChatGPT, Grok, Copilot, etc.).  
> Permite conocer **toda la librería FxStyle** sin necesidad de escanear ni parsear los archivos `.java` del código fuente.  
> Cada componente detalla su equivalente nativo en JavaFX, para qué sirve, sus constructores y un snippet de código listo para usar.

---

## 🚀 1. Reglas Rápidas de Integración

1. **Paquete base de componentes**: `com.jjarroyo.components.*`
2. **Paquete de animaciones**: `com.jjarroyo.animation.JAnimation`
3. **Inicialización obligatoria** en tu clase `Application` o controlador principal:
   ```java
   import com.jjarroyo.FxStyle;
   
   // En start(Stage stage):
   FxStyle.init(scene); // Carga la hoja de estilos global fxstyle.css
   FxStyle.setModalContainer(rootStackPane); // Registra el StackPane raíz para modales
   ```
4. **Regla de Oro para la IA**:  
   **Nunca instancies controles nativos estándar planos** (`new Button()`, `new TextField()`, `new ComboBox()`) si existe un componente `J*` en FxStyle. Usa siempre el componente de FxStyle correspondiente (`JButton`, `JInput`, `JSelect`, etc.).

---

## ⚡ 2. Tabla Cheat-Sheet de Equivalencias (Quick Lookup)

| Componente FxStyle | Equivalente JavaFX Nativo | Propósito en una frase |
| :--- | :--- | :--- |
| **`JButton`** | `Button` | Botón moderno con soporte de variantes de color, iconos SVG y estado de carga (`loading`). |
| **`JSplitButton`** | `HBox` / `MenuButton` | Botón dividido: acción principal a la izquierda y menú desplegable a la derecha. |
| **`JFloatingButton`**| `Button` | Botón de acción flotante (FAB) circular para acciones prioritarias. |
| **`JSwitch`** | `CheckBox` | Interruptor toggle on/off moderno tipo iOS/Tailwind. |
| **`JRippleContainer`**| `StackPane` | Contenedor envoltorio que genera ondas táctiles (ripple effect) al hacer clic. |
| **`JInput`** | `TextField` | Campo de texto con estilos suaves, autocompletado y slot para icono o botón a la derecha. |
| **`JPasswordInput`** | `TextField` / `HBox` | Campo de contraseña con botón integrado de alternar visibilidad (ojo mostrar/ocultar). |
| **`JNumberInput`** | `HBox` / `Spinner` | Input numérico con botones +/- integrados, step y límites min/max. |
| **`JSearchInput`** | `HBox` / `TextField` | Campo de búsqueda con icono de lupa y botón para limpiar texto en un clic. |
| **`JInputGroup`** | `HBox` | Contenedor para agrupar inputs con prefijos, sufijos o botones pegados en una sola fila. |
| **`JSelect<T>`** | `ComboBox<T>` | Selector desplegable con popup elegante, filtro de búsqueda integrado y selección múltiple. |
| **`JCheckBox`** | `CheckBox` | Casilla de verificación moderna con marca estilizada. |
| **`JRadioButton`** | `RadioButton` | Botón de opción de selección única estilizado. |
| **`JSlider`** | `Slider` | Control deslizante moderno para selección de rangos o valores. |
| **`JDatePicker`** | `DatePicker` | Selector de fechas con calendario popup moderno. |
| **`JTimePicker`** | `HBox` | Selector de tiempo (horas y minutos) con diseño limpio. |
| **`JCalendar`** | `VBox` | Calendario mensual interactivo completo con navegación entre meses. |
| **`JTextArea`** | `TextArea` | Área de texto multilínea con esquinas redondeadas y padding suave. |
| **`JRating`** | `HBox` | Calificación por estrellas interactiva de 1 a 5 (o personalizado). |
| **`JTagInput`** | `FlowPane` | Campo para agregar y remover etiquetas o chips dinámicamente. |
| **`JFile`** | `HBox` / `VBox` | Selector de archivos con botón examinar, drag-and-drop y visualización de ruta. |
| **`JAlert`** | `HBox` | Banner de alerta contextual (success, danger, warning, info) con icono y cerrar. |
| **`JModal`** | `StackPane` | Diálogo modal flotante superpuesto con backdrop oscuro, scroll interno y tamaños. |
| **`JConfirmDialog`**| `JModal` | Modal de confirmación rápida con botones Aceptar/Cancelar y callback. |
| **`JToast`** | `Popup` | Mensajes toast flotantes temporizados en cualquier esquina de la pantalla. |
| **`JNotification`** | `VBox` / `Popup` | Centro y elementos de notificación con badges de estado. |
| **`JProgressBar`** | `ProgressBar` | Barra de progreso lineal moderna con variantes de color. |
| **`JCircularProgress`**| `ProgressIndicator` | Indicador de carga circular moderno con animación suave. |
| **`JSkeleton`** | `Region` | Placeholder de carga tipo "esqueleto" con animación shimmer para estados de loading. |
| **`JTable<T>`** | `TableView<T>` / `VBox` | Tabla empresarial con buscador integrado, checkboxes, acciones por fila y exportación CSV. |
| **`JTreeView<T>`** | `TreeView<T>` | Árbol jerárquico moderno para estructuras anidadas. |
| **`JCard`** | `VBox` | Tarjeta contenedora con header, título, toolbar, body con scroll y footer. |
| **`JStatCard`** | `VBox` | Tarjeta KPI para dashboards con valor animado, porcentaje de cambio y tendencia. |
| **`JAnimatedNumber`**| `Label` | Etiqueta numérica con animación fluida CountUp/CountDown para métricas y finanzas. |
| **`JFlipCard`** | `StackPane` | Tarjeta 3D con cara frontal y trasera que rota 180° al hacer clic o hover. |
| **`JMarquee`** | `StackPane` | Tira o marquesina de texto con desplazamiento continuo horizontal (ticker de noticias). |
| **`JGauge`** / **`JGaugeCard`** | `StackPane` | Medidor de aguja / arco radial tipo velocímetro con zonas de color configurables. |
| **`JLed`** | `Region` | Indicador luminoso LED industrial (redondo/cuadrado, varios colores y parpadeo). |
| **`JTowerLight`** | `VBox` | Torre de baliza / semáforo industrial (rojo, amarillo, verde, azul) con estados. |
| **`JBattery`** | `HBox` / `Region` | Indicador visual de porcentaje de batería con cambio de color y estado de carga. |
| **`JAvatar`** | `StackPane` | Avatar de usuario circular con imagen o iniciales y color aleatorio o asignado. |
| **`JBadge`** | `Label` | Insignia pequeña para conteos numéricos o estados cortos. |
| **`JStatusBadge`** | `HBox` | Insignia con punto indicador luminoso (*dot*) y animación de pulso (online/offline/live). |
| **`JRibbon`** | `Label` | Cinta / lazo decorativo para esquinas de tarjetas (`TOP_RIGHT`, `TOP_LEFT`). |
| **`JChip`** | `HBox` | Cápsula compacta con texto, icono y botón de eliminación opcional. |
| **`JTimeline`** | `VBox` | Línea de tiempo visual para eventos secuenciales, auditoría o bitácoras. |
| **`JChart`** | `VBox` | Contenedor y estilizado de gráficos estadísticos para dashboards. |
| **`JLabel`** | `Label` | Etiqueta de texto con clases tipográficas preconfiguradas. |
| **`JParagraph`** | `Label` / `TextFlow` | Párrafo de texto estilizado para lectura y explicaciones. |
| **`JSidebar`** | `VBox` | Barra lateral de navegación profesional con modo colapsable (íconos o invisible). |
| **`JSidebarItem`** | `HBox` | Ítem de navegación para el sidebar con icono, texto y badge opcional. |
| **`JSidebarSubmenu`**| `VBox` | Submenú desplegable anidado dentro de un sidebar. |
| **`JHeader`** | `HBox` | Barra superior de encabezado con buscador, acciones y perfil de usuario. |
| **`JTabs`** / **`JTab`**| `TabPane` / `Tab` | Sistema de pestañas moderno con transición suave entre contenidos. |
| **`JDrawer`** | `StackPane` | Panel lateral deslizante (offcanvas) que entra desde izquierda o derecha. |
| **`JAccordion`** | `Accordion` / `VBox` | Acordeón colapsable con soporte para expandir múltiples paneles a la vez. |
| **`JAccordionPane`**| `TitledPane` | Panel individual para `JAccordion`. |
| **`JTitleBar`** | `HBox` | Barra de título de ventana personalizada con botones de minimizar, maximizar y cerrar. |
| **`JBreadcrumb`** | `HBox` | Miga de pan de navegación jerárquica con separadores estilizados. |
| **`JPagination`** | `HBox` / `Pagination` | Barra de paginación para tablas y listas de datos. |
| **`JStepper`** | `HBox` | Control de progreso de pasos guiados (asistentes paso a paso / wizards). |
| **`JPopover`** | `Popup` | Ventana emergente con flecha indicadora anclada a cualquier nodo. |
| **`JDropdown`** | `ContextMenu` | Menú desplegable con diseño moderno y sombras suaves. |
| **`JTooltip`** | `Tooltip` | Tooltip flotante estilizado con fondo oscuro y texto claro. |
| **`JSqlEditor`** | `VBox` | Editor de consultas SQL con área de texto y barra de acciones. |
| **`JDesignCanvas`** | `Pane` | Lienzo interactivo para arrastrar, soltar y posicionar elementos visuales. |
| **`JZoom`** | `ScrollPane` | Contenedor con zoom mediante rueda del ratón y arrastre (pan & zoom). |
| **`JList`** | `VBox` / `ListView` | Lista vertical de ítems estilizados con separadores y hover. |
| **`JDraggableField`**| `JSidebarItem` | Campo arrastrable mediante Drag and Drop para constructores visuales. |
| **`JAnimatedSwitcher`**| `StackPane` | Contenedor que anima la transición entre vistas (Fade, Slide, Zoom). |
| **`JIcon`** | *(Enum SVG)* | Catálogo de más de 100 iconos SVG con método `.view()` o `.view(hexColor)`. |
| **`JAnimation`** | *(Utility Engine)* | Motor fluido de animaciones declarativas tipo Animate.css (`fadeIn`, `pulse`, etc.). |

---

## 📦 3. Catálogo Detallado por Componente

### 1. Botones y Acciones

#### `JButton`
- **Hereda de**: `javafx.scene.control.Button`
- **Descripción**: Botón estilizado moderno. Soporta iconos directos, clases de color y spinner de carga integrado.
- **Clases CSS disponibles**: `btn-primary`, `btn-secondary`, `btn-success`, `btn-danger`, `btn-warning`, `btn-dark`, `btn-outline-primary`, `btn-sm`, `btn-lg`, `btn-rounded`.
- **Métodos clave**:
  - `setLoading(boolean loading)`: Muestra u oculta automáticamente un spinner y deshabilita el botón mientras dura una operación asíncrona.
  - `setIcon(JIcon icon)` / `setIcon(JIcon icon, String hexColor)`
- **Ejemplo**:
  ```java
  JButton btn = new JButton("Guardar", JIcon.CHECK);
  btn.getStyleClass().add("btn-success");
  btn.setOnAction(e -> {
      btn.setLoading(true);
      // realizar tarea en background...
  });
  ```

#### `JSplitButton`
- **Hereda de**: `javafx.scene.layout.HBox`
- **Descripción**: Botón dividido en dos segmentos: el botón principal con acción por defecto y un botón con flecha que despliega un `ContextMenu`.
- **Constructores / Métodos**:
  - `JSplitButton()`
  - `setText(String text)`, `setIcon(JIcon icon)`
  - `setOnAction(EventHandler<ActionEvent> handler)`
  - `setVariant(Variant variant)`: `PRIMARY`, `SECONDARY`, `SUCCESS`, `DANGER`, `WARNING`, `DARK`, `OUTLINE`.
  - `addItem(String text, EventHandler<ActionEvent> handler)`, `addSeparator()`
- **Ejemplo**:
  ```java
  JSplitButton split = new JSplitButton();
  split.setText("Exportar");
  split.setOnAction(e -> exportarCsv());
  split.addItem("Exportar PDF", e -> exportarPdf());
  split.addItem("Exportar Excel", e -> exportarExcel());
  ```

#### `JFloatingButton`
- **Hereda de**: `javafx.scene.control.Button`
- **Descripción**: Botón de acción flotante (FAB) circular para acciones rápidas en esquinas.
- **Constructores**: `new JFloatingButton(JIcon.ADD)` o `new JFloatingButton("+")`.

#### `JSwitch`
- **Hereda de**: `JCheckBox` -> `javafx.scene.control.CheckBox`
- **Descripción**: Interruptor deslizante on/off moderno.
- **Constructores / Métodos**:
  - `new JSwitch("Notificaciones push")`
  - `isSelected()`, `setSelected(boolean value)`, `selectedProperty()`
- **Ejemplo**:
  ```java
  JSwitch switchToggle = new JSwitch("Modo Oscuro");
  switchToggle.selectedProperty().addListener((obs, old, isDark) -> cambiarTema(isDark));
  ```

#### `JRippleContainer`
- **Hereda de**: `javafx.scene.layout.StackPane`
- **Descripción**: Envuelve cualquier control JavaFX y agrega un efecto de onda táctil Material (ripple) al hacer clic.
- **Métodos**: `setContent(Node node)`, `setRippleColor(Paint paint)`, `setDuration(Duration duration)`.
- **Ejemplo**:
  ```java
  JRippleContainer ripple = new JRippleContainer();
  ripple.setContent(new JCard("Título", new Label("Contenido con efecto onda")));
  ```

---

### 2. Formularios y Entradas de Datos

#### `JInput`
- **Hereda de**: `javafx.scene.control.TextField`
- **Descripción**: Campo de texto estilizado que sustituye a `TextField`. Incluye soporte de autocompletado y método para incrustar un botón o icono a la derecha sin desalinear la UI.
- **Constructores / Métodos**:
  - `new JInput()`, `new JInput("Escribe tu nombre...")`
  - `addClass(String... classes)`
  - `createWithRightNode(Node rightNode, double rightOffsetPadding)`: Devuelve un `StackPane` con el input y un nodo (ej. botón o enlace) alineado a la derecha.
  - `setupAutocomplete(List<String> suggestions)`
- **Ejemplo**:
  ```java
  JInput emailInput = new JInput("correo@empresa.com");
  ```

#### `JPasswordInput`
- **Hereda de**: `javafx.scene.layout.HBox` (gestiona internamente el `PasswordField` y `TextField`)
- **Descripción**: Campo de contraseña con botón de ojo para alternar visibilidad de texto plano a puntos ocultos.
- **Constructores / Métodos**:
  - `new JPasswordInput("Contraseña")`
  - `getText()`, `setText(String value)`, `textProperty()`
- **Ejemplo**:
  ```java
  JPasswordInput passInput = new JPasswordInput("Ingrese contraseña...");
  String pass = passInput.getText();
  ```

#### `JNumberInput`
- **Hereda de**: `javafx.scene.layout.HBox`
- **Descripción**: Entrada numérica estilizada con botones de incremento (+) y decremento (-), step configurable y validación numérica automática.
- **Métodos**: `getValue()`, `setValue(double val)`, `setMin(double min)`, `setMax(double max)`, `setStep(double step)`.

#### `JSearchInput`
- **Hereda de**: `javafx.scene.layout.HBox`
- **Descripción**: Campo de búsqueda estilizado con icono de lupa integrado a la izquierda y botón "X" para limpiar el texto a la derecha.
- **Métodos**: `getText()`, `textProperty()`, `setOnSearch(Consumer<String> callback)`.

#### `JInputGroup`
- **Hereda de**: `javafx.scene.layout.HBox`
- **Descripción**: Contenedor para unir visualmente un prefijo (ej. `$`, `@`), un `JInput` y un botón en una sola pieza estética sin bordes dobles.
- **Ejemplo**:
  ```java
  JInputGroup group = new JInputGroup();
  group.addPrefix(new Label("https://"));
  group.setInput(new JInput("tudominio.com"));
  group.addSuffixButton(new JButton(".com"));
  ```

#### `JSelect<T>`
- **Hereda de**: `javafx.scene.layout.StackPane`
- **Descripción**: Selector desplegable moderno que reemplaza a `ComboBox`. Cuenta con popup flotante fluido, buscador opcional de opciones y selección múltiple opcional.
- **Constructores / Métodos**:
  - `new JSelect<>()`
  - `getItems().addAll(...)`
  - `setSearchable(true)`: Agrega barra de búsqueda interna para filtrar opciones.
  - `setMultiple(true)`: Habilita selección de múltiples ítems con checkboxes.
  - `getSelectedItem()`, `getSelectedItems()`, `selectedItemProperty()`
- **Ejemplo**:
  ```java
  JSelect<String> select = new JSelect<>();
  select.getItems().addAll("Admin", "Editor", "Invitado");
  select.setSearchable(true);
  select.selectedItemProperty().addListener((o, prev, actual) -> System.out.println("Seleccionado: " + actual));
  ```

#### `JCheckBox` & `JRadioButton`
- **Heredan de**: `javafx.scene.control.CheckBox` y `javafx.scene.control.RadioButton`.
- **Descripción**: Casillas y botones de opción con diseño moderno y soporte de tema.

#### `JSlider`
- **Hereda de**: `javafx.scene.control.Slider`
- **Descripción**: Barra deslizadora estilizada para valores continuos o rangos.

#### `JDatePicker` & `JTimePicker`
- **`JDatePicker`**: Reemplaza a `DatePicker` con calendario emergente estilizado.
- **`JTimePicker`**: Control para seleccionar horas y minutos con formato 24h o 12h.

#### `JCalendar`
- **Hereda de**: `javafx.scene.layout.VBox`
- **Descripción**: Calendario mensual visual completo e interactivo con navegación entre meses y selección de fechas.

#### `JTextArea`
- **Hereda de**: `javafx.scene.control.TextArea`
- **Descripción**: Campo de texto multilínea con esquinas redondeadas y padding moderno.

#### `JRating`
- **Hereda de**: `javafx.scene.layout.HBox`
- **Descripción**: Control de puntuación con estrellas interactivas (hover y click).
- **Métodos**: `setRating(int score)`, `getRating()`, `ratingProperty()`.

#### `JTagInput`
- **Hereda de**: `javafx.scene.layout.FlowPane`
- **Descripción**: Campo de entrada que convierte palabras en etiquetas o chips al presionar `Enter`, permitiendo eliminarlas con un clic en la 'x'.
- **Métodos**: `getTags()`, `addTag(String tag)`, `removeTag(String tag)`.

#### `JFile`
- **Hereda de**: `javafx.scene.layout.HBox` / `VBox`
- **Descripción**: Selector de archivos estilizado con botón de búsqueda en el explorador del sistema, drag & drop y etiqueta con la ruta del archivo seleccionado.

---

### 3. Feedback, Notificaciones y Diálogos

#### `JModal`
- **Hereda de**: `javafx.scene.layout.StackPane`
- **Descripción**: Diálogo modal superpuesto que se presenta sobre el contenedor configurado en `FxStyle.setModalContainer(rootStackPane)`. Tiene backdrop semitransparente con desenfoque/oscurecimiento, header con botón cerrar, cuerpo con scroll automático si el contenido excede la pantalla, y footer para botones de acción.
- **Tamaños (`JModal.Size`)**: `SMALL`, `MEDIUM`, `LARGE`, `FULL`.
- **Constructores / Métodos**:
  - `new JModal(String title, Node body, JModal.Size size)`
  - `setTitle(String title)`, `setBody(Node body)`, `setFooter(Node footer)`
  - `open()`, `close()`
- **Ejemplo**:
  ```java
  VBox body = new VBox(10, new Label("¿Seguro de guardar los cambios?"));
  JButton btnConfirmar = new JButton("Confirmar");
  
  JModal modal = new JModal("Confirmación", body, JModal.Size.MEDIUM);
  modal.setFooter(new HBox(10, btnConfirmar));
  btnConfirmar.setOnAction(e -> {
      guardar();
      modal.close();
  });
  modal.open();
  ```

#### `JConfirmDialog`
- **Descripción**: Modal prefabricado para diálogos de confirmación ("¿Desea eliminar este registro?").
- **Método estático**: `JConfirmDialog.show("Eliminar", "¿Está seguro?", () -> { /* acción si confirma */ });`

#### `JToast`
- **Hereda de**: `javafx.stage.Popup`
- **Descripción**: Mensajes flotantes efímeros que aparecen en esquinas y se cierran automáticamente tras un tiempo configurado.
- **Tipos (`JToast.Type`)**: `DEFAULT`, `SUCCESS`, `DANGER`, `WARNING`, `INFO`.
- **Posiciones (`JToast.Position`)**: `TOP_RIGHT`, `TOP_LEFT`, `BOTTOM_RIGHT`, `BOTTOM_LEFT`, `TOP_CENTER`, `BOTTOM_CENTER`.
- **Uso estático**:
  ```java
  JToast.show(stage, "Éxito", "Usuario creado correctamente", JToast.Type.SUCCESS, JToast.Position.TOP_RIGHT, 3000);
  ```

#### `JAlert`
- **Hereda de**: `javafx.scene.layout.HBox`
- **Descripción**: Banner de alerta insertable en layouts con icono contextual y opción de botón de cerrar.
- **Constructores**: `new JAlert(String message, AlertType type)` o con título y contenido.

#### `JSkeleton`
- **Hereda de**: `javafx.scene.layout.Region`
- **Descripción**: Rectángulo o círculo con animación de gradiente (shimmer) para usar como placeholder mientras cargan datos asíncronos.
- **Constructores**: `new JSkeleton(double width, double height)` o `new JSkeleton(double radius)`.

#### `JProgressBar` & `JCircularProgress`
- Controles de progreso lineales y circulares con estilo Tailwind/Moderno.

---

### 4. Datos y Visualización para Dashboards

#### `JTable<T>`
- **Hereda de**: `javafx.scene.layout.VBox`
- **Descripción**: Tabla empresarial completa para grandes volúmenes de datos. Integra internamente barra de búsqueda, selector de filas por checkboxes, paginación opcional, ordenamiento, renderizadores de celdas personalizados, botones de acción por fila y exportación a CSV.
- **Métodos clave**:
  - `setCheckable(true)`: Agrega columna de checkboxes a la izquierda.
  - `setSearchable(true)`: Muestra barra de búsqueda en tiempo real arriba de la tabla.
  - `setStriped(true)`: Filas alternadas en color.
  - `addColumn(String title, String propertyName)`
  - `addColumn(String title, String propertyName, Function<T, Node> cellRenderer)`
  - `setRowActions((item, hbox) -> { ... })`: Añade botones o menús por cada fila.
  - `setItems(ObservableList<T> items)`
- **Ejemplo**:
  ```java
  JTable<User> table = new JTable<>();
  table.setSearchable(true);
  table.setCheckable(true);
  table.addColumn("ID", "id");
  table.addColumn("Nombre", "name");
  table.addColumn("Rol", "role", user -> new JChip(user.getRole()));
  table.setRowActions((user, box) -> {
      JButton edit = new JButton("", JIcon.EDIT);
      edit.setOnAction(e -> editarUsuario(user));
      box.getChildren().add(edit);
  });
  table.setItems(listaUsuarios);
  ```

#### `JCard`
- **Hereda de**: `javafx.scene.layout.VBox`
- **Descripción**: Tarjeta base de interfaz con bordes redondeados y sombra suave. Permite definir título, subtítulo, toolbar superior de acciones, cuerpo y pie de página.
- **Constructores / Métodos**:
  - `new JCard(String title, Node body)`
  - `setTitle(String title)`, `setSubtitle(String subtitle)`
  - `setToolbar(Node... nodes)`
  - `setBody(Node content)`, `setFooter(Node footer)`
- **Ejemplo**:
  ```java
  JCard card = new JCard("Ventas Recientes", tablaVentas);
  card.setToolbar(new JButton("Refrescar", JIcon.REFRESH));
  ```

#### `JStatCard`
- **Hereda de**: `javafx.scene.layout.VBox`
- **Descripción**: Tarjeta KPI lista para dashboards ejecutivos. Muestra título, valor principal animado, icono descriptivo y porcentaje de tendencia (+12.5% verde / -3.2% rojo).
- **Constructores / Métodos**:
  - `new JStatCard()`
  - `setTitle(String title)`
  - `setValue(String value)` o `setValueAnimated(double value, Duration duration)`
  - `setIcon(Node icon, String colorClass)`
  - `setTrend(String trendText, boolean isPositive)`
- **Ejemplo**:
  ```java
  JStatCard stat = new JStatCard();
  stat.setTitle("Ingresos Mensuales");
  stat.setValueAnimated(48500.0, Duration.millis(1200));
  stat.setIcon(JIcon.MONEY.view("#10b981"), "stat-icon-green");
  stat.setTrend("+14.2% vs mes anterior", true);
  ```

#### `JAnimatedNumber`
- **Hereda de**: `javafx.scene.control.Label`
- **Descripción**: Etiqueta que anima fluidamente las transiciones numéricas con efecto CountUp / CountDown.
- **Métodos**: `animateTo(double targetValue)`, `setPrefix("$")`, `setSuffix(" USD")`, `setDecimalPlaces(2)`.

#### `JFlipCard`
- **Hereda de**: `javafx.scene.layout.StackPane`
- **Descripción**: Tarjeta 3D con cara frontal (`setFront(Node)`) y posterior (`setBack(Node)`). Rota 180° en 3D al hacer clic o con `flip()`.

#### `JMarquee`
- **Hereda de**: `javafx.scene.layout.StackPane`
- **Descripción**: Marquesina con texto desplazable continuamente de derecha a izquierda con velocidad regulable y pausa automática al pasar el ratón (`pauseOnHover`).

#### `JGauge` & `JGaugeCard`
- **Hereda de**: `javafx.scene.layout.StackPane`
- **Descripción**: Medidor industrial de aguja o arco radial (velocímetro, temperatura, presión, porcentaje) con zonas de colores configurables (`GaugeZone`).
- **Enums**: `GaugeType` (`RADIAL`, `ARC`, `LINEAR`), `GaugeStatus` (`NORMAL`, `WARNING`, `DANGER`).

#### `JLed`
- **Hereda de**: `javafx.scene.layout.Region`
- **Descripción**: Indicador LED de estado físico o de máquina.
- **Enums / Métodos**:
  - `LedColor`: `GREEN`, `RED`, `YELLOW`, `BLUE`, `ORANGE`, `WHITE`.
  - `LedShape`: `ROUND`, `SQUARE`.
  - `setOn(boolean on)`, `setBlinking(boolean blinking)`.

#### `JTowerLight`
- **Hereda de**: `javafx.scene.layout.VBox`
- **Descripción**: Baliza / torreta de señalización industrial (Andon) con módulos de colores apilados (Rojo, Amarillo, Verde, Azul). Cada módulo puede estar `OFF`, `ON` o `BLINKING`.

#### `JBattery`
- **Descripción**: Indicador gráfico del estado y porcentaje de batería, con colores adaptativos (verde > 50%, amarillo > 20%, rojo crítico < 20%) e icono de rayo de carga.

#### `JStatusBadge`
- **Hereda de**: `javafx.scene.layout.HBox`
- **Descripción**: Insignia con punto brillante (*status dot*) y opción de animación de pulso continuo.
- **Estados (`JStatusBadge.Status`)**: `ONLINE`, `OFFLINE`, `AWAY`, `PROCESSING`, `IDLE`, `SUCCESS`, `DANGER`, `WARNING`, `INFO`, `PURPLE`.
- **Ejemplo**:
  ```java
  JStatusBadge badge = new JStatusBadge("En línea", JStatusBadge.Status.ONLINE, true); // true = pulso animado
  ```

#### `JRibbon`
- **Hereda de**: `javafx.scene.control.Label`
- **Descripción**: Lazo decorativo inclinado para colocar en la esquina de una tarjeta o imagen.
- **Posición / Variantes**: `Position.TOP_RIGHT`, `Position.TOP_LEFT`, `Variant.PRIMARY`, `Variant.SUCCESS`, `Variant.DANGER`, etc.

#### `JAvatar`, `JBadge` & `JChip`
- **`JAvatar`**: Avatar circular con imagen o iniciales (`new JAvatar("Carlos Gómez")`).
- **`JBadge`**: Etiqueta redondeada para contadores (`new JBadge("99+")`).
- **`JChip`**: Cápsula con texto y botón 'x' para remover (`new JChip("JavaFX", true, chip -> eliminar(chip))`).

#### `JTimeline`
- **Hereda de**: `javafx.scene.layout.VBox`
- **Descripción**: Línea de tiempo visual vertical para listas de eventos o seguimiento de estados (historial de pedidos, auditorías).

---

### 5. Navegación y Estructura de Aplicación

#### `JSidebar`, `JSidebarItem` & `JSidebarSubmenu`
- **Descripción**: Sistema completo de barra lateral de navegación para paneles administrativos.
- **Características**:
  - Soporta colapso a solo iconos (`CollapseMode.COMPACT`) o a ancho cero (`CollapseMode.HIDDEN`).
  - Variantes de color: `SidebarVariant.DEFAULT` (oscuro) y `SidebarVariant.LIGHT` (blanco).
- **Ejemplo**:
  ```java
  JSidebar sidebar = new JSidebar();
  JSidebarItem itemDashboard = new JSidebarItem("Dashboard", JIcon.HOME.view());
  JSidebarItem itemUsers = new JSidebarItem("Usuarios", JIcon.PERSON.view());
  
  JSidebarSubmenu settingsSubmenu = new JSidebarSubmenu("Configuración", JIcon.SETTINGS.view());
  settingsSubmenu.addSubItem(new JSidebarItem("General"));
  settingsSubmenu.addSubItem(new JSidebarItem("Seguridad"));
  
  sidebar.getItems().addAll(itemDashboard, itemUsers, settingsSubmenu);
  ```

#### `JHeader`
- **Hereda de**: `javafx.scene.layout.HBox`
- **Descripción**: Barra superior para colocar el botón de toggle del sidebar, buscador global, notificaciones y perfil de usuario.

#### `JTabs` & `JTab`
- **Descripción**: Contenedor de pestañas moderno con transición suave que sustituye a `TabPane`.

#### `JDrawer`
- **Hereda de**: `javafx.scene.layout.StackPane`
- **Descripción**: Panel lateral deslizable (offcanvas/drawer) que entra desde el borde de la pantalla (ideal para filtros avanzados o paneles de detalle).

#### `JAccordion` & `JAccordionPane`
- **Descripción**: Acordeón estilizado que reemplaza a `Accordion`. Permite configurar si se puede abrir un solo panel a la vez o múltiples simultáneamente.

#### `JTitleBar`
- **Hereda de**: `javafx.scene.layout.HBox`
- **Descripción**: Barra de título para aplicaciones con ventanas sin bordes estándar (`StageStyle.UNDECORATED`), integrando botones de minimizar, maximizar y cerrar con arrastre de ventana fluido.

#### `JBreadcrumb`
- **Hereda de**: `javafx.scene.layout.HBox`
- **Descripción**: Miga de pan de navegación jerárquica con separadores automáticos (`Inicio > Ventas > Factura #102`).

#### `JPagination`
- **Hereda de**: `javafx.scene.layout.HBox`
- **Descripción**: Control de paginación para avanzar, retroceder y saltar de página en tablas o catálogos.

#### `JStepper`
- **Hereda de**: `javafx.scene.layout.HBox`
- **Descripción**: Indicador de pasos visuales (Paso 1: Datos -> Paso 2: Pago -> Paso 3: Confirmación) para formularios wizard.

---

### 6. Overlays y Popovers

#### `JPopover`
- **Hereda de**: `javafx.stage.Popup`
- **Descripción**: Ventana emergente flotante con flecha indicadora anclada dinámicamente a cualquier nodo emisor.

#### `JDropdown`
- **Descripción**: Menú contextual o dropdown moderno estilizado para opciones contextuales.

#### `JTooltip`
- **Hereda de**: `javafx.scene.control.Tooltip`
- **Descripción**: Tooltip flotante moderno con tipografía clara y bordes redondeados.

---

### 7. Especiales, Animaciones y Utilidades

#### `JIcon` (Enum SVG)
- **Descripción**: Catálogo vectorial con más de 100 iconos SVG integrados (Navigation, Actions, Files, Social, etc.).
- **Métodos**:
  - `JIcon.HOME.view()`: Devuelve un `Node` JavaFX con el icono renderizado.
  - `JIcon.SEARCH.view("#3b82f6")`: Devuelve el icono con color hexadecimal personalizado.
- **Iconos comunes**: `ARROW_BACK`, `ARROW_FORWARD`, `CHEVRON_RIGHT`, `MENU`, `CLOSE`, `HOME`, `SEARCH`, `SETTINGS`, `DONE`, `DELETE`, `EDIT`, `ADD`, `REFRESH`, `CHECK`, `CHECK_CIRCLE`, `LOCK`, `PERSON`, `BELL`, etc.

#### `JAnimation` (Motor de Animación Declarativo)
- **Ubicación**: `com.jjarroyo.animation.JAnimation`
- **Descripción**: Sistema fluido de animaciones para cualquier `Node` de JavaFX.
- **Efectos disponibles**:
  - `fadeIn(Node)`, `fadeOut(Node)`
  - `slideInUp(Node)`, `slideInDown(Node)`, `slideInLeft(Node)`, `slideInRight(Node)`
  - `zoomIn(Node)`, `zoomOut(Node)`
  - `pulse(Node)`, `shake(Node)`, `bounce(Node)`
- **Ejemplo**:
  ```java
  // Animación fluida con callback al finalizar:
  JAnimation.slideInUp(miCard)
      .delay(Duration.millis(150))
      .onFinished(() -> System.out.println("Card visible"))
      .play();
  ```

#### `JAnimatedSwitcher`
- **Hereda de**: `javafx.scene.layout.StackPane`
- **Descripción**: Contenedor que anima automáticamente la transición cuando cambias su contenido (`setContent(newNode)`).
- **Animaciones (`SwitchAnimation`)**: `FADE`, `SLIDE_LEFT`, `SLIDE_RIGHT`, `SLIDE_UP`, `SLIDE_DOWN`, `ZOOM`.
- **Ejemplo**:
  ```java
  JAnimatedSwitcher switcher = new JAnimatedSwitcher();
  switcher.setSwitchAnimation(JAnimatedSwitcher.SwitchAnimation.SLIDE_LEFT);
  switcher.setContent(vistaNueva); // Transición animada automática
  ```

#### `JSqlEditor`
- **Hereda de**: `javafx.scene.layout.VBox`
- **Descripción**: Editor de consultas SQL con barra de acciones (Ejecutar, Limpiar, Formatear).

#### `JDesignCanvas` & `JZoom`
- **`JDesignCanvas`**: Lienzo visual para drag and drop y construcción de diagramas o layouts.
- **`JZoom`**: Contenedor interactivo que permite acercar, alejar y arrastrar el contenido visual (pan & zoom).

---

## 💡 4. Plantilla de Pantalla Ejemplo Completa

Si una IA necesita construir una pantalla de dashboard o formulario usando FxStyle, este es el patrón idiomático:

```java
import com.jjarroyo.FxStyle;
import com.jjarroyo.components.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DashboardApp extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Contenedor raíz (StackPane necesario para modales)
        StackPane root = new StackPane();
        FxStyle.setModalContainer(root);

        // 2. Layout principal con Sidebar y Contenido
        BorderPane layout = new BorderPane();
        root.getChildren().add(layout);

        // 3. Sidebar lateral
        JSidebar sidebar = new JSidebar();
        sidebar.getItems().addAll(
            new JSidebarItem("Dashboard", JIcon.HOME.view()),
            new JSidebarItem("Usuarios", JIcon.PERSON.view()),
            new JSidebarItem("Configuración", JIcon.SETTINGS.view())
        );
        layout.setLeft(sidebar);

        // 4. Encabezado superior
        JHeader header = new JHeader();
        layout.setTop(header);

        // 5. Contenido central con KPIs y Tabla
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));

        // KPIs en una fila
        HBox kpis = new HBox(16);
        JStatCard kpi1 = new JStatCard();
        kpi1.setTitle("Ventas Totales");
        kpi1.setValue("$124,500");
        kpi1.setTrend("+8.4% este mes", true);

        JStatCard kpi2 = new JStatCard();
        kpi2.setTitle("Usuarios Activos");
        kpi2.setValue("1,420");
        kpi2.setTrend("+3.1% hoy", true);

        kpis.getChildren().addAll(kpi1, kpi2);

        // Tabla de datos
        JTable<User> table = new JTable<>();
        table.setSearchable(true);
        table.setCheckable(true);
        table.addColumn("ID", "id");
        table.addColumn("Nombre", "name");
        table.addColumn("Estado", "status", u -> new JStatusBadge(u.getStatus(), JStatusBadge.Status.ONLINE, true));

        JCard cardTabla = new JCard("Usuarios Registrados", table);
        cardTabla.setToolbar(new JButton("Nuevo Usuario", JIcon.ADD));

        content.getChildren().addAll(kpis, cardTabla);
        layout.setCenter(content);

        // 6. Configurar Scene e inicializar tema FxStyle
        Scene scene = new Scene(root, 1200, 800);
        FxStyle.init(scene);

        stage.setScene(scene);
        stage.setTitle("FxStyle Dashboard");
        stage.show();
    }
}
```

---

## 📌 Resumen para el Prompter / Agente

Cuando te pidan crear o editar pantallas en este repositorio:
1. Revisa la tabla del apartado **2. Tabla Cheat-Sheet de Equivalencias**.
2. Usa directamente la clase `J*` adecuada de `com.jjarroyo.components.*`.
3. Nunca inventes estilos inline en Java (`setStyle(...)`) cuando existan clases CSS o métodos del componente (`addClass(...)`, `btn-primary`, etc.).
4. Para modales, recuerda que se muestran con `modal.open()` sobre el `FxStyle.getModalContainer()`.
