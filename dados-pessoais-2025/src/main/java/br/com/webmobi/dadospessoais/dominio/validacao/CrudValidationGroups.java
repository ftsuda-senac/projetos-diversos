package br.com.webmobi.dadospessoais.dominio.validacao;

import jakarta.validation.groups.Default;

public class CrudValidationGroups {
	// https://stackoverflow.com/a/35359965
	public interface Create extends Default {

	}

	public interface Update extends Default {

	}
}
