package br.senac.tads.dsw.exemplos;

import java.util.List;
import java.util.Optional;

public interface DadosPessoaisService {

    List<DadosPessoaisDto> findAll();

    List<DadosPessoaisDto> searchByTermoBusca(String termoBusca, int pagina, int quantidade);

    DadosPessoaisDto findById(Integer id);

    Optional<DadosPessoaisDto> findByIdComOptional(Integer id);

    void save(DadosPessoaisDto dados);

}
