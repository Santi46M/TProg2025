package logica;

import java.time.LocalDate;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import logica.Institucion;
import logica.Registro;

public class Asistente extends Usuario {
    private String apellido;
    private LocalDate fechaDeNacimiento;
    private Institucion institucion;
    private Map<String, Registro> registros;
    
    public Asistente(String nickname, String nombre, String email, String apellido, LocalDate fechaDeNacimiento, Institucion institucion) {
    	
    	super();
    	this.apellido = apellido;
    	this.fechaDeNacimiento = fechaDeNacimiento;
    	this.institucion = null;
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