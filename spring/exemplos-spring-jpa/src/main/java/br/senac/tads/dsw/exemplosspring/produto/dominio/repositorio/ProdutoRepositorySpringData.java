package br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Produto;

@Repository
public interface ProdutoRepositorySpringData extends JpaRepository<Produto, Long> {

	List<Produto> findByCategorias_IdIn(List<Integer> idsCategorias);

	List<Produto> findDistinctByCategorias_IdIn(List<Integer> idsCategorias);

	Optional<Produto> findByNome(String nome);

	List<Produto> findByPrecoVendaGreaterThanAndPrecoVendaLessThan(BigDecimal min, BigDecimal max);
}
