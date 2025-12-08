package br.com.webmobi.dadospessoais.rest;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.webmobi.dadospessoais.dominio.UrlMapper;
import br.com.webmobi.dadospessoais.dominio.dto.*;
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


	// @ParameterObject - "abre" os parâmetros de paginação na tela do Swagger
	@GetMapping
	public PagedContentContainerDto<PessoaDto> listar(@ParameterObject Pageable pageable) {
		return new PagedContentContainerDto<>(pessoaService.listar(pageable).map(dto -> {
			List<FotoDto> fotosUrls = dto.pessoasFotos().stream().map(foto -> toFotoDto(dto, foto.legenda())).toList();
			return new PessoaDto(dto, fotosUrls);
		}));
	}

	@GetMapping(params = "all")
	public ListContentContainerDto<PessoaDto> listarTudo() {
		return new ListContentContainerDto<PessoaDto>(pessoaService.listarTudo().stream().map(dto -> {
			List<FotoDto> fotosUrls = dto.pessoasFotos().stream().map(foto -> toFotoDto(dto, foto.legenda())).toList();
			return new PessoaDto(dto, fotosUrls);
		}).toList());
	}

	@GetMapping("/{id}")
	public PessoaDto buscarPorId(@PathVariable UUID id) {
		PessoaDto dto = pessoaService.buscarPorId(id);
		List<FotoDto> fotosUrls = dto.pessoasFotos().stream().map(foto -> toFotoDto(dto, foto.legenda())).toList();
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
