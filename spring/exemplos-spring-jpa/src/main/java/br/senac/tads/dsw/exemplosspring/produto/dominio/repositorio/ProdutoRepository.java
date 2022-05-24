package br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio;

import java.util.List;

import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Produto;

public interface ProdutoRepository {

    List<Produto> findAll(int offset, int quantidade);

    List<Produto> findByCategoria(List<Integer> idsCat, int offset, int quantidade);

    Produto findById(Integer id);

    Produto save(Produto p);

    void deleteById(Integer id);

}
