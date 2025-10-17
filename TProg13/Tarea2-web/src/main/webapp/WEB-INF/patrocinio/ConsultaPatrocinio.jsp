<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx = request.getContextPath();
  String nick = (String) session.getAttribute("nick");
  logica.datatypes.DTPatrocinio patrocinio = (logica.datatypes.DTPatrocinio) request.getAttribute("patrocinio");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta Patrocinio — <%= (patrocinio != null ? patrocinio.getCodigo() : "Patrocinio") %></title>
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
        <h1 class="event-title">Patrocinio: <%= (patrocinio != null ? patrocinio.getCodigo() : "Patrocinio") %></h1>
      </div>
      <div class="event-info">
        <% if (patrocinio != null) { %>
          <div class="event-meta"><strong>Institución:</strong> <%= patrocinio.getInstitucion() %></div>
          <div class="event-meta"><strong>Nivel:</strong> <%= String.valueOf(patrocinio.getNivel()) %></div>
          <div class="event-meta"><strong>Tipo de Registro:</strong> <%= patrocinio.getTipoRegistro() %></div>
          <div class="event-meta"><strong>Aporte:</strong> <%= patrocinio.getMonto() %></div>
          <div class="event-meta"><strong>Fecha de Patrocinio:</strong> <%= patrocinio.getFecha() %></div>
          <div class="event-meta"><strong>Cantidad de Registros:</strong> <%= patrocinio.getCantRegistrosGratuitos() %></div>
          <div class="event-meta"><strong>Código de Patrocinio:</strong> <%= patrocinio.getCodigo() %></div>
          <% if (patrocinio.getSiglaEdicion() != null) { %>
            <div class="event-meta"><strong>Edición asociada:</strong> <%= patrocinio.getSiglaEdicion() %></div>
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
