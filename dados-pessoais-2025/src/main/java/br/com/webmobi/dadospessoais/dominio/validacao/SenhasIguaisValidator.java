package br.com.webmobi.dadospessoais.dominio.validacao;

import java.util.Objects;

import br.com.webmobi.dadospessoais.dominio.dto.SenhaConfirmacao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SenhasIguaisValidator implements ConstraintValidator<SenhasIguais, SenhaConfirmacao> {

	// Obter message da anotação
	private String message;

	@Override
	public void initialize(SenhasIguais annotation) {
		this.message = annotation.message();
	}

	@Override
	public boolean isValid(SenhaConfirmacao dados, ConstraintValidatorContext context) {
		boolean resultado = dados != null && Objects.equals(dados.getSenha(), dados.getSenhaConfirmacao());
		if (resultado) {
			return true;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addPropertyNode("senha").addConstraintViolation();
		return false;
	}

}
