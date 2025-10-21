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
  <link rel="stylesheet" href="<%=ctx%>/css/AltaEvento.css">
  <link rel="stylesheet" href="<%=ctx%>/css/layoutMenu.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
  <style>
    main.container { display: flex; justify-content: center; }
    .form-card-altaEvento.form-card--wide { max-width: 880px; width: 100%; margin: 0 auto; }
    .helper-note { color:#555; font-size:.9rem; }
  </style>
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp" />

<div class="container row" style="margin-top:1rem;">
  <jsp:include page="/WEB-INF/templates/menu.jsp" />

  <main class="container">
    <section class="form-card-altaEvento form-card--wide">
      <header class="form-header">
        <h1 class="form-title">Crear Edición de Evento</h1>
        <p class="form-subtitle">Completá los datos de la edición. Todos los campos son obligatorios salvo la imagen.</p>
      </header>

      <%-- Mensajes --%>
      <div class="alert <%= request.getAttribute("error") != null ? "alert-error" : "hidden" %>" role="alert" aria-live="assertive">
        <i class='bx bxs-error-circle'></i>
        <span><%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %></span>
      </div>
      <% if (request.getAttribute("sinEventos") != null && (Boolean)request.getAttribute("sinEventos")) { %>
        <div class="alert alert-error" role="alert" aria-live="assertive">
          <i class='bx bxs-error-circle'></i>
          <span>No hay eventos disponibles. Debe crear un evento antes de poder crear una edición.</span>
        </div>
      <% } %>
      <form id="form-alta-edicion" method="post" action="<%=ctx%>/edicion/alta" enctype="multipart/form-data" novalidate>
        <div class="grid-2">
          <div class="form-group-altaEvento">
            <label for="evento">Evento <span class="req">*</span></label>
            <select id="evento" name="evento" required <%= (request.getAttribute("sinEventos") != null && (Boolean)request.getAttribute("sinEventos")) ? "disabled" : "" %>>
              <option value="">Seleccione un evento</option>
              <% 
                java.util.List eventos = (java.util.List) request.getAttribute("listaEventos");
                if (eventos != null) {
                  for (Object obj : eventos) {
                    String nombre = null;
                    try {
                      java.lang.reflect.Method m = obj.getClass().getMethod("getNombre");
                      Object v = m.invoke(obj);
                      nombre = v == null ? "" : v.toString();
                    } catch (Exception e) { nombre = obj.toString(); }
              %>
                <option value="<%=nombre%>"><%=nombre%></option>
              <%   }
                }
              %>
            </select>
            <small class="helper">Debe seleccionar un evento existente.</small>
          </div>
          <div class="form-group-altaEvento">
            <label for="nombre">Nombre de la edición <span class="req">*</span></label>
            <input type="text" id="nombre" name="nombre" required>
            <small class="helper">Debe ser único y descriptivo.</small>
          </div>
        </div>
        <div class="form-group-altaEvento">
          <label for="desc">Sigla <span class="req">*</span></label>
          <input type="text" id="desc" name="desc" required>
          <small class="helper">2–10 caracteres (abreviatura oficial).</small>
        </div>
        <div class="grid-2">
          <div class="form-group-altaEvento">
            <label for="fechaInicio">Fecha de inicio <span class="req">*</span></label>
            <input type="date" id="fechaInicio" name="fechaInicio" required>
          </div>
          <div class="form-group-altaEvento">
            <label for="fechaFin">Fecha de fin <span class="req">*</span></label>
            <input type="date" id="fechaFin" name="fechaFin" required>
          </div>
        </div>
        <div class="grid-2">
          <div class="form-group-altaEvento">
            <label for="ciudad">Ciudad <span class="req">*</span></label>
            <input type="text" id="ciudad" name="ciudad" required>
          </div>
          <div class="form-group-altaEvento">
            <label for="pais">País <span class="req">*</span></label>
            <input type="text" id="pais" name="pais" required>
          </div>
        </div>
        <div class="form-group-altaEvento">
          <label for="imagen">Imagen (opcional)</label>
          <input type="file" id="imagen" name="imagen" accept="image/*">
          <small class="helper-note">Formatos sugeridos: JPG/PNG. Tamaño máx. 2&nbsp;MB.</small>
        </div>
        <p class="form-hint-altaEvento">Los campos marcados con <span class="req">*</span> son obligatorios.</p>
        <div class="form-actions-altaEvento actions">
          <button type="submit" class="btn-guardar-altaEvento" <%= (request.getAttribute("sinEventos") != null && (Boolean)request.getAttribute("sinEventos")) ? "disabled" : "" %>>
            <i class='bx bx-save'></i> Guardar
          </button>
          <button type="submit" class="btn-cancelar-altaEvento btn" name="accion" value="cancelar"> <i class='bx bx-x-circle'></i> Cancelar</button>
        </div>
      </form>
      <script>
        (function () {
          const form = document.getElementById('form-alta-edicion');
          const cancelarBtn = document.querySelector('.btn-cancelar-altaEvento');
          cancelarBtn.addEventListener('click', function(e) {
            Array.from(form.querySelectorAll('[required]')).forEach(function(input) {
              input.removeAttribute('required');
            });
          });
          // verificación básica de tamaño de imagen 
          const inputImg = document.getElementById('imagen');
          const MAX_BYTES = 2 * 1024 * 1024; // 2MB
          inputImg.addEventListener('change', function () {
            const file = this.files && this.files[0];
            if (file && file.size > MAX_BYTES) {
              alert('La imagen supera 2 MB. Elegí un archivo más liviano.');
              this.value = '';
            }
          });
        })();
      </script>
    </section>
  </main>
</div>

</body>
</html>