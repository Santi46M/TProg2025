<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx = request.getContextPath();
  String nick = (String) session.getAttribute("nick");
  String rol  = (String) session.getAttribute("rol");
  Boolean precargado = (Boolean) application.getAttribute("datosPrecargados");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Eventos.uy</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/index.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>

<body>
  <!-- Header -->
  <header class="site-header">
    <div class="container">
      <a class="brand" href="<%=ctx%>/">Eventos.uy</a>

      <nav class="main-nav">
        <form class="search" action="<%=ctx%>/buscar" method="get" role="search" aria-label="Buscar">
          <input class="search-input" type="search" name="q" placeholder="Eventos, Ediciones">
          <button class="btn" type="submit">Buscar</button>
        </form>
      </nav>

      <nav class="user-nav" id="userNav">
        <% if (nick == null) { %>
          <a class="btn ghost" href="<%= ctx %>/auth/login">
            Iniciar Sesión
          </a> 
          <a class="btn" href="<%= ctx %>/usuario/AltaUsuario">
            Registrarse
          </a>

          <% if (precargado == null || !precargado) { %>
            <form action="<%= ctx %>/precargar" method="post">
              <button type="submit">Precargar datos</button>
            </form>
          <% } else { %>
            <p>Datos precargados correctamente</p>
          <% } %>

        <% } else { %>
          <span class="user-chip">
            <%= nick %>
          </span>
          <a class="btn ghost" href="<%= ctx %>/auth/logout">
            Cerrar sesión
          </a>
        <% } %>
      </nav>
    </div>
  </header>

  <div class="container row layout-inicio">
    
    <!-- Sidebar -->
    <aside class="card aside-inicio">
      <h3>Menú</h3>
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
      <div id="menuPorRol"></div>
    </aside>

    <!-- Main -->
    <main class="main-inicio">
      <h1>Próximos eventos</h1>

      <div id="eventList" class="row">
        <div class="card">
          <h2>Conferencia de Tecnología</h2>
          <p><strong>Ciudad:</strong> Punta del Este (Uruguay)</p>
          <div class="actions">
            <a href="<%=ctx%>/evento?id=EV01" class="btn">Ver más</a>
          </div>
        </div>

        <div class="card">
          <img src="<%=ctx%>/img/IMG-EV04.png" alt="Maratón de Montevideo" style="width:100%;">
          <h2>Maratón de Montevideo</h2>
          <p><strong>Ciudad:</strong> Montevideo (Uruguay)</p>
          <div class="actions">
            <a href="<%=ctx%>/evento?id=EV04" class="btn">Ver más</a>
          </div>
        </div>
      </div>
    </main>
  </div>

  <script src="<%=ctx%>/js/Login.js"></script>
</body>
</html>