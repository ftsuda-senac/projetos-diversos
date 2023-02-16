package br.senac.tads.dsw.exemplosspring.advanced;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface HtmlInputConfig {

	String type() default "text";

	String placeholder() default "";

	String label() default "";

}
