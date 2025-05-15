package br.com.webmobi.dadospessoais;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.webmobi.dadospessoais.dominio.dto.PessoaInclusaoDto;
import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.repository.InteresseRepository;
import br.com.webmobi.dadospessoais.dominio.service.PessoaService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppInitializer implements CommandLineRunner {

    private final InteresseRepository interesseRepository;

    private final PessoaService pessoaService;

    @Override
    public void run(String... args) throws Exception {
        if (interesseRepository.count() > 0) {
            return;
        }
        interesseRepository.saveAll(List.of(new InteresseEntity("Java"),
                new InteresseEntity("Spring"),
                new InteresseEntity("JavaScript"),
                new InteresseEntity("Python"),
                new InteresseEntity("PHP"),
                new InteresseEntity("Ruby"),
                new InteresseEntity("Go"),
                new InteresseEntity("Rust"),
                new InteresseEntity("C#"),
                new InteresseEntity("Kotlin"),
                new InteresseEntity("Swift"),
                new InteresseEntity("Dart"),
                new InteresseEntity("TypeScript"),
                new InteresseEntity("HTML"),
                new InteresseEntity("CSS"),
                new InteresseEntity("SQL")));
        pessoaService.incluirNovo(new PessoaInclusaoDto("fulano", "Fulano da Silva", "fulano@email.com",
                "11 98123-1234", LocalDate.parse("2000-10-20"), "Abcd1234", "Abcd1234", List.of(101, 102, 103)));
        pessoaService.incluirNovo(new PessoaInclusaoDto("ciclano", "Ciclano de Souza", "ciclano@email.com",
                "11 98234-2233", LocalDate.parse("1999-05-10"), "Abcd1234", "Abcd1234", List.of(104, 105, 106)));
        pessoaService.incluirNovo(new PessoaInclusaoDto("beltrana", "Beltrana dos Santos", "beltrana@email.com",
                "11 98987-4567", LocalDate.parse("2001-08-15"), "Abcd1234", "Abcd1234", List.of(101, 106, 107)));
    }

}
