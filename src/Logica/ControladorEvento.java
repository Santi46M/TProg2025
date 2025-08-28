package Logica;

import java.util.List;
import usuario.Eventos;
import Logica.ManejadorEvento;

// Importa aquí los DTOs y modelos necesarios
// import ...

public class ControladorEvento {

    public void AltaEvento(String nombre, String desc, DTFecha fechaDeAlta, String sigla, DTCategorias categorias) {
        ManejadorEvento manejador = ManejadorEvento.getInstancia();
        if (manejador.existeEvento(sigla)) {
            throw new RuntimeException("Ya existe un evento con la sigla: " + sigla);
        }
        Eventos nuevoEvento = new Eventos(nombre, sigla, desc, fechaDeAlta);
        manejador.agregarEvento(nuevoEvento);
    }

    public List<DTEvento> ListarEventos() {
        // Implementar lógica para listar eventos
        return null;
    }

    public DTEvento DetallesEvento(String sigla) {
        // Implementar lógica para obtener detalles de un evento
        return null;
    }

    public void AltaTipoRegistro(String nombre, String descripcion, float costo, int cupo) {
        // Implementar lógica de alta de tipo de registro
    }

    public DTTipoRegistro DetallesTipoRegistro(String nombre) {
        // Implementar lógica para obtener detalles de tipo de registro
        return null;
    }

    public List<String> ListarTipoRegistro() {
        // Implementar lógica para listar tipos de registro
        return null;
    }

    public int CuposDisponibles(String siglaEdicion, String nombreTipoRegistro) {
        // Implementar lógica para obtener cupos disponibles
        return 0;
    }

    public boolean ExistePatrocinio(String codigo) {
        // Implementar lógica para verificar existencia de patrocinio
        return false;
    }

    public List<DTPatrocinio> ListarPatrocinios() {
        // Implementar lógica para listar patrocinios
        return null;
    }

    public DTPatrocinio DetallesPatrocinio(String codigo) {
        // Implementar lógica para obtener detalles de patrocinio
        return null;
    }

    public void AltaPatrocinio(DTFecha fecha, int monto, String codigo, DTNivel nivel, int cantRegistrosGratuitos) {
        // Implementar lógica de alta de patrocinio
    }

    public void AltaEdicionEvento(String siglaEvento, String nombre, String sigla, String desc, DTFecha fechaInicio, DTFecha fechaFin, DTFecha fechaAlta, String organizador, String ciudad, String pais) {
        // Implementar lógica de alta de edición de evento
    }

    public List<String> ListarEdicionesEvento(String siglaEvento) {
        // Implementar lógica para listar ediciones de un evento
        return null;
    }

    public DTEdicion DetallesEdicion(String siglaEdicion) {
        // Implementar lógica para obtener detalles de una edición
        return null;
    }

    public List<String> ListarEdiciones() {
        // Implementar lógica para listar todas las ediciones
        return null;
    }

    public void RegistroEdicionEvento(String nicknameAsistente, String siglaEdicion, String nombreTipoRegistro) {
        // Implementar lógica de registro en edición de evento
    }

    public void AltaCategoria(String nombre) {
        // Implementar lógica de alta de categoría
    }

    public List<String> ListarCategorias() {
        // Implementar lógica para listar categorías
        return null;
    }

    public List<DTRegistro> ListarRegistros(String nicknameAsistente) {
        // Implementar lógica para listar registros de un asistente
        return null;
    }

    public DTRegistro DetallesRegistro(int idRegistro) {
        // Implementar lógica para obtener detalles de un registro
        return null;
    }

    public DTEdicionEvento ConsultaEdicionEvento(Evento ev, EdicionEvento ed, TipoRegistro reg, Patrocinio pat) {
        // Implementar lógica de consulta de edición de evento
        return null;
    }

    public DTEvento ConsultaEvento(Evento ev, EdicionEvento ed) {
        // Implementar lógica de consulta de evento
        return null;
    }

    public DTRegistro ConsultaRegistro(Usuario usuario, Registro registro) {
        // Implementar lógica de consulta de registro
        return null;
    }

    public DTTipoRegistro ConsultaTipoRegistro(Evento evento, Edicion edicion, TipoRegistro tipoRegistro) {
        // Implementar lógica de consulta de tipo de registro
        return null;
    }

    public DTPatrocinio ConsultaPatrocinio(String nombreCategoria) {
        // Implementar lógica de consulta de patrocinio por categoría
        return null;
    }
}