@echo off
REM ===========================
REM  Regenerar Stubs de ClienteWeb
REM ===========================

setlocal

set JAR_PATH=lib\jaxws-tools-4.0.0.jar
set JAVA_BIN="C:\Program Files\Java\jdk-21\bin\java.exe"
set BASE_URL=http://localhost:8090
set SRC=src/main/java
set PKG=publicadores

echo ==============================
echo Generando stubs de Web Services...
echo ==============================

%JAVA_BIN% -cp "%JAR_PATH%" com.sun.tools.ws.WsImport -keep -extension -s %SRC% -p %PKG% %BASE_URL%/publicadorUsuario?wsdl
%JAVA_BIN% -cp "%JAR_PATH%" com.sun.tools.ws.WsImport -keep -extension -s %SRC% -p %PKG% %BASE_URL%/publicadorEvento?wsdl

echo.
echo ✅ Generación completada.
echo Revisar carpeta: %SRC%\%PKG%
echo ==============================

pause