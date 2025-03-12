<%@page import="java.util.Enumeration"%>
<%@page import="java.time.LocalDateTime"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Exemplo JSP</title>
</head>
<body>
  <h1>Exemplo JSP + Scriptlet</h1>
  <%
    String nome = request.getParameter("nome");
    if (nome == null) {
      nome = "usuário";
    }
  %>
  <h2>Olá <%= nome %></h2>
  <p>Data e hora atual: <%= LocalDateTime.now() %></p>
  <hr />
  <%
    StringBuilder headerIn = new StringBuilder();
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        headerIn.append(headerName).append(": ")
                .append(request.getHeader(headerName)).append("\r\n");
    }  
  %>
  <pre><%= headerIn %></pre>
</body>
</html>
