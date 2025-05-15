package br.com.webmobi.dadospessoais.dominio.exception;

public class NaoEncontradoException extends RuntimeException {

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
