package tpgr13v2;

import java.time.LocalDate;

public class Patrocinio {
    private LocalDate fecha;
    private int monto;
    private int codigo;
    //private DTNivel nivel;
    private int cantRegistrosGratuitos;

    public Patrocinio(LocalDate fecha, int monto, int codigo, String nivel, int cantRegistrosGratuitos) {
        this.fecha = fecha;
        this.monto = monto;
        this.codigo = codigo;
        //this.nivel = nivel;
        this.cantRegistrosGratuitos = cantRegistrosGratuitos;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

//    public String getNivel() {
//        return nivel;
//    }
//
//    public void setNivel(String nivel) {
//        this.nivel = nivel;
//    }

    public int getCantRegistrosGratuitos() {
        return cantRegistrosGratuitos;
    }

    public void setCantRegistrosGratuitos(int cantRegistrosGratuitos) {
        this.cantRegistrosGratuitos = cantRegistrosGratuitos;
    }
}