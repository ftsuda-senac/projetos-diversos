/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplojs;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author fernando.tsuda
 */
@Controller
public class TesteJsController {

    // Mantém alguns itens com dados aleatórios
    private final static List<Item> ITENS = new ArrayList<>();

    static {
        ITENS.add(new Item(1));
        ITENS.add(new Item(2));
        ITENS.add(new Item(3));
        ITENS.add(new Item(4));
        ITENS.add(new Item(5));
        ITENS.add(new Item(6));
    }

    @GetMapping
    public ModelAndView mostrarView() {
        return new ModelAndView("teste-js").addObject("itens", ITENS);
    }

}
