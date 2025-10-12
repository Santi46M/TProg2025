package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

import logica.fabrica;
import logica.Interfaces.IControladorUsuario;
import logica.Datatypes.DTDatosUsuario;
import excepciones.UsuarioYaExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioTipoIncorrectoException;

@WebServlet(urlPatterns = {"/usuario/AltaUsuario", "/usuario/modificar"})
public class UsuarioServlet extends HttpServlet {

  private static final String JSP_LOGIN    = "/WEB-INF/auth/login.jsp";
  private static final String JSP_ALTA     = "/WEB-INF/usuario/AltaUsuario.jsp";
  private static final String JSP_CONSULTA = "/WEB-INF/usuario/ConsultaUsuario.jsp";
  private static final String JSP_MODIF    = "/WEB-INF/usuario/ModificarUsuario.jsp";

  private final IControladorUsuario cu = fabrica.getInstance().getIControladorUsuario();

  private String ctx(HttpServletRequest req) { return req.getContextPath(); }

  private String nickEnSesion(HttpServletRequest req) {
    HttpSession s = req.getSession(false);
    return s == null ? null : (String) s.getAttribute("nick");
  }

  private void cargarInstituciones(HttpServletRequest req) {
    // Carga las instituciones directamente desde la lógica, no desde la sesión
    java.util.Collection<String> instituciones = cu.getInstituciones();
    req.setAttribute("instituciones", instituciones);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

//    String path = req.getPathInfo();
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
          DTDatosUsuario dto = cu.obtenerDatosUsuario(nick);
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
          DTDatosUsuario dto = cu.obtenerDatosUsuario(nick);
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
        return;
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

	  String path = req.getServletPath();  // ← cambio clave
	  System.out.println("path post: " + path);
      req.setAttribute("error", null);
      System.out.println("valor de error en servelt: " + req.getAttribute("error"));

	  if ("/usuario/AltaUsuario".equals(path)) {

		    String rol         = req.getParameter("rol");
		    String nick        = req.getParameter("nick");
		    String nombre      = req.getParameter("nombreA");
		    String pass1      = req.getParameter("pass");
		    String pass2      = req.getParameter("pass2");
		    String organizacion = req.getParameter("nombreO");
		    String apellido    = req.getParameter("apellidoA");
		    String correo      = req.getParameter("email");
		    String descripcion = req.getParameter("descO");
		    String link        = req.getParameter("webO");
		    String institucion = req.getParameter("instIdA");
		    String nacStr      = req.getParameter("nacA");
		   
		    
		    // CHEQUEAMOS CONTRASEÑAS
		    if (pass1 == null || pass2 == null || pass1.isBlank() || pass2.isBlank() || !pass1.equals(pass2)) {
		        req.setAttribute("error", "Las contraseñas no coinciden o están vacías.");
		        cargarInstituciones(req);
		        
		        req.setAttribute("rol", rol);
		        req.setAttribute("nick", nick);
		        req.setAttribute("nombreA", nombre);
			    req.setAttribute("nombreO", organizacion);
		        req.setAttribute("apellidoA", apellido);
		        req.setAttribute("email", correo);
		        req.setAttribute("descripcion", descripcion);
		        req.setAttribute("link", link);
		        req.setAttribute("instIdA", institucion);
		        req.setAttribute("nacA", nacStr);
		        
		        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
		        return;
		    }
		    
		    // Datos obligatorios para ambos roles
		    if (nick == null || correo == null || rol == null ||
		        nick.isBlank() || correo.isBlank()) {
		        req.setAttribute("error", "Faltan datos obligatorios.");
		        
		        req.setAttribute("rol", rol);
		        req.setAttribute("nick", nick);
		        req.setAttribute("nombreA", nombre);
		        req.setAttribute("pass", pass1);
		        req.setAttribute("pass2", pass2);
		        req.setAttribute("apellidoA", apellido);
		        req.setAttribute("nombreO", organizacion);
		        req.setAttribute("email", correo);
		        req.setAttribute("descripcion", descripcion);
		        req.setAttribute("link", link);
		        req.setAttribute("instIdA", institucion);
		        req.setAttribute("nacA", nacStr);

		        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
		        return;
		    }

		    
		    if ("ASISTENTE".equalsIgnoreCase(rol)) {
		    	
		    	// ✅ Validación de fecha de nacimiento
			    LocalDate fechaNac = null;
			    // En este caso es asistente
			    if (nacStr != null && !nacStr.isBlank()) {
			        try {
			            fechaNac = LocalDate.parse(nacStr);
			            LocalDate hoy = LocalDate.now();

			            if (fechaNac.isAfter(hoy)) {
			                throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
			            }

			            int edad = hoy.getYear() - fechaNac.getYear();
			            if (fechaNac.plusYears(edad).isAfter(hoy)) {
			                edad--; // ajustar si aún no cumplió años este año
			            }

			        } catch (IllegalArgumentException e) {
			            req.setAttribute("error", e.getMessage());
			            cargarInstituciones(req);
			            
			            req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
			            return;
			        } catch (Exception e) {
			            req.setAttribute("error", "Formato de fecha inválido. Use un formato correcto (dd-MM-yyyy).");
			            cargarInstituciones(req);
				        req.setAttribute("rol", rol);
				        req.setAttribute("nick", nick);
				        req.setAttribute("nombreA", nombre);
				        req.setAttribute("pass", pass1);
				        req.setAttribute("pass2", pass2);
				        req.setAttribute("apellidoA", apellido);
				        req.setAttribute("email", correo);
				        req.setAttribute("instIdA", institucion);
			            req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
			            return;
			        }
			    } else {
			        req.setAttribute("error", "Debe ingresar una fecha de nacimiento.");
			        cargarInstituciones(req);
			        req.setAttribute("error", "Faltan datos obligatorios.");
			        req.setAttribute("rol", rol);
			        req.setAttribute("nick", nick);
			        req.setAttribute("nombreA", nombre);
			        req.setAttribute("pass", pass1);
			        req.setAttribute("pass2", pass2);
			        req.setAttribute("apellidoA", apellido);
			        req.setAttribute("email", correo);
			        req.setAttribute("instIdA", institucion);
			        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
			        return;
			    }
			}else {
		    	
		    	//En este caso es organizador
		    	if (organizacion == null || organizacion.isBlank() || descripcion == null || descripcion.isBlank()) {
			        req.setAttribute("error", "Faltan datos obligatorios.");
			        cargarInstituciones(req);
			        
			        req.setAttribute("rol", rol);
			        req.setAttribute("nick", nick);
			        req.setAttribute("nombreA", nombre);
			        req.setAttribute("pass", pass1);
			        req.setAttribute("pass2", pass2);
			        req.setAttribute("apellidoA", apellido);
			        req.setAttribute("nombreO", organizacion);
			        req.setAttribute("email", correo);
			        req.setAttribute("descripcion", descripcion);
			        req.setAttribute("link", link);
			        req.setAttribute("instIdA", institucion);
			        req.setAttribute("nacA", nacStr);
			        
			        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
			        return;
			    }
		    }

		    boolean esOrganizador = "ORGANIZADOR".equalsIgnoreCase(rol);
		    LocalDate fechaNac = "ASISTENTE".equalsIgnoreCase(rol) ? LocalDate.parse(nacStr) : null;
		    try {
		        cu.AltaUsuario(
		            nick,
		            nombre,
		            correo,
		            descripcion,     // puede ser null
		            link,            // puede ser nullLo
		            apellido,        // para asistente
		            fechaNac,        // para asistente
		            institucion,     // para asistente
		            esOrganizador,
		            pass1,
		            null
		        );

		        // ✅ Dejar logueado al recién creado
		        HttpSession s = req.getSession(true);
		        s.setAttribute("nick", nick);
		        s.setAttribute("rol", esOrganizador ? "ORGANIZADOR" : "ASISTENTE");
		        resp.sendRedirect(ctx(req) + "/inicio");
		        return;

		    } catch (UsuarioYaExisteException e) {
		        req.setAttribute("error", e.getMessage());
		        cargarInstituciones(req);


		        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
		    }

		    return;
		}


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
        cu.modificarDatosUsuario(nick, nombre, descripcion, link, apellido, fechaNac, institucion);
        resp.sendRedirect(ctx(req) + "/usuario/consulta?nick=" + nick);
      } catch (UsuarioNoExisteException | UsuarioTipoIncorrectoException e) {
        req.setAttribute("error", e.getMessage());
        req.getRequestDispatcher(JSP_MODIF).forward(req, resp);
      }
      return;
    }

    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
  }
}