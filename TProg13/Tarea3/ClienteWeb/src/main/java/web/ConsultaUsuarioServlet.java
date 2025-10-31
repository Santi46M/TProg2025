package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.*;
import java.time.LocalDate;

import publicadores.DtDatosUsuario;
import publicadores.DtDatosUsuarioArray;
import publicadores.PublicadorUsuario;
import publicadores.PublicadorUsuarioService;
import publicadores.UsuarioNoExisteException_Exception;
import publicadores.UsuarioTipoIncorrectoException_Exception;
import publicadores.StringArray;

@WebServlet(urlPatterns = {
        "/usuario/ConsultaUsuario",
        "/usuario/seguir",
        "/usuario/dejarSeguir"
})
@jakarta.servlet.annotation.MultipartConfig
public class ConsultaUsuarioServlet extends HttpServlet {
    private static final String JSP_CONSULTA = "/WEB-INF/usuario/ConsultaUsuario.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        boolean forzarListado = isTrue(request.getParameter("listar")) ||
                "list".equalsIgnoreCase(trim(request.getParameter("view")));

        String nick = trim(request.getParameter("nick"));
        if (!forzarListado && isBlank(nick)) {
            HttpSession sAux = request.getSession(false);
            if (sAux != null) {
                Object obj = sAux.getAttribute("nick");
                if (obj instanceof String) nick = (String) obj;
            }
        }

        PublicadorUsuarioService service = new PublicadorUsuarioService();
        PublicadorUsuario port = service.getPublicadorUsuarioPort();

        // --- AJAX helper: revalidate follow-status for a list of nicks ---
        String checkSeguidos = request.getParameter("checkSeguidos");
        String nicksParam = request.getParameter("nicks");
        if ("1".equals(checkSeguidos) && nicksParam != null) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            String[] parts = nicksParam.split(",");
            String sessionNick = nickEnSesion(request);
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            boolean first = true;
            for (String p : parts) {
                String target = p == null ? null : p.trim();
                if (target == null || target.isEmpty()) continue;
                boolean sigue = false;
                try {
                    if (sessionNick != null && !sessionNick.isBlank()) {
                        sigue = port.sigueA(sessionNick, target);
                    }
                } catch (Exception ignore) { }
                if (!first) sb.append(','); first = false;
                sb.append('"').append(escapeJson(target)).append('"').append(':').append(sigue);
            }
            sb.append('}');
            response.getWriter().write(sb.toString());
            return;
        }

        if (forzarListado || isBlank(nick)) {
            List<DtDatosUsuario> usuarios = new ArrayList<>();
            try {
                DtDatosUsuarioArray arr = port.obtenerUsuariosDT();
                usuarios = asList(arr);

                // ✅ Construir mapa de fotos de usuarios
                Map<String, String> fotos = new HashMap<>();
                String ctx = request.getContextPath();
                for (DtDatosUsuario u : usuarios) {
                    if (u.getImagen() != null && !u.getImagen().isBlank()) {
                        fotos.put(u.getNickname(), ctx + "/img/usuarios/" + u.getImagen());
                    }
                }
                request.setAttribute("fotos", fotos);

                // === Si hay usuario en sesión, construir mapa de a quién ya sigue
                String nickSesion = nickEnSesion(request);
                Map<String, Boolean> yaLoSigoMap = new HashMap<>();
                if (nickSesion != null && !nickSesion.isBlank()) {
                    for (DtDatosUsuario u : usuarios) {
                        if (u == null || u.getNickname() == null) continue;
                        String objetivo = u.getNickname();
                        if (nickSesion.equalsIgnoreCase(objetivo)) {
                            yaLoSigoMap.put(objetivo, false);
                            continue;
                        }
                        try {
                            boolean sigue = port.sigueA(nickSesion, objetivo);
                            yaLoSigoMap.put(objetivo, sigue);
                        } catch (Exception ignore) {
                            yaLoSigoMap.put(objetivo, false);
                        }
                    }
                }
                request.setAttribute("yaLoSigoMap", yaLoSigoMap);

            } catch (Exception e) {
                request.setAttribute("error", "No se pudo obtener la lista de usuarios.");
            }

            request.setAttribute("usuarios", usuarios);
        } else {
            try {
                DtDatosUsuario usuario = port.obtenerDatosUsuario(nick);
                String ctx = request.getContextPath();
                String imagenUrl = ctx + "/img/usuarios/" + usuario.getImagen(); 
                request.setAttribute("usrImagenUrl", imagenUrl);
                request.setAttribute("usuario", usuario);

                // instituciones para el dropdown
                List<String> instituciones = Collections.emptyList();
                try {
                    StringArray arr = port.listarInstituciones();
                    if (arr != null && arr.getItem() != null) instituciones = arr.getItem();
                } catch (Exception ignore) {}
                request.setAttribute("instituciones", instituciones);

                // mapa edicion -> evento (no disponible en este cliente webservice - enviar empty map)
                Map<String, String> edicionToEvento = new HashMap<>();
                request.setAttribute("edicionToEvento", edicionToEvento);

                // imágenes instituciones
                Map<String,String> instFotos = new HashMap<>();
                String[] exts = new String[]{".png",".jpg",".jpeg",".webp",".gif"};
                ServletContext sc = getServletContext();
                for (String inst : instituciones) {
                    if (inst == null || inst.isBlank()) continue;
                    String safe = inst.replaceAll("[^a-zA-Z0-9]", "_");
                    List<String> candidates = new ArrayList<>();
                    for (String ext: exts) {
                        candidates.add("/img/instituciones/" + safe + ext);
                        candidates.add("/img/instituciones/" + inst + ext);
                        candidates.add("/img/" + safe + ext);
                        candidates.add("/img/" + inst + ext);
                    }
                    candidates.add("/img/instituciones/" + safe);
                    candidates.add("/img/instituciones/" + inst);
                    for (String candRel : candidates) {
                        String abs = sc.getRealPath(candRel);
                        if (abs != null && Files.exists(Path.of(abs))) {
                            instFotos.put(inst, ctx + candRel);
                            break;
                        }
                    }
                }
                request.setAttribute("instFotos", instFotos);

                // === Rol real del perfil consultado (organizador/asistente)
                boolean esPerfilOrganizador = (usuario.getDesc() != null) || (usuario.getLink() != null);
                request.setAttribute("esPerfilOrganizador", esPerfilOrganizador);

                // === Follow/unfollow flags
                String nickSesion = nickEnSesion(request);
                boolean esSuPropioPerfil = nickSesion != null && nickSesion.equals(usuario.getNickname());
                request.setAttribute("esSuPropioPerfil", esSuPropioPerfil);

                boolean yaLoSigo = false;
                if (!esSuPropioPerfil && nickSesion != null) {
                    try {
                        yaLoSigo = port.sigueA(nickSesion, usuario.getNickname());
                    } catch (Exception ignore) {}
                }
                request.setAttribute("yaLoSigo", yaLoSigo);

            } catch (UsuarioNoExisteException_Exception e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("error", "El usuario '" + nick + "' no existe.");
            }
        }
        request.getRequestDispatcher(JSP_CONSULTA).forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        PublicadorUsuarioService service = new PublicadorUsuarioService();
        PublicadorUsuario port = service.getPublicadorUsuarioPort();

        // === Seguir / Dejar de seguir (SIN AJAX) ===
        if ("/usuario/seguir".equals(path) || "/usuario/dejarSeguir".equals(path)) {
            HttpSession sAux = request.getSession(false);
            String nickSesion = (sAux != null) ? (String) sAux.getAttribute("nick") : null;
            if (nickSesion == null || nickSesion.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/auth/login");
                return;
            }

            String objetivo = trim(request.getParameter("a")); // nick del perfil objetivo
            if (isBlank(objetivo)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'a'.");
                return;
            }

            if (!nickSesion.equalsIgnoreCase(objetivo)) {
                try {
                    if ("/usuario/seguir".equals(path)) {
                        port.seguirUsuario(nickSesion, objetivo);
                    } else {
                        port.dejarSeguirUsuario(nickSesion, objetivo);
                    }
                } catch (Exception ignore) {}
            }

            // If this was an AJAX request, return a simple OK response (no redirect)
            String xrw = request.getHeader("X-Requested-With");
            boolean isAjax = xrw != null && "XMLHttpRequest".equalsIgnoreCase(xrw);
            if (isAjax) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/plain; charset=UTF-8");
                response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
                response.getWriter().write("OK");
                return;
            }

            // If the request came from the listing (param 'listar' present), stay on the listing
            String listarParam = trim(request.getParameter("listar"));
            if (!isBlank(listarParam)) {
                response.sendRedirect(request.getContextPath() + "/usuario/ConsultaUsuario?listar=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/usuario/ConsultaUsuario?nick=" +
                        java.net.URLEncoder.encode(objetivo, java.nio.charset.StandardCharsets.UTF_8));
            }
            return;
        }

        // POST /usuario/modificar has been moved to a dedicated servlet.
        // This servlet no longer performs modification; return 404 so the caller
        // can be updated to the new endpoint (or implement the new servlet).
        if ("/usuario/modificar".equals(path)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        doGet(request, response);
    }

    private String nickEnSesion(HttpServletRequest req) {
        HttpSession sAux = req.getSession(false);
        return sAux == null ? null : (String) sAux.getAttribute("nick");
    }

    private static String trim(String s) { return (s == null) ? null : s.trim(); }
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
    private static boolean isTrue(String v) {
        if (v == null) return false;
        String s = v.trim().toLowerCase(Locale.ROOT);
        return "1".equals(s) || "true".equals(s) || "on".equals(s) || "yes".equals(s) || "y".equals(s);
    }
    private static java.util.List<DtDatosUsuario> asList(DtDatosUsuarioArray arr) {
        if (arr == null) return java.util.List.of();
        try {
            java.util.List<DtDatosUsuario> l = arr.getItem();
            return (l == null) ? java.util.List.of() : l;
        } catch (NoSuchMethodError e) {
            try {
                java.util.List<DtDatosUsuario> l = (java.util.List<DtDatosUsuario>) arr.getClass()
                        .getMethod("getItems").invoke(arr);
                return (l == null) ? java.util.List.of() : l;
            } catch (Exception ignore) {
                return java.util.List.of();
            }
        }
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}