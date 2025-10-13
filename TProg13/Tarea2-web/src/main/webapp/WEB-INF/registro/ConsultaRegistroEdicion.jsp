<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
String ctx = request.getContextPath();
String nick = (String) session.getAttribute("nick");
logica.clases.Registro registro = (logica.clases.Registro) request.getAttribute("registro");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta Registro Edición</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaRegistro.css">
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp" />
<div class="container row" style="margin-top:1rem; display: flex; align-items: flex-start;">
  <jsp:include page="/WEB-INF/templates/menu.jsp" />
  <main class="container consulta-layout" style="flex:2; min-width:0;">
    <section class="event-card">
      <div class="event-header">
        <h1 class="event-title">Consulta de Registro en Edición</h1>
      </div>
      <div class="event-info">
        <% if (registro != null) { %>
          <div class="event-meta"><strong>Identificador:</strong> <%= registro.getId() %></div>
          <% if (registro.getUsuario() instanceof logica.clases.Asistente) { %>
            <div class="event-meta"><strong>Usuario:</strong> <%= registro.getUsuario().getNickname() %></div>
          <% } %>
          <div class="event-meta"><strong>Edición:</strong> <%= registro.getEdicion().getNombre() %></div>
          <div class="event-meta"><strong>Tipo de registro:</strong> <%= registro.getTipoRegistro().getNombre() %></div>
          <div class="event-meta"><strong>Fecha de registro:</strong> <%= registro.getFechaRegistro() %></div>
          <div class="event-meta"><strong>Costo:</strong> <%= registro.getCosto() %></div>
          <div class="event-meta"><strong>Fecha de inicio:</strong> <%= registro.getFechaInicio() %></div>
        <% } else { %>
          <p>No se encontró información del registro.</p>
        <% } %>
      </div>
    </section>
  </main>
</div>
</body>
</html>