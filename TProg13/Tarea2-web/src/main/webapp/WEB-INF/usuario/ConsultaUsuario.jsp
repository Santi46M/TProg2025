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
              <div class="card usuario-card">
                <h3>
                  <!-- Podés dejar <a> o cambiar a <form> si preferís -->
                  <a href="<%=ctx%>/usuario/ConsultaUsuario?nick=<%=u.getNickname()%>">
                    <i class='bx <%= esOrg ? "bxs-microphone" : "bxs-id-card" %>'></i>
                    <%=u.getNickname()%>
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

      <!-- ====== PERFIL INDIVIDUAL ====== -->
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
  boolean esMiPerfil = (nickSesion != null && usuario != null && nickSesion.equals(usuario.getNickname()));
  boolean tieneEdiciones = (usuario != null && usuario.getEdiciones() != null && !usuario.getEdiciones().isEmpty());
  @SuppressWarnings("unchecked")
  java.util.Map<String,String> edicionToEvento = (java.util.Map<String,String>) request.getAttribute("edicionToEvento");
%>

<% if (tieneEdiciones) { %>
  <h2>Ediciones de eventos</h2>
  <ul class="lista-ediciones">
    <% for (DTEdicion e : usuario.getEdiciones()) {
         String estado = e.getEstado();
         estado = (estado == null) ? "" : estado.trim();
         boolean aceptada = "ACEPTADA".equalsIgnoreCase(estado);
         boolean mostrar = esMiPerfil || aceptada; // propio: todas; terceros/visit: solo aceptadas
         if (!mostrar) continue;

         String liClass = "estado-" + (estado.isEmpty() ? "sin-estado" : estado.toLowerCase());
         String edNombre = e.getNombre();
         String evNombre = (edicionToEvento == null) ? null : edicionToEvento.get(edNombre);
    %>
      <li class="<%= liClass %>">
        <% if (aceptada) { %>
          <!-- Aceptadas: ver detalle (GET) -->
          <form action="<%= ctx %>/edicion/ConsultaEdicionDeEvento" method="get" style="display:inline;">
            <%-- Si tu endpoint de consulta también requiere 'evento', lo mandamos: --%>
            <% if (evNombre != null) { %>
              <input type="hidden" name="evento" value="<%= evNombre %>">
            <% } %>
            <input type="hidden" name="edicion" value="<%= edNombre %>">
            <button type="submit" class="linklike"><strong><%= edNombre %></strong></button>
          </form>
        <% } else { %>
          <!-- No aceptadas: solo texto -->
          <strong><%= edNombre %></strong>
        <% } %>

        (<%= e.getFechaInicio() %> → <%= e.getFechaFin() %>) —
        <span><%= e.getCiudad() %>, <%= e.getPais() %></span>
        <% if (!estado.isEmpty()) { %>
          <em class="estado"> — <%= estado %></em>
        <% } %>

        <% if (esMiPerfil) { %>
          <!-- Acciones de moderación SOLO en mi perfil -->
          <form action="<%= ctx %>/edicion/aceptar" method="get" style="display:inline; margin-left:.5rem;">
            <% if (evNombre != null) { %>
              <input type="hidden" name="evento" value="<%= evNombre %>">
            <% } %>
            <input type="hidden" name="edicion" value="<%= edNombre %>">
            <button type="submit" class="linklike">Aceptar</button>
          </form>
          <form action="<%= ctx %>/edicion/rechazar" method="get" style="display:inline; margin-left:.5rem;">
            <% if (evNombre != null) { %>
              <input type="hidden" name="evento" value="<%= evNombre %>">
            <% } %>
            <input type="hidden" name="edicion" value="<%= edNombre %>">
            <button type="submit" class="linklike">Rechazar</button>
          </form>
        <% } %>
      </li>
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
