package br.senac.tads.dsw.exemplosspring.produto.controller;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Categoria;
import br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio.CategoriaRepositorySpringData;

@Controller
@RequestMapping("/categorias-sd")
public class CategoriaControllerSpringData {

    @Autowired
    private CategoriaRepositorySpringData repository;

    @GetMapping
    public ModelAndView listar() {
        List<Categoria> resultados = repository.findAll();
        return new ModelAndView("categorias-sd/lista").addObject("categorias", resultados);
    }

    @GetMapping("/novo")
    public ModelAndView adicionarNovo() {
        return new ModelAndView("categorias-sd/form").addObject("categoria", new Categoria());
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable("id") Integer id, RedirectAttributes redirAttr) {
        Optional<Categoria> optCat = repository.findById(id);
        if (!optCat.isPresent()) {
            redirAttr.addFlashAttribute("msgErro", "Categoria com ID " + id + " não encontrado");
            return new ModelAndView("redirect:/categorias-sd");
        }
        Categoria cat = optCat.get();
        return new ModelAndView("categorias-sd/form").addObject("categoria", cat);
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@ModelAttribute("categoria") @Valid Categoria cat,
            BindingResult bindingResult, RedirectAttributes redirAttr) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("categorias-sd/form");
        }
        repository.save(cat);
        redirAttr.addFlashAttribute("msgSucesso",
                "Categoria " + cat.getNome() + " salva com sucesso");
        return new ModelAndView("redirect:/categorias-sd");
    }

}
