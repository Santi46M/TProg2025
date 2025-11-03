@echo off
REM ===== CONFIG =====
set "BASE_URL=http://192.168.1.91:8090"
set "PKG=publicadores"
set "SRC=%CD%\src\main\java"
set "JAVA_BIN=java"

REM ===== Build classpath con Maven (incluye jaxws-tools, jaxws-rt, JAXB, etc.) =====
for /f "usebackq tokens=*" %%i in (`
  mvn -q dependency:build-classpath ^
     -DincludeArtifactIds=jaxws-tools,jaxws-rt,jakarta.xml.ws-api,jakarta.xml.bind-api,jaxb-runtime,jakarta.activation-api,istack-commons-runtime,stax-ex,woodstox-core ^
     -DincludeScope=runtime ^
     -Dmdep.outputAbsoluteArtifactFilename=true ^
     -Dmdep.pathSeparator=";"
`) do set "JAXWS_CP=%%i"

echo.
echo CLASSPATH listo:
echo %JAXWS_CP%
echo.

REM ===== Ejecutar wsimport (JAX-WS 4.x) para los 3 WSDL =====
%JAVA_BIN% -cp "%JAXWS_CP%" com.sun.tools.ws.WsImport -keep -extension -s "%SRC%" -p %PKG% %BASE_URL%/publicadorUsuario?wsdl
if errorlevel 1 goto :err

%JAVA_BIN% -cp "%JAXWS_CP%" com.sun.tools.ws.WsImport -keep -extension -s "%SRC%" -p %PKG% %BASE_URL%/publicadorEvento?wsdl
if errorlevel 1 goto :err

%JAVA_BIN% -cp "%JAXWS_CP%" com.sun.tools.ws.WsImport -keep -extension -s "%SRC%" -p %PKG% %BASE_URL%/publicadorEstadisticas?wsdl
if errorlevel 1 goto :err

echo.
echo ==== OK: Stubs generados en %SRC%\%PKG% ====
goto :eof

:err
echo.
echo **** FALLO wsimport (revisar URL del WSDL, red, o dependencias) ****
exit /b 1
