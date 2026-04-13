package br.com.webmobi.dadospessoais.dominio.dto;

import java.time.LocalDate;
import java.util.List;

import br.com.webmobi.dadospessoais.dominio.validacao.UsernameUnico;
import jakarta.validation.constraints.*;

public record PessoaInclusaoDto (
		@NotBlank @Size(max = 64) @UsernameUnico String username,
		@NotBlank @Size(max = 100) String nome,
		@NotBlank @Email @Size(max = 100) String email,
		String telefone,
		@PastOrPresent LocalDate dataNascimento,

		// Explicação da expressão regular abaixo em https://stackoverflow.com/a/18181478
		// Teste de regex online: https://regex101.com/
		@NotBlank @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{8,}")String senha,
		String senhaConfirmacao,
		@Size(min = 1) List<Integer> interessesIds) implements SenhaConfirmacao {

	@Override
	public String getSenha() {
		return senha();
	}

	@Override
	public String getSenhaConfirmacao() {
		return senhaConfirmacao();
	}

}
