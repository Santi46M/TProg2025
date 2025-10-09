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
  <link rel="stylesheet" href="<%=ctx%>/css/altaEvento.css"><!-- nuevo CSS -->
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>
<body>

<header class="site-header">
  <div class="container">
    <a class="brand" href="<%=ctx%>/inicio">Eventos.uy</a>
    <nav class="main-nav">
      <form class="search" action="<%=ctx%>/buscar" method="get" role="search" aria-label="Buscar">
        <input class="search-input" type="search" name="q" placeholder="Eventos, Ediciones">
        <button class="btn" type="submit">Buscar</button>
      </form>
    </nav>
    <nav class="user-nav" id="userNav">
      <span class="user-name">Hola, <strong><%= nick != null ? nick : "miseventos" %></strong></span>
      <a class="btn" href="<%=ctx%>/usuario/perfil"><i class='bx bxs-user'></i> Ver Perfil</a>
      <a class="btn" href="<%=ctx%>/auth/logout"><i class='bx bxs-user'></i> Cerrar sesión</a>
    </nav>
  </div>
</header>

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

  <!-- Main -->
  <main class="container">
    <section class="form-card-altaEvento">
      <h2>Crear Evento</h2>

      <% if (request.getAttribute("error") != null) { %>
        <p class="error-msg"><%= request.getAttribute("error") %></p>
      <% } %>

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
  (function () {
    const form = document.getElementById('form-alta-evento');
    form.addEventListener('submit', function (e) {
      const checks = Array.from(document.querySelectorAll('.cat'));
      const sel = checks.filter(c => c.checked).map(c => c.value);
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