package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

import logica.fabrica;
import logica.Interfaces.IControladorUsuario;
import logica.Clases.Usuario;

@WebServlet(urlPatterns = {"/auth/login", "/auth/logout"})
public class LoginServlet extends HttpServlet {
  private static final String JSP_LOGIN = "/WEB-INF/auth/login.jsp";
  private final IControladorUsuario cu = fabrica.getInstance().getIControladorUsuario();

  private String ctx(HttpServletRequest req){ 
    return req.getContextPath(); 
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String path = req.getServletPath(); // /auth/login o /auth/logout

    if ("/auth/login".equals(path)) {
    	
    	req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
    	
      return;
    }

    if ("/auth/logout".equals(path)) {
      HttpSession s = req.getSession(false);
      if (s != null) s.invalidate();
      resp.sendRedirect(ctx(req) + "/");
      return;
    }

    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

	  
	  if (!"/auth/login".equals(req.getServletPath())) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
      
      return;
    }

    String nickOrEmail = req.getParameter("email"); // del form: name="email"
    String pass        = req.getParameter("pass");  // del form: name="pass"

    if (nickOrEmail == null || nickOrEmail.isBlank()) {
      req.setAttribute("error", "Ingresá tu usuario (nickname).");
      req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);

      return;
    }

    String nick = nickOrEmail.trim();

    Map<String, Usuario> usuarios = cu.listarUsuarios();





    if (usuarios == null || !usuarios.containsKey(nick)) {
      req.setAttribute("estado_sesion", "LOGIN_INCORRECTO");
      req.setAttribute("error", "Usuario no existe.");
      req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
      return;
    }

    Usuario usr = usuarios.get(nick);

    
//    // ⚠️ Simulación de validación de contraseña (ajustar según tu implementación)
//    if (pass == null || pass.isBlank() || !pass.equals(usr.getPassword())) {
//      req.setAttribute("estado_sesion", "LOGIN_INCORRECTO");
//      req.setAttribute("error", "Contraseña incorrecta.");
//      req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
//      return;
//    }

    // Rol (según tu interfaz)
    String rol = cu.esAsistente(nick) ? "ASISTENTE" : "ORGANIZADOR";

    // Crear sesión y guardar datos del usuario
    HttpSession s = req.getSession(true);
    s.setAttribute("usuario_logueado", usr);
    s.setAttribute("nick", nick);
    s.setAttribute("rol", rol);
    s.setAttribute("estado_sesion", "LOGIN_CORRECTO");

    // Redirigir o hacer forward al home (siguiendo el modelo)
    req.getRequestDispatcher("/index.jsp").forward(req, resp);


  }
}
