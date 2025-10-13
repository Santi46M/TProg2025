package web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
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

    // Forzar listado: ?listar=1 / true / on / yes  o  ?view=list
    final boolean forzarListado = isTrue(request.getParameter("listar"))
        || "list".equalsIgnoreCase(trim(request.getParameter("view")));

    String nick = trim(request.getParameter("nick"));

    // Si no se fuerza listado y no vino nick, usar el de sesión (si hay)
    if (!forzarListado && isBlank(nick)) {
      HttpSession sAux = request.getSession(false);
      if (sAux != null) {
        Object obj = sAux.getAttribute("nick");
        if (obj instanceof String) nick = (String) obj;
      }
    }

    final IControladorUsuario ctrlUsuario = fabrica.getInstance().getIControladorUsuario();

    if (forzarListado || isBlank(nick)) {
      // ============ LISTA DE USUARIOS ============
      Map<String, Usuario> mapa = ctrlUsuario.listarUsuarios();
      Collection<Usuario> values = (mapa == null) ? List.of() : mapa.values();
      request.setAttribute("usuarios", values);

      // Fotos y nombres seguros para el listado
      final Map<String, String> fotos   = new HashMap<>();
      final Map<String, String> nombres = new HashMap<>();

      final String ctx = request.getContextPath();
      final ServletContext sc = getServletContext();

      for (Usuario u : values) {
        if (u == null) continue;

        // nombre seguro: si nombre está vacío → usar nickname
        final String nombreSeguro = nvl(u.getNombre(), u.getNickname());
        nombres.put(u.getNickname(), nombreSeguro);

        // foto (puede ser null)
        final String url = resolveUserImageUrl(u.getImagen(), ctx, sc);
        if (url != null) {
          fotos.put(u.getNickname(), url);
        }
      }

      request.setAttribute("fotos", fotos);
      request.setAttribute("nombres", nombres);

    } else {
      // ============ PERFIL INDIVIDUAL ============
      try {
        DTDatosUsuario usuario = ctrlUsuario.obtenerDatosUsuario(nick);
        request.setAttribute("usuario", usuario);

        // Nombre seguro también para el perfil (por si viene null)
        request.setAttribute("usuarioNombreSeguro",
            nvl(usuario.getNombre(), usuario.getNickname()));

        // Foto
        final String url = resolveUserImageUrl(usuario.getImagen(),
                                               request.getContextPath(),
                                               getServletContext());
        if (url != null) {
          request.setAttribute("usrImagenUrl", url);
        }

        // Mapa edicion -> evento (para construir links)
        final IControladorEvento controladorEv = fabrica.getInstance().getIControladorEvento();
        final List<DTEvento> eventos = controladorEv.listarEventos();
        final Map<String, String> edicionToEvento = new HashMap<>();

        if (eventos != null) {
          for (DTEvento ev : eventos) {
            if (ev == null || ev.getEdiciones() == null) continue;
            for (String edNombre : ev.getEdiciones()) {
              if (edNombre != null && !edNombre.isBlank()) {
                edicionToEvento.put(edNombre, ev.getNombre());
              }
            }
          }
        }
        request.setAttribute("edicionToEvento", edicionToEvento);

      } catch (UsuarioNoExisteException unee) {
        // Usuario no encontrado: 404 + mensaje, y mostramos listado para navegar
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        request.setAttribute("error", "El usuario \"" + nick + "\" no existe.");

        Map<String, Usuario> mapa = ctrlUsuario.listarUsuarios();
        request.setAttribute("usuarios", (mapa == null) ? List.of() : mapa.values());
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
  private static String trim(String s) { return (s == null) ? null : s.trim(); }
  private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
  private static String nvl(String s, String alt) {
    return (s == null || s.trim().isEmpty()) ? alt : s;
  }
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
   *    * "img/..." o "img/usuarios/..." → ctx + "/" + esa ruta.
   *    * "usuarios/..."                 → ctx + "/img/" + esa ruta.
   *    * Solo nombre → intenta, en orden verificado:
   *        /img/<archivo>
   *        /img/usuarios/<archivo>
   *      Si no se puede verificar (getRealPath null), por defecto /img/<archivo>.
   */
  private static String resolveUserImageUrl(String raw, String ctx, ServletContext sc) {
    if (raw == null || raw.isBlank()) return null;

    final String v = raw.trim().replace("\\", "/");
    final String low = v.toLowerCase(Locale.ROOT);

    if (low.startsWith("http://") || low.startsWith("https://")) return v;
    if (v.startsWith("/")) return ctx + v;

    if (low.startsWith("img/")) {
      return ctx + "/" + v;
    }
    if (low.startsWith("usuarios/")) {
      return ctx + "/img/" + v;
    }

    // Solo nombre de archivo → probar posibles ubicaciones
    final String relImg = "/img/" + v;
    final String relUsr = "/img/usuarios/" + v;

    final Boolean exImg = exists(sc, relImg);
    final Boolean exUsr = exists(sc, relUsr);

    if (exImg != null || exUsr != null) {
      if (isTrue(exImg)) return ctx + relImg;
      if (isTrue(exUsr)) return ctx + relUsr;
      return null;
    }

    // No se pudo verificar (WAR no explotado) → fallback a /img/
    return ctx + relImg;
  }

  /** TRUE/FALSE si se pudo verificar, o null si no se puede (getRealPath == null). */
  private static Boolean exists(ServletContext sc, String rel) {
    final String abs = sc.getRealPath(rel);
    if (abs == null) return null;
    return Files.exists(Path.of(abs));
  }

  private static boolean isTrue(Boolean b) { return b != null && b; }
}
