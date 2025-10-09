package web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import logica.Interfaces.IControladorEvento;
import logica.Datatypes.DTEvento;
import logica.fabrica;

@WebServlet({"/inicio", "/"})
public class InicioServlet extends HttpServlet {

    private IControladorEvento ce() {
        return fabrica.getInstance().getIControladorEvento();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<DTEvento> eventos = ce().listarEventos();
        req.setAttribute("eventos", eventos);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
    

}