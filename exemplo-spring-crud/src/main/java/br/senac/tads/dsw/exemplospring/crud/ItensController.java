package br.senac.tads.dsw.exemplospring.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.senac.tads.dsw.exemplospring.crud.item.Item;
import br.senac.tads.dsw.exemplospring.crud.item.ItemService;

@Controller
@RequestMapping("/itens")
public class ItensController {

	@Autowired
	private ItemService itemService;

	@GetMapping
	public ModelAndView mostrar() {
		return new ModelAndView("itens");
	}

	@ModelAttribute("itens")
	public List<Item> obterItens() {
		return itemService.findAll();
	}

}
