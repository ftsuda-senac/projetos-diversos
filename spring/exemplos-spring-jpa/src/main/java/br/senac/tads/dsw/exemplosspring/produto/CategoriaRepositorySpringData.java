package br.senac.tads.dsw.exemplosspring.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepositorySpringData extends JpaRepository<Categoria, Integer> {

}