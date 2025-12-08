package br.com.webmobi.dadospessoais.dominio.dto;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;

public record PessoaDto (
		UUID id,
		String username,
		String nome,
		String email,
		String telefone,
		LocalDate dataNascimento,
		List<String> interesses,
		@JsonIgnore List<PessoaFotoDto> pessoasFotos,
		@JsonProperty("fotos") List<FotoDto> fotosUrl) {

	@JsonCreator
	public PessoaDto(UUID id, String username, String nome, String email, String telefone, LocalDate dataNascimento,
			List<String> interesses, @JsonProperty("fotos") List<FotoDto> fotosUrl) {
		this(id, username, nome, email, telefone, dataNascimento, interesses, null, fotosUrl);
	}

	public PessoaDto(PessoaEntity pessoaEntity) {
		this(pessoaEntity.getPublicId(), pessoaEntity.getUsername(), pessoaEntity.getNome(), pessoaEntity.getEmail(),
				pessoaEntity.getTelefone(), pessoaEntity.getDataNascimento(),
				pessoaEntity.getInteresses().stream().map(InteresseEntity::getNome).toList(),
				pessoaEntity.getFotos() != null ? pessoaEntity.getFotos().stream().map(PessoaFotoDto::new).toList()
						: Collections.emptyList(),
				null);
	}

	public PessoaDto(PessoaDto original, List<FotoDto> fotos) {
		this(original.id(), original.username(), original.nome(), original.email(), original.telefone(),
				original.dataNascimento(), original.interesses(), null, fotos);
	}

}
