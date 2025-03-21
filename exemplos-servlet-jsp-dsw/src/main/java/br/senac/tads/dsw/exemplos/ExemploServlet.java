/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package br.senac.tads.dsw.exemplos;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ExemploServlet", urlPatterns = "/exemplo-servlet")
public class ExemploServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        
        String nome = request.getParameter("nome");
        if (nome == null) {
            nome = "usuário";
        }
        
        StringBuilder headerIn = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerIn.append(headerName).append(": ")
                    .append(request.getHeader(headerName)).append("\r\n");
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\" />");
            out.println("<title>Exemplo Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Exemplo Servlet</h1>");
            out.println("<h2>Olá " + nome + "</h2>");
            out.println("<p>Data e hora atual: " + LocalDateTime.now() + "</p>");
            out.println("<pre>");
            out.println(headerIn.toString());
            out.println("</pre>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
