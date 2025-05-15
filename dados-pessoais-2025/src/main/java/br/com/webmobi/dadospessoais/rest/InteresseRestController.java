package br.com.webmobi.dadospessoais.rest;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.webmobi.dadospessoais.dominio.dto.InteresseDto;
import br.com.webmobi.dadospessoais.dominio.service.InteresseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/interesses")
@RequiredArgsConstructor
public class InteresseRestController {

    private final InteresseService interesseService;

    @GetMapping
    public List<InteresseDto> listar() {
        return interesseService.listarTudo();
    }

    @PostMapping
    public ResponseEntity<InteresseDto> incluirNovo(@RequestBody @Valid InteresseDto inputDto) {
        InteresseDto dto = interesseService.incluirNovo(inputDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(location).body(dto);
    }

}
