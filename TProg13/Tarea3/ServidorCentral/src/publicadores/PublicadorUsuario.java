package publicadores;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.jws.soap.SOAPBinding.ParameterStyle;
import jakarta.xml.ws.Endpoint;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaExisteException;
import logica.fabrica;
import logica.interfaces.IControladorUsuario;
import logica.datatypes.DTDatosUsuario;
import java.util.ArrayList;
import java.util.List;

@WebService
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.WRAPPED)
public class PublicadorUsuario {

    private Endpoint endpoint = null;
    private IControladorUsuario ctrl = fabrica.getInstance().getIControladorUsuario();

    @WebMethod(exclude = true)
    public void publicar() {
        String url = "http://localhost:8090/publicadorUsuario";
        endpoint = Endpoint.publish(url, this);
        System.out.println("Servicio PublicadorUsuario disponible en: " + url);
    }

    @WebMethod
    public String altaUsuario(String nick, String nombre, String correo, String descripcion,
                              String link, String apellido, LocalDate fechaNac, String institucion,
                              boolean esOrganizador, String password, String imagen) {
        try {
            ctrl.altaUsuario(nick, nombre, correo, descripcion, link, apellido, fechaNac,
                             institucion, esOrganizador, password, imagen);
            return "OK";
        } catch (UsuarioYaExisteException e) {
            return "ERROR: " + e.getMessage();
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }


    
}