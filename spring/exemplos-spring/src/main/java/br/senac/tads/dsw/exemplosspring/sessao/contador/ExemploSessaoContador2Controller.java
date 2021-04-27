/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring.sessao.contador;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ftsuda
 */
@Controller
@RequestMapping("/sessao-contador2")
public class ExemploSessaoContador2Controller {

    @GetMapping("/{apelido}")
    public ModelAndView somar(@PathVariable("apelido") String nome, HttpServletRequest request) {

        HttpSession sessao = request.getSession();
        if (sessao.getAttribute("contador2") == null) {
            sessao.setAttribute("contador2", new Contador());
        }
        Contador contador = (Contador) sessao.getAttribute("contador2");
        contador.adicionar(nome);
        sessao.setAttribute("contador2", contador);

        return new ModelAndView("sessao-contador/exemplo2");
    }

    @GetMapping("/limpar")
    public ModelAndView limparSessao(HttpSession sessao) {
        sessao.removeAttribute("contador2");
        return new ModelAndView("sessao-contador/exemplo3");
    }

    @ModelAttribute("titulo")
    public String getTitulo() {
        return "Exemplo Sessao 2 - Uso do HttpServletRequest + HttpSession nativo";
    }
}
