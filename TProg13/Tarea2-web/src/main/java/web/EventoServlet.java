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
import logica.clases.Ediciones;
import excepciones.EventoYaExisteException;
import logica.controladores.ControladorEvento;

@WebServlet("/evento/*")
@MultipartConfig(
    maxFileSize = 2 * 1024 * 1024,
    maxRequestSize = 4 * 1024 * 1024
)
public class EventoServlet extends HttpServlet {

  private static final String JSP_ALTA     = "/WEB-INF/evento/alta.jsp";
  private static final String JSP_CONSULTA = "/WEB-INF/evento/ConsultaEvento.jsp";
  private static final String JSP_REGISTRO = "/WEB-INF/evento/RegistrarseEvento.jsp";
  private static final String JSP_LISTAR   = "/WEB-INF/evento/listado.jsp";

  // Carpeta pública para imágenes (URL: <ctx>/eventos/archivo.ext)
  private static final String IMG_REL_BASE = "/eventos";

  private final IControladorEvento controladorEv = fabrica.getInstance().getIControladorEvento();
  private String ctx(HttpServletRequest req) { return req.getContextPath(); }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    String path = req.getPathInfo();

    if (path == null || "/".equals(path) || "/ConsultaEvento".equals(path)) {
      String nombre = trim(req.getParameter("nombre"));
      if (isBlank(nombre)) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'nombre'");
        return;
      }
      Eventos eventIter = controladorEv.consultaEvento(nombre);
      if (eventIter == null) {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Evento no encontrado: " + nombre);
        return;
      }

      // Atributos base para JSP
      req.setAttribute("evNombre", safe(() -> eventIter.getNombre()));
      req.setAttribute("evSigla",  safe(() -> eventIter.getSigla()));
      req.setAttribute("evDesc",   safe(() -> eventIter.getDescripcion()));
      req.setAttribute("evFecha",  formatFecha(safeObj(() -> eventIter.getFecha())));
      req.setAttribute("evCategorias", categoriasALista(safeObj(() -> eventIter.getCategorias())));

      // === Imagen: resolver URL final igual que en Inicio ===
      String raw = eventIter.getImagen(); // puede ser "archivo.jpg" o "/img/archivo.jpg" o "/eventos/archivo.jpg" o URL absoluta
      req.setAttribute("evImagen", raw); // compat por si alguna JSP lo usa así

      String imgUrl = null;
      String ctxPath = ctx(req);

      if (raw != null && !raw.isBlank()) {
        if (raw.startsWith("http://") || raw.startsWith("https://")) {
          imgUrl = raw;                       // absoluta
        } else if (raw.startsWith("/")) {
          imgUrl = ctxPath + raw;             // relativa ya formada (/img/..., /eventos/...)
        } else {
          // Solo nombre de archivo: intentamos rutas conocidas
          String[] candidates = new String[] {
            "/img/" + raw,
            "/img/eventos/" + raw,
            "/eventos/" + raw
          };
          for (String rel : candidates) {
            String abs = getServletContext().getRealPath(rel);
            boolean exists;
            if (abs != null) {
              exists = java.nio.file.Files.exists(java.nio.file.Path.of(abs));
            } else {
              // Si getRealPath es null (exploded false), no bloqueamos la visualización
              exists = true;
            }
            if (exists) { imgUrl = ctxPath + rel; break; }
          }
        }
      }
      if (imgUrl != null) {
        req.setAttribute("evImagenUrl", imgUrl);
      }
      // === fin imagen ===

      // 🟢 Obtener ediciones asociadas al evento directamente del objeto real
      try {
        List<DTEdicion> ediciones = new ArrayList<>();

        if (eventIter.getEdiciones() != null && !eventIter.getEdiciones().isEmpty()) {
          for (Ediciones ed : eventIter.getEdiciones().values()) {
            String organizadorNick = null;
            if (ed.getOrganizador() != null) {
              organizadorNick = ed.getOrganizador().getNickname();
            }
            String ciudad = ed.getCiudad() != null ? ed.getCiudad() : "(sin ciudad)";
            String pais   = ed.getPais() != null ? ed.getPais() : "(sin país)";
            DTEdicion dte = new DTEdicion(
              ed.getNombre(),
              ed.getSigla(),
              ed.getFechaInicio(),
              ed.getFechaFin(),
              ed.getFechaAlta(),
              organizadorNick,
              ciudad,
              pais
            );
            ediciones.add(dte);
            System.out.println("[EventoServlet] Edición cargada desde evento: " + ed.getNombre());
          }
        } else {
          System.out.println("[EventoServlet] No hay ediciones en el evento: " + nombre);
        }

        req.setAttribute("evEdiciones", ediciones);
      } catch (Exception ex) {
        System.err.println("[EventoServlet] Error al obtener ediciones del evento " + nombre + ": " + ex.getMessage());
        req.setAttribute("evEdiciones", java.util.Collections.emptyList());
      }

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
      case "/listado": {
        String cat = trim(req.getParameter("categoria"));

        List<DTEvento> lista;
        if (!isBlank(cat)) {
          lista = controladorEv.listarEventosPorCategoria(cat);
          req.setAttribute("categoriaSeleccionada", cat);
        } else {
          lista = controladorEv.listarEventos();
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

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    String path = req.getPathInfo();

    // Cancelar: redirige a inicio sin validaciones (mantener botón sin <a href>)
    String accion = req.getParameter("accion");
    if ("cancelar".equalsIgnoreCase(accion)) {
      resp.sendRedirect(ctx(req) + "/inicio");
      return;
    }

    if ("/alta".equals(path)) {
      if (!requiereOrganizador(req, resp)) return;

      String nombre = trim(req.getParameter("nombre"));
      String desc   = trim(req.getParameter("desc"));
      String sigla  = trim(req.getParameter("sigla"));
      String cats   = trim(req.getParameter("categorias"));

      List<String> categoriasList = new ArrayList<>();
      if (!isBlank(cats)) {
        for (String cAux : cats.split(",")) {
          String tAux = cAux.trim();
          if (!tAux.isEmpty()) categoriasList.add(tAux);
        }
      }
      if (categoriasList.isEmpty()) {
        req.setAttribute("error", "Debe asociar al menos una categoría al evento");
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
        return;
      }

      // Guarda la imagen en /eventos y persiste SOLO el nombre (ej: "MISIGLA.jpg")
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

          String baseImg = getServletContext().getRealPath(IMG_REL_BASE);
          if (baseImg == null) {
            String root = getServletContext().getRealPath("/");
            if (root != null) baseImg = Path.of(root, "eventos").toString();
          }

          if (baseImg != null) {
            Files.createDirectories(Path.of(baseImg));

            String original = getSafeFilename(imgPart);
            String ext = getExtension(original);
            if (isBlank(ext)) ext = guessExtensionFromContentType(ctype);
            if (isBlank(ext)) ext = ".jpg"; // fallback seguro

            String finalName = (isBlank(sigla) ? "evento" : sigla) + ext;
            Path destino = Path.of(baseImg, finalName);
            imgPart.write(destino.toAbsolutePath().toString());

            imagenFileName = finalName; // persistimos solo el nombre
            System.out.println("[IMG] Guardada en: " + destino + " | URL: " + ctx(req) + IMG_REL_BASE + "/" + finalName);
          } else {
            System.err.println("WARN: No se pudo resolver ruta física para " + IMG_REL_BASE);
          }
        }
      } catch (Exception fileEx) {
        req.setAttribute("error", "Error al procesar la imagen: " + fileEx.getMessage());
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
        return;
      }

      DTCategorias dtCategorias = new DTCategorias(categoriasList);

      try {
        controladorEv.altaEvento(nombre, desc, LocalDate.now(), sigla, dtCategorias, sigla);

        if (imagenFileName != null) {
          try {
            controladorEv.actualizarImagenEvento(nombre, imagenFileName);
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

  private boolean requiereOrganizador(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession sAux = req.getSession(false);
    String rol = sAux == null ? null : (String) sAux.getAttribute("rol");
    if (!"ORGANIZADOR".equals(rol)) {
      resp.sendRedirect(ctx(req) + "/auth/login");
      return false;
    }
    return true;
  }

  private boolean requiereLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession sAux = req.getSession(false);
    if (sAux == null || sAux.getAttribute("nick") == null) {
      resp.sendRedirect(ctx(req) + "/auth/login");
      return false;
    }
    return true;
  }

  private static String trim(String sAux){ return sAux == null ? null : sAux.trim(); }
  private static boolean isBlank(String sAux){ return sAux == null || sAux.trim().isEmpty(); }

  @FunctionalInterface
  private interface SCall { Object get() throws Exception; }

  private String safe(SCall cAux) {
    try { Object vAux = cAux.get(); return vAux == null ? null : vAux.toString(); }
    catch (Exception e) { return null; }
  }

  private Object safeObj(SCall cAux) {
    try { return cAux.get(); } catch (Exception e) { return null; }
  }

  private String extractNombre(Object oAux) {
    if (oAux == null) return "—";
    try {
      var mAux = oAux.getClass().getMethod("getNombre");
      Object vAux = mAux.invoke(oAux);
      return vAux == null ? "—" : vAux.toString();
    } catch (Exception ignore) { }
    return oAux.toString();
  }

  private List<String> categoriasALista(Object cobj) {
    List<String> out = new ArrayList<>();
    if (cobj == null) return out;

    try {
      if (cobj instanceof java.util.Map<?,?> mAux) {
        for (Object kAux : mAux.keySet()) if (kAux != null) out.add(kAux.toString());
        if (out.isEmpty()) for (Object v : mAux.values()) out.add(extractNombre(v));
        return out;
      }
      if (cobj instanceof java.util.Collection<?> col) {
        for (Object v : col) out.add(extractNombre(v));
        return out;
      }
      if (cobj.getClass().isArray()) {
        int nAux = java.lang.reflect.Array.getLength(cobj);
        for (int i=0;i<nAux;i++) out.add(extractNombre(java.lang.reflect.Array.get(cobj,i)));
        return out;
      }
      if (cobj instanceof String sAux) {
        for (String pAux : sAux.split("[,;]")) {
          String tAux = pAux.trim(); if (!tAux.isEmpty()) out.add(tAux);
        }
        return out;
      }
    } catch (Exception ignore) {}
    out.add(cobj.toString());
    return out;
  }

  private String formatFecha(Object fAux) {
    if (fAux == null) return null;
    try {
      if (fAux instanceof java.time.LocalDate ident) {
        return ident.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      }
      if (fAux instanceof java.time.LocalDateTime ident2) {
        return ident2.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
      }
      if (fAux instanceof java.util.Date dateAux) {
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(dateAux);
      }
    } catch (Exception ignore) {}
    return fAux.toString();
  }

  // ====== helpers de archivo ======
  private static String getSafeFilename(Part partAux) {
    String name = partAux.getSubmittedFileName();
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
