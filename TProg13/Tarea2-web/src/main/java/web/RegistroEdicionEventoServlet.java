package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import logica.fabrica;
import logica.interfaces.IControladorEvento;
import logica.interfaces.IControladorUsuario;
import logica.clases.Ediciones;
import logica.clases.Usuario;
import logica.clases.Asistente;
import logica.clases.Eventos;
import logica.clases.TipoRegistro;
import logica.datatypes.DTEvento;
import logica.enumerados.DTEstado;

import java.time.LocalDate;
import java.util.UUID;

@WebServlet("/registro/inscripcion")
public class RegistroEdicionEventoServlet extends HttpServlet {

    private static final String JSP_INSCRIPCION = "/WEB-INF/registro/RegistroEdicionEvento.jsp";
    private static final String JSP_OK = "/WEB-INF/registro/AltaRegistroOK.jsp";

    private IControladorEvento ce() {
        return fabrica.getInstance().getIControladorEvento();
    }

    private IControladorUsuario cu() {
        return logica.fabrica.getInstance().getIControladorUsuario();
    }

    private String ctx(HttpServletRequest req) {
        return req.getContextPath();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (!requiereAsistente(req, resp)) return;

        // Listar eventos y ediciones aceptadas
        List<DTEvento> eventos = ce().listarEventos();
        req.setAttribute("eventos", eventos);

        List<String> eventosConEdiciones = ce().listarEventosConEdicionesIngresadas();
        List<Ediciones> edicionesAceptadas = new ArrayList<>();
        for (String nombreEvento : eventosConEdiciones) {
            List<String> nombresEd = ce().listarEdicionesEvento(nombreEvento);
            for (String nomEd : nombresEd) {
                Ediciones ed = ce().obtenerEdicion(nombreEvento, nomEd);
                if (ed != null && "ACEPTADA".equals(ed.getEstado())) edicionesAceptadas.add(ed);
            }
        }
        req.setAttribute("ediciones", edicionesAceptadas);
        req.getRequestDispatcher(JSP_INSCRIPCION).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
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

            // ✔ Selecciona el primer tipo disponible desde una Collection<TipoRegistro>
            TipoRegistro tipoRegistro = null;
            Collection<TipoRegistro> tipos = ed.getTiposRegistro(); // <-- aquí es Collection
            if (tipos != null && !tipos.isEmpty()) {
                tipoRegistro = tipos.iterator().next();
            }
            if (tipoRegistro == null) {
                req.setAttribute("error", "No hay tipos de registro disponibles para esta edición.");
                recargarDatos(req);
                req.getRequestDispatcher(JSP_INSCRIPCION).forward(req, resp);
                return;
            }

            String idRegistro = java.util.UUID.randomUUID().toString();
            LocalDate fechaRegistro = LocalDate.now();
            float costo = tipoRegistro.getCosto();
            LocalDate fechaInicio = ed.getFechaInicio();

            ce().altaRegistroEdicionEvento(
                    idRegistro, usuario, evento, ed, tipoRegistro, fechaRegistro, costo, fechaInicio
            );

            req.setAttribute("mensaje", "Inscripción realizada correctamente.");
            req.getRequestDispatcher(JSP_OK).forward(req, resp);

        } catch (IllegalArgumentException e) {
            // Manejo “amigable” de validaciones de dominio
            req.setAttribute("error", e.getMessage());
            recargarDatos(req);
            req.getRequestDispatcher(JSP_INSCRIPCION).forward(req, resp);
        }
    }


    private void recargarDatos(HttpServletRequest req) {
        List<DTEvento> eventos = ce().listarEventos();
        req.setAttribute("eventos", eventos);
        List<String> eventosConEdiciones = ce().listarEventosConEdicionesIngresadas();
        List<Ediciones> edicionesAceptadas = new ArrayList<>();
        for (String nombreEvento : eventosConEdiciones) {
            List<String> nombresEd = ce().listarEdicionesEvento(nombreEvento);
            for (String nomEd : nombresEd) {
                Ediciones ed = ce().obtenerEdicion(nombreEvento, nomEd);
                if (ed != null && "ACEPTADA".equals(ed.getEstado())) edicionesAceptadas.add(ed);
            }
        }
        req.setAttribute("ediciones", edicionesAceptadas);
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

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}