package logica;

import excepciones.UsuarioYaExisteException;
import excepciones.InstitucionYaExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioTipoIncorrectoException;
import excepciones.CategoriaYaExisteException;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public interface IControladorUsuario {

    // Crear usuarios
    public void AltaUsuario(String nickname, String nombre, String correo, String descripcion, String link, String apellido, LocalDate fechaNacimiento, String institucion, boolean esOrganizador) throws UsuarioYaExisteException;
    public Organizador ingresarOrganizador(String nickname, String nombre, String email, String desc, String link);
    public Asistente ingresarAsistente(String nickname, String nombre, String email, String apellido,LocalDate fechaDeNacimiento, Institucion institucion);
    
    // Crear institución
    public void AltaInstitucion(String nombre, String descripcion, String link)throws InstitucionYaExisteException;

    // Listados
    public Map<String, Usuario> listarUsuarios();
    public Map<String, Usuario> listarAsistentes();
    public Map<String, Usuario> listarOrganizadores();
    public Set<String> getInstituciones();

    // Actualizaciones
    public void actualizarAsistente(String nickname, String apellido, LocalDate fechaNacimiento) throws UsuarioYaExisteException, UsuarioTipoIncorrectoException;

    public void actualizarOrganizador(String nickname, String desc, String link) throws UsuarioNoExisteException, UsuarioTipoIncorrectoException;

    // Consulta
    public DTDatosUsuario obtenerDatosUsuario(String nickname) throws UsuarioNoExisteException;

    // Categorías
    public void AltaCategoriaSinGUI(String nombre) throws CategoriaYaExisteException;
}
