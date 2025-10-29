<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="publicadores.DtEvento" %>
<%@ page import="publicadores.DtEdicion" %>
<%
    String ctx = request.getContextPath();
    String query = (String) request.getAttribute("query");
    List<DtEvento> eventos = (List<DtEvento>) request.getAttribute("resultEventos");
    List<DtEdicion> ediciones = (List<DtEdicion>) request.getAttribute("resultEdiciones");
    String orden = request.getParameter("orden") != null ? request.getParameter("orden") : "fecha";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Resultados de búsqueda</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<%=ctx%>/css/style.css">
    <link rel="stylesheet" href="<%=ctx%>/css/ConsultaEdicionBase.css">
    <link rel="stylesheet" href="<%=ctx%>/css/ConsultaEdicion.css">
    <style>
      .page-busqueda .result-card{ border-radius: 12px; overflow: hidden; margin-bottom:2rem; }
      .page-busqueda .result-title{ font-size:2rem; margin-bottom:.5rem; }
      .page-busqueda .result-type{ font-weight:bold; color:#007bff; margin-bottom:.5rem; }
      .page-busqueda .btn{ margin-top: .5rem; }
      .page-busqueda .container{ max-width: 1200px; margin: 0 auto; }
      .orden-form { margin-bottom: 2rem; }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />
<div class="container page-busqueda" style="margin-top:1rem;">
    <form class="orden-form" method="get" action="<%=ctx%>/buscar">
        <input type="hidden" name="q" value="<%= query != null ? query : "" %>">
        <label for="orden">Ordenar por:</label>
        <select name="orden" id="orden" onchange="this.form.submit()">
            <option value="fecha" <%= "fecha".equals(orden) ? "selected" : "" %>>Fecha de alta (desc)</option>
            <option value="alfabetico_asc" <%= "alfabetico_asc".equals(orden) ? "selected" : "" %>>Alfabético (A-Z)</option>
            <option value="alfabetico_desc" <%= "alfabetico_desc".equals(orden) ? "selected" : "" %>>Alfabético (Z-A)</option>
        </select>
    </form>
    <%-- El ordenamiento real se hace en el servlet --%>
    <% if ((eventos != null && !eventos.isEmpty()) || (ediciones != null && !ediciones.isEmpty())) { %>
        <% if (eventos != null) {
            for (DtEvento ev : eventos) { %>
            <section class="result-card">
                <div style="display:flex; align-items:center; gap:2rem;">
                    <span class="result-title" style="font-size:2rem;">
                        <%= ev.getNombre() %> <span style="font-size:2rem; font-style:italic;">(Evento)</span>
                    </span>
                    <form action="<%=ctx%>/evento/ConsultaEvento" method="get" style="display:inline">
                        <input type="hidden" name="nombre" value="<%= ev.getNombre() %>">
                        <button type="submit" class="btn btn-primary">Ver detalle</button>
                    </form>
                </div>
            </section>
        <% }} %>
        <% if (ediciones != null) {
            for (DtEdicion ed : ediciones) { %>
            <section class="result-card">
                <div style="display:flex; align-items:center; gap:2rem;">
                    <span class="result-title" style="font-size:2rem;">
                        <%= ed.getNombre() %> <span style="font-size:2rem; font-style:italic;">(Edición)</span>
                    </span>
                    <form action="<%=ctx%>/edicion/ConsultaEdicion" method="get" style="display:inline">
                        <input type="hidden" name="nombre" value="<%= ed.getNombre() %>">
                        <button type="submit" class="btn btn-primary">Ver detalle</button>
                    </form>
                </div>
            </section>
        <% }} %>
    <% } else { %>
        <p>No se encontraron resultados.</p>
    <% } %>
</div>
</body>
</html>