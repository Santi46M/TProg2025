<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.net.URLEncoder, java.nio.charset.StandardCharsets" %>
<%
String ctx = request.getContextPath();

  java.util.List<logica.datatypes.DTEvento> lista =
      (java.util.List<logica.datatypes.DTEvento>) request.getAttribute("lista");

  java.util.List<String> categorias =
      (java.util.List<String>) request.getAttribute("categorias");

  String catSel = (String) request.getAttribute("categoriaSeleccionada");
%>

<!-- CSS global -->
<link rel="stylesheet" href="<%=ctx%>/css/style.css">
<!-- CSS específico de listado -->
<link rel="stylesheet" href="<%=ctx%>/css/listado.css">

<jsp:include page="/WEB-INF/templates/header.jsp"/>

<div class="container">
  <div class="page-list">
    <!-- Aside -->
    <jsp:include page="/WEB-INF/templates/menu.jsp"/>

    <!-- Contenido -->
    <main class="content">
      <h1 class="list-title"><%= (catSel==null||catSel.isBlank()) ? "Todos los eventos" : "Eventos en " + catSel %></h1>
      <p class="list-sub">
        <%
          int n = (lista == null) ? 0 : lista.size();
          out.print(n + (n==1 ? " resultado" : " resultados"));
        %>
      </p>

      <div class="cards-grid">
        <% if (lista != null) for (var ev : lista) {
             String nombre = ev.getNombre();
             String sigla  = ev.getSigla();
             String desc   = (ev.getDescripcion() == null ? "" : ev.getDescripcion());
             java.util.List<String> evCats = ev.getCategorias();
             java.util.List<String> evEds  = ev.getEdiciones();
        %>
          <article class="card event-card list">
            <%
            String img = ev.getImagen();
            if (img != null && !img.isBlank()) { %>
              <img class="event-cover" src="<%=ctx%>/img/<%=img%>" alt="Imagen de <%=nombre%>">
            <% }%>

            <h3 class="event-title"><%= nombre %></h3>
            <p class="event-sub"><%= (sigla==null||sigla.isBlank()) ? "—" : sigla %></p>

            <% if (desc != null && !desc.isBlank()) { %>
              <p class="event-desc"><%= desc %></p>
            <% } %>

            <% if (evCats != null && !evCats.isEmpty()) { %>
              <div class="chips" aria-label="Categorías">
                <% for (String c : evCats) { %>
                  <span class="chip"><%= c %></span>
                <% } %>
              </div>
            <% } %>

            <% if (evEds != null && !evEds.isEmpty()) { %>
              <div class="chips" aria-label="Ediciones">
                <% for (String ed : evEds) { %>
                  <span class="chip"><%= ed %></span>
                <% } %>
              </div>
            <% } %>

            <div class="event-footer">
              <span class="event-meta"><!-- espacio para fecha/meta si querés --></span>
              <a class="btn"
                 href="<%=ctx%>/evento/ConsultaEvento?nombre=<%= URLEncoder.encode(nombre, StandardCharsets.UTF_8.name()) %>">
                Ver detalle
              </a>
            </div>
          </article>
        <% } %>
      </div>
    </main>
  </div>
</div>
