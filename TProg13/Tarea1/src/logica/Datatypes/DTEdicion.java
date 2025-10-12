package logica.Datatypes;
import logica.Enumerados.*;
import java.time.LocalDate;

import logica.Enumerados.DTEstado;

public class DTEdicion {
    private String nombre;
    private String sigla;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaAlta;
    private String organizador;
    private String ciudad;
    private String pais;
    private DTEstado estado;

    public DTEdicion(String nombre, String sigla, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta, String organizador, String ciudad, String pais) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaAlta = fechaAlta;
        this.organizador = organizador;
        this.ciudad = ciudad;
        this.pais = pais;
    }
    
    public DTEdicion(String nombre, String sigla, LocalDate fechaInicio, LocalDate fechaFin,
            LocalDate fechaAlta, String organizador, String ciudad, String pais, DTEstado estado) {
this.nombre = nombre;
this.sigla = sigla;
this.fechaInicio = fechaInicio;
this.fechaFin = fechaFin;
this.fechaAlta = fechaAlta;
this.organizador = organizador;
this.ciudad = ciudad;
this.pais = pais;
this.estado = estado;
}
    
    public DTEdicion(String nombre, String sigla, LocalDate fechaInicio, LocalDate fechaFin,
            LocalDate fechaAlta, String organizador, String ciudad, String pais,
            DTEstado estado) {
this.nombre = nombre;
this.sigla = sigla;
this.fechaInicio = fechaInicio;
this.fechaFin = fechaFin;
this.fechaAlta = fechaAlta;
this.organizador = organizador;
this.ciudad = ciudad;
this.pais = pais;
this.estado = (estado == null ? null : estado.name()); // o estado.toString()
}

    public String getNombre() { return nombre; }
    public String getSigla() { return sigla; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public String getOrganizador() { return organizador; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }
    public DTEstado getEstado() { return estado; }
    
    
}
