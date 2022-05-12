package br.senac.tads.dsw.exemplospringsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/erro")
public class ErroController {

    @GetMapping("/403")
    public String erroNaoPermitido() {
        return "erro403";
    }
    
}
