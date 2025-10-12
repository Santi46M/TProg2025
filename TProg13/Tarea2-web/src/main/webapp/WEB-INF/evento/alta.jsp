<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx  = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Crear Evento — Eventos.uy</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/AltaEvento.css">
  <link rel="stylesheet" href="<%=ctx%>/css/layoutMenu.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>
<body>

  <jsp:include page="/WEB-INF/templates/header.jsp" />

  <div class="container row" style="margin-top:1rem;">
    <jsp:include page="/WEB-INF/templates/menu.jsp" />

    <!-- Main -->
    <main class="container">
      <section class="form-card-altaEvento form-card--wide">
        <header class="form-header">
          <h1 class="form-title">Crear Evento</h1>
          <p class="form-subtitle">Completá los datos básicos del evento y asociá al menos una categoría.</p>
        </header>

        <%-- Mensajes --%>
        <%
          String duplicado = (String) request.getAttribute("nombreEventoDuplicado");
          boolean hayError = request.getAttribute("error") != null;
        %>
        <div class="alert <%= hayError ? "alert-error" : "hidden" %>" role="alert" aria-live="assertive">
          <i class='bx bxs-error-circle'></i>
          <span><%= hayError ? ("Ya existe un evento con el nombre " + (duplicado!=null?duplicado:"")) : "" %></span>
        </div>
        <p id="error-categorias" class="helper-error hidden" aria-live="polite"></p>

        <form id="form-alta-evento" method="post" action="<%=ctx%>/evento/alta" novalidate>
          <div class="grid-2">
            <div class="form-group-altaEvento">
              <label for="nombre">Nombre del evento <span class="req">*</span></label>
              <input type="text" id="nombre" name="nombre">
              <small class="helper">Debe ser único y descriptivo.</small>
            </div>

            <div class="form-group-altaEvento">
              <label for="sigla">Sigla <span class="req">*</span></label>
              <input type="text" id="sigla" name="sigla">
              <small class="helper">2–10 caracteres (por ejemplo, abreviatura oficial).</small>
            </div>
          </div>

          <div class="form-group-altaEvento">
            <label for="desc">Descripción <span class="req">*</span></label>
            <textarea id="desc" name="desc" rows="4"></textarea>
          </div>

          <fieldset class="form-group-altaEvento" id="fs-categorias">
            <legend>Categorías <span class="req">*</span></legend>
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
            <small class="helper">Podés elegir más de una.</small>
          </fieldset>

          <p class="form-hint-altaEvento">Los campos marcados con <span class="req">*</span> son obligatorios.</p>

          <div class="form-actions-altaEvento actions">
            <button type="submit" class="btn-guardar-altaEvento">
              <i class='bx bx-save'></i> Guardar
            </button>

            <!-- Cancelar sin href -->
            <form action="<%=ctx%>/inicio" method="get" style="display:inline">
              <button type="submit" class="btn btn-cancelar-altaEvento">
                <i class='bx bx-x-circle'></i> Cancelar
              </button>
            </form>
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
          const errorCat = document.getElementById('error-categorias');
          errorCat.textContent = 'Debe marcar al menos una categoría.';
          errorCat.classList.remove('hidden');
          return;
        }
        document.getElementById('error-categorias').classList.add('hidden');
        document.getElementById('categorias').value = sel.join(',');
      });
    })();
  </script>

</body>
</html>
