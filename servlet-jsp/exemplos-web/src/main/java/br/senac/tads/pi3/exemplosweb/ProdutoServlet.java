/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.pi3.exemplosweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fernando.tsuda
 */
@WebServlet(name = "ProdutoServlet", urlPatterns = {"/produto-servlet"})
public class ProdutoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<Integer, Categoria> categorias = new HashMap<>();
        categorias.put(1, new Categoria("Categoria 1"));
        categorias.put(2, new Categoria("Categoria 2"));
        categorias.put(3, new Categoria("Categoria 3"));

        Produto p1 = new Produto(1L, "Bolo de chocolate",
                "descrição do bolo de chocolate", new BigDecimal(30.0),
                "http://lorempixel.com/200/200/food/10/");
        p1.setCategorias(Arrays.asList(categorias.get(1)));
        Produto p2 = new Produto(2L, "Bolo de cenoura",
                "descrição do bolo de cenoura", new BigDecimal(20.0),
                "http://lorempixel.com/200/200/food/10/");
        p2.setCategorias(Arrays.asList(categorias.get(2), categorias.get(3)));
        Produto p3 = new Produto(3L, "Torta de morango",
                "descrição da torta de morango", new BigDecimal(25.0),
                "http://lorempixel.com/200/200/food/10/");
        p3.setCategorias(Arrays.asList(categorias.get(1), categorias.get(3)));
        Produto p4 = new Produto(4L, "Bolo de doce de leite",
                "descrição do bolo de doce de leite", new BigDecimal(30.0),
                "http://lorempixel.com/200/200/food/10/");
        List<Produto> lista = Arrays.asList(p1, p2, p3, p4);

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Cake Web</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Cake Web - Servlet</h1>");
            out.println("<ul>");
            for (Produto p : lista) {
                out.println("<li><div>");
                out.println("<img src=\"" + p.getUrlImagem() +"\" />");
                out.println("<h3>" + p.getNome() + "</h3>");
                out.println("<p>" + p.getDescricao() + "</p>");
                out.println("<p>" + p.getPrecoVenda().toString() + "</p>");
                if (p.getCategorias() != null && !p.getCategorias().isEmpty()) {
                    out.println("<ul>");
                    for (Categoria c : p.getCategorias()) {
                        out.println("<li>" + c.getNome() + "</li>");
                    }
                    out.println("</ul>");
                }
                out.println("</div></li>");
            }
            out.println("</ul>");
            out.println("</body>");
            out.println("</html>");
        }
    }

}
