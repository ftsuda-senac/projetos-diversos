package br.senac.tads.dsw.exemplorest.dominio;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DadosPessoaisRepository extends JpaRepository<DadosPessoais, Integer> {

    Page<DadosPessoais> findDistinctByInteresses_IdIn(List<Integer> idsInteresses, Pageable pageable);
   
    Optional<DadosPessoais> findByApelidoIgnoringCase(String apelido);

}
