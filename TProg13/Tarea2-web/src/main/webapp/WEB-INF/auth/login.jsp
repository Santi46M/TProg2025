<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx   = request.getContextPath();
  String error = (String) request.getAttribute("error"); // mensaje del servlet si falló el login
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Iniciar Sesión — Eventos.uy</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>
<body>
  <!-- Header -->
  <header class="site-header">
    <div class="container">
      <!-- Eliminado el ícono del avión -->
      <a class="brand" href="<%=ctx%>/inicio">Eventos.uy</a>

      <nav class="main-nav">
        <form class="search" action="<%=ctx%>/buscar" method="get" role="search" aria-label="Buscar">
          <input class="search-input" type="search" name="q" placeholder="Eventos, Ediciones">
          <button class="btn" type="submit">Buscar</button>
        </form>
      </nav>

      <!-- Zona de usuario (visitante) -->
      <nav class="user-nav" id="userNav">
        <a class="btn ghost" href="<%=ctx%>/auth/login">Iniciar Sesión</a>
        <a class="btn" href="<%=ctx%>/usuario/AltaUsuario">Registrarse</a>
      </nav>
    </div>
  </header>

  <!-- Main -->
  <main class="wrapper-iniciar-sesion">
    <h1 class="titulo-iniciar-sesion">Iniciar sesión</h1>

    <!-- IMPORTANTE: enviar al servlet -->
    <form id="loginForm" class="card form-iniciar-sesion" method="post" action="<%=ctx%>/auth/login">
      <div class="row-iniciar-sesion">
        <div class="campo-iniciar-sesion">
          <label for="email">Correo electrónico o Nickname</label>
          <input id="email" name="email" type="text" required placeholder="Correo o Nick">
        </div>
      </div>

      <div class="row-iniciar-sesion">
        <div class="campo-iniciar-sesion">
          <label for="pass">Contraseña</label>
          <input id="pass" name="pass" type="password" required>
        </div>
      </div>

      <div class="acciones-iniciar-sesion">
        <button type="submit" class="btn btn-iniciar-sesion">Ingresar</button>
        <a class="btn ghost btn-cancelar-iniciar-sesion" href="<%=ctx%>/">Cancelar</a>
      </div>

      <!-- Mensaje de error del servidor -->
      <p id="error" class="error-iniciar-sesion" style="<%= (error==null)? "display:none;" : "" %>">
        <%= (error==null)? "" : error %>
      </p>
    </form>
  </main>
</body>
</html>
