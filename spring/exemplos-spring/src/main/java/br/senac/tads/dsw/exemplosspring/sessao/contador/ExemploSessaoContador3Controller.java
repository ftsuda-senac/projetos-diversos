/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring.sessao.contador;

import java.io.Serializable;
import org.springframework.context.annotation.Scope;
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
@Scope("session")
@RequestMapping("/sessao-contador3")
public class ExemploSessaoContador3Controller implements Serializable {

    private static final long serialVersionUID = 1L;

    private Contador contador = new Contador();

    @GetMapping("/{apelido}")
    public ModelAndView somar(@PathVariable("apelido") String nome) {
        contador.adicionar(nome);
        return new ModelAndView("sessao-contador/exemplo3");
    }

    @ModelAttribute("titulo")
    public String getTitulo() {
        return "Exemplo Sessao 3 - Uso do @Controller + @Scope(\"session\")";
    }

    public Contador getContador() {
        return contador;
    }

    public void setContador(Contador contador) {
        this.contador = contador;
    }
}
