package br.senac.tads.dsw.exemplosspring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/exemplo-erro")
public class ErroController {

    @GetMapping("/erro1")
    public ModelAndView erro1() {
        return new ModelAndView("tela-nao-criada");
    }

    @GetMapping("/erro2")
    public ModelAndView erro2() throws ErroException {
        throw new ErroException("Erro gerado pela ErroException em /exemplo-erro/erro2");
    }

    @GetMapping("/erro3")
    public ModelAndView erro3() throws ErroException {
        throw new ErroException("Erro gerado pela ErroException em /exemplo-erro/erro3");
    }

    @GetMapping("/erro4")
    public ModelAndView erro4() throws ErroException {
        String teste = null;
        teste.substring(1);
        return new ModelAndView("pagina-erro");
    }
}
