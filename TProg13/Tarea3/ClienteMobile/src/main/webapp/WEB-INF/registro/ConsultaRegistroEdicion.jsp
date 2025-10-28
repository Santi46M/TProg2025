<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*, publicadores.DtRegistro" %>
<%
  String ctx = request.getContextPath();
  String nickSesion = (String) session.getAttribute("nick");
  DtRegistro registro = (DtRegistro) request.getAttribute("registro");
  String error = (String) request.getAttribute("error");
  String mensaje = (String) request.getAttribute("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta Registro Edición — Eventos.uy</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaRegistro.css">
</head>
<body>
  <jsp:include page="/WEB-INF/templates/header.jsp" />

  <div class="container row" style="margin-top:1rem; display: flex; align-items: flex-start;">

    <main class="container consulta-layout" style="flex:2; min-width:0;">
      <section class="event-card">
        <div class="event-header">
          <h1 class="event-title">Consulta de Registro en Edición</h1>
        </div>

        <div class="event-info">
          <% if (error != null) { %>
            <p class="error"><%= error %></p>
          <% } else if (registro != null) { %>
            <%-- <div class="event-meta"><strong>Identificador:</strong> <%= registro.getIdentificador() %></div> --%>
            <div class="event-meta"><strong>Usuario:</strong> <%= registro.getUsuario() %></div>
            <div class="event-meta"><strong>Edición:</strong> <%= registro.getEdicion() %></div>
            <div class="event-meta"><strong>Tipo de registro:</strong> <%= registro.getTipoRegistro() %></div>
            <div class="event-meta"><strong>Fecha de registro:</strong> <%= registro.getFechaRegistro() %></div>
            <div class="event-meta"><strong>Costo:</strong> $<%= registro.getCosto() %></div>
            <div class="event-meta"><strong>Fecha de inicio:</strong> <%= registro.getFechaInicio() %></div>
            <% if (mensaje != null) { %>
              <p class="success"><%= mensaje %></p>
            <% } else { %>
              <form action="<%= ctx %>/registro/ConsultaRegistroEdicion" method="post" style="margin-top:1rem;">
                <input type="hidden" name="usuario" value="<%= nickSesion %>" />
                <input type="hidden" name="edicion" value="<%= registro.getEdicion() %>" />
                <input type="hidden" name="registroId" value="<%= registro.getIdentificador() %>" />
                <button type="submit" class="btn btn-success">Confirmar asistencia</button>
              </form>
            <% } %>
          <% } else { %>
            <p>No se encontró información del registro.</p>
          <% } %>
        </div>
      </section>
    </main>
  </div>
</body>
</html>