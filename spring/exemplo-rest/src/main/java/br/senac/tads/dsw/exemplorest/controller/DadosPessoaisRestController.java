package br.senac.tads.dsw.exemplorest.controller;

import br.senac.tads.dsw.exemplorest.dominio.DadosPessoais;
import br.senac.tads.dsw.exemplorest.dominio.DadosPessoaisService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/rest/pessoas")
// tentar acessar usando 127.0.0.1:8080 na URL do frontend
// @CrossOrigin(origins = "http://127.0.0.1:8080")
// @CrossOrigin(origins = "*")

// Annotations do Springdoc/Swagger
// Exemplos em https://github.com/springdoc/springdoc-openapi-demos/blob/master/springdoc-openapi-spring-boot-2-webmvc/src/main/java/org/springdoc/demo/app2/api/StoreApi.java

public class DadosPessoaisRestController {

    private DadosPessoaisService service;

    public DadosPessoaisRestController(DadosPessoaisService service) {
        this.service = service;
    }

    @GetMapping
    public Page<DadosPessoais> listar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "qtde", defaultValue = "10") int qtde,
            @RequestParam(name = "interessesIds", required = false) List<Integer> interessesIds) {
        Page<DadosPessoais> resultadosPagina = service.findAll(page, qtde, interessesIds);
        return resultadosPagina;
    }

    @GetMapping("/{id}")
    public DadosPessoais findById(@PathVariable("id") Integer id) {
        Optional<DadosPessoais> optPessoa = service.findById(id);
        if (!optPessoa.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Pessoa com ID " + id + " não encontrada");
        }
        DadosPessoais resultado = optPessoa.get();
        return resultado;
    }

    @PostMapping
    public ResponseEntity<?> salvarNovo(
            @RequestBody @Valid DadosPessoais dados,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // TODO: MELHORAR O DETALHAMENTO DA RESPOSTA EM CASO DE ERRO
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Erro nos dados enviados");
        }
        dados = service.save(dados);

        // Prepara a URI que identifica a pessoa salva
        // Essa informacão é retornada no cabeçalho "Location"
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dados.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}") // Ação de alterar já está implicito no PUT
    public ResponseEntity<?> salvarExistente(@PathVariable("id") Integer id, @RequestBody @Valid DadosPessoais dados, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // TODO: MELHORAR O DETALHAMENTO DA RESPOSTA EM CASO DE ERRO
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Erro nos dados enviados");
        }
        dados.setId(id);
        service.save(dados);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}") // Ação de excluir já está implicito no DELETE
    public ResponseEntity<?> excluirExistente(@PathVariable("id") Integer id, RedirectAttributes redirAttr) {
        Optional<DadosPessoais> optPessoa = service.findById(id);
        if (!optPessoa.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa com ID " + id + " não encontrada");
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
