<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Listado de Ediciones</title>
</head>
<body>
    <h1>Listado de Ediciones</h1>
    <c:if test="${not empty listaEdiciones}">
        <ul>
            <c:forEach var="edicion" items="${listaEdiciones}">
                <li>${edicion}</li>
            </c:forEach>
        </ul>
    </c:if>
    <c:if test="${empty listaEdiciones}">
        <p>No hay ediciones para este evento.</p>
    </c:if>
    <a href="${pageContext.request.contextPath}/home">Volver al inicio</a>
</body>
</html>