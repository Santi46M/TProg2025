package logica.Controladores;

import excepciones.UsuarioYaExisteException;
import excepciones.InstitucionYaExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioTipoIncorrectoException;
import excepciones.CategoriaYaExisteException;
import excepciones.RegistroNoExiste;

import java.util.Map;
import java.util.Set;
import java.time.LocalDate;
import java.util.HashSet;

import logica.Manejadores.*;
import logica.Interfaces.IControladorUsuario;
import logica.Clases.*;
import logica.Datatypes.*;

public class ControladorUsuario implements IControladorUsuario {
	
    manejadorUsuario manejador = manejadorUsuario.getInstancia();
    ManejadorEvento manejadorEv = ManejadorEvento.getInstancia();
    manejadorAuxiliar manejadorAux = manejadorAuxiliar.getInstancia();

    // --- estado de selección para consultas ---
    private String usuarioSeleccionadoNickname = null;
    private String registroSeleccionadoId = null;

    public Organizador ingresarOrganizador(String nickname, String nombre, String email, String contrasena, String imagen,  String desc, String link) {
        return new Organizador(nickname, nombre, email, contrasena, imagen, desc, link);
    }

    public Asistente ingresarAsistente(String nickname, String nombre, String email, String contrasena, String imagen, String apellido,
                                       LocalDate fechaDeNacimiento, Institucion institucion) {
        return new Asistente(nickname, nombre, email, contrasena, imagen, apellido, fechaDeNacimiento, institucion);
    }

    public void AltaUsuario(String nickname, String nombre, String correo, String descripcion, String link,
                            String apellido, LocalDate fechaNacimiento, String institucion, boolean esOrganizador, String contrasena, String imagen) throws UsuarioYaExisteException {

        // verificar unicidad de nickname y correo
        if (manejador.findUsuario(nickname) != null) {
            throw new UsuarioYaExisteException("El usuario con nickname " + nickname + " ya esta registrado");
        }
        if (manejador.findCorreo(correo)) {
        	throw new UsuarioYaExisteException("El usuario con correo " + correo + " ya esta registrado");

        }

        Usuario nuevoUsuario;

        if (esOrganizador) {
            nuevoUsuario = new Organizador(nickname, nombre, correo, contrasena, imagen, descripcion, link);
        } else {
            Institucion inst = manejador.findInstitucion(institucion);
            nuevoUsuario = new Asistente(nickname, nombre, correo, contrasena, imagen, apellido, fechaNacimiento, inst);
        }

        manejador.addUsuario(nuevoUsuario);
    }

    public void AltaInstitucion(String nombre, String descripcion, String link) throws InstitucionYaExisteException {
        if (manejador.findInstitucion(nombre) != null) {
            throw new InstitucionYaExisteException("La institución " + nombre + " ya existe");        
            }

        Institucion nuevaInstitucion = new Institucion(nombre, descripcion, link);
        manejador.addInstitucion(nuevaInstitucion);
    }

    public Map<String, Usuario> listarUsuarios() {
        return manejador.getUsuarios();
    }

    public Map<String, Asistente> listarAsistentes() {
        return manejador.getAsistentes();
    }

    public Map<String, Organizador> listarOrganizadores() {
        return manejador.getOrganizadores();
    }
    
    public Set<String> getInstituciones(){
        return manejador.getInstituciones();
    }

    public void actualizarAsistente(String nickname, String apellido, LocalDate fechaNacimiento) throws UsuarioYaExisteException, UsuarioTipoIncorrectoException, UsuarioNoExisteException {
        Usuario u = manejador.findUsuario(nickname);
        if (u == null) {
            throw new UsuarioNoExisteException(nickname);
        }
        if (!(u instanceof Asistente)) {
            throw new UsuarioTipoIncorrectoException(nickname);
        }
        Asistente a = (Asistente) u;
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

        // guardamos el usuario consultado
        this.usuarioSeleccionadoNickname = nickname;

        DTDatosUsuario dto = new DTDatosUsuario(u.getNickname(), u.getNombre(), u.getEmail());
        if (u instanceof Asistente) {
            Asistente a = (Asistente) u;
            dto.setApellido(a.getApellido());
            dto.setFechaNac(a.getFechaDeNacimiento());
            dto.setRegistros(obtenerRegistrosAsistente(a));
            dto.setInstitucion(obtenerInstitucion(a));
        } else if (u instanceof Organizador) {
            Organizador o = (Organizador) u;
            dto.setDesc(o.getDesc());
            dto.setLink(o.getLink());
            dto.setEdicion(listarEdicionesAPartirDeOrganizador(o));
        }
        return dto;
    }
    
    public String obtenerInstitucion(Asistente asist) {
    	Institucion inst = asist.getInstitucion();
    	if (inst != null) {
    		String nombreInstitucion = inst.getNombre();
    		return nombreInstitucion;
    	}
    	return null;
    }
    
    public Set<DTRegistro> obtenerRegistrosAsistente(Asistente asist){
        Set<DTRegistro> dtr = new HashSet<>();
        Map<String, Registro> registros = asist.getRegistros();
        for (Registro reg : registros.values()) {
            DTRegistro detalle = obtenerDatosRegistros(reg.getId());
            dtr.add(detalle);
        }
        return dtr;
    }

    public static Set<DTEdicion> listarEdicionesAPartirDeOrganizador(Organizador o) {
        Set<DTEdicion> lista = new HashSet<>();
        for (Ediciones e : o.getEdiciones().values()) {
            lista.add(new DTEdicion(
                e.getNombre(),
                e.getSigla(),
                e.getFechaInicio(),
                e.getFechaFin(),
                e.getFechaAlta(),
                o.getNombre(),
                e.getCiudad(),
                e.getPais(),
                e.getEstado()));
        }
        return lista;
    }
    
    public void ConsultaUsuario(String nickname) throws UsuarioNoExisteException {
        Usuario u = manejador.findUsuario(nickname);
        if (u == null) {
            throw new UsuarioNoExisteException(nickname);
        }
        this.usuarioSeleccionadoNickname = nickname;
        // Lógica adicional según caso de uso
    }

    public DTRegistro obtenerDatosRegistros(String id) {
        DTRegistro dto = null;
        if (manejadorEv.existeRegistro(id)) {
            Registro reg = manejadorEv.obtenerRegistro(id);
            dto = new DTRegistro(
                reg.getId(),
                reg.getUsuario().getNickname(),
                reg.getEdicion().getNombre(),
                reg.getTipoRegistro().getNombre(),
                reg.getFechaRegistro(),
                reg.getCosto(),
                reg.getFechaInicio()
            );
        }
        return dto;
    }

    public boolean esAsistente(String nickname) {
        return listarAsistentes() != null && listarAsistentes().containsKey(nickname);
    }

    // --- manejo de selección de Registro ---
    public void seleccionarRegistro(String id) {
        if (!manejadorEv.existeRegistro(id)) {
            throw new RegistroNoExiste(id);
        }
        this.registroSeleccionadoId = id;
    }

    public String getRegistroSeleccionadoId() {
        return registroSeleccionadoId;
    }

    // --- manejo de selección de Usuario ---
    public String getUsuarioSeleccionadoNickname() {
        return usuarioSeleccionadoNickname;
    }

    public void AltaCategoriaSinGUI(String nombre) throws CategoriaYaExisteException {
        if (manejadorAux.existeCategoria(nombre)) {
            throw new CategoriaYaExisteException(nombre);
        }
        Categoria c = new Categoria(nombre);
        manejadorAux.agregarCategoria(c.getNombre(), c);
    }

    public Institucion getInstitucionPorNombre(String nombre) {
        return manejador.findInstitucion(nombre);
    }
    
    @Override
    public DTRegistro obtenerRegistroSeleccionado() {
        if (registroSeleccionadoId == null) {
            return null;
        }
        return obtenerDatosRegistros(registroSeleccionadoId);
    }

    public void modificarDatosUsuario(String nickname, String nombre, String descripcion, String link, String apellido, LocalDate fechaNacimiento, String institucion) throws UsuarioNoExisteException, UsuarioTipoIncorrectoException {
        Usuario u = manejador.findUsuario(nickname);
        if (u == null) {
            throw new UsuarioNoExisteException(nickname);
        }
        u.setNombre(nombre);
        if (u instanceof Organizador) {
            Organizador o = (Organizador) u;
            o.setDesc(descripcion);
            o.setLink(link);
        } else if (u instanceof Asistente) {
            Asistente a = (Asistente) u;
            a.setApellido(apellido);
            a.setFechaDeNacimiento(fechaNacimiento);
            if (institucion != null && !institucion.isEmpty()) {
                Institucion inst = manejador.findInstitucion(institucion);
                a.setInstitucion(inst);
            }
        } else {
            throw new UsuarioTipoIncorrectoException(nickname);
        }
    }
    

    public boolean inicioSesion(String nickOrEmail, String contrasena) {
        Usuario usuario = manejador.findUsuario(nickOrEmail);
        if (usuario == null) {
            for (Usuario u : manejador.getUsuarios().values()) {
                if (u.getEmail().equals(nickOrEmail)) {
                    usuario = u;
                    break;
                }
            }
        }
        if (usuario == null) {
            return false;
        }
        if (usuario.getContrasena() == null || !usuario.getContrasena().equals(contrasena)) {
            return false;
        }
        return true;
    }

    /**
     * Caso de uso: cierre de sesión. Por ahora no realiza ninguna acción.
     */
    public void cierreSesion() {
        // No hace nada por ahora
    }
    
    @Override
    public boolean validarLogin(String nickOrEmail, String contrasena) {
        manejadorUsuario mu = manejadorUsuario.getInstancia();

        // Buscar por nickname o por correo electrónico
        Usuario u = mu.obtenerUsuarioPorNickOEmail(nickOrEmail);

        // Si no existe, devolvemos false directamente
        if (u == null) {
            return false;
        }

        // Si la contraseña está vacía o no coincide → false
        if (u.getContrasena() == null || !u.getContrasena().equals(contrasena)) {
            return false;
        }

        // Si llegó acá → es válido
        return true;
    }
	}
