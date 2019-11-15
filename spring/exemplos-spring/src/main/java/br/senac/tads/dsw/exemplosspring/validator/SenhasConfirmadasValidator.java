/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring.validator;

import br.senac.tads.dsw.exemplosspring.DadosPessoais;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author ftsuda
 */
public class SenhasConfirmadasValidator implements ConstraintValidator<SenhasConfirmadas, Object> {

    @Override
    public void initialize(final SenhasConfirmadas annotation) {

    }

    @Override
    public boolean isValid(final Object item, final ConstraintValidatorContext context) {
        DadosPessoais dadosPessoais = (DadosPessoais) item;
        return dadosPessoais.getSenha().equals(dadosPessoais.getSenhaRepetida());
    }

}
