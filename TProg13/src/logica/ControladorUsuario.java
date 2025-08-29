package logica;

import excepciones.UsuarioYaExisteException;
import excepciones.InstitucionYaExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioTipoIncorrectoException;
import excepciones.CategoriaSinSeleccionarException;
import excepciones.CategoriaYaExisteException;
import logica.Asistente;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logica.manejadorUsuario;
import java.time.LocalDate;


public class ControladorUsuario {
	
	manejadorUsuario manejador = manejadorUsuario.getInstancia();
    manejadorAuxiliar manejadorAux = manejadorAuxiliar.getInstancia();
	
	public Organizador ingresarOrganizador(String nickname, String nombre, String email, String desc, String link) {
        return new Organizador(nickname, nombre, email, desc, link);
    }

    public Asistente ingresarAsistente(String nickname, String nombre, String email, String apellido,
                                       LocalDate fechaDeNacimiento, Institucion institucion) {
        return new Asistente(nickname, nombre, email, apellido, fechaDeNacimiento, institucion);
    }

    public void AltaUsuario(String nickname, String nombre, String correo, String descripcion, String link,
                            String apellido, LocalDate fechaNacimiento, Institucion institucion, boolean esOrganizador) {

        manejadorUsuario manejador = manejadorUsuario.getInstancia();

        // verificar unicidad de nickname y correo
        if (manejador.findUsuario(nickname) != null) {
            throw new UsuarioYaExisteException(nickname);
        }
        if (manejador.findCorreo(nickname, correo) != null) {
            throw new UsuarioYaExisteException(correo);
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
        manejador.addUsuario(nuevoUsuario);
    }

    public void AltaInstitucion(String nombre, String descripcion, String link) {

        if (manejador.findInstitucion(nombre) != null) {
            throw new InstitucionYaExisteException(nombre);
        }

        Institucion nuevaInstitucion = new Institucion(nombre, descripcion, link);
        manejador.addInstitucion(nuevaInstitucion);
    }

    public Map<String, Usuario> listarUsuarios() {
        return manejador.getUsuarios();
    }

    public Map<String, Usuario> listarAsistentes() {
        return manejador.getAsistentes();
    }

    public Map<String, Usuario> listarOrganizadores() {
        return manejador.getOrganizadores();
    }

    public void actualizarAsistente(String nickname, String apellido, LocalDate fechaNacimiento) {

        Usuario u = manejador.findUsuario(nickname);

        if (u == null) {
            throw new UsuarioYaExisteException(nickname);
        }

        if (!(u instanceof Asistente)) {
            throw new UsuarioTipoIncorrectoException(nickname);
        }

        Asistente a = (Asistente) u; // casting
        a.setApellido(apellido);
        a.setFechaDeNacimiento(fechaNacimiento);
    }

    public void actualizarOrganizador(String nickname, String desc, String link) {

        Usuario u = manejador.findUsuario(nickname);

        if (u == null) {
            throw new UsuarioNoExisteException(nickname);
        }

        if (!(u instanceof Organizador)) {
            throw new UsuarioTipoIncorrectoException(nickname);
        }

        Organizador o = (Organizador) u;
        o.setDesc(desc);
        o.setLink(link);
    }

    public DTDatosUsuario obtenerDatosUsuario(String nickname) {

        Usuario u = manejador.findUsuario(nickname);

        if (u == null) {
            throw new UsuarioNoExisteException(nickname);
        }

        return new DTDatosUsuario();
    }

    public void AltaCategoriaSinGUI(String nombre) {

        if (manejadorAux.findCategoria(nombre) != null) {
            throw new CategoriaYaExisteException(nombre);
        }

        Categoria c = new Categoria(nombre);
        manejadorAux.addCategoria(c.getNombre());
    }



public void ConsultaUsuario(String nickname) {

    Usuario u = manejador.findUsuario(nickname);

    if (u == null) {
        throw new UsuarioNoExisteException(nickname);
    }

    // Datos básicos toto se la re come
    String nick = u.getNickname();
    String nombre = u.getNombre();
    String correo = u.getEmail();

    if (u instanceof Organizador) {
        Organizador o = (Organizador) u;
        String descripcion = o.getDesc();
        String link = o.getLink();

        List<DTEdicionEvento> ediciones = listarEdicionesAPartirDeOrganizador(o);

        // Retornás o almacenás estos datos en un DTO si querés exponerlos
    } else if (u instanceof Asistente) {
        Asistente a = (Asistente) u;
        String apellido = a.getApellido();
        LocalDate fechaNacimiento = a.getFechaDeNacimiento();

        Map<String, Registro> registros = a.getRegistros();
}
    
    //falta la parte de seleccionar administrador en ConsultaUsuario

    
    	
    }

	
}