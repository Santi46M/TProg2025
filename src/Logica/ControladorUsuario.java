package com.miapp.controladores;

import modelos.*;
import java.util.ArrayList;
import java.util.List;
import manejadores.ManejadorUsuario;
import java.util.Date;


public class ControladorUsuario {
	
	public Organizador ingresarOrganizador(String nickname, String nombre, String email, String desc, String link) {
        return new Organizador(nickname, nombre, email, desc, link);
    }

    public Asistente ingresarAsistente(String nickname, String nombre, String email, String apellido,
                                       Date fechaDeNacimiento, Institucion institucion) {
        return new Asistente(nickname, nombre, email, apellido, fechaDeNacimiento, institucion);
    }

    public void AltaUsuario(String nickname, String nombre, String correo, String descripcion, String link,
                            String apellido, Date fechaNacimiento, Institucion institucion, boolean esOrganizador) {

        ManejadorUsuario manejador = ManejadorUsuario.getInstancia();

        // verificar unicidad de nickname y correo
        if (manejador.existeNickname(nickname)) {
            throw new UsuarioExisteException(nickname);
        }
        if (manejador.existeCorreo(correo)) {
            throw new CorreoExisteException(correo);
        }

        Usuario nuevoUsuario;

        if (esOrganizador) {
            // crear organizador
            nuevoUsuario = ingresarOrganizador(nickname, nombre, correo, descripcion, link);
        } else {
            // crear asistente
            nuevoUsuario = ingresarAsistente(nickname, nombre, correo, apellido, fechaNacimiento, institucion);
        }

        // lo agrego a la base de datos
        manejador.agregarUsuario(nuevoUsuario);
    }

    public void AltaInstitucion(String nombre, String descripcion, String link) {
        ManejadorUsuario manejador = ManejadorUsuario.getInstancia();

        if (manejador.existeInstitucion(nombre)) {
            throw new InstitucionExisteException(nombre);
        }

        Institucion nuevaInstitucion = new Institucion(nombre, descripcion, link);
        manejador.agregarInstitucion(nuevaInstitucion);
    }

    public Set<String> listarUsuarios() {
        return ManejadorUsuario.getInstancia().listarUsuarios();
    }

    public Set<String> listarAsistentes() {
        return ManejadorUsuario.getInstancia().listarAsistentes();
    }

    public Set<String> listarOrganizadores() {
        return ManejadorUsuario.getInstancia().listarOrganizadores();
    }

    public void actualizarAsistente(String nickname, String apellido, DTFecha fechaNacimiento) {
        ManejadorUsuario manejador = ManejadorUsuario.getInstancia();

        Usuario u = manejador.finduser(nickname);

        if (u == null) {
            throw new UsuarioNoExisteException(nickname);
        }

        if (!(u instanceof Asistente)) {
            throw new UsuarioTipoIncorrectoException(nickname, "Asistente");
        }

        Asistente a = (Asistente) u; // casting
        a.setApellido(apellido);
        a.setFechaNacimiento(fechaNacimiento);
    }

    public void actualizarOrganizador(String nickname, String desc, String link) {
        ManejadorUsuario manejador = ManejadorUsuario.getInstancia();

        Usuario u = manejador.obtenerUsuarioPorNickname(nickname);

        if (u == null) {
            throw new UsuarioNoExisteException(nickname);
        }

        if (!(u instanceof Organizador)) {
            throw new UsuarioTipoIncorrectoException(nickname, "Organizador");
        }

        Organizador o = (Organizador) u;
        o.setDescripcion(desc);
        o.setLink(link);
    }

    public DTDatosUsuario obtenerDatosUsuario(String nickname) {
        ManejadorUsuario manejador = ManejadorUsuario.getInstancia();

        Usuario u = manejador.obtenerUsuarioPorNickname(nickname);

        if (u == null) {
            throw new UsuarioNoExisteException(nickname);
        }

        return new DTDatosUsuario(nickname, u.getNombre(), u.getCorreo());
    }

    public void AltaCategoriaSinGUI(String nombre) {
        ManejadorAux manejador = ManejadorAux.getInstancia();

        if (manejador.existeCategoria(nombre)) {
            throw new CategoriaExisteException(nombre);
        }

        Categoria c = new Categoria(nombre);
        manejador.agregarCategoria(c);
    }
}


public void ConsultaUsuario(String nickname) {
    ManejadorUsuario manejador = ManejadorUsuario.getInstancia();

    Usuario u = manejador.obtenerUsuarioPorNickname(nickname);

    if (u == null) {
        throw new UsuarioNoExisteException(nickname);
    }

    // Datos básicos
    String nick = u.getNickname();
    String nombre = u.getNombre();
    String correo = u.getCorreo();

    if (u instanceof Organizador) {
        Organizador o = (Organizador) u;
        String descripcion = o.getDescripcion();
        String link = o.getLink();

        List<DTEdicionEvento> ediciones = listarEdicionesAPartirDeOrganizador(o);

        // Retornás o almacenás estos datos en un DTO si querés exponerlos
    } else if (u instanceof Asistente) {
        Asistente a = (Asistente) u;
        String apellido = a.getApellido();
        DTFecha fechaNacimiento = a.getFechaNacimiento();

        Map<String, Registro> registros = a.getRegistros();
}
    
    //falta la parte de seleccionar administrador en ConsultaUsuario




