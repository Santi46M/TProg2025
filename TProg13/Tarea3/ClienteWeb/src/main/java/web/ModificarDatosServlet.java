package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        // ===== Colección de alerts (se muestran en la JSP) =====
        List<String> alertsOk   = new ArrayList<>();
        List<String> alertsWarn = new ArrayList<>();
        List<String> alertsErr  = new ArrayList<>();

        // Para comparar cambios, traemos el usuario actual
        DtDatosUsuario antes;
        try {
            antes = port.obtenerDatosUsuario(nick);
        } catch (Exception e) {
            // Si ni siquiera podemos cargar el perfil, devolvemos error duro
            request.setAttribute("error", "No se pudo cargar el perfil actual: " + safeMsg(e));
            request.getRequestDispatcher("/WEB-INF/usuario/ConsultaUsuario.jsp").forward(request, response);
            return;
        }

        // ---- Parámetros del formulario
        String nombre      = orEmpty(request.getParameter("nombre"));
        String apellido    = orEmpty(request.getParameter("apellido"));
        String institucion = orEmpty(request.getParameter("institucion"));
        String descripcion = orEmpty(request.getParameter("descripcion"));
        String link        = orEmpty(request.getParameter("link"));
        String password    = orEmpty(request.getParameter("password"));
        String password2   = orEmpty(request.getParameter("password2"));
        String nacStr      = orEmpty(request.getParameter("fechaNac"));

        // Guardamos overrides para que la JSP pueda repintar lo que el usuario tipeó
        request.setAttribute("form_nombre", nombre);
        request.setAttribute("form_apellido", apellido);
        request.setAttribute("form_institucion", institucion);
        request.setAttribute("form_descripcion", descripcion);
        request.setAttribute("form_link", link);
        request.setAttribute("form_fechaNac", nacStr);

        // ===== Validaciones no fatales → vamos acumulando en alertsErr y si hay, forward
        String fechaStr = "";
        if (!nacStr.isBlank()) {
            LocalDate hoy = LocalDate.now();
            try {
                LocalDate fnac = LocalDate.parse(nacStr); // yyyy-MM-dd
                if (fnac.isAfter(hoy)) {
                    alertsErr.add("La fecha de nacimiento no puede ser futura.");
                } else if (fnac.isBefore(hoy.minusYears(120))) {
                    alertsErr.add("La fecha de nacimiento es inválida (¿más de 120 años?).");
                } else {
                    fechaStr = nacStr; // OK
                }
            } catch (DateTimeParseException e) {
                alertsErr.add("Fecha de nacimiento inválida. Use formato yyyy-MM-dd.");
            }
        } // si viene vacía, queda "" (no cambia)

        boolean quiereCambiarPass = !password.isBlank() || !password2.isBlank();
        if (quiereCambiarPass) {
            if (password.isBlank() || password2.isBlank()) {
                alertsErr.add("Debés completar ambos campos de contraseña.");
            } else if (!password.equals(password2)) {
                alertsErr.add("Las contraseñas no coinciden.");
            } else if (password.length() < 6) {
                alertsErr.add("La contraseña debe tener al menos 6 caracteres.");
            }
        }

        // Imagen: si falla el upload, lo reportamos como error y no seguimos
        String imgFileName = "";
        try {
            Part imgPart = request.getPart("imagen"); // name="imagen"
            if (imgPart != null && imgPart.getSize() > 0) {
                String onlyName = Path.of(imgPart.getSubmittedFileName()).getFileName().toString();
                String rel = "/img/usuarios/" + onlyName;
                String abs = getServletContext().getRealPath(rel);
                if (abs == null) throw new IOException("No se pudo resolver ruta de escritura para " + rel);
                Files.createDirectories(Path.of(abs).getParent());
                imgPart.write(abs);
                imgFileName = onlyName;
                alertsOk.add("Imagen subida correctamente.");
            } else {
                String actualImg = antes.getImagen();
                imgFileName = (actualImg != null) ? actualImg : "";
            }
        } catch (IllegalStateException ise) {
            alertsErr.add("La imagen excede el tamaño permitido o es inválida.");
        } catch (IOException ioe) {
            alertsErr.add("No se pudo guardar la imagen: " + safeMsg(ioe));
        }

        // Si hubo errores de validación → mostramos alerts y volvemos a la vista
        if (!alertsErr.isEmpty()) {
            setAlertsAndForward(request, response, alertsOk, alertsWarn, alertsErr, antes);
            return;
        }

        // ===== Invocar WS (sin nulls)
        boolean huboCambios = false;
        try {
            // Detectar cambios básicos (para alertita si no cambió nada)
            boolean cambioNombre      = !eqSafe(antes.getNombre(), nombre);
            boolean cambioApellido    = !eqSafe(nvl(antes.getApellido()), apellido);
            boolean cambioInstitucion = !eqSafe(nvl(antes.getNombreInstitucion()), institucion);
            boolean cambioDesc        = !eqSafe(nvl(antes.getDesc()), descripcion);
            boolean cambioLink        = !eqSafe(nvl(antes.getLink()), link);
            boolean cambioFecha       = !eqSafe(dateToStr(antes.getFechaNac()), fechaStr);
            boolean cambioImg         = !eqSafe(nvl(antes.getImagen()), imgFileName);

            huboCambios = cambioNombre || cambioApellido || cambioInstitucion ||
                          cambioDesc   || cambioLink    || cambioFecha || cambioImg;

            if (!huboCambios && !quiereCambiarPass) {
                alertsWarn.add("No realizaste cambios en tus datos.");
                setAlertsAndForward(request, response, alertsOk, alertsWarn, alertsErr, antes);
                return;
            }

            // modificarDatosUsuario(nick, nombre, descripcion, link, apellido, fechaStr, institucion, imgFileName)
            port.modificarDatosUsuario(
                    nick,
                    nombre,
                    descripcion,
                    link,
                    apellido,
                    fechaStr,     // "" si no se cambió
                    institucion,
                    imgFileName
            );
            if (huboCambios) alertsOk.add("Datos actualizados correctamente.");

            if (quiereCambiarPass) {
                tryUpdatePasswordByReflection(port, nick, password);
                alertsOk.add("Contraseña actualizada.");
            }

            // Recargamos DTO actualizado para mostrar la vista con los nuevos datos y alerts
            DtDatosUsuario despues = port.obtenerDatosUsuario(nick);

            request.setAttribute("usuario", despues);
            putAlerts(request, alertsOk, alertsWarn, alertsErr);
            request.getRequestDispatcher("/WEB-INF/usuario/ConsultaUsuario.jsp").forward(request, response);
            return;

        } catch (UsuarioNoExisteException_Exception | UsuarioTipoIncorrectoException_Exception e) {
            alertsErr.add("El usuario '" + nick + "' no existe o el tipo es inválido.");
            setAlertsAndForward(request, response, alertsOk, alertsWarn, alertsErr, antes);
        } catch (Exception e) {
            alertsErr.add("No se pudieron actualizar los datos: " + safeMsg(e));
            setAlertsAndForward(request, response, alertsOk, alertsWarn, alertsErr, antes);
        }
    }

    /* ===== Helpers ===== */

    private static void setAlertsAndForward(HttpServletRequest req, HttpServletResponse resp,
                                            List<String> ok, List<String> warn, List<String> err,
                                            DtDatosUsuario usuarioParaMostrar)
            throws ServletException, IOException {
        putAlerts(req, ok, warn, err);
        if (req.getAttribute("usuario") == null && usuarioParaMostrar != null) {
            req.setAttribute("usuario", usuarioParaMostrar);
        }
        req.getRequestDispatcher("/WEB-INF/usuario/ConsultaUsuario.jsp").forward(req, resp);
    }

    private static void putAlerts(HttpServletRequest req,
                                  List<String> ok, List<String> warn, List<String> err) {
        if (ok   != null && !ok.isEmpty())   req.setAttribute("alerts_ok",   ok);
        if (warn != null && !warn.isEmpty()) req.setAttribute("alerts_warn", warn);
        if (err  != null && !err.isEmpty())  req.setAttribute("alerts_err",  err);
    }

    // Intenta port.modificarContrasenia / modificarContrasena / cambiarContrasenia / cambiarContrasena
    private static void tryUpdatePasswordByReflection(PublicadorUsuario port, String nick, String newPass) throws Exception {
        Exception last = null;
        String[] nombres = { "modificarContrasenia", "modificarContrasena", "cambiarContrasenia", "cambiarContrasena" };
        for (String m : nombres) {
            try {
                port.getClass().getMethod(m, String.class, String.class).invoke(port, nick, newPass);
                return; // éxito
            } catch (NoSuchMethodException nsme) {
                last = nsme;
            } catch (Exception ex) {
                last = ex;
            }
        }
        throw (last != null) ? last : new NoSuchMethodException("No existe método para cambiar contraseña en el stub.");
    }

    private static String orEmpty(String s) { return (s == null) ? "" : s; }
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    private static String nvl(String s) { return (s == null) ? "" : s; }
    private static boolean eqSafe(String a, String b) { return Objects.equals(nvl(a), nvl(b)); }
    private static String dateToStr(Object fechaNac) {
        return (fechaNac == null) ? "" : fechaNac.toString(); // el stub suele traer LocalDate -> toString() yyyy-MM-dd
    }

    private static String safeMsg(Throwable t) {
        String m = (t == null) ? "" : t.getMessage();
        return (m == null) ? t.getClass().getSimpleName() : m;
    }
}
