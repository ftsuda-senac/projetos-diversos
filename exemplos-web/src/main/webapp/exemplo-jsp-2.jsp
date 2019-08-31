<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Exemplo JSP 2</title>
    </head>
    <body>
        <h1>Exemplo JSP 2</h1>
        <p>val1 digitado: <%= request.getParameter("val1") %></p>
        <p>val2 digitado: <%= request.getParameter("val2") %></p>
    </body>
</html>
