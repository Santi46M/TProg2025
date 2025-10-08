<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  	String ctx = request.getContextPath();
	String error = (String) request.getAttribute("error"); 
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Crear cuenta — Eventos.uy</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>
<body>
  <!-- Header -->
  <header class="site-header">
    <div class="container">
      <a class="brand" href="<%=ctx%>/"><i class='bx bxs-plane-alt'></i> Eventos.uy</a>

      <nav class="main-nav">
        <!-- Buscador -->
        <form class="search" action="<%=ctx%>/buscar" method="get" role="search" aria-label="Buscar">
          <input class="search-input" type="search" name="q" placeholder="Eventos, Ediciones">
          <button class="btn" type="submit">Buscar</button>
        </form>
      </nav>

      <!-- Zona de usuario -->
      <nav class="user-nav" id="userNav">
        <a class="btn ghost" href="<%=ctx%>/auth/login"><i class='bx bxs-user'></i> Iniciar Sesión</a>
        <a class="btn" href="<%=ctx%>/usuario/AltaUsuario"><i class='bx bxs-user-plus'></i> Registrarse</a>
      </nav>
    </div>
  </header>

  <!-- Layout -->
  <div class="container row" style="margin-top:1rem;">
    <!-- Sidebar -->
    <aside class="card">
      <h3><i class='bx bx-list-ul'></i> Menú</h3>

      <h4>Categorías</h4>
      <ul>
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

    <main class="form-wrapper">
      <h1>Crear cuenta</h1>

      <!-- IMPORTANTE: enviar al servlet -->
      <form id="altaForm" class="card form-alta" method="post" action="<%=ctx%>/usuario/alta" novalidate>

        <!-- Tipo de cuenta -->
        <fieldset class="fieldset">
          <legend>Tipo de cuenta</legend>
          <label class="rol-opcion">
            <input type="radio" name="rol" value="asistente" checked>
            <span>Asistente</span>
          </label>
          <label class="rol-opcion">
            <input type="radio" name="rol" value="organizador">
            <span>Organizador</span>
          </label>
        </fieldset>

        <!-- Datos comunes -->
        <div class="fila-espaciada">
          <div class="input-group">
            <label for="nick">Nick*</label>
            <input id="nick" name="nick" required placeholder="Tu alias">
          </div>
          <div class="input-group">
            <label for="email">Correo*</label>
            <input id="email" name="email" type="email" required placeholder="tucorreo@ejemplo.com">
          </div>
        </div>

        <div class="fila-espaciada">
          <div class="input-group">
            <label for="pass">Contraseña*</label>
            <input id="pass" name="pass" type="password" required minlength="4">
          </div>
          <div class="input-group">
            <label for="pass2">Repetir contraseña*</label>
            <input id="pass2" name="pass2" type="password" required minlength="4">
          </div>
        </div>

        <!-- Datos asistente -->
        <section id="grupoAsistente" data-role="asistente">
          <h3>Datos de asistente</h3>
          <div class="fila-espaciada">
            <div class="input-group">
              <label for="nombreA">Nombre*</label>
              <input id="nombreA" name="nombreA" placeholder="Nombre">
            </div>
            <div class="input-group">
              <label for="apellidoA">Apellido*</label>
              <input id="apellidoA" name="apellidoA" placeholder="Apellido">
            </div>
          </div>
          <div class="fila-espaciada">
            <div class="input-group">
              <label for="nacA">Fecha de nacimiento*</label>
              <input id="nacA" name="nacA" type="date">
            </div>
            <div class="input-group">
              <label for="instIdA">Institución (opcional)</label>
              <input id="instIdA" name="instIdA" placeholder="ID institución">
            </div>
          </div>
        </section>

        <!-- Datos organizador -->
        <section id="grupoOrganizador" data-role="organizador" style="display:none;">
          <h3>Datos de organizador</h3>
          <div class="fila-espaciada">
            <div class="input-group">
              <label for="nombreO">Nombre de la organización*</label>
              <input id="nombreO" name="nombreO" placeholder="Ej.: Instituto de Computación">
            </div>
            <div class="input-group">
              <label for="webO">Sitio web (opcional)</label>
              <input id="webO" name="webO" type="url" placeholder="https://...">
            </div>
          </div>
          <div class="input-full">
            <label for="descO">Descripción*</label>
            <textarea id="descO" name="descO" rows="3" placeholder="Breve descripción de la organización"></textarea>
          </div>
        </section>

        <div class="acciones-form">
          <button type="submit" class="btn">Crear cuenta</button>
          <a class="btn ghost" href="<%=ctx%>/">Cancelar</a>
        </div>

        <p id="msg" style="display:none;"></p>
      </form>
    </main>
  </div>
</body>
</html>
