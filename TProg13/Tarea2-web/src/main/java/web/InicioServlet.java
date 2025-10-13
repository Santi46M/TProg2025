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
import logica.clases.Eventos;
import logica.fabrica;

@WebServlet({"/inicio"})
public class InicioServlet extends HttpServlet {

  private IControladorEvento ce() {
    return fabrica.getInstance().getIControladorEvento();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    System.out.println("✅ Entró al InicioServlet");
    IControladorEvento controladorEv = ce();
    List<DTEvento> eventos = controladorEv.listarEventos();
    System.out.println("Eventos listados: " + (eventos != null ? eventos.size() : "null"));

    if (eventos == null || eventos.isEmpty()) {
      getServletContext().setAttribute("datosPrecargados", Boolean.FALSE);
      System.out.println("⚠️ No hay eventos, datosPrecargados = FALSE");
    }

    // nombreEvento -> URL lista para <img src="">
    Map<String, String> imgUrls = new HashMap<>();
    String ctx = req.getContextPath();

    if (eventos != null) {
      for (DTEvento e : eventos) {
        String nombre = e.getNombre();
        String raw = null;

        // 1) intentar desde el DTO
        try { raw = e.getImagen(); } catch (Exception ignore) {}

        // 2) si no vino en el DTO, consultar el evento completo
        if (raw == null || raw.isBlank()) {
          try {
            Eventos eventIter = controladorEv.consultaEvento(nombre);
            if (eventIter != null) raw = eventIter.getImagen();
          } catch (Exception ex) { /* noop */ }
        }

        String url = null;
        if (raw != null && !raw.isBlank()) {
          if (raw.startsWith("http://") || raw.startsWith("https://")) {
            url = raw; // absoluta
          } else if (raw.startsWith("/")) {
            url = ctx + raw; // ya viene con ruta (/img/... u otra)
          } else {
            // Solo nombre de archivo:
            // Preferimos /img/<archivo> (estáticos precargados). Si no existe, /eventos/<archivo> (nuevos).
            String[] candidates = new String[] {
              "/img/" + raw,
              "/img/eventos/" + raw,   // por si tenés esta subcarpeta
              "/eventos/" + raw        // subidos por la app
            };

            for (String rel : candidates) {
              String abs = getServletContext().getRealPath(rel);
              boolean exists;
              if (abs != null) {
                exists = Files.exists(Path.of(abs));
              } else {
                // Si getRealPath es null (algunos entornos), igual elegimos el primer candidato
                // y dejamos que el servidor sirva si está disponible.
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
