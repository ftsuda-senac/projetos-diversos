/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package br.senac.tads.pi3.exemploauth.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import br.senac.tads.pi3.exemploauth.usuario.UsuarioSistema;
import br.senac.tads.pi3.exemploauth.usuario.UsuarioSistemaService;
import br.senac.tads.pi3.exemploauth.usuario.UsuarioSistemaServiceMockImpl;

/**
 *
 * @author fernando.tsuda
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String senhaAberta = request.getParameter("senha");

        // 1) Verifica se existe usuário cadastrado com o username informado
        UsuarioSistemaService service = new UsuarioSistemaServiceMockImpl();
        UsuarioSistema usuario = service.findByUsername(username);

        if (usuario != null && usuario.validarSenha(senhaAberta)) {
            // 2) Se usuario existir e a senha estiver correta, adiciona informações
            // do usuário na sessao e redireciona para tela principal (/home)
            HttpSession sessao = request.getSession();
            sessao.setAttribute("usuario", usuario);

            response.sendRedirect("home");
        } else {
            // 3) Se usuário não existir ou a senha estiver incorreta, reapresentar
            // a tela de login com uma mensagem de erro.
            request.setAttribute("msgErro", "Usuario ou senha inválido");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }

    }

}
