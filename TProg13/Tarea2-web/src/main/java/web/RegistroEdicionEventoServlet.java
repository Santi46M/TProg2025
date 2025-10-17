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
import logica.enumerados.DTEstado;

@WebServlet("/registro/inscripcion")
public class RegistroEdicionEventoServlet extends HttpServlet {

    private static final String JSP_INSCRIPCION = "/WEB-INF/registro/RegistroEdicionEvento.jsp";

    private IControladorEvento ce() { return fabrica.getInstance().getIControladorEvento(); }
    private IControladorUsuario cu() { return fabrica.getInstance().getIControladorUsuario(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!requiereAsistente(req, resp)) return;

        List<DTEvento> eventos = ce().listarEventos();
        req.setAttribute("eventos", eventos);

        Map<String, List<DTEdicion>> edicionesPorEvento = new LinkedHashMap<>();
        for (DTEvento ev : eventos) {
            List<String> nombresEd = ce().listarEdicionesEvento(ev.getNombre());
            if (nombresEd == null) continue;
            List<DTEdicion> aceptadas = new ArrayList<>();
            for (String nomEd : nombresEd) {
                DTEdicion ed = ce().consultaEdicionEvento(ev.getNombre(), nomEd);
                if (ed != null && ed.getEstado() == DTEstado.Aceptada) {
                    aceptadas.add(ed);
                }
            }
            if (!aceptadas.isEmpty()) edicionesPorEvento.put(ev.getNombre(), aceptadas);
        }
        req.setAttribute("edicionesPorEvento", edicionesPorEvento);

        String evento = trim(req.getParameter("evento"));
        String edicion = trim(req.getParameter("edicion"));

        if (!isBlank(evento) && !isBlank(edicion)) {
            DTEdicion edSel = ce().consultaEdicionEvento(evento, edicion);
            if (edSel != null && edSel.getEstado() == DTEstado.Aceptada) {
                req.setAttribute("edicionSeleccionada", edSel);
                List<DTTipoRegistro> tipos = edSel.getTiposRegistro() != null ? edSel.getTiposRegistro() : new ArrayList<>();
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

        String evento = trim(req.getParameter("evento"));
        String edicion = trim(req.getParameter("edicion"));
        String tipoNom = trim(req.getParameter("tipo"));
        String codigoPatrocinio = trim(req.getParameter("codigoPatrocinio"));

        HttpSession s = req.getSession(false);
        String nick = (s == null) ? null : (String) s.getAttribute("nick");

        if (isBlank(evento) || isBlank(edicion) || isBlank(tipoNom) || isBlank(nick)) {
            req.setAttribute("error", "Debe seleccionar evento, edición y tipo de registro.");
            doGet(req, resp);
            return;
        }

        DTDatosUsuario dtUsuario = null;
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

        DTEdicion edSel = ce().consultaEdicionEvento(evento, edicion);
        if (edSel == null || edSel.getEstado() != DTEstado.Aceptada) {
            req.setAttribute("error", "La edición seleccionada no está aceptada o no existe.");
            doGet(req, resp);
            return;
        }

        List<DTTipoRegistro> tipos = edSel.getTiposRegistro();
        DTTipoRegistro tipoSel = null;
        if (tipos != null) {
            for (DTTipoRegistro t : tipos) {
                if (t.getNombre().equalsIgnoreCase(tipoNom)) {
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

        if (!isBlank(codigoPatrocinio)) {
            List<DTPatrocinio> patrocinios = edSel.getPatrocinios();
            boolean valido = false;
            if (patrocinios != null) {
                for (DTPatrocinio p : patrocinios) {
                    if (p != null && p.getCodigo() != null &&
                        p.getCodigo().equalsIgnoreCase(codigoPatrocinio) &&
                        p.getTipoRegistro() != null &&
                        p.getTipoRegistro().equalsIgnoreCase(tipoNom)) {
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

            ce().altaRegistroEdicionEvento(
                idRegistro,
                nick,
                evento,
                edicion,
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
}