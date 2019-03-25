/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads4.dsw.servletjsp.servlet;

import br.senac.tads4.dsw.servletjsp.modelo.Pessoa;
import br.senac.tads4.dsw.servletjsp.service.PessoaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Fernando
 */
@WebServlet(name = "ListaPessoasAjaxServlet", urlPatterns = {"/lista-pessoas-ajax"})
public class ListaPessoasAjaxServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PessoaService service = new PessoaService();

        // Lista todas as pessoas cadastradas
        List<Pessoa> lista = service.listar();

        // Preparar resposta
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        //response.addHeader("Access-Control-Allow-Origin", "*");
        
        // Jackson 2
        ObjectMapper mapper = new ObjectMapper();
        try (PrintWriter out = response.getWriter()) {
            out.print(mapper.writeValueAsString(lista));
        }
    }

}
