package br.senac.tads.dsw.exemplodatarest.dominio;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// path sem "/"
@RepositoryRestResource(path = "pessoas")
public interface DadosPessoaisRepository extends JpaRepository<DadosPessoais, Integer> {

    Page<DadosPessoais> findDistinctByInteresses_IdIn(List<Integer> idsInteresses, Pageable pageable);
   
    Optional<DadosPessoais> findByApelidoIgnoringCase(String apelido);

}
