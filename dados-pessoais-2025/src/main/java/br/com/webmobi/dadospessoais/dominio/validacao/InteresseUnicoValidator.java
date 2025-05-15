package br.com.webmobi.dadospessoais.dominio.validacao;

import br.com.webmobi.dadospessoais.dominio.repository.InteresseRepository;
import br.com.webmobi.dadospessoais.util.AppUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InteresseUnicoValidator implements ConstraintValidator<InteresseUnico, String> {

    private final InteresseRepository interesseRepository;

    @Override
    public boolean isValid(String nome, ConstraintValidatorContext context) {
        String nomeNormalizado = AppUtil.normalizarString(nome);
        return !interesseRepository.existsByNomeNormalizado(nomeNormalizado);
    }

}
