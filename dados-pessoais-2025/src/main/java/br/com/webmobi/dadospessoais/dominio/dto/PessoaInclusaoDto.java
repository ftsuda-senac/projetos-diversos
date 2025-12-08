package br.com.webmobi.dadospessoais.dominio.dto;

import java.time.LocalDate;
import java.util.List;

import br.com.webmobi.dadospessoais.dominio.validacao.SenhasIguais;
import br.com.webmobi.dadospessoais.dominio.validacao.UsernameUnico;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@SenhasIguais
public record PessoaInclusaoDto (
		@NotBlank @Size(max = 64) @UsernameUnico String username,
		@NotBlank @Size(max = 100) String nome,
		@NotBlank @Email @Size(max = 100) String email,
		String telefone,
		@PastOrPresent LocalDate dataNascimento,
		@NotBlank String senha,
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
