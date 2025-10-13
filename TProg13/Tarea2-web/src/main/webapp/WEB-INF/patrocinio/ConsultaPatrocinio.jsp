<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
String ctx = request.getContextPath();
  String nick = (String) session.getAttribute("nick");
  logica.clases.Patrocinio patrocinio = (logica.clases.Patrocinio) request.getAttribute("patrocinio");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta Patrocinio — <%= (patrocinio != null ? patrocinio.getCodigoPatrocinio() : "Patrocinio") %></title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaPatrocinio.css">
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp" />
<div class="container row layout-inicio">
  <jsp:include page="/WEB-INF/templates/menu.jsp" />
  <main class="main-inicio">
    <section class="event-card">
      <div class="event-header">
        <h1 class="event-title">Patrocinio: <%= (patrocinio != null ? patrocinio.getCodigoPatrocinio() : "Patrocinio") %></h1>
      </div>
      <div class="event-info">
        <% if (patrocinio != null) { %>
          <div class="event-meta"><strong>Institución:</strong> <%= patrocinio.getInstitucion().getNombre() %></div>
          <div class="event-meta"><strong>Nivel:</strong> <%= patrocinio.getNivel() %></div>
          <div class="event-meta"><strong>Tipo de Registro:</strong> <%= patrocinio.getTipoRegistro().getNombre() %></div>
          <div class="event-meta"><strong>Aporte:</strong> <%= patrocinio.getAporte() %></div>
          <div class="event-meta"><strong>Fecha de Patrocinio:</strong> <%= patrocinio.getFechaPatrocinio() %></div>
          <div class="event-meta"><strong>Cantidad de Registros:</strong> <%= patrocinio.getCantidadRegistros() %></div>
          <div class="event-meta"><strong>Código de Patrocinio:</strong> <%= patrocinio.getCodigoPatrocinio() %></div>
          <% if (patrocinio.getEdicion() != null) { %>
            <div class="event-meta"><strong>Edición asociada:</strong> <%= patrocinio.getEdicion().getNombre() %></div>
          <% } %>
        <% } else { %>
          <p>No se encontró información del patrocinio.</p>
        <% } %>
      </div>
    </section>
  </main>
</div>
</body>
</html>