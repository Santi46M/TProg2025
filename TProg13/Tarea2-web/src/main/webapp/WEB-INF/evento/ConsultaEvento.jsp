<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx  = request.getContextPath();
  String nick = (String) session.getAttribute("nick");

  String evNombre = (String) request.getAttribute("evNombre");
  String evSigla  = (String) request.getAttribute("evSigla");
  String evDesc   = (String) request.getAttribute("evDesc");
  String evFecha  = (String) request.getAttribute("evFecha");
  java.util.List<String> evCategorias =
      (java.util.List<String>) request.getAttribute("evCategorias");
  if (evCategorias == null) evCategorias = java.util.Collections.emptyList();

  // URL de imagen ya resuelta por el servlet (si existe)
  String evImagenUrl = (String) request.getAttribute("evImagenUrl");
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
  <style>
    .event-hero { display:flex; gap:1rem; align-items:flex-start; margin-bottom:1rem; }
    .event-hero__img { width:360px; max-width:40vw; aspect-ratio:16/9; background:#f3f4f6; border-radius:12px; overflow:hidden; flex-shrink:0; display:flex; align-items:center; justify-content:center; }
    .event-hero__img img { width:100%; height:100%; object-fit:cover; display:block; }
    .event-hero__placeholder { color:#6b7280; font-size:.95rem; }
    .chips { display:flex; flex-wrap:wrap; gap:.4rem; }
    .chip { background:#eef2ff; color:#3730a3; padding:.2rem .5rem; border-radius:999px; font-size:.9rem; }
  </style>
</head>
<body>

  <!-- Header -->
  <jsp:include page="/WEB-INF/templates/header.jsp" />

  <div class="container row layout-inicio" style="display:flex; align-items:flex-start;">
    <jsp:include page="/WEB-INF/templates/menu.jsp" />

    <!-- Contenido principal -->
    <main class="container consulta-evento-main" style="flex:2; min-width:0;">
      <section class="event-card">
        <div class="event-hero">
          <div class="event-hero__img">
            <% if (evImagenUrl != null && !evImagenUrl.isBlank()) { %>
              <img src="<%= evImagenUrl %>" alt="Imagen de <%= (evNombre != null ? evNombre : "Evento") %>">
            <% } else { %>
              <div class="event-hero__placeholder">Sin imagen</div>
            <% } %>
          </div>

          <div class="event-hero__meta">
            <h1 class="event-title"><%= (evNombre != null ? evNombre : "Evento") %></h1>

            <div class="event-info">
              <h3>Descripción</h3>
              <p><%= (evDesc != null ? evDesc : "—") %></p>

              <div class="event-meta"><strong>Sigla:</strong> <%= (evSigla != null ? evSigla : "—") %></div>

              <div class="chips" style="margin:.5rem 0 0;">
                <span class="categorias-label" style="margin-right:.3rem;"><strong>Categorías:</strong></span>
                <% if (evCategorias.isEmpty()) { %>
                  <span class="chip">—</span>
                <% } else { 
                     for (String c : evCategorias) { %>
                       <span class="chip"><%= c %></span>
                <%   }
                   } %>
              </div>

              <div class="event-meta" style="margin-top:.5rem;"><strong>Fecha alta:</strong> <%= (evFecha != null ? evFecha : "—") %></div>
            </div>
          </div>
        </div>
      </section>
    </main>

    <aside class="editions" style="min-width:300px; flex:1; margin-left:2rem; align-self:flex-start;">
      <h3>Ediciones</h3>
      <%
        java.util.List<logica.datatypes.DTEdicion> ediciones =
          (java.util.List<logica.datatypes.DTEdicion>) request.getAttribute("evEdiciones");
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
            <form action="<%= ctx %>/edicion/ConsultaEdicion" method="get" style="display:inline;">
              <input type="hidden" name="evento" value="<%= evNombre %>" />
              <input type="hidden" name="edicion" value="<%= ed.getNombre() %>" />
              <button type="submit" class="btn btn-ver-detalles" style="margin-left:0.5rem;">Ver detalles</button>
            </form>
          </li>
        <% } %>
        </ul>
      <% } %>
    </aside>
  </div>

</body>
</html>