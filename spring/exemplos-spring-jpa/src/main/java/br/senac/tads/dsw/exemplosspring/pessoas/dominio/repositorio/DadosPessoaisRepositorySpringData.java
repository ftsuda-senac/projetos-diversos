package br.senac.tads.dsw.exemplosspring.pessoas.dominio.repositorio;

import br.senac.tads.dsw.exemplosspring.pessoas.dominio.entidade.DadosPessoais;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface DadosPessoaisRepositorySpringData extends JpaRepository<DadosPessoais, Integer> {

    // Problema de duplicação de dados resultante do produto cartesiano dos Joins
    // Corrigido no método abaixo
    List<DadosPessoais> findByInteresses_IdIn(List<Integer> idsInteresses);

    List<DadosPessoais> findDistinctByInteresses_IdIn(List<Integer> idsInteresses);

    Page<DadosPessoais> findDistinctByInteresses_IdIn(List<Integer> idsInteresses, Pageable pageable);

    List<DadosPessoais> findDistinctByInteresses_IdInAndGenero(List<Integer> idsInteresses, int genero);

    // Usando JPQL customizada
    @Query("SELECT DISTINCT dp FROM DadosPessoais dp LEFT JOIN FETCH dp.interesses i LEFT JOIN FETCH dp.fotos WHERE i.id IN :interessesIds")
    List<DadosPessoais> findByInteresses(List<Integer> interessesIds);

    Optional<DadosPessoais> findByApelido(String apelido);

    Optional<DadosPessoais> findByApelidoIgnoringCase(String apelido);

    // Usando SQL nativo - USAR NOME DA TABELA E COLUNAS CONFORME CADASTRO NO BANCO
    @Query(nativeQuery = true, value = "SELECT * FROM PESSOA WHERE upper(APELIDO) = upper(:apelido)")
    Optional<DadosPessoais> findByApelidoSQL(@Param("apelido") String apelido);

}
