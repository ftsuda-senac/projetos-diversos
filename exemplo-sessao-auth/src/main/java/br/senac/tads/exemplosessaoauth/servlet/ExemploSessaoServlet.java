/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.exemplosessaoauth.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@WebServlet(name = "ExemploSessaoServlet", urlPatterns = {"/exemplo-sessao"})
public class ExemploSessaoServlet extends HttpServlet {
    
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession sessao = request.getSession();
        if (sessao.getAttribute("acessos") == null) {
            sessao.setAttribute("acessos", new ArrayList<>());
        }
        List<LocalDateTime> acessos = 
                (List<LocalDateTime>) sessao.getAttribute("acessos");
       
        acessos.add(LocalDateTime.now());

        request.getRequestDispatcher("/WEB-INF/jsp/exemplo-sessao.jsp")
            .forward(request, response);
        
    }
}
