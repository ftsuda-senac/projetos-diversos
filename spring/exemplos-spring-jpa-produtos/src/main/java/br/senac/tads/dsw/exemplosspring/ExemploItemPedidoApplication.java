package br.senac.tads.dsw.exemplosspring;

import br.senac.tads.dsw.exemplosspring.item.service.PedidoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExemploItemPedidoApplication /*implements CommandLineRunner */{

    @Autowired
    private PedidoItemService pedidoItemService;

    public static void main(String[] args) {
        SpringApplication.run(ExemploItemPedidoApplication.class, args);
    }

    /*
    @Override
    public void run(String... args) throws Exception {
        pedidoItemService.incluirItensPedidos();
        pedidoItemService.listarPedidos();
        pedidoItemService.alterarPedido(3L);
        pedidoItemService.listarPedidos();
    }
*/

}
