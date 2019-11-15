/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.servletjsp.service;

import br.senac.tads.servletjsp.modelo.Produto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author fernando.tsuda
 */
public class ProdutoService {

    private static final Map<Long, Produto> MOCK_DATA = new ConcurrentHashMap<Long, Produto>();

    static {
        for (long i = 0; i < 120; i++) {
            MOCK_DATA.put(1L + 6 * i, new Produto(1L + 6 * i, "Bolacha " + i, "Bolacha " + i, new BigDecimal(1.5), new BigDecimal(2.0), "http://lorempixel.com/300/300/abstract/1"));
            MOCK_DATA.put(2L + 6 * i, new Produto(2L + 6 * i, "Cheetos " + i, "Cheetos " + i, new BigDecimal(4.0), new BigDecimal(5.0), "http://lorempixel.com/300/300/abstract/2"));
            MOCK_DATA.put(3L + 6 * i, new Produto(3L + 6 * i, "Sabonete " + i, "Sabonete " + i, new BigDecimal(0.5), new BigDecimal(0.9), "http://lorempixel.com/300/300/abstract/3"));
            MOCK_DATA.put(4L + 6 * i, new Produto(4L + 6 * i, "Shampoo " + i, "Shampoo " + i, new BigDecimal(9.3), new BigDecimal(12.9), "http://lorempixel.com/300/300/abstract/4"));
            MOCK_DATA.put(5L + 6 * i, new Produto(5L + 6 * i, "Refrigerante " + i, "Refrigerante " + i, new BigDecimal(3.0), new BigDecimal(4.5), "http://lorempixel.com/300/300/abstract/5"));
            MOCK_DATA.put(5L + 6 * i, new Produto(6L + 6 * i, "Cerveja " + i, "Cerveja " + i, new BigDecimal(4.0), new BigDecimal(6.0), "http://lorempixel.com/300/300/abstract/6"));
        }
    }

    public List<Produto> listar(int offset, int quantidade) {
        return new ArrayList<>(MOCK_DATA.values()).subList(offset, offset + quantidade);
    }

    public Produto obter(long idProduto) {
        return MOCK_DATA.get(idProduto);
    }
}
