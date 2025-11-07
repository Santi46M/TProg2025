package logica.datatypes;

public class DTArchEdicion {
  public final String nombreEvento;
  public final String siglaEdicion;
  public final String fechaInicio;
  public final String fechaFin;
  public final String fechaArchivado;
  public final String organizadorNick;
  public final int cantidadAsistentes;

  
  public DTArchEdicion(String nombreEvento, String siglaEdicion,
                       String ini, String fin,
                       String archivado, String organizadorNick,
                       int cantidadAsistentes) {
    this.nombreEvento = nombreEvento;
    this.siglaEdicion = siglaEdicion;
    this.fechaInicio = ini;
    this.fechaFin = fin;
    this.fechaArchivado = archivado;
    this.organizadorNick = organizadorNick;
    this.cantidadAsistentes = cantidadAsistentes;
  }
  
}