<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
String ctx = request.getContextPath();
  String rol = (String) session.getAttribute("rol");
  String nick = (String) session.getAttribute("nick");
  boolean precargado = Boolean.TRUE.equals(application.getAttribute("datosPrecargados"));

  java.util.List<logica.datatypes.DTEvento> eventos =
    (java.util.List<logica.datatypes.DTEvento>) request.getAttribute("eventos");
%>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Eventos.uy</title>
		<link rel="stylesheet" href="<%=ctx%>/css/style.css">
<%--   	<link rel="stylesheet" href="<%=ctx%>/css/index.css"> --%>
<link rel="stylesheet" href="<%=ctx%>/css/layoutMenu.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>

<body>
  <!-- Header -->
    <jsp:include page="/WEB-INF/templates/header.jsp" />

  <div class="container row layout-inicio">
	<jsp:include page="/WEB-INF/templates/menu.jsp" />


    <!-- Main -->
    <main class="main-inicio">
      <h1>Próximos eventos</h1>

      <div id="eventList" class="row">
        <%
        if (eventos != null && !eventos.isEmpty()) {
                    for (logica.datatypes.DTEvento e : eventos) {
        %>
          <div class="card">
            <h2><%= e.getNombre() %></h2>
            <p><strong>Descripción:</strong> <%= e.getDescripcion() %></p>
            <p><strong>Fecha:</strong> <%= e.getFecha() %></p>
            <div class="actions">
              <a href="<%=ctx%>/evento/ConsultaEvento?nombre=<%= java.net.URLEncoder.encode(e.getNombre(), "UTF-8") %>" class="btn">
                Ver más
              </a>
            </div>
          </div>
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
