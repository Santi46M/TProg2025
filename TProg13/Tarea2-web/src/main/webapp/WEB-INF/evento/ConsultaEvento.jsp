<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx  = request.getContextPath();
  String nick = (String) session.getAttribute("nick");
  String rol  = (String) session.getAttribute("rol");

  Object obj = request.getAttribute("evento");
  // Intentamos leer por reflexión ligera para no romper si falta algo
  String nombre = null, sigla = null, desc = null, fechaAlta = null, categoriasTxt = null;
  try {
    if (obj != null) {
      java.lang.reflect.Method m;
      m = obj.getClass().getMethod("getNombre");    nombre = (String) m.invoke(obj);
      try { m = obj.getClass().getMethod("getSigla");       sigla = (String) m.invoke(obj); } catch (Exception ignore) {}
      try { m = obj.getClass().getMethod("getDescripcion"); desc  = (String) m.invoke(obj); } catch (Exception ignore) {}
      try { 
        m = obj.getClass().getMethod("getFechaAlta"); 
        Object fa = m.invoke(obj);
        fechaAlta = (fa != null ? fa.toString() : null);
      } catch (Exception ignore) {}
      try {
        m = obj.getClass().getMethod("getCategorias"); 
        Object cats = m.invoke(obj);
        categoriasTxt = (cats != null ? cats.toString() : null);
      } catch (Exception ignore) {}
    }
  } catch (Exception e) {
    // si algo falla, seguimos y mostramos lo que haya
  }
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Consulta de Evento — <%= (nombre!=null? nombre : "Evento") %></title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/ConsultaEventoBase.css">
</head>
<body>

<header class="site-header">
  <div class="container">
    <a class="brand" href="<%=ctx%>/"><i class='bx bxs-plane-alt'></i> Eventos.uy</a>

    <nav class="main-nav">
      <form class="search" action="<%=ctx%>/buscar" method="get" role="search" aria-label="Buscar">
        <input class="search-input" type="search" name="q" placeholder="Eventos, Ediciones">
        <button class="btn" type="submit">Buscar</button>
      </form>
    </nav>

    <nav class="user-nav" id="userNav">
      <% if (nick == null) { %>
        <a class="btn" href="<%=ctx%>/auth/login"><i class='bx bxs-user'></i> Iniciar sesión</a>
      <% } else { %>
        <span class="user-name">Hola, <strong><%= nick %></strong></span>
        <a class="btn" href="<%=ctx%>/auth/logout"><i class='bx bxs-user'></i> Cerrar sesión</a>
      <% } %>
    </nav>
  </div>
</header>

<div class="container row" style="margin-top:1rem;">

  <aside class="card">
    <h3><i class='bx bx-list-ul'></i> Menú</h3>
    <h4>Acciones</h4>
    <ul>
      <li><a href="<%=ctx%>/evento/alta"><i class='bx bx-edit'></i> Alta Evento</a></li>
      <li><a href="#"><i class='bx bx-edit'></i> Alta de Edición Evento</a></li>
      <li><a href="#"><i class='bx bx-edit'></i> Alta Tipo Registro</a></li>
    </ul>
  </aside>

  <main class="container consulta-layout">
    <section class="event-card">
      <div class="event-header">
        <h1 class="event-title"><%= (nombre!=null? nombre : "Evento") %></h1>
      </div>
      <div class="event-info">
        <h3>Descripción</h3>
        <p><%= (desc!=null? desc : "—") %></p>
        <div class="event-meta"><strong>Sigla:</strong> <%= (sigla!=null? sigla : "—") %></div>
        <div class="chips">
          <span class="categorias-label">Categorías:</span>
          <%
            if (categoriasTxt != null) {
              String[] arr = categoriasTxt.replace("[","").replace("]","").split("[,;]");
              for (String c : arr) {
          %>
                <span class="chip"><%= c.trim() %></span>
          <%
              }
            } else {
          %>
              <span class="chip">—</span>
          <%
            }
          %>
        </div>
        <div class="event-meta"><strong>Fecha alta:</strong> <%= (fechaAlta!=null? fechaAlta : "—") %></div>
      </div>
    </section>

    <!-- Si luego agregás ediciones, renderízalas acá con atributos que setees en el servlet -->
    <aside class="editions">
      <h3>Ediciones</h3>
      <p>(Próximamente)</p>
    </aside>
  </main>
</div>

</body>
</html>
