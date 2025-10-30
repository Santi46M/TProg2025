<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="publicadores.PublicadorEventoService, publicadores.DtEvento, publicadores.DtEventoArray, java.util.ArrayList, java.util.List, java.util.Map" %>
<%
  String ctx = request.getContextPath();
  String rol = (String) session.getAttribute("rol");
  String nick = (String) session.getAttribute("nick");
  boolean precargado = Boolean.TRUE.equals(application.getAttribute("datosPrecargados"));

  // Use the generated webservice client to fetch events instead of calling logic directly
  List<DtEvento> eventos = new ArrayList<>();
  try {
      PublicadorEventoService svc = new PublicadorEventoService();
      publicadores.PublicadorEvento port = svc.getPublicadorEventoPort();
      DtEventoArray arr = port.listarEventosVigentes();
      if (arr != null && arr.getItem() != null) {
          eventos.addAll(arr.getItem());
      }
  } catch (Exception ex) {
      // If the webservice call fails, log and fallback to any request attribute that might exist
      ex.printStackTrace();
      Object reqEvents = request.getAttribute("eventos");
      if (reqEvents instanceof java.util.List) {
          for (Object o : (java.util.List) reqEvents) {
              if (o instanceof DtEvento) {
                  eventos.add((DtEvento) o);
              }
          }
      }
  }

  Map<String,String> imgUrls = (java.util.Map<String,String>) request.getAttribute("imgUrls");
  //quisiera hacer responsive el index.jsp siguiendo los lineamientos de rwd
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Eventos.uy</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/layoutMenu.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
  <style>
    .cards {
      display: grid;
      grid-template-columns: repeat(auto-fill,minmax(260px,1fr));
      gap: 16px;
    }
    .card { border:1px solid #e5e7eb; border-radius:12px; overflow:hidden; background:#fff; padding:12px; }
    .card__media { position:relative; aspect-ratio:16/9; background:#f3f4f6; overflow:hidden; border-radius:10px; margin-bottom:8px; }
    .card__media img { width:100%; height:100%; object-fit:cover; display:block; }
    .card h2 { margin:.25rem 0 .5rem; }
    .card .actions { margin-top:.5rem; }
    .btn-linklike { background:#f9fafb; border:1px solid #d1d5db; padding:.45rem .75rem; border-radius:8px; cursor:pointer; }
    .btn-linklike:hover { background:#f3f4f6; }
    .card.no-media h2 { margin-top:0; }
  </style>
  <link rel="stylesheet" href="<%=ctx%>/css/custom.css">
</head>

<body>
  <jsp:include page="/WEB-INF/templates/header.jsp" />

  <div class="container row layout-inicio">
    <jsp:include page="/WEB-INF/templates/menu.jsp" />

    <main class="main-inicio">
      <h1>Próximos eventos</h1>

      <div id="eventList" class="cards">
        <%
		  if (eventos != null && !eventos.isEmpty()) {
		    for (DtEvento e : eventos) {
		      String nombre = e.getNombre();
		      String imgUrl = (imgUrls != null) ? imgUrls.get(nombre) : null;
		      if (imgUrl == null || imgUrl.isBlank()) {
		          imgUrl = ctx + "/img/eventos/" + (e.getImagen() != null ? e.getImagen() : "evento-default.svg");
		      }
		      boolean hasImg = (imgUrl != null && !imgUrl.isBlank());
		%>
		  <article class="card <%= hasImg ? "" : "no-media" %>">
		    <% if (hasImg) { %>
		      <div class="card__media">
		        <img src="<%= imgUrl %>" 
		             alt="Imagen de <%= e.getNombre() %>"
		             onerror="this.onerror=null;this.src='<%=ctx%>/img/evento-default.png';">
		      </div>
		    <% } %>
		
		    <h2><%= e.getNombre() %></h2>
		    <p><strong>Descripción:</strong> <%= e.getDescripcion() == null ? "" : e.getDescripcion() %></p>
		    <p><strong>Fecha:</strong> <%= (e.getFecha() == null ? "" : e.getFecha().toString()) %></p>
		
		    <div class="actions">
		      <form action="<%=ctx%>/evento/ConsultaEvento" method="get" style="display:inline">
		        <input type="hidden" name="nombre" value="<%= e.getNombre() %>">
		        <button type="submit" class="btn-linklike">Ver más</button>
		      </form>
		    </div>
		  </article>
		<%
		    }
		  } else {
		%>
		  <p>No hay eventos disponibles.</p>
		<%
		  }
		%>

      </div>
    </main>
  </div>
</body>
</html>
