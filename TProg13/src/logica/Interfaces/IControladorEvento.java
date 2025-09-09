package logica.Interfaces;

import java.time.LocalDate;
import java.util.List;

import logica.Clases.*;
import logica.Datatypes.*;
import logica.Enumerados.*;

import excepciones.EdicionYaExisteException;
import excepciones.EventoYaExisteException;
import excepciones.FechasCruzadasException;
import excepciones.TipoRegistroYaExisteException;
import excepciones.ValorPatrocinioExcedidoException;

public interface IControladorEvento {
	public void AltaEvento(String nombre, String desc, LocalDate fechaDeAlta, String sigla, DTCategorias categorias) throws EventoYaExisteException;
	public void AltaTipoRegistro(Ediciones edicion, String nombre, String descripcion, int costo, int cupo) throws TipoRegistroYaExisteException;
	public void AltaPatrocinio(Ediciones edicion, Institucion institucion, DTNivel nivel, TipoRegistro tipoRegistro, int aporte, LocalDate fechaPatrocinio, int cantidadRegistros, String codigoPatrocinio) throws ValorPatrocinioExcedidoException;
	public void AltaCategoria(String nombre);
    public void AltaEdicionEvento(Eventos evento, Usuario organizador, String nombre, String sigla, String desc, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta, String ciudad, String pais)throws EdicionYaExisteException, EventoYaExisteException, FechasCruzadasException;
    public DTEdicion consultaEdicionEvento(String siglaEvento, String siglaEdicion);
    public Eventos consultaEvento(String nombreEvento);
    public void altaRegistroEdicionEvento(String idRegistro, Usuario usuario, Eventos evento, Ediciones edicion, TipoRegistro tipoRegistro, LocalDate fechaRegistro, float costo, LocalDate fechaInicio);
    public List<DTEvento> listarEventos();
    public List<String> listarEdicionesEvento(String nombreEvento);
    public Ediciones obtenerEdicion(String nombreEvento, String nombreEdicion);

    public void seleccionarEdicion(String sigla);

    public String getEdicionSeleccionadaSigla();
    public DTEdicion obtenerEdicionSeleccionada();
}
