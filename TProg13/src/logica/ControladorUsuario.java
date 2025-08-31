package logica;

import excepciones.UsuarioYaExisteException;
import excepciones.InstitucionYaExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioTipoIncorrectoException;
import excepciones.CategoriaYaExisteException;

import java.util.Map;
import java.util.Set;
import java.time.LocalDate;
import java.util.HashSet;

public class ControladorUsuario implements IControladorUsuario {
	
	manejadorUsuario manejador = manejadorUsuario.getInstancia();
	ManejadorEvento manejadorEv = ManejadorEvento.getInstancia();
    manejadorAuxiliar manejadorAux = manejadorAuxiliar.getInstancia();
	
	public Organizador ingresarOrganizador(String nickname, String nombre, String email, String desc, String link) {
        
		return new Organizador(nickname, nombre, email, desc, link);
    }

    public Asistente ingresarAsistente(String nickname, String nombre, String email, String apellido,
                                       LocalDate fechaDeNacimiento, Institucion institucion) {
        return new Asistente(nickname, nombre, email, apellido, fechaDeNacimiento, institucion);
    }

    public void AltaUsuario(String nickname, String nombre, String correo, String descripcion, String link,
                            String apellido, LocalDate fechaNacimiento, String institucion, boolean esOrganizador) throws UsuarioYaExisteException {



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
        	Institucion inst = manejador.findInstitucion(institucion);
            nuevoUsuario = ingresarAsistente(nickname, nombre, correo, apellido, fechaNacimiento, inst);
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

    public Map<String, Asistente> listarAsistentes() {
        return manejador.getAsistentes();
    }

    public Map<String, Organizador> listarOrganizadores() {
        return manejador.getOrganizadores();
    }
    
    public Set<String> getInstituciones(){
    	return manejador.getInstituciones();
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
        System.out.println(" llega a obtener datos" );
        // Datos específicos
        if (u instanceof Asistente) {
            Asistente a = (Asistente) u;
            dto.setApellido(a.getApellido());
            dto.setFechaNac(a.getFechaDeNacimiento());
            Map<String, Registro> registros = a.getRegistros();
            System.out.println(" llega a es asistente" );
            
            // Ejemplo de llamada a detalle de un registro seleccionado:
            for (Registro reg : registros.values()) {
            	System.out.println("Entra para registro " + reg.getId());
            	DTRegistro detalle = obtenerDatosRegistros(reg.getId());
            	dto.addRegistro(detalle);
            }

        } else if (u instanceof Organizador) {
            Organizador o = (Organizador) u;
            dto.setDesc(o.getDesc());
            dto.setLink(o.getLink());
            System.out.println(" llega a es org" );
            dto.setEdicion(listarEdicionesAPartirDeOrganizador(o));
//            Set<DTEdicion> ediciones = listarEdicionesAPartirDeOrganizador(o);
//            	// Ejemplo de llamada a detalle de una edición seleccionada:
//                for (DTEdicion dtEd : ediciones) {
//                      DTEdicion detalle =  obtenerDatosEdicionEvento(dtEd.getNombre());
//                      dto.addEdicion(detalle);
//                }
        }

        return dto;
    }
    public static Set<DTEdicion> listarEdicionesAPartirDeOrganizador(Organizador o) {
        Set<DTEdicion> lista = new HashSet<>();
        System.out.println(" llega a listarEdiciones" );
        // Recorremos el Map de ediciones del organizador
        if (o.getEdiciones().isEmpty()) {
        	System.out.println(" no tiene ediciones" );
        }
        for (Ediciones e : o.getEdiciones().values()) {
        	System.out.println("Entra para edicion" + e.getNombre());
        	lista.add(new DTEdicion(
                e.getNombre(),
                e.getSigla(),
                e.getFechaInicio(),
                e.getFechaFin(),
                e.getFechaAlta(),
                o.getNombre(), // nombre del organizador
                e.getCiudad(),
                e.getPais()));
        }
        return lista;
    }
    
    
    public void ConsultaUsuario(String nickname) throws UsuarioNoExisteException {
        Usuario u = manejador.findUsuario(nickname);

        if (u == null) {
            throw new UsuarioNoExisteException(nickname);
        }

        // Datos básicos
        String nick = u.getNickname();
        String nombre = u.getNombre();
        String correo = u.getEmail();

        if (u instanceof Organizador o) {
            Set<DTEdicion> ediciones = listarEdicionesAPartirDeOrganizador(o);

            // Ejemplo de llamada a detalle de una edición seleccionada:
            for (DTEdicion dtEd : ediciones) {
            	DTEdicion detalle = consultaEdicionEvento(dtEd.getNombre());
                // ahora podés exponer o almacenar el detalle
            }

        } else if (u instanceof Asistente a) {
            Map<String, Registro> registros = a.getRegistros();
            // Ejemplo de llamada a detalle de un registro seleccionado:
            for (Registro reg : registros.values()) {
                DTRegistro detalle = consultaRegistro(reg.getId());
                // ahora podés exponer o almacenar el detalle
            }
        }
    }


private DTRegistro obtenerDatosRegistros(String id) {
	// TODO Auto-generated method stub
	DTRegistro dto = null;
	if (manejadorEv.existeRegistro(id)) {
		Registro reg = manejadorEv.obtenerRegistro(id);
		dto = new DTRegistro(reg.getId(),reg.getUsuario().getNickname(),reg.getEdicion().getNombre(),reg.getTipoRegistro().getNombre(),reg.getFechaRegistro(),reg.getCosto(),reg.getFechaInicio());
	}
	return dto;
}

    
private DTRegistro consultaRegistro(String id) {
	return null;
	}

private DTEdicion consultaEdicionEvento(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

public void AltaCategoriaSinGUI(String nombre) throws CategoriaYaExisteException {

    if (manejadorAux.existeCategoria(nombre)) {
        throw new CategoriaYaExisteException(nombre);
    }

    Categoria c = new Categoria(nombre);
    manejadorAux.agregarCategoria(c.getNombre(), c);
}

    // Permite obtener una Institucion por su nombre directamente
    public Institucion getInstitucionPorNombre(String nombre) {
        return manejador.findInstitucion(nombre);
    }
	
}