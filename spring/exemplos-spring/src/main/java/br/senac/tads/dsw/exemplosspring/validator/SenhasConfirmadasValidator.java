/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import br.senac.tads.dsw.exemplosspring.DadosPessoais;

/**
 *
 * @author ftsuda
 */
public class SenhasConfirmadasValidator implements ConstraintValidator<SenhasConfirmadas, DadosPessoais> {

    @Override
    public boolean isValid(DadosPessoais item, ConstraintValidatorContext context) {
        return item.getSenha().equals(item.getSenhaRepetida());
    }


}
