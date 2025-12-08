package br.com.webmobi.dadospessoais.dominio.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import br.com.webmobi.dadospessoais.dominio.dto.FotoInputDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaFotoDto;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaFotoEntity;
import br.com.webmobi.dadospessoais.dominio.exception.NaoEncontradoException;
import br.com.webmobi.dadospessoais.dominio.repository.PessoaFotoRepository;
import br.com.webmobi.dadospessoais.dominio.repository.PessoaRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class PessoaFotoService {

	private final PessoaRepository pessoaRepository;

	private final PessoaFotoRepository pessoaFotoRepository;

	@Value("${app.upload-path}")
	private String uploadPath;

	private String getFileExtension(String fileName) {
		int lastIndex = fileName.lastIndexOf('.');
		if (lastIndex == -1) {
			return "";
		}
		return fileName.substring(lastIndex).toLowerCase();
	}

	// Padrão do diretório: /[UUID]/[subdir-tipo]/nome-arquivo
	private Path getFilePath(PessoaEntity pessoaEntity) {
		return Paths.get(uploadPath, pessoaEntity.getPublicId().toString(), "fotos");
	}

	@Transactional(readOnly = true)
	public List<PessoaFotoDto> listar(UUID pessoaId) {
		return pessoaFotoRepository.findByPessoa_PublicId(pessoaId).stream().map(PessoaFotoDto::new).toList();
	}

	@Transactional
	public PessoaFotoDto salvar(UUID pessoaId, @Valid FotoInputDto dto) {
		PessoaEntity pessoaEntity = pessoaRepository.findByPublicId(pessoaId)
				.orElseThrow(() -> new NaoEncontradoException("Pessoa ID " + pessoaId + " não encontrada"));

		// Gravar no disco

		Path dirDestino = getFilePath(pessoaEntity);
		if (!Files.exists(dirDestino)) {
			try {
				Files.createDirectories(dirDestino);
			} catch (Exception ex) {
				throw new RuntimeException("Erro ao criar diretório para salvar a foto", ex);
			}
		}
		MultipartFile arquivo = dto.getArquivo();
		// Gerar nome de arquivo aleatório
		String nomeArquivo = "" + Instant.now().getEpochSecond() + getFileExtension(arquivo.getOriginalFilename());
		Path arquivoDestino = Paths.get(dirDestino.toString(), nomeArquivo);
		log.debug("Gravando arquivo em {}", arquivoDestino);
		try {
			arquivo.transferTo(arquivoDestino);
		} catch (Exception ex) {
			throw new RuntimeException("Erro ao gravar a foto no arquivo", ex);
		}

		PessoaFotoEntity fotoEntity = new PessoaFotoEntity();
		fotoEntity.setPessoa(pessoaEntity);
		fotoEntity.setNomeArquivo(nomeArquivo);
		fotoEntity.setNomeArquivoOriginal(arquivo.getOriginalFilename());
		fotoEntity.setLegenda(dto.getLegenda());

		pessoaFotoRepository.save(fotoEntity);
		return new PessoaFotoDto(fotoEntity);
	}

	@Transactional
	public void excluir(UUID pessoaId, String nomeArquivo) {
		PessoaFotoEntity fotoEntity = pessoaFotoRepository.findByPessoa_PublicIdAndNomeArquivo(pessoaId, nomeArquivo)
				.orElseThrow(() -> new NaoEncontradoException(
						"Foto com nome de arquivo " + nomeArquivo + " não encontrada"));
		PessoaEntity pessoaEntity = fotoEntity.getPessoa();
		pessoaFotoRepository.delete(fotoEntity);

		// Excluir arquivo do disco
		Path arquivoDestino = Paths.get(getFilePath(pessoaEntity).toString(), nomeArquivo);
		try {
			Files.deleteIfExists(arquivoDestino);
		} catch (Exception ex) {
			throw new RuntimeException("Erro ao excluir a foto do arquivo", ex);
		}
	}

}
