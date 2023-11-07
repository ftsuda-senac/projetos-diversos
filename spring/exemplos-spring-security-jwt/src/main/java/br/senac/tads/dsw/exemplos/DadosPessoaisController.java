package br.senac.tads.dsw.exemplos;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/dados-pessoais")
public class DadosPessoaisController {

    @Autowired
    private DadosPessoaisServiceJpaImpl service;

    @GetMapping
    public List<DadosPessoaisDto> findAll() {
        return service.findAll();
    }
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // ISO-8601 
    @GetMapping("/busca")
    public List<DadosPessoaisDto> searchByTermoBusca(
        @RequestParam(name = "termo") String termoBusca,
        @RequestParam(defaultValue = "1") Integer pagina,
        @RequestParam(defaultValue = "10") Integer quantidade) {
        return service.searchByTermoBusca(termoBusca, pagina, quantidade);
    }

    @GetMapping("/busca-page")
    public Page<DadosPessoaisDto> searchByTermoBuscaPage(
        @RequestParam(name = "termo") String termoBusca,
        @RequestParam(defaultValue = "1") Integer pagina,
        @RequestParam(defaultValue = "10") Integer quantidade) {
        return service.searchByTermoBuscaPage(termoBusca, pagina, quantidade);
    }

    @GetMapping("/{id}")
    public DadosPessoaisDto findByIdComOptional(@PathVariable Integer id) {
        // Optional<DadosPessoais> optDados = service.findByIdComOptional(id);
        // if (optDados.isPresent()) {
        //     return optDados.get();
        // } else {
        //     throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID " + id + " não encontrado");
        // }

        // Alternativa
        return service.findByIdComOptional(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "ID " + id + " não encontrado"));
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid DadosPessoaisDto dados) {

        System.out.println(dados.toString());
        service.save(dados);

        // Prepara a URI que identifica a pessoa salva
        // Essa informacão é retornada no cabeçalho "Location"
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dados.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
    
}
