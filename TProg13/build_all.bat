@echo off
rem build_all.bat - Compila y empaqueta todos los módulos del sistema eventosUy
set BASE_DIR=%~dp0
rem Elimina comillas finales si existen
if "%BASE_DIR:~-1%"=="\" set BASE_DIR=%BASE_DIR:~0,-1%

rem Compila todo automáticamente sin menú ni argumentos
echo [LOG] Iniciando compilación de todos los módulos
call :servidorCentral
  echo [LOG] ServidorCentral compilado
call :clienteWeb
  echo [LOG] ClienteWeb compilado
call :clienteMobile
  echo [LOG] ClienteMobile compilado
call :copiarConfig
  echo [LOG] config.properties copiado a %USERPROFILE%\.eventosUy
call :finalizar
  echo [LOG] Script finalizado
exit /b

:servidorCentral
  echo [LOG] Entrando a Tarea3\ServidorCentral
  cd Tarea3\ServidorCentral
  echo [LOG] Ejecutando mvn clean package en ServidorCentral
  call mvn clean package
  echo [LOG] Volviendo al directorio base
  cd "%BASE_DIR%"
  echo [LOG] Directorio actual: %CD%
  set JAR_PATH=%BASE_DIR%\Tarea3\ServidorCentral\target\ServidorCentral-0.0.1-SNAPSHOT-jar-with-dependencies.jar
  echo [LOG] Verificando existencia del JAR en %JAR_PATH%
  if exist "%JAR_PATH%" (
    echo [LOG] El JAR existe, copiando...
    copy /Y "%JAR_PATH%" servidor.jar
  ) else (
    echo [ERROR] El JAR NO existe: %JAR_PATH%
  )
  exit /b

:clienteWeb
  echo [LOG] Entrando a Tarea3\ClienteWeb
  cd Tarea3\ClienteWeb
  echo [LOG] Ejecutando mvn clean package en ClienteWeb
  call mvn clean package
  echo [LOG] Volviendo al directorio base
  cd "%BASE_DIR%"
  echo [LOG] Copiando WAR generado de ClienteWeb
  copy /Y Tarea3\ClienteWeb\target\ClienteWeb.war web.war
  exit /b

:clienteMobile
  echo [LOG] Entrando a Tarea3\ClienteMobile
  cd Tarea3\ClienteMobile
  echo [LOG] Ejecutando mvn clean package en ClienteMobile
  call mvn clean package
  echo [LOG] Volviendo al directorio base
  cd "%BASE_DIR%"
  echo [LOG] Copiando WAR generado de ClienteMobile
  copy /Y Tarea3\ClienteMobile\target\ClienteMobile.war movil.war
  exit /b

:copiarConfig
  set CONFIG_SRC=%BASE_DIR%\config.properties
  set CONFIG_DEST=%USERPROFILE%\.eventosUy
  if not exist "%CONFIG_DEST%" (
    mkdir "%CONFIG_DEST%"
  )
  echo [LOG] Copiando config.properties a %CONFIG_DEST%
  copy /Y "%CONFIG_SRC%" "%CONFIG_DEST%\config.properties"
  exit /b

:finalizar

  exit /b