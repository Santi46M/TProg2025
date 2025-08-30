package logica;

import java.time.LocalDate;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


public class Organizador extends Usuario {
    private String desc;
    private String link;
    private Map<String, Ediciones> ediciones;

    public Organizador(String nickname, String nombre, String email, String desc, String link) {
    	super(nickname,nombre,email);
    	this.desc = desc;
    	this.link = link;
        this.ediciones = new HashMap<>();

    }
    public String getDesc() {
        return desc;
    }
    public Map<String, Ediciones> getEdiciones() {
        return ediciones;
    }

    public List<DTEdicion> listarEdicionesAPartirDeOrganizador() {
        List<DTEdicion> lista = new ArrayList<>();
        for (Ediciones e : ediciones.values()) {
            lista.add(new DTEdicion(
                e.getNombre(),
                e.getSigla(),
                e.getFechaInicio(),
                e.getFechaFin(),
                e.getFechaAlta(),
                this.getNombre(), // nombre del organizador
                e.getCiudad(),
                e.getPais()
            ));
        }
        return lista;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}