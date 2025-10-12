package web;

import java.io.IOException;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import logica.fabrica;
import logica.Clases.Usuario;
import logica.Datatypes.DTDatosUsuario;
import logica.Datatypes.DTEvento;
import logica.Interfaces.IControladorUsuario;
import logica.Interfaces.IControladorEvento;

@WebServlet("/usuario/ConsultaUsuario")
public class ConsultaUsuarioServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String nick = trim(request.getParameter("nick"));

    IControladorUsuario ctrlUsuario = fabrica.getInstance().getIControladorUsuario();

    if (nick != null && !nick.isEmpty()) {
      try {
        DTDatosUsuario usuario = ctrlUsuario.obtenerDatosUsuario(nick);
        request.setAttribute("usuario", usuario);

        // === Mapa edición -> evento (para poder llamar cambiarEstadoEdicion(evento, edicion, ...)) ===
        IControladorEvento ce = fabrica.getInstance().getIControladorEvento();
        List<DTEvento> eventos = ce.listarEventos(); // ya lo usás en otros lados
        Map<String, String> edicionToEvento = new HashMap<>();

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

      } catch (Exception e) {
        request.setAttribute("error", "Usuario no encontrado: " + nick);
      }
    } else {
      Map<String, Usuario> usuarios = ctrlUsuario.listarUsuarios();
      request.setAttribute("usuarios", usuarios == null ? List.of() : usuarios.values());
    }

    request.getRequestDispatcher("/WEB-INF/usuario/ConsultaUsuario.jsp").forward(request, response);
  }

  private static String trim(String s){ return s == null ? null : s.trim(); }
}
