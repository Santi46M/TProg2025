package logica;


import java.time.LocalDate;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;


public class Registro {
    private String id;
    private float costo;
    private Usuario usuario;
    private Ediciones edicion;
    private TipoRegistro tipoRegistro;
    private LocalDate fechaRegistro;
    private LocalDate fechaInicio;

    // Constructor actualizado
    public Registro(String id, Usuario usuario, Ediciones edicion, TipoRegistro tipoRegistro, LocalDate fechaRegistro, float costo, LocalDate fechaInicio) {
        this.id = id;
        this.usuario = usuario;
        this.edicion = edicion;
        this.tipoRegistro = tipoRegistro;
        this.fechaRegistro = fechaRegistro;
        this.costo = costo;
        this.fechaInicio = fechaInicio;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Ediciones getEdicion() { return edicion; }
    public void setEdicion(Ediciones edicion) { this.edicion = edicion; }
    public TipoRegistro getTipoRegistro() { return tipoRegistro; }
    public void setTipoRegistro(TipoRegistro tipoRegistro) { this.tipoRegistro = tipoRegistro; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public float getCosto() { return costo; }
    public void setCosto(float costo) { this.costo = costo; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
}
// Comentario: En la lógica de alta, si se alcanza el cupo para el tipo de registro o el usuario ya está registrado en la edición, se debe informar al administrador para que decida editar o cancelar el registro.