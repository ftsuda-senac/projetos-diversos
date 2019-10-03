package br.senac.tads.pi3.exemplosservlets;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste-rest")
public class TesteRestController {

    @GetMapping
    public ResponseEntity<Teste> teste() {
        return ResponseEntity.ok(new Teste(1L, "teste", true));
    }

}
