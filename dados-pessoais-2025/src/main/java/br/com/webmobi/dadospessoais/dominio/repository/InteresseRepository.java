package br.com.webmobi.dadospessoais.dominio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;

public interface InteresseRepository extends JpaRepository<InteresseEntity, Integer> {

    boolean existsByNomeNormalizado(String nome);

    Optional<InteresseEntity> findByNomeIgnoreCase(String nome);

    List<InteresseEntity> findByIdIn(List<Integer> ids);

}
