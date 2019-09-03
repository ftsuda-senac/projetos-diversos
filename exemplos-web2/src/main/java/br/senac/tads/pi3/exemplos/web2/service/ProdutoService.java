/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.pi3.exemplos.web2.service;

import br.senac.tads.pi3.exemplos.web2.Produto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fernando.tsuda
 */
public class ProdutoService {
    
    public Map<Long, Produto> produtos = new LinkedHashMap<>();
    
    private CategoriaService categoriaService = new CategoriaService();

    public ProdutoService() {
        Produto p1 = new Produto(1L, "Bolo de chocolate",
                "descrição do bolo de chocolate", new BigDecimal(30.0),
                "http://lorempixel.com/200/200/food/10/");
        p1.setCategorias(Arrays.asList(categoriaService.findById(1)));
        produtos.put(1L, p1);
        Produto p2 = new Produto(2L, "Bolo de cenoura",
                "descrição do bolo de cenoura", new BigDecimal(20.0),
                "http://lorempixel.com/200/200/food/10/");
        p2.setCategorias(Arrays.asList(categoriaService.findById(2), categoriaService.findById(3)));
        produtos.put(2L, p2);
        Produto p3 = new Produto(3L, "Torta de morango",
                "descrição da torta de morango", new BigDecimal(25.0),
                "http://lorempixel.com/200/200/food/10/");
        p3.setCategorias(Arrays.asList(categoriaService.findById(1), categoriaService.findById(3)));
        produtos.put(3L, p3);
        Produto p4 = new Produto(4L, "Bolo de doce de leite",
                "descrição do bolo de doce de leite", new BigDecimal(30.0),
                "http://lorempixel.com/200/200/food/10/");
        produtos.put(4L, p4);
        Produto p5 = new Produto(5L, "Bolo de paçoca",
                "descrição do bolo de paçoca", new BigDecimal(42.0),
                "http://lorempixel.com/200/200/food/10/");
        p5.setCategorias(Arrays.asList(categoriaService.findById(5)));
        produtos.put(5L, p5);
    }

    public List<Produto> listar() {
        return new ArrayList<>(produtos.values());
    }
}
