package br.com.webmobi.dadospessoais.rest;

import java.util.List;

// "Simula" a parte do content do Page do spring.data para retorno de array
public record ListContentContainerDto<T>(List<T> content) {

}
