<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx = request.getContextPath();
  String mensaje = (String) request.getAttribute("mensaje");
  if (mensaje == null) mensaje = "El registro se realizó correctamente.";
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Registro creado — Eventos.uy</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
  <link rel="stylesheet" href="<%=ctx%>/css/index.css">
  <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
  <style>
    .ok-box {
      background: #e6ffed;
      border: 1px solid #b3e6c1;
      color: #0a662e;
      padding: 2rem;
      border-radius: 10px;
      text-align: center;
      margin-top: 2rem;
    }
    .ok-box h2 {
      margin-top: 0;
    }
  </style>
</head>
<body>

<jsp:include page="/WEB-INF/templates/header.jsp" />
<div class="container row" style="margin-top:1rem;">

	  <jsp:include page="/WEB-INF/templates/menu.jsp" />


  <!-- Contenido principal -->
  <main class="container">
    <section class="ok-box">
      <h2>✅ ¡Registro creado con éxito!</h2>
      <p><%= mensaje %></p>

      <div style="margin-top: 2rem;">
        <a class="btn" href="<%=ctx%>/registro/alta"><i class='bx bx-plus-circle'></i> Crear otro</a>
        <a class="btn ghost" href="<%=ctx%>/"><i class='bx bx-home'></i> Volver al inicio</a>
      </div>
    </section>
  </main>
</div>

</body>
</html>