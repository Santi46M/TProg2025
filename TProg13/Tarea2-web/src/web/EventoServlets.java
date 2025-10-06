package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

import logica.Fabrica;
import logica.IControladorEvento;
import dtos.DTEvento;
import excepciones.EventoRepetidoException;

@WebServlet("/evento/*")
public class EventoServlet extends HttpServlet {
  private final IControladorEvento ce = Fabrica.getInstancia().getControladorEvento();

  private String ctx(HttpServletRequest req) { return req.getContextPath(); }

  private boolean requiereOrganizador(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession s = req.getSession(false);
    String rol = s == null ? null : (String) s.getAttribute("rol");
    if (!"ORGANIZADOR".equals(rol)) {
      resp.sendRedirect(ctx(req)+"/auth/login");
      return false;
    }
    return true;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String path = req.getPathInfo(); // null,"/","/listar","/alta"

    if (path == null || "/".equals(path)) {
      // /evento?id=EV123
      String id = req.getParameter("id");
      if (id == null || id.isBlank()) { resp.sendError(400, "Falta id"); return; }
      DTEvento dto = ce.obtenerDTEvento(id);        // <-- tu lógica
      req.setAttribute("dto", dto);
      req.getRequestDispatcher("/eventos/consulta.jsp").forward(req, resp);
      return;
    }

    switch (path) {
      case "/listar": {
        List<DTEvento> lista = ce.listarEventos();  // <-- tu lógica
        req.setAttribute("lista", lista);
        req.getRequestDispatcher("/eventos/listar.jsp").forward(req, resp);
        break;
      }
      case "/alta":
        if (!requiereOrganizador(req, resp)) return;
        req.getRequestDispatcher("/eventos/alta.jsp").forward(req, resp);
        break;
      default:
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String path = req.getPathInfo();

    if ("/alta".equals(path)) {
      if (!requiereOrganizador(req, resp)) return;

      // Nombres de inputs en eventos/alta.jsp:
      // nombre, categorias (ej: "IA,Redes"), urlImagen (o file si luego haces multipart)
      String nombre = req.getParameter("nombre");
      String categorias = req.getParameter("categorias");
      String urlImagen = req.getParameter("urlImagen");

      if (nombre == null || nombre.isBlank()) {
        req.setAttribute("error", "Nombre requerido");
        req.getRequestDispatcher("/eventos/alta.jsp").forward(req, resp);
        return;
      }

      try {
        String id = ce.altaEvento(nombre, categorias, urlImagen); // <-- tu lógica (devuelve id)
        resp.sendRedirect(ctx(req)+"/evento?id="+id);
      } catch (EventoRepetidoException e) {
        req.setAttribute("error", e.getMessage());
        req.getRequestDispatcher("/eventos/alta.jsp").forward(req, resp);
      }
      return;
    }

    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
  }
}
