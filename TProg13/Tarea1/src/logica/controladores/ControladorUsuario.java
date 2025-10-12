package logica.controladores;

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

import logica.clases.Asistente;
import logica.clases.Categoria;
import logica.clases.Ediciones;
import logica.clases.Institucion;
import logica.clases.Organizador;
import logica.clases.Registro;
import logica.clases.Usuario;
import logica.datatypes.DTDatosUsuario;
import logica.datatypes.DTEdicion;
import logica.datatypes.DTRegistro;
import logica.interfaces.IControladorUsuario;
import logica.manejadores.ManejadorEvento;
import logica.manejadores.manejadorAuxiliar;
import logica.manejadores.manejadorUsuario;

public class ControladorUsuario implements IControladorUsuario {
	
    private manejadorUsuario manejador = manejadorUsuario.getInstancia();
    private ManejadorEvento manejadorEv = ManejadorEvento.getInstancia();
    private manejadorAuxiliar manejadorAux = manejadorAuxiliar.getInstancia();

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

    public void altaUsuario(String nickname, String nombre, String correo, String descripcion, String link,
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

    public void altaInstitucion(String nombre, String descripcion, String link) throws InstitucionYaExisteException {
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
        Usuario user = manejador.findUsuario(nickname);
        if (user == null) {
            throw new UsuarioNoExisteException(nickname);
        }
        if (!(user instanceof Asistente)) {
            throw new UsuarioTipoIncorrectoException(nickname);
        }
        Asistente asistUser = (Asistente) user;
        asistUser.setApellido(apellido);
        asistUser.setFechaDeNacimiento(fechaNacimiento);
    }

    public void actualizarOrganizador(String nickname, String desc, String link) throws UsuarioNoExisteException, UsuarioTipoIncorrectoException {
        Usuario user = manejador.findUsuario(nickname);
        if (user == null) {
            throw new UsuarioNoExisteException(nickname);
        }
        if (!(user instanceof Organizador)) {
            throw new UsuarioTipoIncorrectoException(nickname);
        }
        Organizador orgUser = (Organizador) user;
        orgUser.setDesc(desc);
        orgUser.setLink(link);
    }

    public DTDatosUsuario obtenerDatosUsuario(String nickname) throws UsuarioNoExisteException {
        Usuario user = manejador.findUsuario(nickname);
        if (user == null) {
            throw new UsuarioNoExisteException(nickname);
        }

        // guardamos el usuario consultado
        this.usuarioSeleccionadoNickname = nickname;

        DTDatosUsuario dto = new DTDatosUsuario(user.getNickname(), user.getNombre(), user.getEmail());
        if (user instanceof Asistente) {
            Asistente asisUser = (Asistente) user;
            dto.setApellido(asisUser.getApellido());
            dto.setFechaNac(asisUser.getFechaDeNacimiento());
            dto.setRegistros(obtenerRegistrosAsistente(asisUser));
            dto.setInstitucion(obtenerInstitucion(asisUser));
        } else if (user instanceof Organizador) {
            Organizador orgUser = (Organizador) user;
            dto.setDesc(orgUser.getDesc());
            dto.setLink(orgUser.getLink());
            dto.setEdicion(listarEdicionesAPartirDeOrganizador(orgUser));
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

    public static Set<DTEdicion> listarEdicionesAPartirDeOrganizador(Organizador orgUser) {
        Set<DTEdicion> lista = new HashSet<>();
        for (Ediciones edicionIter : orgUser.getEdiciones().values()) {
            lista.add(new DTEdicion(
                edicionIter.getNombre(),
                edicionIter.getSigla(),
                edicionIter.getFechaInicio(),
                edicionIter.getFechaFin(),
                edicionIter.getFechaAlta(),
                orgUser.getNombre(),
                edicionIter.getCiudad(),
                edicionIter.getPais(),
                edicionIter.getEstado()));
        }
        return lista;
    }
    
    public void consultaUsuario(String nickname) throws UsuarioNoExisteException {
        Usuario user = manejador.findUsuario(nickname);
        if (user == null) {
            throw new UsuarioNoExisteException(nickname);
        }
        this.usuarioSeleccionadoNickname = nickname;
        // Lógica adicional según caso de uso
    }

    public DTRegistro obtenerDatosRegistros(String identificador) {
        DTRegistro dto = null;
        if (manejadorEv.existeRegistro(identificador)) {
            Registro reg = manejadorEv.obtenerRegistro(identificador);
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
    public void seleccionarRegistro(String ident) {
        if (!manejadorEv.existeRegistro(ident)) {
            throw new RegistroNoExiste(ident);
        }
        this.registroSeleccionadoId = ident;
    }

    public String getRegistroSeleccionadoId() {
        return registroSeleccionadoId;
    }

    // --- manejo de selección de Usuario ---
    public String getUsuarioSeleccionadoNickname() {
        return usuarioSeleccionadoNickname;
    }

    public void altaCategoriaSinGUI(String nombre) throws CategoriaYaExisteException {
        if (manejadorAux.existeCategoria(nombre)) {
            throw new CategoriaYaExisteException(nombre);
        }
        Categoria catIter = new Categoria(nombre);
        manejadorAux.agregarCategoria(catIter.getNombre(), catIter);
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
        Usuario user = manejador.findUsuario(nickname);
        if (user == null) {
            throw new UsuarioNoExisteException(nickname);
        }
        user.setNombre(nombre);
        if (user instanceof Organizador) {
            Organizador orgUser = (Organizador) user;
            orgUser.setDesc(descripcion);
            orgUser.setLink(link);
        } else if (user instanceof Asistente) {
            Asistente asisUser = (Asistente) user;
            asisUser.setApellido(apellido);
            asisUser.setFechaDeNacimiento(fechaNacimiento);
            if (institucion != null && !institucion.isEmpty()) {
                Institucion inst = manejador.findInstitucion(institucion);
                asisUser.setInstitucion(inst);
            }
        } else {
            throw new UsuarioTipoIncorrectoException(nickname);
        }
    }
    

    public boolean inicioSesion(String nickOrEmail, String contrasena) {
        Usuario usuario = manejador.findUsuario(nickOrEmail);
        if (usuario == null) {
            for (Usuario user : manejador.getUsuarios().values()) {
                if (user.getEmail().equals(nickOrEmail)) {
                    usuario = user;
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
        manejadorUsuario manejadorUser = manejadorUsuario.getInstancia();

        // Buscar por nickname o por correo electrónico
        Usuario user = manejadorUser.obtenerUsuarioPorNickOEmail(nickOrEmail);

        // Si no existe, devolvemos false directamente
        if (user == null) {
            return false;
        }

        // Si la contraseña está vacía o no coincide → false
        if (user.getContrasena() == null || !user.getContrasena().equals(contrasena)) {
            return false;
        }

        // Si llegó acá → es válido
        return true;
    }
	}
