/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.servletjsp.servlet;

import java.io.IOException;
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
@WebServlet(name = "SessaoContadorServlet", urlPatterns = {"/sessao-contador"})
public class SessaoContadorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao = request.getSession();
        Integer contador = (Integer) sessao.getAttribute("contador");
        if (contador == null) {
            contador = 0;
        }
        contador++;
        sessao.setAttribute("contador", contador);

        RequestDispatcher dispatcher
                = request.getRequestDispatcher("/WEB-INF/jsp/sessao-contador.jsp");
        dispatcher.forward(request, response);
    }
}
