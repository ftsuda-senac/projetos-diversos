package br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio;

import java.util.List;

import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Categoria;

public interface CategoriaRepository {

	List<Categoria> findAll();

	Categoria findById(Integer id);

	Categoria save(Categoria cat);

}