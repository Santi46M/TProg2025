<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Collection" %>
<%@ page import="logica.Clases.Usuario" %>
<%@ page import="logica.Datatypes.DTDatosUsuario" %>
<%@ page import="logica.Datatypes.DTEdicion" %>
<%@ page import="logica.Datatypes.DTRegistro" %>

<%
  String ctx = request.getContextPath();
  String nickSesion = (String) session.getAttribute("nick");
  String rolSesion  = (String) session.getAttribute("rol");

  Collection<Usuario> usuarios = (Collection<Usuario>) request.getAttribute("usuarios");
  DTDatosUsuario usuario = (DTDatosUsuario) request.getAttribute("usuario");
  String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta de Usuarios — Eventos.uy</title>
  <link rel="stylesheet" href="<%= ctx %>/css/style.css">
<%--   <link rel="stylesheet" href="<%= ctx %>/css/index.css"> --%>
<link rel="stylesheet" href="<%= ctx %>/css/ConsultaUsuario.css">
  <link rel="stylesheet" href="<%=ctx%>/css/layoutMenu.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>

<body>
<!-- Header -->
<jsp:include page="/WEB-INF/templates/header.jsp" />

<!-- Layout -->
<div class="container row layout-inicio">
  <!-- Sidebar -->

	<jsp:include page="/WEB-INF/templates/menu.jsp" />

  

  <!-- Main -->
  <main class="main-inicio">
    <% if (error != null) { %>
      <p class="error"><%= error %></p>
    <% } %>

    <% if (usuario == null) { %>
      <h1>Usuarios registrados</h1>
      <div class="usuarios-grid">
        <% if (usuarios == null || usuarios.isEmpty()) { %>
          <p>No hay usuarios registrados.</p>
        <% } else { %>
          <% for (Usuario u : usuarios) { 
               boolean esOrg = (u instanceof logica.Clases.Organizador);
               boolean esAsist = (u instanceof logica.Clases.Asistente);
          %>
            <div class="card usuario-card">
              <h3>
                <a href="<%=ctx%>/usuario/ConsultaUsuario?nick=<%=u.getNickname()%>">
                  <i class='bx <%= esOrg ? "bxs-microphone" : "bxs-id-card" %>'></i> <%=u.getNickname()%>
                </a>
              </h3>
              <p><strong>Nombre:</strong> <%=u.getNombre()%></p>
              <p><strong>Email:</strong> <%=u.getEmail()%></p>
              <% if (esOrg) { %>
                <span class="rol-tag org">Organizador</span>
              <% } else if (esAsist) { %>
                <span class="rol-tag asist">Asistente</span>
              <% } %>
            </div>
          <% } %>
        <% } %>
      </div>

    <% } else { %>
      <h1>Perfil de <%= usuario.getNickname() %></h1>
      <div class="usuario-detalle">
        <p><strong>Nombre:</strong> <%= usuario.getNombre() %></p>
        <p><strong>Email:</strong> <%= usuario.getEmail() %></p>
        <% if (usuario.getInstitucion() != null) { %>
          <p><strong>Institución:</strong> <%= usuario.getInstitucion() %></p>
        <% } %>
        <% if (usuario.getDesc() != null) { %>
          <p><strong>Descripción:</strong> <%= usuario.getDesc() %></p>
        <% } %>

        <% 
          boolean esSuPropioPerfil = nickSesion != null && nickSesion.equals(usuario.getNickname());
          boolean esOrg = usuario.getEdiciones() != null && !usuario.getEdiciones().isEmpty();
          boolean esAsist = usuario.getRegistros() != null && !usuario.getRegistros().isEmpty();
        %>

        <% if (esOrg) { %>
          <h2>Ediciones de eventos</h2>
          <ul class="lista-ediciones">
            <% for (DTEdicion e : usuario.getEdiciones()) { 
                String estado = e.getEstado().toString();
                 boolean mostrar = "Aceptada".equalsIgnoreCase(estado) || 
                                  (esSuPropioPerfil && ("Ingresada".equalsIgnoreCase(estado) || "Rechazada".equalsIgnoreCase(estado)));
                 if (mostrar) {
            %>
              <li class="estado-<%= estado.toLowerCase() %>">
                <strong><%= e.getNombre() %></strong> 
                (<%= e.getFechaInicio() %> → <%= e.getFechaFin() %>) — 
                <span><%= e.getCiudad() %>, <%= e.getPais() %></span> 
                <em class="estado"><%= estado %></em>
              </li>
            <% } } %>
          </ul>
        <% } %>

        <% if (esAsist && esSuPropioPerfil) { %>
          <h2>Mis registros</h2>
          <ul class="lista-registros">
            <% for (DTRegistro r : usuario.getRegistros()) { %>
              <li><%= r.getEdicion() %> — <%= r.getTipoRegistro() %></li>
            <% } %>
          </ul>
        <% } %>

        <a href="<%=ctx%>/usuario/ConsultaUsuario" class="btn">Volver al listado</a>
      </div>
    <% } %>
  </main>
</div>

</body>
</html>

