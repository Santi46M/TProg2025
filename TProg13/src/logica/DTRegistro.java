package logica;

import java.time.LocalDate;

public class DTRegistro {

    private String id;
    private LocalDate fechaRealizacion;
    private float costo;
    private String nombreUsuario;
    private String nombreEdicion;
    private String tipoRegistro;
    private LocalDate fechaRegistro;
    private LocalDate fechaInicio;

    public DTRegistro(String id, LocalDate fechaRealizacion, float costo,
                      String nombreUsuario, String nombreEdicion, String tipoRegistro,
                      LocalDate fechaRegistro, LocalDate fechaInicio) {
        this.id = id;
        this.fechaRealizacion = fechaRealizacion;
        this.costo = costo;
        this.nombreUsuario = nombreUsuario;
        this.nombreEdicion = nombreEdicion;
        this.tipoRegistro = tipoRegistro;
        this.fechaRegistro = fechaRegistro;
        this.fechaInicio = fechaInicio;
    }

    // Getters
    public String getId() { return id; }
    public LocalDate getFechaRealizacion() { return fechaRealizacion; }
    public float getCosto() { return costo; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getNombreEdicion() { return nombreEdicion; }
    public String getTipoRegistro() { return tipoRegistro; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public LocalDate getFechaInicio() { return fechaInicio; }
}
