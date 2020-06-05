package br.senac.tads.dsw.exemplorest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.senac.tads.dsw.exemplorest.dominio.Interesse;
import br.senac.tads.dsw.exemplorest.dominio.InteresseRepository;

@RestController
@RequestMapping("/rest/interesses")
public class InteressesController {
	
	private InteresseRepository interesseRepository;
	
	public InteressesController(InteresseRepository interesseRepository) {
		this.interesseRepository = interesseRepository;
	}

	@GetMapping
	public List<Interesse> findAll() {
		return interesseRepository.findAll();
	}

}
