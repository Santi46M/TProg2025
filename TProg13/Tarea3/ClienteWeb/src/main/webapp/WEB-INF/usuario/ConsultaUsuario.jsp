<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*, java.net.URLEncoder, publicadores.PublicadorUsuarioService, publicadores.DtDatosUsuario, publicadores.DtDatosUsuarioArray, publicadores.DtEdicion, publicadores.DtRegistro" %>
<%
  String ctx = request.getContextPath();
  String nickSesion = (String) session.getAttribute("nick");

  Collection<DtDatosUsuario> usuarios = null;
  DtDatosUsuario usuario = null;
  Map<String, String> edicionToEvento = (Map<String, String>) request.getAttribute("edicionToEvento");
  String error = (String) request.getAttribute("error");

  Map<String,String> fotos = (Map<String,String>) request.getAttribute("fotos");
  String usrImagenUrl = (String) request.getAttribute("usrImagenUrl");

  // Try to use attributes set by servlet first
  Object attrUsuarios = request.getAttribute("usuarios");
  Object attrUsuario = request.getAttribute("usuario");
  if (attrUsuario instanceof DtDatosUsuario) {
    usuario = (DtDatosUsuario) attrUsuario;
  }

  try {
    if (attrUsuarios instanceof Collection) {
      usuarios = (Collection<DtDatosUsuario>) attrUsuarios;
    } else {
      // fetch via webservice if not provided
      PublicadorUsuarioService svc = new PublicadorUsuarioService();
      publicadores.PublicadorUsuario port = svc.getPublicadorUsuarioPort();
      DtDatosUsuarioArray arr = port.obtenerUsuariosDT();
      if (arr != null && arr.getItem() != null) {
        usuarios = new ArrayList<>();
        usuarios.addAll(arr.getItem());
      }
    }

    // if single usuario missing but nick param present, fetch
    if (usuario == null) {
      String nickParam = request.getParameter("nick");
      if (nickParam != null && !nickParam.isBlank()) {
        PublicadorUsuarioService svc2 = new PublicadorUsuarioService();
        publicadores.PublicadorUsuario port2 = svc2.getPublicadorUsuarioPort();
        try { usuario = port2.obtenerDatosUsuario(nickParam); } catch (Exception ignore) {}
      }
    }
  } catch (Exception ex) {
    ex.printStackTrace();
    if (usuarios == null) usuarios = Collections.emptyList();
  }

  // Role real del perfil consultado (lo setea el servlet)
  Boolean esPerfilOrganizadorAttr = (Boolean) request.getAttribute("esPerfilOrganizador");
  boolean esPerfilOrganizador = (esPerfilOrganizadorAttr != null)
      ? esPerfilOrganizadorAttr.booleanValue()
      : (usuario != null && (usuario.getDesc() != null || usuario.getLink() != null));

  // follow flags
  Boolean esSuPropioPerfilAttr = (Boolean) request.getAttribute("esSuPropioPerfil");
  boolean esSuPropioPerfil = (esSuPropioPerfilAttr != null) ? esSuPropioPerfilAttr.booleanValue()
                        : (usuario != null && nickSesion != null && nickSesion.equals(usuario.getNickname()));
  Boolean yaLoSigoAttr = (Boolean) request.getAttribute("yaLoSigo");
  boolean yaLoSigo = (yaLoSigoAttr != null) ? yaLoSigoAttr.booleanValue() : false;
%>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta de Usuarios — Eventos.uy</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaUsuario.css">
  <link rel="stylesheet" href="<%=ctx%>/css/layoutMenu.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
  <style>
  
	  .btn-link.user-nick {
	  display: inline-flex;
	  align-items: center;
	  gap: 0.4em;
	  background: none;
	  border: none;
	  color: #0d6efd;        /* azul del botón “Seguir” */
	  font-weight: 600;
	  font-size: 0.95rem;
	  text-decoration: none; /* 🔹 saca el subrayado */
	  padding: 0.2em 0.4em;
	  border-radius: 6px;
	  transition: color 0.2s ease, background 0.2s ease;
	}
	
	.btn-link.user-nick i {
	  font-size: 1.1em;
	  color: #0d6efd;        /* mismo azul que el texto */
	}
	
	.btn-link.user-nick:hover {
	  background: rgba(13,110,253,0.1); /* sutil azul de hover */
	  color: #0b5ed7;                   /* tono azul más oscuro */
	  cursor: pointer;
	  text-decoration: none;            /* asegura que no se subraye en hover */
	}

	  
    .usuario-card .avatar { width:72px; height:72px; border-radius:50%; object-fit:cover; background:#f3f4f6; display:block; margin-bottom:.25rem; }
    .perfil-header { display:flex; align-items:center; gap:1rem; margin-bottom:1rem; }
    .perfil-header .avatar { width:96px; height:96px; border-radius:50%; object-fit:cover; background:#f3f4f6; }
    .no-avatar {
      width:72px; height:72px; border-radius:50%;
      display:flex; align-items:center; justify-content:center;
      background:#f3f4f6; color:#6b7280; font-size:.72rem; text-align:center;
      padding:6px; box-sizing:border-box; margin-bottom:.25rem;
    }
    .perfil-header .no-avatar { width:96px; height:96px; font-size:.82rem; padding:8px; margin:0; }
    .follow-bar { display:flex; align-items:center; gap:.75rem; margin:.5rem 0; }
    .btn-link { background:none; border:none; color:#007bff; text-decoration:underline; cursor:pointer; padding:0; }
    .hint { font-size:.85rem; color:#6b7280; }
  </style>
</head>

<body>
  <!-- Header -->
  <jsp:include page="/WEB-INF/templates/header.jsp" />

  <div class="container row layout-inicio">
    <!-- Sidebar -->
    <jsp:include page="/WEB-INF/templates/menu.jsp" />

    <!-- Main -->
    <main class="main-inicio">
      <% if (error != null) { %>
        <p class="error"><%=error%></p>
      <% } %>

      <% if (usuario == null) { %>
        <!-- ===========================
             LISTADO DE USUARIOS
        ============================ -->
        <h1>Usuarios registrados</h1>
<div class="usuarios-grid">
  <% if (usuarios == null || usuarios.isEmpty()) { %>
    <p>No hay usuarios registrados.</p>
  <% } else {
       for (DtDatosUsuario u : usuarios) {
         String fotoUrl = (fotos == null) ? null : fotos.get(u.getNickname());
         if (fotoUrl == null || fotoUrl.isBlank()) {
             fotoUrl = ctx + "/img/user-default.jpg"; // imagen genérica (existe en src/main/webapp/img)
         }
  %>
    <div class="card usuario-card">
      <img class="avatar"
     src="<%= fotoUrl %>"
     alt="Avatar de <%= u.getNickname() %>"
     onerror="this.onerror=null;this.src='<%=ctx%>/img/user-default.jpg';">

      <h3>
        <form action="<%=ctx%>/usuario/ConsultaUsuario" method="get" style="display:inline;">
          <input type="hidden" name="nick" value="<%=u.getNickname()%>" />
          <button type="submit" class="btn-link user-nick">
            <i class='bx bxs-user-circle'></i>
            <span><strong>Ver Perfil</strong></span>
          </button>
        </form>
      </h3>

      <p><strong>Nickname:</strong> <%=u.getNickname()%></p>

      <%-- FOLLOW BUTTON IN LIST VIEW --%>
      <%
        @SuppressWarnings("unchecked")
        Map<String,Boolean> yaLoSigoMap = (Map<String,Boolean>) request.getAttribute("yaLoSigoMap");
        boolean yaSigueUser = false;
        if (yaLoSigoMap != null && yaLoSigoMap.get(u.getNickname()) != null) {
          yaSigueUser = yaLoSigoMap.get(u.getNickname()).booleanValue();
        }
      %>
      <% if (nickSesion != null && !nickSesion.equals(u.getNickname())) { %>
        <form class="follow-form" data-nick="<%= u.getNickname() %>"
              action="<%= ctx %>/<%= (!yaSigueUser ? "usuario/seguir" : "usuario/dejarSeguir") %>"
              method="post"
              style="display:inline;margin-top:.5rem;">
          <input type="hidden" name="a" value="<%= u.getNickname() %>">
          <input type="hidden" name="listar" value="1">
          <button type="submit" class="btn"><%= (!yaSigueUser ? "Seguir" : "Dejar de seguir") %></button>
        </form>
      <% } %>

    </div>
  <% } } %>
</div>



      <% } else { %>
        <!-- ===========================
             PERFIL INDIVIDUAL
        ============================ -->
        <h1>Perfil de <%= usuario.getNickname() %></h1>

<div class="perfil-header">
  <% 
    // Prefer the image name stored in the DtDatosUsuario. If missing, use the generic avatar.
    String imgName = (usuario != null) ? usuario.getImagen() : null;
    String avatarSrc;
    if (imgName != null && !imgName.isBlank()) {
      avatarSrc = ctx + "/img/usuarios/" + imgName;
    } else {
      avatarSrc = ctx + "/img/user-default.jpg";
    }
  %>
  <img class="avatar" src="<%= avatarSrc %>" alt="Avatar de <%= usuario.getNickname() %>" onerror="this.onerror=null;this.src='<%=ctx%>/img/user-default.jpg';">
   <div id="datosUsuario">
      <!-- === BARRA SEGUIR / DEJAR SEGUIR (FORM SEPARADO, NO ANIDADO) === -->
    <div class="follow-bar" style="margin:.5rem 0;">
      <% if (!esSuPropioPerfil && nickSesion != null) { %>
        <form class="follow-form" data-nick="<%= usuario.getNickname() %>" action="<%= ctx %>/<%= (!yaLoSigo ? "usuario/seguir" : "usuario/dejarSeguir") %>" method="post" style="display:inline;">
          <input type="hidden" name="a" value="<%= usuario.getNickname() %>">
          <button type="submit" class="btn"><%= (!yaLoSigo ? "Seguir" : "Siguiendo") %></button>
        </form>
      <% } %>
    </div>
    
    <p><strong>Nickname:</strong> <span><%= usuario.getNickname() %></span></p>
    <p><strong>Email:</strong> <span><%= usuario.getEmail()%></span></p>



    <!-- === FORM DE EDICIÓN (SEPARADO) === -->
    <form id="formEditarUsuario" action="<%= ctx %>/usuario/modificar" method="post" enctype="multipart/form-data">
      <input type="hidden" name="nick" value="<%= usuario.getNickname() %>">

              <!-- Campo de imagen (en edición) -->
              <div class="edit-mode" style="display:none; margin:.25rem 0;">
                <label for="imagen"><strong>Foto:</strong></label>
                <input type="file" id="imagen" name="imagen" accept="image/*">
                <div class="hint">Formatos: JPG/PNG/WebP. Tamaño máx: 5 MB.</div>
              </div>

              <% if (!esPerfilOrganizador) { %>
                <!-- 🔹 CAMPOS DE ASISTENTE -->
                <p><strong>Nombre:</strong>
                  <span class="view-mode"><%= usuario.getNombre() %></span>
                  <input class="edit-mode" type="text" name="nombre" value="<%= usuario.getNombre() %>" style="display:none;">
                </p>

                <p><strong>Apellido:</strong>
                  <span class="view-mode"><%= usuario.getApellido() %></span>
                  <input class="edit-mode" type="text" name="apellido" value="<%= usuario.getApellido() %>" style="display:none;">
                </p>

<%
  String fn = (usuario.getFechaNac() != null) ? usuario.getFechaNac() : "";
%>
<p><strong>Fecha de nacimiento:</strong>
  <span class="view-mode"><%= fn.isEmpty() ? "—" : fn %></span>
  <input class="edit-mode" type="date" name="fechaNac"
         value="<%= fn %>" style="display:none;">
</p>

                <p><strong>Institución:</strong>
                  <span class="view-mode">
                    <% String instName = (usuario.getNombreInstitucion() != null ? usuario.getNombreInstitucion() : null);
                       Map<String,String> instFotos = (Map<String,String>) request.getAttribute("instFotos");
                       String instImg = (instFotos != null && instName != null) ? instFotos.get(instName) : null;
                    %>
                    <% if (instName == null) { %>
                      — 
                    <% } else { %>
                      <span style="display:inline-flex;align-items:center;gap:0.4em;">
                        <% if (instImg != null) { %>
                          <img src="<%= instImg %>" alt="Logo <%= instName %>" style="width:24px;height:24px;object-fit:cover;border-radius:4px;vertical-align:middle;" />
                        <% } %>
                        <span><%= instName %></span>
                      </span>
                    <% } %>
                  </span>
                   <select class="edit-mode" name="institucion" style="display:none;">
                     <option value="">— Seleccione —</option>
                     <%
                       Collection<String> instituciones = (Collection<String>) request.getAttribute("instituciones");
                       if (instituciones != null) {
                         for (String inst : instituciones) {
                           String selected = (usuario.getNombreInstitucion() != null && usuario.getNombreInstitucion().equals(inst)) ? "selected" : "";
                     %>
                         <option value="<%= inst %>" <%= selected %>><%= inst %></option>
                     <% } } %>
                   </select>
                 </p>

              <% } else { %>
                <!-- 🔹 CAMPOS DE ORGANIZADOR -->
                <p><strong>Descripción:</strong>
                  <span class="view-mode"><%= usuario.getDesc() != null ? usuario.getDesc() : "—" %></span>
                  <textarea class="edit-mode" name="descripcion" rows="3" style="display:none;"><%= usuario.getDesc() %></textarea>
                </p>

                <p><strong>Link:</strong>
                  <span class="view-mode"><%= usuario.getLink() != null ? usuario.getLink() : "—" %></span>
                  <input class="edit-mode" type="text" name="link" value="<%= usuario.getLink() != null ? usuario.getLink() : "" %>" style="display:none;">
                </p>
              <% } %>

              <% boolean esSuPropioPerfil2 = esSuPropioPerfil; %>
              <% if (esSuPropioPerfil2) { %>
                <p><strong>Contraseña:</strong>
                  <span class="view-mode">••••••••</span>
                  <input class="edit-mode" type="password" name="password" placeholder="Nueva contraseña" style="display:none;">
                </p>

                <div id="accionesPerfil" style="margin-top:1rem;">
                  <button type="button" id="btnEditar" class="btn">Modificar datos</button>
                  <button type="submit" id="btnGuardar" class="btn" style="display:none;">Guardar</button>
                  <button type="button" id="btnCancelar" class="btn" style="display:none;">Cancelar</button>
                </div>
              <% } %>
            </form>
          </div>
        </div>

        <% boolean tieneEdiciones = usuario.getEdiciones() != null && !usuario.getEdiciones().getEdicion().isEmpty();
           boolean tieneRegistros = usuario.getRegistros() != null && !usuario.getRegistros().getRegistro().isEmpty();
        %>

        <% if (tieneEdiciones) { %>
          <h2>Ediciones de eventos</h2>
          <ul class="lista-ediciones">
            <% for (DtEdicion e : usuario.getEdiciones().getEdicion()) {
                 String estado = (e.getEstado() != null) ? e.getEstado().toString() : "Sin estado";
                 boolean mostrar = "Aceptada".equalsIgnoreCase(estado) ||
                                   (esSuPropioPerfil && ("Ingresada".equalsIgnoreCase(estado) || "Rechazada".equalsIgnoreCase(estado)));
                 if (mostrar) {
                	 String eventoNombre = "";
                	 if (edicionToEvento != null && edicionToEvento.get(e.getNombre()) != null) {
                	     eventoNombre = edicionToEvento.get(e.getNombre());
                	 } else if (e.getEvento() != null) { 
                	     eventoNombre = e.getEvento().getNombre();
                	 }

            %>
              <li>
                <form action="<%= ctx %>/edicion/ConsultaEdicion" method="get" style="display:inline;">
                  <input type="hidden" name="evento" value="<%= eventoNombre %>" />
                  <input type="hidden" name="edicion" value="<%= e.getNombre() %>" />
                  <button type="submit" class="btn-link">
                    <strong><%= e.getNombre() %></strong>
                  </button>
                </form>
                <span>(<%= e.getFechaInicio() %> - <%= e.getFechaFin() %>)</span>
                <em class="estado"> — <%= estado %></em>
              </li>
            <% } } %>
          </ul>
        <% } %>

        <% if (esSuPropioPerfil && tieneRegistros) { %>
          <h2>Eventos registrados</h2>
          <ul class="lista-eventos">
            <% for (DtRegistro r : usuario.getRegistros().getRegistro()) {
                 String eventoNombre = (edicionToEvento != null) ? edicionToEvento.get(r.getEdicion()) : "";
            %>
              <li>
                <form action="<%= ctx %>/edicion/ConsultaEdicion" method="get" style="display:inline;">
                  <input type="hidden" name="evento" value="<%= eventoNombre %>" />
                  <input type="hidden" name="edicion" value="<%= r.getEdicion() %>" />
                  <button type="submit" class="btn-link">
                    <strong><%= r.getEdicion() %></strong>
                  </button>
                </form>
                <span>| <strong>Tipo:</strong> <%= r.getTipoRegistro() %></span>
                <span>| <strong>Fecha registro:</strong> <%= r.getFechaRegistro() %></span>
                <span>| <strong>Costo:</strong> $<%= r.getCosto() %></span>
                <a class="btn" href="<%= ctx %>/registro/ConsultaRegistroEdicion?idRegistro=<%= URLEncoder.encode(r.getIdentificador(), "UTF-8") %>">Ver detalles</a>
              </li>
            <% } %>
          </ul>
        <% } %>
      <% } %>
    </main>
  </div>

  <script>
    const btnEditar = document.getElementById("btnEditar");
    const btnGuardar = document.getElementById("btnGuardar");
    const btnCancelar = document.getElementById("btnCancelar");

    if (btnEditar) {
      btnEditar.addEventListener("click", () => {
        document.querySelectorAll(".view-mode").forEach(e => e.style.display = "none");
        document.querySelectorAll(".edit-mode").forEach(e => e.style.display = "inline");
        btnEditar.style.display = "none";
        btnGuardar.style.display = "inline";
        btnCancelar.style.display = "inline";
      });

      btnCancelar.addEventListener("click", () => {
        document.querySelectorAll(".view-mode").forEach(e => e.style.display = "inline");
        document.querySelectorAll(".edit-mode").forEach(e => e.style.display = "none");
        btnEditar.style.display = "inline";
        btnGuardar.style.display = "none";
        btnCancelar.style.display = "none";
        const fi = document.getElementById("imagen");
        if (fi) fi.value = "";
      });
    }

    // AJAX follow/unfollow handler: prevents navigation and toggles button state.
    (function(){
      function isFollowAction(action){ return action && action.endsWith('/seguir'); }
      document.querySelectorAll('form.follow-form').forEach(form => {
        form.addEventListener('submit', async function(evt){
          // progressive enhancement: if JS available, handle via fetch; otherwise fallback to normal submit
          evt.preventDefault();
          const btn = form.querySelector('button[type="submit"]');
          if (!btn) return;
          // disable while processing
          btn.disabled = true;
          const formData = new FormData(form);
          try {
            const resp = await fetch(form.action, {
              method: 'POST',
              headers: { 'X-Requested-With': 'XMLHttpRequest' },
              body: formData,
              credentials: 'same-origin'
            });
            if (!resp.ok) throw new Error('HTTP ' + resp.status);
            const text = (await resp.text()).trim();
            if (text === 'OK') {
              // toggle UI: swap label and action
              const currentlyFollow = isFollowAction(form.action);
              if (currentlyFollow) {
                btn.textContent = 'Dejar de seguir';
                form.action = form.action.replace('/seguir', '/dejarSeguir');
              } else {
                btn.textContent = 'Seguir';
                form.action = form.action.replace('/dejarSeguir', '/seguir');
              }
            } else {
              // server returned something else; try to toggle conservatively
              const currentlyFollow = isFollowAction(form.action);
              if (currentlyFollow) {
                btn.textContent = 'Dejar de seguir';
                form.action = form.action.replace('/seguir', '/dejarSeguir');
              } else {
                btn.textContent = 'Seguir';
                form.action = form.action.replace('/dejarSeguir', '/seguir');
              }
            }
          } catch (err) {
            console.error('Follow error', err);
            alert('Error procesando la acción. Intente de nuevo.');
          } finally {
            btn.disabled = false;
          }
        });
      });

      // Revalidate follow-state when the page is shown (back/restore from bfcache)
      var ctx = "<%=ctx%>";
      window.addEventListener('pageshow', function(evt){
        try {
          var forms = Array.from(document.querySelectorAll('form.follow-form'));
          if (!forms || forms.length === 0) return;
          var nicks = forms.map(f => f.dataset.nick).filter(Boolean);
          if (nicks.length === 0) return;
          var url = ctx + '/usuario/ConsultaUsuario?checkSeguidos=1&nicks=' + encodeURIComponent(nicks.join(',')) + '&_=' + Date.now();
          fetch(url, { credentials: 'same-origin', cache: 'no-store' })
            .then(function(r){ if (!r.ok) throw new Error('HTTP ' + r.status); return r.json(); })
            .then(function(obj){
              forms.forEach(function(form){
                var nick = form.dataset.nick;
                if (!nick) return;
                var btn = form.querySelector('button[type="submit"]');
                var sigue = !!obj[nick];
                if (btn) {
                  if (sigue) {
                    btn.textContent = 'Dejar de seguir';
                    form.action = form.action.replace('/seguir','/dejarSeguir');
                    btn.classList.add('following'); btn.classList.remove('not-following');
                  } else {
                    btn.textContent = 'Seguir';
                    form.action = form.action.replace('/dejarSeguir','/seguir');
                    btn.classList.add('not-following'); btn.classList.remove('following');
                  }
                }
              });
            })
            .catch(function(err){ console.debug('Revalidate follow state failed:', err); });
        } catch (e) { console.debug('pageshow handler error', e); }
      });

    })();
  </script>
</body>
</html>