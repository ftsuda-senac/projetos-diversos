package br.senac.tads.dsw.exemplosspring;

public class ErroException extends Exception {
    
	private static final long serialVersionUID = 1L;

	public ErroException(String mensagem) {
        super(mensagem);
    }
}
