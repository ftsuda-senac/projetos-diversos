<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Cake Web</title>
    </head>
    <body>
        <h1>Cake Web - MVC</h1>
        <ul>
            <c:forEach items="${produtos}" var="prod">
            <li>
                <div>
                    <img src="${prod.getUrlImagem()}" />
                    <h3><c:out value="${prod.getNome()}" /></h3>
                    <p><c:out value="${prod.getDescricao()}" /></p>
                    <p><c:out value="${prod.getPrecoVenda().toString()}" /></p>
                    <c:if test="${not empty prod.getCategorias()}">
                        <ul>
                            <c:forEach items="${prod.getCategorias()}" var="cat">
                                <li><c:out value="${cat.getNome()}" /></li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </div>
            </li>
            </c:forEach>
        </ul>
    </body>
</html>
