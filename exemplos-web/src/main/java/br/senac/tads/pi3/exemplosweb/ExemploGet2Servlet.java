/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.pi3.exemplosweb;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fernando.tsuda
 */
@WebServlet(name = "ExemploGet2Servlet", urlPatterns = {"/exemplo-get-2"})
public class ExemploGet2Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String valor1 = request.getParameter("val1");
        String valor2 = request.getParameter("val2");
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Exemplo Servlet 2</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Exemplo Servlet 2</h1>");
            out.println("<p>val1 digitado: " + valor1 + "</p>");
            out.println("<p>val2 digitado: " + valor2 + "</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

}
