package br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio;

import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Categoria;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.ImagemProduto;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Produto;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

// @Service
public class ProdutoRepositoryMockImpl implements ProdutoRepository {

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Map<Integer, Produto> mapItens;

    private int sequenciaId = 0;
    
    private Set<Categoria> buildCategorias(Integer... catIds) {
        List<Integer> categoriasIds = Arrays.asList(catIds);
        Set<Categoria> categorias = new HashSet<>();
        for (Integer catId  :categoriasIds) {
            Optional<Categoria> optCat = categoriaRepository.findById(catId);
            if (optCat.isPresent()) {
                categorias.add(optCat.get());
            }
        }
        return categorias;
    }

    @PostConstruct
    public void init() {
        mapItens = new ConcurrentHashMap<>();
        for (int i = 1; i < 6; i++) {

            String nomeProduto = "Eletrônico " + i;
            Produto p = new Produto(++sequenciaId, nomeProduto,
                    "Descrição do produto " + nomeProduto, new BigDecimal(100.0),
                    new BigDecimal(200.0), 100, true, LocalDateTime.now(),
                    new LinkedHashSet<>(Arrays.asList(
                            new ImagemProduto("http://lorempixel.com/300/300/technics/1/",
                                    "technics 1"),
                            new ImagemProduto("http://lorempixel.com/300/300/technics/2/",
                                    "technics 2"),
                            new ImagemProduto("http://lorempixel.com/300/300/technics/7/",
                                    "technics 7"))),
                    buildCategorias(1, 2));
            mapItens.put(p.getId(), p);

            nomeProduto = "Roupa " + i;
            p = new Produto(++sequenciaId, nomeProduto, "Descrição do produto " + nomeProduto,
                    new BigDecimal(40.0), new BigDecimal(70.0), 200, true, LocalDateTime.now(),
                    new LinkedHashSet<>(Arrays.asList(
                            new ImagemProduto("http://lorempixel.com/300/300/fashion/1/",
                                    "fashion 1"),
                            new ImagemProduto("http://lorempixel.com/300/300/fashion/3/",
                                    "fashion 3"),
                            new ImagemProduto("http://lorempixel.com/300/300/fashion/6/",
                                    "fashion 6"))),
                    buildCategorias(3, 4));
            mapItens.put(p.getId(), p);

            nomeProduto = "Viagem " + i;
            p = new Produto(++sequenciaId, nomeProduto, "Descrição do produto " + nomeProduto,
                    new BigDecimal(800.0), new BigDecimal(1100.0), 20, true, LocalDateTime.now(),
                    new LinkedHashSet<>(Arrays.asList(
                            new ImagemProduto("http://lorempixel.com/300/300/animals/1/",
                                    "animals 1"),
                            new ImagemProduto("http://lorempixel.com/300/300/animals/2/",
                                    "animals 2"),
                            new ImagemProduto("http://lorempixel.com/300/300/animals/4/",
                                    "animals 4"))),
                    buildCategorias(5));
            mapItens.put(p.getId(), p);

            nomeProduto = "Esporte " + i;
            p = new Produto(++sequenciaId, nomeProduto, "Descrição do produto " + nomeProduto,
                    new BigDecimal(200.0), new BigDecimal(300.0), 150, true, LocalDateTime.now(),
                    new LinkedHashSet<>(Arrays.asList(
                            new ImagemProduto("http://lorempixel.com/300/300/sports/2/",
                                    "sports 2"),
                            new ImagemProduto("http://lorempixel.com/300/300/sports/3/",
                                    "sports 3"))),
                    buildCategorias(1, 3));
            mapItens.put(p.getId(), p);

            nomeProduto = "Comida " + i;
            p = new Produto(++sequenciaId, nomeProduto, "Descrição do produto " + nomeProduto,
                    new BigDecimal(15.0), new BigDecimal(30.0), 500, true, LocalDateTime.now(),
                    new LinkedHashSet<>(Arrays.asList(
                            new ImagemProduto("http://lorempixel.com/300/300/food/1/", "food 1"),
                            new ImagemProduto("http://lorempixel.com/300/300/food/3/", "food 3"),
                            new ImagemProduto("http://lorempixel.com/300/300/food/9/", "food 9"))),
                    buildCategorias(2, 5));
            mapItens.put(p.getId(), p);

            nomeProduto = "Diversão " + i;
            p = new Produto(++sequenciaId, nomeProduto, "Descrição do produto " + nomeProduto,
                    new BigDecimal(120.0), new BigDecimal(350.0), 150, true, LocalDateTime.now(),
                    new LinkedHashSet<>(Arrays.asList(
                            new ImagemProduto("http://lorempixel.com/300/300/nightlife/2/",
                                    "nightlife 2"),
                            new ImagemProduto("http://lorempixel.com/300/300/nightlife/10/",
                                    "nightlife 10"))),
                    buildCategorias(3, 5));
            mapItens.put(p.getId(), p);
        }
    }

    @Override
    public List<Produto> findAll(int pagina, int quantidade) {
        int offset = pagina * quantidade;
        return new ArrayList<>(mapItens.values()).subList(offset,
                Math.min(offset + quantidade, mapItens.size()));
    }

    @Override
    public List<Produto> findByCategoria(List<Integer> idsCat, int pagina, int quantidade) {
        int offset = pagina * quantidade;
        return new ArrayList<>(mapItens.values()).subList(offset,
                Math.min(offset + quantidade, mapItens.size()));
    }

    @Override
    public Optional<Produto> findById(Integer id) {
        return Optional.ofNullable(mapItens.get(id));
    }

    @Override
    public Produto save(Produto p) {
        if (p.getId() == null) {
            p.setId(++sequenciaId);
            p.setDtCadastro(LocalDateTime.now());
        }
        mapItens.put(p.getId(), p);
        return p;
    }

    @Override
    public void deleteById(Integer id) {
        mapItens.remove(id);
    }

}
