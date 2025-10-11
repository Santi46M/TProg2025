<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx  = request.getContextPath();
  String nick = (String) session.getAttribute("nick");
  String rol  = (String) session.getAttribute("rol");

  Object obj = request.getAttribute("edicion");
  // Intentamos leer por reflexión ligera para no romper si falta algo
  String nombre = null, desc = null, fechaInicio = null, fechaFin = null, lugar = null, eventoNombre = null, imagenUrl = null;
  try {
    if (obj != null) {
      java.lang.reflect.Method m;
      m = obj.getClass().getMethod("getNombre");    nombre = (String) m.invoke(obj);
      try { m = obj.getClass().getMethod("getDescripcion"); desc  = (String) m.invoke(obj); } catch (Exception ignore) {}
      try { m = obj.getClass().getMethod("getFechaInicio"); Object fi = m.invoke(obj); fechaInicio = (fi != null ? fi.toString() : null); } catch (Exception ignore) {}
      try { m = obj.getClass().getMethod("getFechaFin"); Object ff = m.invoke(obj); fechaFin = (ff != null ? ff.toString() : null); } catch (Exception ignore) {}
      try { m = obj.getClass().getMethod("getLugar"); lugar = (String) m.invoke(obj); } catch (Exception ignore) {}
      try { m = obj.getClass().getMethod("getEventoNombre"); eventoNombre = (String) m.invoke(obj); } catch (Exception ignore) {}
      try { m = obj.getClass().getMethod("getImagenUrl"); imagenUrl = (String) m.invoke(obj); } catch (Exception ignore) {}
    }
  } catch (Exception e) {
    // si algo falla, seguimos y mostramos lo que haya
  }
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta de Edición — <%= (nombre!=null? nombre : "Edición") %></title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaEventoBase.css">
</head>
<body>

<jsp:include page="/WEB-INF/templates/header.jsp" />

<div class="container row" style="margin-top:1rem;">

  <aside class="card">
    <h3><i class='bx bx-list-ul'></i> Menú</h3>
    <h4>Acciones</h4>
    <ul>
      <li><a href="<%=ctx%>/evento/alta"><i class='bx bx-edit'></i> Alta Evento</a></li>
      <li><a href="<%=ctx%>/edicion/alta"><i class='bx bx-edit'></i> Alta de Edición Evento</a></li>
      <li><a href="#"><i class='bx bx-edit'></i> Alta Tipo Registro</a></li>
    </ul>
  </aside>

  <main class="container consulta-layout">
    <section class="event-card">
      <div class="event-header">
        <h1 class="event-title"><%= (nombre!=null? nombre : "Edición") %></h1>
        <% if (eventoNombre != null) { %>
          <div class="event-meta"><strong>Evento:</strong> <%= eventoNombre %></div>
        <% } %>
      </div>
      <div class="event-info">
        <h3>Descripción</h3>
        <p><%= (desc!=null? desc : "—") %></p>
        <div class="event-meta"><strong>Fecha inicio:</strong> <%= (fechaInicio!=null? fechaInicio : "—") %></div>
        <div class="event-meta"><strong>Fecha fin:</strong> <%= (fechaFin!=null? fechaFin : "—") %></div>
        <div class="event-meta"><strong>Lugar:</strong> <%= (lugar!=null? lugar : "—") %></div>
        <% if (imagenUrl != null && !imagenUrl.isEmpty()) { %>
          <div class="event-meta"><strong>Imagen:</strong><br><img src="<%= imagenUrl %>" alt="Imagen de la edición" style="max-width:300px;max-height:200px;"></div>
        <% } %>
      </div>
    </section>
  </main>
</div>

</body>
</html>
