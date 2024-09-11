/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.senac.tads.dsw.exemplos;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SimpleServlet", urlPatterns = {"/exemplo-simples"})
public class SimpleServlet extends HttpServlet {

    public String gerarHtml(String nome, String userAgent, String enderecoIp) {
        return "<!DOCTYPE html>" + System.lineSeparator()
                + "<html>" + System.lineSeparator()
                + "<head>" + System.lineSeparator()
                + "\t<meta charset=\"UTF-8\" />" + System.lineSeparator()
                + "\t<title>Exemplo HTML</title>" + System.lineSeparator()
                + "</head>" + System.lineSeparator()
                + "<body>" + System.lineSeparator()
                + "\t<h1>Exemplo HTML gerado por Servlet</h1>" + System.lineSeparator()
                + "\t<h2>Nome: " + nome + "</h2>" + System.lineSeparator()
                + "\t<p>Data e hora: " + LocalDateTime.now() + "</p>" + System.lineSeparator()
                + "\t<p>User-agent usado: " + userAgent + "</p>" + System.lineSeparator()
                + "\t<p>Endereço IP: " + enderecoIp + "</p>" + System.lineSeparator()
                + "</body>" + System.lineSeparator()
                + "</html>";
    }
    
    public String gerarJson(String nome, String userAgent, String enderecoIp) {
        return "{" + System.lineSeparator()
                + "\t\"texto\" : \"Exemplo JSON gerado no Servlet\"," + System.lineSeparator()
                + "\t\"dataHora\" : \"" + LocalDateTime.now() + "\"," + System.lineSeparator()
                + "\t\"nome\" : \"" + nome + "\"," + System.lineSeparator()
                + "\t\"userAgent\" : \"" + userAgent + "\"," + System.lineSeparator()
                + "\t\"enderecoIp\" : \"" + enderecoIp + "\"" + System.lineSeparator()
                + "}";
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String userAgent = request.getHeader("user-agent");
        String enderecoIp = request.getRemoteAddr();
        String nome = request.getParameter("nome");
        if (nome == null || nome.trim().length() == 0) {
            nome = "Anônimo";
        }
        String resposta = gerarJson(nome, userAgent, enderecoIp);
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.println(resposta);
        }

    }

}
