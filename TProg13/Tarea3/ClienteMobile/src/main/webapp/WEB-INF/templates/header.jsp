<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx = request.getContextPath();
  String error = (String) request.getAttribute("error");
  //obtenemos el usuario logueado 
  String nick = (String) session.getAttribute("nick");
%>

<header class="site-header">
  <div class="container d-flex justify-content-between align-items-center">
    <span class="brand">Eventos.uy</span>
    <!-- Hamburger menu for mobile -->
    <div class="d-md-none position-relative" style="flex:1;">
      <button class="btn btn-outline-secondary" type="button" id="menuHamburguesa" aria-label="Menú" style="width:56px;height:56px;">
        <i class="bx bx-menu" style="font-size:2.5rem;"></i>
      </button>
      <div id="menuOpciones" class="bg-white border rounded shadow-lg mt-2" style="display:none; position:absolute; top:60px; left:0; right:0; z-index:1000; min-width:220px; width:90vw; max-width:400px;">
        <ul class="list-unstyled mb-0 py-3">
          <li><a class="dropdown-item fs-4 py-3" href="<%=ctx%>/evento/listado">Consulta Edición</a></li>
          <li><a class="dropdown-item fs-4 py-3" href="<%=ctx%>/usuario/edicionesRegistradas">Consulta Registro</a></li>
          <li><a class="dropdown-item fs-4 py-3" href="<%=ctx%>/usuario/listarRegistros">Registro Asistencia a Edición</a></li>
        </ul>
      </div>
      <script>
        const btn = document.getElementById('menuHamburguesa');
        const menu = document.getElementById('menuOpciones');
        btn.addEventListener('click', function(e) {
          e.stopPropagation();
          menu.style.display = (menu.style.display === 'none' || menu.style.display === '') ? 'block' : 'none';
        });
        document.addEventListener('click', function(e) {
          if (!btn.contains(e.target) && !menu.contains(e.target)) {
            menu.style.display = 'none';
          }
        });
      </script>
    </div>
    <% if (nick != null) { %>
      <!-- Usuario logueado -->
      <nav class="user-nav d-none d-md-flex" id="userNav">
        <form action="<%=ctx%>/auth/logout" method="get">
          <button type="submit" class="btn">Cerrar sesión</button>
        </form>
      </nav>
    <% } else { %>
      <!-- Visitante -->
      <nav class="user-nav d-none d-md-flex" id="userNav">
         <form action="<%=ctx%>/auth/login" method="get">
			<button type="submit" class="btn">Iniciar Sesión</button>
		</form>
        <form action="<%=ctx%>/usuario/AltaUsuario" method="get">
			<button type="submit" class="btn">Registrarse</button>
		</form>
      </nav>
    <% } %>
  </div>
</header>