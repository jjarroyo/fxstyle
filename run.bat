@echo off
set JAVAFX_PATH=E:\Java\javafx-sdk-26\lib

@echo off
set "MAVEN_HOME=%~dp0maven\apache-maven-3.9.12"
set "PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin;%PATH%"

echo ==========================================
echo Building and Running FxStyle Demo App v2.0.0
echo ==========================================

rem Build the entire project (Library + Demo)
call mvn clean install
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ==========================================                        
echo Running Demo Application...
echo ==========================================

rem Run the Demo JAR
java -jar fxstyle-demo/target/fxstyle-demo-2.0.0.jar
if %ERRORLEVEL% NEQ 0 (
    echo Application execution failed!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Application finished.
pause
