package logica;

import java.time.LocalDate;

public class DTEdicionEvento {
    private String nombre;
    private String sigla;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaAlta;
    private String organizador;
    private String ciudad;
    private String pais;

    public DTEdicionEvento(String nombre, String sigla, String descripcion,
                            LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta,
                            String organizador, String ciudad, String pais) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaAlta = fechaAlta;
        this.organizador = organizador;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getSigla() { return sigla; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public String getOrganizador() { return organizador; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }
}
