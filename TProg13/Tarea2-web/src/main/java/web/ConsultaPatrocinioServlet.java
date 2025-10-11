package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import logica.fabrica;
import logica.Interfaces.IControladorEvento;
import logica.Clases.Ediciones;
import logica.Clases.Patrocinio;

@WebServlet("/edicion/ConsultaPatrocinio")
public class ConsultaPatrocinioServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    String evento = req.getParameter("evento");
    String edicion = req.getParameter("edicion");
    String codigoPatrocinio = req.getParameter("codigoPatrocinio");
    if (evento == null || edicion == null || codigoPatrocinio == null) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan parámetros");
      return;
    }
    IControladorEvento ce = fabrica.getInstance().getIControladorEvento();
    Ediciones ed = ce.obtenerEdicion(evento, edicion);
    if (ed == null) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Edición no encontrada");
      return;
    }
    Patrocinio p = ed.getPatrocinio(codigoPatrocinio);
    if (p == null) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Patrocinio no encontrado");
      return;
    }
    req.setAttribute("patrocinio", p);
    req.getRequestDispatcher("/WEB-INF/patrocinio/ConsultaPatrocinio.jsp").forward(req, resp);
  }
}
