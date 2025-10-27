<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, publicadores.DtEdicion" %>
<%
String ctx = request.getContextPath();
String nick = (String) session.getAttribute("nick");
List<DtEdicion> edicionesRegistradas = (List<DtEdicion>) request.getAttribute("edicionesRegistradas");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Mis registros de ediciones</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp" />
<div class="container py-4">
  <h1 class="mb-4 text-center">Ediciones en las que estás registrado</h1>
  <% if (edicionesRegistradas == null || edicionesRegistradas.isEmpty()) { %>
    <div class="alert alert-info text-center">No estás registrado en ninguna edición.</div>
  <% } else { %>
    <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
      <% for (DtEdicion ed : edicionesRegistradas) { %>
        <div class="col d-flex">
          <div class="card h-100 shadow-sm flex-fill">
            <div class="card-body d-flex flex-column">
              <h5 class="card-title text-primary"><%= ed.getNombre() %></h5>
              <ul class="list-unstyled flex-grow-1">
                <li><strong>Evento:</strong> <%= ed.getEvento() %></li>
                <li><strong>Sigla:</strong> <%= ed.getSigla() %></li>
                <li><strong>Fecha inicio:</strong> <%= ed.getFechaInicio() %></li>
                <li><strong>Fecha fin:</strong> <%= ed.getFechaFin() %></li>
                <li><strong>Ciudad:</strong> <%= ed.getCiudad() %></li>
                <li><strong>País:</strong> <%= ed.getPais() %></li>
              </ul>
              <form action="<%= ctx %>/registro/ConsultaRegistroEdicion" method="get" class="mt-auto">
                <input type="hidden" name="usuario" value="<%= nick %>" />
                <input type="hidden" name="edicion" value="<%= ed.getNombre() %>" />
                <button type="submit" class="btn btn-primary w-100">Ver detalles de mi registro</button>
              </form>
            </div>
          </div>
        </div>
      <% } %>
    </div>
  <% } %>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
