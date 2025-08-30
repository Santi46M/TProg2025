package logica;

import java.util.List;
import java.time.LocalDate;
import logica.Eventos;
import logica.ManejadorEvento;
import logica.manejadorUsuario;
import logica.manejadorAuxiliar;
import java.util.ArrayList;
import java.util.Map;

import excepciones.EdicionYaExisteException;
import excepciones.EventoYaExisteException;
import excepciones.NombreEdicionEnUsoException;
import excepciones.EventoYaExisteException;
import excepciones.EdicionYaExisteException;


public class ControladorEvento implements IControladorEvento{
	ManejadorEvento manejador = ManejadorEvento.getInstancia();
	manejadorUsuario mUsuario = manejadorUsuario.getInstancia();

    public void AltaEvento(String nombre, String desc, LocalDate fechaDeAlta, String sigla, DTCategorias categorias) throws EventoYaExisteException {
        if (categorias == null || categorias.getCategorias() == null || categorias.getCategorias().isEmpty()) {
            throw new RuntimeException("Debe asociar al menos una categoría al evento");
        }
        if (manejador.existeEvento(nombre)) {
            throw new EventoYaExisteException(nombre);
        }
        manejadorAuxiliar manejadorAux = manejadorAuxiliar.getInstancia();
        Map<String, Categoria> categoriasMap = new java.util.HashMap<>();
        for (String nombreCat : categorias.getCategorias()) {
            Categoria cat = manejadorAux.obtenerCategoria(nombreCat);
            if (cat == null) {
                throw new RuntimeException("La categoría '" + nombreCat + "' no existe");
            }
            categoriasMap.put(nombreCat, cat);
        }
        Eventos nuevoEvento = new Eventos(nombre, sigla, desc, fechaDeAlta, categoriasMap);
        manejador.agregarEvento(nuevoEvento);
    }

    // Recibe la edición, nombre, descripción, costo y cupo
    public void AltaTipoRegistro(Ediciones edicion, String nombre, String descripcion, int costo, int cupo) {
        // Falta chequeo de nombre repetido para la edición (agregar aquí si se requiere)
        TipoRegistro tipo = new TipoRegistro(edicion, nombre, descripcion, costo, cupo);
        edicion.agregarTipoRegistro(tipo);
        ManejadorEvento manejadorEvento = ManejadorEvento.getInstancia();
        manejadorEvento.agregarTipoRegistro(tipo);
    }
    
    // Nuevo método ajustado para AltaPatrocinio
    public void AltaPatrocinio(Ediciones edicion, Institucion institucion, DTNivel nivel, TipoRegistro tipoRegistro, int aporte, LocalDate fechaPatrocinio, int cantidadRegistros, String codigoPatrocinio) {
        manejadorAuxiliar manejadorAux = manejadorAuxiliar.getInstancia();
        // Validar código único
        if (manejadorAux.existePatrocinio(codigoPatrocinio)) {
            throw new RuntimeException("Ya existe un patrocinio con el código: " + codigoPatrocinio);
        }
        // Validar que no exista un patrocinio de la misma institución para la misma edición
        for (Patrocinio p : manejadorAux.listarPatrocinios()) {
            if (p.getInstitucion().equals(institucion) && p.getEdicion().equals(edicion)) {
                // TODO: Informar al administrador que ya existe un patrocinio de la institución para la edición
                // El administrador puede optar por editar el patrocinio o cancelar el alta
                return;
            }
        }
        // Validar porcentaje
        int costoRegistros = cantidadRegistros * tipoRegistro.getCosto();
        if (costoRegistros > aporte * 0.2) {
            // TODO: Informar al administrador que el costo de los registros gratuitos supera el 20% del aporte
            // El administrador puede optar por editar el patrocinio o cancelar el alta
            return;
        }
        // Crear y guardar el patrocinio
        Patrocinio pat = new Patrocinio(edicion, institucion, nivel, tipoRegistro, aporte, fechaPatrocinio, cantidadRegistros, codigoPatrocinio);
        manejadorAux.agregarPatrocinio(pat);
    }
    // Método auxiliar para obtener el costo del tipo de registro
    /*
    private int obtenerCostoTipoRegistro(String siglaEdicion, String tipoRegistro) {
        // Buscar el costo del tipo de registro en la edición
        for (Eventos evento : ManejadorEvento.getInstancia().obtenerEventos().values()) {
            Ediciones ed = evento.obtenerEdicion(siglaEdicion);
            if (ed != null) {
                TipoRegistro tr = ed.obtenerTipoRegistro(tipoRegistro);
                if (tr != null) {
                    return tr.getCosto();
                }
            }
        }
        throw new RuntimeException("No se encontró el tipo de registro en la edición");
    }
    */
    
    public void AltaCategoria(String nombre) {
    	manejadorAuxiliar manejadorAux = manejadorAuxiliar.getInstancia();
        if (manejadorAux.existeCategoria(nombre)) {
            throw new RuntimeException("Ya existe la categoría: " + nombre);
        }
        Categoria categoria = new Categoria(nombre);
        manejadorAux.agregarCategoria(nombre, categoria);
    }
    /*
    facu public List<DTEvento> ListarEventos() {
        return null;
    }

    public DTEvento DetallesEvento(String sigla) {
        return null;
    }

    public List<String> ListarTipoRegistro() {
        return null;
    }

    public int CuposDisponibles(String siglaEdicion, String nombreTipoRegistro) {
        return 0;
    }

    public boolean ExistePatrocinio(String codigo) {
        return false;
    }

    public List<DTPatrocinio> ListarPatrocinios() {
        return null;
    }

    public DTPatrocinio DetallesPatrocinio(String codigo) {
        return null;
    }

    public List<String> ListarEdicionesEvento(String siglaEvento) {
        return null;
    }

    public DTEdicion DetallesEdicion(String siglaEdicion) {
        return null;
    }

    public List<String> ListarEdiciones() {
        return null;
    }

    public void RegistroEdicionEvento(String nicknameAsistente, String siglaEdicion, String nombreTipoRegistro) {
    }

    public List<String> ListarCategorias() {
        return null;
    }

    public List<DTRegistro> ListarRegistros(String nicknameAsistente) {
        return null;
    }

    public DTRegistro DetallesRegistro(int idRegistro) {
        return null;
    }

    public DTEdicionEvento ConsultaEdicionEvento(Evento ev, EdicionEvento ed, TipoRegistro reg, Patrocinio pat) {
        return null;
    }
	*/
    /*
    /*
    public DTRegistro ConsultaRegistro(Usuario usuario, Registro registro) {
        return null;
    }

    public DTTipoRegistro ConsultaTipoRegistro(Evento evento, Edicion edicion, TipoRegistro tipoRegistro) {
        return null;
    }

    public DTPatrocinio ConsultaPatrocinio(String nombreCategoria) {
        return null;
    } */

    public void AltaEdicionEvento(Eventos evento, Usuario organizador, String nombre, String sigla, String desc, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta, String ciudad, String pais)throws EdicionYaExisteException, EventoYaExisteException {
    	ManejadorEvento manejador = ManejadorEvento.getInstancia();
    	if(manejador.existeEvento(evento.getNombre())){
    		if(!manejador.existeEdicion(nombre)) {
	        Ediciones nuevaEdicion = new Ediciones(evento, nombre, sigla, fechaInicio, fechaFin, fechaAlta, organizador, ciudad, pais);
	        evento.agregarEdicion(nuevaEdicion);
	        manejador.agregarEdicion(nuevaEdicion);
    		}
    		else throw new EdicionYaExisteException(nombre);
    	}
    	else throw new EventoYaExisteException(evento.getNombre());
    }

    // ConsultaEdicionEvento: Devuelve los detalles de una edición de un evento
    public DTEdicion consultaEdicionEvento(String siglaEvento, String siglaEdicion) {
        ManejadorEvento manejador = ManejadorEvento.getInstancia();
        Eventos evento = manejador.obtenerEvento(siglaEvento);
        if (evento == null) return null;
        Ediciones edicion = evento.obtenerEdicion(siglaEdicion);
        if (edicion == null) return null;
        // Devuelve los datos de la edición usando DTEdicion
        return new DTEdicion(
            edicion.getNombre(),
            edicion.getSigla(),
            edicion.getFechaInicio(),
            edicion.getFechaFin(),
            edicion.getFechaAlta(),
            edicion.getOrganizador().getNickname(),
            edicion.getCiudad(),
            edicion.getPais()
        );
    }

    public Eventos consultaEvento(String nombreEvento) {
        ManejadorEvento manejador = ManejadorEvento.getInstancia();
        Eventos evento = manejador.obtenerEvento(nombreEvento);
        if (evento == null) return null;
        // Aquí se pueden agregar más detalles si se requiere (categorías, ediciones, etc.)
        // Si el administrador selecciona una edición, se debe consultar la edición de evento:
        // TODO: Implementar consulta de edición de evento según el caso de uso
        return evento;
    }

    public void altaRegistroEdicionEvento(String idRegistro, Usuario usuario, Eventos evento, Ediciones edicion, TipoRegistro tipoRegistro, LocalDate fechaRegistro, float costo, LocalDate fechaInicio) {
        ManejadorEvento manejadorEvento = ManejadorEvento.getInstancia();
        if (edicion == null) {
            throw new RuntimeException("No se encontró la edición especificada");
        }
        if (tipoRegistro == null) {
            throw new RuntimeException("No se encontró el tipo de registro especificado para la edición");
        }
        // Verificar si el usuario ya está registrado en la edición
        boolean yaRegistrado = false;
        for (Registro reg : manejadorEvento.obtenerRegistros().values()) {
            if (reg.getUsuario().equals(usuario) && reg.getEdicion().equals(edicion)) {
                yaRegistrado = true;
                break;
            }
        }
        // Verificar cupo del tipo de registro
        int cantidadRegistrados = 0;
        for (Registro reg : manejadorEvento.obtenerRegistros().values()) {
            if (reg.getTipoRegistro().equals(tipoRegistro) && reg.getEdicion().equals(edicion)) {
                cantidadRegistrados++;
            }
        }
        if (yaRegistrado) {
            // TODO: Informar al administrador que el usuario ya está registrado en la edición
            // El administrador puede optar por editar el registro o cancelar el alta
            return;
        }
        if (cantidadRegistrados >= tipoRegistro.getCupo()) {
            // TODO: Informar al administrador que se alcanzó el cupo para el tipo de registro
            // El administrador puede optar por editar el registro o cancelar el alta
            return;
        }
        // Crear y guardar el registro
        Registro nuevoRegistro = new Registro(idRegistro, usuario, edicion, tipoRegistro, fechaRegistro, costo, fechaInicio);
        manejadorEvento.agregarRegistro(nuevoRegistro);
    }
    
    public List<DTEvento> listarEventos() {

	    Map<String, Eventos> eventos = manejador.obtenerEventos();

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
    public void altaEdicionEvento(String nombreEvento, String nombre, String sigla, String desc, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta, String organizador,String ciudad,String pais) throws NombreEdicionEnUsoException {

	    if (manejador.existeEdicion(nombre)) {
	        throw new NombreEdicionEnUsoException(nombre);
	    }

	    Eventos evento = manejador.obtenerEventos().get(nombreEvento);
	    if (evento == null) {
	        throw new IllegalArgumentException("No existe el evento con sigla: " + nombreEvento);
	    }

	    Organizador org = (Organizador) mUsuario.findOrganizador(organizador);
	    if (org == null) {
	        throw new IllegalArgumentException("No existe el organizador: " + organizador);
	    }
	    
	    Ediciones nuevaEdicion = new Ediciones(evento,nombre,sigla,fechaInicio, fechaFin,fechaAlta, org, ciudad,pais);

	    evento.agregarEdicion(nuevaEdicion);

	    manejador.agregarEdicion(nuevaEdicion);

		}
    
    public List<String> listarEdicionesEvento(String nombreEvento) {
        Eventos evento = manejador.obtenerEvento(nombreEvento);
        if (evento == null) return new ArrayList<>();
        return new ArrayList<>(evento.getEdiciones().keySet());
    }

    public Ediciones obtenerEdicion(String nombreEvento, String nombreEdicion) {
        Eventos evento = manejador.obtenerEvento(nombreEvento);
        if (evento == null) return null;
        return evento.obtenerEdicion(nombreEdicion);
    }
}