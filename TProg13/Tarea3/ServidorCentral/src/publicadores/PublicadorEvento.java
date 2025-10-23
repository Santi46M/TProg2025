package publicadores;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.jws.soap.SOAPBinding.ParameterStyle;
import jakarta.xml.ws.Endpoint;
import java.util.List;
import java.util.Set;

import logica.fabrica;
import logica.interfaces.IControladorEvento;
import logica.datatypes.DTEvento;

@WebService
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.WRAPPED)
public class PublicadorEvento {

    private Endpoint endpoint = null;
    private IControladorEvento ctrl = fabrica.getInstance().getIControladorEvento();

    @WebMethod(exclude = true)
    public void publicar() {
        String url = "http://localhost:8091/publicadorEvento";
        endpoint = Endpoint.publish(url, this);
        System.out.println("Servicio PublicadorEvento disponible en: " + url);
    }




    @WebMethod
    public DTEvento obtenerDatosEvento(String nombre) {
        try {
            return ctrl.consultaDTEvento(nombre);
        } catch (Exception e) {
            return null;
        }
    }
}