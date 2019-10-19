package br.senac.tads.dsw.exemplosspring;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.senac.tads.dsw.exemplosspring.item.domain.Endereco;
import br.senac.tads.dsw.exemplosspring.item.domain.Item;
import br.senac.tads.dsw.exemplosspring.item.domain.Pedido;
import br.senac.tads.dsw.exemplosspring.item.domain.PedidoItem;
import br.senac.tads.dsw.exemplosspring.item.repository.EnderecoRepository;
import br.senac.tads.dsw.exemplosspring.item.repository.ItemRepository;
import br.senac.tads.dsw.exemplosspring.item.repository.PedidoRepository;
import br.senac.tads.dsw.exemplosspring.produto.Categoria;
import br.senac.tads.dsw.exemplosspring.produto.CategoriaRepositorySpringData;
import br.senac.tads.dsw.exemplosspring.produto.ImagemProduto;
import br.senac.tads.dsw.exemplosspring.produto.Produto;
import br.senac.tads.dsw.exemplosspring.produto.ProdutoRepositorySpringData;

@SpringBootApplication
public class ExemplosSpringJpaApplication implements CommandLineRunner {
	
	@Autowired
	private CategoriaRepositorySpringData categoriaRepository;

	@Autowired
	private ProdutoRepositorySpringData produtoRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	public static void main(String[] args) {
		SpringApplication.run(ExemplosSpringJpaApplication.class, args);
	}
	
	private Produto construirProduto(String nomeProduto, BigDecimal precoCompra, BigDecimal precoVenda, int quantidade,
			Set<ImagemProduto> imagens, Set<Categoria> categorias) {
		Produto p = new Produto(nomeProduto, "Descrição do produto " + nomeProduto, precoCompra, precoVenda, quantidade,
				true, LocalDateTime.now());
		if (imagens != null && !imagens.isEmpty()) {
			for (ImagemProduto img : imagens) {
				img.setProduto(p);
			}
			p.setImagens(imagens);
		}
		if (categorias != null && !categorias.isEmpty()) {
			for (Categoria cat : categorias) {
				cat.setProdutos(new LinkedHashSet<>(Arrays.asList(p)));
			}
			p.setCategorias(categorias);
		}
		return p;
	}

	@Override
	public void run(String... args) throws Exception {
		
		List<Categoria> categoriasBd = categoriaRepository.findAll();
		if (categoriasBd != null && !categoriasBd.isEmpty()) {
			return;
		}
		
		List<Categoria> todasCategorias = new ArrayList<>();
		// Adiciona 5 categorias
		for (int i = 1; i < 6; i++) {
			Categoria cat = categoriaRepository.save(new Categoria("Categoria " + i));
			todasCategorias.add(cat);
		}

		String nomeProduto;
		Produto p;
		Set<ImagemProduto> imagens;
		Set<Categoria> categorias;

		// Adiciona 6*5 produtos
		for (int i = 1; i < 11; i++) {
			nomeProduto = "Eletrônico " + i;
			imagens = new LinkedHashSet<>();
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/technics/1/", "technics 1"));
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/technics/2/", "technics 2"));
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/technics/7/", "technics 7"));
			categorias = new LinkedHashSet<>();
			categorias.add(todasCategorias.get(0));
			categorias.add(todasCategorias.get(1));
			p = construirProduto(nomeProduto, new BigDecimal(100.0), new BigDecimal(200.0), 100, imagens, categorias);
			produtoRepository.save(p);

			nomeProduto = "Roupa " + i;
			imagens = new LinkedHashSet<>();
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/fashion/1/", "fashion 1"));
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/fashion/3/", "fashion 3"));
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/fashion/6/", "fashion 6"));
			
			categorias = new LinkedHashSet<>();
			categorias.add(todasCategorias.get(2));
			categorias.add(todasCategorias.get(3));
			p = construirProduto(nomeProduto, new BigDecimal(40.0), new BigDecimal(70.0), 250, imagens, categorias);
			produtoRepository.save(p);
			

			nomeProduto = "Viagem " + i;
			imagens = new LinkedHashSet<>();
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/animals/1/", "animals 1"));
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/animals/2/", "animals 2"));
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/animals/4/", "animals 4"));
			categorias = new LinkedHashSet<>();
			categorias.add(todasCategorias.get(4));
			p = construirProduto(nomeProduto, new BigDecimal(800.0), new BigDecimal(1100.0), 20, imagens, categorias);
			produtoRepository.save(p);

			nomeProduto = "Esporte " + i;
			imagens = new LinkedHashSet<>();
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/sports/2/", "sports 2"));
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/sports/3/", "sports 3"));
			categorias = new LinkedHashSet<>();
			categorias.add(todasCategorias.get(0));
			categorias.add(todasCategorias.get(2));
			p = construirProduto(nomeProduto, new BigDecimal(200.0), new BigDecimal(300.0), 180, imagens, categorias);
			produtoRepository.save(p);

			nomeProduto = "Comida " + i;
			imagens = new LinkedHashSet<>();
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/food/1/", "food 1"));
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/food/3/", "food 3"));
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/food/9/", "food 9"));
			categorias = new LinkedHashSet<>();
			categorias.add(todasCategorias.get(1));
			categorias.add(todasCategorias.get(3));
			categorias.add(todasCategorias.get(4));
			p = construirProduto(nomeProduto, new BigDecimal(12.0), new BigDecimal(30.0), 680, imagens, categorias);
			produtoRepository.save(p);

			nomeProduto = "Diversão " + i;
			imagens = new LinkedHashSet<>();
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/nightlife/2/", "nightlife 2"));
			imagens.add(new ImagemProduto("http://lorempixel.com/300/300/nightlife/10/", "nightlife 10"));
			categorias = new LinkedHashSet<>();
			categorias.add(todasCategorias.get(2));
			categorias.add(todasCategorias.get(4));
			p = construirProduto(nomeProduto, new BigDecimal(185.0), new BigDecimal(450.0), 310, imagens, categorias);
			produtoRepository.save(p);
		}
		
		// Pedidos, Itens e Endereco
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
		
		// Adiciona 6 itens
		Item item1 = new Item();
		item1.setNome("Item 1");
		item1.setPreco(BigDecimal.valueOf(100.0));
		itemRepository.save(item1);
		
		Item item2 = new Item();
		item2.setNome("Item 2");
		item2.setPreco(BigDecimal.valueOf(150.0));
		itemRepository.save(item2);
		
		Item item3 = new Item();
		item3.setNome("Item 3");
		item3.setPreco(BigDecimal.valueOf(200.0));
		itemRepository.save(item3);
		
		Item item4 = new Item();
		item4.setNome("Item 4");
		item4.setPreco(BigDecimal.valueOf(250.0));
		itemRepository.save(item4);
		
		Item item5 = new Item();
		item5.setNome("Item 5");
		item5.setPreco(BigDecimal.valueOf(300.0));
		itemRepository.save(item5);
		
		Item item6 = new Item();
		item6.setNome("Item 6");
		item6.setPreco(BigDecimal.valueOf(500.0));
		itemRepository.save(item6);
		
		Pedido ped = new Pedido();
		ped.setCodigo("19-000001");
		ped.setEnderecoEntrega(endereco2);
		endereco2.setPedido(ped);
		
		PedidoItem pi1 = new PedidoItem();
		pi1.setPedido(ped);
		pi1.setItem(item1);
		
		PedidoItem pi2 = new PedidoItem();
		pi2.setPedido(ped);
		pi2.setItem(item3);
		
		PedidoItem pi3 = new PedidoItem();
		pi3.setPedido(ped);
		pi3.setItem(item5);
		
		Set<PedidoItem> pedItens = new LinkedHashSet<PedidoItem>(Arrays.asList(pi1, pi2, pi3));
		ped.setItens(pedItens);
		pedidoRepository.save(ped);
		

		
	}

}
