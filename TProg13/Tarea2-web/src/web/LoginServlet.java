package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import logica.Fabrica;
import logica.IControladorUsuario;

@WebServlet("/auth/*")
public class LoginServlet extends HttpServlet {
  private static final String JSP_LOGIN = "/WEB-INF/auth/login.jsp";
  private final IControladorUsuario cu = Fabrica.getInstancia().getControladorUsuario();

  private String ctx(HttpServletRequest req){ return req.getContextPath(); }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String path = req.getPathInfo(); // puede ser null, "/login", "/logout"
    if (path == null || "/login".equals(path)) {
      req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
      return;
    }
    if ("/logout".equals(path)) {
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
    if (!"/login".equals(req.getPathInfo())) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }

    String emailOrNick = req.getParameter("email"); // tu form usa "email"
    String pass = req.getParameter("pass");
    if (emailOrNick == null || pass == null || emailOrNick.isBlank() || pass.isBlank()) {
      req.setAttribute("error", "Completá usuario y contraseña.");
      req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
      return;
    }

    String nick = emailOrNick; // si tenés método correo→nick, úsalo aquí
    boolean ok = cu.validarLogin(nick, pass); // ajustá firma si difiere
    if (!ok) {
      req.setAttribute("error", "Credenciales incorrectas");
      req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
      return;
    }

    HttpSession s = req.getSession(true);
    s.setAttribute("nick", nick);
    s.setAttribute("rol", cu.obtenerRol(nick)); // "ASISTENTE" | "ORGANIZADOR"
    resp.sendRedirect(ctx(req) + "/");
  }
}
