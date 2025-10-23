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
        @WebParam(name = "fechaAlta") LocalDate fechaAlta,
        @WebParam(name = "sigla") String sigla,
        @WebParam(name = "categorias") DTCategorias categorias,
        @WebParam(name = "imagen") String imagen
    ) throws EventoYaExisteException {
        ice.altaEvento(nombre, descripcion, fechaAlta, sigla, categorias, imagen);
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

    @WebMethod(exclude = true)
    public Endpoint getEndpoint() {
        return endpoint;
    }
}