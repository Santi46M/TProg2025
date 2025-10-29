package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.time.LocalDate;

import publicadores.DtDatosUsuario;
import publicadores.DtDatosUsuarioArray;
import publicadores.PublicadorUsuario;
import publicadores.PublicadorUsuarioService;
import publicadores.UsuarioNoExisteException_Exception;
import publicadores.UsuarioTipoIncorrectoException_Exception;

@WebServlet(urlPatterns = {
        "/usuario/ConsultaUsuario",
        "/usuario/modificar",
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
                String nickSesion = nickEnSesion(request);
                boolean esSuPropioPerfil = nickSesion != null && nickSesion.equals(usuario.getNickname());
                request.setAttribute("esSuPropioPerfil", esSuPropioPerfil);
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

        // === POST /usuario/modificar ===
        if ("/usuario/modificar".equals(path)) {

            HttpSession sAux = request.getSession(false);
            String nick = (sAux != null) ? (String) sAux.getAttribute("nick") : null;

            if (nick == null || nick.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/auth/login");
                return;
            }

            // Leer parámetros del formulario
            String nombre      = Optional.ofNullable(request.getParameter("nombre")).orElse("");
            String apellido    = Optional.ofNullable(request.getParameter("apellido")).orElse("");
            String institucion = Optional.ofNullable(request.getParameter("institucion")).orElse("");
            String descripcion = Optional.ofNullable(request.getParameter("descripcion")).orElse("");
            String nacStr      = Optional.ofNullable(request.getParameter("fechaNac")).orElse("");
            String password    = Optional.ofNullable(request.getParameter("password")).orElse("");
            String link        = Optional.ofNullable(request.getParameter("link")).orElse("");


       
            

            LocalDate fechaNac = null;
            if (nacStr != null && !nacStr.isBlank()) {
                try {
                    fechaNac = LocalDate.parse(nacStr);
                } catch (Exception ignored) {}
            }
            String fechaStr = (fechaNac != null) ? fechaNac.toString() : null;
            Part imgPart = request.getPart("imagen");
            String imgFileName = null;

            if (imgPart != null && imgPart.getSize() > 0) {
                imgFileName = Path.of(imgPart.getSubmittedFileName()).getFileName().toString();
                imgPart.write(getServletContext().getRealPath("/img/usuarios/" + imgFileName));
            } else {
                imgFileName = ""; // o mantener la actual del usuario
            }

            

            try {
                port.modificarDatosUsuario(nick, nombre, descripcion, link, apellido, fechaStr, institucion,imgFileName);

                // Si se ingresó nueva contraseña, actualizarla
                if (password != null && !password.isBlank()) {
                    port.modificarContrasenia(nick, password);
                }

                // Redirigir al perfil actualizado
                response.sendRedirect(request.getContextPath() + "/usuario/ConsultaUsuario?nick=" +
                        java.net.URLEncoder.encode(nick, java.nio.charset.StandardCharsets.UTF_8));

            } catch (UsuarioNoExisteException_Exception | UsuarioTipoIncorrectoException_Exception e) {
//                request.setAttribute("error", e.getMessage());
//                doGet(request, response); // recargar vista con error
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("error", "El usuario '" + nick + "' no existe.");
            }
        } else {
            doGet(request, response);
        }
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
}