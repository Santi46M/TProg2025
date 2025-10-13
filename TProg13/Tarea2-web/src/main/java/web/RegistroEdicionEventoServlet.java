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

    // =============== GET: lista eventos/ediciones y muestra detalle si ya hay edición ===============
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!requiereAsistente(req, resp)) return;

        // --- 1) Listar eventos ---
        List<DTEvento> eventos = ce().listarEventos();
        req.setAttribute("eventos", eventos);

        // --- 2) Mapa evento → ediciones aceptadas (para el combo dinámico con JS) ---
        Map<String, List<Ediciones>> edicionesPorEvento = new LinkedHashMap<>();
        for (DTEvento ev : eventos) {
            List<String> nombresEd = ce().listarEdicionesEvento(ev.getNombre());
            if (nombresEd == null) continue;
            List<Ediciones> aceptadas = new ArrayList<>();
            for (String nomEd : nombresEd) {
                Ediciones ed = ce().obtenerEdicion(ev.getNombre(), nomEd);
                if (ed != null && ed.getEstado() == DTEstado.Aceptada) {
                    aceptadas.add(ed);
                }
            }
            if (!aceptadas.isEmpty()) edicionesPorEvento.put(ev.getNombre(), aceptadas);
        }
        req.setAttribute("edicionesPorEvento", edicionesPorEvento);

        // --- 3) Si hay evento y edición seleccionados → traer detalle completo ---
        String evento = trim(req.getParameter("evento"));
        String edicion = trim(req.getParameter("edicion"));
        if (!isBlank(evento) && !isBlank(edicion)) {
            Ediciones edSel = ce().obtenerEdicion(evento, edicion);
            if (edSel != null && edSel.getEstado() == DTEstado.Aceptada) {
                req.setAttribute("edicionSeleccionada", edSel);
            } else {
                req.setAttribute("error", "La edición seleccionada no existe o no está aceptada.");
            }
        }

        req.getRequestDispatcher(JSP_INSCRIPCION).forward(req, resp);
    }

    // =============== POST: realiza inscripción y aplica código de patrocinio si corresponde ===============
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

        Usuario usuario = cu().listarUsuarios().get(nick);
        if (!(usuario instanceof Asistente asistente)) {
            req.setAttribute("error", "Solo los asistentes pueden registrarse en ediciones.");
            doGet(req, resp);
            return;
        }

        Ediciones ed = ce().obtenerEdicion(evento, edicion);
        if (ed == null || ed.getEstado() != DTEstado.Aceptada) {
            req.setAttribute("error", "La edición seleccionada no está aceptada o no existe.");
            doGet(req, resp);
            return;
        }

        // --- Buscar el tipo de registro elegido ---
        TipoRegistro tipo = null;
        Collection<TipoRegistro> tipos = ed.getTiposRegistro();
        if (tipos != null) {
            for (TipoRegistro t : tipos) {
                if (t.getNombre().equalsIgnoreCase(tipoNom)) {
                    tipo = t;
                    break;
                }
            }
        }
        if (tipo == null) {
            req.setAttribute("error", "El tipo de registro seleccionado no es válido.");
            doGet(req, resp);
            return;
        }

        // --- Costo base ---
        float costo = tipo.getCosto();

        // --- Verificar código de patrocinio ---
        if (!isBlank(codigoPatrocinio)) {
            Collection<Patrocinio> patrocinios = ed.getPatrocinios();
            boolean valido = false;

            if (patrocinios != null) {
                for (Patrocinio p : patrocinios) {
                    if (p != null && p.getCodigoPatrocinio() != null &&
                        p.getCodigoPatrocinio().equalsIgnoreCase(codigoPatrocinio)) {
                        valido = true;
                        break;
                    }
                }
            }

            if (valido) {
                costo = 0f;
                System.out.println("✅ Código de patrocinio válido: " + codigoPatrocinio);
            } else {
                System.out.println("❌ Código de patrocinio inválido: " + codigoPatrocinio);
                req.setAttribute("error", "El código de patrocinio no es válido.");
                doGet(req, resp);
                return;
            }
        }

        // --- Crear registro ---
        try {
            String idRegistro = UUID.randomUUID().toString();
            LocalDate fechaRegistro = LocalDate.now();

            ce().altaRegistroEdicionEvento(
                idRegistro, usuario, ed.getEvento(), ed, tipo,
                fechaRegistro, costo, ed.getFechaInicio()
            );

            // Mantener coherencia en memoria
            Registro nuevo = new Registro(idRegistro, usuario, ed, tipo, fechaRegistro, costo, ed.getFechaInicio());
            ed.agregarRegistro(idRegistro, nuevo);
            asistente.addRegistro(idRegistro, nuevo);

            req.setAttribute("mensaje", "Inscripción realizada correctamente.");
            req.getRequestDispatcher(JSP_OK).forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Ocurrió un error al registrar: " + e.getMessage());
            doGet(req, resp);
        }
    }

    // =============== Métodos auxiliares ===============

    private boolean requiereAsistente(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        String rol = s == null ? null : (String) s.getAttribute("rol");
        if (!"ASISTENTE".equals(rol)) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return false;
        }
        return true;
    }

    private static String trim(String s){ return s==null?null:s.trim(); }
    private static boolean isBlank(String s){ return s==null || s.trim().isEmpty(); }
}