/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring.crud;

import br.senac.tads.dsw.exemplosspring.item.Categoria;
import br.senac.tads.dsw.exemplosspring.item.CategoriaService;
import br.senac.tads.dsw.exemplosspring.item.Item;
import br.senac.tads.dsw.exemplosspring.item.ItemService;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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

/**
 *
 * @author ftsuda
 */
@Controller
@RequestMapping("/crud-item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ModelAndView listar(
            @RequestParam(value = "offset", defaultValue = "0") int offset, 
            @RequestParam(value = "qtd", defaultValue = "30") int qtd) {
        return new ModelAndView("crud/lista").addObject("itens", itemService.findAll());
    }

    @GetMapping("/incluir")
    public ModelAndView incluir() {
        return new ModelAndView("crud/form").addObject("item", new Item());
    }

    @GetMapping("/{id}/alterar")
    public ModelAndView alterar(@PathVariable("id") int id) {
        return new ModelAndView("crud/form").addObject("item", itemService.findById(id));
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@ModelAttribute("item") @Valid Item item, BindingResult bindingResult, RedirectAttributes redirAttr) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("crud/form");
        }
        if (item.getCategoriasIds() != null && item.getCategoriasIds().size() > 0) {
            Set<Categoria> categorias = new LinkedHashSet<>();
            for (Integer id : item.getCategoriasIds()) {
                categorias.add(categoriaService.findById(id));
            }
            item.setCategorias(categorias);
        }
        redirAttr.addFlashAttribute("msg", "Item " + item.getNome() + " salvo com sucesso");
        return new ModelAndView("redirect:/crud-item");
    }

    @PostMapping("{id}/remover")
    public ModelAndView remover(@PathVariable("id") int id, RedirectAttributes redirAttr) {
        redirAttr.addFlashAttribute("msg", "Item ID " + id + " removido com sucesso");
        return new ModelAndView("redirect:/crud-item");
    }

    @ModelAttribute("categorias")
    public List<Categoria> getCategorias() {
        return categoriaService.findAll();
    }

}
