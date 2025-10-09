package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import logica.CargaDatosPrueba;

@WebServlet("/precargar")
public class PrecargaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Cargar datos
            CargaDatosPrueba.cargar();

            // Guardar bandera a nivel de aplicación (solo una vez)
            getServletContext().setAttribute("datosPrecargados", true);

            // Redirigir al inicio dinámico
            resp.sendRedirect(req.getContextPath() + "/inicio");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar datos de prueba.");
        }
    }
}
