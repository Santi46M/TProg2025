package logica;

import exceptions.NombreEdicionEnUsoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControladorEvento {
	manejadorUsuario mUsuarios = manejadorUsuario.getInstancia();
    ManejadorEventos manejador = ManejadorEventos.getInstancia();
	public List<DTEvento> listarEventos() {

	    Map<String, Eventos> eventos = manejador.getEventos();

	    List<DTEvento> lista = new ArrayList<>();
	    for (Eventos e : eventos.values()) {
	        lista.add(new DTEvento(
	            e.getNombre(),
	            e.getSigla(),
	            e.getDescripcion(),
	            e.getFecha()
	        ));
	    }
	    return lista;
	}	
	
	


	public void altaEdicionEvento(String nombreEvento, String nombre, String sigla, String desc, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta, String organizador,
	        String ciudad,
	        String pais
	) {

	    if (manejador.existeEdicion(nombre)) {
	        throw new NombreEdicionEnUsoException(nombre);
	    }

	    Eventos evento = manejador.getEventos().get(nombreEvento);
	    if (evento == null) {
	        throw new IllegalArgumentException("No existe el evento con sigla: " + nombreEvento);
	    }

	    Organizador org = (Organizador) mUsuarios.findOrganizador(organizador);
	    if (org == null) {
	        throw new IllegalArgumentException("No existe el organizador: " + organizador);
	    }


	    Ediciones nuevaEdicion = new Ediciones(nombre,sigla,desc,fechaInicio, fechaFin,fechaAlta, org.getNombre(), ciudad,pais);

	    evento.agregarEdicion(nuevaEdicion);

	    manejador.altaEdicion(nuevaEdicion.getNombre(), nuevaEdicion);
	}
	
	
	
	
}
