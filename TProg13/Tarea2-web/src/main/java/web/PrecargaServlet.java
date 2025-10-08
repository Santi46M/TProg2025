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
	        CargaDatosPrueba cargador = new CargaDatosPrueba();
	        cargador.cargar();

	        // Guardar a nivel de aplicación (no sesión)
	        getServletContext().setAttribute("datosPrecargados", true);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}


}
