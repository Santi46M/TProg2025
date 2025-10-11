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
import excepciones.CupoTipoRegistroInvalidoException;
import excepciones.CostoTipoRegistroInvalidoException;

import logica.Manejadores.*;
import logica.Interfaces.IControladorEvento;
import logica.Clases.*;
import logica.Datatypes.*;
import logica.Enumerados.*;

public class ControladorEvento implements IControladorEvento {
    ManejadorEvento manejador = ManejadorEvento.getInstancia();
    manejadorUsuario mUsuario = manejadorUsuario.getInstancia();

    private String edicionSeleccionadaSigla = null;

    public void AltaEvento(String nombre, String desc, LocalDate fechaDeAlta, String sigla, DTCategorias categorias, String imagen) throws EventoYaExisteException {
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
        Eventos nuevoEvento = new Eventos(nombre, sigla, desc, fechaDeAlta, categoriasMap, imagen);
        manejador.agregarEvento(nuevoEvento);
    }

    public void AltaTipoRegistro(Ediciones edicion, String nombre, String descripcion, float costo, int cupo) throws TipoRegistroYaExisteException, CupoTipoRegistroInvalidoException, CostoTipoRegistroInvalidoException {
        if (edicion.obtenerTipoRegistro(nombre) != null) {
            throw new TipoRegistroYaExisteException(nombre);
        }
        if (cupo <= 0 || cupo > Integer.MAX_VALUE) {
            throw new CupoTipoRegistroInvalidoException(cupo);
        }
        if (costo < 0 || costo > Float.MAX_VALUE) {
            throw new CostoTipoRegistroInvalidoException(costo);
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
        float valorRegistros = cantidadRegistros * tipoRegistro.getCosto();
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

    public void AltaEdicionEvento(Eventos evento, Usuario usuario, String nombre, String sigla, String desc, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta, String ciudad, String pais, String imagen) throws EdicionYaExisteException, EventoYaExisteException, FechasCruzadasException {
        ManejadorEvento manejador = ManejadorEvento.getInstancia();
        if (fechaInicio.isAfter(fechaFin)) {
            throw new FechasCruzadasException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }
        if (manejador.existeEvento(evento.getNombre())) {
            if (!manejador.existeEdicion(nombre)) {
                Ediciones nuevaEdicion = new Ediciones(evento, nombre, sigla, fechaInicio, fechaFin, fechaAlta, usuario, ciudad, pais, imagen);
                evento.agregarEdicion(nuevaEdicion);
                manejador.agregarEdicion(nuevaEdicion);
                mUsuario.findOrganizador(usuario.getNickname()).agregarEdicion(nuevaEdicion);
            } else {
                throw new EdicionYaExisteException("El nombre de la edición " + nombre + " ya está en uso.");
            }
        } else {
            throw new EventoYaExisteException(evento.getNombre());
        }
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
        return manejador.obtenerEvento(nombreEvento);
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
                throw new RuntimeException("El usuario ya está registrado a esta edición.");
            }
            if (cantidadRegistrados >= tipoRegistro.getCupo()) {
                throw new excepciones.CupoTipoRegistroInvalidoException(tipoRegistro.getCupo());
            }
            Registro nuevoRegistro = new Registro(idRegistro, usuario, edicion, tipoRegistro, fechaRegistro, costo, fechaInicio);
            manejadorEvento.agregarRegistro(nuevoRegistro);
            edicion.agregarRegistro(idRegistro, nuevoRegistro); // <-- Agrega el registro a la edición
            Asistente asist = (Asistente) usuario;
            asist.addRegistro(idRegistro, nuevoRegistro);
        } else {

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
        return new DTRegistro(
            r.getId(),
            u.getNombre(),
            r.getEdicion().getNombre(),
            r.getTipoRegistro().getNombre(),
            r.getFechaRegistro(),
            r.getCosto(),
            r.getFechaInicio()
        );
    }

    // --- selección de edición para consultas ---
    public void seleccionarEdicion(String sigla) {
        Ediciones ed = manejador.obtenerEdicion(sigla);
        if (ed == null) {
            throw new RuntimeException("No existe la edición con sigla: " + sigla);
        }
        this.edicionSeleccionadaSigla = sigla;
    }

    public String getEdicionSeleccionadaSigla() {
        return edicionSeleccionadaSigla;
    }

    public DTEdicion obtenerEdicionSeleccionada() {
        if (edicionSeleccionadaSigla == null) return null;
        Ediciones ed = manejador.obtenerEdicion(edicionSeleccionadaSigla);
        if (ed == null) return null;
        return new DTEdicion(
            ed.getNombre(),
            ed.getSigla(),
            ed.getFechaInicio(),
            ed.getFechaFin(),
            ed.getFechaAlta(),
            ed.getOrganizador() != null ? ed.getOrganizador().getNickname() : null,
            ed.getCiudad(),
            ed.getPais()
        );
    }
    @Override
    public Ediciones obtenerEdicionPorSigla(String sigla) {
        return manejador.obtenerEdicion(sigla);
    }
    
    @Override
    public String encontrarEventoPorSigla(String siglaEdicion) {
        Ediciones ed = manejador.obtenerEdicion(siglaEdicion);
        if (ed != null && ed.getEvento() != null) {
            return ed.getEvento().getNombre();
        }
        return null;
    }
    
    public List<String> listarEventosConEdicionesIngresadas() {
        List<String> resultado = new ArrayList<>();
        for (Eventos e : manejador.obtenerEventos().values()) {
            for (Ediciones ed : e.getEdiciones().values()) {
                if (ed.getEstado() == DTEstado.Ingresada) {
                    resultado.add(e.getNombre());
                    break;
                }
            }
        }
        return resultado;
    }

    public List<String> listarEdicionesIngresadasDeEvento(String nombreEvento) {
        Eventos evento = manejador.obtenerEvento(nombreEvento);
        List<String> resultado = new ArrayList<>();
        if (evento != null) {
            for (Ediciones ed : evento.getEdiciones().values()) {
                if (ed.getEstado() == DTEstado.Ingresada) {
                    resultado.add(ed.getNombre());
                }
            }
        }
        return resultado;
    }

    public void aceptarRechazarEdicion(Ediciones edicion, boolean aceptar) {
        if (edicion != null) {
            edicion.setEstado(aceptar ? DTEstado.Aceptada : DTEstado.Rechazada);
        }
    }
    
    public void cambiarEstadoEdicion(Eventos evento, Ediciones edicion, boolean aceptar) {
    			if (evento != null && edicion != null) {
			edicion.setEstado(aceptar ? DTEstado.Aceptada : DTEstado.Rechazada);
		}
    }
}