package br.senac.tads.dsw.exemplosspring.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

// http://codetutr.com/2013/05/29/custom-spring-mvc-validation-annotations/
// http://www.baeldung.com/spring-mvc-custom-validator
// https://stackoverflow.com/questions/40353638/spring-custom-annotation-validation-with-multiple-field
// http://www.silverbaytech.com/2013/04/16/custom-messages-in-spring-validation/
@Documented
@Constraint(validatedBy = SenhasConfirmadasValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SenhasConfirmadas {

    String message() default "A senha e a repetição não são iguais";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
