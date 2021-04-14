package br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Produto;

@Repository
public interface ProdutoRepositorySpringData extends JpaRepository<Produto, Long> {

	List<Produto> findByCategorias_IdIn(List<Integer> idsCategorias);

    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-property-expressions
	List<Produto> findDistinctByCategorias_IdIn(List<Integer> idsCategorias);
	
    Page<Produto> findAllByCategorias_IdIn(List<Integer> ids, Pageable page);

	Optional<Produto> findByNome(String nome);
	
    // USAR JPQL COM Spring Data JPA
    @Query("SELECT p FROM Produto p WHERE upper(p.nome) LIKE UPPER('%'||:termoBusca||'%')")
    List<Produto> buscaPorNomeJpql(@Param("termoBusca") String nome);

    // USAR SQL NATIVO COM Spring Data JPA
    @Query(value = "SELECT * FROM PRODUTO WHERE upper(nome) LIKE upper('%'||:termoBusca||'%')", nativeQuery = true)
    List<Produto> buscaPorNomeSql(@Param("termoBusca") String nome);
    
	List<Produto> findByPrecoVendaGreaterThanAndPrecoVendaLessThan(BigDecimal min, BigDecimal max);
}
