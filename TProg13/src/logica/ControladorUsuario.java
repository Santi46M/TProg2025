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
import java.time.LocalDate;

public class ControladorUsuario implements IControladorUsuario {
	
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
                            String apellido, LocalDate fechaNacimiento, Institucion institucion, boolean esOrganizador) throws UsuarioYaExisteException {

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

    public void AltaInstitucion(String nombre, String descripcion, String link) throws InstitucionYaExisteException {

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

    public void actualizarAsistente(String nickname, String apellido, LocalDate fechaNacimiento) throws UsuarioYaExisteException, UsuarioTipoIncorrectoException {

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

    public void actualizarOrganizador(String nickname, String desc, String link) throws UsuarioNoExisteException, UsuarioTipoIncorrectoException {

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

    public DTDatosUsuario obtenerDatosUsuario(String nickname) throws UsuarioNoExisteException {

    	Usuario u = manejador.findUsuario(nickname);

        if (u == null) {
            throw new UsuarioNoExisteException(nickname);
        }

        // Datos básicos
        DTDatosUsuario dto = new DTDatosUsuario(u.getNickname(), u.getNombre(), u.getEmail());

        // Datos específicos
        if (u instanceof Asistente) {
            Asistente a = (Asistente) u;
            dto.setApellido(a.getApellido());
            dto.setFechaNac(a.getFechaDeNacimiento());
//          Map<String, Registro> registros = a.getRegistros();
//
//          // Ejemplo de llamada a detalle de un registro seleccionado:
//          for (Registro reg : registros.values()) {
//              DTRegistro detalle = consultaRegistro(reg.getId());
//              // ahora podés exponer o almacenar el detalle
//          }

        } else if (u instanceof Organizador) {
            Organizador o = (Organizador) u;
            dto.setDesc(o.getDesc());
            dto.setLink(o.getLink());
//          List<DTEdicionEvento> ediciones = listarEdicionesAPartirDeOrganizador(o);
          //
//                      // Ejemplo de llamada a detalle de una edición seleccionada:
//                      for (DTEdicionEvento dtEd : ediciones) {
//                          DTEdicionEvento detalle = consultaEdicionEvento(dtEd.getNombre());
//                          // ahora podés exponer o almacenar el detalle
//                      }
        }

        return dto;
    }



public void ConsultaUsuario(String nickname) throws UsuarioNoExisteException {

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
private List<DTEdicionEvento> listarEdicionesAPartirDeOrganizador(Organizador o) {
	// TODO Auto-generated method stub
	return null;
}



public void AltaCategoriaSinGUI(String nombre) throws CategoriaYaExisteException {

    if (manejadorAux.findCategoria(nombre) != null) {
        throw new CategoriaYaExisteException(nombre);
    }

    Categoria c = new Categoria(nombre);
    manejadorAux.addCategoria(c.getNombre());
}

	
}