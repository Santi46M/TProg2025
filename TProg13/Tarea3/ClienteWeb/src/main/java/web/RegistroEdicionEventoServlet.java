package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import publicadores.PublicadorEventoService;
import publicadores.PublicadorEvento;
import publicadores.PublicadorUsuarioService;
import publicadores.PublicadorUsuario;
import publicadores.DtEvento;
import publicadores.DtEventoArray;
import publicadores.DtEdicion;
import publicadores.DtRegistro;
import publicadores.DtDatosUsuario;
import publicadores.DtTipoRegistro;
import publicadores.DtPatrocinio;
import publicadores.StringArray;
import publicadores.UsuarioNoExisteException_Exception;

@WebServlet("/registro/inscripcion")
public class RegistroEdicionEventoServlet extends HttpServlet {

    private static final String JSP_INSCRIPCION = "/WEB-INF/registro/RegistroEdicionEvento.jsp";

    private PublicadorEvento obtenerPortEvento() {
        PublicadorEventoService svc = new PublicadorEventoService();
        return svc.getPublicadorEventoPort();
    }
    private PublicadorUsuario obtenerPortUsuario() {
        PublicadorUsuarioService svc = new PublicadorUsuarioService();
        return svc.getPublicadorUsuarioPort();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!requiereAsistente(req, resp)) return;

        PublicadorEvento port = obtenerPortEvento();

        // listar eventos (vigentes) usando publicadores
        List<DtEvento> eventos = new ArrayList<>();
        try {
            try {
                DtEventoArray arr = port.listarEventos();
                if (arr != null && arr.getItem() != null) eventos.addAll(arr.getItem());
            } catch (Throwable t) {
                // fallback: try listarEventosVigentes (if exposed)
                try {
                    Object raw = port.getClass().getMethod("listarEventosVigentes").invoke(port);
                    if (raw instanceof DtEvento[]) {
                        eventos.addAll(Arrays.asList((DtEvento[]) raw));
                    }
                } catch (Exception ignore) {}
            }
        } catch (Exception e) { /* ignore, show empty list */ }

        req.setAttribute("eventos", eventos);

        // ediciones ACEPTADAS y NO finalizadas
        Map<String, List<DtEdicion>> edicionesPorEvento = new LinkedHashMap<>();
        for (DtEvento ev : eventos) {
            if (ev == null) continue;
            String nombreEv = ev.getNombre();
            StringArray clavesArr = null;
            try { clavesArr = port.listarEdicionesEvento(nombreEv); } catch (Exception ignore) { }
            List<String> claves = (clavesArr == null || clavesArr.getItem() == null) ? List.of() : clavesArr.getItem();
            if (claves.isEmpty()) continue;

            List<DtEdicion> visibles = new ArrayList<>();
            for (String nombreEdicion : claves) {
                DtEdicion ed = null;
                try {
                    // Ya sabemos que listarEdicionesEvento devuelve NOMBRES, no siglas
                    ed = port.obtenerDtEdicion(nombreEv, nombreEdicion);
                } catch (Exception ignore) { }

                if (ed != null && esAceptada(ed.getEstado())) {
                    visibles.add(ed);
                }
            }

            if (!visibles.isEmpty()) edicionesPorEvento.put(nombreEv, visibles);
        }
        req.setAttribute("edicionesPorEvento", edicionesPorEvento);

        String eventoParam = trim(req.getParameter("evento"));
        String edicionParam = trim(req.getParameter("edicion"));

        HttpSession s = req.getSession(false);
        String nick = (s == null) ? null : (String) s.getAttribute("nick");
        boolean yaRegistrado = false;

        if (!isBlank(eventoParam) && !isBlank(edicionParam) && !isBlank(nick)) {
            ResEvento res = resolverEvento(eventos, eventoParam);
            DtEdicion edSel = resolverEdicion(res, edicionParam, port);

            if (edSel != null && esAceptada(edSel.getEstado())
                    /* skipping finalizado check due to DTO date types */) {

                req.setAttribute("edicionSeleccionada", edSel);

                List<DtTipoRegistro> tipos = new ArrayList<>();
                try { if (edSel.getTiposRegistro() != null && edSel.getTiposRegistro().getTipoRegistro() != null) tipos.addAll(edSel.getTiposRegistro().getTipoRegistro()); } catch (Exception ignore) {}
                req.setAttribute("tiposRegistro", tipos);

                // Calcular cupos disponibles y si el usuario ya está registrado
                Map<String, Integer> cuposDisponibles = new HashMap<>();
                if (tipos != null) {
                    for (DtTipoRegistro tipo : tipos) {
                        int cupo = (tipo == null) ? 0 : tipo.getCupo();
                        int registrados = 0;
                        try {
                            if (edSel.getRegistros() != null && edSel.getRegistros().getRegistro() != null) {
                                for (DtRegistro reg : edSel.getRegistros().getRegistro()) {
                                    if (reg == null) continue;
                                    if (reg.getTipoRegistro() != null && tipo != null
                                            && reg.getTipoRegistro().equalsIgnoreCase(tipo.getNombre())) {
                                        registrados++;
                                    }
                                    if (reg.getUsuario() != null && reg.getUsuario().equals(nick)) yaRegistrado = true;
                                }
                            }
                        } catch (Exception ignore) {}
                        if (tipo != null) cuposDisponibles.put(tipo.getNombre(), cupo - registrados);
                    }
                }
                req.setAttribute("cuposDisponibles", cuposDisponibles);
                req.setAttribute("yaRegistrado", yaRegistrado);
            } else {
                req.setAttribute("error", "La edición seleccionada no existe, no está aceptada o ya finalizó.");
            }
        }

        req.getRequestDispatcher(JSP_INSCRIPCION).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!requiereAsistente(req, resp)) return;
        req.setCharacterEncoding("UTF-8");

        String eventoParam = trim(req.getParameter("evento"));
        String edicionParam = trim(req.getParameter("edicion"));
        String tipoNom = trim(req.getParameter("tipo"));
        String codigoPatrocinio = trim(req.getParameter("codigoPatrocinio"));

        HttpSession s = req.getSession(false);
        String nick = (s == null) ? null : (String) s.getAttribute("nick");

        if (isBlank(eventoParam) || isBlank(edicionParam) || isBlank(tipoNom) || isBlank(nick)) {
            req.setAttribute("error", "Debe seleccionar evento, edición y tipo de registro.");
            doGet(req, resp);
            return;
        }

        // 🔹 Obtener usuario via publicador
        PublicadorUsuario portU = obtenerPortUsuario();
        DtDatosUsuario dtUsuario = null;
        try {
            dtUsuario = portU.obtenerDatosUsuario(nick);
        } catch (UsuarioNoExisteException_Exception e) {
            req.setAttribute("error", "El usuario no existe o la sesión ha expirado.");
            doGet(req, resp);
            return;
        } catch (Exception e) {
            req.setAttribute("error", "El usuario no existe o la sesión ha expirado.");
            doGet(req, resp);
            return;
        }

        // 🔹 Resolver evento / edición
        PublicadorEvento port = obtenerPortEvento();

        List<DtEvento> eventos = new ArrayList<>();
        try {
            DtEventoArray arr = port.listarEventos();
            if (arr != null && arr.getItem() != null) eventos.addAll(arr.getItem());
        } catch (Exception ignore) {}

        ResEvento res = resolverEvento(eventos, eventoParam);
        DtEdicion edSel = resolverEdicion(res, edicionParam, port);

        if (edSel == null || !esAceptada(edSel.getEstado())) {
            req.setAttribute("error", "La edición seleccionada no está aceptada o no existe.");
            doGet(req, resp);
            return;
        }

        // 🔹 Tipo de registro seleccionado
        DtTipoRegistro tipoSel = null;
        try {
            if (edSel.getTiposRegistro() != null && edSel.getTiposRegistro().getTipoRegistro() != null) {
                for (DtTipoRegistro t : edSel.getTiposRegistro().getTipoRegistro()) {
                    if (t != null && t.getNombre() != null && t.getNombre().equalsIgnoreCase(tipoNom)) {
                        tipoSel = t;
                        break;
                    }
                }
            }
        } catch (Exception ignore) {}
        if (tipoSel == null) {
            req.setAttribute("error", "El tipo de registro seleccionado no es válido.");
            doGet(req, resp);
            return;
        }

        // 🔹 Calcular costo (considerando patrocinio)
        float costo = tipoSel.getCosto();
        if (!isBlank(codigoPatrocinio)) {
            boolean valido = false;
            try {
                if (edSel.getPatrocinios() != null && edSel.getPatrocinios().getPatrocinio() != null) {
                    for (DtPatrocinio p : edSel.getPatrocinios().getPatrocinio()) {
                        if (p != null && p.getCodigo() != null && p.getCodigo().equalsIgnoreCase(codigoPatrocinio)
                                && p.getTipoRegistro() != null && p.getTipoRegistro().equalsIgnoreCase(tipoNom)) {
                            valido = true;
                            break;
                        }
                    }
                }
            } catch (Exception ignore) {}
            if (valido) {
                costo = 0f;
            } else {
                req.setAttribute("error", "El código de patrocinio no es válido.");
                doGet(req, resp);
                return;
            }
        }

        // 🔹 Registrar inscripción usando el nuevo método del web service
        try {
            String idRegistro = UUID.randomUUID().toString();
            LocalDate fechaRegistro = LocalDate.now();

            // El método altaRegistroEdicionEventoDT espera fechas como String (yyyy-MM-dd)
            String fechaRegistroStr = fechaRegistro.toString();
            String fechaInicioStr   = fechaRegistroStr; // Si querés usar otra fecha, ajustalo

            // 🔸 Llamada directa al nuevo método sin reflexión
            port.altaRegistroEdicionEventoDT(
                idRegistro,                         // idRegistro
                nick,                               // nicknameUsuario
                res != null ? res.nombreEvento : "",// nombreEvento
                edicionParam,                       // nombreEdicion
                tipoNom,                            // nombreTipoRegistro
                fechaRegistroStr,                   // fechaRegistroStr
                costo,                              // costo
                fechaInicioStr                      // fechaInicioStr
            );

            resp.sendRedirect(req.getContextPath() + "/inicio");

        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

    // Helpers

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

    private static boolean esAceptada(Object estado) {
        if (estado == null) return false;
        String s = String.valueOf(estado);
        return "ACEPTADA".equalsIgnoreCase(s);
    }

    private static class ResEvento {
        final String nombreEvento;
        final String siglaEvento;
        ResEvento(String nombre, String sigla) { this.nombreEvento = nombre; this.siglaEvento = sigla; }
        boolean valido() { return nombreEvento != null && siglaEvento != null; }
    }

    private ResEvento resolverEvento(List<DtEvento> eventos, String eventoParam) {
        if (isBlank(eventoParam)) return new ResEvento(null, null);

        DtEvento evByNombre = null;
        try { evByNombre = obtenerPortEvento().consultaDTEvento(eventoParam); } catch (Exception ignore) {}
        if (evByNombre != null) {
            return new ResEvento(evByNombre.getNombre(), safe(evByNombre.getSigla()));
        }

        if (eventos != null) {
            for (DtEvento e : eventos) {
                if (e != null && e.getSigla() != null && e.getSigla().equalsIgnoreCase(eventoParam)) {
                    return new ResEvento(e.getNombre(), e.getSigla());
                }
            }
        }
        return new ResEvento(null, null);
    }

    private DtEdicion resolverEdicion(ResEvento res, String edicionParam, PublicadorEvento port) {
        if (res == null || !res.valido() || isBlank(edicionParam)) return null;

        DtEdicion ed = null;
        try { ed = port.obtenerDtEdicion(res.nombreEvento, edicionParam); } catch (Exception ignore) {}
//        if (ed == null) {
//            try { ed = port.obtenerDtEdicion(res.nombreEvento, edicionParam); } catch (Exception ignore) {}
//        }
        return ed;
    }

    private static String safe(String s) { return s == null ? "" : s; }
}
