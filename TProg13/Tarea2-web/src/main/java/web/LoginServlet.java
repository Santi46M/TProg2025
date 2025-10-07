package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import logica.Fabrica;
import logica.IControladorUsuario;

@WebServlet(urlPatterns = {"/auth/login", "/auth/logout"})
public class LoginServlet extends HttpServlet {
  private static final String JSP_LOGIN = "/WEB-INF/auth/login.jsp";
  private final IControladorUsuario cu = Fabrica.getInstancia().getControladorUsuario();

  private String ctx(HttpServletRequest req){ return req.getContextPath(); }

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

    String emailOrNick = req.getParameter("email"); // name="email" en el form
    String pass        = req.getParameter("pass");  // name="pass"  en el form

    if (emailOrNick == null || pass == null || emailOrNick.isBlank() || pass.isBlank()) {
      req.setAttribute("error", "Completá usuario y contraseña.");
      req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
      return;
    }

    String nick = emailOrNick; // si tenés email→nick, hacelo aquí
    boolean ok = cu.validarLogin(nick, pass); // ajustá la firma si difiere
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

//falta probarlo