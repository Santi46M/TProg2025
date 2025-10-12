<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="logica.Clases.Usuario" %>
<%@ page import="logica.Datatypes.*" %>

<%
  String ctx = request.getContextPath();
  String nickSesion = (String) session.getAttribute("nick");
  String rolSesion  = (String) session.getAttribute("rol");

  Collection<Usuario> usuarios = (Collection<Usuario>) request.getAttribute("usuarios");
  DTDatosUsuario usuario = (DTDatosUsuario) request.getAttribute("usuario");
  Map<String, String> edicionToEvento = (Map<String, String>) request.getAttribute("edicionToEvento");
  String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta de Usuarios — Eventos.uy</title>
  <link rel="stylesheet" href="<%= ctx %>/css/style.css">
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
              <% 
                for (DTEdicion e : usuario.getEdiciones()) { 
                  String estado = (e.getEstado() != null) ? e.getEstado().toString() : "Sin estado";
                  boolean mostrar = "Aceptada".equalsIgnoreCase(estado) || 
                                    (esSuPropioPerfil && ("Ingresada".equalsIgnoreCase(estado) || "Rechazada".equalsIgnoreCase(estado)));
                  if (mostrar) {
                    String eventoNombre = (edicionToEvento != null) ? edicionToEvento.get(e.getNombre()) : "";
              %>
                <li>
                  <strong><%= e.getNombre() %></strong>
                  <span>(<%= e.getFechaInicio() %> - <%= e.getFechaFin() %>)</span>
                  <a class="btn" href="<%= ctx %>/edicion/ConsultaEdicion?evento=<%= java.net.URLEncoder.encode(eventoNombre, "UTF-8") %>&edicion=<%= java.net.URLEncoder.encode(e.getNombre(), "UTF-8") %>">Ver detalles</a>
                  <em class="estado"> — <%= estado %></em>
                </li>
              <% 
                  } 
                } 
              %>
            </ul>
          <% } %>

          <% if (esSuPropioPerfil && esAsist) { %>
            <h2>Eventos registrados</h2>
            <ul class="lista-eventos">
              <% for (DTRegistro r : usuario.getRegistros()) { %>
                <li>
                  <strong><%= r.getEdicion() %></strong>
                  <span>| <strong>Tipo:</strong> <%= r.getTipoRegistro() %></span>
                  <span>| <strong>Fecha registro:</strong> <%= r.getFechaRegistro() %></span>
                  <span>| <strong>Costo:</strong> $<%= r.getCosto() %></span>
                  <a class="btn" href="<%= ctx %>/registro/ConsultaTipoRegistro?edicion=<%= java.net.URLEncoder.encode(r.getEdicion(), "UTF-8") %>&tipoRegistro=<%= java.net.URLEncoder.encode(r.getTipoRegistro(), "UTF-8") %>">Ver detalles</a>
                </li>
              <% } %>
            </ul>
          <% } %>
        </div>
      <% } %>
    </main>
  </div>
</body>
</html>