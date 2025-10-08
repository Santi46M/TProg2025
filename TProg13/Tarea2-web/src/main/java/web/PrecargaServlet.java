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
            // 🔹 Instancia la clase de carga de datos
            CargaDatosPrueba cargador = new CargaDatosPrueba();

            // 🔹 Ejecuta el método que está dentro del .jar
            cargador.cargar();

            req.setAttribute("mensaje", "Datos precargados correctamente desde logica.jar");
        

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("mensaje", "Error al precargar: " + e.getMessage());

        }

        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
