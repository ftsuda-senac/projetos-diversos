package br.senac.tads.dsw.exemplorest.controller;

import br.senac.tads.dsw.exemplorest.dominio.Interesse;
import br.senac.tads.dsw.exemplorest.dominio.InteresseRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/interesses")
public class InteresseRestController {
    
    private InteresseRepository interesseRepository;
    
    public InteresseRestController(InteresseRepository interesseRepository) {
        this.interesseRepository = interesseRepository;
    }
    
    @GetMapping
    public List<Interesse> findAll() {
        return interesseRepository.findAll();
    }
    
}
