package logica;

import java.time.LocalDate;
public class DTDatosUsuario {
	private String nickname;
    private String nombre;
    private String email;
    private String apellido;       // si es Asistente
    private LocalDate fechaNac;    // si es Asistente
    private String desc;           // si es Organizador
    private String link;           // si es Organizador

    // Constructor básico para todos
    public DTDatosUsuario(String nickname, String nombre, String email) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
    }

    // --- Getters ---
    public String getNickname() { return nickname; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getApellido() { return apellido; }
    public LocalDate getFechaNac() { return fechaNac; }
    public String getDesc() { return desc; }
    public String getLink() { return link; }

    // --- Setters opcionales ---
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setFechaNac(LocalDate fechaNac) { this.fechaNac = fechaNac; }
    public void setDesc(String desc) { this.desc = desc; }
    public void setLink(String link) { this.link = link; }
}