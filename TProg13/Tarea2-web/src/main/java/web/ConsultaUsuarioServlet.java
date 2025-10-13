package web;

import java.io.IOException;
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
import excepciones.UsuarioNoExisteException; // <-- IMPORTANTE

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
      request.setAttribute("usuarios", usuarios == null ? List.of() : usuarios.values());
    } else {
      // === PERFIL ===
      try {
        DTDatosUsuario usuario = ctrlUsuario.obtenerDatosUsuario(nick);
        request.setAttribute("usuario", usuario);

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
        // Usuario no encontrado: 404 + mensaje para la JSP
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        request.setAttribute("error", "El usuario \"" + nick + "\" no existe.");
        // opcional: también mostrar listado para facilitar navegación:
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
  private static String trim(String sAux) { return sAux == null ? null : sAux.trim(); }
  private static boolean isBlank(String sAux) { return sAux == null || sAux.trim().isEmpty(); }
  private static boolean isTrue(String vAux) {
    if (vAux == null) return false;
    String sAux = vAux.trim().toLowerCase(java.util.Locale.ROOT);
    return "1".equals(sAux) || "true".equals(sAux) || "on".equals(sAux) || "yes".equals(sAux) || "y".equals(sAux);
  }
}
