package br.com.webmobi.dadospessoais.dominio.exception;

import java.io.Serial;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NaoEncontradoException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public NaoEncontradoException(String message) {
		super(message);
	}

	public NaoEncontradoException(String message, Throwable cause) {
		super(message, cause);
	}

	public NaoEncontradoException(Throwable cause) {
		super(cause);
	}

}
