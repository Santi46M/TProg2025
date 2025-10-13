<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*,logica.datatypes.DTEvento,logica.clases.Ediciones,logica.clases.TipoRegistro" %>
<%
  String ctx = request.getContextPath();

  List<DTEvento> eventos = (List<DTEvento>) request.getAttribute("eventos");
  Map<String, List<Ediciones>> mapa = (Map<String, List<Ediciones>>) request.getAttribute("edicionesPorEvento");
  Ediciones edSel = (Ediciones) request.getAttribute("edicionSeleccionada");

  String eventoSel = request.getParameter("evento");         // nombre del evento (GET)
  String edicionSel = request.getParameter("edicion");       // nombre de edición (GET)
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"/>
  <title>Registrarse a una Edición de Evento</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css"/>
  <link rel="stylesheet" href="../../css/RegistroEdicionEvento.css">
<style>
.form-card-registroEdicionEvento,.event-card{background:#fff;border:1px solid var(--line,#ddd);border-radius:10px;padding:1.2rem 1.5rem;box-shadow:0 2px 6px rgba(0,0,0,0.05);margin-bottom:.8rem}
.form-card-registroEdicionEvento h2,.event-card h3{color:#111;margin:0 0 .75rem;font-weight:700;font-size:1.2rem}
.form-group-registroEdicionEvento label{display:block;font-weight:600;color:#333;margin-bottom:.25rem}
.form-group-registroEdicionEvento select,.form-group-registroEdicionEvento input{width:100%;padding:.45rem .7rem;border:1px solid #ccc;border-radius:6px;font-size:.95rem;outline:none;transition:border-color .2s,box-shadow .2s}
.form-group-registroEdicionEvento select:focus,.form-group-registroEdicionEvento input:focus{border-color:#0b5ed7;box-shadow:0 0 0 2px rgba(11,94,215,.15)}
#label-tipo{font-weight:600;margin:.75rem 0 .4rem}
.event-card label input[type=radio]{accent-color:#0b5ed7;margin-right:.25rem}
.event-card label span{font-size:.95rem}
.btn-guardar-registroEdicionEvento{background:#0b5ed7;color:#fff;border:none;padding:.6rem 1.2rem;border-radius:8px;font-weight:600;font-size:.95rem;cursor:pointer;transition:background-color .2s,transform .1s,box-shadow .2s}
.btn-guardar-registroEdicionEvento:hover{background:#084bb1;box-shadow:0 3px 8px rgba(11,94,215,.25);transform:translateY(-1px)}
.btn-guardar-registroEdicionEvento:active{transform:translateY(1px);box-shadow:none}
small{color:#555;font-size:.85rem}
p[style*="color:#c00"]{background:#ffecec;border:1px solid #ffb3b3;border-radius:6px;padding:.5rem 1rem;font-weight:500;color:#a00!important;margin-bottom:1rem}
</style>
</head>
<body>

<jsp:include page="/WEB-INF/templates/header.jsp" />

<div class="container row" style="margin-top:1rem; display:flex; align-items:flex-start;">
  <jsp:include page="/WEB-INF/templates/menu.jsp" />

  <main class="container" style="flex:2; min-width:0;">

    <% if (request.getAttribute("error") != null) { %>
      <p style="color:#c00; font-weight:600"><%= request.getAttribute("error") %></p>
    <% } %>

    <!-- === PASO 1: Selección (GET) - JS solo para poblar ediciones === -->
    <section class="form-card-registroEdicionEvento">
      <h2>Registrarse a una Edición de Evento</h2>

      <form id="formSelección" action="<%=ctx%>/registro/inscripcion" method="get">
        <div class="form-group-registroEdicionEvento">
          <label for="selectEvento">Evento <span style="color:red">*</span></label>
          <select id="selectEvento" name="evento" required>
            <option value="">-- Seleccione un evento --</option>
            <% if (eventos != null) {
                 for (DTEvento ev : eventos) {
                   String sel = (ev.getNombre().equals(eventoSel)) ? "selected" : "";
            %>
              <option value="<%=ev.getNombre()%>" <%=sel%>><%=ev.getNombre()%></option>
            <% } } %>
          </select>
        </div>

        <div class="form-group-registroEdicionEvento">
          <label for="selectEdicion">Edición aceptada <span style="color:red">*</span></label>
          <select id="selectEdicion" name="edicion" required <%= (eventoSel==null||eventoSel.isBlank())?"disabled":"" %>>
            <option value="">-- Seleccione primero un evento --</option>
          </select>
        </div>
      </form>
    </section>

    <!-- === PASO 2: Detalle de la edición (SIN JS) y elección de tipo (POST) === -->
    <% if (edSel != null) { %>
      <section class="event-card" style="margin-top:1rem">
        <h3 style="margin-bottom:.5rem">Datos de la edición</h3>
        <ul>
          <li><strong>Evento:</strong> <%= edSel.getEvento().getNombre() %></li>
          <li><strong>Edición:</strong> <%= edSel.getNombre() %></li>
          <li><strong>Sigla:</strong> <%= edSel.getSigla() %></li>
          <li><strong>Fecha inicio:</strong> <%= edSel.getFechaInicio() %></li>
          <li><strong>Fecha fin:</strong> <%= edSel.getFechaFin() %></li>
          <li><strong>Ciudad:</strong> <%= edSel.getCiudad() %></li>
          <li><strong>País:</strong> <%= edSel.getPais() %></li>
        </ul>

        <h3 style="margin:.75rem 0 .25rem">Tipos de registro</h3>
        <%
          Collection<TipoRegistro> tipos = edSel.getTiposRegistro();
          boolean hayTipos = (tipos != null && !tipos.isEmpty());
        %>
        <% if (!hayTipos) { %>
          <p>No hay tipos de registro disponibles para esta edición.</p>
        <% } else { %>
          <form action="<%=ctx%>/registro/inscripcion" method="post" id="formInscripcion" style="margin-top:.5rem">
            <!-- Datos fijos para el POST -->
            <input type="hidden" name="evento"  value="<%= edSel.getEvento().getNombre() %>"/>
            <input type="hidden" name="edicion" value="<%= edSel.getNombre() %>"/>

            <div role="group" aria-labelledby="label-tipo">
              <p id="label-tipo" style="margin:.25rem 0 .5rem"><strong>Seleccione un tipo:</strong></p>
              <% for (TipoRegistro tr : tipos) { %>
                <label style="display:block; margin:.25rem 0;">
                  <input type="radio" name="tipo" value="<%= tr.getNombre() %>" required/>
                  <span style="margin-left:.25rem">
                    <strong><%= tr.getNombre() %></strong>
                    — <em><%= tr.getDescripcion() %></em>
                    — $<%= tr.getCosto() %>
                  </span>
                </label>
              <% } %>
            </div>

            <div class="form-group-registroEdicionEvento" style="margin-top:.75rem">
              <label for="codigoPatrocinio">Código de patrocinio (opcional)</label>
              <input id="codigoPatrocinio" name="codigoPatrocinio" placeholder="Ej: CORREANTEL"/>
              <small>Si es válido, el costo será $0.</small>
            </div>

            <div class="form-actions-registroEdicionEvento" style="margin-top:1rem">
              <button type="submit" class="btn-guardar-registroEdicionEvento">Confirmar inscripción</button>
            </div>
          </form>
        <% } %>
      </section>
    <% } %>
  </main>
</div>

<script>
  // ===== Datos para poblar ediciones por evento (solo JS aquí) =====
  const data = {
    <% if (mapa != null && !mapa.isEmpty()) {
         int ecount = 0;
         for (Map.Entry<String, List<Ediciones>> entry : mapa.entrySet()) {
           String evName = entry.getKey().replace("\"","\\\"");
           List<Ediciones> list = entry.getValue();
           if (list == null || list.isEmpty()) continue;
           if (ecount++ > 0) out.print(",");
           out.print("\""+evName+"\":[");
           for (int i=0;i<list.size();i++){
             Ediciones ed = list.get(i);
             String nom = (ed.getNombre()==null)?"":ed.getNombre().replace("\"","\\\"");
             String sig = (ed.getSigla()==null)?"":ed.getSigla().replace("\"","\\\"");
             out.print("{\"nombre\":\""+nom+"\",\"sigla\":\""+sig+"\"}");
             if (i<list.size()-1) out.print(",");
           }
           out.print("]");
         }
       } %>
  };

  const selEvento  = document.getElementById("selectEvento");
  const selEdicion = document.getElementById("selectEdicion");
  const formSel    = document.getElementById("formSelección");

  function renderEdiciones(nombreEvento, preselect) {
    selEdicion.innerHTML = "";
    const eds = data[nombreEvento] || [];
    if (!eds.length) {
      selEdicion.innerHTML = "<option value=''>-- No hay ediciones aceptadas --</option>";
      selEdicion.disabled = true;
      return;
    }
    selEdicion.disabled = false;
    selEdicion.insertAdjacentHTML("beforeend","<option value=''>-- Seleccione una edición --</option>");
    eds.forEach(ed => {
      const opt = document.createElement("option");
      opt.value = ed.nombre;                          // enviamos el NOMBRE de la edición en el GET
      opt.textContent = ed.nombre;
      if (preselect && preselect === ed.nombre) opt.selected = true;
      selEdicion.appendChild(opt);
    });
  }

  // Cambiar evento → poblar ediciones (sin enviar)
  selEvento.addEventListener("change", () => {
    renderEdiciones(selEvento.value, null);
  });

  // Cambiar edición → enviar GET (sin más JS después)
  selEdicion.addEventListener("change", () => {
    if (selEvento.value && selEdicion.value) formSel.submit();
  });

  // Estado inicial (si venimos con parámetros)
  (function init(){
    const ev = "<%= (eventoSel==null?"":eventoSel.replace("\"","\\\"")) %>";
    const ed = "<%= (edicionSel==null?"":edicionSel.replace("\"","\\\"")) %>";
    if (ev) renderEdiciones(ev, ed);
  })();
</script>

</body>
</html>