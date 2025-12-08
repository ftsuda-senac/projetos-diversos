package br.com.webmobi.dadospessoais.webmvc;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.webmobi.dadospessoais.dominio.UrlMapper;
import br.com.webmobi.dadospessoais.dominio.dto.*;
import br.com.webmobi.dadospessoais.dominio.service.InteresseService;
import br.com.webmobi.dadospessoais.dominio.service.PessoaFotoService;
import br.com.webmobi.dadospessoais.dominio.service.PessoaService;
import br.com.webmobi.dadospessoais.webmvc.util.AlertMessage;
import br.com.webmobi.dadospessoais.webmvc.util.ConstraintViolationsToErrorsConverter;
import br.com.webmobi.dadospessoais.webmvc.util.AlertMessage.AlertMessageType;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mvc/pessoas")
@RequiredArgsConstructor
public class PessoaMvcController {

	private final PessoaService pessoaService;

	private final PessoaFotoService pessoaFotoService;

	private final InteresseService interesseService;

	private final UrlMapper urlMapper;

	private FotoDto toFotoDto(PessoaDto dto, String nomeArquivo) {
		String path = urlMapper.getImagemUrlPath(dto.id(), nomeArquivo);
		URI fotoUri = ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUri();
		return new FotoDto(fotoUri, nomeArquivo);
	}

	// Ver https://docs.spring.io/spring-data/commons/reference/repositories/core-extensions.html#core.web.page.paged-model
	@GetMapping
	public ModelAndView listar(Pageable pageable) {
		ModelAndView mv = new ModelAndView("pessoas/lista");
		mv.addObject("itens", pessoaService.listar(pageable).map(dto -> {
			List<FotoDto> fotosUrls = dto.pessoasFotos().stream().map(foto -> toFotoDto(dto, foto.legenda())).toList();
			return new PessoaDto(dto, fotosUrls);
		}));
		return mv;
	}

	@GetMapping("/{id}")
	public PessoaDto buscarPorId(@PathVariable UUID id) {
		PessoaDto dto = pessoaService.buscarPorId(id);
		List<FotoDto> fotosUrls = dto.pessoasFotos().stream().map(foto -> toFotoDto(dto, foto.legenda())).toList();
		return new PessoaDto(dto, fotosUrls);
	}

	@GetMapping("/incluir")
	public ModelAndView incluirForm() {
		return new ModelAndView("pessoas/form").addObject("item", new PessoaMvcDto()).addObject("opcoesInteresses",
				interesseService.listarTudo());
	}

	@GetMapping("/{id}/alterar")
	public ModelAndView alterarForm(@PathVariable UUID id) {
		PessoaMvcDto dto = pessoaService.buscarPorIdMvc(id);
		return new ModelAndView("pessoas/form").addObject("item", dto).addObject("opcoesInteresses",
				interesseService.listarTudo());
	}

	@PostMapping("/incluir")
	public ModelAndView incluirNovo(@ModelAttribute("item") PessoaMvcDto inputDto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		try {
			PessoaDto dto = pessoaService.incluirNovo(inputDto.inclusaoDto());
			redirectAttributes.addFlashAttribute("msg",
					new AlertMessage(AlertMessageType.SUCCESS, "Pessoa incluida com sucesso"));
			return new ModelAndView("redirect:/mvc/pessoas");
		} catch (ConstraintViolationException ex) {
			ConstraintViolationsToErrorsConverter cve = new ConstraintViolationsToErrorsConverter();
			cve.addConstraintViolations(ex.getConstraintViolations(), bindingResult);
			return new ModelAndView("pessoas/form").addObject("opcoesInteresses", interesseService.listarTudo());
		}
	}

	@PostMapping("/{id}/alterar")
	public ModelAndView alterar(@PathVariable UUID id, @ModelAttribute("item") PessoaMvcDto inputDto,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		try {
			PessoaDto dto = pessoaService.alterar(id, inputDto.alteracaoDto());
			redirectAttributes.addFlashAttribute("msg",
					new AlertMessage(AlertMessageType.SUCCESS, "Pessoa alterada com sucesso"));
			return new ModelAndView("redirect:/mvc/pessoas");
		} catch (ConstraintViolationException ex) {
			ConstraintViolationsToErrorsConverter cve = new ConstraintViolationsToErrorsConverter();
			cve.addConstraintViolations(ex.getConstraintViolations(), bindingResult);
			return new ModelAndView("pessoas/form").addObject("opcoesInteresses", interesseService.listarTudo());
		}
	}

	@PostMapping("/{id}/excluir")
	public ModelAndView excluir(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
		pessoaService.excluir(id);
		redirectAttributes.addFlashAttribute("msg",
				new AlertMessage(AlertMessageType.SUCCESS, "Pessoa excluida com sucesso"));
		return new ModelAndView("redirect:/mvc/pessoas");
	}

	@PostMapping(path = "/{pessoaId}/fotos", consumes = "multipart/form-data")
	public ResponseEntity<Void> incluirFoto(@PathVariable UUID pessoaId, @ModelAttribute FotoInputDto arquivo) {
		PessoaFotoDto dto = pessoaFotoService.salvar(pessoaId, arquivo);
		String path = urlMapper.getImagemUrlPath(pessoaId, dto.nomeArquivo());
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/{pessoaId}/fotos/{nomeArquivo}")
	public ResponseEntity<Void> excluirFoto(@PathVariable UUID pessoaId, @PathVariable String nomeArquivo) {
		pessoaFotoService.excluir(pessoaId, nomeArquivo);
		return ResponseEntity.noContent().build();
	}

}
