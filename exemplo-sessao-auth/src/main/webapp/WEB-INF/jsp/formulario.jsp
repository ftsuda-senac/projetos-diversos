<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form action="${pageContext.request.contextPath}/formulario" method="post">
            <label>Texto</label>
            <input type="text" name="texto" />
            <button type="submit">Enviar</button>
                
        </form>
        
    </body>
</html>
