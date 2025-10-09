package web;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logica.fabrica;
import logica.Clases.Usuario;
import logica.Interfaces.IControladorUsuario;
import logica.Datatypes.DTDatosUsuario;

@WebServlet("/usuario/ConsultaUsuario")
public class ConsultaUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nick = request.getParameter("nick");
        IControladorUsuario ctrlUsuario = fabrica.getInstance().getIControladorUsuario();

        if (nick != null && !nick.isEmpty()) {
            // Caso 1: consulta individual
            try {
                DTDatosUsuario usuario = ctrlUsuario.obtenerDatosUsuario(nick);
                request.setAttribute("usuario", usuario);
            } catch (Exception e) {
                request.setAttribute("error", "Usuario no encontrado");
            }
        } else {
            // Caso 2: listar todos los usuarios
            Map<String, Usuario> usuarios = ctrlUsuario.listarUsuarios();

            // Debug para consola
            System.out.println("Usuarios encontrados: " + usuarios.size());
            for (String key : usuarios.keySet()) {
                System.out.println("- " + key);
            }

            // Solo dejamos una línea, con la colección de valores
            request.setAttribute("usuarios", usuarios.values());
        }

        request.getRequestDispatcher("/WEB-INF/usuario/ConsultaUsuario.jsp").forward(request, response);
    }
}