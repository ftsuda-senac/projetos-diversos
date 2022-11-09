package br.senac.tads.dsw.exemplosspring.sessao.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.senac.tads.dsw.exemplosspring.item.Item;
import br.senac.tads.dsw.exemplosspring.item.ItemService;

@Controller
@Scope("session")
@RequestMapping("/exemplo-sessao3")
public class ExemploSessaoController3 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private transient ItemService itemService;

    private List<ItemSelecionado> itensSelecionados = new ArrayList<>();

    @GetMapping
    public ModelAndView mostrarTela() {
        return new ModelAndView("sessao-item/exemplo-sessao3").addObject("itens",
                itemService.findAll());
    }

    @PostMapping
    public ModelAndView adicionarItem(
            @ModelAttribute("itemId") Integer itemId,
            @RequestHeader("user-agent") String userAgent,
            RedirectAttributes redirAttr) {
        Item item = itemService.findById(itemId);
        itensSelecionados.add(new ItemSelecionado(item, userAgent));
        redirAttr.addFlashAttribute("msg", "Item ID " + item.getId() + " adicionado com sucesso");
        return new ModelAndView("redirect:/exemplo-sessao3");
    }

    @GetMapping("/limpar")
    public ModelAndView limparSelecionados(RedirectAttributes redirAttr) {
        itensSelecionados.clear();
        redirAttr.addFlashAttribute("msg", "Itens removidos");
        return new ModelAndView("redirect:/exemplo-sessao3");
    }

    public List<ItemSelecionado> getItensSelecionados() {
        return itensSelecionados;
    }

    @ModelAttribute("titulo")
    public String getTitulo() {
        return "Exemplo Sessao 3 - Uso do @Controller com @Scope(\"session\")";
    }

}
