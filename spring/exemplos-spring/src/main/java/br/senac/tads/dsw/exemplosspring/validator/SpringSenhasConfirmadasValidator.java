/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring.validator;

import br.senac.tads.dsw.exemplosspring.DadosPessoais;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author ftsuda
 */
public class SpringSenhasConfirmadasValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return DadosPessoais.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DadosPessoais dadosPessoais = (DadosPessoais) target;
        if (!dadosPessoais.getSenha().equals(dadosPessoais.getSenhaRepetida())) {
            errors.rejectValue("senhaRepetida", "A senha e a repetição não são iguais");
        }
    }

}
