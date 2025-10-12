package web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import logica.interfaces.IControladorEvento;
import logica.datatypes.DTEvento;
import logica.fabrica;

@WebServlet({"/inicio"})

public class InicioServlet extends HttpServlet {

    private IControladorEvento ce() {
        return fabrica.getInstance().getIControladorEvento();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println("✅ Entró al InicioServlet");
        IControladorEvento ce = ce();
        List<DTEvento> eventos = ce.listarEventos();
        System.out.println("Eventos listados: " + (eventos != null ? eventos.size() : "null"));

        // Si no hay eventos, se considera que la precarga se perdió
        if (eventos == null || eventos.isEmpty()) {
            getServletContext().setAttribute("datosPrecargados", Boolean.FALSE);
            System.out.println("⚠️ No hay eventos, datosPrecargados = FALSE");
        }

        req.setAttribute("eventos", eventos);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }


}