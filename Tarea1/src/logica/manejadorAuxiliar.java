package logica;


import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class manejadorAuxiliar {
	private static manejadorAuxiliar instancia; //singleton
	private Set<String> categorias = new HashSet<String>();
	private Set<int> patrocinios = new HashSet<int>();
	
	//// instancia de manejador singleton (no se si esta del todo bien)
	private manejadorAuxiliar() {
		 categorias = new HashSet<>();
		 patrocinios = new HashSet<>();
	}
	
	public static manejadorAuxiliar getInstancia() {
		if (instancia == null) {
			instancia = new manejadorAuxiliar();
		}
		return instancia;
	}
	
	public Set<String> getCategorias() {
		return this.categorias;
	}

	public Set<int> getPatrocinios() {
		return this.patrocinios;
	}

	public void addCategoria(String c) {
		this.categorias.put(c);
	}
	
	public void addPatrocinio(int p) {
		this.patrocinios.put(p);
	}

	public String findCategoria(String nombre) {
		return categorias.get(nombre);
	}
	
	public int findPatrocinio(int patrocinio) {
		return this.patrocinios.get(patrocinio);
	}
	
}
	
	