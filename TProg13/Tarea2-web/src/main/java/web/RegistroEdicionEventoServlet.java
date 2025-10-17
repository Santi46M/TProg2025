package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import excepciones.UsuarioNoExisteException;
import logica.fabrica;
import logica.interfaces.IControladorEvento;
import logica.interfaces.IControladorUsuario;
import logica.datatypes.*;

@WebServlet("/registro/inscripcion")
public class RegistroEdicionEventoServlet extends HttpServlet {

    private static final String JSP_INSCRIPCION = "/WEB-INF/registro/RegistroEdicionEvento.jsp";

    private IControladorEvento ce() { return fabrica.getInstance().getIControladorEvento(); }
    private IControladorUsuario cu() { return fabrica.getInstance().getIControladorUsuario(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!requiereAsistente(req, resp)) return;

        // 1) Eventos disponibles
        List<DTEvento> eventos = ce().listarEventos();
        req.setAttribute("eventos", eventos);

        // 2) Ediciones (tolerante: listar puede devolver nombres o siglas)
        Map<String, List<DTEdicion>> edicionesPorEvento = new LinkedHashMap<>();
        for (DTEvento ev : eventos) {
            List<String> claves = ce().listarEdicionesEvento(ev.getNombre()); // nombres o siglas
            if (claves == null) continue;

            List<DTEdicion> aceptadas = new ArrayList<>();
            for (String clave : claves) {
                DTEdicion ed = null;

                // a) Intento por SIGLA (lo que pide consultaEdicionEvento)
                try { ed = ce().consultaEdicionEvento(ev.getSigla(), clave); } catch (Exception ignore) {}

                // b) Fallback por NOMBRE de edición
                if (ed == null) {
                    try { ed = ce().obtenerDtEdicion(ev.getNombre(), clave); } catch (Exception ignore) {}
                }

                if (ed != null && esAceptada(ed.getEstado())) {
                    aceptadas.add(ed);
                }
            }
            if (!aceptadas.isEmpty()) {
                edicionesPorEvento.put(ev.getNombre(), aceptadas);
            }
        }
        req.setAttribute("edicionesPorEvento", edicionesPorEvento);

        // 3) Si vienen preseleccionados (por querystring), resolvémoslos
        String eventoParam = trim(req.getParameter("evento"));
        String edicionParam = trim(req.getParameter("edicion"));

        if (!isBlank(eventoParam) && !isBlank(edicionParam)) {
            ResEvento res = resolverEvento(eventos, eventoParam); // obtiene nombre/sigla consistentes
            DTEdicion edSel = resolverEdicion(res, edicionParam);
            if (edSel != null && esAceptada(edSel.getEstado())) {
                req.setAttribute("edicionSeleccionada", edSel);
                List<DTTipoRegistro> tipos = (edSel.getTiposRegistro() != null)
                        ? edSel.getTiposRegistro()
                        : new ArrayList<>();
                req.setAttribute("tiposRegistro", tipos);
            } else {
                req.setAttribute("error", "La edición seleccionada no existe o no está aceptada.");
            }
        }

        req.getRequestDispatcher(JSP_INSCRIPCION).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!requiereAsistente(req, resp)) return;
        req.setCharacterEncoding("UTF-8");

        String eventoParam = trim(req.getParameter("evento"));   // suele venir por NOMBRE
        String edicionParam = trim(req.getParameter("edicion")); // suele venir por NOMBRE
        String tipoNom = trim(req.getParameter("tipo"));
        String codigoPatrocinio = trim(req.getParameter("codigoPatrocinio"));

        HttpSession s = req.getSession(false);
        String nick = (s == null) ? null : (String) s.getAttribute("nick");

        if (isBlank(eventoParam) || isBlank(edicionParam) || isBlank(tipoNom) || isBlank(nick)) {
            req.setAttribute("error", "Debe seleccionar evento, edición y tipo de registro.");
            doGet(req, resp);
            return;
        }

        DTDatosUsuario dtUsuario;
        try {
            dtUsuario = cu().obtenerDatosUsuario(nick);
        } catch (UsuarioNoExisteException e) {
            req.setAttribute("error", "El usuario no existe o la sesión ha expirado.");
            doGet(req, resp);
            return;
        }
        if (dtUsuario == null) {
            req.setAttribute("error", "El usuario no existe o la sesión ha expirado.");
            doGet(req, resp);
            return;
        }

        // Necesitamos el DTO de la edición para validar estado, tipos y fechaInicio.
        // Resolver evento y edición tolerando nombre o sigla en los parámetros.
        List<DTEvento> eventos = ce().listarEventos();
        ResEvento res = resolverEvento(eventos, eventoParam);
        DTEdicion edSel = resolverEdicion(res, edicionParam);

        if (edSel == null || !esAceptada(edSel.getEstado())) {
            req.setAttribute("error", "La edición seleccionada no está aceptada o no existe.");
            doGet(req, resp);
            return;
        }

        // Tipo de registro
        DTTipoRegistro tipoSel = null;
        List<DTTipoRegistro> tipos = edSel.getTiposRegistro();
        if (tipos != null) {
            for (DTTipoRegistro t : tipos) {
                if (t != null && t.getNombre() != null && t.getNombre().equalsIgnoreCase(tipoNom)) {
                    tipoSel = t;
                    break;
                }
            }
        }
        if (tipoSel == null) {
            req.setAttribute("error", "El tipo de registro seleccionado no es válido.");
            doGet(req, resp);
            return;
        }

        float costo = tipoSel.getCosto();

        // Código de patrocinio (si aplica)
        if (!isBlank(codigoPatrocinio)) {
            List<DTPatrocinio> patrocinios = edSel.getPatrocinios();
            boolean valido = false;
            if (patrocinios != null) {
                for (DTPatrocinio p : patrocinios) {
                    if (p != null
                        && p.getCodigo() != null
                        && p.getCodigo().equalsIgnoreCase(codigoPatrocinio)
                        && p.getTipoRegistro() != null
                        && p.getTipoRegistro().equalsIgnoreCase(tipoNom)) {
                        valido = true;
                        break;
                    }
                }
            }
            if (valido) {
                costo = 0f;
            } else {
                req.setAttribute("error", "El código de patrocinio no es válido.");
                doGet(req, resp);
                return;
            }
        }

        try {
            String idRegistro = UUID.randomUUID().toString();
            LocalDate fechaRegistro = LocalDate.now();

            // IMPORTANTE: la firma espera NOMBRES (no siglas). Usamos res.nombreEvento y edicionParam (nombre).
            ce().altaRegistroEdicionEvento(
                idRegistro,
                nick,
                res.nombreEvento,     // nombre del evento
                edicionParam,         // nombre de la edición (el form lo envía así)
                tipoNom,
                fechaRegistro,
                costo,
                edSel.getFechaInicio()
            );

            resp.sendRedirect(req.getContextPath() + "/inicio");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Ocurrió un error al registrar: " + e.getMessage());
            doGet(req, resp);
        }
    }

    // ==================== Helpers ====================

    private boolean requiereAsistente(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        String rol = s == null ? null : (String) s.getAttribute("rol");
        if (!"ASISTENTE".equals(rol)) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return false;
        }
        return true;
    }

    private static String trim(String s) { return s == null ? null : s.trim(); }
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    /** Aceptada robusto: soporta ACEPTADA/Aceptada/etc. sin acentos. */
    private static boolean esAceptada(Object estado) {
        if (estado == null) return false;
        String s = String.valueOf(estado);
        return "ACEPTADA".equalsIgnoreCase(s);
    }

    /** Mantiene juntos nombre y sigla de un evento resuelto a partir del parámetro (que puede ser nombre o sigla). */
    private static class ResEvento {
        final String nombreEvento;
        final String siglaEvento;
        ResEvento(String nombre, String sigla) { this.nombreEvento = nombre; this.siglaEvento = sigla; }
        boolean valido() { return nombreEvento != null && siglaEvento != null; }
    }

    /** Dado el parámetro `evento` (nombre o sigla), resuelve nombre+sigla usando los eventos listados. */
    private ResEvento resolverEvento(List<DTEvento> eventos, String eventoParam) {
        if (isBlank(eventoParam)) return new ResEvento(null, null);

        // 1) Intentar como NOMBRE via consultaDTEvento
        DTEvento evByNombre = null;
        try { evByNombre = ce().consultaDTEvento(eventoParam); } catch (Exception ignore) {}
        if (evByNombre != null) {
            return new ResEvento(evByNombre.getNombre(), safe(evByNombre.getSigla()));
        }

        // 2) Intentar como SIGLA buscando en la lista
        if (eventos != null) {
            for (DTEvento e : eventos) {
                if (e != null && e.getSigla() != null && e.getSigla().equalsIgnoreCase(eventoParam)) {
                    return new ResEvento(e.getNombre(), e.getSigla());
                }
            }
        }
        // 3) No se pudo resolver
        return new ResEvento(null, null);
    }

    /** Dado un evento resuelto (nombre+sigla) y el parámetro `edicion` (nombre o sigla), devuelve el DTO. */
    private DTEdicion resolverEdicion(ResEvento res, String edicionParam) {
        if (res == null || !res.valido() || isBlank(edicionParam)) return null;

        DTEdicion ed = null;
        // a) Intento por SIGLA de edición
        try { ed = ce().consultaEdicionEvento(res.siglaEvento, edicionParam); } catch (Exception ignore) {}

        // b) Fallback por NOMBRE de edición
        if (ed == null) {
            try { ed = ce().obtenerDtEdicion(res.nombreEvento, edicionParam); } catch (Exception ignore) {}
        }
        return ed;
    }

    private static String safe(String s) { return s == null ? "" : s; }
}
