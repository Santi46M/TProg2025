package logica.Manejadores;


import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;


import logica.Clases.Asistente;
import logica.Clases.Institucion;
import logica.Clases.Organizador;
import logica.Clases.Usuario;

public class manejadorUsuario {
	private static manejadorUsuario instancia; //singleton
	private Map<String, Usuario> usuarios = new HashMap<String, Usuario>();
	private Map<String, Asistente> asistentes = new HashMap<String, Asistente>();
	private Map<String, Organizador> organizadores = new HashMap<String, Organizador>();
	private Map<String, Institucion> institucionesMap = new HashMap<>();
	private Set<String> instituciones = new HashSet<String>();
	
	//// instancia de manejador singleton (no se si esta del todo bien)
	private manejadorUsuario() {
		 usuarios = new HashMap<>();
		 asistentes = new HashMap<>();
		 organizadores = new HashMap<>();
		 instituciones = new HashSet<>();
	}
	
	public static manejadorUsuario getInstancia() {
		if (instancia == null) {
			instancia = new manejadorUsuario();
		}
		return instancia;
	}
	
	public Map<String, Usuario> getUsuarios() {
		return this.usuarios;
	}
	
	public Map<String, Asistente> getAsistentes() {
		return this.asistentes;
	}
	
	public Map<String, Organizador> getOrganizadores() {
		return this.organizadores;
	}

	public Set<String> getInstituciones() {
		return this.instituciones;
	}
	public void addUsuario(Usuario u) {
		this.usuarios.put(u.getNickname(), u);
		if (u instanceof Asistente) {
			Asistente ast = findAsistente(u.getNickname());
			this.asistentes.put(ast.getNickname(), ast);
		}else {
			Organizador org = findOrganizador(u.getNickname());
			this.organizadores.put(org.getNickname(), org);
		}
	}
	public void addInstitucion(Institucion i) {
		this.instituciones.add(i.getNombre());
		this.institucionesMap.put(i.getNombre(), i);
	}
	public Usuario findUsuario(String nickname) {
		return usuarios.get(nickname);
	}
	
	public Organizador findOrganizador(String nickname) {
	    Usuario u = usuarios.get(nickname);
	    if (u instanceof Organizador) {
	        return (Organizador) u;
	    }
	    return null; // o podés tirar una excepción si preferís
	}
	public Asistente findAsistente(String nickname) {
	    Usuario u = usuarios.get(nickname);
	    if (u instanceof Asistente) {
	        return (Asistente) u;
	    }
	    return null; // o podés tirar una excepción si preferís
	}
	
	public Usuario obtenerUsuarioPorNickOEmail(String valor) {
	    // Buscar por nick
	    Usuario u = usuarios.get(valor);
	    if (u != null) return u;

	    // Buscar por email
	    for (Usuario user : usuarios.values()) {
	        if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(valor)) {
	            return user;
	        }
	    }
	    return null;
	}

	
	public Boolean findCorreo(String correo) {
		for (Map.Entry<String, Usuario> i : usuarios.entrySet()){
			Usuario u = i.getValue();
			if (u.getEmail().equals(correo)) {
				return true;
				
			}
		}
		return false;
	}
	
	public Institucion findInstitucion(String nombre) {
		return institucionesMap.get(nombre);
	}
	
	public boolean existeInstitucion(String nombre) {
		return instituciones.contains(nombre);
	}
	private static void doReset() {
        if (instancia != null) {
            instancia.usuarios.clear();
            instancia.organizadores.clear();
            instancia.asistentes.clear();
            instancia.instituciones.clear();
        }
        instancia = null;
    }
    public static void testReset() { doReset(); }
    public static void reset()       { doReset(); }
    public static void clear()       { doReset(); }
	
}