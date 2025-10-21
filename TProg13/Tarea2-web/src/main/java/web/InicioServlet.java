package web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import logica.interfaces.IControladorEvento;
import logica.datatypes.DTEvento;
import logica.fabrica;

@WebServlet({"/inicio"})
public class InicioServlet extends HttpServlet {

  private IControladorEvento ce() {
    return fabrica.getInstance().getIControladorEvento();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    IControladorEvento controladorEv = ce();
//    List<DTEvento> eventos = controladorEv.listarEventos();
    List<DTEvento> eventos = controladorEv.listarEventosVigentes();

    if (eventos == null || eventos.isEmpty()) {
      getServletContext().setAttribute("datosPrecargados", Boolean.FALSE);
    }

    // nombreEvento -> urlImagen
    Map<String, String> imgUrls = new HashMap<>();
    String ctx = req.getContextPath();

    if (eventos != null) {
      for (DTEvento e : eventos) {
        String nombre = e.getNombre();
        System.out.println("Nombre del evento" + nombre);
        String raw = null;

        //  intentar desde el DTO
        try { raw = e.getImagen(); } catch (Exception ignore) {}

        //  si no vino en el DTO, consultar el evento completo
        if (raw == null || raw.isBlank()) {
          try {
            DTEvento eventIter = controladorEv.consultaDTEvento(nombre);
            if (eventIter != null) raw = eventIter.getImagen();
          } catch (Exception ex) { /* noop */ }
        }

        String url = null;
        if (raw != null && !raw.isBlank()) {
          if (raw.startsWith("http://") || raw.startsWith("https://")) {
            url = raw; 
          } else if (raw.startsWith("/")) {
            url = ctx + raw; // ya viene con ruta (/img/ o otra)
          } else {
            // Solo nombre de archivo:
            String[] candidates = new String[] {
              "/img/" + raw,
              "/img/eventos/" + raw,  
              "/eventos/" + raw        // subidos por la app
            };

            for (String rel : candidates) {
              String abs = getServletContext().getRealPath(rel);
              boolean exists;
              if (abs != null) {
                exists = Files.exists(Path.of(abs));
              } else {
                exists = true; // asumimos true para no bloquear
              }
              if (exists) {
                url = ctx + rel;
                break;
              }
            }
          }
        }

        imgUrls.put(nombre, url); // puede ser null si realmente no hay imagen
      }
    }

    req.setAttribute("eventos", eventos);
    req.setAttribute("imgUrls", imgUrls);
    req.getRequestDispatcher("/index.jsp").forward(req, resp);
  }
}
