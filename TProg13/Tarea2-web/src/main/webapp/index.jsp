<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx = request.getContextPath();
  String nick = (String) session.getAttribute("nick");
  String rol  = (String) session.getAttribute("rol"); // "ASISTENTE" | "ORGANIZADOR" | null
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Eventos.uy</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <!-- Boxicons -->
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>

<body>
  <!-- Header -->
  <header class="site-header">
    <div class="container">
      <a class="brand" href="<%=ctx%>/"><i class='bx bxs-plane-alt'></i> Eventos.uy</a>

      <nav class="main-nav">
        <!-- Buscador -->
        <form class="search" action="<%=ctx%>/buscar" method="get" role="search" aria-label="Buscar">
          <input class="search-input" type="search" name="q" placeholder="Eventos, Ediciones">
          <button class="btn" type="submit">Buscar</button>
        </form>
      </nav>
      <!--  Zona de usuario -->
     <nav class="user-nav" id="userNav">


<% Boolean precargado = (Boolean) application.getAttribute("datosPrecargados"); %>

<% if (nick == null) { %>
    <a class="btn ghost" href="<%= ctx %>/auth/login">
      <i class='bx bxs-user'></i> Iniciar Sesión
    </a> 
    <a class="btn" href="<%= ctx %>/usuario/AltaUsuario">
      <i class='bx bxs-user-plus'></i> Registrarse
    </a>

    <% if (precargado == null || !precargado) { %>
      <form action="<%= ctx %>/precargar" method="post">
        <button type="submit">Precargar datos</button>
      </form>
    <% } else { %>
      <p>✅ Datos precargados correctamente</p>
    <% } %>

  <% } else { %>
    <!-- Usuario logueado -->
    <span class="user-chip">
      <i class='bx bxs-user-circle'></i> <%= nick %>
    </span>
    <a class="btn ghost" href="<%= ctx %>/auth/logout">
      <i class='bx bx-log-out'></i> Cerrar sesión
    </a>
  <% } %>
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
        <li><a href="#"><i class='bx bx-wifi-0'></i> Tecnología</a></li>
        <li><a href="#"><i class='bx bx-wifi-0'></i> Innovación</a></li>
        <li><a href="#"><i class='bx bx-wifi-0'></i> Literatura</a></li>
        <li><a href="#"><i class='bx bx-wifi-0'></i> Cultura</a></li>
        <li><a href="#"><i class='bx bx-wifi-0'></i> Música</a></li>
        <li><a href="#"><i class='bx bx-wifi-0'></i> Deporte</a></li>
        <li><a href="#"><i class='bx bx-wifi-0'></i> Salud</a></li>
        <li><a href="#"><i class='bx bx-wifi-0'></i> Entretenimiento</a></li>
        <li><a href="#"><i class='bx bx-wifi-0'></i> Negocios</a></li>
      </ul>
      <h4><a href="<%=ctx%>/usuario/listar">Listar Usuarios</a></h4>
      <div id="menuPorRol"></div>
    </aside>

    <!-- Main -->
    <main>
      <h1>Próximos eventos</h1>

      <div id="eventList" class="row">
        <!-- EV01: Conferencia de Tecnología -->
        <div class="card">
          <h2>Conferencia de Tecnología</h2>
          <p><strong>Ciudad:</strong> Punta del Este (Uruguay)</p>
          <div class="actions">
            <a href="<%=ctx%>/evento?id=EV01" class="btn">Ver más</a>
          </div>
        </div>

        <!-- EV04: Maratón de Montevideo -->
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

  <!-- Scripts -->
  <script src="<%=ctx%>/js/Login.js"></script>
  <script>
    const sidebar = document.getElementById('sidebar');
    if (sidebar) sidebar.style.display = 'none';
  </script>
</body>
</html>