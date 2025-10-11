<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Invalida la sesión actual
    session.invalidate();

    // Opcional: limpiar mensajes o flags de error en la request
    request.removeAttribute("error");

    // Redirigir al inicio o al login
    response.sendRedirect(request.getContextPath() + "/inicio");
%>
