package logica;

import java.util.*;

public class ManejadorEventos {

 
	private static ManejadorEventos instancia;


	private Map<String, Eventos> eventos;
	private Map<String, Ediciones> ediciones;
	private Set<TipoRegistro> tiposRegistro;
	private Map<String, Registro> registros;

	private ManejadorEventos() {
	    eventos = new HashMap<>();
	    ediciones = new HashMap<>();
	    tiposRegistro = new HashSet<>();
	    registros = new HashMap<>();
	}
    
    public static ManejadorEventos getInstancia() {
        if (instancia == null) {
            instancia = new ManejadorEventos();
        }
        return instancia;
    }

    // Eventos 
    public void altaEvento(String clave, Eventos e) {
        eventos.put(clave, e);
    }
    
    public Eventos obtenerEvento(String clave) { 
    	return eventos.get(clave); 
    }
    
    public void eliminarEvento(String clave) { 
    	 eventos.remove(clave); 
    }
    

    // Ediciones
    public void altaEdicion(String clave, Ediciones ed) {
        ediciones.put(clave, ed);
    }
    public Ediciones obtenerEdicion(String clave) { 
    	return ediciones.get(clave); 
    }
    public void eliminarEdicion(String clave) { 
    	 ediciones.remove(clave); 
    }


    //  Tipos de Registro 
    public void altaTipoRegistro(TipoRegistro tr) {
         tiposRegistro.add(tr); 
    }
    public void eliminarTipoRegistro(TipoRegistro tr) {
    		tiposRegistro.remove(tr);
    }


    // Registros
    public void altaRegistro(String id, Registro r) {
        registros.put(id, r);
    }
    public Registro obtenerRegistro(String id) { 
    	return registros.get(id); 
    }
    public void eliminarRegistro(String id) { 
    	 registros.remove(id) ; 
    }



}