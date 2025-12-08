package br.com.webmobi.dadospessoais.dominio.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

// Atualização somente de alguns campos
// Senha deve ser alterada em outra funcionalidade
// Foto será gerenciada em outra funcionalidade
public record PessoaAlteracaoDto (
		@NotBlank @Size(max = 100) String nome,
		@NotBlank @Email @Size(max = 100) String email,
		String telefone,
		@PastOrPresent LocalDate dataNascimento,
		@Size(min = 1) List<Integer> interessesIds) {

}
