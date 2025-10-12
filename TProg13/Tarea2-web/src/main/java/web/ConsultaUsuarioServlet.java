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

@WebServlet("/usuario/ConsultaUsuario")
public class ConsultaUsuarioServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    String nick = trim(request.getParameter("nick"));
    IControladorUsuario ctrlUsuario = fabrica.getInstance().getIControladorUsuario();

    try {
      if (nick != null && !nick.isEmpty()) {
        // === PERFIL DE USUARIO ===
        DTDatosUsuario usuario = ctrlUsuario.obtenerDatosUsuario(nick);
        request.setAttribute("usuario", usuario);

        // Construir un mapa auxiliar: edicion → evento
        IControladorEvento ce = fabrica.getInstance().getIControladorEvento();
        List<DTEvento> eventos = ce.listarEventos();
        Map<String, String> edicionToEvento = new HashMap<>();

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
      } else {
        // === LISTADO DE USUARIOS ===
        Map<String, Usuario> usuarios = ctrlUsuario.listarUsuarios();
        request.setAttribute("usuarios", 
            usuarios == null ? List.of() : usuarios.values());
      }

    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute("error", "Error al consultar el usuario: " + e.getMessage());
    }

    request.getRequestDispatcher("/WEB-INF/usuario/ConsultaUsuario.jsp").forward(request, response);
  }

  private static String trim(String s) {
    return s == null ? null : s.trim();
  }
}
