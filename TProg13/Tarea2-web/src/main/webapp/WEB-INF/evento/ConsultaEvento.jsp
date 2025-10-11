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
  <div class="container row layout-inicio">
    
      <jsp:include page="/WEB-INF/templates/menu.jsp" />

    <!-- Contenido principal -->
    <main class="container consulta-evento-main">
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

      <aside class="card editions">
        <h3>Ediciones</h3>
        <p>(Próximamente)</p>
      </aside>
    </main>
  </div>

</body>
</html>
