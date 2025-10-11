package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import logica.fabrica;
import logica.Interfaces.IControladorEvento;
import logica.Datatypes.DTEvento;
import logica.Clases.Eventos;
import excepciones.EventoYaExisteException;

@WebServlet("/evento/*")
public class EventoServlet extends HttpServlet {

  // ===== JSPs =====
  private static final String JSP_ALTA     = "/WEB-INF/evento/alta.jsp";
  private static final String JSP_CONSULTA = "/WEB-INF/evento/ConsultaEvento.jsp";
  private static final String JSP_REGISTRO = "/WEB-INF/evento/RegistrarseEvento.jsp";

  // ===== Lógica =====
  private IControladorEvento ce() { return fabrica.getInstance().getIControladorEvento(); }
  private String ctx(HttpServletRequest req) { return req.getContextPath(); }

  // ===== GET =====
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    String path = req.getPathInfo(); // puede ser null

    if (path == null || "/".equals(path) || "/ConsultaEvento".equals(path)) {
      // Consulta por NOMBRE (query param ?nombre=...)
      String nombre = trim(req.getParameter("nombre"));
      if (isBlank(nombre)) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'nombre'");
        return;
      }
      Eventos e = ce().consultaEvento(nombre);
      if (e == null) {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Evento no encontrado: " + nombre);
        return;
      }

      // ---- preparar atributos planos para el JSP ----
      req.setAttribute("evNombre", safe(() -> e.getNombre()));
      req.setAttribute("evSigla",  safe(() -> e.getSigla()));
      req.setAttribute("evDesc",   safe(() -> e.getDescripcion()));
      req.setAttribute("evFecha",  formatFecha(safeObj(() -> e.getFecha())));
      req.setAttribute("evCategorias", categoriasALista(safeObj(() -> e.getCategorias())));

      req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
      return;
    }

    switch (path) {
      case "/alta": {
        if (!requiereOrganizador(req, resp)) return;
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
        return;
      }
      case "/RegistrarseEvento": {
        if (!requiereLogin(req, resp)) return;
        req.getRequestDispatcher(JSP_REGISTRO).forward(req, resp);
        return;
      }
      case "/listar": {
        List<DTEvento> lista = ce().listarEventos();
        req.setAttribute("lista", lista);
        resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Falta JSP de listado");
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

      String nombre = trim(req.getParameter("nombre"));
      String desc   = trim(req.getParameter("desc"));
      String sigla  = trim(req.getParameter("sigla"));
      // String cats = trim(req.getParameter("categorias")); // si luego mapean a DTO

      if (isBlank(nombre) || isBlank(sigla) || isBlank(desc)) {
        req.setAttribute("error", "Nombre, sigla y descripción son obligatorios.");
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
        return;
      }

      try {
        // TODO: si tienen DTO de categorías, construirlo desde 'cats' y pasarlo
        ce().AltaEvento(nombre, desc, LocalDate.now(), sigla, /*categorias*/ null, sigla);

        String nombreEnc = URLEncoder.encode(nombre, StandardCharsets.UTF_8.name());
        resp.sendRedirect(ctx(req) + "/evento/ConsultaEvento?nombre=" + nombreEnc);
      } catch (EventoYaExisteException e) {
        req.setAttribute("error", e.getMessage());
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
      }
      return;
    }

    if ("/RegistrarseEvento".equals(path)) {
      if (!requiereLogin(req, resp)) return;

      String nombre = trim(req.getParameter("nombre"));
      if (isBlank(nombre)) {
        req.setAttribute("error", "Falta parámetro 'nombre' para registrarse.");
        req.getRequestDispatcher(JSP_REGISTRO).forward(req, resp);
        return;
      }
      String nombreEnc = URLEncoder.encode(nombre, StandardCharsets.UTF_8.name());
      resp.sendRedirect(ctx(req) + "/evento/ConsultaEvento?nombre=" + nombreEnc);
      return;
    }

    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
  }

  // ===== Auth helpers =====
  private boolean requiereOrganizador(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession s = req.getSession(false);
    String rol = s == null ? null : (String) s.getAttribute("rol");
    if (!"ORGANIZADOR".equals(rol)) {
      resp.sendRedirect(ctx(req) + "/auth/login");
      return false;
    }
    return true;
  }

  private boolean requiereLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession s = req.getSession(false);
    if (s == null || s.getAttribute("nick") == null) {
      resp.sendRedirect(ctx(req) + "/auth/login");
      return false;
    }
    return true;
  }

  // ===== Utils =====
  private static String trim(String s){ return s == null ? null : s.trim(); }
  private static boolean isBlank(String s){ return s == null || s.trim().isEmpty(); }

  @FunctionalInterface
  private interface SCall { Object get() throws Exception; }

  private String safe(SCall c) {
    try { Object v = c.get(); return v == null ? null : v.toString(); }
    catch (Exception e) { return null; }
  }

  private Object safeObj(SCall c) {
    try { return c.get(); } catch (Exception e) { return null; }
  }

  private String extractNombre(Object o) {
    if (o == null) return "—";
    try {
      var m = o.getClass().getMethod("getNombre");
      Object v = m.invoke(o);
      return v == null ? "—" : v.toString();
    } catch (Exception ignore) { /* caemos al toString */ }
    return o.toString();
  }

  private List<String> categoriasALista(Object cobj) {
    List<String> out = new ArrayList<>();
    if (cobj == null) return out;

    try {
      if (cobj instanceof java.util.Map<?,?> m) {
        // intento con keys primero
        for (Object k : m.keySet()) if (k != null) out.add(k.toString());
        if (out.isEmpty()) for (Object v : m.values()) out.add(extractNombre(v));
        return out;
      }
      if (cobj instanceof java.util.Collection<?> col) {
        for (Object v : col) out.add(extractNombre(v));
        return out;
      }
      if (cobj.getClass().isArray()) {
        int n = java.lang.reflect.Array.getLength(cobj);
        for (int i=0;i<n;i++) out.add(extractNombre(java.lang.reflect.Array.get(cobj,i)));
        return out;
      }
      if (cobj instanceof String s) {
        for (String p : s.split("[,;]")) {
          String t = p.trim(); if (!t.isEmpty()) out.add(t);
        }
        return out;
      }
    } catch (Exception ignore) {}
    // fallback
    out.add(cobj.toString());
    return out;
  }

  private String formatFecha(Object f) {
    if (f == null) return null;
    try {
      if (f instanceof java.time.LocalDate ld) {
        return ld.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      }
      if (f instanceof java.time.LocalDateTime ldt) {
        return ldt.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
      }
      if (f instanceof java.util.Date d) {
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(d);
      }
    } catch (Exception ignore) {}
    return f.toString();
  }
}
