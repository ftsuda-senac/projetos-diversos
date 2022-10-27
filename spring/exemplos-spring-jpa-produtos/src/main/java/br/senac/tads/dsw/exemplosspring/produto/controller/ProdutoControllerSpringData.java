package br.senac.tads.dsw.exemplosspring.produto.controller;

import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Categoria;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Produto;
import br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio.CategoriaRepositorySpringData;
import br.senac.tads.dsw.exemplosspring.produto.service.ProdutoServiceSpringData;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/produtos-sd")
public class ProdutoControllerSpringData {
    
    @Autowired
    private ProdutoServiceSpringData service;

    @Autowired
    private CategoriaRepositorySpringData categoriaRepository;

    @GetMapping
    public ModelAndView listar(
            @RequestParam(name = "offset", defaultValue = "0") int pagina,
            @RequestParam(name = "qtd", defaultValue = "500") int qtd,
            @RequestParam(name = "idsCat", required = false) List<Integer> idsCat) {
        return new ModelAndView("produtos-sd/lista").addObject("produtos", service.findAll(idsCat));
    }

    @GetMapping("/paginacao")
    public ModelAndView listarPaginacao(
            @RequestParam(name = "offset", defaultValue = "0") int pagina,
            @RequestParam(name = "qtd", defaultValue = "500") int qtd,
            @RequestParam(name = "idsCat", required = false) List<Integer> idsCat) {
        return new ModelAndView("produto-sd/lista").addObject("produtos", service.findAllPaged(idsCat, pagina, qtd));
    }

    @GetMapping("/novo")
    public ModelAndView adicionarNovo() {
        return new ModelAndView("produtos-sd/form").addObject("produto", new Produto());
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable("id") Integer id, RedirectAttributes redirAttr) {
        Optional<Produto> optProd = service.findById(id);
        if (!optProd.isPresent()) {
            redirAttr.addFlashAttribute("msgErro", "Produto com ID " + id + " não encontrado");
            return new ModelAndView("redirect:/produtos-sd");
        }
        Produto prod = optProd.get();
        return new ModelAndView("produtos-sd/form").addObject("produto", prod);
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@ModelAttribute("produto") @Valid Produto produto,
            BindingResult bindingResult, RedirectAttributes redirAttr) {
        service.salvar(produto);
        redirAttr.addFlashAttribute("msgSucesso",
                "Produto " + produto.getNome() + " salvo com sucesso");
        return new ModelAndView("redirect:/produtos-sd");
    }

    @PostMapping("/{id}/remover")
    public ModelAndView remover(@PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {
        service.remover(id);
        redirectAttributes.addFlashAttribute("msgSucesso",
                "Produto ID " + id + " removido com sucesso");
        return new ModelAndView("redirect:/produtos-sd");
    }

    @GetMapping("/busca")
    public ModelAndView listarFiltrarPorNome(@RequestParam("nome") String nome) {
        List<Produto> resultados = service.findByNomeIgnoreCaseContaining(nome);
        return new ModelAndView("produtos-sd/lista").addObject("produtos", resultados);
    }

    @ModelAttribute("categorias")
    public List<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }

}
