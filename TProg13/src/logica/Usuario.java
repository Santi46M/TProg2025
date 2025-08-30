package logica;

import java.time.LocalDate;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;

public abstract class Usuario {
    private String nickname;
    private String nombre;
    private String email;

    
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean esAsistente(Usuario u) {
    	if (u instanceof Asistente) {
    		return true;
    	}
    	else return false;
    }
    
    public boolean esOrganizador(Usuario u) {
    	if (u instanceof Organizador) {
    		return true;
    	}
    	else return false;
    }
}