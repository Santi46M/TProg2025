package Logica;

import usuario.Eventos;
import java.util.HashMap;
import java.util.Map;

public class ManejadorEvento {
    private static ManejadorEvento instancia = null;
    private Map<String, Eventos> eventos;

    private ManejadorEvento() {
        eventos = new HashMap<>();
    }

    public static ManejadorEvento getInstancia() {
        if (instancia == null) {
            instancia = new ManejadorEvento();
        }
        return instancia;
    }

    public boolean existeEvento(String sigla) {
        return eventos.containsKey(sigla);
    }

    public void agregarEvento(Eventos evento) {
        eventos.put(evento.getSigla(), evento);
    }

    public Eventos obtenerEvento(String sigla) {
        return eventos.get(sigla);
    }

    // Puedes agregar métodos para listar, eliminar, etc.
}
