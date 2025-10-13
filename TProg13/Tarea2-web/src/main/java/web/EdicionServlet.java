package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;

import logica.fabrica;
import logica.interfaces.IControladorEvento;
import excepciones.EdicionYaExisteException;
import excepciones.EventoYaExisteException;
import excepciones.FechasCruzadasException;
import logica.clases.Eventos;
import logica.clases.Usuario;
import logica.clases.Ediciones;

@WebServlet("/edicion/*")
@MultipartConfig
public class EdicionServlet extends HttpServlet {

  private static final String JSP_ALTA     = "/WEB-INF/ediciones/AltaEdicion.jsp";
  private static final String JSP_CONSULTA = "/WEB-INF/ediciones/ConsultaEdicion.jsp";
  private static final String JSP_LISTADO  = "/WEB-INF/ediciones/ListarEdiciones.jsp";

  private IControladorEvento ce() {  
    return fabrica.getInstance().getIControladorEvento();
  }

  private String ctx(HttpServletRequest req) { return req.getContextPath(); }

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
    String path = req.getPathInfo(); 

    if (path == null || "/".equals(path) || "/ConsultaEdicion".equals(path)) {
      String evento  = trim(req.getParameter("evento"));
      String edicion = trim(req.getParameter("edicion"));

      if (isBlank(evento) || isBlank(edicion)) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan parámetros 'evento' y/o 'edicion'");
        return;
      }

      // 🔹 Obtener la edición solicitada
      Ediciones edicionObj = ce().obtenerEdicion(evento, edicion);
      if (edicionObj == null) {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Edición no encontrada: " + edicion);
        return;
      }

      req.setAttribute("edicion", edicionObj);
      req.setAttribute("organizador", edicionObj.getOrganizador());
      req.setAttribute("tiposRegistro", edicionObj.getTiposRegistro());
      req.setAttribute("patrocinios", edicionObj.getPatrocinios());
      req.setAttribute("evNombre", evento);

      // 🟢 Nuevo: recargar ediciones directamente desde el evento actual
      java.util.List<Ediciones> edicionesLista = listarEdicionesEventoCompleto(evento);
      req.setAttribute("evEdiciones", edicionesLista);

      System.out.println("[EdicionServlet] Ediciones del evento '" + evento + "':");
      if (edicionesLista.isEmpty()) {
        System.out.println("  (ninguna)");
      } else {
        for (Ediciones e : edicionesLista) {
          System.out.println("  - " + e.getNombre());
        }
      }

      // --- lógica para mostrar registros solo al organizador ---
      HttpSession session = req.getSession(false);
      String nickSesion = session != null ? (String) session.getAttribute("nick") : null;
      boolean esOrganizador = nickSesion != null && edicionObj.getOrganizador() != null 
                              && nickSesion.equals(edicionObj.getOrganizador().getNickname());

      java.util.List<logica.clases.Registro> registrosList = null;
      if (esOrganizador) {
        java.util.Map<String, logica.clases.Registro> registrosMap = edicionObj.getRegistros();
        registrosList = new java.util.ArrayList<>(registrosMap.values());
      } else if (nickSesion != null) {
        logica.clases.Usuario usuarioLogueado = logica.manejadores.manejadorUsuario.getInstancia().findUsuario(nickSesion);
        if (usuarioLogueado instanceof logica.clases.Asistente asistente) {
          registrosList = new java.util.ArrayList<>();
          java.util.Map<String, logica.clases.Registro> registrosAsist = asistente.getRegistros();
          for (logica.clases.Registro r : registrosAsist.values()) {
            if (r.getEdicion() != null && 
                r.getEdicion().getNombre().equals(edicionObj.getNombre()) && 
                r.getEdicion().getEvento().getNombre().equals(edicionObj.getEvento().getNombre())) {
              registrosList.add(r);
            }
          }
        }
      }

      if (registrosList == null) registrosList = new java.util.ArrayList<>();
      req.setAttribute("registros", registrosList);
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
        String evento = trim(req.getParameter("evento"));
        if (isBlank(evento)) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'evento'");
          return;
        }
        req.setAttribute("listaEdiciones", listarEdicionesEventoCompleto(evento));
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

      String evento = trim(req.getParameter("evento"));
      String nombre = trim(req.getParameter("nombre"));
      String desc   = trim(req.getParameter("desc"));
      String iniStr = trim(req.getParameter("fechaInicio"));
      String finStr = trim(req.getParameter("fechaFin"));
      String ciudad = trim(req.getParameter("ciudad"));
      String pais   = trim(req.getParameter("pais"));

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
        Usuario org = getUsuario(req);
        if (org == null || !"ORGANIZADOR".equals(getRol(req))) {
          req.setAttribute("error", "Debés iniciar sesión como organizador.");
          req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
          return;
        }

        Eventos evObj = ce().consultaEvento(evento);
        ce().altaEdicionEvento(evObj, org, nombre, nombre, desc, ini, fin, LocalDate.now(), ciudad, pais, null);

        // 🔹 Confirmar que se agregó en memoria
        System.out.println("[EdicionServlet] Ediciones actuales para " + evento + ":");
        if (evObj != null && evObj.getEdiciones() != null) {
          for (Ediciones e : evObj.getEdiciones().values()) {
            System.out.println("  - " + e.getNombre());
          }
        }

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

  // ===== Método auxiliar =====
  private java.util.List<Ediciones> listarEdicionesEventoCompleto(String nombreEvento) {
    java.util.List<Ediciones> lista = new ArrayList<>();
    try {
      Eventos evento = ce().consultaEvento(nombreEvento);
      if (evento == null) return lista;
      if (evento.getEdiciones() != null && !evento.getEdiciones().isEmpty()) {
        lista.addAll(evento.getEdiciones().values());
      }
    } catch (Exception e) {
      System.err.println("[listarEdicionesEventoCompleto] Error: " + e.getMessage());
    }
    return lista;
  }

  private boolean requiereOrganizador(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession s = req.getSession(false);
    String rol = s == null ? null : (String) s.getAttribute("rol");
    if (!"ORGANIZADOR".equals(rol)) {
      resp.sendRedirect(ctx(req) + "/auth/login");
      return false;
    }
    return true;
  }

  private static String trim(String s){ return s == null ? null : s.trim(); }
  private static boolean isBlank(String s){ return s == null || s.trim().isEmpty(); }
}
