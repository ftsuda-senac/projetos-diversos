package br.senac.tads.dsw.exemplosspring.pessoas.controller;

import br.senac.tads.dsw.exemplosspring.pessoas.service.*;
import br.senac.tads.dsw.exemplosspring.pessoas.dominio.entidade.Interesse;
import br.senac.tads.dsw.exemplosspring.pessoas.dominio.repositorio.InteresseRepository;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/interesses")
public class InteresseController {

    private InteresseRepository repository;

    public InteresseController(InteresseRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ModelAndView listar() {
        List<Interesse> resultados = repository.findAll();
        ModelAndView mv = new ModelAndView("interesses/lista");
        mv.addObject("dadosLista", resultados);
        return mv;
    }

    @GetMapping("/incluir")
    public ModelAndView formIncluir() {
        ModelAndView mv = new ModelAndView("interesses/form");
        mv.addObject("dados", new Interesse());
        return mv;
    }

    @GetMapping("/{id}/alterar")
    public ModelAndView formAlterar(@PathVariable("id") Integer id, RedirectAttributes redirAttr) {
        Optional<Interesse> optPessoa = repository.findById(id);
        if (!optPessoa.isPresent()) {
            redirAttr.addFlashAttribute("msgErro", "Interesse com ID " + id + " não encontrado");
            return new ModelAndView("redirect:/interesses");
        }
        ModelAndView mv = new ModelAndView("interesses/form");
        mv.addObject("dados", optPessoa.get());
        return mv;
    }

    @PostMapping("/salvar-novo")
    public ModelAndView salvarNovo(@ModelAttribute("dados") @Valid Interesse dados, BindingResult bindingResult, RedirectAttributes redirAttr) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("interesses/form");
            mv.addObject("msgErro", "Erro no preenchimento - verificar abaixo");
            return mv;
        }
        repository.save(dados);
        redirAttr.addFlashAttribute("msgSucesso", "Interesse incluido com sucesso - ID " + dados.getId());
        return new ModelAndView("redirect:/interesses");
    }

    @PostMapping("/{id}/salvar")
    public ModelAndView salvarExistente(@PathVariable("id") Integer id, @ModelAttribute("dados") @Valid Interesse dados, BindingResult bindingResult, RedirectAttributes redirAttr) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("interesses/form");
            mv.addObject("msgErro", "Erro no preenchimento - verificar abaixo");
            return mv;
        }
        dados.setId(id);
        repository.save(dados);
        redirAttr.addFlashAttribute("msgSucesso", "Interesse com ID " + id + " alterado com sucesso");
        return new ModelAndView("redirect:/interesses");
    }

}
