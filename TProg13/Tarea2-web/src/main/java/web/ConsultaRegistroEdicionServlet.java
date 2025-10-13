package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import logica.fabrica;
import logica.interfaces.IControladorEvento;
import logica.clases.Registro;
import logica.clases.Usuario;

@WebServlet("/registro/ConsultaRegistroEdicion")
public class ConsultaRegistroEdicionServlet extends HttpServlet {
    private static final String JSP_CONSULTA = "/WEB-INF/registro/ConsultaRegistroEdicion.jsp";

    private IControladorEvento ce() { return fabrica.getInstance().getIControladorEvento(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        String nick = (session != null) ? (String) session.getAttribute("nick") : null;
        String idRegistro = req.getParameter("idRegistro");
        if (idRegistro == null || idRegistro.isBlank() || nick == null) {
            req.setAttribute("error", "Registro no especificado o sesión no iniciada.");
            req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
            return;
        }
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario == null) {
                // Si no está en sesión, buscar por nick
                usuario = logica.fabrica.getInstance().getIControladorUsuario().listarUsuarios().get(nick);
            }
            Registro registro = null;
            if (usuario instanceof logica.clases.Asistente asistente) {
                registro = asistente.getRegistros().get(idRegistro);
            } else if (usuario instanceof logica.clases.Organizador organizador) {
                // Si es organizador, buscar en las ediciones que organiza
                for (logica.clases.Ediciones ed : organizador.getEdiciones().values()) {
                    registro = ed.getRegistros().get(idRegistro);
                    if (registro != null) break;
                }
            }
            if (registro == null) {
                req.setAttribute("error", "No se encontró el registro solicitado.");
            } else {
                req.setAttribute("registro", registro);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Error al consultar el registro: " + e.getMessage());
        }
        req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
    }
}