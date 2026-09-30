@echo off
setlocal enabledelayedexpansion

cd /d "%~dp0"

echo ===================================================
echo   FxStyle - Build & Launch Demo v2.3.0
echo ===================================================

rem 1. Detectar o configurar JAVA_HOME
if not defined JAVA_HOME (
    if exist "C:\Program Files\Java\jdk-26" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-26"
    ) else if exist "C:\Program Files\Java\latest" (
        set "JAVA_HOME=C:\Program Files\Java\latest"
    )
)

if defined JAVA_HOME (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
    echo [JAVA] Usando JAVA_HOME: %JAVA_HOME%
)

rem 2. Detectar Maven en el sistema
set "MVN_CMD="

if exist "%~dp0maven\apache-maven-3.9.12\bin\mvn.cmd" (
    set "MVN_CMD=%~dp0maven\apache-maven-3.9.12\bin\mvn.cmd"
) else if exist "C:\Users\JORGE\Desktop\Java\maven-3.9.15\bin\mvn.cmd" (
    set "MVN_CMD=C:\Users\JORGE\Desktop\Java\maven-3.9.15\bin\mvn.cmd"
) else (
    where mvn >nul 2>nul
    if !ERRORLEVEL! EQU 0 (
        set "MVN_CMD=mvn"
    )
)

if not defined MVN_CMD (
    echo.
    echo [ERROR] No se encontro Maven en ninguna de las siguientes rutas:
    echo   - %~dp0maven\apache-maven-3.9.12\bin\mvn.cmd
    echo   - C:\Users\JORGE\Desktop\Java\maven-3.9.15\bin\mvn.cmd
    echo   - PATH del sistema
    echo.
    echo Por favor verifica la ubicacion de tu carpeta Maven.
    pause
    exit /b 1
)

echo [MAVEN] Usando: "%MVN_CMD%"
echo.
echo ===================================================
echo   Compilando libreria y demo...
echo ===================================================

call "%MVN_CMD%" clean install -DskipTests -Dmaven.javadoc.skip=true
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Fallo la compilacion con Maven.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ===================================================
echo   Iniciando FxStyle Demo Application...
echo ===================================================

java -jar fxstyle-demo\target\fxstyle-demo-2.3.0.jar
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Error al ejecutar la aplicacion.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Aplicacion finalizada.
pause
