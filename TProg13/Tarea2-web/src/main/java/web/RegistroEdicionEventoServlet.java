package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import logica.fabrica;
import logica.interfaces.IControladorEvento;
import logica.interfaces.IControladorUsuario;
import logica.clases.*;
import logica.datatypes.DTEvento;
import logica.enumerados.DTEstado;

@WebServlet("/registro/inscripcion")
public class RegistroEdicionEventoServlet extends HttpServlet {

    private static final String JSP_INSCRIPCION = "/WEB-INF/registro/RegistroEdicionEvento.jsp";
    private static final String JSP_OK = "/WEB-INF/registro/AltaRegistroOK.jsp";

    private IControladorEvento ce() { return fabrica.getInstance().getIControladorEvento(); }
    private IControladorUsuario cu() { return fabrica.getInstance().getIControladorUsuario(); }
    private String ctx(HttpServletRequest req) { return req.getContextPath(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (!requiereAsistente(req, resp)) return;

        List<DTEvento> eventos = ce().listarEventos();
        req.setAttribute("eventos", eventos);

        List<Ediciones> edicionesAceptadas = new ArrayList<>();
        Map<String, List<Ediciones>> edicionesPorEvento = new LinkedHashMap<>();

        for (DTEvento ev : eventos) {
            String nombreEvento = ev.getNombre();
            List<String> nombresEd = ce().listarEdicionesEvento(nombreEvento);
            if (nombresEd == null) continue;

            for (String nomEd : nombresEd) {
                Ediciones ed = ce().obtenerEdicion(nombreEvento, nomEd);
                if (ed != null && ed.getEstado() == DTEstado.Aceptada) {
                    edicionesAceptadas.add(ed);
                    edicionesPorEvento
                        .computeIfAbsent(nombreEvento, k -> new ArrayList<>())
                        .add(ed);
                }
            }
        }

        req.setAttribute("ediciones", edicionesAceptadas);
        req.setAttribute("edicionesPorEvento", edicionesPorEvento);
        req.getRequestDispatcher(JSP_INSCRIPCION).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String accion = req.getParameter("accion");
        if ("cancelar".equalsIgnoreCase(accion)) {
            resp.sendRedirect(ctx(req) + "/inicio");
            return;
        }
        if (!requiereAsistente(req, resp)) return;

        String siglaEdicion = trim(req.getParameter("edicion"));
        HttpSession s = req.getSession(false);
        String nick = (s == null) ? null : (String) s.getAttribute("nick");

        if (isBlank(siglaEdicion) || isBlank(nick)) {
            req.setAttribute("error", "Debe seleccionar una edición aceptada.");
            recargarDatos(req);
            req.getRequestDispatcher(JSP_INSCRIPCION).forward(req, resp);
            return;
        }

        try {
            Ediciones ed = ce().obtenerEdicionPorSigla(siglaEdicion);
            if (ed == null || ed.getEstado() != DTEstado.Aceptada) {
                req.setAttribute("error", "La edición seleccionada no está aceptada.");
                recargarDatos(req);
                req.getRequestDispatcher(JSP_INSCRIPCION).forward(req, resp);
                return;
            }

            Usuario usuario = cu().listarUsuarios().get(nick);
            if (!(usuario instanceof Asistente)) {
                req.setAttribute("error", "Solo asistentes pueden inscribirse.");
                recargarDatos(req);
                req.getRequestDispatcher(JSP_INSCRIPCION).forward(req, resp);
                return;
            }

            Eventos evento = ed.getEvento();

            // Primer tipo disponible
            TipoRegistro tipoRegistro = null;
            Collection<TipoRegistro> tipos = ed.getTiposRegistro();
            if (tipos != null && !tipos.isEmpty()) {
                tipoRegistro = tipos.iterator().next();
            }
            if (tipoRegistro == null) {
                req.setAttribute("error", "No hay tipos de registro disponibles para esta edición.");
                recargarDatos(req);
                req.getRequestDispatcher(JSP_INSCRIPCION).forward(req, resp);
                return;
            }

            // Crear registro a través de la lógica
            String idRegistro = UUID.randomUUID().toString();
            LocalDate fechaRegistro = LocalDate.now();
            float costo = tipoRegistro.getCosto();
            LocalDate fechaInicio = ed.getFechaInicio();

            ce().altaRegistroEdicionEvento(
                idRegistro, usuario, evento, ed, tipoRegistro, fechaRegistro, costo, fechaInicio
            );

            // ===================== 🔧 BLOQUE AÑADIDO 🔧 =====================
            // Crear el registro en las estructuras locales también
            Registro nuevo = new Registro(idRegistro, usuario, ed, tipoRegistro, fechaRegistro, costo, fechaInicio);
            ed.agregarRegistro(idRegistro, nuevo);

            if (usuario instanceof Asistente asistente) {
                asistente.addRegistro(idRegistro, nuevo);
            }

            System.out.println("✅ Registro agregado: " + usuario.getNickname() + " en edición " + ed.getNombre());
            // ================================================================

            req.setAttribute("mensaje", "Inscripción realizada correctamente.");
            req.getRequestDispatcher(JSP_OK).forward(req, resp);

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            recargarDatos(req);
            req.getRequestDispatcher(JSP_INSCRIPCION).forward(req, resp);
        }
    }

    private void recargarDatos(HttpServletRequest req) {
        List<DTEvento> eventos = ce().listarEventos();
        req.setAttribute("eventos", eventos);

        List<Ediciones> edicionesAceptadas = new ArrayList<>();
        Map<String, List<Ediciones>> edicionesPorEvento = new LinkedHashMap<>();

        for (DTEvento ev : eventos) {
            String nombreEvento = ev.getNombre();
            List<String> nombresEd = ce().listarEdicionesEvento(nombreEvento);
            if (nombresEd == null) continue;

            for (String nomEd : nombresEd) {
                Ediciones ed = ce().obtenerEdicion(nombreEvento, nomEd);
                if (ed != null && ed.getEstado() == DTEstado.Aceptada) {
                    edicionesAceptadas.add(ed);
                    edicionesPorEvento
                        .computeIfAbsent(nombreEvento, k -> new ArrayList<>())
                        .add(ed);
                }
            }
        }

        req.setAttribute("ediciones", edicionesAceptadas);
        req.setAttribute("edicionesPorEvento", edicionesPorEvento);
    }

    private boolean requiereAsistente(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        String rol = s == null ? null : (String) s.getAttribute("rol");
        if (!"ASISTENTE".equals(rol)) {
            resp.sendRedirect(ctx(req) + "/auth/login");
            return false;
        }
        return true;
    }

    private static String trim(String s) { return s == null ? null : s.trim(); }
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}