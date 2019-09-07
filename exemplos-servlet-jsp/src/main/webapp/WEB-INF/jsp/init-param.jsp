<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Exemplos Init param</h1>
        <ul>
            <li>Servlet param1: ${xparam1}</li>
            <li>Servlet param2: ${xparam2}</li>
            <li>Context param1: ${ctxparam1}</li>
            <li>Context param2: ${ctxparam2}</li>
            <li>Env-entry1: ${enventry1}</li>
            <li>Env-entry2: ${enventry2}</li>
        </ul>
        <h2>Context-param recuperado direto no JSP</h2>
        <ul>
            <li>Context param1: ${initParam.contextParam1}</li>
            <li>Context param2: ${initParam.contextParam2}</li>
        </ul>
    </body>
</html>
