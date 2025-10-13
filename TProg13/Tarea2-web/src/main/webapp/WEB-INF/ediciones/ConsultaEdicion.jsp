<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx  = request.getContextPath();
  String nick = (String) session.getAttribute("nick");
  String rol  = (String) session.getAttribute("rol");

  logica.clases.Ediciones edicion = (logica.clases.Ediciones) request.getAttribute("edicion");
  logica.clases.Usuario organizador = (logica.clases.Usuario) request.getAttribute("organizador");
  java.util.Collection tiposRegistro = (java.util.Collection) request.getAttribute("tiposRegistro");
  java.util.Collection patrocinios = (java.util.Collection) request.getAttribute("patrocinios");
  java.util.List registros = (java.util.List) request.getAttribute("registros");
%>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta de Edición — <%=(edicion != null ? edicion.getNombre() : "Edición")%></title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaEventoBase.css">
  <style>
    .lista-asistentes {
      list-style: none;
      padding: 0;
      margin: 0;
    }
    .asistente-item {
      margin-bottom: 1rem;
      border: 1px solid var(--line);
      border-radius: 8px;
      background: #fff;
      padding: 0.75rem 1rem;
      box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    }
    .asistente-btn {
      background: none;
      border: none;
      font-size: 1.05rem;
      font-weight: 600;
      cursor: pointer;
      color: #333;
      width: 100%;
      text-align: left;
    }
    .asistente-btn:hover {
      color: var(--accent);
    }
    .asistente-detalle {
      margin-top: 0.5rem;
      padding-left: 1rem;
      font-size: 0.95rem;
    }
    .oculto {
      display: none;
    }
  </style>
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp" />

<div class="container row" style="margin-top:1rem; display:flex; align-items:flex-start;">
  <jsp:include page="/WEB-INF/templates/menu.jsp" />

  <main class="container consulta-layout" style="flex:2; min-width:0;">
    <section class="event-card">
      <div class="event-header">
        <h1 class="event-title"><%= (edicion != null ? edicion.getNombre() : "Edición") %></h1>
      </div>

      <div class="event-info">
        <h3>Datos de la Edición</h3>
        <% if (edicion != null) { %>
          <div class="event-meta"><strong>Evento:</strong> <%= edicion.getEvento().getNombre() %></div>
          <div class="event-meta"><strong>Sigla:</strong> <%= edicion.getSigla() %></div>
          <div class="event-meta"><strong>Fecha inicio:</strong> <%= edicion.getFechaInicio() %></div>
          <div class="event-meta"><strong>Fecha fin:</strong> <%= edicion.getFechaFin() %></div>
          <div class="event-meta"><strong>Fecha alta:</strong> <%= edicion.getFechaAlta() %></div>
          <div class="event-meta"><strong>Ciudad:</strong> <%= edicion.getCiudad() %></div>
          <div class="event-meta"><strong>País:</strong> <%= edicion.getPais() %></div>
          <div class="event-meta"><strong>Estado:</strong> <%= edicion.getEstado() %></div>
          <% if (edicion.getImagen() != null && !edicion.getImagen().isEmpty()) { %>
            <div class="event-meta"><strong>Imagen:</strong><br>
              <img src="<%=edicion.getImagen()%>" alt="Imagen de la edición" style="max-width:300px;max-height:200px;">
            </div>
          <% } %>
        <% } %>

        <% if (registros != null && !registros.isEmpty()) { %>
          <%-- CASO 1: ASISTENTE --%>
          <% if ("ASISTENTE".equals(rol) && registros.size() == 1) {
                logica.clases.Registro registro = (logica.clases.Registro) registros.get(0);
          %>
              <h3>Tu registro en esta edición</h3>
              <p><strong>Tipo:</strong> <%= registro.getTipoRegistro().getNombre() %></p>
              <p><strong>Fecha registro:</strong> <%= registro.getFechaRegistro() %></p>
              <p><strong>Costo:</strong> $<%= registro.getCosto() %></p>

          <%-- CASO 2: ORGANIZADOR DE LA EDICIÓN --%>
          <% } else if ("ORGANIZADOR".equals(rol)
                        && edicion != null
                        && edicion.getOrganizador() != null
                        && edicion.getOrganizador().getNickname().equals(nick)) { %>

              <h3>Asistentes registrados</h3>
              <ul class="lista-asistentes">
                <% int i = 0;
                   for (Object regObj : registros) {
                     logica.clases.Registro registro = (logica.clases.Registro) regObj;
                     String id = "detalle-" + i++;
                %>
                  <li class="asistente-item">
                    <button class="asistente-btn" type="button" onclick="toggleDetalles('<%=id%>')">
                      👤 <%= registro.getUsuario().getNickname() %>
                    </button>
                    <div id="<%=id%>" class="asistente-detalle oculto">
                      <p><strong>Tipo:</strong> <%= registro.getTipoRegistro().getNombre() %></p>
                      <p><strong>Fecha registro:</strong> <%= registro.getFechaRegistro() %></p>
                      <p><strong>Costo:</strong> $<%= registro.getCosto() %></p>
                    </div>
                  </li>
                <% } %>
              </ul>

          <%-- CASO 3: OTROS ROLES --%>
          <% } else { %>
              <h3>Registros de la edición</h3>
              <table class="tabla-registros" style="width:100%; border-collapse:collapse; margin-bottom:1rem;">
                <thead>
                  <tr>
                    <th>Usuario</th>
                    <th>Tipo</th>
                    <th>Fecha registro</th>
                    <th>Costo</th>
                  </tr>
                </thead>
                <tbody>
                  <% for (Object regObj : registros) {
                       logica.clases.Registro registro = (logica.clases.Registro) regObj;
                  %>
                    <tr>
                      <td><%= registro.getUsuario().getNickname() %></td>
                      <td><%= registro.getTipoRegistro().getNombre() %></td>
                      <td><%= registro.getFechaRegistro() %></td>
                      <td>$<%= registro.getCosto() %></td>
                    </tr>
                  <% } %>
                </tbody>
              </table>
          <% } %>
        <% } else { %>
          <p>No hay registros para esta edición.</p>
        <% } %>
      </div>
    </section>
  </main>

  <aside class="card" style="min-width:300px; flex:1; margin-left:2rem; align-self:flex-start;">
    <h3>Organizador</h3>
    <% if (organizador != null) { %>
      <p><strong>Nombre:</strong> <%= organizador.getNombre() %></p>
    <% } else { %>
      <p>No disponible</p>
    <% } %>

    <h3>Tipos de Registro</h3>
    <% if (tiposRegistro != null && !tiposRegistro.isEmpty()) { %>
      <ul>
        <% for (Object trObj : tiposRegistro) {
             logica.clases.TipoRegistro tr = (logica.clases.TipoRegistro) trObj;
        %>
          <li>
            <strong><%= tr.getNombre() %></strong>
            <form action="<%=ctx%>/registro/ConsultaTipoRegistro" method="get" style="display:inline;">
              <input type="hidden" name="evento" value="<%=edicion.getEvento().getNombre()%>" />
              <input type="hidden" name="edicion" value="<%=edicion.getNombre()%>" />
              <input type="hidden" name="tipoRegistro" value="<%=tr.getNombre()%>" />
              <button type="submit" class="btn btn-ver-detalles" style="margin-left:0.5rem;">Ver detalles</button>
            </form>
          </li>
        <% } %>
      </ul>
    <% } else { %>
      <p>No hay tipos de registro asociados.</p>
    <% } %>

    <h3>Patrocinios</h3>
    <% if (patrocinios != null && !patrocinios.isEmpty()) { %>
      <ul>
        <% for (Object pObj : patrocinios) {
             logica.clases.Patrocinio p = (logica.clases.Patrocinio) pObj;
        %>
          <li>
            <strong><%= p.getInstitucion().getNombre() %></strong>
            <form action="<%=ctx%>/edicion/ConsultaPatrocinio" method="get" style="display:inline;">
              <input type="hidden" name="evento" value="<%=edicion.getEvento().getNombre()%>" />
              <input type="hidden" name="edicion" value="<%=edicion.getNombre()%>" />
              <input type="hidden" name="codigoPatrocinio" value="<%=p.getCodigoPatrocinio()%>" />
              <button type="submit" class="btn btn-ver-detalles" style="margin-left:0.5rem;">Ver detalles</button>
            </form>
          </li>
        <% } %>
      </ul>
    <% } else { %>
      <p>No hay patrocinios asociados.</p>
    <% } %>
  </aside>
</div>

<script>
function toggleDetalles(id) {
  const detalle = document.getElementById(id);
  detalle.classList.toggle('oculto');
}
</script>

</body>
</html>