package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import publicadores.DtDatosUsuario;
import publicadores.DtRegistro;
import publicadores.PublicadorUsuario;
import publicadores.PublicadorUsuarioService;
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
        String idRegistro = req.getParameter("idRegistro");

        if (idRegistro == null || idRegistro.isBlank() || nick == null) {
            req.setAttribute("error", "Registro no especificado o sesión no iniciada.");
            req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
            return;
        }

        PublicadorUsuarioService service = new PublicadorUsuarioService();
        PublicadorUsuario port = service.getPublicadorUsuarioPort();

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

            DtRegistro dtRegistro = null;
            try {
                dtRegistro = port.obtenerDatosRegistro(idRegistro);
            } catch (Exception e) {
                req.setAttribute("error", "No se pudo obtener el registro: " + e.getMessage());
                req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
                return;
            }

            if (dtRegistro == null) {
                req.setAttribute("error", "No se encontró el registro solicitado.");
            } else {
                req.setAttribute("registro", dtRegistro);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Error al consultar el registro: " + e.getMessage());
        }

        req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
    }
}