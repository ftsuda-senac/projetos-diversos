package br.senac.tads.pi3.exemplosservlets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/teste-mvc")
public class TesteController {

    @GetMapping
    public ModelAndView teste() {
        return new ModelAndView("teste-mvc");
    }

}
