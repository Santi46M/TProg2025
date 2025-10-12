<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/layoutMenu.css"><!-- <= acá -->

<%
String ctx = request.getContextPath();
  String rol = (String) session.getAttribute("rol");

  @SuppressWarnings("unchecked")
  java.util.List<String> categorias = (java.util.List<String>) request.getAttribute("categorias");
  if (categorias == null) {
      categorias = logica.controladores.ControladorEvento.listarCategorias();
  }
%>
<aside class="card aside-inicio">
  <h3>Menú</h3>

  <% if ("ORGANIZADOR".equals(rol)) { %>
    <h4>Acciones</h4>
    <ul>
      <li>
        <form action="<%=ctx%>/evento/alta" method="get" style="display:inline">
          <button type="submit" class="linklike">Crear Evento</button>
        </form>
      </li>
      <li>
        <form action="<%=ctx%>/edicion/alta" method="get" style="display:inline">
          <button type="submit" class="linklike">Crear Edición de Evento</button>
        </form>
      </li>
      <li>
        <form action="<%=ctx%>/registro/alta" method="get" style="display:inline">
          <button type="submit" class="linklike">Crear Registro</button>
        </form>
      </li>
    </ul>
  <% } else if ("ASISTENTE".equals(rol)) { %>
    <h4>Acciones</h4>
    <ul>
      <li>
        <form action="<%=ctx%>/registro/inscripcion" method="get" style="display:inline">
          <button type="submit" class="linklike">Registrarse a Edición de un Evento</button>
        </form>
      </li>
    </ul>
  <% } %>

  <h4>Categorías</h4>
  <ul class="menu-categorias">
    <% if (categorias != null && !categorias.isEmpty()) {
         for (String cat : categorias) { %>
      <li>
        <form action="<%=ctx%>/evento/listado" method="get">
          <input type="hidden" name="categoria" value="<%=cat%>">
          <button type="submit" class="linklike"><%=cat%></button>
        </form>
      </li>
    <% } } else { %>
      <li><span>(Sin categorías)</span></li>
    <% } %>
  </ul>

  <h4>
   <form action="<%=ctx%>/usuario/ConsultaUsuario" method="get" style="display:inline">
  <input type="hidden" name="listar" value="1">
  <button type="submit" class="linklike">Listar Usuarios</button>
</form>

  </h4>
</aside>
