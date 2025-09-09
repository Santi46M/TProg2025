package logica.Clases;

import java.time.LocalDate;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;

public abstract class Usuario {
    private String nickname;
    private String nombre;
    private String email;

    public Usuario(String nickname, String nombre, String email) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
    }
    
    public String getNickname() {
        return this.nickname;
    }

    /*public void setNickname(String nickname) {
        this.nickname = nickname;
    }*/

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return this.email;
    }

    // Si necesitas setEmail, descomenta y usa:
    // public void setEmail(String email) {
    //     this.email = email;
    // }
    
    public boolean esAsistente(Usuario u) {
    	if (u instanceof Asistente) {
    		return true;
    	}
    	else return false;
    }
    
   /* public boolean esOrganizador(Usuario u) {
    	if (u instanceof Organizador) {
    		return true;
    	}
    	else return false;
    }*/

    public Institucion getInstitucion() {
        return null;
    }
}