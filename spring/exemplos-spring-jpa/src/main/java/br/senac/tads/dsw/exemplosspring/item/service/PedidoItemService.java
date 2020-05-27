package br.senac.tads.dsw.exemplosspring.item.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senac.tads.dsw.exemplosspring.item.dominio.entidade.Endereco;
import br.senac.tads.dsw.exemplosspring.item.dominio.entidade.Item;
import br.senac.tads.dsw.exemplosspring.item.dominio.entidade.Pedido;
import br.senac.tads.dsw.exemplosspring.item.dominio.entidade.PedidoItem;
import br.senac.tads.dsw.exemplosspring.item.dominio.repositorio.ItemRepository;
import br.senac.tads.dsw.exemplosspring.item.dominio.repositorio.PedidoItemRepository;
import br.senac.tads.dsw.exemplosspring.item.dominio.repositorio.PedidoRepository;

@Service
@Transactional
public class PedidoItemService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PedidoItemService.class);
	
	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private PedidoItemRepository pedidoItemRepository;
	
	public void listarPedidos() {
		List<Pedido> listaPedidos = pedidoRepository.findAll();
		if (listaPedidos != null && !listaPedidos.isEmpty()) {
			LOGGER.debug("Lista de pedidos realizados:");
			for (Pedido ped : listaPedidos) {
				LOGGER.debug("ID: {}", ped.getId());
				LOGGER.debug("Código: {}", ped.getCodigo());
				LOGGER.debug("Data e hora: {}", ped.getDataHoraFechamento());
				if (ped.getEnderecoEntrega() != null) {
					Endereco end = ped.getEnderecoEntrega();
					LOGGER.debug("Endereco de entrega:");
					if (end.getComplemento() != null) {
						LOGGER.debug("{} - {}", end.getLogradouro(), end.getComplemento());
					} else {
						LOGGER.debug("{}", end.getLogradouro());
					}
					LOGGER.debug("{}", end.getBairro());
					LOGGER.debug("{} - {}", end.getCidade(), end.getEstado());
					LOGGER.debug("CEP: {}", end.getCep());
				}
				if (ped.getItens() != null && !ped.getItens().isEmpty()) {
					LOGGER.debug("===== Itens do pedido =====");
					for (PedidoItem pi : ped.getItens()) {
						Item item = pi.getItem();
						LOGGER.debug("{} - quantidade: {}, preço unitário: {}", item.getNome(), pi.getQuantidade(),
								item.getPreco());
					}
					LOGGER.debug("===========================\n");
				} else {
					LOGGER.debug("===== Pedido sem itens =====");
				}
			}
		}
	}

	public void incluirItensPedidos() {
		// Pedidos, Itens e Endereco

		// Adiciona 6 itens
		Item item1 = new Item();
		item1.setNome("Item 1");
		item1.setPreco(BigDecimal.valueOf(100.0));
		itemRepository.save(item1);
		LOGGER.debug(item1.toString());

		Item item2 = new Item();
		item2.setNome("Item 2");
		item2.setPreco(BigDecimal.valueOf(150.0));
		itemRepository.save(item2);
		LOGGER.debug(item2.toString());

		Item item3 = new Item();
		item3.setNome("Item 3");
		item3.setPreco(BigDecimal.valueOf(200.0));
		itemRepository.save(item3);
		LOGGER.debug(item3.toString());

		Item item4 = new Item();
		item4.setNome("Item 4");
		item4.setPreco(BigDecimal.valueOf(250.0));
		itemRepository.save(item4);
		LOGGER.debug(item4.toString());

		Item item5 = new Item();
		item5.setNome("Item 5");
		item5.setPreco(BigDecimal.valueOf(300.0));
		itemRepository.save(item5);
		LOGGER.debug(item5.toString());

		Item item6 = new Item();
		item6.setNome("Item 6");
		item6.setPreco(BigDecimal.valueOf(500.0));
		itemRepository.save(item6);
		LOGGER.debug(item6.toString());
		
		itemRepository.flush();
		
		// Adiciona 2 endereços
		Endereco endereco1 = new Endereco();
		endereco1.setLogradouro("Avenida Engenheiro Eusébio Stevaux, 823");
		endereco1.setBairro("Santo Amaro");
		endereco1.setCidade("São Paulo");
		endereco1.setEstado("SP");
		endereco1.setCep("04696000");

		Endereco endereco2 = new Endereco();
		endereco2.setLogradouro("Avenida Paulista, 900");
		endereco2.setBairro("Bela Vista");
		endereco2.setCidade("São Paulo");
		endereco2.setEstado("SP");
		endereco2.setCep("01310200");

		// ================================
		Pedido ped1 = new Pedido();
		ped1.setEnderecoEntrega(endereco2);
		endereco2.setPedido(ped1);
		pedidoRepository.save(ped1);

		PedidoItem pi11 = new PedidoItem(ped1, item1, 1);
		PedidoItem pi12 = new PedidoItem(ped1, item3, 1);
		PedidoItem pi13 = new PedidoItem(ped1, item5, 2);

		Set<PedidoItem> ped1Itens = new LinkedHashSet<PedidoItem>(Arrays.asList(pi11, pi12, pi13));
		ped1.setItens(ped1Itens);
		item1.setItens(ped1Itens);
		item3.setItens(ped1Itens);
		item5.setItens(ped1Itens);

		pedidoItemRepository.saveAll(ped1Itens);

		// ================================
		Pedido ped2 = new Pedido();
		ped2.setEnderecoEntrega(endereco1);
		endereco1.setPedido(ped2);
		pedidoRepository.save(ped2);

		PedidoItem pi21 = new PedidoItem(ped2, item1, 6);
		PedidoItem pi22 = new PedidoItem(ped2, item2, 2);
		PedidoItem pi23 = new PedidoItem(ped2, item3, 1);
		PedidoItem pi24 = new PedidoItem(ped2, item4, 1);

		Set<PedidoItem> ped2Itens = new LinkedHashSet<PedidoItem>(Arrays.asList(pi21, pi22, pi23, pi24));
		pedidoItemRepository.saveAll(ped2Itens);

		// ================================
		Pedido ped3 = new Pedido();
		ped3.setEnderecoEntrega(endereco1);
		endereco1.setPedido(ped3);
		pedidoRepository.save(ped3);

		PedidoItem pi31 = new PedidoItem(ped3, item5, 2);
		PedidoItem pi32 = new PedidoItem(ped3, item6, 2);

		Set<PedidoItem> ped3Itens = new LinkedHashSet<PedidoItem>(Arrays.asList(pi31, pi32));
		pedidoItemRepository.saveAll(ped3Itens);
	}

	public void alterarPedido(Long idPedido) {
		Pedido ped3 = pedidoRepository.findById(idPedido).get();
		pedidoItemRepository.deleteById_IdPedido(ped3.getId());

		Item item1 = itemRepository.findById(1L).get();
		Item item2 = itemRepository.findById(2L).get();
		Item item3 = itemRepository.findById(3L).get();

		PedidoItem pi31b = new PedidoItem(ped3, item1, 1);
		PedidoItem pi32b = new PedidoItem(ped3, item2, 2);
		PedidoItem pi33b = new PedidoItem(ped3, item3, 4);

		Set<PedidoItem> ped3bItens = new LinkedHashSet<PedidoItem>(Arrays.asList(pi31b, pi32b, pi33b));
		pedidoItemRepository.saveAll(ped3bItens);
	}
	
}
