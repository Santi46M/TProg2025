package publicadores;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.Endpoint;

import logica.datatypes.DTTopEvento;
import util.ConfigLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class PublicadorEstadisticas {

    private Endpoint endpoint = null;

    @WebMethod(exclude = true)
    public void publicar() {
        String ip = ConfigLoader.get("ipServidor");
        String puerto = ConfigLoader.get("puerto");
        String address = "http://" + ip + ":" + puerto + "/publicadorEstadisticas";

        endpoint = Endpoint.publish(address, this);
        System.out.println("Servicio PublicadorEstadisticas publicado en: " + address);
        System.out.println("WSDL disponible en: " + address + "?wsdl");
    }

    // --- Métodos auxiliares para IP y puerto ---
    private String obtenerIpLocal() {
        return ConfigLoader.get("ipServidor") != null ? ConfigLoader.get("ipServidor") : "localhost";
    }

    private String obtenerPuerto() {
        return ConfigLoader.get("puerto") != null ? ConfigLoader.get("puerto") : "8090";
    }

    /* =============================
       LÓGICA DEL SERVICIO
       ============================= */

    // Contador en memoria por nombre de evento
    private static final Map<String, Integer> VISITAS = new ConcurrentHashMap<>();

    @WebMethod
    public void registrarVisita(@WebParam(name = "eventoNombre") String eventoNombre) {
        if (eventoNombre == null || eventoNombre.isBlank()) return;
        String key = eventoNombre.trim();
        int nuevo = VISITAS.merge(key, 1, Integer::sum);
        System.out.println("[ESTAD] +1 '" + key + "' => " + nuevo);
    }

    @WebMethod
    public DTTopEvento[] topEventos(@WebParam(name = "n") int n) {
        if (n <= 0 || VISITAS.isEmpty()) return new DTTopEvento[0];
        System.out.println("[ESTAD] topEventos(" + n + ") | total claves=" + VISITAS.size());

        List<DTTopEvento> lista = new ArrayList<>(VISITAS.size());
        for (Map.Entry<String, Integer> e : VISITAS.entrySet()) {
            int count = (e.getValue() == null ? 0 : e.getValue());
            lista.add(new DTTopEvento(e.getKey(), count));
        }

        lista.sort(Comparator.comparingInt(DTTopEvento::getVisitas).reversed());
        if (lista.size() > n) lista = lista.subList(0, n);

        return lista.toArray(new DTTopEvento[0]);
    }

    @WebMethod(exclude = true)
    public Endpoint getEndpoint() {
        return endpoint;
    }
}