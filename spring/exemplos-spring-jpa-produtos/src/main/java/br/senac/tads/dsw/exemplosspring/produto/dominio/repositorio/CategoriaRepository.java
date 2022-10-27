package br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio;

import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {

    List<Categoria> findAll();

    Optional<Categoria> findById(Integer id);

    Categoria save(Categoria cat);

}
