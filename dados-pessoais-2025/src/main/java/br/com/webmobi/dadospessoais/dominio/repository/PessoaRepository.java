package br.com.webmobi.dadospessoais.dominio.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;

public interface PessoaRepository extends JpaRepository<PessoaEntity, Integer> {

	boolean existsByPublicId(UUID email);

	boolean existsByUsername(String username);

	Optional<PessoaEntity> findByPublicId(UUID publicId);

	Optional<PessoaEntity> findByUsername(String username);

	@Modifying
	void deleteByPublicId(UUID publicId);

}
