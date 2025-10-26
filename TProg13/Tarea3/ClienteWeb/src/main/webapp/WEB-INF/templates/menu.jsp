<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="publicadores.PublicadorEventoService, publicadores.DtCategorias, java.util.List, java.util.ArrayList" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/layoutMenu.css">

<%
  String ctx = request.getContextPath();
  String rol = (String) session.getAttribute("rol");

  @SuppressWarnings("unchecked")
  java.util.List<Object> dtCategoriasRaw = (java.util.List<Object>) request.getAttribute("dtCategorias");
  java.util.List<String> categorias = new java.util.ArrayList<>();

  if (dtCategoriasRaw != null) {
      for (Object o : dtCategoriasRaw) {
          try {
              // try to treat as generated DtCategorias
              if (o instanceof DtCategorias) {
                  DtCategorias dtCat = (DtCategorias) o;
                  if (dtCat.getCategorias() != null) categorias.addAll(dtCat.getCategorias().getCategoria());
              } else {
                  // fallback: assume it's a legacy object with getCategorias() returning List<String>
                  java.lang.reflect.Method m = o.getClass().getMethod("getCategorias");
                  Object res = m.invoke(o);
                  if (res instanceof java.util.Collection) {
                      for (Object s : (java.util.Collection) res) {
                          if (s != null) categorias.add(s.toString());
                      }
                  }
              }
          } catch (Exception e) {
              // ignore per-item errors
          }
      }
  }

  // If no categories found, try calling webservice
  if (categorias.isEmpty()) {
      try {
          PublicadorEventoService svc = new PublicadorEventoService();
          publicadores.PublicadorEvento port = svc.getPublicadorEventoPort();
          // no array wrapper type assumed here; use reflection/fallback below
          // There is no listarDTCategorias returning array in client, try listarDTCategorias via service (if exists)
          try {
              // Some generated client might not have array wrapper; attempt to call listarDTCategorias()
              // Use reflection to call if present
              java.lang.reflect.Method m = port.getClass().getMethod("listarDTCategorias");
              Object res = m.invoke(port);
              if (res instanceof java.util.List) {
                  for (Object dt : (java.util.List) res) {
                      try {
                          java.lang.reflect.Method gm = dt.getClass().getMethod("getCategorias");
                          Object cats = gm.invoke(dt);
                          if (cats != null) {
                              java.lang.reflect.Method g = cats.getClass().getMethod("getCategoria");
                              Object list = g.invoke(cats);
                              if (list instanceof java.util.Collection) {
                                  for (Object it : (java.util.Collection) list) categorias.add(it.toString());
                              }
                          }
                      } catch (Exception ignore) {}
                  }
              }
          } catch (NoSuchMethodException nsme) {
              // ignore
          }
      } catch (Exception ex) {
          // ignore
      }
  }
%>

<aside class="card aside-inicio">
  <h3>Menú</h3>

  <% if ("ORGANIZADOR".equals(rol)) { %>
    <h4>Acciones</h4>
    <ul>
      <li>
        <form action="<%=ctx%>/evento/alta" method="get" style="display:inline">
          <button type="submit" class="linklike">Crear Evento</button>
        </form>
      </li>
      <li>
        <form action="<%=ctx%>/edicion/alta" method="get" style="display:inline">
          <button type="submit" class="linklike">Crear Edición de Evento</button>
        </form>
      </li>
      <li>
        <form action="<%=ctx%>/registro/alta" method="get" style="display:inline">
          <button type="submit" class="linklike">Crear Registro</button>
        </form>
      </li>
      <li>
        <form action="<%=ctx%>/institucion/alta" method="get" style="display:inline">
          <button type="submit" class="linklike">Crear Institución</button>
        </form>
      </li>
      <li>
        <!-- NUEVO: un botón simple que abre la pantalla de Alta Patrocinio -->
        <form action="<%=ctx%>/edicion/patrocinio/alta" method="get" style="display:inline">
          <button type="submit" class="linklike">Crear Patrocinio</button>
        </form>
      </li>
    </ul>
  <% } else if ("ASISTENTE".equals(rol)) { %>
    <h4>Acciones</h4>
    <ul>
      <li>
        <form action="<%=ctx%>/registro/inscripcion" method="get" style="display:inline">
          <button type="submit" class="linklike">Registrarse a Edición de un Evento</button>
        </form>
      </li>
    </ul>
  <% } %>

  <h4>Categorías</h4>
  <ul class="menu-categorias">
    <% if (categorias != null && !categorias.isEmpty()) {
         for (String cat : categorias) { %>
      <li>
        <form action="<%=ctx%>/evento/listado" method="get">
          <input type="hidden" name="categoria" value="<%=cat%>">
          <button type="submit" class="linklike"><%=cat%></button>
        </form>
      </li>
    <% } } else { %>
      <li><span>(Sin categorías)</span></li>
    <% } %>
  </ul>

  <h4>
    <form action="<%=ctx%>/usuario/ConsultaUsuario" method="get" style="display:inline">
      <input type="hidden" name="listar" value="1">
      <button type="submit" class="linklike">Listar Usuarios</button>
    </form>
  </h4>
</aside>