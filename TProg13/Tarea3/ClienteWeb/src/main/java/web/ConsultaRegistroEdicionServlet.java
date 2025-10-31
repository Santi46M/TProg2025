package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import publicadores.DtDatosUsuario;
import publicadores.DtEdicion;
import publicadores.DtRegistro;
import publicadores.DtEvento;
import publicadores.PublicadorUsuario;
import publicadores.PublicadorUsuarioService;
import publicadores.PublicadorEvento;
import publicadores.PublicadorEventoService;
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
        String accion = req.getParameter("accion"); 

        if (idRegistro == null || idRegistro.isBlank() || nick == null) {
            req.setAttribute("error", "Registro no especificado o sesión no iniciada.");
            req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
            return;
        }
        System.out.println("Entra al doGet");

        PublicadorUsuarioService service = new PublicadorUsuarioService();
        PublicadorUsuario port = service.getPublicadorUsuarioPort();

        PublicadorEventoService evSvc = new PublicadorEventoService();
        PublicadorEvento evPort = evSvc.getPublicadorEventoPort();

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

            DtRegistro dtRegistro = evPort.consultaRegistro(nick, idRegistro);
            if (dtRegistro == null) {
                req.setAttribute("error", "No se encontró el registro solicitado.");
                req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
                return;
            }

            // ============================================================
            // 🔹 Usamos el atributo del DTO en lugar de la sesión
            // ============================================================
            boolean asistio = false;
            try {
                asistio = dtRegistro.isAsistio(); // ← propiedad del DTO
                System.out.println("La asistencia en servlet es " + asistio);
            } catch (Exception ignore) {}

            // 🔹 Si viene del POST de marcar asistencia, refrescar el registro
            if ("true".equals(req.getParameter("marcoAsistencia"))) {
                dtRegistro = evPort.consultaRegistro(nick, idRegistro);
                asistio = dtRegistro.isAsistio();
            }

            // ============================================================
            // 🔹 Si la acción es "certificado", generar y descargar el PDF
            // ============================================================
            if ("certificado".equalsIgnoreCase(accion)) {
                DtEvento eventoRegistro = evPort.consultaDTEvento(dtRegistro.getEvento());
                DtEdicion edicionRegisro = evPort.obtenerDtEdicion(
                        eventoRegistro.getNombre(), dtRegistro.getEdicion());
                generarCertificadoPDF(resp, dtoUsuario, dtRegistro, eventoRegistro, edicionRegisro);
                return;
            }

            // 🔸 Pasamos al JSP el flag real del DTO
            req.setAttribute("usuario", dtoUsuario);
            req.setAttribute("registro", dtRegistro);
            req.setAttribute("asistio", asistio);
            req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error al consultar el registro: " + e.getMessage());
            req.getRequestDispatcher(JSP_CONSULTA).forward(req, resp);
        }
    }

    // ============================================================
    // 🔹 POST: marcar asistencia en el publicador
    // ============================================================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");
        String idRegistro = req.getParameter("idRegistro");
        HttpSession session = req.getSession(false);
        String nick = (session != null) ? (String) session.getAttribute("nick") : null;

        if ("marcarAsistencia".equalsIgnoreCase(accion)) {
            try {
                PublicadorEventoService evSvc = new PublicadorEventoService();
                PublicadorEvento evPort = evSvc.getPublicadorEventoPort();

                // 🔹 Llamada al método remoto para marcar asistencia
                evPort.marcarAsistenciaRegistro(nick, idRegistro); // ← método que debes tener en el publicador
                

                // Redirigir a GET para refrescar estado
                resp.sendRedirect(req.getContextPath()
                        + "/registro/ConsultaRegistroEdicion?idRegistro=" + idRegistro + "&marcoAsistencia=true");
                return;
            } catch (Exception e) {
                req.setAttribute("error", "No se pudo registrar la asistencia: " + e.getMessage());
                doGet(req, resp);
                return;
            }
        }

        doGet(req, resp);
    }

    /** ================================================================
     *  Genera dinámicamente el PDF del certificado usando iText
     *  ================================================================ */
    private void generarCertificadoPDF(HttpServletResponse resp, DtDatosUsuario usr, DtRegistro reg, DtEvento ev, DtEdicion ed)
            throws IOException, DocumentException {

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition",
                "attachment; filename=Certificado_" + usr.getNickname() + ".pdf");

        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, resp.getOutputStream());
        doc.open();

        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Font textoFont = new Font(Font.FontFamily.HELVETICA, 12);

        Paragraph titulo = new Paragraph("Certificado de Asistencia", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(25);
        doc.add(titulo);

        doc.add(new Paragraph("Se certifica que:", textoFont));
        doc.add(new Paragraph(" ", textoFont));
        doc.add(new Paragraph("    " + usr.getNombre() + " " + usr.getApellido(),
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
        doc.add(new Paragraph(" ", textoFont));
        
        doc.add(new Paragraph("Ha asistido al evento: " + ev.getNombre(), textoFont));
        doc.add(new Paragraph("Edición: " + reg.getEdicion(), textoFont));
        doc.add(new Paragraph("Ciudad: " + ed.getCiudad(), textoFont));
        
        String fechaIni = reg.getFechaInicio();
        doc.add(new Paragraph("Fecha: " + fechaIni, textoFont));
        doc.add(new Paragraph(" ", textoFont));

        doc.add(new Paragraph("______________________________", textoFont));
        doc.add(new Paragraph("Firma del Organizador", textoFont));

        doc.close();
    }
}
