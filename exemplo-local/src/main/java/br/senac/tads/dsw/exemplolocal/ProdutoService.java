/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplolocal;

import java.util.List;

/**
 *
 * @author fernando.tsuda
 */
public interface ProdutoService {

    List<Produto> findAll(int offset, int quantidade);

    Produto findById(long id);

    Produto save(Produto produto);
    
}




