package br.senac.tads.dsw.exemplos.dominio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface DadosPessoaisRepository extends JpaRepository<DadosPessoais, Integer> {
    
    Page<DadosPessoais> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Optional<DadosPessoais> findByApelido(String apelido);

    Page<DadosPessoais> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(String nome, String email, Pageable pageable);

    @Query("""
        SELECT dp FROM DadosPessoais dp
            WHERE UPPER(dp.nome) LIKE UPPER(concat('%', ?1,'%')) OR
                UPPER(dp.email) LIKE UPPER(concat('%', ?1,'%'))
            """)
    List<DadosPessoais> findComJpql(String termo);

    @Query(nativeQuery = true, value = """
        SELECT * FROM dados_pessoais
            WHERE nome ILIKE %?1% OR 
                email ILIKE %?1
            """)
    List<DadosPessoais> findComSqlNativo(String termo);

}
