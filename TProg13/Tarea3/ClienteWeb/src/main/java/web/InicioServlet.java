package web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
//import logica.interfaces.IControladorEvento;
//import logica.datatypes.DTEvento;
import publicadores.DtEvento;
import publicadores.PublicadorEventoService;
//import logica.fabrica;
import publicadores.PublicadorEvento;


@WebServlet({"/inicio"})
public class InicioServlet extends HttpServlet {

    // ✅ mantenemos los publicadores
    private final PublicadorEventoService service = new PublicadorEventoService();
    private final PublicadorEvento port = service.getPublicadorEventoPort();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ✅ ahora obtenemos los eventos a través del publicador, no del controlador local
        List<DtEvento> eventos = new ArrayList<>();
        try {
            // reemplaza esta llamada si tu WSDL usa otro método (por ejemplo listarEventosVigentes o listarEventos)
            eventos = port.listarEventosVigentes().getItem();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (eventos == null || eventos.isEmpty()) {
            getServletContext().setAttribute("datosPrecargados", Boolean.FALSE);
        }

        // nombreEvento -> urlImagen
        Map<String, String> imgUrls = new HashMap<>();
        String ctx = req.getContextPath();

        if (eventos != null) {
            for (DtEvento e : eventos) {
                String nombre = e.getNombre();
                String raw = null;

                // intentar obtener imagen directamente del DTO
                try { raw = e.getImagen(); } catch (Exception ignore) {}

                // si no vino, hacemos una consulta puntual al publicador
                if (raw == null || raw.isBlank()) {
                    try {
                        DtEvento eventIter = port.consultaDTEvento(nombre);
                        if (eventIter != null) raw = eventIter.getImagen();
                    } catch (Exception ex) {
                        System.err.println("Error consultando imagen para evento " + nombre);
                    }
                }

                String url = null;
                if (raw != null && !raw.isBlank()) {
                    if (raw.startsWith("http://") || raw.startsWith("https://")) {
                        url = raw;
                    } else if (raw.startsWith("/")) {
                        url = ctx + raw;
                    } else {
                        // buscar dentro de las rutas locales conocidas
                        String[] candidates = new String[]{
                                "/img/" + raw,
                                "/img/eventos/" + raw,
                                "/eventos/" + raw
                        };

                        for (String rel : candidates) {
                            String abs = getServletContext().getRealPath(rel);
                            boolean exists;
                            if (abs != null) {
                                exists = Files.exists(Path.of(abs));
                            } else {
                                exists = true;
                            }
                            if (exists) {
                                url = ctx + rel;
                                break;
                            }
                        }
                    }
                }

                imgUrls.put(nombre, url);
            }
        }

        req.setAttribute("eventos", eventos);
        req.setAttribute("imgUrls", imgUrls);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}