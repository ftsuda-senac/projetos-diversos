package br.com.webmobi.dadospessoais.dominio.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.webmobi.dadospessoais.dominio.entity.PessoaFotoEntity;

public interface PessoaFotoRepository extends JpaRepository<PessoaFotoEntity, Integer> {

    List<PessoaFotoEntity> findByPessoa_PublicId(UUID pessoaId);

    Optional<PessoaFotoEntity> findByPessoa_PublicIdAndNomeArquivo(UUID pessoaId, String nomeArquivo);

}
