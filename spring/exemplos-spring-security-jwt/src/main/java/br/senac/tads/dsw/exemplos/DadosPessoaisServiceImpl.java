package br.senac.tads.dsw.exemplos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.PostConstruct;

public class DadosPessoaisServiceImpl implements DadosPessoaisService {

    private static int contador = 0;

    private Map<Integer, DadosPessoaisDto> usuarios = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        usuarios.put(++contador, new DadosPessoaisDto(1, "Fulano da Silva", "fulano", "fulano@teste.com.br", "(11) 99999-1122",
                "abcd$1234", "abcd$1234", LocalDate.parse("2000-10-20"), Arrays.asList("Java", "HTML", "CSS")));
        usuarios.put(++contador, new DadosPessoaisDto(2, "Ciclano de Souza", "ciclano", "ciclano@teste.com.br", "(11) 98765-1234",
                "abcd$1234", "abcd$1234", LocalDate.parse("2001-05-15"),  Arrays.asList("Java", "HTML", "Javascript")));
        usuarios.put(++contador, new DadosPessoaisDto(3, "Beltrana dos Santos", "beltrana", "beltrana@teste.com.br", "(11) 91234-8877",
                "abcd$1234", "abcd$1234", LocalDate.parse("1999-02-01"),  Arrays.asList("HTML", "CSS", "Javascript")));
    }

    @Override
    public List<DadosPessoaisDto> findAll() {
        return new ArrayList<>(usuarios.values());
    }

    @Override
    public List<DadosPessoaisDto> searchByTermoBusca(String termoBusca, int pagina, int quantidade) {
        List<DadosPessoaisDto> resultadosTotal = new ArrayList<>();
        for (DadosPessoaisDto dados : usuarios.values()) {
            if (dados.getNome().toLowerCase()
                    .contains(termoBusca.toLowerCase())) {
                resultadosTotal.add(dados);
            }
        }
        // Para usuarios, contagem começa em 1, mas
        // na programacao, indices começam em 0
        int paginaAjustada = pagina - 1;
        int offset = paginaAjustada * quantidade;

        List<DadosPessoaisDto> resultados = new ArrayList<>();
        for (int i = offset; i < (offset + quantidade) && i < resultadosTotal.size(); i++) {
            resultados.add(resultadosTotal.get(i));
        }
        return resultados;
    }

    @Override
    public DadosPessoaisDto findById(Integer id) {
        DadosPessoaisDto dados = usuarios.get(id);
        if (dados != null) {
            return dados;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID " + id + " não encontrado");
        }
    }

    @Override
    public Optional<DadosPessoaisDto> findByIdComOptional(Integer id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public void save(DadosPessoaisDto dados) {
        dados.setId(++contador);
        usuarios.put(dados.getId(), dados);
    }

}
