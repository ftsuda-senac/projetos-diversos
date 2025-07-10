package br.com.webmobi.dadospessoais.webmvc.util;

import java.util.Set;

import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

// https://stackoverflow.com/a/66069937
public class ConstraintViolationsToErrorsConverter extends SpringValidatorAdapter {

	public ConstraintViolationsToErrorsConverter() {
		super(Validation.buildDefaultValidatorFactory().getValidator()); // Validator is not actually used
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addConstraintViolations(Set<? super ConstraintViolation<?>> violations, Errors errors) {
		// Using raw type since processConstraintViolations specifically expects ConstraintViolation<Object>
		super.processConstraintViolations((Set) violations, errors);
	}
}
