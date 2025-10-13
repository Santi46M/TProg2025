<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*,logica.datatypes.DTEvento,logica.clases.Ediciones,logica.enumerados.DTEstado" %>
<%
  String ctx = request.getContextPath();
  List<DTEvento> eventos = (List<DTEvento>) request.getAttribute("eventos");
  List<Ediciones> ediciones = (List<Ediciones>) request.getAttribute("ediciones");
  Map<String, List<Ediciones>> mapa = (Map<String, List<Ediciones>>) request.getAttribute("edicionesPorEvento");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Registrarse a Edición de Evento — Eventos.uy</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/RegistroEdicionEvento.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
</head>
<body>

<jsp:include page="/WEB-INF/templates/header.jsp" />
<div class="container row" style="margin-top:1rem; display:flex; align-items:flex-start;">
  <jsp:include page="/WEB-INF/templates/menu.jsp" />
  <main class="container" style="flex:2; min-width:0;">
    <section class="form-card-registroEdicionEvento">
      <h2>Registrarse a Edición de Evento</h2>
      <% if (request.getAttribute("error") != null) { %>
        <p style="color:#c00"><%= request.getAttribute("error") %></p>
      <% } %>
      <form action="<%=ctx%>/registro/inscripcion" method="post" id="form-registro-edicion">
        <div class="form-group-registroEdicionEvento">
          <label>Evento <span style="color:red">*</span></label>
          <select id="selectEvento" name="evento" required>
            <option value="">-- Seleccione un evento --</option>
            <% if (eventos != null) {
                 for (DTEvento ev : eventos) { %>
                   <option value="<%= ev.getNombre() %>"><%= ev.getNombre() %></option>
            <%   }
               } %>
          </select>
        </div>
        <div class="form-group-registroEdicionEvento">
          <label>Edición aceptada <span style="color:red">*</span></label>
          <select id="selectEdicion" name="edicion" required disabled>
            <option value="">-- Seleccione primero un evento --</option>
          </select>
        </div>
        <div class="form-actions-registroEdicionEvento">
          <button type="submit" class="btn-guardar-registroEdicionEvento">Registrarse</button>
          <button type="submit" class="btn-cancelar-registroEdicionEvento" name="accion" value="cancelar">Cancelar</button>
        </div>
      </form>
      <script>
        document.querySelector('.btn-cancelar-registroEdicionEvento').addEventListener('click', function(e) {
          var form = document.getElementById('form-registro-edicion');
          Array.from(form.querySelectorAll('[required]')).forEach(function(input) {
            input.removeAttribute('required');
          });
        });
      </script>
    </section>
  </main>
</div>

<script>
  const data = {
  <% if (mapa != null && !mapa.isEmpty()) {
       int ecount = 0;
       for (Map.Entry<String, List<Ediciones>> entry : mapa.entrySet()) {
         String evName = entry.getKey() == null ? "" : entry.getKey().replace("\"","\\\"");
         List<Ediciones> list = entry.getValue();
         if (list == null || list.isEmpty()) continue;
         if (ecount++ > 0) out.print(",");
         out.print("\"" + evName + "\":[");
         for (int i = 0; i < list.size(); i++) {
           Ediciones ed = list.get(i);
           String sigla = ed.getSigla() == null ? "" : ed.getSigla().replace("\"","\\\"");
           String nombre = ed.getNombre() == null ? "" : ed.getNombre().replace("\"","\\\"");
           out.print("{\"sigla\":\"" + sigla + "\",\"nombre\":\"" + nombre + "\"}");
           if (i < list.size() - 1) out.print(",");
         }
         out.print("]");
       }
     } %>
  };

  const selectEvento = document.getElementById("selectEvento");
  const selectEdicion = document.getElementById("selectEdicion");

  selectEvento.addEventListener("change", function() {
    const nombreEv = this.value;
    const eds = data[nombreEv] || [];
    selectEdicion.innerHTML = "";
    if (eds.length === 0) {
      selectEdicion.innerHTML = "<option value=''>-- No hay ediciones aceptadas --</option>";
      selectEdicion.disabled = true;
    } else {
      selectEdicion.innerHTML = "<option value=''>-- Seleccione una edición --</option>";
      eds.forEach(function(ed) {
        const opt = document.createElement("option");
        opt.value = ed.sigla;     // se envía 'sigla' al POST
        opt.textContent = ed.nombre;
        selectEdicion.appendChild(opt);
      });
      selectEdicion.disabled = false;
    }
  });
</script>

</body>
</html>