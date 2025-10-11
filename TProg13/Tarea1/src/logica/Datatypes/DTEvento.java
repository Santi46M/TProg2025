package logica.Datatypes;

import java.time.LocalDate;
import java.util.List;

public class DTEvento {
    private String nombre;
    private String sigla;
    private String descripcion;
    private LocalDate fecha;
    private List<String> categorias;
    private List<String> ediciones;

    // NUEVO: imagen del evento
    private String imagen;

    // Constructor existente (sin imagen) -> mantiene compatibilidad
    public DTEvento(String nombre, String sigla, String descripcion, LocalDate fecha,
                    List<String> categorias, List<String> ediciones) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.categorias = categorias;
        this.ediciones = ediciones;
        this.imagen = null; // por defecto
    }

    // NUEVO: constructor con imagen (opcional)
    public DTEvento(String nombre, String sigla, String descripcion, LocalDate fecha,
                    List<String> categorias, List<String> ediciones, String imagen) {
        this(nombre, sigla, descripcion, fecha, categorias, ediciones);
        this.imagen = imagen;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getSigla() { return sigla; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFecha() { return fecha; }
    public List<String> getCategorias() { return categorias; }
    public List<String> getEdiciones() { return ediciones; }

    // NUEVO: getter de imagen
    public String getImagen() { return imagen; }

    // (Opcional) setter si querés setearla luego de creado el DTO
    public void setImagen(String imagen) { this.imagen = imagen; }
}
