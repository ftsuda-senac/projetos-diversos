/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosrest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author fernando.tsuda
 */
@Service
public class ItemService {

    // Mantém alguns itens com dados aleatórios
    private final static Map<Integer, Item> ITENS = new LinkedHashMap<>();

    static {
        for (int i = 0; i < 100; i++) {
            ITENS.put(i, new Item(i));
        }
    }

    public List<Item> findAll() {
        return new ArrayList<>(ITENS.values());
    }

    public Item findById(Integer id) {
        return ITENS.get(id);
    }

}
