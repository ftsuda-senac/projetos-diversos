package br.senac.tads.dsw.exemplospring.crud;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senac.tads.dsw.exemplospring.crud.item.Item;
import br.senac.tads.dsw.exemplospring.crud.item.ItemService;

@Controller
@Scope("session")
@RequestMapping("/carrinho")
public class CarrinhoSessaoController {
	
	@Autowired
	private ItemService itemService;
	
	private List<ItemSelecionado> itensSelecionados = new ArrayList<>();

	@GetMapping
	public ModelAndView mostrar() {
		return new ModelAndView("carrinho");
	}

	@PostMapping
	public ModelAndView adicionar(@RequestParam("itemId") int itemId, @RequestParam("qtd") int quantidade, RedirectAttributes redirAttr) {
		Item item = itemService.findById(itemId);
		itensSelecionados.add(new ItemSelecionado(item, quantidade));
		return new ModelAndView("redirect:/carrinho");
	}

	public List<ItemSelecionado> getItensSelecionados() {
		return itensSelecionados;
	}

	public void setItensSelecionados(List<ItemSelecionado> itensSelecionados) {
		this.itensSelecionados = itensSelecionados;
	}
	
	public int getQtdeItens() {
		return itensSelecionados.size();
	}
	
	

}
