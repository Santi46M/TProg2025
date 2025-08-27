package tpgr13v2;

//No usamos por el momento
//import java.time.LocalDate;
//import java.util.Set;
//import java.util.Map;
//import java.util.HashSet;
//import java.util.HashMap;


public class Organizador extends Usuario {
    private String desc;
    private String link;

    public Organizador(String nickname, String nombre, String email, String desc, String link) {
    	super(nickname, nombre, email);
    	this.desc = desc;
    	this.link = link;
    }
    
    public String getDesc() {
        return desc;
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