package br.senac.tads.dsw.exemplosspring.advanced;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface MvcCrudConfig {

	String templatePath() default "";
	
	String listFile() default "list";
	
	String formFile() default "form";

}
