#!/bin/bash
# build_all.sh - Compila y empaqueta todos los módulos del sistema eventosUy en Linux
set -e
BASE_DIR="$(dirname "$(readlink -f "$0")")"

log() {
  echo "[LOG] $1"
}
error() {
  echo "[ERROR] $1" >&2
}

log "Iniciando compilación de todos los módulos"

servidorCentral() {
  log "Entrando a Tarea3/ServidorCentral"
  cd "$BASE_DIR/Tarea3/ServidorCentral"
  log "Ejecutando mvn clean package en ServidorCentral"
  mvn clean package
  cd "$BASE_DIR"
  JAR_PATH="$BASE_DIR/Tarea3/ServidorCentral/target/ServidorCentral-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
  log "Verificando existencia del JAR en $JAR_PATH"
  if [ -f "$JAR_PATH" ]; then
    log "El JAR existe, copiando..."
    cp "$JAR_PATH" servidor.jar
  else
    error "El JAR NO existe: $JAR_PATH"
  fi
}

clienteWeb() {
  log "Entrando a Tarea3/ClienteWeb"
  cd "$BASE_DIR/Tarea3/ClienteWeb"
  log "Ejecutando mvn clean package en ClienteWeb"
  mvn clean package
  cd "$BASE_DIR"
  log "Copiando WAR generado de ClienteWeb"
  cp "$BASE_DIR/Tarea3/ClienteWeb/target/ClienteWeb.war" web.war
}

clienteMobile() {
  log "Entrando a Tarea3/ClienteMobile"
  cd "$BASE_DIR/Tarea3/ClienteMobile"
  log "Ejecutando mvn clean package en ClienteMobile"
  mvn clean package
  cd "$BASE_DIR"
  log "Copiando WAR generado de ClienteMobile"
  cp "$BASE_DIR/Tarea3/ClienteMobile/target/ClienteMobile.war" movil.war
}

copiarConfig() {
  CONFIG_SRC="$BASE_DIR/config.properties"
  CONFIG_DEST="$HOME/.eventosUy"
  if [ ! -d "$CONFIG_DEST" ]; then
    mkdir -p "$CONFIG_DEST"
  fi
  log "Copiando config.properties a $CONFIG_DEST"
  cp "$CONFIG_SRC" "$CONFIG_DEST/config.properties"
}

servidorCentral
log "ServidorCentral compilado"
clienteWeb
log "ClienteWeb compilado"
clienteMobile
log "ClienteMobile compilado"
copiarConfig
log "config.properties copiado a $HOME/.eventosUy"
log "Script finalizado"
