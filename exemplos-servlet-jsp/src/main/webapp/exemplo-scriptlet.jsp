<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="br.senac.tads.servletjsp.modelo.Produto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Exemplo JSP</title>
    </head>
    <body>
        <%
        Produto p1 = new Produto(1L, "Bolo de chocolate",
                "descrição do bolo de chocolate",
                new BigDecimal(30.0), "http://lorempixel.com/g/300/300/");
        Produto p2 = new Produto(2L, "Bolo de cenoura",
                "descrição do bolo de cenoura",
                new BigDecimal(20.0), "http://lorempixel.com/g/300/300/");
        Produto p3 = new Produto(3L, "Torta de limão",
                "descrição da torta de limão",
                new BigDecimal(25.0), "http://lorempixel.com/g/300/300/");
        Produto p4 = new Produto(4L, "Bolo floresta negra",
                "descrição do bolo floresta negra",
                new BigDecimal(40.0), "http://lorempixel.com/g/300/300/");
        List<Produto> lista = Arrays.asList(p1, p2, p3, p4);
        %>
        <h1>Cake Web - JSP + Scriptlet</h1>
        <ul>
            <% for (Produto p : lista) {%>
            <li><div>
                    <h3><%= p.getNome()%></h3>
                    <p><%= p.getDescricao()%></p>
                    <p><%= p.getPrecoVenda().toString()%></p>
                    <% }%>
                </div></li>
        </ul>
    </body>
</html>