<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Exemplo sessão</title>
    </head>
    <body>
        <h1>Exemplo sessão</h1>
        <c:if test="${sessionScope.acessos != null && !sessionScope.acessos.isEmpty()}">
            <ul>
                <c:forEach items="${sessionScope.acessos}" var="acesso">
                    <li><c:out value="${acesso}" /></li>
                </c:forEach>
            </ul>
        </c:if>
    </body>
</html>
