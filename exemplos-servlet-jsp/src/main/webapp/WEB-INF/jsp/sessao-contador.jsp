<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Exemplo Sessão</title>
    </head>
    <body>
        <h1>Contador de acessos</h1>
        <h2>Acessos: <c:out value="${sessionScope.contador}" /></h2>
    </body>
</html>