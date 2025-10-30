<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="publicadores.DtEdicion, publicadores.DtRegistro, publicadores.DtTipoRegistro, publicadores.DtPatrocinio" %>
<%
  String ctx  = request.getContextPath();
  String nick = (String) session.getAttribute("nick");
  String rol  = (String) session.getAttribute("rol");

  DtEdicion edicion = (DtEdicion) request.getAttribute("edicion");
  String organizador = (String) request.getAttribute("organizador");
  @SuppressWarnings("unchecked")
  List<DtRegistro> registros = (List<DtRegistro>) request.getAttribute("registros");
  @SuppressWarnings("unchecked")
  List<DtTipoRegistro> tiposRegistro = (List<DtTipoRegistro>) request.getAttribute("tiposRegistro");
  @SuppressWarnings("unchecked")
  List<DtPatrocinio> patrocinios = (List<DtPatrocinio>) request.getAttribute("patrocinios");

  publicadores.DtRegistro regUsr = (publicadores.DtRegistro) request.getAttribute("registroUsuario");

  // nombre del evento 
  String evNombre = (String) request.getAttribute("evNombre");

  // (no duplicar ctx en la JSP)
  String edImagenUrl = (String) request.getAttribute("edImagenUrl");
  boolean hasAnyImg = (edImagenUrl != null && !edImagenUrl.isBlank());
  String edVideoRaw = (edicion != null) ? edicion.getVideo() : null;
  String edVideoEmbed = null;
  boolean hasVideo = false;
  if (edVideoRaw != null && !edVideoRaw.isBlank()) {
    String lower = edVideoRaw.toLowerCase();
    try {
      if (lower.contains("youtube.com/watch") || lower.contains("youtube.com/watch?")) {
        int idx = edVideoRaw.indexOf("v=");
        if (idx >= 0) {
          String id = edVideoRaw.substring(idx + 2);
          int amp = id.indexOf('&');
          if (amp > 0) id = id.substring(0, amp);
          edVideoEmbed = "https://www.youtube.com/embed/" + id;
        }
      } else if (lower.contains("youtu.be/")) {
        int slash = edVideoRaw.lastIndexOf('/');
        if (slash >= 0) {
          String id = edVideoRaw.substring(slash + 1);
          int q = id.indexOf('?'); if (q > 0) id = id.substring(0, q);
          edVideoEmbed = "https://www.youtube.com/embed/" + id;
        }
      } else if (lower.contains("vimeo.com/")) {
        int slash = edVideoRaw.lastIndexOf('/');
        if (slash >= 0) {
          String id = edVideoRaw.substring(slash + 1);
          int q = id.indexOf('?'); if (q > 0) id = id.substring(0, q);
          edVideoEmbed = "https://player.vimeo.com/video/" + id;
        }
      } else if (lower.startsWith("http://") || lower.startsWith("https://")) {
        edVideoEmbed = edVideoRaw; // fallback: try direct URL
      }
    } catch (Exception ignore) { edVideoEmbed = edVideoRaw; }
    hasVideo = (edVideoEmbed != null && !edVideoEmbed.isBlank());
  }
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta de Edición — <%=(edicion != null ? edicion.getNombre() : "Edición")%></title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaEdicionBase.css">
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaEdicion.css">
  <style>
    .page-consulta-edicion .ed-center{ max-width: 1400px !important; margin: 0 auto !important; padding: 0 1rem; }
    .page-consulta-edicion .event-card{ border-radius: 12px; overflow: hidden; }
    .page-consulta-edicion .img-frame{ width:100% !important; max-width:1200px !important; aspect-ratio:16/9; background:#f3f4f6; border-radius:14px; overflow:hidden; margin:.5rem auto 1rem; }
    .page-consulta-edicion .img-frame img{ width:100%; height:100%; object-fit:cover; display:block; }
    .page-consulta-edicion .event-info.event-text{ max-width:1100px !important; margin:0 auto !important; }
    .page-consulta-edicion .event-header{ text-align:center; }
    .page-consulta-edicion.no-img .event-info.event-text{ margin-top:.5rem !important; }
    .page-consulta-edicion .video-frame iframe { height: 640px; max-height: 80vh; }
    @media (max-width: 1200px){
      .page-consulta-edicion .img-frame{ max-width:100% !important; }
      .page-consulta-edicion .event-info.event-text{ max-width:100% !important; }
      .page-consulta-edicion .video-frame iframe { height: 420px; }
    }
    @media (max-width: 900px){
      .page-consulta-edicion .img-frame{ max-width:100% !important; }
      .page-consulta-edicion .event-info.event-text{ max-width:100% !important; }
    }
    @media (max-width: 600px){
      .page-consulta-edicion .video-frame iframe { height: 260px; }
    }
    .registro-detalle {
      border: 1px solid #e5e7eb;
      background: #fafafa;
      border-radius: 10px;
      padding: 1rem;
      margin-top: 1rem;
    }
    .registro-detalle h3 { margin-top: 0; }
  </style>
</head>
<body>

<jsp:include page="/WEB-INF/templates/header.jsp" />

<div class="container row page-consulta-edicion <%= hasAnyImg ? "" : "no-img" %>" style="margin-top:1rem; display:flex; align-items:flex-start;">
  <jsp:include page="/WEB-INF/templates/menu.jsp" />

  <main class="container page-consulta-edicion" style="margin-top:1.5rem;">
  <section class="card event-card" style="max-width:750px; margin:0 auto; padding:1.25rem;">
	<div class="event-meta" style="margin-bottom:1rem; text-align:center;">
  <% if (hasAnyImg) { %>
    <div class="img-frame" style="
         margin: 0 auto 1rem;
         border-radius: 10px;
         overflow: hidden;
         width: 60%;
         max-width: 500px;
         box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    ">
      <img src="<%= edImagenUrl %>" 
           alt="Imagen de la edición <%= edicion.getNombre() %>" 
           style="width:100%; height:auto; display:block;">
    </div>
  <% } %>

  <% if (hasVideo) { %>
    <div class="video-frame" style="
         margin: 1rem auto;
         width: 60%;
         max-width: 500px;
         border-radius: 10px;
         overflow: hidden;
         box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    ">
      <iframe src="<%= edVideoEmbed %>" 
              width="100%" height="280" 
              frameborder="0" allowfullscreen></iframe>
    </div>
  <% } %>
</div>

    <h1 style="font-size:1.6rem; font-weight:700; margin-bottom:.25rem;"><%= edicion.getNombre() %></h1>
    <p style="font-size:1rem; color:#666; margin-bottom:1rem;">
      <strong>Evento:</strong> <%= evNombre %> — 
      <strong>Organizador:</strong> <%= organizador %>
    </p>

    <div class="event-meta" style="margin-bottom:1rem;">
      <p><strong>Sigla:</strong> <%= edicion.getSigla() %></p>
      <p><strong>Ciudad:</strong> <%= edicion.getCiudad() %></p>
      <p><strong>País:</strong> <%= edicion.getPais() %></p>
      <p><strong>Inicio:</strong> <%= edicion.getFechaInicio() %></p>
      <p><strong>Fin:</strong> <%= edicion.getFechaFin() %></p>
      <p><strong>Alta:</strong> <%= edicion.getFechaAlta() %></p>
      <p><strong>Estado:</strong> <%= edicion.getEstado() %></p>
    <% if (organizador != null && !organizador.isBlank()) { %>
      <p><strong>Organizador:</strong> <%= organizador %></p>
    <% } else { %>
      <p>No disponible</p>
    <% } %>
    </div>

    

    <% if (regUsr != null && "ASISTENTE".equalsIgnoreCase(rol)) { %>
      <section class="registro-detalle" style="border:1px solid #ddd; background:#fafafa; border-radius:10px; padding:1rem; margin-top:1.5rem;">
        <h3 style="margin-top:0;">Tu registro en esta edición</h3>
        <p>Ya estás registrado en esta edición del evento.</p>
        <form action="<%= ctx %>/registro/ConsultaRegistroEdicion" method="get">
          <input type="hidden" name="idRegistro" value="<%= regUsr.getIdentificador() %>">
          <button type="submit" class="btn btn-ver-detalles" style="margin-top:.5rem;">Ver detalles del registro</button>
        </form>
      </section>
    <% } %>

    <section style="margin-top:1.5rem;">
      <h3>Tipos de Registro</h3>
      <% if (tiposRegistro != null && !tiposRegistro.isEmpty()) { %>
        <ul style="list-style:none; padding:0;">
          <% for (DtTipoRegistro tr : tiposRegistro) { %>
            <li style="margin:.3rem 0;">
              <strong><%= tr.getNombre() %></strong>
              <form action="<%=ctx%>/registro/ConsultaTipoRegistro" method="get" style="display:inline;">
                <input type="hidden" name="evento" value="<%= evNombre %>">
                <input type="hidden" name="edicion" value="<%= edicion.getNombre() %>">
                <input type="hidden" name="tipoRegistro" value="<%= tr.getNombre() %>">
                <button type="submit" class="btn btn-ver-detalles" style="margin-left:0.5rem;">Ver detalles</button>
              </form>
            </li>
          <% } %>
        </ul>
      <% } else { %>
        <p>No hay tipos de registro asociados.</p>
      <% } %>
    </section>

    <section style="margin-top:1.5rem;">
      <h3>Patrocinios</h3>
      <% if (patrocinios != null && !patrocinios.isEmpty()) { %>
        <ul style="list-style:none; padding:0;">
          <% for (DtPatrocinio p : patrocinios) { %>
            <li style="margin:.3rem 0;">
              <strong><%= p.getInstitucion() %></strong>
              <form action="<%=ctx%>/edicion/ConsultaPatrocinio" method="get" style="display:inline;">
                <input type="hidden" name="evento" value="<%= evNombre %>">
                <input type="hidden" name="edicion" value="<%= edicion.getNombre() %>">
                <input type="hidden" name="codigoPatrocinio" value="<%= p.getCodigo() %>">
                <button type="submit" class="btn btn-ver-detalles" style="margin-left:0.5rem;">Ver detalles</button>
              </form>
            </li>
          <% } %>
        </ul>
      <% } else { %>
        <p>No hay patrocinios asociados.</p>
      <% } %>
    </section>
  </section>
</main>


 
</div>

<script>
  function toggleDetalles(id) {
    const detalle = document.getElementById(id);
    if (detalle) detalle.classList.toggle('oculto');
  }
</script>

</body>
</html>
