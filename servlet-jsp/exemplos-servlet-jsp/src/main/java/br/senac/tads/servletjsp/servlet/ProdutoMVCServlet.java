/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.servletjsp.servlet;

import br.senac.tads.servletjsp.modelo.Produto;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author fernando.tsuda
 */
@WebServlet(name = "ProdutoMVCServlet", urlPatterns = {"/produto-mvc"})
public class ProdutoMVCServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Produto p1 = new Produto(1L, "Bolo de chocolate",
                "descrição do bolo de chocolate",
                new BigDecimal(30.0), "http://lorempixel.com/200/200/food/10/");
        Produto p2 = new Produto(2L, "Bolo de cenoura",
                "descrição do bolo de cenoura",
                new BigDecimal(20.0), "http://lorempixel.com/200/200/food/10/");
        Produto p3 = new Produto(3L, "Torta de limão",
                "descrição da torta de limão",
                new BigDecimal(25.0), "http://lorempixel.com/200/200/food/10/");
        Produto p4 = new Produto(4L, "Bolo floresta negra",
                "descrição do bolo floresta negra",
                new BigDecimal(40.0), "http://lorempixel.com/200/200/food/10/");
        List<Produto> lista = Arrays.asList(p1, p2, p3, p4);
        request.setAttribute("listaProd", lista);

        // Encaminha requisição para JSP
        RequestDispatcher dispatcher
                = request.getRequestDispatcher("/WEB-INF/jsp/produto-mvc.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mensagemPost", "Tente recarregar a página e veja o que acontece");
        doGet(request, response);
    }

}
