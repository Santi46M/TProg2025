package logica.Interfaces;

import excepciones.UsuarioYaExisteException;
import excepciones.InstitucionYaExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioTipoIncorrectoException;
import excepciones.CategoriaYaExisteException;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import logica.Clases.*;
import logica.Datatypes.*;

public interface IControladorUsuario {

    // Crear usuarios
    public void AltaUsuario(String nickname, String nombre, String correo, String descripcion, String link, String apellido, LocalDate fechaNacimiento, String institucion, boolean esOrganizador, String contrasena, String imagen) throws UsuarioYaExisteException;
    public Organizador ingresarOrganizador(String nickname, String nombre, String email, String contrasena, String imagen, String desc, String link);
    public Asistente ingresarAsistente(String nickname, String nombre, String email, String contrasena, String imagen, String apellido, LocalDate fechaDeNacimiento, Institucion institucion);
    
    // Crear institución
    public void AltaInstitucion(String nombre, String descripcion, String link)throws InstitucionYaExisteException;

    // Listados
    public Map<String, Usuario> listarUsuarios();
    public Map<String, Asistente> listarAsistentes();
    public Map<String, Organizador> listarOrganizadores();
    public Set<String> getInstituciones();

    // Actualizaciones
    public void actualizarAsistente(String nickname, String apellido, LocalDate fechaNacimiento) throws UsuarioYaExisteException, UsuarioTipoIncorrectoException, UsuarioNoExisteException;

    public void actualizarOrganizador(String nickname, String desc, String link) throws UsuarioNoExisteException, UsuarioTipoIncorrectoException;

    // Consulta
    public DTDatosUsuario obtenerDatosUsuario(String nickname) throws UsuarioNoExisteException;
    public Set<DTRegistro> obtenerRegistrosAsistente(Asistente asist);
    public DTRegistro obtenerDatosRegistros(String id);
    public boolean esAsistente(String nickname);

    // Categorías
    public void AltaCategoriaSinGUI(String nombre) throws CategoriaYaExisteException;
    

    public void seleccionarRegistro(String id);

    public String getRegistroSeleccionadoId();
    public DTRegistro obtenerRegistroSeleccionado();
    public String getUsuarioSeleccionadoNickname();
	void modificarDatosUsuario(String nickname, String nombre, String descripcion, String link, String apellido, java.time.LocalDate fechaNacimiento, String institucion) throws excepciones.UsuarioNoExisteException, excepciones.UsuarioTipoIncorrectoException;


    // Inicio de sesión
    public boolean inicioSesion(String nickOrEmail, String contrasena);

    // Cierre de sesión
    public void cierreSesion();
    
    public boolean validarLogin(String nickOrEmail, String contrasena);
}