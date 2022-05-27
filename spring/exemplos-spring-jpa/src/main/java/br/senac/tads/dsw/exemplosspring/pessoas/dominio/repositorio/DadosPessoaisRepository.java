package br.senac.tads.dsw.exemplosspring.pessoas.dominio.repositorio;

import br.senac.tads.dsw.exemplosspring.pessoas.dominio.entidade.DadosPessoais;
import java.util.List;
import java.util.Optional;

public interface DadosPessoaisRepository {

    List<DadosPessoais> findAll(int page, int qtde);

    List<DadosPessoais> findByInteresses(List<Integer> interessesIds, int page, int qtde);

    Optional<DadosPessoais> findById(Integer id);

    Optional<DadosPessoais> findByApelido(String apelido);

    DadosPessoais save(DadosPessoais dados);

    void deleteById(Integer id);

}
