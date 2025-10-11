<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx = request.getContextPath();
  String rol = (String) session.getAttribute("rol");
  String nick = (String) session.getAttribute("nick");
  boolean precargado = Boolean.TRUE.equals(application.getAttribute("datosPrecargados"));

  java.util.List<logica.Datatypes.DTEvento> eventos =
    (java.util.List<logica.Datatypes.DTEvento>) request.getAttribute("eventos");
%>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Eventos.uy</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/index.css">
  <link rel="stylesheet" href="<%=ctx%>/css/layoutAside.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>

<body>
  <!-- Header -->
    <jsp:include page="/WEB-INF/templates/header.jsp" />
<%--   <header class="site-header">
    <div class="container">
      <a class="brand" href="<%=ctx%>/inicio">Eventos.uy</a>

      <nav class="main-nav">
        <form class="search" action="<%=ctx%>/buscar" method="get" role="search" aria-label="Buscar">
          <input class="search-input" type="search" name="q" placeholder="Eventos, Ediciones">
          <button class="btn" type="submit">Buscar</button>
        </form>
      </nav>

      <nav class="user-nav" id="userNav">
        <% if (nick == null) { %>
          <a class="btn ghost" href="<%=ctx%>/auth/login"><i class='bx bxs-user'></i> Iniciar Sesión</a>
          <a class="btn" href="<%=ctx%>/usuario/AltaUsuario"><i class='bx bxs-user-plus'></i> Registrarse</a>
          <% if (!precargado) { %>
            <form action="<%=ctx%>/precargar" method="post" style="display:inline;">
              <button class="btn" type="submit">Precargar datos</button>
            </form>
          <% } else { %>
            <span>Datos precargados correctamente</span>
          <% } %>
        <% } else { %>
          <span class="user-chip"><i class='bx bxs-user'></i> <%= nick %></span>
          <a class="btn ghost" href="<%=ctx%>/auth/logout"><i class='bx bx-log-out'></i> Cerrar sesión</a>
        <% } %>
      </nav>
    </div>
  </header> --%>

  <div class="container row layout-inicio">
    <!-- Sidebar -->
    <aside class="card aside-inicio">
      <h3>Menú</h3>

      <% if ("ORGANIZADOR".equals(rol)) { %>
        <h4>Acciones</h4>
        <ul>
          <li><a href="<%=ctx%>/evento/alta">Alta Evento</a></li>
          <li><a href="<%=ctx%>/edicion/alta">Alta Edición Evento</a></li>
          <li><a href="<%=ctx%>/registro/alta">Alta Registro</a></li>
        </ul>
      <% } else if ("ASISTENTE".equals(rol)) { %>
        <h4>Acciones</h4>
        <ul>
          <li><a href="<%=ctx%>/registro/inscripcion">Registrarse a Edición de Evento</a></li>
        </ul>
      <% } %>

      <h4>Categorías</h4>
      <ul class="menu-categorias">
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

      <h4><a href="<%=ctx%>/usuario/ConsultaUsuario">Listar Usuarios</a></h4>
    </aside>

    <!-- Main -->
    <main class="main-inicio">
      <h1>Próximos eventos</h1>

      <div id="eventList" class="row">
        <%
          if (eventos != null && !eventos.isEmpty()) {
            for (logica.Datatypes.DTEvento e : eventos) {
        %>
          <div class="card">
            <h2><%= e.getNombre() %></h2>
            <p><strong>Descripción:</strong> <%= e.getDescripcion() %></p>
            <p><strong>Fecha:</strong> <%= e.getFecha() %></p>
            <div class="actions">
              <a href="<%=ctx%>/evento/ConsultaEvento?nombre=<%= java.net.URLEncoder.encode(e.getNombre(), "UTF-8") %>" class="btn">
                Ver más
              </a>
            </div>
          </div>
        <%
            }
          } else {
        %>
          <p>No hay eventos disponibles.</p>
        <%
          }
        %>
      </div>
    </main>
  </div>
</body>
</html>
