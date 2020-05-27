package br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Categoria;

@Repository
public interface CategoriaRepositorySpringData extends JpaRepository<Categoria, Integer> {

}
