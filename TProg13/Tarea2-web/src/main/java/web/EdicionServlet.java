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

  // Carpeta pública para imágenes de ediciones (URL final: <ctx>/ediciones/archivo.ext)
  private static final String IMG_REL_BASE_ED = "/ediciones";

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

      // -------- IMAGEN: resolver URL final (igual que en Inicio/Evento) ----------
      String raw = edicionObj.getImagen(); // puede ser "archivo.jpg" o ruta "/img/... /ediciones/..."
      String imgUrl = null;
      String ctxPath = ctx(req);

      if (raw != null && !raw.isBlank()) {
        if (raw.startsWith("http://") || raw.startsWith("https://")) {
          imgUrl = raw;                       // absoluta
        } else if (raw.startsWith("/")) {
          imgUrl = ctxPath + raw;             // ya viene con ruta
        } else {
          String[] candidates = new String[] {
            "/img/" + raw,
            "/img/ediciones/" + raw,
            IMG_REL_BASE_ED + "/" + raw      // subidas por la app
          };
          for (String rel : candidates) {
            String abs = getServletContext().getRealPath(rel);
            boolean exists;
            if (abs != null) {
              exists = java.nio.file.Files.exists(java.nio.file.Path.of(abs));
            } else {
              exists = true; // en algunos runtimes getRealPath() puede ser null; no bloqueamos
            }
            if (exists) { imgUrl = ctxPath + rel; break; }
          }
        }
      }
      if (imgUrl != null) {
        req.setAttribute("edImagenUrl", imgUrl);
      }
      // ---------------------------------------------------------------------------

      // Registros visibles (organizador: todos; asistente: sólo el suyo)
      HttpSession session = req.getSession(false);
      String nickSesion = session != null ? (String) session.getAttribute("nick") : null;
      boolean esOrganizador = nickSesion != null
          && edicionObj.getOrganizador() != null
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
            if (r.getEdicion() != null
                && r.getEdicion().getNombre().equals(edicionObj.getNombre())
                && r.getEdicion().getEvento().getNombre().equals(edicionObj.getEvento().getNombre())) {
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
        req.setAttribute("listaEdiciones", ce().listarEdicionesEvento(evento));
        req.getRequestDispatcher(JSP_LISTADO).forward(req, resp);
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

      // ---- Guardar imagen (opcional) en /ediciones y persistir SOLO el nombre ----
      String imagenFileName = null;
      try {
        if (imagen != null && imagen.getSize() > 0) {
          String ctype = imagen.getContentType();
          if (ctype == null || !ctype.toLowerCase().startsWith("image/")) {
            req.setAttribute("error", "El archivo subido no es una imagen válida.");
            req.setAttribute("listaEventos", ce().listarEventos());
            req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
            return;
          }
          String baseImg = getServletContext().getRealPath(IMG_REL_BASE_ED);
          if (baseImg == null) {
            String root = getServletContext().getRealPath("/");
            if (root != null) baseImg = Path.of(root, "ediciones").toString();
          }
          if (baseImg != null) {
            Files.createDirectories(Path.of(baseImg));

            String original = getSafeFilename(imagen);
            String ext = getExtension(original);
            if (ext == null || ext.isBlank()) ext = guessExtensionFromContentType(ctype);
            if (ext == null || ext.isBlank()) ext = ".jpg";

            // Usamos el nombre de la edición para el archivo
            String finalName = (isBlank(nombre) ? "edicion" : nombre) + ext;
            Path destino = Path.of(baseImg, finalName);
            imagen.write(destino.toAbsolutePath().toString());

            imagenFileName = finalName; // persistimos solo el NOMBRE
            System.out.println("[IMG-ED] Guardada en: " + destino + " | URL: " + ctx(req) + IMG_REL_BASE_ED + "/" + finalName);
          }
        }
      } catch (Exception ex) {
        req.setAttribute("error", "Error al procesar la imagen: " + ex.getMessage());
        req.setAttribute("listaEventos", ce().listarEventos());
        req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
        return;
      }
      // --------------------------------------------------------------------------

      try {
        Usuario org = getUsuario(req);
        if (org == null || !"ORGANIZADOR".equals(getRol(req))) {
          req.setAttribute("error", "Debés iniciar sesión como organizador.");
          req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
          return;
        }

        Eventos evObj = ce().consultaEvento(evento);

        // Ajustá la firma si tu método difiere; aquí asumimos que el último parámetro es imagen
        ce().altaEdicionEvento(evObj, org, nombre, nombre, desc, ini, fin,
                               LocalDate.now(), ciudad, pais, imagenFileName);

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

  // ===== Auth helper =====
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
