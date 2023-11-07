package br.senac.tads.dsw.exemplos.dominio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InteresseRepository extends JpaRepository<Interesse, Integer> {

    Optional<Interesse> findByNomeIgnoreCase(String nome);
}
