# FxStyle - JavaFX Design System v2.2.0

Sistema de diseno moderno para JavaFX, inspirado en conceptos de Tailwind CSS y orientado a crear interfaces consistentes y reutilizables.

Documentacion y tutoriales: visita el blog oficial en https://javafx-blog-page.vercel.app/

## Requisitos previos

Antes de ejecutar el proyecto en Visual Studio Code, asegurate de tener:

1. Java SDK instalado (recomendado JDK 21 o superior).
2. JAVA_HOME configurado apuntando a tu JDK.
3. JavaFX SDK 26 descargado y descomprimido.
4. Maven descargado y ubicado dentro de la raiz del proyecto.

## Preparar entorno (Windows)

### 1) Instalar Java SDK

- Descarga e instala un JDK (por ejemplo, JDK 21).
- Configura la variable JAVA_HOME con la ruta de instalacion del JDK.
- Agrega %JAVA_HOME%\bin al PATH del sistema.

Ejemplo:

- JAVA_HOME=E:\Java\jdk-21

### 2) Descargar JavaFX SDK 26

- Descarga JavaFX SDK 26 desde la pagina oficial:
  https://gluonhq.com/products/javafx/
- Descomprime el archivo y deja la carpeta en una ruta como:
  E:\Java\javafx-sdk-26

Nota: el script run.bat usa esta ruta:

- E:\Java\javafx-sdk-26\lib

Si tu ruta es diferente, actualiza la variable JAVAFX_PATH dentro de run.bat.

### 3) Descargar Maven y colocarlo en la raiz del proyecto

- Descarga Apache Maven desde:
  https://maven.apache.org/download.cgi
- Descomprime Maven dentro de la carpeta raiz del proyecto FxStyle, con esta estructura:

- fxstyle/
    - maven/
        - apache-maven-3.9.12/

El script run.bat esta preparado para usar:

- maven\apache-maven-3.9.12

Si usas otra version, ajusta esa ruta en run.bat.

## Ejecutar proyecto en Visual Studio Code

1. Abre la carpeta fxstyle en Visual Studio Code.
2. Abre una terminal en la raiz del proyecto.
3. Ejecuta:

- run.bat

Este script hace lo siguiente:

1. Ejecuta mvn clean install para compilar libreria y demo.
2. Lanza la aplicacion demo:
   fxstyle-demo/target/fxstyle-demo-2.2.0.jar

## Ejecucion manual (opcional)

Si prefieres ejecutar manualmente:

1. Compilar:

- mvn clean install

2. Ejecutar demo:

- java -jar fxstyle-demo/target/fxstyle-demo-2.2.0.jar

## 🚀 Cómo implementar FxStyle en tu proyecto

Para integrar **FxStyle** en tu aplicación JavaFX en un proyecto real, sigue estos pasos:

### 1. Agregar la dependencia Maven

Agrega el módulo a tu `pom.xml`:

```xml
<dependency>
    <groupId>com.jjarroyo</groupId>
    <artifactId>fxstyle-library</artifactId>
    <version>2.2.0</version>
</dependency>
```

### 2. Inicializar FxStyle en la clase principal (`Application`)

Debes invocar `FxStyle.init(scene)` al momento de crear la `Scene`. Esto cargará automáticamente el tema, variables CSS y estilos globales necesarios para que todos los componentes se rendericen correctamente.

Si usas componentes que requieren un contenedor global para capas/superposiciones (como `JModal` o `JConfirmDialog`), opcionalmente puedes registrar el contenedor principal (`StackPane`) usando `FxStyle.setModalContainer(...)`.

#### Ejemplo de integración (`App.java`):

```java
package com.miempresa.app;

import com.jjarroyo.FxStyle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 1280, 800);
        
        // (Opcional) Cargar hoja de estilos propia de tu app
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
       
        // Inicializar el sistema de diseño FxStyle (Carga CSS base y estilos globales)
        FxStyle.init(scene); 
       
        // (Opcional) Si usas JModal / JConfirmDialog, establece el StackPane contenedor raíz:
        // FxStyle.setModalContainer(rootStackPane);

        primaryStage.setTitle("Mi Aplicación JavaFX");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

## Catálogo de Componentes

Navega a la documentación de cada componente y visualiza cómo lucen de forma estructurada.

### 🔘 Botones y Acciones
| Componente | Documentación | Preview |
|------------|---------------|---------|
| **JButton** | [📄 Docs](docs/components/JButton.html) | [🖼️ Ver Preview](docs/images/buttons.png) |
| **JFloatingButton** | [📄 Docs](docs/components/JFloatingButton.html) | - |
| **JSwitch** | [📄 Docs](docs/components/JSwitch.html) | - |

### 📝 Entradas de Datos y Formularios
| Componente | Documentación | Preview |
|------------|---------------|---------|
| **JInput** / **JInputGroup** | [📄 Docs](docs/components/JInput.html) | [🖼️ Ver Preview](docs/images/inputs.png) |
| **JSearchInput** | [📄 Docs](docs/components/JSearchInput.html) | [🖼️ Ver Preview](docs/images/searchinputs.png) |
| **JSelect** | [📄 Docs](docs/components/JSelect.html) | [🖼️ Ver Preview](docs/images/selects.png) |
| **JCheckBox** & **JRadioButton**| [📄 Docs](docs/components/JCheckBox.html) | [🖼️ Ver Preview](docs/images/checkandradio.png) |
| **JPasswordInput** | [📄 Docs](docs/components/JPasswordInput.html) | - |
| **JNumberInput** | [📄 Docs](docs/components/JNumberInput.html) | - |
| **JSlider** | [📄 Docs](docs/components/JSlider.html) | [🖼️ Ver Preview](docs/images/sliders.png) |
| **JDatePicker** / **JTimePicker**| [📄 Docs](docs/components/JDatePicker.html) | - |
| **JCalendar** | [📄 Docs](docs/components/JCalendar.html) | - |
| **JTextArea** | [📄 Docs](docs/components/JTextArea.html) | - |
| **JRating** | [📄 Docs](docs/components/JRating.html) | - |
| **JTagInput** | [📄 Docs](docs/components/JTagInput.html) | - |

### 🔔 Feedback y Comunicación
| Componente | Documentación | Preview |
|------------|---------------|---------|
| **JAlert** | [📄 Docs](docs/components/JAlert.html) | [🖼️ Ver Preview](docs/images/alerts.png) |
| **JModal** | [📄 Docs](docs/components/JModal.html) | [🖼️ Ver Preview](docs/images/modals.png) |
| **JConfirmDialog** | [📄 Docs](docs/components/JConfirmDialog.html) | [🖼️ Ver Preview](docs/images/modalalert.png) |
| **JNotification** & **JToast** | [📄 Docs](docs/components/JNotification.html) | - |
| **JProgressBar** & **JCircular**| [📄 Docs](docs/components/JProgressBar.html) | [🖼️ Ver Preview](docs/images/progress.png) |
| **JSkeleton** | [📄 Docs](docs/components/JSkeleton.html) | [🖼️ Ver Preview](docs/images/skeletons.png) |

### 📊 Despliegue de Datos
| Componente | Documentación | Preview |
|------------|---------------|---------|
| **JTable** | [📄 Docs](docs/components/JTable.html) | [🖼️ Ver Preview](docs/images/tables.png) |
| **JTreeView** | [📄 Docs](docs/components/JTreeView.html) | [🖼️ Ver Preview](docs/images/treeview.png) |
| **JCard** & **JStatCard** | [📄 Docs](docs/components/JCard.html) | [🖼️ Ver Preview](docs/images/cards.png) |
| **JAvatar** | [📄 Docs](docs/components/JAvatar.html) | [🖼️ Ver Preview](docs/images/avatars.png) |
| **JBadge** & **JChip** | [📄 Docs](docs/components/JBadge.html) | - |
| **JTimeline** | [📄 Docs](docs/components/JTimeline.html) | - |
| **JChart** | [📄 Docs](docs/components/JChart.html) | - |
| **Typography** (Label/Paragraph)| [📄 Docs](docs/components/Typography.html) | - |

### 🧭 Navegación y Contenedores
| Componente | Documentación | Preview |
|------------|---------------|---------|
| **JSidebar** & **JHeader** | [📄 Docs](docs/components/JSidebar.html) | [🖼️ Ver Preview](docs/images/dashboard.png) |
| **JTabs** | [📄 Docs](docs/components/JTabs.html) | [🖼️ Ver Preview](docs/images/tabs.png) |
| **JDrawer** | [📄 Docs](docs/components/JDrawer.html) | [🖼️ Ver Preview](docs/images/drawers.png) |
| **JAccordion** | [📄 Docs](docs/components/JAccordion.html) | [🖼️ Ver Preview](docs/images/accordions.png) |
| **JTitleBar** | [📄 Docs](docs/components/JTitleBar.html) | - |
| **JBreadcrumb** & **JPagination**| [📄 Docs](docs/components/JBreadcrumb.html) | - |
| **JStepper** | [📄 Docs](docs/components/JStepper.html) | - |

### 🕹️ Overlays & Popovers
| Componente | Documentación | Preview |
|------------|---------------|---------|
| **JPopover** | [📄 Docs](docs/components/JPopover.html) | [🖼️ Ver Preview](docs/images/popovers.png) |
| **JDropdown** | [📄 Docs](docs/components/JDropdown.html) | - |

### ⚙️ Especiales / Helpers
| Componente | Documentación | Preview |
|------------|---------------|---------|
| **JFile** | [📄 Docs](docs/components/JFile.html) | [🖼️ Ver Preview](docs/images/files.png) |
| **JSqlEditor** | [📄 Docs](docs/components/JSqlEditor.html) | - |
| **JDesignCanvas** | [📄 Docs](docs/components/JDesignCanvas.html) | - |
| **JIcon** | [📄 Docs](docs/components/JIcon.html) | - |
| **Colores y Utilidades CSS** | [📄 Docs](docs/components/Colors.html) | - |

## Problemas comunes

1. Error "mvn no se reconoce": revisa que Maven este en fxstyle/maven/apache-maven-3.9.12 o corrige run.bat.
2. Error con Java: valida JAVA_HOME y que apunte a un JDK (no solo JRE).
3. Error JavaFX: valida que exista E:\Java\javafx-sdk-26\lib o actualiza JAVAFX_PATH.

## Licencia

[MIT](LICENSE)
