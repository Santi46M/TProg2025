package logica.datatypes;

import java.time.LocalDate;

public class DTRegistro {
    private String id;
    private String usuario;
    private String edicion;
    private String tipoRegistro; 
    private LocalDate fechaRegistro;
    private float costo;
    private LocalDate fechaInicio;

    
    public DTRegistro(String id, String usuario, String edicion, String tipoRegistro, LocalDate fechaRegistro, float costo, LocalDate fechaInicio) {
        this.id = id;
        this.usuario = usuario;
        this.edicion = edicion;
        this.tipoRegistro = tipoRegistro;
        this.fechaRegistro = fechaRegistro;
        this.costo = costo;
        this.fechaInicio = fechaInicio;
    }

    
    public String getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getEdicion() {
        return edicion;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public float getCosto() {
        return costo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
}
