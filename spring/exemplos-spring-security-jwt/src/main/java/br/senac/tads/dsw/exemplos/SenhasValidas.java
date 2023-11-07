package br.senac.tads.dsw.exemplos;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = SenhaValidasValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SenhasValidas {
    
    String message() default "A senha e a repetição devem ser iguais";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
