package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import publicadores.PublicadorUsuario;
import publicadores.PublicadorUsuarioService;
import publicadores.DtDatosUsuario;
import publicadores.UsuarioNoExisteException_Exception;
import publicadores.UsuarioTipoIncorrectoException_Exception;

@WebServlet(urlPatterns = { "/usuario/modificar" })
@MultipartConfig
public class ModificarDatosServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ---- Sesión obligatoria
        HttpSession sAux = request.getSession(false);
        String nick = (sAux != null) ? (String) sAux.getAttribute("nick") : null;
        if (isBlank(nick)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        PublicadorUsuarioService service = new PublicadorUsuarioService();
        PublicadorUsuario port = service.getPublicadorUsuarioPort();

        // ---- Parámetros del formulario
        String nombre      = orEmpty(request.getParameter("nombre"));
        String apellido    = orEmpty(request.getParameter("apellido"));
        String institucion = orEmpty(request.getParameter("institucion"));
        String descripcion = orEmpty(request.getParameter("descripcion"));
        String link        = orEmpty(request.getParameter("link"));
        String password    = orEmpty(request.getParameter("password"));

        // OJO: en tu JSP el name es "fechaNac" (no "fechaNacimiento")
        String nacStr      = orEmpty(request.getParameter("fechaNac"));

        // Parseo opcional (sólo para validar formato); el WS recibe String.
        String fechaStr = "";
        if (!nacStr.isBlank()) {
            try {
                LocalDate.parse(nacStr); // valida "yyyy-MM-dd" de input type=date
                fechaStr = nacStr;
            } catch (Exception e) {
                // Si viene mal, devolvemos error de formulario
                request.setAttribute("error", "Fecha de nacimiento inválida. Use formato yyyy-MM-dd.");
                request.getRequestDispatcher("/WEB-INF/usuario/ConsultaUsuario.jsp").forward(request, response);
                return;
            }
        } else {
            // MUY IMPORTANTE: jamás enviar null → usar "" (string vacío) o el valor actual.
            fechaStr = "";
        }

        // ---- Imagen: si no suben una nueva, conservar la actual
        String imgFileName = "";
        try {
            Part imgPart = request.getPart("imagen"); // name="imagen" en el form
            if (imgPart != null && imgPart.getSize() > 0) {
                String onlyName = Path.of(imgPart.getSubmittedFileName()).getFileName().toString();
                String rel = "/img/usuarios/" + onlyName;
                String abs = getServletContext().getRealPath(rel);
                if (abs == null) {
                    // fallback por si realPath no está disponible (ej., empaquetado)
                    throw new IOException("No se pudo resolver ruta de escritura para " + rel);
                }
                // aseguramos carpeta
                Files.createDirectories(Path.of(abs).getParent());
                imgPart.write(abs);
                imgFileName = onlyName;
            } else {
                // mantener imagen actual
                try {
                    DtDatosUsuario actual = port.obtenerDatosUsuario(nick);
                    String actualImg = actual.getImagen();
                    imgFileName = (actualImg != null) ? actualImg : "";
                } catch (Exception ignore) {
                    imgFileName = "";
                }
            }
        } catch (IllegalStateException ise) {
            // tamaño excedido u otro error de multipart
            request.setAttribute("error", "La imagen excede el tamaño permitido o es inválida.");
            request.getRequestDispatcher("/WEB-INF/usuario/ConsultaUsuario.jsp").forward(request, response);
            return;
        }

        // ---- Invocar WS (sin nulls)
        try {
            // Firma según tu stub actual:
            // modificarDatosUsuario(nick, nombre, descripcion, link, apellido, fechaStr, institucion, imgFileName)
            port.modificarDatosUsuario(
                    nick,
                    nombre,
                    descripcion,
                    link,
                    apellido,
                    fechaStr,     // nunca null ("" si vacío)
                    institucion,
                    imgFileName
            );

            if (!password.isBlank()) {
                port.modificarContrasenia(nick, password);
            }

            // Ok → redirigir al perfil
            response.sendRedirect(request.getContextPath() + "/usuario/ConsultaUsuario?nick=" +
                    java.net.URLEncoder.encode(nick, java.nio.charset.StandardCharsets.UTF_8));

        } catch (UsuarioNoExisteException_Exception | UsuarioTipoIncorrectoException_Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("error", "El usuario '" + nick + "' no existe o el tipo es inválido.");
            request.getRequestDispatcher("/WEB-INF/usuario/ConsultaUsuario.jsp").forward(request, response);
        } catch (Exception e) {
            // Errores genéricos del WS (incluye BP 1.1 si algo quedó null)
            request.setAttribute("error", "No se pudieron actualizar los datos: " + safeMsg(e));
            request.getRequestDispatcher("/WEB-INF/usuario/ConsultaUsuario.jsp").forward(request, response);
        }
    }

    // ===== Helpers =====
    private static String orEmpty(String s) { return (s == null) ? "" : s; }
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    private static String safeMsg(Throwable t) {
        String m = (t == null) ? "" : t.getMessage();
        return (m == null) ? t.getClass().getSimpleName() : m;
    }
}
