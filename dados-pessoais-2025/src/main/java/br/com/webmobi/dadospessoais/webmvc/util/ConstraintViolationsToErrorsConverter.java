package br.com.webmobi.dadospessoais.webmvc.util;

import java.util.Set;
import java.util.StringJoiner;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.validation.Validator;

@Component
public class ConstraintViolationsToErrorsConverter {

    /**
     * Converte ConstraintViolations do Bean Validation para Errors do Spring MVC,
     * replicando o comportamento essencial de SpringValidatorAdapter
     * sem herdar sua infraestrutura (e sem criar dependência circular).
     */
    public void addConstraintViolations(Set<ConstraintViolation<?>> violations, Errors errors) {
        for (ConstraintViolation<?> violation : violations) {

            String field = extractField(violation.getPropertyPath());
            String code  = violation.getConstraintDescriptor()
                                    .getAnnotation()
                                    .annotationType()
                                    .getSimpleName();
            String message = violation.getMessage();

            if (field.isEmpty()) {
                errors.reject(code, message);
            } else {
                errors.rejectValue(field, code, message);
            }
        }
    }

    /**
     * Extrai o caminho relativo ao bean validado, ignorando o nó raiz.
     * Exemplo: "address.street" → "address.street"; "" para violações de classe.
     */
    private String extractField(Path propertyPath) {
        StringJoiner joiner = new StringJoiner(".");

        for (Path.Node node : propertyPath) {
            ElementKind kind = node.getKind();
            if (kind == ElementKind.PROPERTY || kind == ElementKind.CONTAINER_ELEMENT) {
                joiner.add(node.getName());
            }
        }

        return joiner.toString();
    }
}

// Versão inicial que herda SpringValidatorAdapter porém causa Circular Dependency
// https://stackoverflow.com/a/66069937
// Corrigido pelo Claude Code

// @Component
// public class ConstraintViolationsToErrorsConverter extends SpringValidatorAdapter {

//     /**
//      * Injeta o Validator já gerenciado pelo Spring (singleton, lifecycle correto).
//      * O SpringValidatorAdapter precisa dele em sua inicialização interna.
//      */
//     public ConstraintViolationsToErrorsConverter(Validator validator) {
//         super(validator);
//     }

//     /**
//      * Converte ConstraintViolations do Bean Validation para Errors do Spring MVC.
//      *
//      * O cast para raw Set é necessário porque processConstraintViolations declara
//      * Set<ConstraintViolation<Object>>, mas funciona corretamente via type erasure.
//      * Não há risco de ClassCastException em runtime neste contexto.
//      */
//     @SuppressWarnings({"rawtypes", "unchecked"})
//     public void addConstraintViolations(Set<ConstraintViolation<?>> violations, Errors errors) {
//         super.processConstraintViolations((Set) violations, errors);
//     }
// }