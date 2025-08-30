package logica;

import java.time.LocalDate;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;


public class Ediciones {
    private Eventos evento;
    private String nombre;
    private String sigla;
    private String desc;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaAlta;
    private Usuario organizador;
    private String ciudad;
    private String pais;


    public Ediciones(Eventos evento, String nombre, String sigla, String desc,
                     LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta,
                     Usuario organizador, String ciudad, String pais) {
        this.evento = evento;
        this.nombre = nombre;
        this.sigla = sigla;
        this.desc = desc;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaAlta = fechaAlta;
        this.organizador = organizador;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    public Eventos getEvento() {
        return evento;
    }

    public void setEvento(Eventos evento) {
        this.evento = evento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Usuario getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Usuario organizador) {
        this.organizador = organizador;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

	public void agregarTipoRegistro(TipoRegistro tipo) {
		// TODO Auto-generated method stub
		
	}
}