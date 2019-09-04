/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.pi3.exemplos.web2.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fernando.tsuda
 */
@WebServlet(name = "FormularioServlet", urlPatterns = {"/formulario"})
public class FormularioServlet extends HttpServlet {

    // GET /formulario -> Retorna formulário para preenchimento
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher
                = request.getRequestDispatcher("/WEB-INF/jsp/formulario.jsp");
        dispatcher.forward(request, response);
    }

    // POST /formulario -> recebe os dados preenchidos, valida e retorna mensagem (sucesso ou erro)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // RECUPERA INFORMACOES DA REQUISICAO
        String escondido = request.getParameter("escondido");
        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");
        String senha = request.getParameter("senha");
        String quantidadeStr = request.getParameter("quantidade");
        String precoCompraStr = request.getParameter("prcompra");
        String precoVendaStr = request.getParameter("prvenda");
        String disponivelStr = request.getParameter("disponivel");
        String[] categoriasArr = request.getParameterValues("categorias");

        String userAgent = request.getHeader("user-agent");
        String remoteAddress = request.getRemoteAddr();

        // VALIDAR DADOS
        boolean temErros = false;
        if (nome == null || nome.trim().length() == 0) {
            temErros = true;
            request.setAttribute("erroNome", "Nome não preenchido");
        }
        if (descricao == null || descricao.length() == 0) {
            temErros = true;
            request.setAttribute("erroDescricao", "Descrição não preenchida");
        }
        if (categoriasArr == null || categoriasArr.length == 0) {
            temErros = true;
            request.setAttribute("erroCategorias", "Categoria não selecionada");
        }

        if (temErros) {
            // REAPRESENTA FORMULARIO INDICANDO OS ERROS
            RequestDispatcher dispatcher
                    = request.getRequestDispatcher("/WEB-INF/jsp/formulario.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // ARMAZENANDO VALORES COMO ATRIBUTOS
        request.setAttribute("metodoHttp", "POST");
        request.setAttribute("escondido", escondido);
        request.setAttribute("nome", nome);
        request.setAttribute("descricao", descricao);
        request.setAttribute("senha", senha);
        int quantidade = 0;
        try {
            quantidade = Integer.parseInt(quantidadeStr);
        } catch (Exception e) {

        }
        request.setAttribute("quantidade", quantidade);
        request.setAttribute("precoCompra", new BigDecimal(precoCompraStr));
        request.setAttribute("precoVenda", new BigDecimal(precoVendaStr));
        request.setAttribute("disponivel", "1".equals(disponivelStr) ? "SIM" : "NÃO");
        request.setAttribute("categorias", (categoriasArr != null) ? Arrays.asList(categoriasArr) : new ArrayList<>());

        RequestDispatcher dispatcher
                = request.getRequestDispatcher("/WEB-INF/jsp/resultado.jsp");
        dispatcher.forward(request, response);
    }
}
