package br.com.webmobi.dadospessoais.dominio.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;

/**
 * Interface genérica para serviços CRUD.
 *
 * @param <T>  Tipo do DTO de retorno
 * @param <TC> Tipo do DTO de inclusão (TypeCreate)
 * @param <TU> Tipo do DTO de alteração (TypeUpdate)
 * @param <ID> Tipo do ID informado no DTO/parâmetro
 */
public interface CrudService<T, TC, TU, ID> {

    Page<T> listar(Pageable pageable);

    List<T> listarTudo();

    // Se não encontrar lançar exception
    T buscarPorId(ID id);

    // Adicionar @Valid para evitar erro https://stackoverflow.com/questions/76874888/hv000151-hibernate-validator-exception-constraints-not-being-inherited-from-i
    T incluirNovo(@Valid TC inputDto);

    // Se não encontrar lançar exception
    // Adicionar @Valid para evitar erro https://stackoverflow.com/questions/76874888/hv000151-hibernate-validator-exception-constraints-not-being-inherited-from-i
    T alterar(ID publicId, @Valid TU inputDto);

    // Se não encontrar lançar exception
    void excluir(ID id);

}
