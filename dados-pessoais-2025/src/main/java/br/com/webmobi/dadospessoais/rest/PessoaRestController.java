package br.com.webmobi.dadospessoais.rest;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.webmobi.dadospessoais.dominio.UrlMapper;
import br.com.webmobi.dadospessoais.dominio.dto.FotoDto;
import br.com.webmobi.dadospessoais.dominio.dto.FotoInputDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaAlteracaoDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaFotoDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaInclusaoDto;
import br.com.webmobi.dadospessoais.dominio.service.PessoaFotoService;
import br.com.webmobi.dadospessoais.dominio.service.PessoaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pessoas")
@RequiredArgsConstructor
public class PessoaRestController {

	private final PessoaService pessoaService;

	private final PessoaFotoService pessoaFotoService;

	private final UrlMapper urlMapper;

	private FotoDto toFotoDto(PessoaDto dto, String nomeArquivo) {
		String path = urlMapper.getImagemUrlPath(dto.id(), nomeArquivo);
		URI fotoUri = ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUri();
		return new FotoDto(fotoUri, nomeArquivo);
	}

	// Ver https://docs.spring.io/spring-data/commons/reference/repositories/core-extensions.html#core.web.page.paged-model
	// @ParameterObject - "abre" os parâmetros de paginação na tela do Swagger
	@GetMapping
	public PagedModel<PessoaDto> listar(@ParameterObject Pageable pageable) {
		return new PagedModel<>(pessoaService.listar(pageable).map(dto -> {
			List<FotoDto> fotosUrls = dto.fotos().stream().map(foto -> toFotoDto(dto, foto.legenda())).toList();
			return new PessoaDto(dto, fotosUrls);
		}));
	}

	@GetMapping(params = "all")
	public ListContentContainerDto<PessoaDto> listarTudo() {
		return new ListContentContainerDto<PessoaDto>(pessoaService.listarTudo().stream().map(dto -> {
			List<FotoDto> fotosUrls = dto.fotos().stream().map(foto -> toFotoDto(dto, foto.legenda())).toList();
			return new PessoaDto(dto, fotosUrls);
		}).toList());
	}

	@GetMapping("/{id}")
	public PessoaDto buscarPorId(@PathVariable UUID id) {
		PessoaDto dto = pessoaService.buscarPorId(id);
		List<FotoDto> fotosUrls = dto.fotos().stream().map(foto -> toFotoDto(dto, foto.legenda())).toList();
		return new PessoaDto(dto, fotosUrls);
	}

	@PostMapping
	public ResponseEntity<PessoaDto> incluirNovo(@RequestBody PessoaInclusaoDto inputDto) {
		PessoaDto dto = pessoaService.incluirNovo(inputDto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(dto.id())
				.toUri();
		return ResponseEntity.created(location).body(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PessoaDto> alterar(@PathVariable UUID id, @RequestBody PessoaAlteracaoDto inputDto) {
		PessoaDto dto = pessoaService.alterar(id, inputDto);
		return ResponseEntity.ok().body(dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable UUID id) {
		pessoaService.excluir(id);
		return ResponseEntity.noContent().build();
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
