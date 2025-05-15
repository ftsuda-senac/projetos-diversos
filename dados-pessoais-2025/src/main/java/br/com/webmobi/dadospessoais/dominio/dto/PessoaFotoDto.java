package br.com.webmobi.dadospessoais.dominio.dto;

import br.com.webmobi.dadospessoais.dominio.entity.PessoaFotoEntity;

public record PessoaFotoDto(String nomeArquivo, String legenda) {

    public PessoaFotoDto(PessoaFotoEntity pessoaFotoEntity) {
        this(pessoaFotoEntity.getNomeArquivo(), pessoaFotoEntity.getLegenda());
    }

}
