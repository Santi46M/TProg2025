<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
String ctx  = request.getContextPath();
  String nick = (String) session.getAttribute("nick");
  String rol  = (String) session.getAttribute("rol");

  logica.clases.Ediciones edicion = (logica.clases.Ediciones) request.getAttribute("edicion");
  logica.clases.Usuario organizador = (logica.clases.Usuario) request.getAttribute("organizador");
  java.util.Collection tiposRegistro = (java.util.Collection) request.getAttribute("tiposRegistro");
  java.util.Collection patrocinios = (java.util.Collection) request.getAttribute("patrocinios");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta de Edición — <%=(edicion != null ? edicion.getNombre() : "Edición")%></title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaEventoBase.css">
</head>
<body>

<jsp:include page="/WEB-INF/templates/header.jsp" />

<div class="container row" style="margin-top:1rem; display: flex; align-items: flex-start;">
  <jsp:include page="/WEB-INF/templates/menu.jsp" />
  <main class="container consulta-layout" style="flex:2; min-width:0;">
    <section class="event-card">
      <div class="event-header">
        <h1 class="event-title"><%=(edicion != null ? edicion.getNombre() : "Edición")%></h1>
        <%
        if (edicion != null && edicion.getEvento() != null) {
        %>
          <div class="event-meta"><strong>Evento:</strong> <%=edicion.getEvento().getNombre()%></div>
        <%
        }
        %>
      </div>
      <div class="event-info">
        <h3>Datos de la Edición</h3>
        <%
        if (edicion != null) {
        %>
          <div class="event-meta"><strong>Sigla:</strong> <%=edicion.getSigla()%></div>
          <div class="event-meta"><strong>Fecha inicio:</strong> <%=edicion.getFechaInicio()%></div>
          <div class="event-meta"><strong>Fecha fin:</strong> <%=edicion.getFechaFin()%></div>
          <div class="event-meta"><strong>Fecha alta:</strong> <%=edicion.getFechaAlta()%></div>
          <div class="event-meta"><strong>Ciudad:</strong> <%=edicion.getCiudad()%></div>
          <div class="event-meta"><strong>País:</strong> <%=edicion.getPais()%></div>
          <div class="event-meta"><strong>Estado:</strong> <%=edicion.getEstado()%></div>
          <%
          if (edicion.getImagen() != null && !edicion.getImagen().isEmpty()) {
          %>
            <div class="event-meta"><strong>Imagen:</strong><br><img src="<%=edicion.getImagen()%>" alt="Imagen de la edición" style="max-width:300px;max-height:200px;"></div>
          <%
          }
          %>
        <%
        }
        %>
      </div>
    </section>
  </main>
  <aside class="card" style="min-width:300px; flex:1; margin-left:2rem; align-self: flex-start;">
    <h3>Organizador</h3>
    <%
    if (organizador != null) {
    %>
      <p><strong>Nombre:</strong> <%=organizador.getNombre()%></p>
    <%
    } else {
    %>
      <p>No disponible</p>
    <%
    }
    %>
    <h3>Tipos de Registro</h3>
    <%
    if (tiposRegistro != null && !tiposRegistro.isEmpty()) {
    %>
      <ul>
      <%
      for (Object trObj : tiposRegistro) {
                 logica.clases.TipoRegistro tr = (logica.clases.TipoRegistro) trObj;
      %>
        <li>
          <strong><%=tr.getNombre()%></strong>
          <a class="btn" href="<%=ctx%>/registro/ConsultaTipoRegistro?evento=<%=edicion.getEvento().getNombre()%>&edicion=<%=edicion.getNombre()%>&tipoRegistro=<%=tr.getNombre()%>">Ver detalles</a>
        </li>
      <%
      }
      %>
      </ul>
    <%
    } else {
    %>
      <p>No hay tipos de registro asociados.</p>
    <%
    }
    %>
    <h3>Patrocinios</h3>
    <%
    if (patrocinios != null && !patrocinios.isEmpty()) {
    %>
      <ul>
      <%
      for (Object pObj : patrocinios) {
                 logica.clases.Patrocinio p = (logica.clases.Patrocinio) pObj;
      %>
        <li>
          <strong><%= p.getInstitucion().getNombre() %></strong>
          <a class="btn" href="<%= ctx %>/edicion/ConsultaPatrocinio?evento=<%= edicion.getEvento().getNombre() %>&edicion=<%= edicion.getNombre() %>&codigoPatrocinio=<%= p.getCodigoPatrocinio() %>">Ver detalles</a>
        </li>
      <% } %>
      </ul>
    <% } else { %>
      <p>No hay patrocinios asociados.</p>
    <% } %>
  </aside>
</div>

</body>
</html>