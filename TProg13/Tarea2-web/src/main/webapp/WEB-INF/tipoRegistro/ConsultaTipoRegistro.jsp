<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx = request.getContextPath();
  String nick = (String) session.getAttribute("nick");
  logica.Clases.TipoRegistro tipoRegistro = (logica.Clases.TipoRegistro) request.getAttribute("tipoRegistro");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta Tipo de Registro — <%= (tipoRegistro != null ? tipoRegistro.getNombre() : "Tipo de Registro") %></title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaTipoRegistro.css">
</head>
<body>
<header class="site-header">
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
        <a class="btn ghost" href="<%=ctx%>/auth/login"><i class='bx bxs-user'></i> Iniciar sesión</a>
      <% } else { %>
        <span class="user-chip"><i class='bx bxs-user'></i> <%= nick %></span>
        <a class="btn ghost" href="<%=ctx%>/auth/logout"><i class='bx bx-log-out'></i> Cerrar sesión</a>
      <% } %>
    </nav>
  </div>
</header>
<div class="container row" style="margin-top:1rem;">
  <main class="container consulta-layout">
    <section class="event-card">
      <div class="event-header">
        <h1 class="event-title">Tipo de Registro: <%= (tipoRegistro != null ? tipoRegistro.getNombre() : "Tipo de Registro") %></h1>
      </div>
      <div class="event-info">
        <% if (tipoRegistro != null) { %>
          <div class="event-meta"><strong>Descripción:</strong> <%= tipoRegistro.getDescripcion() %></div>
          <div class="event-meta"><strong>Costo:</strong> <%= tipoRegistro.getCosto() %></div>
          <div class="event-meta"><strong>Cupo:</strong> <%= tipoRegistro.getCupo() %></div>
          <% if (tipoRegistro.getEdicion() != null) { %>
            <div class="event-meta"><strong>Edición asociada:</strong> <%= tipoRegistro.getEdicion().getNombre() %></div>
          <% } %>
        <% } else { %>
          <p>No se encontró información del tipo de registro.</p>
        <% } %>
      </div>
    </section>
  </main>
</div>
</body>
</html>
