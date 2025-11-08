package web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import publicadores.*;

@WebServlet({"/inicio"})
public class InicioServlet extends HttpServlet {
    private final PublicadorEventoService service = new PublicadorEventoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<DtEvento> eventos = new ArrayList<>();
        try {
            PublicadorEvento port = service.getPublicadorEventoPort();
            DtEventoArray arr = port.listarEventosVigentes();
            if (arr != null && arr.getItem() != null) eventos = arr.getItem();
        } catch (Exception ignored) {}

        String context = "/ServidorCentral-0.0.1-SNAPSHOT";
        String baseUrl = "http://";
        try {
            Path propsPath = Path.of(System.getProperty("user.home"), ".trabajoUy", ".properties");
            Properties props = new Properties();
            props.load(Files.newInputStream(propsPath));
            String ip = props.getProperty("servidor.ip", "localhost");
            String puerto = props.getProperty("servidor.puerto", "8080");
            baseUrl += ip + ":" + puerto + context + "/images/";
        } catch (IOException e) {
            baseUrl += "localhost:8080" + context + "/images/";
        }

        req.setAttribute("eventos", eventos);
        req.setAttribute("baseUrl", baseUrl);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}