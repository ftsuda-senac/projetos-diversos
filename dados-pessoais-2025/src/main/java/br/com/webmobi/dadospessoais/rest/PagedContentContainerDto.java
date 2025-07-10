package br.com.webmobi.dadospessoais.rest;

import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel.PageMetadata;

// PagedContentContainerDto - PagedModel original do Spring não tem construtor reconhecido pelo Jackson2
// Inicialmente criado somente para testes, mas faz sentido manter para uso geral
// Ver https://docs.spring.io/spring-data/commons/reference/repositories/core-extensions.html#core.web.page.paged-model
// https://stackoverflow.com/questions/55965523/error-during-deserialization-of-pageimpl-cannot-construct-instance-of-org-spr
/**
 * Informações paginadas baseado no {@link org.springframework.data.web.PagedModel}
 * @param <T>
 */
public record PagedContentContainerDto<T> (@NonNull List<T> content, @NonNull PageMetadata page) {

	public PagedContentContainerDto(@NonNull Page<T> page) {
		this(page.getContent(),
				new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages()));
	}

}
