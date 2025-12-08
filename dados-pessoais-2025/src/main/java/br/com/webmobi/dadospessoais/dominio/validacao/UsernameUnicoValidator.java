package br.com.webmobi.dadospessoais.dominio.validacao;

import br.com.webmobi.dadospessoais.dominio.repository.PessoaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsernameUnicoValidator implements ConstraintValidator<UsernameUnico, String> {

	private final PessoaRepository pessoaRepository;

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		return !pessoaRepository.existsByUsername(username);
	}

}
