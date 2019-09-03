/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.pi3.exemplos.web2.service;

import br.senac.tads.pi3.exemplos.web2.Categoria;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fernando.tsuda
 */
public class CategoriaService {

    private final Map<Integer, Categoria> categorias = new LinkedHashMap<>();

    public CategoriaService() {
        categorias.put(1, new Categoria("Categoria 1"));
        categorias.put(2, new Categoria("Categoria 2"));
        categorias.put(3, new Categoria("Categoria 3"));
        categorias.put(4, new Categoria("Categoria 4"));
        categorias.put(5, new Categoria("Categoria 5"));
    }

    public List<Categoria> listar() {
        return new ArrayList<>(categorias.values());
    }

    public Categoria findById(int id) {
        return categorias.get(id);
    }

}
