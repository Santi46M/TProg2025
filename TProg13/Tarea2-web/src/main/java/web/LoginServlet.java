package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import logica.fabrica;
import logica.interfaces.IControladorUsuario;
import logica.clases.Usuario;

@WebServlet(urlPatterns = {"/auth/login", "/auth/logout"})
public class LoginServlet extends HttpServlet {

    private static final String JSP_LOGIN = "/WEB-INF/auth/login.jsp";
    private final IControladorUsuario cu = fabrica.getInstance().getIControladorUsuario();

    private String ctx(HttpServletRequest req) {
        return req.getContextPath();
    }

    private void cargarInstituciones(HttpServletRequest req) {
        IControladorUsuario cu = fabrica.getInstance().getIControladorUsuario();
        Collection<String> instituciones = cu.getInstituciones();
        req.setAttribute("instituciones", instituciones);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/auth/login".equals(path)) {
            cargarInstituciones(req);
            req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
            return;
        }

        if ("/auth/logout".equals(path)) {
            HttpSession s = req.getSession(false);
            if (s != null) s.invalidate();
            resp.sendRedirect(ctx(req) + "/inicio");
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!"/auth/login".equals(req.getServletPath())) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String nickOrEmail = req.getParameter("email");
        String pass = req.getParameter("pass");

        if (nickOrEmail == null || nickOrEmail.isBlank() || pass == null || pass.isBlank()) {
            req.setAttribute("error", "Ingresá usuario y contraseña.");
            req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
            return;
        }

        String nick = nickOrEmail.trim();

        // ✅ Validación con la lógica
        boolean valido = cu.validarLogin(nick, pass);

        if (!valido) {
            req.setAttribute("estado_sesion", "LOGIN_INCORRECTO");
            req.setAttribute("error", "Usuario o contraseña incorrectos.");
            req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
            return;
        }

        // ✅ Login correcto
        Map<String, Usuario> usuarios = cu.listarUsuarios();
        Usuario usr = usuarios.get(nick);

        // Buscar por correo si no se encontró
        if (usr == null) {
            for (Usuario u : usuarios.values()) {
                if (u.getEmail().equalsIgnoreCase(nickOrEmail)) {
                    usr = u;
                    nick = u.getNickname();
                    break;
                }
            }
        }

        if (usr == null) {
            req.setAttribute("error", "Usuario no encontrado.");
            req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
            return;
        }

        String rol = cu.esAsistente(nick) ? "ASISTENTE" : "ORGANIZADOR";

        HttpSession s = req.getSession(true);
        s.setAttribute("usuario_logueado", usr);
        s.setAttribute("nick", nick);
        s.setAttribute("rol", rol);
        s.setAttribute("estado_sesion", "LOGIN_CORRECTO");

        // 🧩 --- DEBUG DE SESIÓN ---

        java.util.Enumeration<String> names = s.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Object val = s.getAttribute(name);
            
        }
        // ----------------------------

        resp.sendRedirect(ctx(req) + "/inicio");
    }
}