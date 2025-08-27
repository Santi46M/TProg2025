package tpgr13v2;

import java.time.LocalDate;
//Marca que no usamos
//import java.util.Set;
//import java.util.Map;
//import java.util.HashSet;
//import java.util.HashMap;


public class Registro {
    private String id;
    private LocalDate fechaRealizacion;
    private float costo;

    public Registro(String id, LocalDate fechaRealizacion, float costo) {
        this.id = id;
        this.fechaRealizacion = fechaRealizacion;
        this.costo = costo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(LocalDate fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }
}