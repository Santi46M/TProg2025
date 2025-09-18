package logica.Manejadores;


import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import logica.Clases.*;

public class manejadorAuxiliar {
	private static manejadorAuxiliar instancia; //singleton
	// Usar Map para categorías y Set para patrocinios (objetos)
	private Map<String, Categoria> categorias = new HashMap<>();
	private Set<Patrocinio> patrocinios = new HashSet<>();
	
	//// instancia de manejador singleton (no se si esta del todo bien)
	private manejadorAuxiliar() {
		 this.categorias = new HashMap<>();
		 this.patrocinios = new HashSet<>();
	}
	
	public static manejadorAuxiliar getInstancia() {
		if (instancia == null) {
			instancia = new manejadorAuxiliar();
		}
		return instancia;
	}
	
	public boolean existeCategoria(String nombre) {
	    return categorias.containsKey(nombre);
	}

	public void agregarCategoria(String nombre, Categoria categoria) {
	    categorias.put(nombre, categoria);
	}

	public Categoria obtenerCategoria(String nombre) {
		return categorias.get(nombre);
	}
	
	public Set<String> listarCategorias() {
	    return categorias.keySet();
	}

	public boolean existePatrocinio(String codigo) {
	    for (Patrocinio p : patrocinios) {
	        if (p.getCodigoPatrocinio().equals(codigo)) return true;
	    }
	    return false;
	}

	public void agregarPatrocinio(Patrocinio patrocinio) {
	    patrocinios.add(patrocinio);
	}

	public Set<Patrocinio> listarPatrocinios() {
	    return patrocinios;
	}
}