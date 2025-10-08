<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="logica.Clases.Usuario" %>
<%
  String ctx   = request.getContextPath();
  String error = (String) request.getAttribute("error"); // mensaje del servlet si falló el login
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Consulta de Usuarios - Asistente</title>
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
<header class="site-header">
    <div class="container">
        <a class="brand" href="index.jsp"><i class='bx bxs-plane-alt'></i> Eventos.uy</a>

        <nav class="main-nav">
            <!-- Buscador -->
            <form class="search" action="/buscar" method="get" role="search" aria-label="Buscar">
                <input class="search-input" type="search" name="q" placeholder="Eventos, Ediciones">
                <button class="btn" type="submit">Buscar</button>
            </form>
        </nav>

        <!-- Zona de usuario -->
        <nav class="user-nav" id="userNav">
            <a class="btn ghost" href="InicioSesion.jsp"><i class='bx bxs-user'></i> Iniciar Sesión</a>
            <a class="btn" href="AltaUsuario.jsp"><i class='bx bxs-user-plus'></i> Registrarse</a>
        </nav>
    </div>
</header>

<!-- Layout -->
<div class="container row" style="margin-top:1rem;">
    <!-- Sidebar -->
    <aside class="card">
        <h3><i class='bx bx-list-ul'></i> Menú</h3>

        <h4>Categorías</h4>
        <ul>
            <li><a href="#">Tecnología</a></li>
            <li><a href="#">Innovación</a></li>
            <li><a href="#">Literatura</a></li>
            <li><a href="#">Cultura</a></li>
            <li><a href="#">Música</a></li>
            <li><a href="#">Deporte</a></li>
            <li><a href="#">Salud</a></li>
            <li><a href="#">Entretenimiento</a></li>
            <li><a href="#">Negocios</a></li>
        </ul>
        <h4><a href="ConsultaUsuario.jsp">Listar Usuarios</a></h4>
    </aside>

    <!-- Main -->
    <main>
        <h1>Usuarios</h1>
        <div id="usuariosList" class="row">

            <% 
                // Supongamos que el Servlet pasó una lista de usuarios al request
                List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
                if (usuarios != null && !usuarios.isEmpty()) {
                    for (Usuario u : usuarios) {
            %>
<%--                         <div class="card">
                            <img src="<%= request.getContextPath() + "/img/" + (u.getImagen() != null ? u.getImagen() : "default-user.png") %>" 
                                 alt="<%= u.getNombre() %>" style="width:100%">
                            <h2><%= u.getNombre() %> <%= (u.getApellido() != null ? u.getApellido() : "") %></h2>
                            <p><strong>Nickname:</strong> <%= u.getNick() %></p>
                            <p><strong>Rol:</strong> <%= u.getRol() %></p>
                            <div class="actions">
                                <a href="ConsultaUsuarioDetalle.jsp?nick=<%= u.getNick() %>" class="btn">Ver más</a>
                            </div>
                        </div> --%>
            <% 
                    }
                } else { 
            %>
                <p>No hay usuarios registrados.</p>
            <% 
                } 
            %>

        </div>
    </main>
</div>

</body>
</html>
