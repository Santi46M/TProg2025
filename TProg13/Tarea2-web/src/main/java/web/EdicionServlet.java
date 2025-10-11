package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import logica.fabrica;
import logica.Interfaces.IControladorEvento;   // <-- ajustá si el nombre real difiere
import logica.Datatypes.DTEdicion;             // <-- ajustá si usás otro DTO
import excepciones.EdicionYaExisteException;   // <-- ajustá a tu excepción real
import excepciones.EventoYaExisteException;
import excepciones.FechasCruzadasException;
import logica.Clases.Eventos;
import logica.Clases.Usuario;
import logica.Clases.Ediciones;

@WebServlet("/edicion/*")
@MultipartConfig // porque tu form tiene <input type="file" name="imagen">
public class EdicionServlet extends HttpServlet {

  // ===== JSPs =====
  private static final String JSP_ALTA     = "/WEB-INF/ediciones/AltaEdicion.jsp";
  private static final String JSP_CONSULTA = "/WEB-INF/ediciones/ConsultaEdicion.jsp";
  private static final String JSP_LISTADO  = "/WEB-INF/ediciones/ListarEdiciones.jsp";

  // ===== Lógica =====
  private IControladorEvento ce() {  // fábrica hacia tu capa de lógica
    return fabrica.getInstance().getIControladorEvento();
  }

  private String ctx(HttpServletRequest req) { return req.getContextPath(); }

  // ===== Helpers =====
  private Usuario getUsuario(HttpServletRequest req) {
    HttpSession s = req.getSession(false);
    return s == null ? null : (Usuario) s.getAttribute("usuario_logueado");
  }
  private String getRol(HttpServletRequest req) {
    HttpSession s = req.getSession(false);
    return s == null ? null : (String) s.getAttribute("rol");
  }

  // ===== GET =====
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    String path = req.getPathInfo(); // puede ser null

    if (path == null || "/".equals(path) || "/ConsultaEdicion".equals(path)) {
      // Ejemplo: consulta por nombre (?evento=...&edicion=...)
      String evento  = trim(req.getParameter("evento"));
      String edicion = trim(req.getParameter("edicion"));
      if (isBlank(evento) || isBlank(edicion)) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan parámetros 'evento' y/o 'edicion'");
        return;
      }

      Ediciones edicionObj = ce().obtenerEdicion(evento, edicion); // Obtener la clase Ediciones
      if (edicionObj == null) {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Edición no encontrada: " + edicion);
        return;
      }
      req.setAttribute("edicion", edicionObj); // Pasar el objeto Ediciones
      req.setAttribute("organizador", edicionObj.getOrganizador());
      req.setAttribute("tiposRegistro", edicionObj.getTiposRegistro());
      req.setAttribute("patrocinios", edicionObj.getPatrocinios());
      req.setAttribute("evNombre", evento);
      req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
      return;
    }

    switch (path) {
      case "/alta": {
        if (!requiereOrganizador(req, resp)) return;
        var listaEventos = ce().listarEventos();
        req.setAttribute("listaEventos", listaEventos);
        if (listaEventos == null || listaEventos.isEmpty()) {
          req.setAttribute("sinEventos", true);
        }
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
        return;
      }
      case "/listar": {
        String evento = trim(req.getParameter("evento")); // obtener el nombre del evento
        if (isBlank(evento)) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'evento'");
          return;
        }
        req.setAttribute("listaEdiciones", ce().listarEdicionesEvento(evento)); // ajustado
        req.getRequestDispatcher(JSP_LISTADO).forward(req, resp);
        return;
      }
      default:
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  // ===== POST =====
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    String path = req.getPathInfo();

    if ("/alta".equals(path)) {
      if (!requiereOrganizador(req, resp)) return;

      String evento = trim(req.getParameter("evento"));   // viene del <select name="evento">
      String nombre = trim(req.getParameter("nombre"));
      String desc   = trim(req.getParameter("desc"));
      String iniStr = trim(req.getParameter("fechaInicio"));
      String finStr = trim(req.getParameter("fechaFin"));
      String ciudad = trim(req.getParameter("ciudad"));
      String pais   = trim(req.getParameter("pais"));

      // Archivo (opcional)
      Part imagen = null;
      try { imagen = req.getPart("imagen"); } catch (Exception ignore) {}

      if (isBlank(evento) || isBlank(nombre) || isBlank(desc) ||
          isBlank(iniStr) || isBlank(finStr) || isBlank(ciudad) || isBlank(pais)) {
        req.setAttribute("error", "Todos los campos obligatorios deben completarse.");
        req.setAttribute("listaEventos", ce().listarEventos());
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
        return;
      }

      LocalDate ini, fin;
      try {
        ini = LocalDate.parse(iniStr);
        fin = LocalDate.parse(finStr);
      } catch (Exception e) {
        req.setAttribute("error", "Formato de fecha inválido.");
        req.setAttribute("listaEventos", ce().listarEventos());
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
        return;
      }

      try {
        // Obtener el organizador desde la sesión
        Usuario org = getUsuario(req);
        if (org == null) {
          req.setAttribute("error", "Debe iniciar sesión como organizador para crear una edición.");
          req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
          return;
        }

        // Guardado en capa lógica. Ajustá firma: quizá pases bytes de imagen, stream, o null.
        // byte[] imgBytes = null; // imagen no se usa en la lógica real
        Eventos evObj = ce().consultaEvento(evento);
        ce().AltaEdicionEvento(evObj, org, nombre, nombre, desc, ini, fin, LocalDate.now(), ciudad, pais, null); // ajustado a la firma real

        String evEnc = URLEncoder.encode(evento, StandardCharsets.UTF_8);
        String edEnc = URLEncoder.encode(nombre, StandardCharsets.UTF_8);
        resp.sendRedirect(ctx(req) + "/edicion/ConsultaEdicion?evento=" + evEnc + "&edicion=" + edEnc);
      } catch (EdicionYaExisteException | EventoYaExisteException | FechasCruzadasException ex) {
        req.setAttribute("error", ex.getMessage());
        req.setAttribute("listaEventos", ce().listarEventos());
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
      }
      return;
    }

    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
  }

  // ===== Auth helpers (idénticos a los de tu EventoServlet) =====
  private boolean requiereOrganizador(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession s = req.getSession(false);
    String rol = s == null ? null : (String) s.getAttribute("rol");
    if (!"ORGANIZADOR".equals(rol)) {
      resp.sendRedirect(ctx(req) + "/auth/login");
      return false;
    }
    return true;
  }

  // ===== Utils =====
  private static String trim(String s){ return s == null ? null : s.trim(); }
  private static boolean isBlank(String s){ return s == null || s.trim().isEmpty(); }
}