<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx = request.getContextPath();
  String error = (String) request.getAttribute("error");
  //obtenemos el usuario logueado 
  String nick = (String) session.getAttribute("nick");
%>

<header class="site-header">
  <div class="container">

    <a class="brand" href="<%=ctx%>/inicio">Eventos.uy</a>

    <nav class="main-nav">
      <form class="search" action="<%=ctx%>/buscar" method="get" role="search" aria-label="Buscar">
        <input class="search-input" type="search" name="q" placeholder="Eventos, Ediciones">
        <button class="btn" type="submit">Buscar</button>
      </form>
    </nav>

    <% if (nick != null) { %>
      <!-- Usuario logueado -->
      <nav class="user-nav" id="userNav">
        <span class="user-name">Hola, <strong><%= nick %></strong></span>
        <form action="<%=ctx%>/usuario/ConsultaUsuario" method="get">
          <input type="hidden" name="nick" value="<%= nick %>">
          <button type="submit" class="btn">Ver Perfil</button>
        </form>
        <form action="<%=ctx%>/auth/logout" method="get">
          <button type="submit" class="btn">
            Cerrar sesión
          </button>
        </form>
      </nav>
    <% } else { %>
      <!-- Visitante -->
      <nav class="user-nav" id="userNav">
         <form action="<%=ctx%>/auth/login" method="get">
			<button type="submit" class="btn">
				 Iniciar Sesión
			</button>
		</form>
        <form action="<%=ctx%>/usuario/AltaUsuario" method="get">
			<button type="submit" class="btn">
				 Registrarse
			</button>
		</form>
      </nav>
    <% } %>
  </div>
</header>