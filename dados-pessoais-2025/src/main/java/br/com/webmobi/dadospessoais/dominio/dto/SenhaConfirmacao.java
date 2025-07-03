package br.com.webmobi.dadospessoais.dominio.dto;

import br.com.webmobi.dadospessoais.dominio.validacao.SenhasIguais;

@SenhasIguais
public interface SenhaConfirmacao {

	String getSenha();

	String getSenhaConfirmacao();

}
