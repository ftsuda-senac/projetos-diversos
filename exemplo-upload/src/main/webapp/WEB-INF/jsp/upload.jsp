<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Exemplo Upload de arquivo</title>
    </head>
    <body>
        <h1>Upload de arquivo</h1>
        <c:if test="${msg != null}">
            <p><c:out value="${msg}" /></p>
            <p>Acessar o arquivo através do link <a href="${urlAcessoUpload}" target="_blank">${urlAcessoUpload}</a></p>
        </c:if>   
        
        <form action="${pageContext.request.contextPath}/upload"  method="post" enctype="multipart/form-data">
            <input type="file" name="arquivo" />
            <button type="submit">Enviar</button>
        </form>
              
        <p>OBS: Para testes criar o diretório C:\uploads antes de enviar arquivo</p>
    </body>
</html>