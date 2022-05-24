package br.senac.tads.dsw.exemplosspring.pessoas.dominio.repositorio;

import br.senac.tads.dsw.exemplosspring.pessoas.dominio.entidade.Interesse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

//@Repository
//@Primary
public class InteresseRepositoryMockImpl implements InteresseRepository {

    private Map<Integer, Interesse> mapItens;

    private int sequenciaId = 0;

    public InteresseRepositoryMockImpl() {
        mapItens = new ConcurrentHashMap<>();
        mapItens.put(++sequenciaId, new Interesse(sequenciaId, "Java"));
        mapItens.put(++sequenciaId, new Interesse(sequenciaId, "Python"));
        mapItens.put(++sequenciaId, new Interesse(sequenciaId, "Javascript"));
        mapItens.put(++sequenciaId, new Interesse(sequenciaId, "User experience"));
        mapItens.put(++sequenciaId, new Interesse(sequenciaId, "Banco de dados"));
        mapItens.put(++sequenciaId, new Interesse(sequenciaId, "Testes"));
    }

    @Override
    public List<Interesse> findAll() {
        return new ArrayList<>(mapItens.values());
    }

    @Override
    public Optional<Interesse> findById(Integer id) {
        return Optional.ofNullable(mapItens.get(id));
    }

    @Override
    public Interesse save(Interesse interesse) {
        if (interesse.getId() == null) {
            interesse.setId(++sequenciaId);
        }
        mapItens.put(interesse.getId(), interesse);
        return interesse;
    }

}
