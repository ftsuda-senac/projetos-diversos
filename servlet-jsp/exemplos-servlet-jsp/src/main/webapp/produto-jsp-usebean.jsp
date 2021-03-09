<%@page import="java.math.BigDecimal"%>
<%@page import="br.senac.tads.servletjsp.modelo.Produto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Exemplo JSP - jsp:useBean</title>
    </head>
    <body>
        
        <jsp:useBean id="p1" scope="request" class="br.senac.tads.servletjsp.modelo.Produto" />
        <jsp:setProperty name="p1" property="id" value="1" />
        <jsp:setProperty name="p1" property="nome" value="Bolo de chocolate" />
        <jsp:setProperty name="p1" property="descricao" value="descrição do bolo de chocolate" />
        <jsp:setProperty name="p1" property="precoCompra" value="<%= new BigDecimal(30.0) %>" />
        <jsp:setProperty name="p1" property="precoVenda" value="<%= new BigDecimal(30.0) %>" />
        <jsp:setProperty name="p1" property="imagem" value="http://lorempixel.com/200/200/food/10/" />
        
        <jsp:useBean id="p2" scope="request" class="br.senac.tads.servletjsp.modelo.Produto" />
        <jsp:setProperty name="p2" property="id" value="2" />
        <jsp:setProperty name="p2" property="nome" value="Bolo de cenoura" />
        <jsp:setProperty name="p2" property="descricao" value="descrição do bolo de cenoura" />
        <jsp:setProperty name="p2" property="precoCompra" value="<%= new BigDecimal(20.0) %>" />
        <jsp:setProperty name="p2" property="precoVenda" value="<%= new BigDecimal(20.0) %>" />
        <jsp:setProperty name="p2" property="imagem" value="http://lorempixel.com/200/200/food/10/" />
        
        <jsp:useBean id="p3" scope="request" class="br.senac.tads.servletjsp.modelo.Produto" />
        <jsp:setProperty name="p3" property="id" value="3" />
        <jsp:setProperty name="p3" property="nome" value="Torta de limão" />
        <jsp:setProperty name="p3" property="descricao" value="descrição da torta de limão" />
        <jsp:setProperty name="p3" property="precoCompra" value="<%= new BigDecimal(25.0) %>" />
        <jsp:setProperty name="p3" property="precoVenda" value="<%= new BigDecimal(25.0) %>" />
        <jsp:setProperty name="p3" property="imagem" value="http://lorempixel.com/200/200/food/10/" />
        
        <jsp:useBean id="p4" scope="request" class="br.senac.tads.servletjsp.modelo.Produto" />
        <jsp:setProperty name="p4" property="id" value="4" />
        <jsp:setProperty name="p4" property="nome" value="Bolo floresta negra" />
        <jsp:setProperty name="p4" property="descricao" value="descrição do bolo floresta negra" />
        <jsp:setProperty name="p4" property="precoCompra" value="<%= new BigDecimal(40.0) %>" />
        <jsp:setProperty name="p4" property="precoVenda" value="<%= new BigDecimal(40.0) %>" />
        <jsp:setProperty name="p4" property="imagem" value="http://lorempixel.com/200/200/food/10/" />
        
        <jsp:useBean id="listaProdutos" scope="request" class="br.senac.tads.servletjsp.modelo.ArrayProdutos" />
        <jsp:setProperty name="listaProdutos" property="produto" value="<%= p1 %>" />
        <jsp:setProperty name="listaProdutos" property="produto" value="<%= p2 %>" />
        <jsp:setProperty name="listaProdutos" property="produto" value="<%= p3 %>" />
        <jsp:setProperty name="listaProdutos" property="produto" value="<%= p4 %>" />

        <h1>Cake Web - jsp:useBean</h1>
        <%--
        <ul>
            <% for (Produto p : listaProdutos.getProdutos()) { %>
            <li>
                <div>
                    <img src="<jsp:getProperty name="p" property="imagem" />" />
                    <h3><jsp:getProperty name="p" property="nome" /></h3>
                    <p><jsp:getProperty name="p" property="descricao" /></p>
                    <p><jsp:getProperty name="p" property="precoVenda" /></p>    
                </div>
            </li>
            <% } %>
        </ul>
        --%>
        <ul>
            <% for (Produto p : listaProdutos.getProdutos()) { %>
            <li>
                <div>
                    <img src="<%= p.getImagem() %>" />
                    <h3><%= p.getNome()%></h3>
                    <p><%= p.getDescricao()%></p>
                    <p><%= p.getPrecoVenda().toString()%></p>
                </div>
            </li>
            <% } %>
        </ul>
    </body>
</html>