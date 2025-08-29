package logica;


import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class manejadorAuxiliar {
	private static manejadorAuxiliar instancia; //singleton
	private Set<String> categorias = new HashSet<String>();
	private Set<String> patrocinios = new HashSet<String>();
	
	//// instancia de manejador singleton (no se si esta del todo bien)
	private manejadorAuxiliar() {
		 this.categorias = new HashSet<>();
		 this.patrocinios = new HashSet<>();
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

	public Set<String> getPatrocinios() {
		return this.patrocinios;
	}

	public void addCategoria(String c) {
		this.categorias.add(c);
	}
	
	public void addPatrocinio(String p) {
		this.patrocinios.add(p);
	}

	public String findCategoria(String nombre) {
		return null ;//categorias.get(nombre);
	}
	
	public String findPatrocinio(int patrocinio) {
		return null ; //this.patrocinios.get(patrocinio);
	}
	
}
	
	