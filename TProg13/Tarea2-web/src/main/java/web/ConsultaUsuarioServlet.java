package web;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import logica.fabrica;
import logica.clases.Usuario;
import logica.datatypes.DTDatosUsuario;
import logica.datatypes.DTEvento;
import logica.interfaces.IControladorUsuario;
import logica.interfaces.IControladorEvento;
import excepciones.UsuarioNoExisteException;

@WebServlet("/usuario/ConsultaUsuario")
public class ConsultaUsuarioServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");

    final boolean forzarListado = isTrue(request.getParameter("listar"))
        || "list".equalsIgnoreCase(trim(request.getParameter("view")));

    String nick = trim(request.getParameter("nick"));

    if (!forzarListado && isBlank(nick)) {
      HttpSession sAux = request.getSession(false);
      if (sAux != null) nick = (String) sAux.getAttribute("nick");
    }

    final IControladorUsuario ctrlUsuario = fabrica.getInstance().getIControladorUsuario();

    if (forzarListado || isBlank(nick)) {
      // === LISTA ===
      Map<String, Usuario> usuarios = ctrlUsuario.listarUsuarios();
      Collection<Usuario> values = usuarios == null ? List.of() : usuarios.values();
      request.setAttribute("usuarios", values);

      // Resolver URLs de fotos para el listado (clave = nick)
      Map<String,String> fotos = new HashMap<>();
      String ctx = request.getContextPath();
      ServletContext sc = getServletContext();

      for (Usuario u : values) {
        String raw = null;
        try { raw = u.getImagen(); } catch (Exception ignore) {}
        String url = resolveUserImageUrl(raw, ctx, sc);
        if (url != null) fotos.put(u.getNickname(), url);
      }
      request.setAttribute("fotos", fotos);

    } else {
      // === PERFIL ===
      try {
        DTDatosUsuario usuario = ctrlUsuario.obtenerDatosUsuario(nick);
        request.setAttribute("usuario", usuario);

        String raw = null;
        try { raw = usuario.getImagen(); } catch (Exception ignore) {}
        String url = resolveUserImageUrl(raw, request.getContextPath(), getServletContext());
        if (url != null) request.setAttribute("usrImagenUrl", url);

        // Mapa edicion -> evento para armar links
        final IControladorEvento controladorEv = fabrica.getInstance().getIControladorEvento();
        final List<DTEvento> eventos = controladorEv.listarEventos();
        final Map<String, String> edicionToEvento = new HashMap<>();
        if (eventos != null) {
          for (DTEvento ev : eventos) {
            if (ev != null && ev.getEdiciones() != null) {
              for (String edNombre : ev.getEdiciones()) {
                if (edNombre != null && !edNombre.isBlank()) {
                  edicionToEvento.put(edNombre, ev.getNombre());
                }
              }
            }
          }
        }
        request.setAttribute("edicionToEvento", edicionToEvento);

      } catch (UsuarioNoExisteException unee) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        request.setAttribute("error", "El usuario \"" + nick + "\" no existe.");
        Map<String, Usuario> usuarios = ctrlUsuario.listarUsuarios();
        request.setAttribute("usuarios", usuarios == null ? List.of() : usuarios.values());
      }
    }

    request.getRequestDispatcher("/WEB-INF/usuario/ConsultaUsuario.jsp")
           .forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  // ===== Helpers =====
  private static String trim(String s) { return s == null ? null : s.trim(); }
  private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
  private static boolean isTrue(String v) {
    if (v == null) return false;
    String s = v.trim().toLowerCase(java.util.Locale.ROOT);
    return "1".equals(s) || "true".equals(s) || "on".equals(s) || "yes".equals(s) || "y".equals(s);
  }

  /**
   * Resuelve la URL final de la imagen del usuario.
   * Reglas:
   * - http/https → tal cual.
   * - Empieza con "/" → ctx + raw.
   * - Si no empieza con "/", normaliza:
   *    * Acepta "img/..." o "img/usuarios/..." → ctx + "/" + esa ruta.
   *    * Acepta "usuarios/..."                 → ctx + "/img/" + esa ruta.
   *    * Si es solo nombre → intenta, en orden verificado:
   *        /img/<archivo>  (estáticos)
   *        /img/usuarios/<archivo> (subidos)
   *      Si no se puede verificar (getRealPath null), prefiero /img/<archivo>.
   */
  private static String resolveUserImageUrl(String raw, String ctx, ServletContext sc) {
    if (raw == null || raw.isBlank()) return null;

    String v = raw.trim().replace("\\", "/");
    String low = v.toLowerCase(Locale.ROOT);

    // Absolutas
    if (low.startsWith("http://") || low.startsWith("https://")) return v;
    // Ya relativas al contexto
    if (v.startsWith("/")) return ctx + v;

    // Rutas parciales comunes (evitar duplicar /usuarios/)
    if (low.startsWith("img/")) {
      return ctx + "/" + v; // ej: "img/IMG-US01.jpg" o "img/usuarios/IMG..."
    }
    if (low.startsWith("usuarios/")) {
      return ctx + "/img/" + v; // ej: "usuarios/IMG..." → "/img/usuarios/IMG..."
    }

    // Solo nombre de archivo → probar en disco
    String relImg = "/img/" + v;
    String relUsr = "/img/usuarios/" + v;

    Boolean exImg = exists(sc, relImg);
    Boolean exUsr = exists(sc, relUsr);

    if (exImg != null || exUsr != null) {
      if (isTrue(exImg)) return ctx + relImg;
      if (isTrue(exUsr)) return ctx + relUsr;
      return null; // ninguno existe físicamente
    }

    // No podemos verificar (WAR no explotado): prefiero estáticos por defecto
    return ctx + relImg;
  }

  /** TRUE/FALSE si se pudo verificar, o null si no se puede (getRealPath == null). */
  private static Boolean exists(ServletContext sc, String rel) {
    String abs = sc.getRealPath(rel);
    if (abs == null) return null;
    return Files.exists(Path.of(abs));
  }

  private static boolean isTrue(Boolean b) { return b != null && b; }
}
