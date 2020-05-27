package br.senac.tads.dsw.exemplorest.controller;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senac.tads.dsw.exemplorest.dominio.Interesse;
import br.senac.tads.dsw.exemplorest.dominio.InteresseRepository;
import br.senac.tads.dsw.exemplorest.dominio.Pessoa;
import br.senac.tads.dsw.exemplorest.dominio.PessoaRepository;

@Controller
@RequestMapping("/classic/pessoas")
public class PessoaClassicController {

	private PessoaRepository pessoaRepository;

	private InteresseRepository interesseRepository;

	public PessoaClassicController(PessoaRepository pessoaRepository, InteresseRepository interesseRepository) {
		this.pessoaRepository = pessoaRepository;
		this.interesseRepository = interesseRepository;
	}

	@GetMapping
	public ModelAndView listar(@RequestParam(value = "pagina", defaultValue = "0") int pagina,
			@RequestParam(value = "qtd", defaultValue = "10") int qtd) {
		Page<Pessoa> pessoas = pessoaRepository.findAll(PageRequest.of(pagina, qtd));
		ModelAndView mv = new ModelAndView("pessoa/lista-template");
		mv.addObject("itens", pessoas);
		return mv;
	}

	@ModelAttribute("interesses")
	public List<Interesse> getInteresses() {
		return interesseRepository.findAll();
	}

	@GetMapping("/novo")
	public ModelAndView abrirForm() {
		Pessoa p = new Pessoa();
		ModelAndView mv = new ModelAndView("pessoa/form-template");
		mv.addObject("item", p);
		return mv;
	}

	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Integer id, RedirectAttributes reditAttr) {
		Optional<Pessoa> optPessoa = pessoaRepository.findById(id);
		if (optPessoa.isPresent()) {
			ModelAndView mv = new ModelAndView("pessoa/form-template");
			mv.addObject("item", optPessoa.get());
			return mv;
		}
		reditAttr.addFlashAttribute("magErro", "Pessoa não encontrada");
		return new ModelAndView("redirect:/classic/pessoas");
	}

	@PostMapping("/salvar")
	@Transactional
	public String save(@ModelAttribute("item") Pessoa p, RedirectAttributes reditAttr) {
		Set<Interesse> interesses = new LinkedHashSet<>();
		for (Integer id : p.getInteressesId()) {
			interesseRepository.findById(id).ifPresent(interesse -> interesses.add(interesse));
		}
		p.setInteresses(interesses);
		p = pessoaRepository.save(p);
		reditAttr.addFlashAttribute("msgSucesso", p.getNome() + " adicionado com sucesso com ID " + p.getId());
		return "redirect:/classic/pessoas";
	}

}
