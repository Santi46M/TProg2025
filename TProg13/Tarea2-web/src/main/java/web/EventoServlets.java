package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import logica.fabrica;
import logica.Interfaces.IControladorEvento;
import logica.Datatypes.DTEvento;
import logica.Clases.Eventos;
// import logica.Datatypes.DTCategorias; // ← si lo necesitás para alta
import excepciones.EventoYaExisteException;

@WebServlet("/evento/*")
public class EventoServlets extends HttpServlet {
  private final IControladorEvento ce = fabrica.getInstance().getIControladorEvento();

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
    String path = req.getPathInfo(); // null,"/","/listar","/alta","/consulta"

    if (path == null || "/".equals(path)) {
      // Consulta por nombre (según tu interfaz)
      String nombre = req.getParameter("nombre");
      if (nombre == null || nombre.isBlank()) { resp.sendError(400, "Falta nombre"); return; }
      Eventos evento = ce.consultaEvento(nombre); // objeto de dominio
      req.setAttribute("evento", evento);
      req.getRequestDispatcher("/eventos/consulta.jsp").forward(req, resp);
      return;
    }

    switch (path) {
      case "/listar": {
        List<DTEvento> lista = ce.listarEventos();
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

      // Inputs esperados en eventos/alta.jsp:
      // nombre, desc, sigla, categorias (p.ej: "IA,Redes")
      String nombre = req.getParameter("nombre");
      String desc   = req.getParameter("desc");
      String sigla  = req.getParameter("sigla");
      String cats   = req.getParameter("categorias");

      if (nombre == null || nombre.isBlank() || sigla == null || sigla.isBlank()) {
        req.setAttribute("error", "Nombre y sigla son requeridos.");
        req.getRequestDispatcher("/eventos/alta.jsp").forward(req, resp);
        return;
      }

      try {
        // TODO: construir DTCategorias según cómo lo definieron ustedes.
        // Ejemplo (si existiera un ctor / factory):
        // DTCategorias categorias = DTCategorias.fromCSV(cats);
        // ce.AltaEvento(nombre, desc, LocalDate.now(), sigla, categorias);

        // Por ahora, dejo sin categorías para que compile si DTCategorias no tiene ctor público:
        // ce.AltaEvento(nombre, desc, LocalDate.now(), sigla, null);

        // Si sí tienen el DTO, descomentá la línea correcta de arriba y borrá esta:
        ce.AltaEvento(nombre, desc, LocalDate.now(), sigla, /*categorias*/ null);

        resp.sendRedirect(ctx(req)+"/evento?nombre="+nombre);
      } catch (EventoYaExisteException e) {
        req.setAttribute("error", e.getMessage());
        req.getRequestDispatcher("/eventos/alta.jsp").forward(req, resp);
      }
      return;
    }

    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
  }
}
