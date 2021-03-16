/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package br.senac.tads.servletjsp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import br.senac.tads.servletjsp.modelo.Produto;

/**
 *
 * @author fernando.tsuda
 */
@WebServlet(name = "ProdutoAjaxServlet", urlPatterns = {"/produto-ajax"})
public class ProdutoAjaxServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Produto p1 = new Produto(1L, "Bolo de chocolate", "descrição do bolo de chocolate",
                new BigDecimal(30.0), "http://lorempixel.com/200/200/food/10/");
        Produto p2 = new Produto(2L, "Bolo de cenoura", "descrição do bolo de cenoura",
                new BigDecimal(20.0), "http://lorempixel.com/200/200/food/10/");
        Produto p3 = new Produto(3L, "Torta de limão", "descrição da torta de limão",
                new BigDecimal(25.0), "http://lorempixel.com/200/200/food/10/");
        Produto p4 = new Produto(4L, "Bolo floresta negra", "descrição do bolo floresta negra",
                new BigDecimal(40.0), "http://lorempixel.com/200/200/food/10/");
        List<Produto> lista = Arrays.asList(p1, p2, p3, p4);

        // Preparar resposta JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        // response.addHeader("Access-Control-Allow-Origin", "*");

        // Jackson 2
        ObjectMapper mapper = new ObjectMapper();
        
        // Serialização de LocalDateTime: https://www.baeldung.com/jackson-serialize-dates
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try (PrintWriter out = response.getWriter()) {
            out.print(mapper.writeValueAsString(lista));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
