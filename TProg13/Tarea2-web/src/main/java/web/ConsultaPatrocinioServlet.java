package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import logica.fabrica;
import logica.interfaces.IControladorEvento;
import logica.clases.Ediciones;
import logica.clases.Patrocinio;

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
    IControladorEvento controladorEv = fabrica.getInstance().getIControladorEvento();
    Ediciones edicionIter = controladorEv.obtenerEdicion(evento, edicion);
    if (edicionIter == null) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Edición no encontrada");
      return;
    }
    Patrocinio patro = edicionIter.getPatrocinio(codigoPatrocinio);
    if (patro == null) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Patrocinio no encontrado");
      return;
    }
    req.setAttribute("patrocinio", patro);
    req.getRequestDispatcher("/WEB-INF/patrocinio/ConsultaPatrocinio.jsp").forward(req, resp);
  }
}
