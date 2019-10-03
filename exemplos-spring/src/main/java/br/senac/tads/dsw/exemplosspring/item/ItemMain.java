package br.senac.tads.dsw.exemplosspring.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

// Ver https://stackoverflow.com/questions/4787719/spring-console-application-configured-using-annotations
// Ver http://zetcode.com/spring/annotationconfigapplicationcontext/
@Component
public class ItemMain {

    @Autowired
    private ItemService itemService;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(); // Use annotated beans from the specified package
        ctx.scan("br.senac.tads.dsw.exemplosspring.item");
        ctx.refresh();
        
        ItemMain main = (ItemMain)ctx.getBean(ItemMain.class);
        main.run(args);
    }

    public void run(String[] args) {
        List<Item> itens = itemService.findAll();
        if (itens != null && !itens.isEmpty()) {
            for (Item item : itens) {
                System.out.println(String.format("Item id=%d, nome=%s, valor=%d, dataHora=%s", item.getId(), item.getNome(), item.getValor(), item.getDataHora()));
            }
        }
    }
}
