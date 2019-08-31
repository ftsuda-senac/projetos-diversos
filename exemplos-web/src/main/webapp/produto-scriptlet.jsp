<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="br.senac.tads.pi3.exemplosweb.Produto"%>
<%@page import="br.senac.tads.pi3.exemplosweb.Categoria"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Cake Web</title>
    </head>
    <body>
        <%
        Map<String, Categoria> categorias = new HashMap<>();
        categorias.put("cat1", new Categoria("cat1"));
        categorias.put("cat2", new Categoria("cat2"));
        categorias.put("cat3", new Categoria("cat3"));

        Produto p1 = new Produto(1L, "Bolo de chocolate",
                "descrição do bolo de chocolate", new BigDecimal(30.0),
                "http://lorempixel.com/200/200/food/10/");
        p1.setCategorias(Arrays.asList(categorias.get("cat1")));
        Produto p2 = new Produto(2L, "Bolo de cenoura",
                "descrição do bolo de cenoura", new BigDecimal(20.0),
                "http://lorempixel.com/200/200/food/10/");
        p2.setCategorias(Arrays.asList(categorias.get("cat2"), categorias.get("cat3")));
        Produto p3 = new Produto(3L, "Torta de morango",
                "descrição da torta de morango", new BigDecimal(25.0),
                "http://lorempixel.com/200/200/food/10/");
        p3.setCategorias(Arrays.asList(categorias.get("cat1"), categorias.get("cat3")));
        Produto p4 = new Produto(4L, "Bolo de doce de leite",
                "descrição do bolo de doce de leite", new BigDecimal(30.0),
                "http://lorempixel.com/200/200/food/10/");
        List<Produto> lista = Arrays.asList(p1, p2, p3, p4);
        %>
        <h1>Cake Web - JSP + Scriptlet</h1>
        <ul>
            <% for (Produto p : lista) { %>
            <li><div>
                    <img src="<%= p.getUrlImagem() %>" />
                    <h3><%= p.getNome()%></h3>
                    <p><%= p.getDescricao()%></p>
                    <p><%= p.getPrecoVenda().toString()%></p>
                    <% if (p.getCategorias() != null && !p.getCategorias().isEmpty()) { %>
                    <ul>
                        <% for (Categoria c : p.getCategorias()) { %>
                        <li><%= c.getNome() %></li>
                        <% } // for Categoria %>
                   </ul>
                    <% } // if %>
            </div></li>
            <% } // for Produto %>
        </ul>
    </body>
</html>
