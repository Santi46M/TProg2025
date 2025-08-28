package logica;

import java.time.LocalDate;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;


public class Eventos{
	private String nombre;
	private String sigla;
	private String descripcion;
	private LocalDate fecha;

	public Eventos(String nombre, String sigla, String descripcion, LocalDate fecha) {
		this.nombre = nombre;
		this.sigla = sigla;
		this.descripcion = descripcion;
		this.fecha = fecha;
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

}