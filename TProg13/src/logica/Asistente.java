package logica;

import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;


public class Asistente extends Usuario {
    private String apellido;
    private LocalDate fechaDeNacimiento;
    private Institucion institucion;
    private Map<String, Registro> registros;
    
    public Asistente(String nickname, String nombre, String email, String apellido, LocalDate fechaDeNacimiento, Institucion institucion) {
    	
    	super(nickname,nombre,email);
    	this.apellido = apellido;
    	this.fechaDeNacimiento = fechaDeNacimiento;
    	this.institucion = institucion;
    	this.registros = new HashMap<String, Registro>();
    }
    
    public Map<String, Registro> getRegistros(){
    	return registros;
    }
    
    public void addRegistro(String id, Registro registro) {
    	this.registros.put(id, registro);
    }
    
    public Institucion getInstitucion() {
    	return institucion;
    }
    
    public void addInstitucion(Institucion institucion) {
    	this.institucion = institucion;
    }
    
    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }
}