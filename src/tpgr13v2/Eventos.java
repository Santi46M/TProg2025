package tpgr13v2;

public class Eventos{
	private String nombre;
	private String sigla;
	private String descripcion;
	private java.util.Date fecha;

	public Eventos(String nombre, String sigla, String descripcion, java.util.Date fecha) {
		super(); //Llama al constructor de Object
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
	
	public java.util.Date getFecha(){
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
	
	public void setFecha(java.util.Date fecha){
		this.fecha = fecha;
	}

}