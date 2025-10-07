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
  <title>Crear Evento — Eventos.uy</title>
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
      <li><a href="#"><i class='bx bx-edit'></i> Alta de Edición Evento</a></li>
      <li><a href="#"><i class='bx bx-edit'></i> Alta Tipo Registro</a></li>
    </ul>
  </aside>

  <main class="container">
    <section class="form-card-altaEvento">
      <h2>Crear Evento</h2>

      <% if (request.getAttribute("error") != null) { %>
        <p style="color:#c00"><%= request.getAttribute("error") %></p>
      <% } %>

      <!-- IMPORTANTE: el action apunta a tu servlet /evento/alta (POST) -->
      <form id="form-alta-evento" method="post" action="<%=ctx%>/evento/alta" enctype="application/x-www-form-urlencoded">
        <div class="form-group-altaEvento">
          <label for="nombre">Nombre del evento<span style="color:red">*</span></label>
          <input type="text" id="nombre" name="nombre" required>
        </div>

        <div class="form-group-altaEvento">
          <label for="sigla">Sigla<span style="color:red">*</span></label>
          <input type="text" id="sigla" name="sigla" required>
        </div>

        <div class="form-group-altaEvento">
          <label for="desc">Descripción<span style="color:red">*</span></label>
          <textarea id="desc" name="desc" rows="4" required></textarea>
        </div>

        <!-- Categorías: mandamos un solo parámetro CSV "categorias" (lo arma el JS) -->
        <fieldset class="form-group-altaEvento" id="fs-categorias">
          <legend>Categorías<span style="color:red">*</span></legend>
          <div class="checkbox-grid-ev">
            <label><span>Tecnología</span><input type="checkbox" class="cat" value="Tecnología"></label>
            <label><span>Innovación</span><input type="checkbox" class="cat" value="Innovación"></label>
            <label><span>Literatura</span><input type="checkbox" class="cat" value="Literatura"></label>
            <label><span>Cultura</span><input type="checkbox" class="cat" value="Cultura"></label>
            <label><span>Música</span><input type="checkbox" class="cat" value="Música"></label>
            <label><span>Deporte</span><input type="checkbox" class="cat" value="Deporte"></label>
            <label><span>Salud</span><input type="checkbox" class="cat" value="Salud"></label>
            <label><span>Entretenimiento</span><input type="checkbox" class="cat" value="Entretenimiento"></label>
            <label><span>Negocios</span><input type="checkbox" class="cat" value="Negocios"></label>
          </div>
          <input type="hidden" id="categorias" name="categorias" value="">
          <small>Marcá una o más categorías.</small>
        </fieldset>

        <div class="form-group-altaEvento">
          <label for="imagen">Imagen (opcional)</label>
          <input type="file" id="imagen" name="imagen" accept="image/*">
        </div>

        <p class="form-hint-altaEvento">Los campos marcados con * son obligatorios.</p>

        <div class="form-actions-altaEvento">
          <button type="submit" class="btn-guardar-altaEvento">Guardar</button>
          <a class="btn-cancelar-altaEvento btn" href="<%=ctx%%>/">Cancelar</a>
        </div>
      </form>
    </section>
  </main>
</div>

<script>
  // Junta las categorías seleccionadas en un CSV para el parámetro "categorias"
  (function () {
    var form = document.getElementById('form-alta-evento');
    form.addEventListener('submit', function (e) {
      var checks = Array.prototype.slice.call(document.querySelectorAll('.cat'));
      var sel = checks.filter(function(c){ return c.checked; }).map(function(c){ return c.value; });
      if (sel.length === 0) {
        // evita enviar si no hay categorías
        e.preventDefault();
        document.getElementById('fs-categorias').scrollIntoView({behavior:'smooth', block:'center'});
        return;
      }
      document.getElementById('categorias').value = sel.join(',');
    });
  })();
</script>

</body>
</html>
