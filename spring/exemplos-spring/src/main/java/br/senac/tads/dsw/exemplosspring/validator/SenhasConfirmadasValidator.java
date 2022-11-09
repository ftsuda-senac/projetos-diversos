package br.senac.tads.dsw.exemplosspring.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import br.senac.tads.dsw.exemplosspring.DadosPessoais;

public class SenhasConfirmadasValidator implements ConstraintValidator<SenhasConfirmadas, DadosPessoais> {

    @Override
    public boolean isValid(DadosPessoais item, ConstraintValidatorContext context) {
        return item.getSenha().equals(item.getSenhaRepetida());
    }


}
