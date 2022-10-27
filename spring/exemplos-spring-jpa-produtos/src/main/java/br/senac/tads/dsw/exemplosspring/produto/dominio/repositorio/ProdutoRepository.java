package br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio;

import java.util.List;

import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Produto;
import java.util.Optional;

public interface ProdutoRepository {

    List<Produto> findAll(int pagina, int quantidade);

    List<Produto> findByCategoria(List<Integer> idsCat, int pagina, int quantidade);

    Optional<Produto> findById(Integer id);

    Produto save(Produto p);

    void deleteById(Integer id);

}
