package web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import logica.fabrica;
import logica.datatypes.DTDatosUsuario;
import logica.datatypes.DTEvento;
import logica.interfaces.IControladorUsuario;
import logica.interfaces.IControladorEvento;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioTipoIncorrectoException;

@WebServlet(urlPatterns = {"/usuario/ConsultaUsuario", "/usuario/modificar"})
public class ConsultaUsuarioServlet extends HttpServlet {

    // =====================================================
    // GET → Consulta de usuario o listado
    // =====================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        boolean forzarListado = isTrue(request.getParameter("listar")) ||
                "list".equalsIgnoreCase(trim(request.getParameter("view")));

        String nick = trim(request.getParameter("nick"));

        // Si no se fuerza listado, usar el de sesión
        if (!forzarListado && isBlank(nick)) {
            HttpSession sAux = request.getSession(false);
            if (sAux != null) {
                Object obj = sAux.getAttribute("nick");
                if (obj instanceof String) nick = (String) obj;
            }
        }

        IControladorUsuario ctrlUsuario = fabrica.getInstance().getIControladorUsuario();

        if (forzarListado || isBlank(nick)) {
            // === LISTA DE USUARIOS ===
            Set<DTDatosUsuario> usuariosSet = new HashSet<>();

            try {
                usuariosSet = ctrlUsuario.obtenerUsuariosDT();
            } catch (UsuarioNoExisteException e) {
                e.printStackTrace(); // solo para debug
                request.setAttribute("error", "No se pudo obtener la lista de usuarios.");
            }

            List<DTDatosUsuario> usuarios = new ArrayList<>(usuariosSet);
            request.setAttribute("usuarios", usuarios);

            Map<String, String> fotos = new HashMap<>();
            Map<String, String> nombres = new HashMap<>();

            String ctx = request.getContextPath();
            ServletContext sc = getServletContext();

            for (DTDatosUsuario u : usuarios) {
                if (u == null) continue;

                String nombreSeguro = nvl(u.getNombre(), u.getNickname());
                nombres.put(u.getNickname(), nombreSeguro);

                String url = resolveUserImageUrl(u.getImagen(), ctx, sc);
                if (url != null) fotos.put(u.getNickname(), url);
            }

            request.setAttribute("fotos", fotos);
            request.setAttribute("nombres", nombres);


            Map<String,String> instFotos = buildInstitutionImageMap(ctrlUsuario.getInstituciones(), request.getContextPath(), getServletContext());
            request.setAttribute("instFotos", instFotos);

        } else {
            // === PERFIL INDIVIDUAL ===
            try {
                DTDatosUsuario usuario = ctrlUsuario.obtenerDatosUsuario(nick);
                request.setAttribute("usuario", usuario);
                request.setAttribute("usuarioNombreSeguro",
                        nvl(usuario.getNombre(), usuario.getNickname()));

                // foto
                String url = resolveUserImageUrl(
                        usuario.getImagen(),
                        request.getContextPath(),
                        getServletContext()
                );
                if (url != null) request.setAttribute("usrImagenUrl", url);

                // mapa edicion -> evento
                IControladorEvento ctrlEvento = fabrica.getInstance().getIControladorEvento();
                List<DTEvento> eventos = ctrlEvento.listarEventos();
                Map<String, String> edicionToEvento = new HashMap<>();

                if (eventos != null) {
                    for (DTEvento ev : eventos) {
                        if (ev == null || ev.getEdiciones() == null) continue;
                        for (String edNombre : ev.getEdiciones()) {
                            if (edNombre != null && !edNombre.isBlank()) {
                                edicionToEvento.put(edNombre, ev.getNombre());
                            }
                        }
                    }
                }

                request.setAttribute("edicionToEvento", edicionToEvento);

                // Cargar instituciones para el dropdown
                request.setAttribute("instituciones", ctrlUsuario.getInstituciones());

                // Prepare institution images mapping (best-effort guess)
                Map<String,String> instFotos = buildInstitutionImageMap(ctrlUsuario.getInstituciones(), request.getContextPath(), getServletContext());
                request.setAttribute("instFotos", instFotos);

            } catch (UsuarioNoExisteException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("error", "El usuario \"" + nick + "\" no existe.");
            }
        }

        request.getRequestDispatcher("/WEB-INF/usuario/ConsultaUsuario.jsp")
                .forward(request, response);
    }

    // =====================================================
    // POST → Modificación de usuario
    // =====================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        IControladorUsuario ctrlUsuario = fabrica.getInstance().getIControladorUsuario();

        // === POST /usuario/modificar ===
        if ("/usuario/modificar".equals(path)) {

            HttpSession sAux = request.getSession(false);
            String nick = (sAux != null) ? (String) sAux.getAttribute("nick") : null;

            if (nick == null || nick.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/auth/login");
                return;
            }

            // Leer parámetros del formulario
            String nombre      = request.getParameter("nombre");
            String apellido    = request.getParameter("apellido");
            String email       = request.getParameter("email");
            String institucion = request.getParameter("institucion");
            String descripcion = request.getParameter("descripcion");
            String nacStr      = request.getParameter("fechaNac");
            String password    = request.getParameter("password");
            String link        = request.getParameter("link");
            

            LocalDate fechaNac = null;
            if (nacStr != null && !nacStr.isBlank()) {
                try {
                    fechaNac = LocalDate.parse(nacStr);
                } catch (Exception ignored) {}
            }

            try {
                ctrlUsuario.modificarDatosUsuario(nick, nombre, descripcion, link, apellido, fechaNac, institucion);

                // Si se ingresó nueva contraseña, actualizarla
                if (password != null && !password.isBlank()) {
                    ctrlUsuario.modificarContrasenia(nick, password);
                }

                // Redirigir al perfil actualizado
                response.sendRedirect(request.getContextPath() + "/usuario/ConsultaUsuario?nick=" +
                        java.net.URLEncoder.encode(nick, java.nio.charset.StandardCharsets.UTF_8));

            } catch (UsuarioNoExisteException | UsuarioTipoIncorrectoException e) {
                request.setAttribute("error", e.getMessage());
                doGet(request, response); // recargar vista con error
            }
        } else {
            doGet(request, response);
        }
    }

    // Helper to guess institution image URLs by name and checking common locations
    private Map<String,String> buildInstitutionImageMap(Set<String> instituciones, String ctx, ServletContext sc) {
        Map<String,String> map = new HashMap<>();
        if (instituciones == null || instituciones.isEmpty()) return map;
        String[] exts = new String[]{".png",".jpg",".jpeg",".webp",".gif"};
        for (String inst : instituciones) {
            if (inst == null || inst.isBlank()) continue;
            // Candidate filenames: sanitized name, original name, lowercase
            String safe = inst.replaceAll("[^a-zA-Z0-9]", "_");
            List<String> candidates = new ArrayList<>();
            for (String ext: exts) {
                candidates.add("/img/instituciones/" + safe + ext);
                candidates.add("/img/instituciones/" + inst + ext);
                candidates.add("/img/" + safe + ext);
                candidates.add("/img/" + inst + ext);
            }
            // also try without extension
            candidates.add("/img/instituciones/" + safe);
            candidates.add("/img/instituciones/" + inst);
            for (String candRel : candidates) {
                Boolean ex = exists(sc, candRel);
                if (Boolean.TRUE.equals(ex)) {
                    map.put(inst, ctx + candRel);
                    break;
                }
            }
        }
        return map;
    }

    // === Helpers ===
    private static String trim(String s) { return (s == null) ? null : s.trim(); }
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
    private static String nvl(String s, String alt) {
        return (s == null || s.trim().isEmpty()) ? alt : s;
    }
    private static boolean isTrue(String v) {
        if (v == null) return false;
        String s = v.trim().toLowerCase(Locale.ROOT);
        return "1".equals(s) || "true".equals(s) || "on".equals(s) || "yes".equals(s) || "y".equals(s);
    }

    private static String resolveUserImageUrl(String raw, String ctx, ServletContext sc) {
        if (raw == null || raw.isBlank()) return null;

        String v = raw.trim().replace("\\", "/");
        String low = v.toLowerCase(Locale.ROOT);

        if (low.startsWith("http://") || low.startsWith("https://")) return v;
        if (v.startsWith("/")) return ctx + v;
        if (low.startsWith("img/")) return ctx + "/" + v;
        if (low.startsWith("usuarios/")) return ctx + "/img/" + v;

        String relImg = "/img/" + v;
        String relUsr = "/img/usuarios/" + v;

        Boolean exImg = exists(sc, relImg);
        Boolean exUsr = exists(sc, relUsr);

        if (exImg != null || exUsr != null) {
            if (Boolean.TRUE.equals(exImg)) return ctx + relImg;
            if (Boolean.TRUE.equals(exUsr)) return ctx + relUsr;
            return null;
        }

        return ctx + relImg;
    }

    private static Boolean exists(ServletContext sc, String rel) {
        String abs = sc.getRealPath(rel);
        if (abs == null) return null;
        return Files.exists(Path.of(abs));
    }
}
