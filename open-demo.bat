@echo off
setlocal

cd /d "%~dp0"

echo ===================================================
echo   FxStyle - Abrir Demo (Lanzamiento Directo)
echo ===================================================

rem 1. Configurar JAVA_HOME si no esta en el entorno
if not defined JAVA_HOME (
    if exist "C:\Program Files\Java\jdk-26" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-26"
    ) else if exist "C:\Program Files\Java\latest" (
        set "JAVA_HOME=C:\Program Files\Java\latest"
    )
)

if defined JAVA_HOME (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
)

rem 2. Verificar si el JAR de la demo ya esta compilado
set "JAR_FILE=fxstyle-demo\target\fxstyle-demo-2.3.0.jar"

if not exist "%JAR_FILE%" (
    echo.
    echo [AVISO] No se encontro el archivo de la aplicacion:
    echo   "%JAR_FILE%"
    echo.
    echo Parece que el proyecto aun no ha sido compilado.
    echo Ejecuta primero "run.bat" para generar el ejecutable.
    echo.
    pause
    exit /b 1
)

echo [OK] Iniciando aplicacion demo...
echo.

java -jar "%JAR_FILE%"
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Ocurrio un problema al ejecutar la aplicacion demo.
    pause
    exit /b %ERRORLEVEL%
)
