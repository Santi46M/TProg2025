package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import logica.CargaDatosPrueba;
import logica.fabrica;
import logica.interfaces.IControladorEvento;

@WebServlet("/precargar")
public class PrecargaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	System.out.println("✅ Entrando al PrecargaServlet...");


    	
        IControladorEvento ce = fabrica.getInstance().getIControladorEvento();
        try {
        	// Con los eventos chequeamos si realmente hay datos cargados o no
        	boolean yaHayDatos = (ce.listarEventos() != null && !ce.listarEventos().isEmpty());
        	System.out.println("Datos existentes: " + yaHayDatos);
        	if (!yaHayDatos) {
            	// Cargar datos
                CargaDatosPrueba.cargar();
                System.out.println("✅ Datos de prueba cargados correctamente.");
        	}else {
        		System.out.println("⚠️ Ya hay datos cargados, no se realizó la precarga.");
        	}
        	getServletContext().setAttribute("datosPrecargados", Boolean.TRUE);


            // Redirigir al inicio dinámico
            resp.sendRedirect(req.getContextPath() + "/inicio");

        } catch (Exception e) {
            e.printStackTrace();
            getServletContext().setAttribute("datosPrecargados", Boolean.FALSE);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar datos de prueba.");
            
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("🔍 Entró al doGet del PrecargaServlet (por GET)");
        doPost(req, resp);
    }

}
