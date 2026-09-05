# AGENTS.md — FxStyle

Este archivo es la entrada rápida para cualquier IA que trabaje en este proyecto. No hace falta revisar todo el repositorio para entender la librería.

## 1) Resumen ejecutivo

FxStyle es un design system para JavaFX inspirado en conceptos de Tailwind/CSS moderno. Está organizado como proyecto Maven multi-módulo:

- `fxstyle-library`: librería principal con componentes JavaFX y estilos CSS.
- `fxstyle-demo`: aplicación de demostración para testear y documentar los componentes.
- `pom.xml` raíz: define módulos, versiones y dependencias.

La librería principal está bajo:

- `fxstyle-library/src/main/java/com/jjarroyo/FxStyle.java`
- `fxstyle-library/src/main/java/com/jjarroyo/components/`

La aplicación demo usa la librería para exponer bloques visuales por categoría y navegación lateral.

## 2) Cómo se organiza la librería

### Punto de entrada principal

`FxStyle.init(Scene scene)`:
- carga el stylesheet global `fxstyle.css`
- evita duplicar la hoja de estilos
- sirve como bootstrap del sistema visual

`FxStyle.setModalContainer(StackPane container)`: registra el contenedor para modales globales.

### Patrón conceptual

La librería sigue este patrón:

- cada componente es una clase JavaFX que extiende un control base (`Button`, `TextField`, `Pane`, etc.)
- cada componente aporta estilos CSS con clases tipo `btn-*`, `j-*`, `text-*`, etc.
- la demo actúa como catálogo y validación visual de componentes

### Estructura de paquetes

- `com.jjarroyo.FxStyle` — inicialización global
- `com.jjarroyo.components.*` — componentes de UI
- `com.jjarroyo.demo.views.*` — vistas de ejemplo y documentación visual
- `com.jjarroyo.demo.layout.*` — layout principal de navegación del demo

## 3) Estado real de componentes

La librería ya tiene una base muy amplia. La demo documenta estas categorías:

### A. Botones y acciones
- `JButton`
- `JFloatingButton`
- `JSwitch`

### B. Entradas de datos y formularios
- `JInput`
- `JInputGroup`
- `JSearchInput`
- `JSelect`
- `JCheckBox`
- `JRadioButton`
- `JPasswordInput`
- `JNumberInput`
- `JSlider`
- `JDatePicker`
- `JTimePicker`
- `JCalendar`
- `JTextArea`
- `JRating`
- `JTagInput`

### C. Feedback y comunicación
- `JAlert`
- `JModal`
- `JConfirmDialog`
- `JNotification`
- `JToast`
- `JProgressBar`
- `JCircularProgress`
- `JSkeleton`

### D. Datos y visualización
- `JTable`
- `JTreeView`
- `JCard`
- `JStatCard`
- `JAvatar`
- `JBadge`
- `JChip`
- `JTimeline`
- `JChart`
- `JLabel`
- `JParagraph`

### E. Navegación y contenedores
- `JSidebar`
- `JSidebarItem`
- `JSidebarSubmenu`
- `JHeader`
- `JTabs`
- `JTab`
- `JDrawer`
- `JAccordion`
- `JAccordionPane`
- `JTitleBar`
- `JBreadcrumb`
- `JPagination`
- `JStepper`

### F. Overlays y popovers
- `JPopover`
- `JDropdown`

### G. Especiales / utilidades
- `JFile`
- `JSqlEditor`
- `JDesignCanvas`
- `JIcon`
- `JM` (helper / util)
- `JZoom`
- `JList`
- `JTooltip`

## 4) Qué ya está bastante bien hecho

La librería tiene una base sólida en estas áreas:

- cobertura amplia de elementos de UI estándar
- diseño visual consistente con enfoque moderno
- documentación visible en demo y README
- estructura modular clara
- bootstrap global mediante `FxStyle.init(scene)`

## 5) Qué falta o está incompleto (priorizado)

### P0 — Necesario para madurez profesional

1. Sistema de temas centralizado
   - falta paleta centralizada y tokens reales (colores, spacing, radius, shadows)
   - hoy hay mucha lógica CSS dispersa en clases

2. Validaciones y accesibilidad
   - no parece haber enfoque claro de focus-visible, keyboard nav, contraste, alta accesibilidad
   - muchos componentes tienen estilo pero no una política de a11y robusta

3. Pruebas automáticas
   - no se observa suite de tests unitaria o visual
   - la demo sirve como validación manual, pero no reemplaza pruebas de regresión

4. Arquitectura de documentación por componente
   - existe demo, pero no hay una estructura de docs mantenible por API, props y ejemplos reutilizables

### P1 — Mejoras de producto

5. Componentes avanzados de gestión de datos
   - `JTable` y `JTreeView` parecen funcionar, pero faltan patrones más completos de edición, filtros y paginación

6. Formularios con validación real
   - `JInput`, `JNumberInput`, `JSelect`, `JDatePicker` podrían mejorar con mensajes de error, validación y estados

7. Componentes de notificación y overlays más robustos
   - `JNotification`, `JToast`, `JModal`, `JPopover` están presentes, pero todavía parecen más gráficos que sistemas de alto nivel

8. Estándar de API consistente
   - conviene normalizar nombre de setters / factories / estilo / iconos / disabled / loading

### P2 — Mejoras de experiencia

9. Más componentes de negocio / dashboard
   - `JStatCard`, `JChart`, `JTimeline` están buenos, pero aún faltan layouts de dashboard más complejos

10. Soporte de temas claro (light/dark)
   - la demo parece tener interacción, pero convendría tener sistema de tema real y no solo clases sueltas

11. Componentes para administración / administración de contenido
   - filtros, listas, editor SQL, drag & drop, file uploader avanzado

12. Módulos de integración con backend
   - no hay una capa de servicios o binding de data model que haga la librería más productiva para apps reales

### P3 — Extras / evolución

13. Soporte de composición por FXML/scene graph más limpia
   - actualmente parece estar orientada a instanciación imperativa

14. Versionado de estilos y tokens exportables
   - útil si la librería se quiere reutilizar como sistema de diseño corporativo

15. Estética avanzada / microinteracciones
   - hover, focus, motion, elevation, animation system, etc.

## 6) Qué componentes parecen más valiosos para construir o pulir primero

### Mayor prioridad
1. `JInput` + `JNumberInput` + `JPasswordInput` + `JSelect` + `JDatePicker`
2. `JModal` + `JConfirmDialog` + `JNotification` + `JToast`
3. `JTable` + `JTreeView` + `JPagination`
4. `JSidebar` + `JHeader` + `JDrawer` + `JTabs`
5. `JChart` + `JStatCard`

### Prioridad media
6. `JTagInput` + `JSearchInput`
7. `JAccordion` + `JStepper` + `JBreadcrumb`
8. `JCalendar` + `JTimePicker` + `JTimeline`
9. `JDesignCanvas` + `JZoom`

### Prioridad baja / extras
10. `JSqlEditor` y utilidades de editor avanzado
11. `JFile` más completo
12. otros widgets decorativos y de demo

## 7) Recomendación práctica para la siguiente IA

Cuando una IA entre a este repo, debe asumir esto:

- la librería ya tiene una base sólida de componentes UI
- el valor real del proyecto está en la capa visual + diseño system de JavaFX
- no empezar rehaciendo el diseño desde cero
- priorizar: tokens, control de estados, accesibilidad, APIs consistentes, pruebas
- usar la demo como referencia visual y la librería como fuente de verdad

## 8) Orden recomendado de trabajo

### Fase 1: base que hace falta
- temas/tokens
- validación de formularios
- sistema de estados de componentes
- accesibilidad y focus

### Fase 2: componentes de negocio
- `JTable` / `JTreeView`
- `JChart` / `JStatCard`
- `JModal` / `JToast` / `JNotification`

### Fase 3: experiencia y productización
- navegación compleja
- layout dashboard
- documentación y demo avanzada
- test visual/automático

## 9) Vista rápida del proyecto

### Archivos clave
- `pom.xml` — módulos y dependencias
- `FxStyle.java` — inicializador global
- `README.md` — documentación base
- `fxstyle-demo/.../layout/MainLayout.java` — navegación demo
- `fxstyle-library/src/main/java/com/jjarroyo/components/` — componentes reales

## 10) Conclusión

Este repositorio ya no es una librería pequeña; es un sistema de diseño JavaFX con una base bastante extensa y ordenada. Lo que más falta no es “nuevos widgets” sino madurez: consistencia de API, tokens/tema, validación, accesibilidad, testing y componentes de flujo de negocio.

Si se quiere sacar mayor valor del proyecto, la mejor ruta es mejorar la base y luego enriquecer la capa de dashboard y formularios, no crear un conjunto de componentes totalmente nuevos sin coherencia.
