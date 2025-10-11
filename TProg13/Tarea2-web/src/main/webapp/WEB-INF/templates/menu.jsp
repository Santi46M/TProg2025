<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx = request.getContextPath();
  String rol = (String) session.getAttribute("rol");
%>

<aside class="card aside-inicio">
  <h3>Menú</h3>

  <% if ("ORGANIZADOR".equals(rol)) { %>
    <h4>Acciones</h4>
    <ul>
      <li><a href="<%=ctx%>/evento/alta">Alta Evento</a></li>
      <li><a href="<%=ctx%>/edicion/alta">Alta Edición Evento</a></li>
      <li><a href="<%=ctx%>/registro/alta">Alta Registro</a></li>
    </ul>
  <% } else if ("ASISTENTE".equals(rol)) { %>
    <h4>Acciones</h4>
    <ul>
      <li><a href="<%=ctx%>/registro/inscripcion">Registrarse a Edición de Evento</a></li>
    </ul>
  <% } %>

  <h4>Categorías</h4>
  <ul class="menu-categorias">
    <li><a href="#">Tecnología</a></li>
    <li><a href="#">Innovación</a></li>
    <li><a href="#">Literatura</a></li>
    <li><a href="#">Cultura</a></li>
    <li><a href="#">Música</a></li>
    <li><a href="#">Deporte</a></li>
    <li><a href="#">Salud</a></li>
    <li><a href="#">Entretenimiento</a></li>
    <li><a href="#">Negocios</a></li>
  </ul>

  <h4><a href="<%=ctx%>/usuario/ConsultaUsuario">Listar Usuarios</a></h4>
</aside>
