package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import publicadores.DtDatosUsuario;
import publicadores.DtRegistro;
import publicadores.PublicadorUsuario;
import publicadores.PublicadorUsuarioService;
import publicadores.PublicadorEvento;
import publicadores.PublicadorEventoService;
import publicadores.UsuarioNoExisteException_Exception;

@WebServlet("/registro/ConsultaRegistroEdicion")
public class ConsultaRegistroEdicionServlet extends HttpServlet {
    private static final String JSP_CONSULTA = "/WEB-INF/registro/ConsultaRegistroEdicion.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        String nick = (session != null) ? (String) session.getAttribute("nick") : null;
        String edicion = req.getParameter("edicion");
        String usuario = req.getParameter("usuario");

        if (edicion == null || edicion.isBlank() || usuario == null || usuario.isBlank() || nick == null) {
            req.setAttribute("error", "Registro no especificado o sesión no iniciada.");
            req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
            return;
        }

        PublicadorUsuarioService service = new PublicadorUsuarioService();
        PublicadorUsuario port = service.getPublicadorUsuarioPort();
        PublicadorEventoService evSvc = new PublicadorEventoService();
        PublicadorEvento evPort = evSvc.getPublicadorEventoPort();

        try {
            DtDatosUsuario dtoUsuario = (DtDatosUsuario) session.getAttribute("usuario_logueado");
            if (dtoUsuario == null) {
                try {
                    dtoUsuario = port.obtenerDatosUsuario(nick);
                } catch (UsuarioNoExisteException_Exception e) {
                    req.setAttribute("error", "No se pudo encontrar el usuario logueado.");
                    req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
                    return;
                }
            }
            req.setAttribute("usuario", dtoUsuario);
            // PASAR ASISTENCIAS AL JSP (usando el tipo correcto)
            List<DtRegistro> asistencias = dtoUsuario.getAsistencias() != null ? dtoUsuario.getAsistencias().getAsistencia() : java.util.List.of();
            req.setAttribute("asistencias", asistencias);

            DtRegistro dtRegistro = null;
            try {
                List<DtRegistro> registros = dtoUsuario.getRegistros() != null ? dtoUsuario.getRegistros().getRegistro() : java.util.List.of();
                for (DtRegistro r : registros) {
                    if (r.getEdicion() != null && r.getEdicion().equals(edicion)) {
                        dtRegistro = r;
                        break;
                    }
                }
            } catch (Exception e) {
                req.setAttribute("error", "No se pudo obtener el registro: " + e.getMessage());
                req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
                return;
            }
            if (dtRegistro == null) {
                req.setAttribute("error", "No se encontró el registro para la edición seleccionada.");
                req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
                return;
            }
            // CHEQUEAR SI YA MARCO ASISTENCIA
            boolean yaAsistio = false;
            if (asistencias != null) {
                for (DtRegistro asis : asistencias) {
                    if (asis.getIdentificador() != null && asis.getIdentificador().equals(dtRegistro.getIdentificador())) {
                        yaAsistio = true;
                        break;
                    }
                }
            }
            req.setAttribute("yaAsistio", yaAsistio);
            req.setAttribute("registro", dtRegistro);
            req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Error inesperado: " + e.getMessage());
            req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        String nick = (session != null) ? (String) session.getAttribute("nick") : null;
        String registroId = req.getParameter("registroId");
        String usuario = req.getParameter("usuario");
        String edicion = req.getParameter("edicion");
        if (nick == null || registroId == null || usuario == null || edicion == null) {
            req.setAttribute("error", "Datos insuficientes para confirmar asistencia.");
            req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
            return;
        }
        try {
            // Obtener el registro para extraer el evento
            DtDatosUsuario dtoUsuario = (DtDatosUsuario) session.getAttribute("usuario_logueado");
            DtRegistro dtRegistro = null;
            if (dtoUsuario != null && dtoUsuario.getRegistros() != null) {
                List<DtRegistro> registros = dtoUsuario.getRegistros().getRegistro();
                for (DtRegistro r : registros) {
                    if (r.getEdicion() != null && r.getEdicion().equals(edicion)) {
                        dtRegistro = r;
                        break;
                    }
                }
            }
            if (dtRegistro == null || dtRegistro.getEvento() == null) {
                req.setAttribute("error", "No se pudo obtener el evento de la edición.");
                doGet(req, resp);
                return;
            }
            String siglaEvento = dtRegistro.getEvento();
            // Obtener la edición y chequear la fecha de fin
            PublicadorEventoService evSvc = new PublicadorEventoService();
            PublicadorEvento evPort = evSvc.getPublicadorEventoPort();
            publicadores.DtEdicion dtEdicion = null;
            try {
                dtEdicion = evPort.consultaEdicionEvento(siglaEvento, edicion);
            } catch (Exception e) {
                req.setAttribute("error", "No se pudo obtener la edición: " + e.getMessage());
                doGet(req, resp);
                return;
            }
            String fechaFinStr = dtEdicion.getFechaFin();
            if (fechaFinStr == null || fechaFinStr.isEmpty()) {
                req.setAttribute("error", "No se puede confirmar asistencia porque la edición no tiene fecha de finalización.");
                doGet(req, resp);
                return;
            }
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fechaFin = null;
            try {
                fechaFin = sdf.parse(fechaFinStr);
            } catch (Exception e) {
                req.setAttribute("error", "Formato de fecha de edición inválido.");
                doGet(req, resp);
                return;
            }
            if (fechaFin.after(new java.util.Date())) {
                req.setAttribute("error", "No se puede confirmar asistencia porque la edición aún no se ha realizado.");
                doGet(req, resp);
                return;
            }
            // Llamar al backend para marcar asistencia
            PublicadorUsuarioService service = new PublicadorUsuarioService();
            PublicadorUsuario port = service.getPublicadorUsuarioPort();
            port.marcarAsistencia(nick, registroId);
            // Actualizar datos del usuario en sesión
            DtDatosUsuario dtoUsuarioActualizado = port.obtenerDatosUsuario(nick);
            session.setAttribute("usuario_logueado", dtoUsuarioActualizado);
            req.setAttribute("mensaje", "¡Asistencia confirmada correctamente!");
        } catch (Exception e) {
            req.setAttribute("error", "No se pudo confirmar la asistencia: " + e.getMessage());
        }
        // Volver a mostrar el registro
        doGet(req, resp);
    }
}