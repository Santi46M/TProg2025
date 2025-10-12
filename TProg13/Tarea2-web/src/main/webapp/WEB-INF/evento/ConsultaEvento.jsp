<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx  = request.getContextPath();
  String nick = (String) session.getAttribute("nick");

  String evNombre = (String) request.getAttribute("evNombre");
  String evSigla  = (String) request.getAttribute("evSigla");
  String evDesc   = (String) request.getAttribute("evDesc");
  String evFecha  = (String) request.getAttribute("evFecha");
  java.util.List<String> evCategorias = (java.util.List<String>) request.getAttribute("evCategorias");
  if (evCategorias == null) evCategorias = java.util.Collections.emptyList();
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta de Evento — <%= (evNombre != null ? evNombre : "Evento") %></title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaEventoBase.css">
  <link rel="stylesheet" href="<%=ctx%>/css/layoutMenu.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>
<body>

  <!-- Header -->
  <jsp:include page="/WEB-INF/templates/header.jsp" />

  <!-- Igual que el index -->
  <div class="container row layout-inicio" style="display:flex; align-items:flex-start;">
    
      <jsp:include page="/WEB-INF/templates/menu.jsp" />
      <!-- Contenido principal -->
      <main class="container consulta-evento-main" style="flex:2; min-width:0;">
        <section class="event-card">
          <h1 class="event-title"><%= (evNombre != null ? evNombre : "Evento") %></h1>

          <div class="event-info">
            <h3>Descripción</h3>
            <p><%= (evDesc != null ? evDesc : "—") %></p>

            <div class="event-meta"><strong>Sigla:</strong> <%= (evSigla != null ? evSigla : "—") %></div>

            <div class="chips">
              <span class="categorias-label">Categorías:</span>
              <% if (evCategorias.isEmpty()) { %>
                <span class="chip">—</span>
              <% } else { 
                   for (String c : evCategorias) { %>
                     <span class="chip"><%= c %></span>
              <%   }
                 } %>
            </div>

            <div class="event-meta"><strong>Fecha alta:</strong> <%= (evFecha != null ? evFecha : "—") %></div>
          </div>
        </section>
      </main>
      <aside class="editions" style="min-width:300px; flex:1; margin-left:2rem; align-self:flex-start;">
        <h3>Ediciones</h3>
        <%
        java.util.List<logica.datatypes.DTEdicion> ediciones = (java.util.List<logica.datatypes.DTEdicion>) request.getAttribute("evEdiciones");
                  if (ediciones == null || ediciones.isEmpty()) {
        %>
          <p>No hay ediciones asociadas a este evento.</p>
        <%
        } else {
        %>
          <ul class="ediciones-list">
          <%
          for (logica.datatypes.DTEdicion ed : ediciones) {
          %>
            <li>
              <strong><%= ed.getNombre() %></strong>
              <span>(<%= ed.getFechaInicio() %> - <%= ed.getFechaFin() %>)</span>
              <a class="btn" href="<%= ctx %>/edicion/ConsultaEdicion?evento=<%= java.net.URLEncoder.encode(evNombre, "UTF-8") %>&edicion=<%= java.net.URLEncoder.encode(ed.getNombre(), "UTF-8") %>">Ver detalles</a>
            </li>
          <% } %>
          </ul>
        <% } %>
      </aside>
    </div>

</body>
</html>