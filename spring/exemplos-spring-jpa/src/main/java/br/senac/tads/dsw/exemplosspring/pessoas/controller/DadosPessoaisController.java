package br.senac.tads.dsw.exemplosspring.pessoas.controller;

import br.senac.tads.dsw.exemplosspring.pessoas.service.*;
import br.senac.tads.dsw.exemplosspring.pessoas.dominio.entidade.DadosPessoais;
import br.senac.tads.dsw.exemplosspring.pessoas.dominio.entidade.Interesse;
import br.senac.tads.dsw.exemplosspring.pessoas.dominio.repositorio.InteresseRepository;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pessoas")
public class DadosPessoaisController {

    private DadosPessoaisService service;

    private InteresseRepository interesseRepository;

    public DadosPessoaisController(DadosPessoaisService service, InteresseRepository interesseRepository) {
        this.service = service;
        this.interesseRepository = interesseRepository;
    }

    @GetMapping
    public ModelAndView listar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "qtde", defaultValue = "300") int qtde,
            @RequestParam(name = "interessesIds", required = false) List<Integer> interessesIds) {
        List<DadosPessoais> resultados = service.findAll(page, qtde);
        ModelAndView mv = new ModelAndView("pessoas/lista");
        mv.addObject("dadosLista", resultados);
        return mv;
    }

    @GetMapping("/incluir")
    public ModelAndView formIncluir() {
        ModelAndView mv = new ModelAndView("pessoas/form");
        mv.addObject("dados", new DadosPessoais());
        return mv;
    }

    @GetMapping("/{id}/visualizar")
    public ModelAndView visualizar(
            @PathVariable("id") Integer id,
            RedirectAttributes redirAttr) {
        Optional<DadosPessoais> optPessoa = service.findById(id);
        if (!optPessoa.isPresent()) {
            redirAttr.addFlashAttribute("msgErro", "Pessoa com ID " + id + " não encontrada");
            return new ModelAndView("redirect:/pessoas");
        }
        ModelAndView mv = new ModelAndView("pessoas/visualizacao");
        mv.addObject("dados", optPessoa.get());
        // mv.addObject("imagemUrlPrefix", imagemUrlPrefix);
        return mv;
    }

    @GetMapping("/{id}/alterar")
    public ModelAndView formAlterar(@PathVariable("id") Integer id, RedirectAttributes redirAttr) {
        Optional<DadosPessoais> optPessoa = service.findById(id);
        if (!optPessoa.isPresent()) {
            redirAttr.addFlashAttribute("msgErro", "Pessoa com ID " + id + " não encontrada");
            return new ModelAndView("redirect:/pessoas");
        }
        ModelAndView mv = new ModelAndView("pessoas/form");
        mv.addObject("dados", optPessoa.get());
        return mv;
    }

    @PostMapping("/salvar-novo")
    public ModelAndView salvarNovo(@ModelAttribute("dados") @Valid DadosPessoais dados, BindingResult bindingResult, RedirectAttributes redirAttr) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("pessoas/form");
            mv.addObject("msgErro", "Erro no preenchimento - verificar abaixo");
            return mv;
        }
        service.save(dados);
        redirAttr.addFlashAttribute("msgSucesso", "Pessoa incluida com sucesso - ID " + dados.getId());
        return new ModelAndView("redirect:/pessoas");
    }

    @PostMapping("/{id}/salvar")
    public ModelAndView salvarExistente(@PathVariable("id") Integer id, @ModelAttribute("dados") @Valid DadosPessoais dados, BindingResult bindingResult, RedirectAttributes redirAttr) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("pessoas/form");
            mv.addObject("msgErro", "Erro no preenchimento - verificar abaixo");
            return mv;
        }
        dados.setId(id);
        service.save(dados);
        redirAttr.addFlashAttribute("msgSucesso", "Pessoa com ID " + id + " alterada com sucesso");
        return new ModelAndView("redirect:/pessoas");
    }

    @PostMapping("/{id}/excluir")
    public ModelAndView excluirExistente(@PathVariable("id") Integer id, RedirectAttributes redirAttr) {
        service.deleteById(id);
        redirAttr.addFlashAttribute("msgSucesso", "Pessoa com ID " + id + " excluida com sucesso");
        return new ModelAndView("redirect:/pessoas");
    }

    // @ModelAttribute Permite acessar a lista de interesses a partir de qualquer View acionada
    // por esta classe @Controller
    // Porém, irá ser chamada mesmo em telas em que as opções não são usadas.
    @ModelAttribute("opcoesInteresses")
    public List<Interesse> getInteresses() {
        return interesseRepository.findAll();
    }

    @ModelAttribute("imagemUrlPrefix")
    public String imagemUrlPrefix(@Value("${app.imagem-url-prefix}") String imagemUrlPrefix) {
        return imagemUrlPrefix;
    }

}
