<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx   = request.getContextPath();
  String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Crear cuenta — Eventos.uy</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/altaUsuario.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>
<body>

<!-- Header -->
<header class="site-header">
  <div class="container">
    <a class="brand" href="<%=ctx%>/">Eventos.uy</a>
    <nav class="main-nav">
      <form class="search" action="<%=ctx%>/buscar" method="get" role="search">
        <input class="search-input" type="search" name="q" placeholder="Eventos, Ediciones">
        <button class="btn" type="submit">Buscar</button>
      </form>
    </nav>
    <nav class="user-nav" id="userNav">
      <a class="btn ghost" href="<%=ctx%>/auth/login">Iniciar Sesión</a>
      <a class="btn" href="<%=ctx%>/usuario/AltaUsuario">Registrarse</a>
    </nav>
  </div>
</header>

<!-- Layout principal con sidebar -->
<div class="container row layout-alta-usuario">

  <!-- Sidebar -->
  <aside class="card">
    <h3>Menú</h3>
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

  <!-- Main -->
  <main class="form-wrapper-alta-usuario">
    <h1 class="titulo-alta-usuario">Crear cuenta</h1>

    <form id="altaForm" class="form-alta-usuario" method="post" action="<%=ctx%>/usuario/AltaUsuario" novalidate>

      <!-- Tipo de cuenta -->
      <fieldset class="fieldset-rol">
        <legend>Tipo de cuenta</legend>
        <label class="rol-opcion">
          <input type="radio" name="rol" value="asistente" checked> Asistente
        </label>
        <label class="rol-opcion">
          <input type="radio" name="rol" value="organizador"> Organizador
        </label>
      </fieldset>

      <!-- Datos comunes -->
      <div class="fila-doble">
        <div class="input-group">
          <label for="nick">Nick*</label>
          <input id="nick" name="nick" required placeholder="Tu alias">
        </div>
        <div class="input-group">
          <label for="email">Correo*</label>
          <input id="email" name="email" type="email" required placeholder="tucorreo@ejemplo.com">
        </div>
      </div>

      <div class="fila-doble">
        <div class="input-group">
          <label for="pass">Contraseña*</label>
          <input id="pass" name="pass" type="password" required minlength="4">
        </div>
        <div class="input-group">
          <label for="pass2">Repetir contraseña*</label>
          <input id="pass2" name="pass2" type="password" required minlength="4">
        </div>
      </div>

      <!-- Asistente -->
      <section id="grupoAsistente" data-role="asistente">
        <h3>Datos de asistente</h3>
        <div class="fila-doble">
          <div class="input-group">
            <label for="nombreA">Nombre*</label>
            <input id="nombreA" name="nombreA">
          </div>
          <div class="input-group">
            <label for="apellidoA">Apellido*</label>
            <input id="apellidoA" name="apellidoA">
          </div>
        </div>
        <div class="fila-doble">
          <div class="input-group">
            <label for="nacA">Fecha de nacimiento*</label>
            <input id="nacA" name="nacA" type="date">
          </div>
          <div class="input-group">
            <label for="instIdA">Institución (opcional)</label>
            <input id="instIdA" name="instIdA">
          </div>
        </div>
      </section>

      <!-- Organizador -->
      <section id="grupoOrganizador" data-role="organizador" style="display:none;">
        <h3>Datos de organizador</h3>
        <div class="fila-doble">
          <div class="input-group">
            <label for="nombreO">Nombre de la organización*</label>
            <input id="nombreO" name="nombreO">
          </div>
          <div class="input-group">
            <label for="webO">Sitio web (opcional)</label>
            <input id="webO" name="webO" type="url">
          </div>
        </div>
        <div class="input-group">
          <label for="descO">Descripción*</label>
          <textarea id="descO" name="descO" rows="3"></textarea>
        </div>
      </section>

      <!-- Acciones -->
      <div class="acciones-form">
        <button type="submit" class="btn">Crear cuenta</button>
        <a class="btn ghost" href="<%=ctx%>/">Cancelar</a>
      </div>

      <!-- Validación -->
      <p id="msg" style="display:none;"></p>
    </form>
  </main>
</div>

<!-- Script -->
<script>
(function(){
  const $ = s => document.querySelector(s);
  const form = $('#altaForm');
  if (!form) return;

  const msg  = $('#msg');
  const grupoA = $('#grupoAsistente');
  const grupoO = $('#grupoOrganizador');
  const rolRadios = form.querySelectorAll('input[name="rol"]');

  function setDisabled(groupEl, disabled){
    groupEl.querySelectorAll('input,select,textarea').forEach(el => el.disabled = disabled);
  }
  function setRequired(el, req){ if (el) el.required = req; }

  function applyRoleUI(role){
    const isAsis = role === 'asistente';
    grupoA.style.display = isAsis ? '' : 'none';
    grupoO.style.display = isAsis ? 'none' : '';
    setDisabled(grupoA, !isAsis);
    setDisabled(grupoO,  isAsis);
    setRequired($('#nombreA'), isAsis);
    setRequired($('#apellidoA'), isAsis);
    setRequired($('#nacA'), isAsis);
    setRequired($('#nombreO'), !isAsis);
    setRequired($('#descO'),   !isAsis);
  }

  applyRoleUI('asistente');
  rolRadios.forEach(r => r.addEventListener('change', e => applyRoleUI(e.target.value)));

  function showMsg(text, ok=false){
    if (!msg) return;
    msg.textContent = text;
    msg.style.display = 'block';
    msg.style.color = ok ? 'green' : 'red';
  }

  function validateCommon(){
    const nick = form.nick.value.trim();
    const email = form.email.value.trim();
    const pass = form.pass.value;
    const pass2 = form.pass2.value;
    if (!nick || !email || !pass || !pass2) {
      showMsg("Completá los campos obligatorios.");
      return false;
    }
    if (pass.length < 4) {
      showMsg("La contraseña debe tener al menos 4 caracteres.");
      return false;
    }
    if (pass !== pass2) {
      showMsg("Las contraseñas no coinciden.");
      return false;
    }
    return true;
  }

  function validateByRole(role){
    if (role === 'asistente'){
      if (!form.nombreA.value.trim() || !form.apellidoA.value.trim() || !form.nacA.value){
        showMsg("Completá Nombre, Apellido y Fecha de nacimiento.");
        return false;
      }
    } else {
      if (!form.nombreO.value.trim() || !form.descO.value.trim()){
        showMsg("Completá Nombre de la organización y Descripción.");
        return false;
      }
    }
    return true;
  }

  form.addEventListener('submit', (e) => {
    const role = form.querySelector('input[name="rol"]:checked')?.value || 'asistente';
    msg.style.display = 'none';
    if (!validateCommon() || !validateByRole(role)){
      e.preventDefault();
    }
  });
})();
</script>

</body>
</html>
