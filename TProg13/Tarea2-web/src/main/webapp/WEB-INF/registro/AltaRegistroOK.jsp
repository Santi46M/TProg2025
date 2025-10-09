<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx = request.getContextPath();
  String mensaje = (String) request.getAttribute("mensaje");
  if (mensaje == null) mensaje = "El registro se realizó correctamente.";
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Registro creado — Eventos.uy</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/index.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
  <style>
    .ok-box {
      background: #e6ffed;
      border: 1px solid #b3e6c1;
      color: #0a662e;
      padding: 2rem;
      border-radius: 10px;
      text-align: center;
      margin-top: 2rem;
    }
    .ok-box h2 {
      margin-top: 0;
    }
  </style>
</head>
<body>

<!-- Header -->
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
      <span class="user-name">Hola, <strong><%= session.getAttribute("nick") != null ? session.getAttribute("nick") : "miseventos" %></strong></span>
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

  <!-- Contenido principal -->
  <main class="container">
    <section class="ok-box">
      <h2>✅ ¡Registro creado con éxito!</h2>
      <p><%= mensaje %></p>

      <div style="margin-top: 2rem;">
        <a class="btn" href="<%=ctx%>/registro/alta"><i class='bx bx-plus-circle'></i> Crear otro</a>
        <a class="btn ghost" href="<%=ctx%>/"><i class='bx bx-home'></i> Volver al inicio</a>
      </div>
    </section>
  </main>
</div>

</body>
</html>