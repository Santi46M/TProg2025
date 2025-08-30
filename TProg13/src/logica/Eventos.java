package logica;

import java.time.LocalDate;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;


public class Eventos{
	private String nombre;
	private String sigla;
	private String descripcion;
	private LocalDate fecha;
	private List<String> categorias;
    private Map<String, Ediciones> ediciones = new HashMap<>();

	public Eventos(String nombre, String sigla, String descripcion, LocalDate fecha, List<String> categorias) {
		this.nombre = nombre;
		this.sigla = sigla;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.categorias = categorias;
	}	
	
	//Definimos los getters
	public String getNombre() {
		return this.nombre;
	}
	
	public String getSigla() {
		return this.sigla;
	} 
	
	public String getDescripcion() {
		return this.descripcion;
	}
	
	public LocalDate getFecha(){
		return this.fecha;
	}
	
	public List<String> getCategorias() {
        return this.categorias;
    }
	
	//Definimos los setters
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	} 
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public void setFecha(LocalDate fecha){
		this.fecha = fecha;
	}
	
	public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public void agregarEdicion(Ediciones edicion) {
        ediciones.put(edicion.getNombre(), edicion);
    }

    public Map<String, Ediciones> getEdiciones() {
        return ediciones;
    }

    public Ediciones obtenerEdicion(String nombreEdicion) {
        return ediciones.get(nombreEdicion);
    }

}