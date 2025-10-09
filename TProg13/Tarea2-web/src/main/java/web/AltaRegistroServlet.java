package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import logica.fabrica;
import logica.Interfaces.IControladorEvento;
import logica.Clases.Ediciones;
import logica.Datatypes.DTEvento;

@WebServlet("/registro/*")
public class AltaRegistroServlet extends HttpServlet {

    private static final String JSP_ALTA = "/WEB-INF/registro/AltaRegistro.jsp";
    private static final String JSP_OK = "/WEB-INF/registro/AltaRegistroOK.jsp";

    private IControladorEvento ce() {
        return fabrica.getInstance().getIControladorEvento();
    }

    private String ctx(HttpServletRequest req) {
        return req.getContextPath();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();

        if (path == null || "/".equals(path) || "/alta".equals(path)) {
            if (!requiereOrganizador(req, resp)) return;

            // Lista de eventos con sus ediciones
            List<DTEvento> eventos = ce().listarEventos();
            req.setAttribute("eventos", eventos);

            List<String> eventosConEdiciones = ce().listarEventosConEdicionesIngresadas();
            List<Ediciones> ediciones = new ArrayList<>();

            for (String nombreEvento : eventosConEdiciones) {
                List<String> nombresEd = ce().listarEdicionesEvento(nombreEvento);
                for (String nomEd : nombresEd) {
                    Ediciones ed = ce().obtenerEdicion(nombreEvento, nomEd);
                    if (ed != null) ediciones.add(ed);
                }
            }

            req.setAttribute("ediciones", ediciones);
            req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();

        if ("/alta".equals(path)) {
            if (!requiereOrganizador(req, resp)) return;

            String siglaEdicion = trim(req.getParameter("edicion"));
            String nombre = trim(req.getParameter("nombre"));
            String descripcion = trim(req.getParameter("descripcion"));
            String costoStr = trim(req.getParameter("costo"));
            String cupoStr = trim(req.getParameter("cupo"));

            if (isBlank(siglaEdicion) || isBlank(nombre) || isBlank(descripcion)
                    || isBlank(costoStr) || isBlank(cupoStr)) {
                req.setAttribute("error", "Todos los campos son obligatorios.");
                recargarDatos(req);
                req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
                return;
            }

            try {
                float costo = Float.parseFloat(costoStr);
                int cupo = Integer.parseInt(cupoStr);
                Ediciones ed = ce().obtenerEdicionPorSigla(siglaEdicion);

                if (ed == null) {
                    req.setAttribute("error", "No se encontró la edición seleccionada.");
                    recargarDatos(req);
                    req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
                    return;
                }

                ce().AltaTipoRegistro(ed, nombre, descripcion, costo, cupo);
                req.setAttribute("mensaje", "Registro creado correctamente.");
                req.getRequestDispatcher(JSP_OK).forward(req, resp);

            } catch (Exception e) {
                req.setAttribute("error", e.getMessage());
                recargarDatos(req);
                req.getRequestDispatcher(JSP_ALTA).forward(req, resp);
            }
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void recargarDatos(HttpServletRequest req) {
        List<DTEvento> eventos = ce().listarEventos();
        req.setAttribute("eventos", eventos);

        List<String> eventosConEdiciones = ce().listarEventosConEdicionesIngresadas();
        List<Ediciones> ediciones = new ArrayList<>();

        for (String nombreEvento : eventosConEdiciones) {
            List<String> nombresEd = ce().listarEdicionesEvento(nombreEvento);
            for (String nomEd : nombresEd) {
                Ediciones ed = ce().obtenerEdicion(nombreEvento, nomEd);
                if (ed != null) ediciones.add(ed);
            }
        }

        req.setAttribute("ediciones", ediciones);
    }

    private boolean requiereOrganizador(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        String rol = s == null ? null : (String) s.getAttribute("rol");
        if (!"ORGANIZADOR".equals(rol)) {
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