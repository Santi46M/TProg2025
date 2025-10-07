package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import logica.Fabrica;
import logica.IControladorUsuario;
import dtos.DTUsuario;
import excepciones.UsuarioRepetidoException;

@WebServlet("/usuario/*")
public class UsuarioServlet extends HttpServlet {

  private static final String JSP_LOGIN = "/WEB-INF/auth/login.jsp";
  private static final String JSP_ALTA  = "/WEB-INF/usuarios/AltaUsuario.jsp";
  // Si más adelante creás estas páginas, actualizá las rutas:
  private static final String JSP_CONSULTA   = "/WEB-INF/usuarios/ConsultaUsuario.jsp";
  private static final String JSP_MODIFICAR  = "/WEB-INF/usuarios/ModificarUsuario.jsp";

  private final IControladorUsuario cu = Fabrica.getInstancia().getControladorUsuario();

  private String ctx(HttpServletRequest req) { return req.getContextPath(); }

  private String nickEnSesion(HttpServletRequest req) {
    HttpSession s = req.getSession(false);
    return s == null ? null : (String) s.getAttribute("nick");
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String path = req.getPathInfo(); // puede ser null, "/", "/alta", "/modificar"
    if (path == null || "/".equals(path)) {
      // Si todavía no tenés ConsultaUsuario.jsp, por ahora mandamos al index.
      resp.sendRedirect(ctx(req) + "/");
      return;
      // Cuando tengas la consulta:
      // String nick = req.getParameter("nick");
      // if (nick == null || nick.isBlank()) {
      //   nick = nickEnSesion(req);
      //   if (nick == null) { req.getRequestDispatcher(JSP_LOGIN).forward(req, resp); return; }
      // }
      // DTUsuario dto = cu.obtenerDTUsuario(nick);
      // req.setAttribute("dto", dto);
      // req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
      // return;
    }

    switch (path) {
      case "/alta":
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
        break;

      case "/modificar": {
        String nick = nickEnSesion(req);
        if (nick == null) { req.getRequestDispatcher(JSP_LOGIN).forward(req, resp); return; }
        DTUsuario dto = cu.obtenerDTUsuario(nick);
        req.setAttribute("dto", dto);
        req.getRequestDispatcher(JSP_MODIFICAR).forward(req, resp);
        break;
      }

      default:
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String path = req.getPathInfo();

    if ("/alta".equals(path)) {
      // NOMBRES DE INPUTS según tu AltaUsuario.jsp (la que convertimos):
      // rol = ASISTENTE|ORGANIZADOR
      // nick, correo, pass, pass2
      // ASISTENTE: nombre, apellido, nac
      // ORGANIZADOR: orgNombre, orgDesc, (orgWeb opcional)
      String rol      = req.getParameter("rol");
      String nick     = req.getParameter("nick");
      String correo   = req.getParameter("correo");
      String pass     = req.getParameter("pass");
      String pass2    = req.getParameter("pass2");

      if (nick==null || correo==null || pass==null || pass2==null || !pass.equals(pass2)) {
        req.setAttribute("error", "Datos inválidos o contraseñas no coinciden");
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
        return;
      }

      try {
        if ("ASISTENTE".equals(rol)) {
          String nombre   = req.getParameter("nombre");
          String apellido = req.getParameter("apellido");
          String nac      = req.getParameter("nac"); // yyyy-MM-dd
          // TODO: si tu lógica requiere institución u otros campos, leé aquí
          cu.altaUsuarioAsistente(nick, correo, nombre, apellido, nac, pass); // adapta a tu firma real
        } else if ("ORGANIZADOR".equals(rol)) {
          String orgNombre = req.getParameter("orgNombre");
          String orgDesc   = req.getP
