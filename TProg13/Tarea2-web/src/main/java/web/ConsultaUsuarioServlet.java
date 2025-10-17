package web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

@WebServlet("/usuario/ConsultaUsuario")
public class ConsultaUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        boolean forzarListado = isTrue(request.getParameter("listar")) ||
                "list".equalsIgnoreCase(trim(request.getParameter("view")));

        String nick = trim(request.getParameter("nick"));

        // Si no se fuerza listado y no vino nick, usar el de sesión
        if (!forzarListado && isBlank(nick)) {
            HttpSession sAux = request.getSession(false);
            if (sAux != null) {
                Object obj = sAux.getAttribute("nick");
                if (obj instanceof String) nick = (String) obj;
            }
        }

        IControladorUsuario ctrlUsuario = fabrica.getInstance().getIControladorUsuario();

        if (forzarListado || isBlank(nick)) {
            // ====== LISTA DE USUARIOS ======
        	Set<DTDatosUsuario> usuariosSet = new HashSet<>();

        	try {
        	    usuariosSet = ctrlUsuario.obtenerUsuariosDT();
        	} catch (UsuarioNoExisteException e) {
        	    e.printStackTrace(); // solo para debugging
        	    request.setAttribute("error", "No se pudo obtener la lista de usuarios.");
        	}

        	// siempre asegurás que no sea null
        	request.setAttribute("usuarios", new ArrayList<>(usuariosSet));
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

        } else {
            // ====== PERFIL INDIVIDUAL ======
            try {
                DTDatosUsuario usuario = ctrlUsuario.obtenerDatosUsuario(nick);
                request.setAttribute("usuario", usuario);

                // nombre seguro (por si viene null)
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

            } catch (UsuarioNoExisteException e) {
                // Usuario no encontrado → 404 + mostrar listado
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("error", "El usuario \"" + nick + "\" no existe.");

                Set<DTDatosUsuario> usuariosSet = new HashSet<>();

                try {
                    usuariosSet = ctrlUsuario.obtenerUsuariosDT();
                } catch (UsuarioNoExisteException f) {
                    e.printStackTrace(); // solo para debugging
                    request.setAttribute("error", "No se pudo obtener la lista de usuarios.");
                }

                // siempre asegurás que no sea null
                request.setAttribute("usuarios", new ArrayList<>(usuariosSet));
                request.setAttribute("usuarios", new ArrayList<>(usuariosSet));
            }
        }

        request.getRequestDispatcher("/WEB-INF/usuario/ConsultaUsuario.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // ===== Helpers =====
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

    // ====== Resolver imagen ======
    private static String resolveUserImageUrl(String raw, String ctx, ServletContext sc) {
        if (raw == null || raw.isBlank()) return null;

        String v = raw.trim().replace("\\", "/");
        String low = v.toLowerCase(Locale.ROOT);

        if (low.startsWith("http://") || low.startsWith("https://")) return v;
        if (v.startsWith("/")) return ctx + v;

        if (low.startsWith("img/")) return ctx + "/" + v;
        if (low.startsWith("usuarios/")) return ctx + "/img/" + v;

        // sólo nombre → probar rutas conocidas
        String relImg = "/img/" + v;
        String relUsr = "/img/usuarios/" + v;

        Boolean exImg = exists(sc, relImg);
        Boolean exUsr = exists(sc, relUsr);

        if (exImg != null || exUsr != null) {
            if (Boolean.TRUE.equals(exImg)) return ctx + relImg;
            if (Boolean.TRUE.equals(exUsr)) return ctx + relUsr;
            return null;
        }

        // fallback
        return ctx + relImg;
    }

    private static Boolean exists(ServletContext sc, String rel) {
        String abs = sc.getRealPath(rel);
        if (abs == null) return null;
        return Files.exists(Path.of(abs));
    }
}
