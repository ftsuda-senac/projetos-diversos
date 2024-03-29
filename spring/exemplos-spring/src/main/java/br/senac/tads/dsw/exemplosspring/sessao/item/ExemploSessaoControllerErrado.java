package br.senac.tads.dsw.exemplosspring.sessao.item;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/exemplo-sessao-errado")
public class ExemploSessaoControllerErrado {

    @Autowired
    private ItemService itemService;

    // NÃO COLOCAR ATRIBUTO DE INSTÂNCIA NOS CONTROLLERS POIS PODE CAUSAR
    // COMPORTAMENTO ERRADO DA APLICAÇÃO.
    // O OBJETO CONTROLLER PODE SER COMPARTILHADO ENTRE REQUISICOES DE DIFERENTES USUÁRIOS
    private List<ItemSelecionado> itensSelecionados = new ArrayList<>();

    @GetMapping
    public ModelAndView mostrarTela() {
        return new ModelAndView("sessao-item/exemplo-sessao-errado").addObject("itens",
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
        return new ModelAndView("redirect:/exemplo-sessao-errado");
    }

    @GetMapping("/limpar")
    public ModelAndView limparSelecionados(RedirectAttributes redirAttr) {
        itensSelecionados.clear();
        redirAttr.addFlashAttribute("msg", "Itens removidos");
        return new ModelAndView("redirect:/exemplo-sessao-errado");
    }

    @ModelAttribute("itensSelecionados")
    public List<ItemSelecionado> getItensSelecionados() {
        return itensSelecionados;
    }

    @ModelAttribute("titulo")
    public String getTitulo() {
        return "Exemplo Sessao ERRADO";
    }
}
