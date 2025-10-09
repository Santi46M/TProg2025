<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx  = request.getContextPath();
  String nick = (String) session.getAttribute("nick");
  boolean precargado = Boolean.TRUE.equals(application.getAttribute("datosPrecargados"));
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Crear Evento — Eventos.uy</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/index.css"><!-- para estilos compartidos del layout -->
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>
<body>

<!-- Header igual al index -->
<header class="site-header">
  <div class="container">
    <a class="brand" href="<%=ctx%>/">Eventos.uy</a>

    <nav class="main-nav">
      <form class="search" action="<%=ctx%>/buscar" method="get" role="search" aria-label="Buscar">
        <input class="search-input" type="search" name="q" placeholder="Eventos, Ediciones">
        <button class="btn" type="submit">Buscar</button>
      </form>
    </nav>

    <nav class="user-nav" id="userNav">
      <% if (nick == null) { %>
        <a class="btn ghost" href="<%=ctx%>/auth/login"><i class='bx bxs-user'></i> Iniciar Sesión</a>
        <a class="btn" href="<%=ctx%>/usuario/alta"><i class='bx bxs-user-plus'></i> Registrarse</a>
        <% if (!precargado) { %>
          <form action="<%=ctx%>/precargar" method="post" style="display:inline;">
            <button class="btn" type="submit">Precargar datos</button>
          </form>
        <% } else { %>
          <span>Datos precargados correctamente</span>
        <% } %>
      <% } else { %>
        <span class="user-chip"><i class='bx bxs-user'></i> <%= nick %></span>
        <a class="btn ghost" href="<%=ctx%>/auth/logout"><i class='bx bx-log-out'></i> Cerrar sesión</a>
      <% } %>
    </nav>
  </div>
</header>

<div class="container row" style="margin-top:1rem;">
  <!-- Sidebar igual al index -->
  <aside class="card aside-inicio">
    <h3>Menú</h3>

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

    <h4><a href="<%=ctx%>/usuario/listar">Listar Usuarios</a></h4>
  </aside>

  <!-- Main -->
  <main class="container">
    <section class="form-card-altaEvento">
      <h2>Crear Evento</h2>

      <% if (request.getAttribute("error") != null) { %>
        <p style="color:#c00"><%= request.getAttribute("error") %></p>
      <% } %>

      <!-- el action habla con tu servlet /evento/alta (POST) -->
      <form id="form-alta-evento" method="post" action="<%=ctx%>/evento/alta">
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

        <!-- Categorías: se envían como CSV en el hidden "categorias" -->
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

        <p class="form-hint-altaEvento">Los campos marcados con * son obligatorios.</p>

        <div class="form-actions-altaEvento">
          <button type="submit" class="btn-guardar-altaEvento">Guardar</button>
          <a class="btn btn-cancelar-altaEvento" href="<%=ctx%>/">Cancelar</a>
        </div>
      </form>
    </section>
  </main>
</div>

<script>
  // Arma el CSV de categorías en el hidden "categorias" antes de enviar
  (function () {
    var form = document.getElementById('form-alta-evento');
    form.addEventListener('submit', function (e) {
      var checks = Array.prototype.slice.call(document.querySelectorAll('.cat'));
      var sel = checks.filter(function(c){ return c.checked; }).map(function(c){ return c.value; });
      if (sel.length === 0) {
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
