package logica.Controladores;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.*;

import excepciones.EdicionYaExisteException;
import excepciones.EventoYaExisteException;
import excepciones.RegistroNoExiste;
import excepciones.TipoRegistroYaExisteException;
import excepciones.UsuarioNoEsAsistente;
import excepciones.PatrocinioYaExisteException;
import excepciones.ValorPatrocinioExcedidoException;
import excepciones.FechasCruzadasException;

import logica.Manejadores.*;
import logica.Interfaces.IControladorEvento;
import logica.Clases.*;
import logica.Datatypes.*;
import logica.Enumerados.*;

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

    public void AltaTipoRegistro(Ediciones edicion, String nombre, String descripcion, int costo, int cupo) throws TipoRegistroYaExisteException {
        if (edicion.obtenerTipoRegistro(nombre) != null) {
            throw new TipoRegistroYaExisteException(nombre);
        }
        TipoRegistro tipo = new TipoRegistro(edicion, nombre, descripcion, costo, cupo);
        edicion.agregarTipoRegistro(nombre, tipo);
        ManejadorEvento manejadorEvento = ManejadorEvento.getInstancia();
        manejadorEvento.agregarTipoRegistro(tipo);
    }
    
    public void AltaPatrocinio(Ediciones edicion, Institucion institucion, DTNivel nivel, TipoRegistro tipoRegistro, int aporte, LocalDate fechaPatrocinio, int cantidadRegistros, String codigoPatrocinio) throws ValorPatrocinioExcedidoException {
        manejadorAuxiliar manejadorAux = manejadorAuxiliar.getInstancia();
        for (Patrocinio p : manejadorAux.listarPatrocinios()) {
            if (p.getInstitucion().equals(institucion) && p.getEdicion().equals(edicion)) {
                throw new PatrocinioYaExisteException(institucion.getNombre(), edicion.getNombre());
            }
            if (p.getCodigoPatrocinio().equals(codigoPatrocinio)) {
                return;
            }
        }
        int valorRegistros = cantidadRegistros * tipoRegistro.getCosto();
        if (valorRegistros > (aporte * 0.2)) {
            throw new ValorPatrocinioExcedidoException();
        }
        Patrocinio pat = new Patrocinio(edicion, institucion, nivel, tipoRegistro, aporte, fechaPatrocinio, cantidadRegistros, codigoPatrocinio);
        manejadorAux.agregarPatrocinio(pat);
        edicion.getPatrocinios().add(pat);
    }
    
    public void AltaCategoria(String nombre) {
    	manejadorAuxiliar manejadorAux = manejadorAuxiliar.getInstancia();
        if (manejadorAux.existeCategoria(nombre)) {
            throw new RuntimeException("Ya existe la categoría: " + nombre);
        }
        Categoria categoria = new Categoria(nombre);
        manejadorAux.agregarCategoria(nombre, categoria);
    }

    public void AltaEdicionEvento(Eventos evento, Usuario usuario, String nombre, String sigla, String desc, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta, String ciudad, String pais)throws EdicionYaExisteException, EventoYaExisteException, FechasCruzadasException {
        ManejadorEvento manejador = ManejadorEvento.getInstancia();
        if (fechaInicio.isAfter(fechaFin)) {
            throw new FechasCruzadasException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }
        if(manejador.existeEvento(evento.getNombre())){
            if(!manejador.existeEdicion(nombre)) {
                Ediciones nuevaEdicion = new Ediciones(evento, nombre, sigla, fechaInicio, fechaFin, fechaAlta, usuario, ciudad, pais);
                evento.agregarEdicion(nuevaEdicion);
                manejador.agregarEdicion(nuevaEdicion);
                System.out.println(" da de alta la edicion" + nombre );
                mUsuario.findOrganizador(usuario.getNickname()).agregarEdicion(nuevaEdicion);
            }
            else throw new EdicionYaExisteException(nombre);
        }
        else throw new EventoYaExisteException(evento.getNombre());
    }

    public DTEdicion consultaEdicionEvento(String siglaEvento, String siglaEdicion) {
        ManejadorEvento manejador = ManejadorEvento.getInstancia();
        Eventos evento = manejador.obtenerEvento(siglaEvento);
        if (evento == null) return null;
        Ediciones edicion = evento.obtenerEdicion(siglaEdicion);
        if (edicion == null) return null;
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
        return evento;
    }

    public void altaRegistroEdicionEvento(String idRegistro, Usuario usuario, Eventos evento, Ediciones edicion, TipoRegistro tipoRegistro, LocalDate fechaRegistro, float costo, LocalDate fechaInicio) {
        ManejadorEvento manejadorEvento = ManejadorEvento.getInstancia();
        if (usuario.esAsistente(usuario)) {
            if (edicion == null) {
                throw new RuntimeException("No se encontró la edición especificada.");
            }
            if (tipoRegistro == null) {
                throw new RuntimeException("No se encontró el tipo de registro especificado para la edición.");
            }
            boolean yaRegistrado = false;
            for (Registro reg : manejadorEvento.obtenerRegistros().values()) {
                if (reg.getUsuario().equals(usuario) && reg.getEdicion().equals(edicion)) {
                    yaRegistrado = true;
                    break;
                }
            }
            int cantidadRegistrados = 0;
            for (Registro reg : manejadorEvento.obtenerRegistros().values()) {
                if (reg.getTipoRegistro().equals(tipoRegistro) && reg.getEdicion().equals(edicion)) {
                    cantidadRegistrados++;
                }
            }
            if (yaRegistrado) {
                return;
            }
            if (cantidadRegistrados >= tipoRegistro.getCupo()) {
                return;
            }
            Registro nuevoRegistro = new Registro(idRegistro, usuario, edicion, tipoRegistro, fechaRegistro, costo, fechaInicio);
            manejadorEvento.agregarRegistro(nuevoRegistro);
            Asistente asist = (Asistente) usuario;
            asist.addRegistro(idRegistro, nuevoRegistro);
        	
        	
        	
        	
        }else {
        	 System.out.println("es organizador");
        }
    }
  
    public List<DTEvento> listarEventos() {

	    Map<String, Eventos> eventos = manejador.obtenerEventos();

	    List<DTEvento> lista = new ArrayList<>();
	    for (Eventos e : eventos.values()) {
	        lista.add(new DTEvento(
	            e.getNombre(),
	            e.getSigla(),
	            e.getDescripcion(),
	            e.getFecha(),
	            new ArrayList<>(e.getCategorias().keySet()),
	            new ArrayList<>(e.getEdiciones().keySet())
	        ));
	    }
	    return lista;
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

    
    public DTRegistro consultaRegistro(Usuario u, String idRegistro) {
        if (!(u instanceof Asistente)) {
            throw new UsuarioNoEsAsistente(u.getNickname());
        }

        Asistente a = (Asistente) u;
        Registro r = a.getRegistros().get(idRegistro);

        if (r == null) {
            throw new RegistroNoExiste(idRegistro);
        }

        return new DTRegistro(r.getId(), u.getNombre(), r.getEdicion().getNombre(), r.getTipoRegistro().getNombre(), r.getFechaRegistro(), r.getCosto(), r.getFechaInicio());
    }
}