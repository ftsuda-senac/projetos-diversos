/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring.produto.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Categoria;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.ImagemProduto;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Produto;
import br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio.CategoriaRepositorySpringData;
import br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio.ProdutoRepositorySpringData;

/**
 *
 * @author fernando.tsuda
 */
@Controller
@RequestMapping("/produto-sd")
public class ProdutoControllerSpringData {

    @Autowired
    private ProdutoRepositorySpringData produtoRepository;

    @Autowired
    private CategoriaRepositorySpringData categoriaRepository;

    @GetMapping
    public ModelAndView listar(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "qtd", defaultValue = "500") int qtd,
            @RequestParam(name = "idsCat", required = false) List<Integer> idsCat) {
        List<Produto> resultados;
        if (idsCat != null && !idsCat.isEmpty()) {
            // Busca pelos IDs das categorias informadas
            resultados = produtoRepository.findByCategorias_IdIn(idsCat);
        } else {
            // Lista todos os produtos
            resultados = produtoRepository.findAll();
        }
        return new ModelAndView("produto-sd/lista").addObject("produtos", resultados);
    }

    @GetMapping("/paginacao")
    public ModelAndView listarPaginacao(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "qtd", defaultValue = "500") int qtd,
            @RequestParam(name = "idsCat", required = false) List<Integer> idsCat) {
        Page<Produto> resultados;
        // Calcula a página a ser apresentada
        int pagina = offset / qtd;
        if (idsCat != null && !idsCat.isEmpty()) {
            // Busca pelos IDs das categorias informadas
            resultados =
                    produtoRepository.findAllByCategorias_IdIn(idsCat, PageRequest.of(pagina, qtd));
        } else {
            // Lista todos os produtos usando paginacao
            resultados = produtoRepository.findAll(PageRequest.of(pagina, qtd));
        }
        return new ModelAndView("produto-sd/lista").addObject("produtos", resultados.getContent());
    }

    @GetMapping("/novo")
    public ModelAndView adicionarNovo() {
        return new ModelAndView("produto-sd/form").addObject("produto", new Produto());
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable("id") long id) {
        Optional<Produto> optProd = produtoRepository.findById(id);
        if (optProd.isPresent()) {
            Produto prod = optProd.get();
            if (prod.getCategorias() != null && !prod.getCategorias().isEmpty()) {
                Set<Integer> idsCategorias = new HashSet<>();
                for (Categoria cat : prod.getCategorias()) {
                    idsCategorias.add(cat.getId());
                }
                prod.setIdsCategorias(idsCategorias);
            }
            if (prod.getImagens() != null && !prod.getImagens().isEmpty()) {
                prod.setImagensList(new ArrayList<>(prod.getImagens()));
            }
            return new ModelAndView("produto-sd/form").addObject("produto", prod);
        }
        ModelAndView notFound = new ModelAndView("redirect:/produto-sd");
        notFound.setStatus(HttpStatus.NOT_FOUND);
        return notFound;
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@ModelAttribute("produto") @Valid Produto produto,
            BindingResult bindingResult, RedirectAttributes redirAttr) {
        produto.setDtCadastro(LocalDateTime.now());
        if (produto.getIdsCategorias() != null && !produto.getIdsCategorias().isEmpty()) {
            Set<Categoria> categoriasSelecionadas = new HashSet<>();
            for (Integer idCat : produto.getIdsCategorias()) {
                Optional<Categoria> optCat = categoriaRepository.findById(idCat);
                if (optCat.isPresent()) {
                    Categoria cat = optCat.get();
                    categoriasSelecionadas.add(cat);
                    cat.setProdutos(new HashSet<>(Arrays.asList(produto)));
                }
            }
            produto.setCategorias(categoriasSelecionadas);
        }
        if (produto.getImagensList() != null && !produto.getImagensList().isEmpty()) {
            Set<ImagemProduto> imagens = new LinkedHashSet<>();
            for (ImagemProduto img : produto.getImagensList()) {
                img.setProduto(produto);
                imagens.add(img);
            }
            produto.setImagens(imagens);
        }
        produtoRepository.save(produto);
        redirAttr.addFlashAttribute("msgSucesso",
                "Produto " + produto.getNome() + " salvo com sucesso");
        return new ModelAndView("redirect:/produto-sd");
    }

    @PostMapping("/{id}/remover")
    public ModelAndView remover(@PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {
        produtoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("msgSucesso",
                "Produto ID " + id + " removido com sucesso");
        return new ModelAndView("redirect:/produto-sd");
    }

    @GetMapping("/busca")
    public ModelAndView listarFiltrarPorNome(@RequestParam("nome") String nome) {
        List<Produto> resultados = produtoRepository.findByNomeIgnoreCaseContaining(nome);
        return new ModelAndView("produto-sd/lista").addObject("produtos", resultados);
    }

    @ModelAttribute("categorias")
    public List<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }

}
