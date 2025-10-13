package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import logica.fabrica;
import logica.interfaces.IControladorEvento;
import logica.datatypes.DTCategorias;
import logica.datatypes.DTEdicion;
import logica.datatypes.DTEvento;
import logica.clases.Eventos;
import excepciones.EventoYaExisteException;
import logica.controladores.ControladorEvento;

@WebServlet("/evento/*")
@MultipartConfig(
    maxFileSize = 2 * 1024 * 1024,     // 2 MB por archivo
    maxRequestSize = 4 * 1024 * 1024   // 4 MB por request
)
public class EventoServlet extends HttpServlet {

  // ===== JSPs =====
  private static final String JSP_ALTA     = "/WEB-INF/evento/alta.jsp";
  private static final String JSP_CONSULTA = "/WEB-INF/evento/ConsultaEvento.jsp";
  private static final String JSP_REGISTRO = "/WEB-INF/evento/RegistrarseEvento.jsp";
  private static final String JSP_LISTAR   = "/WEB-INF/evento/listado.jsp";

  // Carpeta pública donde se guardan las imágenes NUEVAS (URL pública: <ctx>/eventos/archivo.ext)
  private static final String IMG_REL_BASE_EVENTO = "/eventos";

  // ===== Lógica =====
  private final IControladorEvento ce = fabrica.getInstance().getIControladorEvento();
  private String ctx(HttpServletRequest req) { return req.getContextPath(); }

  // ===== GET =====
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    String path = req.getPathInfo(); // puede ser null

    // Consulta de evento (?nombre=...)
    if (path == null || "/".equals(path) || "/ConsultaEvento".equals(path)) {
      String nombre = trim(req.getParameter("nombre"));
      if (isBlank(nombre)) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'nombre'");
        return;
      }
      Eventos e = ce.consultaEvento(nombre);
      if (e == null) {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Evento no encontrado: " + nombre);
        return;
      }

      // Datos básicos
      req.setAttribute("evNombre", safe(() -> e.getNombre()));
      req.setAttribute("evSigla",  safe(() -> e.getSigla()));
      req.setAttribute("evDesc",   safe(() -> e.getDescripcion()));
      req.setAttribute("evFecha",  formatFecha(safeObj(() -> e.getFecha())));
      req.setAttribute("evCategorias", categoriasALista(safeObj(() -> e.getCategorias())));

      // ===== IMAGEN (misma estrategia que en Edición) =====
      // La entidad guarda SOLO el nombre de archivo (p.ej. "myevento.jpg")
      String imgName = null;
      try { imgName = e.getImagen(); } catch (Exception ignore) {}

      String imgUrl = null;
      String ctxPath = ctx(req);

      if (imgName != null && !imgName.isBlank()) {
        // Candidatos en orden: estático primero (/img/<archivo>), luego subidas (/eventos/<archivo>)
        String[] candidates = new String[] {
          "/img/" + imgName,
          IMG_REL_BASE_EVENTO + "/" + imgName
        };
        for (String rel : candidates) {
          String abs = getServletContext().getRealPath(rel);
          boolean exists;
          if (abs != null) {
            exists = java.nio.file.Files.exists(java.nio.file.Path.of(abs));
          } else {
            // En algunos runtimes getRealPath() puede dar null (exploded=false). No bloqueamos.
            exists = true;
          }
          if (exists) { imgUrl = ctxPath + rel; break; }
        }
      }

      if (imgUrl != null) {
        req.setAttribute("evImagenUrl", imgUrl);       // URL ya resuelta para <img src=...>
      }
      req.setAttribute("evImagenNombre", imgName);     // por si lo querés mostrar en algún lado

      // Ediciones del evento
      List<String> nombresEdiciones = ce.listarEdicionesEvento(nombre);
      List<DTEdicion> ediciones = new ArrayList<>();
      if (nombresEdiciones != null) {
        for (String nombreEd : nombresEdiciones) {
          DTEdicion ed = ce.consultaEdicionEvento(nombre, nombreEd);
          if (ed != null) ediciones.add(ed);
        }
      }
      req.setAttribute("evEdiciones", ediciones);

      req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
      return;
    }

    // Rutas específicas
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
      case "/listado": {
        String cat = trim(req.getParameter("categoria"));

        List<DTEvento> lista;
        if (!isBlank(cat)) {
          lista = ce.listarEventosPorCategoria(cat);
          req.setAttribute("categoriaSeleccionada", cat);
        } else {
          lista = ce.listarEventos();
        }

        List<String> categorias = ControladorEvento.listarCategorias();
        req.setAttribute("categorias", categorias);

        req.setAttribute("lista", lista);
        req.getRequestDispatcher(JSP_LISTAR).forward(req, resp);
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

    // Alta de evento (con imagen opcional)
    if ("/alta".equals(path)) {
      if (!requiereOrganizador(req, resp)) return;

      String nombre = trim(req.getParameter("nombre"));
      String desc   = trim(req.getParameter("desc"));
      String sigla  = trim(req.getParameter("sigla"));
      String cats   = trim(req.getParameter("categorias"));

      List<String> categoriasList = new ArrayList<>();
      if (!isBlank(cats)) {
        for (String c : cats.split(",")) {
          String t = c.trim();
          if (!t.isEmpty()) categoriasList.add(t);
        }
      }
      if (categoriasList.isEmpty()) {
        req.setAttribute("error", "Debe asociar al menos una categoría al evento");
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
        return;
      }

      // Subida de imagen: guardamos físicamente en /eventos y persistimos SOLO el nombre
      String imagenFileName = null;
      try {
        Part imgPart = null;
        try { imgPart = req.getPart("imagen"); } catch (IllegalStateException ise) { imgPart = null; }

        if (imgPart != null && imgPart.getSize() > 0) {
          String ctype = imgPart.getContentType();
          if (ctype == null || !ctype.toLowerCase().startsWith("image/")) {
            req.setAttribute("error", "El archivo subido no es una imagen válida.");
            req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
            return;
          }

          // Ruta física de /eventos dentro de la webapp
          String baseImg = getServletContext().getRealPath(IMG_REL_BASE_EVENTO);
          if (baseImg == null) {
            String root = getServletContext().getRealPath("/");
            if (root != null) baseImg = Path.of(root, "eventos").toString();
          }

          if (baseImg != null) {
            Files.createDirectories(Path.of(baseImg));

            String original = getSafeFilename(imgPart);
            String ext = getExtension(original);
            if (ext == null || ext.isBlank()) ext = guessExtensionFromContentType(ctype);
            if (ext == null || ext.isBlank()) ext = ".jpg"; // fallback

            String finalName = (isBlank(sigla) ? "evento" : sigla) + ext;
            Path destino = Path.of(baseImg, finalName);
            imgPart.write(destino.toAbsolutePath().toString());

            imagenFileName = finalName; // persistimos solo el NOMBRE
          } else {
            System.err.println("WARN: No se pudo resolver ruta física para " + IMG_REL_BASE_EVENTO);
          }
        }
      } catch (Exception fileEx) {
        req.setAttribute("error", "Error al procesar la imagen: " + fileEx.getMessage());
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
        return;
      }

      // Crear DTCategorias y dar de alta
      DTCategorias dtCategorias = new DTCategorias(categoriasList);

      try {
        ce.altaEvento(nombre, desc, LocalDate.now(), sigla, dtCategorias, sigla);

        // Asociar imagen (si se subió)
        if (imagenFileName != null) {
          try {
            ce.actualizarImagenEvento(nombre, imagenFileName); // contrato: guardamos SOLO nombre
          } catch (IllegalArgumentException ex) {
            System.err.println("No se pudo asociar imagen al evento: " + ex.getMessage());
          }
        }

        String nombreEnc = URLEncoder.encode(nombre, StandardCharsets.UTF_8.name());
        resp.sendRedirect(ctx(req) + "/evento/ConsultaEvento?nombre=" + nombreEnc);
      } catch (EventoYaExisteException e) {
        req.setAttribute("error", "duplicado");
        req.setAttribute("nombreEventoDuplicado", nombre);
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
      }
      return;
    }

    // Redirección desde "RegistrarseEvento" (usa consulta)
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
    } catch (Exception ignore) { }
    return o.toString();
  }

  private List<String> categoriasALista(Object cobj) {
    List<String> out = new ArrayList<>();
    if (cobj == null) return out;

    try {
      if (cobj instanceof java.util.Map<?,?> m) {
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

  // ====== helpers de archivo ======
  private static String getSafeFilename(Part p) {
    String name = p.getSubmittedFileName();
    if (name == null) return "archivo";
    name = name.replace("\\", "/");
    int slash = name.lastIndexOf('/');
    return (slash >= 0) ? name.substring(slash + 1) : name;
  }

  private static String getExtension(String filename) {
    if (filename == null) return null;
    int dot = filename.lastIndexOf('.');
    if (dot < 0 || dot == filename.length() - 1) return null;
    return filename.substring(dot).toLowerCase();
  }

  private static String guessExtensionFromContentType(String ctype) {
    if (ctype == null) return null;
    ctype = ctype.toLowerCase();
    if (ctype.contains("jpeg")) return ".jpg";
    if (ctype.contains("jpg"))  return ".jpg";
    if (ctype.contains("png"))  return ".png";
    if (ctype.contains("gif"))  return ".gif";
    if (ctype.contains("webp")) return ".webp";
    return null;
  }
}
