<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String ctx = request.getContextPath();
  java.util.List<logica.Datatypes.DTEvento> lista =
      (java.util.List<logica.Datatypes.DTEvento>) request.getAttribute("lista");
  java.util.List<String> categorias =
      (java.util.List<String>) request.getAttribute("categorias");
  String catSel = (String) request.getAttribute("categoriaSeleccionada");
%>
<jsp:include page="/WEB-INF/templates/header.jsp"/>
<div class="page">
  <jsp:include page="/WEB-INF/templates/menu.jsp"/>
  <main class="card">
    <h2><%= (catSel==null||catSel.isBlank()) ? "Todos los eventos" : "Eventos en "+catSel %></h2>
    <ul>
    <% if (lista != null) for (var ev : lista) { %>
      <li><strong><%=ev.getNombre()%></strong> — <%=ev.getSigla()%></li>
    <% } %>
    </ul>
  </main>
</div>
