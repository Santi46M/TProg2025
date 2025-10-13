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
                Ediciones edicionIter = ce().obtenerEdicion(nombreEvento, nomEd);
                if (edicionIter != null && edicionIter.getEstado() == DTEstado.Aceptada) {
                    edicionesAceptadas.add(edicionIter);
                    edicionesPorEvento
                        .computeIfAbsent(nombreEvento, k -> new ArrayList<>())
                        .add(edicionIter);
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
        if (!requiereAsistente(req, resp)) return;

        String siglaEdicion = trim(req.getParameter("edicion"));
        HttpSession sAux = req.getSession(false);
        String nick = (sAux == null) ? null : (String) sAux.getAttribute("nick");

        if (isBlank(siglaEdicion) || isBlank(nick)) {
            req.setAttribute("error", "Debe seleccionar una edición aceptada.");
            recargarDatos(req);
            req.getRequestDispatcher(JSP_INSCRIPCION).forward(req, resp);
            return;
        }

        try {
            Ediciones edicionIter = ce().obtenerEdicionPorSigla(siglaEdicion);
            if (edicionIter == null || edicionIter.getEstado() != DTEstado.Aceptada) {
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

            Eventos evento = edicionIter.getEvento();

            // Primer tipo disponible
            TipoRegistro tipoRegistro = null;
            Collection<TipoRegistro> tipos = edicionIter.getTiposRegistro();
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
            LocalDate fechaInicio = edicionIter.getFechaInicio();

            ce().altaRegistroEdicionEvento(
                idRegistro, usuario, evento, edicionIter, tipoRegistro, fechaRegistro, costo, fechaInicio
            );

            // ===================== 🔧 BLOQUE AÑADIDO 🔧 =====================
            // Crear el registro en las estructuras locales también
            Registro nuevo = new Registro(idRegistro, usuario, edicionIter, tipoRegistro, fechaRegistro, costo, fechaInicio);
            edicionIter.agregarRegistro(idRegistro, nuevo);

            if (usuario instanceof Asistente asistente) {
                asistente.addRegistro(idRegistro, nuevo);
            }

            System.out.println("✅ Registro agregado: " + usuario.getNickname() + " en edición " + edicionIter.getNombre());
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
                Ediciones edicionIter = ce().obtenerEdicion(nombreEvento, nomEd);
                if (edicionIter != null && edicionIter.getEstado() == DTEstado.Aceptada) {
                    edicionesAceptadas.add(edicionIter);
                    edicionesPorEvento
                        .computeIfAbsent(nombreEvento, k -> new ArrayList<>())
                        .add(edicionIter);
                }
            }
        }

        req.setAttribute("ediciones", edicionesAceptadas);
        req.setAttribute("edicionesPorEvento", edicionesPorEvento);
    }

    private boolean requiereAsistente(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession sAux = req.getSession(false);
        String rol = sAux == null ? null : (String) sAux.getAttribute("rol");
        if (!"ASISTENTE".equals(rol)) {
            resp.sendRedirect(ctx(req) + "/auth/login");
            return false;
        }
        return true;
    }

    private static String trim(String sAux) { return sAux == null ? null : sAux.trim(); }
    private static boolean isBlank(String sAux) { return sAux == null || sAux.trim().isEmpty(); }
}