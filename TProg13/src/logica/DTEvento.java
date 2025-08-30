package logica;

import java.time.LocalDate;


public class DTEvento {
    private String nombre;
    private String sigla;
    private String descripcion;
    private LocalDate fecha;

    public DTEvento(String nombre, String sigla, String descripcion, LocalDate fecha) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getSigla() {
        return sigla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
