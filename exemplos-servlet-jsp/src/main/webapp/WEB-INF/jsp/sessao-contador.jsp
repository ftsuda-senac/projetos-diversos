<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Exemplo Sessão</title>
    </head>
    <body>
        <h1>Contador de acessos</h1>
        <h2>Acessos: <c:out value="${sessionScope.contador}" /></h2>
        <hr>
        <p>sessionID: <c:out value="${pageContext.session.id}" /></p>
        <jsp:useBean id="timeValues" class="java.util.Date" />

        <c:set target="${timeValues}" property="time" value="${pageContext.session.creationTime}" />
        <p>Data criação: <fmt:formatDate value="${timeValues}" type="both" dateStyle="medium" /></p>
        <c:set target="${timeValues}" property="time" value="${pageContext.session.lastAccessedTime}" />
        <p>Data último acesso: <fmt:formatDate value="${timeValues}" type="both" dateStyle="medium" /></p>
    </body>
</html>