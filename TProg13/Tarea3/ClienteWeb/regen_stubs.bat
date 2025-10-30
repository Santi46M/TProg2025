@echo off
REM ===========================
REM  Regenerar Stubs con un solo jar (JAX-WS Tools 4.0.0)
REM ===========================

setlocal

set JAVA_BIN=java
set JAXWS_JAR=lib\jaxws-tools-4.0.0.jar
set BASE_URL=http://localhost:8090
set SRC=src/main/java
set PKG=publicadores

echo ==============================
echo 🧹 Eliminando stubs anteriores...
echo ==============================
if exist "%SRC%\%PKG%" rd /s /q "%SRC%\%PKG%"

echo ==============================
echo Generando stubs de Web Services...
echo ==============================

%JAVA_BIN% -cp "%JAXWS_JAR%" com.sun.tools.ws.WsImport -keep -extension -s %SRC% -p %PKG% %BASE_URL%/publicadorUsuario?wsdl
%JAVA_BIN% -cp "%JAXWS_JAR%" com.sun.tools.ws.WsImport -keep -extension -s %SRC% -p %PKG% %BASE_URL%/publicadorEvento?wsdl

echo.
echo ✅ Generación completada.
echo Revisar carpeta: %SRC%\%PKG%
echo ==============================

pause
