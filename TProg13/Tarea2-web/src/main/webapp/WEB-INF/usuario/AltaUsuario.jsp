<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%! 
  // 🔹 Función auxiliar para obtener valores guardados en el request
  String val(jakarta.servlet.http.HttpServletRequest r, String name) {
    Object v = r.getAttribute(name);
    return v == null ? "" : v.toString();
  }
%>
<%
  String ctx   = request.getContextPath();
  String error = (String) request.getAttribute("error");
  boolean esPost = "POST".equalsIgnoreCase(request.getMethod());
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Crear cuenta — Eventos.uy</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/altaUsuario.css">
<link rel="stylesheet" href="<%=ctx%>/css/layoutMenu.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>
<body>

<jsp:include page="/WEB-INF/templates/header.jsp" />
<div class="container row layout-alta-usuario" style="display:flex; align-items:flex-start;">
  <jsp:include page="/WEB-INF/templates/menu.jsp" />
  <!-- Main -->
  <main class="form-wrapper-alta-usuario" style="flex:2; min-width:0;">
    <h1 class="titulo-alta-usuario">Crear cuenta</h1>
	<%
		System.out.println("Método de la request: " + request.getMethod());
		System.out.println("valor del error: " + error);
  		if (esPost && error != null && !error.isEmpty()) {
	%>
  		<div class="alerta-error" style="color: red; font-weight: bold; margin-bottom: 1rem;">
    		<%= error %>
  		</div>
	<%
  		}
	%>

    <form id="altaForm" class="form-alta-usuario" method="post" action="<%=ctx%>/usuario/AltaUsuario" novalidate>

      <!-- Tipo de cuenta -->
      <fieldset class="fieldset-rol">
        <legend>Tipo de cuenta</legend>
		<%
  			String rolSel = val(request, "rol");
  			if (rolSel.isEmpty()) rolSel = "asistente"; // valor por defecto
		%>

		<label class="rol-opcion">
  			<input type="radio" name="rol" value="asistente" 
         	<%= "asistente".equalsIgnoreCase(rolSel) ? "checked" : "" %>> Asistente
		</label>
		<label class="rol-opcion">
  			<input type="radio" name="rol" value="organizador" 
         	<%= "organizador".equalsIgnoreCase(rolSel) ? "checked" : "" %>> Organizador
		</label>

      </fieldset>

      <!-- Datos comunes -->
      <div class="fila-doble">
        <div class="input-group">
          <label for="nick">Nick*</label>
          <input id="nick" name="nick" value="<%= val(request, "nick") %>" required placeholder="Tu alias">
        </div>
        <div class="input-group">
          <label for="email">Correo*</label>
          <input id="email" name="email" type="email" value="<%= val(request, "email") %>"required placeholder="tucorreo@ejemplo.com">
        </div>
      </div>

      <div class="fila-doble">
        <div class="input-group">
          <label for="pass">Contraseña*</label>
          <input id="pass" name="pass" type="password" required minlength="4" value="<%= val(request, "pass") %>">
        </div>
        <div class="input-group">
          <label for="pass2">Repetir contraseña*</label>
          <input id="pass2" name="pass2" type="password" required minlength="4" value="<%= val(request, "pass2") %>">
        </div>
      </div>

      <!-- Asistente -->
      <section id="grupoAsistente" data-role="asistente">
        <h3>Datos de asistente</h3>
        <div class="fila-doble">
          <div class="input-group">
            <label for="nombreA">Nombre*</label>
            <input id="nombreA" name="nombreA" value="<%= val(request, "nombreA") %>">
          </div>
          <div class="input-group">
            <label for="apellidoA">Apellido*</label>
            <input id="apellidoA" name="apellidoA" value="<%= val(request, "apellidoA") %>">
          </div>
        </div>
        <div class="fila-doble">
          <div class="input-group">
            <label for="nacA">Fecha de nacimiento*</label>
            <input id="nacA" name="nacA" type="date" value="<%= val(request, "nacA") %>">
          </div>
          <div class="input-group">
            <label for="instIdA">Institución (opcional)</label>
            <select id="instIdA" name="instIdA">
              <option value="">-- Seleccione una institución --</option>
              <% Collection<String> instituciones = (Collection<String>) request.getAttribute("instituciones");
            		String instSel = val(request, "instIdA");
            		if (instituciones != null) {
                   for (String inst : instituciones) { %>
                     <option value="<%= inst %>" <%= inst.equals(instSel) ? "selected" : "" %>><%= inst %></option>
              <%   }
                 } %>
            </select>
          </div>
        </div>
      </section>

      <!-- Organizador -->
      <section id="grupoOrganizador" data-role="organizador" style="display:none;">
        <h3>Datos de organizador</h3>
        <div class="fila-doble">
          <div class="input-group">
            <label for="nombreO">Nombre de la organización*</label>
            <input id="nombreO" name="nombreO" value="<%= val(request, "nombreO") %>">
          </div>
          <div class="input-group">
            <label for="webO">Sitio web (opcional)</label>
            <input id="webO" name="webO" type="url" value="<%= val(request, "link") %>">
          </div>
        </div>
        <div class="input-group">
          <label for="descO">Descripción*</label>
          <textarea id="descO" name="descO" rows="3"><%= val(request, "descripcion") %></textarea>
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
<script>
(function(){
  const $ = s => document.querySelector(s);
  const form = $('#altaForm');
  if (!form) return;

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

  // Inicializa vista y escucha los cambios
  const rolActual = form.querySelector('input[name="rol"]:checked')?.value || 'asistente';
applyRoleUI(rolActual);

  rolRadios.forEach(r => r.addEventListener('change', e => applyRoleUI(e.target.value)));

})();
</script>

<!-- Script -->
<!-- <script>
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
</script> -->

</body>
</html>