package br.com.webmobi.dadospessoais.dominio.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;
import br.com.webmobi.dadospessoais.dominio.validacao.UsernameUnico;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Usando class ao invés do record para compatibiilidade com Thymeleaf
@Getter
@Setter
@NoArgsConstructor
public class PessoaMvcDto {

	private UUID id;

	@NotBlank
	@Size(max = 64)
	@UsernameUnico
	private String username;

	@NotBlank
	@Size(max = 100)
	private String nome;

	@NotBlank
	@Email
	@Size(max = 100)
	private String email;

	private String telefone;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // ISO-8601
	@PastOrPresent
	private LocalDate dataNascimento;

	@NotBlank
	private String senha;

	private String senhaConfirmacao;

	@Size(min = 1)
	private List<Integer> interessesIds;

	public PessoaMvcDto(UUID id, @NotBlank String username, String nome, String email, String telefone,
			LocalDate dataNascimento, List<Integer> interessesIds) {
		this.id = id;
		this.username = username;
		;
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		this.dataNascimento = dataNascimento;
		this.interessesIds = interessesIds;
	}

	public PessoaMvcDto(PessoaEntity pessoaEntity) {
		this(pessoaEntity.getPublicId(), pessoaEntity.getUsername(), pessoaEntity.getNome(), pessoaEntity.getEmail(),
				pessoaEntity.getTelefone(), pessoaEntity.getDataNascimento(),
				pessoaEntity.getInteresses().stream().map(InteresseEntity::getId).toList());
	}

	public PessoaInclusaoDto inclusaoDto() {
		return new PessoaInclusaoDto(username, nome, email, telefone, dataNascimento, senha, senhaConfirmacao,
				interessesIds);
	}

	public PessoaAlteracaoDto alteracaoDto() {
		return new PessoaAlteracaoDto(nome, email, telefone, dataNascimento, interessesIds);
	}


}
