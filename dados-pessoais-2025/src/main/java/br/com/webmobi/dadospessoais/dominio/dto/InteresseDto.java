package br.com.webmobi.dadospessoais.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.validacao.InteresseUnico;
import jakarta.validation.constraints.NotBlank;

public record InteresseDto(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) Integer id,
    @NotBlank @InteresseUnico String nome) {

    public InteresseDto(InteresseEntity interesseEntity) {
        this(interesseEntity.getId(), interesseEntity.getNome());
    }

}
