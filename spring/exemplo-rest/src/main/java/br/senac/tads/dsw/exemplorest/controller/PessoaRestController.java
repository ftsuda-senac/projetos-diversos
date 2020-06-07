package br.senac.tads.dsw.exemplorest.controller;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.senac.tads.dsw.exemplorest.dominio.Interesse;
import br.senac.tads.dsw.exemplorest.dominio.InteresseRepository;
import br.senac.tads.dsw.exemplorest.dominio.Pessoa;
import br.senac.tads.dsw.exemplorest.dominio.PessoaRepository;

@RestController
@RequestMapping("/rest/pessoas")
public class PessoaRestController {
	
	private PessoaRepository pessoaRepository;
	
	private InteresseRepository interesseRepository;
	
	public PessoaRestController(PessoaRepository pessoaRepository,
			InteresseRepository interesseRepository) {
		this.pessoaRepository = pessoaRepository;
		this.interesseRepository = interesseRepository;
	}

	@GetMapping
	//@CrossOrigin(origins = "localhost:8080") // tentar acessar usando 127.0.0.1 no código Ajax/Javascript
	public Page<Pessoa> listar(
			@RequestParam(value = "pagina", defaultValue = "0") int pagina,
			@RequestParam(value = "qtd", defaultValue = "10") int qtd) {
		return pessoaRepository.findAll(PageRequest.of(pagina, qtd));
	}

	@GetMapping("/all")
	public List<Pessoa> listarTodos() {
		return pessoaRepository.findAll();
	}

	@GetMapping("/{id}")
	//@CrossOrigin(origins = "*")
	public Pessoa findById(@PathVariable("id") Integer id) {
		return pessoaRepository.findById(id).get();
	}

	@PostMapping
	//@CrossOrigin(origins = "*")
	public ResponseEntity<?> salvar(@RequestBody Pessoa pessoa) {
		Set<Interesse> interesses = new LinkedHashSet<>();
		for (Integer id : pessoa.getInteressesId()) {
			interesseRepository.findById(id).ifPresent(interesse -> interesses.add(interesse));
		}
		pessoa.setInteresses(interesses);
		pessoa = pessoaRepository.save(pessoa);
		
		// Prepara a URI que identifica a pessoa salva
		// Essa informacão é retornada no cabeçalho "Location"
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(pessoa.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		pessoaRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
