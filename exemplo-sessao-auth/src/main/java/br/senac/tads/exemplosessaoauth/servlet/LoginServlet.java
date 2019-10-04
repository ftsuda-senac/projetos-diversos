/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.exemplosessaoauth.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.senac.tads.exemplosessaoauth.usuario.UsuarioSistema;
import br.senac.tads.exemplosessaoauth.usuario.UsuarioSistemaService;
import br.senac.tads.exemplosessaoauth.usuario.UsuarioSistemaServiceMockImpl;

/**
 *
 * @author fernando.tsuda
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String senhaAberta = request.getParameter("senha");

        // 1) Verificar se existe usuario com username fornecido
        UsuarioSistemaService service = new UsuarioSistemaServiceMockImpl();
        UsuarioSistema usuario = service.findByUsername(username);

        if (usuario != null && usuario.validarSenha(senhaAberta)) {
            // 2) Se usuario nao for nulo e senha for valida, coloca usuario na 
            // sessao e redireciona para tela principal
            HttpSession sessao = request.getSession();
            sessao.setAttribute("usuario", usuario);

            response.sendRedirect("home");
        } else {
            // 3) Se usuario for nulo ou senha invalida, reapresenta tela de login
            // com mensagem de erro.
            request.setAttribute("msgErro", "Usuario ou senha inválido");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp")
                    .forward(request, response);
        }

    }

}
