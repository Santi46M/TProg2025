package publicadores;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.Endpoint;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import excepciones.EdicionYaExisteException;
import excepciones.EventoYaExisteException;
import excepciones.FechasCruzadasException;
import logica.datatypes.*;
import logica.enumerados.DTNivel;
import logica.fabrica;
import logica.interfaces.IControladorEvento;

// === imports extra para métodos nuevos que usan clases de dominio ===
import logica.clases.Usuario;
import logica.clases.Eventos;
import logica.clases.Ediciones;
import logica.clases.TipoRegistro;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class PublicadorEvento {

    private Endpoint endpoint = null;
    private IControladorEvento ice = fabrica.getInstance().getIControladorEvento();

    @WebMethod(exclude = true)
    public void publicar() {
        endpoint = Endpoint.publish("http://localhost:8090/publicadorEvento", this);
        System.out.println("Servicio PublicadorEvento disponible en: http://localhost:8090/publicadorEvento?wsdl");
    }

    // ==========================
    // Métodos expuestos
    // ==========================

    @WebMethod
    public void altaEvento(
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "fechaAlta") String fechaAlta,
        @WebParam(name = "sigla") String sigla,
        @WebParam(name = "categorias") DTCategorias categorias,
        @WebParam(name = "imagen") String imagen
    ) throws EventoYaExisteException {
        java.time.LocalDate fecha;
        if (fechaAlta == null || fechaAlta.isBlank()) {
            fecha = java.time.LocalDate.now();
        } else {
            fecha = java.time.LocalDate.parse(fechaAlta);
        }

        ice.altaEvento(nombre, descripcion, fecha, sigla, categorias, imagen);
    }

    @WebMethod
    public void altaEdicionEventoDTO(
        @WebParam(name = "evento") DTEvento evento,
        @WebParam(name = "organizador") DTDatosUsuario organizador,
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "sigla") String sigla,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "fechaInicio") LocalDate fechaInicio,
        @WebParam(name = "fechaFin") LocalDate fechaFin,
        @WebParam(name = "fechaAlta") LocalDate fechaAlta,
        @WebParam(name = "ciudad") String ciudad,
        @WebParam(name = "pais") String pais,
        @WebParam(name = "imagen") String imagen,
        @WebParam(name = "video") String video
    ) throws EdicionYaExisteException, EventoYaExisteException, FechasCruzadasException {
        ice.altaEdicionEventoDTO(evento, organizador, nombre, sigla, descripcion, fechaInicio, fechaFin, fechaAlta, ciudad, pais, imagen, video);
    }

    @WebMethod
    public DTEvento consultaDTEvento(
        @WebParam(name = "nombreEvento") String nombreEvento
    ) {
        return ice.consultaDTEvento(nombreEvento);
    }

    @WebMethod
    public String[] listarEdicionesEvento(
        @WebParam(name = "nombreEvento") String nombreEvento
    ) {
        List<String> lista = ice.listarEdicionesEvento(nombreEvento);
        return lista.toArray(new String[0]);
    }

    @WebMethod
    public DTEdicion obtenerDtEdicion(
        @WebParam(name = "nombreEvento") String nombreEvento,
        @WebParam(name = "nombreEdicion") String nombreEdicion
    ) {
        return ice.obtenerDtEdicion(nombreEvento, nombreEdicion);
    }

    @WebMethod
    public DTPatrocinio altaPatrocinioDT(
        @WebParam(name = "siglaEdicion") String siglaEdicion,
        @WebParam(name = "nombreInstitucion") String nombreInstitucion,
        @WebParam(name = "nivel") DTNivel nivel,
        @WebParam(name = "nombreTipoRegistro") String nombreTipoRegistro,
        @WebParam(name = "aporte") int aporte,
        @WebParam(name = "fechaPatrocinio") LocalDate fechaPatrocinio,
        @WebParam(name = "cantidadRegistros") int cantidadRegistros,
        @WebParam(name = "codigoPatrocinio") String codigoPatrocinio
    ) throws excepciones.ValorPatrocinioExcedidoException,
             excepciones.PatrocinioYaExisteException,
             IllegalArgumentException {
        return ice.altaPatrocinioDT(siglaEdicion, nombreInstitucion, nivel, nombreTipoRegistro, aporte, fechaPatrocinio, cantidadRegistros, codigoPatrocinio);
    }

    @WebMethod
    public DTPatrocinio obtenerDTPatrocinio(
        @WebParam(name = "codigoPatrocinio") String codigoPatrocinio
    ) {
        return ice.obtenerDTPatrocinio(codigoPatrocinio);
    }

    @WebMethod
    public DTTipoRegistro consultaTipoRegistro(
        @WebParam(name = "nombreEvento") String nombreEvento,
        @WebParam(name = "nombreEdicion") String nombreEdicion,
        @WebParam(name = "nombreTipoRegistro") String nombreTipoRegistro
    ) {
        return ice.consultaTipoRegistro(nombreEvento, nombreEdicion, nombreTipoRegistro);
    }

    @WebMethod
    public DTEdicion obtenerEdicionPorSiglaDT(
        @WebParam(name = "sigla") String sigla
    ) {
        return ice.obtenerEdicionPorSiglaDT(sigla);
    }
    
    @WebMethod
    public DTEvento[] listarEventos() {
        java.util.List<DTEvento> lista = ice.listarEventos();
        return lista.toArray(new DTEvento[0]);
    }

    @WebMethod
    public void altaTipoRegistro(
        @WebParam(name = "dtEdicion") DTEdicion dtEdicion,
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "costo") float costo,
        @WebParam(name = "cupo") int cupo
    ) throws excepciones.TipoRegistroYaExisteException,
             excepciones.CupoTipoRegistroInvalidoException,
             excepciones.CostoTipoRegistroInvalidoException {
        ice.altaTipoRegistroDTO(dtEdicion, nombre, descripcion, costo, cupo);
    }

    @WebMethod
    public DTRegistro consultaRegistro(
        @WebParam(name = "nickname") String nickname,
        @WebParam(name = "idRegistro") String idRegistro
    ) throws excepciones.UsuarioNoExisteException {
        logica.clases.Usuario user = logica.fabrica.getInstance().getIControladorUsuario()
            .listarUsuarios().get(nickname);
        return ice.consultaRegistro(user, idRegistro);
    }

    @WebMethod
    public DTTipoRegistro[] listarTiposRegistroDeEdicion(
        @WebParam(name = "nombreEvento") String nombreEvento,
        @WebParam(name = "nombreEdicion") String nombreEdicion
    ) {
        java.util.List<DTTipoRegistro> lista = ice.listarTiposRegistroDeEdicion(nombreEvento, nombreEdicion);
        return lista.toArray(new DTTipoRegistro[0]);
    }

    /* =========================
       NUEVOS MÉTODOS AGREGADOS
       ========================= */

    @WebMethod
    public DTEvento[] listarEventosVigentes() {
        List<DTEvento> lista = ice.listarEventosVigentes();
        return lista.toArray(new DTEvento[0]);
    }

    @WebMethod
    public DTEdicion consultaEdicionEvento(
        @WebParam(name = "siglaEvento") String siglaEvento,
        @WebParam(name = "siglaEdicion") String siglaEdicion
    ) {
        return ice.consultaEdicionEvento(siglaEvento, siglaEdicion);
    }

    @WebMethod
    public void altaRegistroEdicionEvento(
        @WebParam(name = "idRegistro") String idRegistro,
        @WebParam(name = "usuario") Usuario usuario,
        @WebParam(name = "evento") Eventos evento,
        @WebParam(name = "edicion") Ediciones edicion,
        @WebParam(name = "tipoRegistro") TipoRegistro tipoRegistro,
        @WebParam(name = "fechaRegistro") LocalDate fechaRegistro,
        @WebParam(name = "costo") float costo,
        @WebParam(name = "fechaInicio") LocalDate fechaInicio
    ) {
        ice.altaRegistroEdicionEvento(idRegistro, usuario, evento, edicion, tipoRegistro, fechaRegistro, costo, fechaInicio);
    }
    @WebMethod
    public void altaRegistroEdicionEventoDT(
        @WebParam(name = "idRegistro") String idRegistro,
        @WebParam(name = "nicknameUsuario") String nicknameUsuario,
        @WebParam(name = "nombreEvento") String nombreEvento,
        @WebParam(name = "nombreEdicion") String nombreEdicion,
        @WebParam(name = "nombreTipoRegistro") String nombreTipoRegistro,
        @WebParam(name = "fechaRegistro") String fechaRegistroStr,
        @WebParam(name = "costo") float costo,
        @WebParam(name = "fechaInicio") String fechaInicioStr
    ) {
        LocalDate fechaRegistro = (fechaRegistroStr == null || fechaRegistroStr.isBlank())
                ? LocalDate.now()
                : LocalDate.parse(fechaRegistroStr);

        LocalDate fechaInicio = (fechaInicioStr == null || fechaInicioStr.isBlank())
                ? fechaRegistro
                : LocalDate.parse(fechaInicioStr);

        ice.altaRegistroEdicionEventoDT(idRegistro, nicknameUsuario, nombreEvento, nombreEdicion, nombreTipoRegistro, fechaRegistro, costo, fechaInicio);
    }

    @WebMethod
    public DTCategorias[] listarDTCategorias() {
        List<DTCategorias> lista = ice.listarDTCategorias();
        return lista.toArray(new DTCategorias[0]);
    }

    @WebMethod
    public DTEvento[] listarEventosPorCategoria(
        @WebParam(name = "nombreCategoria") String nombreCategoria
    ) {
        List<DTEvento> lista = ice.listarEventosPorCategoria(nombreCategoria);
        return lista.toArray(new DTEvento[0]);
    }

    @WebMethod
    public String[] listarCategoriasConEventos() {
        List<String> lista = ice.listarCategoriasConEventos();
        return lista.toArray(new String[0]);
    }

    @WebMethod
    public void actualizarImagenEvento(
        @WebParam(name = "nombreEvento") String nombreEvento,
        @WebParam(name = "imagenPath") String imagenPath
    ) throws IllegalArgumentException {
        ice.actualizarImagenEvento(nombreEvento, imagenPath);
    }

    @WebMethod
    public void finalizarEvento(
        @WebParam(name = "nombreEvento") String nombreEvento
    ) {
        ice.finalizarEvento(nombreEvento);
    }

    @WebMethod
    public void altaTipoRegistroDTO(
        @WebParam(name = "dtEdicion") DTEdicion dtEdicion,
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "costo") float costo,
        @WebParam(name = "cupo") int cupo
    ) throws excepciones.TipoRegistroYaExisteException,
             excepciones.CupoTipoRegistroInvalidoException,
             excepciones.CostoTipoRegistroInvalidoException {
        ice.altaTipoRegistroDTO(dtEdicion, nombre, descripcion, costo, cupo);
    }

    @WebMethod
    public String encontrarEventoPorSigla(
        @WebParam(name = "siglaEdicion") String siglaEdicion
    ) {
        return ice.encontrarEventoPorSigla(siglaEdicion);
    }

    @WebMethod(exclude = true)
    public Endpoint getEndpoint() {
        return endpoint;
    }
}
