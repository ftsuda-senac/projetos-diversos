package br.senac.tads.dsw.exemplosspring.item;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

// Ver https://stackoverflow.com/questions/4787719/spring-console-application-configured-using-annotations
// Ver http://zetcode.com/spring/annotationconfigapplicationcontext/
@Component
public class ItemMain {

	@Autowired
	private ItemService itemService;

	private String listCategorias(Set<Categoria> categorias) {
		if (categorias != null && !categorias.isEmpty()) {
			StringBuilder strBuilder = new StringBuilder();
			for (Categoria cat : categorias) {
				strBuilder.append(cat.getNome());
				strBuilder.append(", ");
			}
			return strBuilder.substring(0, strBuilder.length() - 2);
		}
		return "";
	}

	public void run(String[] args) {
		List<Item> itens = itemService.findAll();
		if (itens != null && !itens.isEmpty()) {
			for (Item item : itens) {
				System.out.println(String.format("Item id=%d, nome=%s, valor=%d, dataHora=%s, categorias=[%s]", item.getId(),
						item.getNome(), item.getValor(), item.getDataHora(), listCategorias(item.getCategorias())));
			}
		}
	}

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(); // Use annotated beans from the specified package
		ctx.scan("br.senac.tads.dsw.exemplosspring.item");
		ctx.refresh();

		ItemMain main = (ItemMain) ctx.getBean(ItemMain.class);
		main.run(args);
		ctx.close();
	}

}
