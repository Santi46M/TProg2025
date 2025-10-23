package publicadores;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.Endpoint;

import logica.datatypes.DTDatosUsuario;
import logica.fabrica;
import logica.interfaces.IControladorUsuario;
import excepciones.UsuarioYaExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioTipoIncorrectoException;
import excepciones.InstitucionYaExisteException;
import excepciones.CategoriaYaExisteException;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class PublicadorUsuario {

    private Endpoint endpoint = null;
    private IControladorUsuario icu = fabrica.getInstance().getIControladorUsuario();

    // ====== Publicación del servicio ======
    @WebMethod(exclude = true)
    public void publicar() {
        endpoint = Endpoint.publish("http://localhost:8090/publicadorUsuario", this);
        System.out.println("Servicio PublicadorUsuario disponible en: http://localhost:8090/publicadorUsuario?wsdl");
    }

    // ====== Métodos expuestos ======

    @WebMethod
    public void altaUsuario(
        @WebParam(name = "nickname") String nickname,
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "correo") String correo,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "link") String link,
        @WebParam(name = "apellido") String apellido,
        @WebParam(name = "fechaNacimiento") LocalDate fechaNacimiento,
        @WebParam(name = "institucion") String institucion,
        @WebParam(name = "esOrganizador") boolean esOrganizador,
        @WebParam(name = "contrasena") String contrasena,
        @WebParam(name = "imagen") String imagen
    ) throws UsuarioYaExisteException {
        icu.altaUsuario(nickname, nombre, correo, descripcion, link, apellido, fechaNacimiento, institucion, esOrganizador, contrasena, imagen);
    }

    @WebMethod
    public DTDatosUsuario obtenerDatosUsuario(
        @WebParam(name = "nickname") String nickname
    ) throws UsuarioNoExisteException {
        return icu.obtenerDatosUsuario(nickname);
    }

   /* @WebMethod
    public Set<DTDatosUsuario> obtenerUsuariosDT() throws UsuarioNoExisteException {
        return icu.obtenerUsuariosDT();
    }*/

  /*  @WebMethod
    public Map<String, String> listarUsuarios() {
        Map<String, logica.clases.Usuario> usuarios = icu.listarUsuarios();
        return usuarios.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getNombre()));
    }*/

    @WebMethod
    public boolean validarLogin(
        @WebParam(name = "nickOrEmail") String nickOrEmail,
        @WebParam(name = "contrasena") String contrasena
    ) {
        return icu.validarLogin(nickOrEmail, contrasena);
    }

    @WebMethod
    public void altaInstitucion(
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "link") String link
    ) throws InstitucionYaExisteException {
        icu.altaInstitucion(nombre, descripcion, link);
    }

   /*z @WebMethod
    public Set<String> listarInstituciones() {
        return icu.getInstituciones();
    } */

    @WebMethod
    public void altaCategoriaSinGUI(
        @WebParam(name = "nombre") String nombre
    ) throws CategoriaYaExisteException {
        icu.altaCategoriaSinGUI(nombre);
    }

    @WebMethod
    public void modificarDatosUsuario(
        @WebParam(name = "nickname") String nickname,
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "link") String link,
        @WebParam(name = "apellido") String apellido,
        @WebParam(name = "fechaNacimiento") LocalDate fechaNacimiento,
        @WebParam(name = "institucion") String institucion,
        @WebParam(name = "imagen") String imagen
    ) throws UsuarioNoExisteException, UsuarioTipoIncorrectoException {
        icu.modificarDatosUsuario(nickname, nombre, descripcion, link, apellido, fechaNacimiento, institucion, imagen);
    }

    // ====== Fin del servicio ======
    @WebMethod(exclude = true)
    public Endpoint getEndpoint() {
        return endpoint;
    }
}