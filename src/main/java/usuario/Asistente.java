package usuario;

import java.time.LocalDate;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import usuario.Institucion;
import usuario.Registro;

public class Asistente extends Usuario {
    private String apellido;
    private LocalDate fechaDeNacimiento;
    private Institucion institucion;
    private Map<String, Registro> registros;
    
    public Asistente(String nickname, String nombre, String email, String apellido, LocalDate fechaDeNacimiento) {
    	super(nickname, nombre, email);
    	this.apellido = apellido;
    	this.fechaDeNacimiento = fechaDeNacimiento;
    	this.institucion = NULL;
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
    	this.institucion = Institucion;
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

    public void setFechaDeNacimiento(java.util.Date fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }
}