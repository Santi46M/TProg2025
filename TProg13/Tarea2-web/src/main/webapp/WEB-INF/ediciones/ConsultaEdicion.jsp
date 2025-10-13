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

  // Imagen inyectada por el servlet (preferida); si no hay, se intenta con la propia de la edición
  String edImagenUrl = (String) request.getAttribute("edImagenUrl");
  boolean hasServletImg = (edImagenUrl != null && !edImagenUrl.isBlank());
  boolean hasEdImg = (edicion != null && edicion.getImagen() != null && !edicion.getImagen().isEmpty());
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta de Edición — <%=(edicion != null ? edicion.getNombre() : "Edición")%></title>
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <!-- CSS base y overrides -->
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaEdicionBase.css"><!-- base -->
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaEdicion.css"><!-- opcional -->

  <!-- Scoped overrides para evitar solapes -->
  <style>
    .page-consulta-edicion .ed-center{
      max-width: 1200px !important;
      margin: 0 auto !important;
      padding: 0 1rem;
    }
    .page-consulta-edicion .event-card{
      border-radius: 12px;
      overflow: hidden;
    }
    /* Imagen centrada y responsiva */
    .page-consulta-edicion .img-frame{
      width: 100% !important;
      max-width: 900px !important;
      aspect-ratio: 16/9;
      background:#f3f4f6;
      border-radius:14px;
      overflow:hidden;
      margin: .5rem auto 1rem;
    }
    .page-consulta-edicion .img-frame img{ width:100%; height:100%; object-fit:cover; display:block; }
    .page-consulta-edicion .img-ph{
      width:100%; height:100%; display:flex; align-items:center; justify-content:center;
      color:#6b7280; font-size:.95rem;
    }
    .page-consulta-edicion .event-info.event-text{
      max-width: 900px !important;
      margin: 0 auto !important;
    }
    .page-consulta-edicion .event-header{ text-align:center; }

    /* Estilos del acordeón de asistentes (de tu 1ª versión) */
    .page-consulta-edicion .lista-asistentes { list-style: none; padding: 0; margin: 0; }
    .page-consulta-edicion .asistente-item {
      margin-bottom: 1rem; border: 1px solid var(--line); border-radius: 8px;
      background: #fff; padding: 0.75rem 1rem; box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    }
    .page-consulta-edicion .asistente-btn {
      background: none; border: none; font-size: 1.05rem; font-weight: 600;
      cursor: pointer; color: #333; width: 100%; text-align: left;
    }
    .page-consulta-edicion .asistente-btn:hover { color: var(--accent); }
    .page-consulta-edicion .asistente-detalle { margin-top: 0.5rem; padding-left: 1rem; font-size: 0.95rem; }
    .page-consulta-edicion .oculto { display: none; }

    @media (max-width: 900px){
      .page-consulta-edicion .img-frame{ max-width: 100% !important; }
      .page-consulta-edicion .event-info.event-text{ max-width: 100% !important; }
    }
  </style>
</head>
<body>

<jsp:include page="/WEB-INF/templates/header.jsp" />

<div class="container row page-consulta-edicion" style="margin-top:1rem; display:flex; align-items:flex-start;">
  <jsp:include page="/WEB-INF/templates/menu.jsp" />

  <main class="container consulta-layout" style="flex:2; min-width:0;">
    <div class="ed-center">
      <section class="event-card">
        <div class="event-header">
          <h1 class="event-title"><%= (edicion != null ? edicion.getNombre() : "Edición") %></h1>
        </div>

        <!-- IMAGEN (prioriza la del servlet; si no, la propia de la edición) -->
        <div class="img-frame">
          <% if (hasServletImg) { %>
            <img src="<%= edImagenUrl %>" alt="Imagen de la edición <%= (edicion != null ? edicion.getNombre() : "") %>">
          <% } else if (hasEdImg) { %>
            <img src="<%= edicion.getImagen() %>" alt="Imagen de la edición <%= (edicion != null ? edicion.getNombre() : "") %>">
          <% } else { %>
            <div class="img-ph">Sin imagen</div>
          <% } %>
        </div>

        <!-- INFO -->
        <div class="event-info event-text" style="padding: 15px; line-height: 2 ">
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
          <% } %>

          <% if (registros != null && !registros.isEmpty()) { %>
            <%-- CASO 1: ASISTENTE con un único registro propio --%>
            <% if ("ASISTENTE".equals(rol) && registros.size() == 1) {
                 logica.clases.Registro registro = (logica.clases.Registro) registros.get(0);
            %>
              <h3>Tu registro en esta edición</h3>
              <p><strong>Tipo:</strong> <%= registro.getTipoRegistro().getNombre() %></p>
              <p><strong>Fecha registro:</strong> <%= registro.getFechaRegistro() %></p>
              <p><strong>Costo:</strong> $<%= registro.getCosto() %></p>

            <%-- CASO 2: ORGANIZADOR de esta edición (acordeón por asistente) --%>
            <% } else if ("ORGANIZADOR".equals(rol)
                          && edicion != null
                          && edicion.getOrganizador() != null
                          && edicion.getOrganizador().getNickname().equals(nick)) { %>
              <h3>Asistentes registrados</h3>
              <ul class="lista-asistentes">
                <%
                  int i = 0;
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

            <%-- CASO 3: Otros roles/visiones -> tabla --%>
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
                       logica.clases.Registro r = (logica.clases.Registro) regObj;
                  %>
                    <tr>
                      <td><%= r.getUsuario().getNickname() %></td>
                      <td><%= r.getTipoRegistro().getNombre() %></td>
                      <td><%= r.getFechaRegistro() %></td>
                      <td>$<%= r.getCosto() %></td>
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
    </div>
  </main>

  <aside class="card" style="min-width:300px; flex:1; margin-left:2rem; align-self:flex-start;">
    <h3>Organizador</h3>
    <% if (organizador != null) { %>

      <p><strong>Nombre:</strong> <%= organizador.getNickname() %></p>

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
    if (detalle) detalle.classList.toggle('oculto');
  }
</script>

</body>
</html>
