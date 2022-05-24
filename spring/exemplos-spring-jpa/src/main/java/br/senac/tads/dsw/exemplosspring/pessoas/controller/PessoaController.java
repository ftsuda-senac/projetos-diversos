package br.senac.tads.dsw.exemplosspring.pessoas.controller;

import br.senac.tads.dsw.exemplosspring.pessoas.service.*;
import br.senac.tads.dsw.exemplosspring.pessoas.dominio.entidade.DadosPessoais;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/visualizar")
public class PessoaController {

    private DadosPessoaisService service;

    public PessoaController(DadosPessoaisService service) {
        this.service = service;
    }

    @GetMapping
    public ModelAndView visualizarLista(@RequestParam(value = "bgvideo", defaultValue = "0") int bgVideo) {
        List<DadosPessoais> resultados = service.findAll(0, 1000);
        ModelAndView mv = new ModelAndView(bgVideo == 1 ? "pessoas/visualizacao-lista-video" : "pessoas/visualizacao-lista");
        mv.addObject("dadosLista", resultados);
        return mv;
    }

    @ModelAttribute("imagemUrlPrefix")
    public String imagemUrlPrefix(@Value("${app.imagem-url-prefix}") String imagemUrlPrefix) {
        return imagemUrlPrefix;
    }
}
