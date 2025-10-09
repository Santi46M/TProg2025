<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Eventos.uy</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/index.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>

<body>
  <!-- Header -->
  <header class="site-header">
    <div class="container">
      <a class="brand" href="<%= request.getContextPath() %>/">Eventos.uy</a>

      <nav class="main-nav">
        <form class="search" action="<%= request.getContextPath() %>/buscar" method="get" role="search" aria-label="Buscar">
          <input class="search-input" type="search" name="q" placeholder="Eventos, Ediciones">
          <button class="btn" type="submit">Buscar</button>
        </form>
      </nav>

      <nav class="user-nav" id="userNav">
        <%
          String nick = (String) session.getAttribute("nick");
          boolean precargado = Boolean.TRUE.equals(application.getAttribute("datosPrecargados"));
          if (nick == null) {
        %>
          <a class="btn ghost" href="<%= request.getContextPath() %>/auth/login">
            <i class='bx bxs-user'></i> Iniciar Sesión
          </a>
          <a class="btn" href="<%= request.getContextPath() %>/usuario/AltaUsuario">
            <i class='bx bxs-user-plus'></i> Registrarse
          </a>

          <% if (!precargado) { %>
            <form action="<%= request.getContextPath() %>/precargar" method="post" style="display:inline;">
              <button class="btn" type="submit">Precargar datos</button>
            </form>
          <% } else { %>
            <span>Datos precargados correctamente</span>
          <% } %>

        <% } else { %>
          <span class="user-chip"><i class='bx bxs-user'></i> <%= nick %></span>
          <a class="btn ghost" href="<%= request.getContextPath() %>/auth/logout">
            <i class='bx bx-log-out'></i> Cerrar sesión
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

      <h4><a href="<%= request.getContextPath() %>/usuario/listar">Listar Usuarios</a></h4>
    </aside>

    <!-- Main -->
    <main class="main-inicio">
      <h1>Próximos eventos</h1>

      <div id="eventList" class="row">
        <div class="card">
          <h2>Conferencia de Tecnología</h2>
          <p><strong>Ciudad:</strong> Punta del Este (Uruguay)</p>
          <div class="actions">
            <!-- Por nombre (URL-encoded): Conferencia%20de%20Tecnolog%C3%ADa -->
            <a href="<%= request.getContextPath() %>/evento/ConsultaEvento?nombre=Conferencia%20de%20Tecnolog%C3%ADa" class="btn">Ver más</a>
          </div>
        </div>

        <div class="card">
          <img src="<%= request.getContextPath() %>/img/IMG-EV04.png" alt="Maratón de Montevideo" style="width:100%;">
          <h2>Maratón de Montevideo</h2>
          <p><strong>Ciudad:</strong> Montevideo (Uruguay)</p>
          <div class="actions">
            <!-- Maratón de Montevideo → Marat%C3%B3n%20de%20Montevideo -->
            <a href="<%= request.getContextPath() %>/evento/ConsultaEvento?nombre=Marat%C3%B3n%20de%20Montevideo" class="btn">Ver más</a>
          </div>
        </div>
      </div>
    </main>
  </div>

  <script src="<%= request.getContextPath() %>/js/Login.js"></script>
</body>
</html>
