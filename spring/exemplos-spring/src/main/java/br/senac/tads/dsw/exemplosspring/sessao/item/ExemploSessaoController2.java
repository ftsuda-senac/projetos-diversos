package br.senac.tads.dsw.exemplosspring.sessao.item;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.senac.tads.dsw.exemplosspring.item.Item;
import br.senac.tads.dsw.exemplosspring.item.ItemService;

@Controller
@RequestMapping("/exemplo-sessao2")
public class ExemploSessaoController2 {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public ModelAndView mostrarTela() {
        return new ModelAndView("sessao-item/exemplo-sessao2").addObject("itens",
                itemService.findAll());
    }

    @PostMapping
    public ModelAndView adicionarItem(
            @ModelAttribute("itemId") Integer itemId,
            HttpServletRequest servletRequest,
            RedirectAttributes redirAttr) {
        Item item = itemService.findById(itemId);

        HttpSession sessao = servletRequest.getSession();
        if (sessao.getAttribute("itensSelecionados2") == null) {
            sessao.setAttribute("itensSelecionados2", new ArrayList<ItemSelecionado>());
        }
        List<ItemSelecionado> itensSelecionados =
                (ArrayList<ItemSelecionado>) sessao.getAttribute("itensSelecionados2");
        itensSelecionados.add(new ItemSelecionado(item, servletRequest.getHeader("user-agent")));
        redirAttr.addFlashAttribute("msg", "Item ID " + item.getId() + " adicionado com sucesso");
        return new ModelAndView("redirect:/exemplo-sessao2");
    }

    @GetMapping("/limpar")
    public ModelAndView limparSelecionados(HttpSession sessao,
            RedirectAttributes redirAttr) {
        if (sessao.getAttribute("itensSelecionados2") != null) {
            List<ItemSelecionado> itensSelecionados =
                    (ArrayList<ItemSelecionado>) sessao.getAttribute("itensSelecionados2");
            itensSelecionados.clear();
        }
        redirAttr.addFlashAttribute("msg", "Itens removidos");
        return new ModelAndView("redirect:/exemplo-sessao2");
    }

    @ModelAttribute("titulo")
    public String getTitulo() {
        return "Exemplo Sessao 2 - Uso do HttpSession do HttpServletRequest";
    }
}
