<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx  = request.getContextPath();
  String nick = (String) session.getAttribute("nick");
  String rol  = (String) session.getAttribute("rol"); // "ASISTENTE" | "ORGANIZADOR"
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Crear Edición de Evento — Eventos.uy</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
</head>
<body>

<header class="site-header">
  <div class="container">
    <a class="brand" href="<%=ctx%>/"><i class='bx bxs-plane-alt'></i> Eventos.uy</a>

    <nav class="main-nav">
      <form class="search" action="<%=ctx%>/buscar" method="get" role="search" aria-label="Buscar">
        <input class="search-input" type="search" name="q" placeholder="Eventos, Ediciones">
        <button class="btn" type="submit">Buscar</button>
      </form>
    </nav>

    <nav class="user-nav" id="userNav">
      <% if (nick == null) { %>
        <a class="btn" href="<%=ctx%>/auth/login"><i class='bx bxs-user'></i> Iniciar sesión</a>
      <% } else { %>
        <span class="user-name">Hola, <strong><%= nick %></strong></span>
        <a class="btn" href="<%=ctx%>/auth/logout"><i class='bx bxs-user'></i> Cerrar sesión</a>
      <% } %>
    </nav>
  </div>
</header>

<div class="container row" style="margin-top:1rem;">
  <!-- Sidebar -->
  <aside class="card">
    <h3><i class='bx bx-list-ul'></i> Menú</h3>
    <h4>Acciones</h4>
    <ul>
      <li><a href="<%=ctx%>/evento/alta"><i class='bx bx-edit'></i> Alta Evento</a></li>
      <li><a href="<%=ctx%>/edicion/alta"><i class='bx bx-edit'></i> Alta de Edición Evento</a></li>
      <li><a href="#"><i class='bx bx-edit'></i> Alta Tipo Registro</a></li>
    </ul>
  </aside>

  <main class="container">
    <section class="form-card-altaEvento">
      <h2>Crear Edición de Evento</h2>

      <% if (request.getAttribute("error") != null) { %>
        <p style="color:#c00"><%= request.getAttribute("error") %></p>
      <% } %>

      <!-- IMPORTANTE: el action apunta a tu servlet /edicion/alta (POST) -->
      <form id="form-alta-edicion" method="post" action="<%=ctx%>/edicion/alta" enctype="application/x-www-form-urlencoded">
        <div class="form-group-altaEvento">
          <label for="evento">Evento<span style="color:red">*</span></label>
          <select id="evento" name="evento" required>
            <option value="">Seleccione un evento</option>
            <!-- Opciones de eventos deben ser generadas dinámicamente desde el backend -->
          </select>
        </div>

        <div class="form-group-altaEvento">
          <label for="nombre">Nombre de la edición<span style="color:red">*</span></label>
          <input type="text" id="nombre" name="nombre" required>
        </div>

        <div class="form-group-altaEvento">
          <label for="desc">Descripción<span style="color:red">*</span></label>
          <textarea id="desc" name="desc" rows="4" required></textarea>
        </div>

        <div class="form-group-altaEvento">
          <label for="fechaInicio">Fecha de inicio<span style="color:red">*</span></label>
          <input type="date" id="fechaInicio" name="fechaInicio" required>
        </div>

        <div class="form-group-altaEvento">
          <label for="fechaFin">Fecha de fin<span style="color:red">*</span></label>
          <input type="date" id="fechaFin" name="fechaFin" required>
        </div>

        <div class="form-group-altaEvento">
          <label for="lugar">Lugar<span style="color:red">*</span></label>
          <input type="text" id="lugar" name="lugar" required>
        </div>

        <div class="form-group-altaEvento">
          <label for="imagen">Imagen (opcional)</label>
          <input type="file" id="imagen" name="imagen" accept="image/*">
        </div>

        <p class="form-hint-altaEvento">Los campos marcados con * son obligatorios.</p>

        <div class="form-actions-altaEvento">
          <button type="submit" class="btn-guardar-altaEvento">Guardar</button>
          <a class="btn-cancelar-altaEvento btn" href="<%=ctx%>/">Cancelar</a>
        </div>
      </form>
    </section>
  </main>
</div>

</body>
</html>
