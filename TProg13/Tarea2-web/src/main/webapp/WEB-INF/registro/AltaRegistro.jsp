<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, logica.Datatypes.DTEvento, logica.Clases.Ediciones" %>
<%
  String ctx = request.getContextPath();
  List<DTEvento> eventos = (List<DTEvento>) request.getAttribute("eventos");
  List<Ediciones> ediciones = (List<Ediciones>) request.getAttribute("ediciones");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Alta de Tipo de Registro — Eventos.uy</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/altaTipoRegistro.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>
<body>

<jsp:include page="/WEB-INF/templates/header.jsp" />

<div class="container row" style="margin-top:1rem;">
  <!-- Sidebar -->
  <aside class="card aside-inicio">
    <h3>Menú</h3>
    <h4>Acciones</h4>
    <ul>
      <li><a href="<%=ctx%>/evento/alta">Alta Evento</a></li>
      <li><a href="<%=ctx%>/edicion/alta">Alta Edición Evento</a></li>
      <li><a href="<%=ctx%>/registro/alta">Alta Registro</a></li>
    </ul>

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

  <!-- Contenido principal -->
  <main class="container">
    <section class="form-card-altaTipoRegistro">
      <h2>Alta de Tipo de Registro</h2>

      <% if (request.getAttribute("error") != null) { %>
        <p style="color:#c00"><%= request.getAttribute("error") %></p>
      <% } %>

      <form action="<%=ctx%>/registro/alta" method="post" id="form-alta-tipo">
        <!-- EDICIÓN filtrada -->
        <div class="form-group-altaTipoRegistro">
          <label>Edición del evento <span style="color:red">*</span></label>
          <select id="selectEdicion" name="edicion" required>
            <option value="">-- Seleccione una edición --</option>
            <% if (ediciones != null) {
                 for (Ediciones ed : ediciones) { %>
                   <option value="<%= ed.getSigla() %>"><%= ed.getNombre() %> (<%= ed.getEvento().getNombre() %>)</option>
            <%   }
               } %>
          </select>
        </div>

        <div class="form-group-altaTipoRegistro">
          <label>Nombre <span style="color:red">*</span></label>
          <input name="nombre" required>
        </div>

        <div class="form-group-altaTipoRegistro">
          <label>Descripción <span style="color:red">*</span></label>
          <textarea name="descripcion" rows="4" required></textarea>
        </div>

        <div class="form-group-altaTipoRegistro">
          <label>Costo <span style="color:red">*</span></label>
          <input type="number" name="costo" min="0" step="0.01" required>
        </div>

        <div class="form-group-altaTipoRegistro">
          <label>Cupo <span style="color:red">*</span></label>
          <input type="number" name="cupo" min="1" required>
        </div>

        <div class="form-actions-altaEvento">
          <button type="submit" class="btn-guardar-altaEvento">Guardar</button>
          <a href="<%=ctx%>/index-organizador.jsp" class="btn-cancelar-altaEvento">Cancelar</a>
        </div>
      </form>
    </section>
  </main>
</div>

</body>
</html>