package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.Map;

import logica.fabrica;
import logica.interfaces.IControladorUsuario;
import logica.clases.Usuario;
import logica.datatypes.DTDatosUsuario;
import excepciones.UsuarioYaExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioTipoIncorrectoException;

@WebServlet(urlPatterns = {"/usuario/AltaUsuario", "/usuario/modificar"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,  // 1 MB antes de escribir en disco
    maxFileSize = 10 * 1024 * 1024,   // Máximo 10 MB por archivo
    maxRequestSize = 20 * 1024 * 1024 // Máximo 20 MB por request
)
public class UsuarioServlet extends HttpServlet {

  private static final String JSP_LOGIN    = "/WEB-INF/auth/login.jsp";
  private static final String JSP_ALTA     = "/WEB-INF/usuario/AltaUsuario.jsp";
  private static final String JSP_CONSULTA = "/WEB-INF/usuario/ConsultaUsuario.jsp";
  private static final String JSP_MODIF    = "/WEB-INF/usuario/ModificarUsuario.jsp";

  private final IControladorUsuario controladorUs = fabrica.getInstance().getIControladorUsuario();

  private String ctx(HttpServletRequest req) { return req.getContextPath(); }

  private String nickEnSesion(HttpServletRequest req) {
    HttpSession sAux = req.getSession(false);
    return sAux == null ? null : (String) sAux.getAttribute("nick");
  }

  private void cargarInstituciones(HttpServletRequest req) {
    java.util.Collection<String> instituciones = controladorUs.getInstituciones();
    req.setAttribute("instituciones", instituciones);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String path = req.getServletPath();
    if (path == null || "/".equals(path)) {
      resp.sendRedirect(ctx(req) + "/");
      return;
    }

    switch (path) {
      case "/usuario/AltaUsuario":
        cargarInstituciones(req);
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
        return;

      case "/usuario/ConsultaUsuario": {
        String nick = req.getParameter("nick");
        if (nick == null || nick.isBlank()) {
          nick = nickEnSesion(req);
          if (nick == null) { req.getRequestDispatcher(JSP_LOGIN).forward(req, resp); return; }
        }
        try {
          DTDatosUsuario dto = controladorUs.obtenerDatosUsuario(nick);
          req.setAttribute("dto", dto);
          req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
        } catch (UsuarioNoExisteException e) {
          req.setAttribute("error", e.getMessage());
          req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
        }
        return;
      }

      case "/modificar": {
        String nick = nickEnSesion(req);
        if (nick == null) { req.getRequestDispatcher(JSP_LOGIN).forward(req, resp); return; }
        try {
          DTDatosUsuario dto = controladorUs.obtenerDatosUsuario(nick);
          req.setAttribute("dto", dto);
          req.getRequestDispatcher(JSP_MODIF).forward(req, resp);
        } catch (UsuarioNoExisteException e) {
          req.setAttribute("error", e.getMessage());
          req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
        }
        return;
      }

      default:
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String path = req.getServletPath();
    System.out.println("POST en: " + path);

    if ("/usuario/AltaUsuario".equals(path)) {

      // === Procesar imagen (opcional) ===
      Part imagenPart = req.getPart("imagen");
      String nombreArchivo = null;

      if (imagenPart != null && imagenPart.getSize() > 0) {
        nombreArchivo = imagenPart.getSubmittedFileName();
        String rutaUploads = getServletContext().getRealPath("/uploads");
        File uploadsDir = new File(rutaUploads);
        if (!uploadsDir.exists()) uploadsDir.mkdir();

        File destino = new File(uploadsDir, nombreArchivo);
        imagenPart.write(destino.getAbsolutePath());
        System.out.println("✅ Imagen guardada en: " + destino.getAbsolutePath());
      }

      // === Leer parámetros ===
      String rol         = req.getParameter("rol");
      String nick        = req.getParameter("nick");
      String nombre      = req.getParameter("nombreA");
      String pass1       = req.getParameter("pass");
      String pass2       = req.getParameter("pass2");
      String organizacion= req.getParameter("nombreO");
      String apellido    = req.getParameter("apellidoA");
      String correo      = req.getParameter("email");
      String descripcion = req.getParameter("descO");
      String link        = req.getParameter("webO");
      String institucion = req.getParameter("instIdA");
      String nacStr      = req.getParameter("nacA");

      // === Validaciones básicas ===
      if (pass1 == null || pass2 == null || pass1.isBlank() || pass2.isBlank() || !pass1.equals(pass2)) {
        req.setAttribute("error", "Las contraseñas no coinciden o están vacías.");
        cargarInstituciones(req);
        forwardConDatos(req, resp, rol, nick, nombre, apellido, correo, descripcion, link, institucion, nacStr);
        return;
      }

      if (nick == null || correo == null || rol == null || nick.isBlank() || correo.isBlank()) {
        req.setAttribute("error", "Faltan datos obligatorios.");
        cargarInstituciones(req);
        forwardConDatos(req, resp, rol, nick, nombre, apellido, correo, descripcion, link, institucion, nacStr);
        return;
      }

      // === Validación específica por rol ===
      LocalDate fechaNac = null;
      if ("ASISTENTE".equalsIgnoreCase(rol)) {
        if (nacStr == null || nacStr.isBlank()) {
          req.setAttribute("error", "Debe ingresar una fecha de nacimiento.");
          cargarInstituciones(req);
          forwardConDatos(req, resp, rol, nick, nombre, apellido, correo, descripcion, link, institucion, nacStr);
          return;
        }
        try {
          fechaNac = LocalDate.parse(nacStr);
          if (fechaNac.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
          }
        } catch (Exception e) {
          req.setAttribute("error", "Formato de fecha inválido o futura.");
          cargarInstituciones(req);
          forwardConDatos(req, resp, rol, nick, nombre, apellido, correo, descripcion, link, institucion, nacStr);
          return;
        }
      } else {
        if (organizacion == null || organizacion.isBlank() || descripcion == null || descripcion.isBlank()) {
          req.setAttribute("error", "Debe completar los campos obligatorios del organizador.");
          cargarInstituciones(req);
          forwardConDatos(req, resp, rol, nick, nombre, apellido, correo, descripcion, link, institucion, nacStr);
          return;
        }
      }

      // === Crear usuario ===
      boolean esOrganizador = "ORGANIZADOR".equalsIgnoreCase(rol);
      try {
        controladorUs.altaUsuario(
            nick,
            nombre,
            correo,
            descripcion,
            link,
            apellido,
            fechaNac,
            institucion,
            esOrganizador,
            pass1,
            nombreArchivo
        );

        // ✅ Crear sesión como en login
        HttpSession sAux = req.getSession(true);
        Map<String, Usuario> usuarios = controladorUs.listarUsuarios();
        Usuario usr = usuarios.get(nick);

        if (usr == null) {
          for (Usuario u : usuarios.values()) {
            if (u.getEmail().equalsIgnoreCase(correo)) {
              usr = u;
              break;
            }
          }
        }

        sAux.setAttribute("usuario_logueado", usr);
        sAux.setAttribute("nick", nick);
        sAux.setAttribute("rol", esOrganizador ? "ORGANIZADOR" : "ASISTENTE");
        sAux.setAttribute("estado_sesion", "LOGIN_CORRECTO");


        Enumeration<String> names = sAux.getAttributeNames();
        while (names.hasMoreElements()) {
          String nAux = names.nextElement();
          Object vAux = sAux.getAttribute(nAux);
          System.out.println("   * " + nAux + " = " + vAux);
        }

        resp.sendRedirect(ctx(req) + "/inicio");

      } catch (UsuarioYaExisteException e) {
        req.setAttribute("error", e.getMessage());
        cargarInstituciones(req);
        forwardConDatos(req, resp, rol, nick, nombre, apellido, correo, descripcion, link, institucion, nacStr);
      }

      return;
    }

    // === Modificar usuario ===
    if ("/modificar".equals(path)) {
      String nick = nickEnSesion(req);
      if (nick == null) { req.getRequestDispatcher(JSP_LOGIN).forward(req, resp); return; }

      String nombre      = req.getParameter("nombre");
      String descripcion = req.getParameter("descripcion");
      String link        = req.getParameter("link");
      String apellido    = req.getParameter("apellido");
      String institucion = req.getParameter("institucion");
      String nacStr      = req.getParameter("nac");
      LocalDate fechaNac = null;
      if (nacStr != null && !nacStr.isBlank()) {
        try { fechaNac = LocalDate.parse(nacStr); } catch (Exception ignored) {}
      }

      try {
        controladorUs.modificarDatosUsuario(nick, nombre, descripcion, link, apellido, fechaNac, institucion);
        resp.sendRedirect(ctx(req) + "/usuario/consulta?nick=" + nick);
      } catch (UsuarioNoExisteException | UsuarioTipoIncorrectoException e) {
        req.setAttribute("error", e.getMessage());
        req.getRequestDispatcher(JSP_MODIF).forward(req, resp);
      }
    } else {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private void forwardConDatos(HttpServletRequest req, HttpServletResponse resp,
                               String rol, String nick, String nombre, String apellido,
                               String correo, String descripcion, String link,
                               String institucion, String nacStr)
      throws ServletException, IOException {
    req.setAttribute("rol", rol);
    req.setAttribute("nick", nick);
    req.setAttribute("nombreA", nombre);
    req.setAttribute("apellidoA", apellido);
    req.setAttribute("email", correo);
    req.setAttribute("descripcion", descripcion);
    req.setAttribute("link", link);
    req.setAttribute("instIdA", institucion);
    req.setAttribute("nacA", nacStr);
    req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
  }
}