package br.com.webmobi.dadospessoais.dominio.service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import br.com.webmobi.dadospessoais.dominio.dto.PessoaAlteracaoDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaInclusaoDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaMvcDto;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;
import br.com.webmobi.dadospessoais.dominio.exception.NaoEncontradoException;
import br.com.webmobi.dadospessoais.dominio.repository.InteresseRepository;
import br.com.webmobi.dadospessoais.dominio.repository.PessoaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional(readOnly = true) // Força a inclusão do Transactional em métodos mutáveis (create/update/delete)
@RequiredArgsConstructor
public class PessoaService implements CrudService<PessoaDto, PessoaInclusaoDto, PessoaAlteracaoDto, UUID> {

	private final PessoaRepository pessoaRepository;

	private final InteresseRepository interesseRepository;

	private final PasswordEncoder passwordEncoder;

	private PessoaEntity createEntityFromDto(PessoaInclusaoDto dto) {
		PessoaEntity entity = new PessoaEntity();
		entity.setUsername(dto.username());
		entity.setNome(dto.nome());
		entity.setEmail(dto.email());
		entity.setTelefone(dto.telefone());
		entity.setDataNascimento(dto.dataNascimento());
		entity.setHashSenha(passwordEncoder.encode(dto.senha()));
		entity.setInteresses(new HashSet<>(interesseRepository.findByIdIn(dto.interessesIds())));
		return entity;
	}

	private PessoaEntity updateEntityFromDto(PessoaEntity entity, PessoaAlteracaoDto dto) {
		entity.setNome(dto.nome());
		entity.setEmail(dto.email());
		entity.setTelefone(dto.telefone());
		entity.setDataNascimento(dto.dataNascimento());
		entity.setInteresses(new HashSet<>(interesseRepository.findByIdIn(dto.interessesIds())));
		return entity;
	}

	@Override
	public Page<PessoaDto> listar(Pageable pageable) {
		return pessoaRepository.findAll(pageable).map(PessoaDto::new);
	}

	@Override
	public List<PessoaDto> listarTudo() {
		return pessoaRepository.findAll().stream().map(PessoaDto::new).toList();
	}

	@Override
	public PessoaDto buscarPorId(UUID publicId) {
		return pessoaRepository.findByPublicId(publicId).map(PessoaDto::new)
				.orElseThrow(() -> new NaoEncontradoException("Pessoa ID " + publicId + " não encontrada"));
	}

	public PessoaMvcDto buscarPorIdMvc(UUID publicId) {
		return pessoaRepository.findByPublicId(publicId).map(PessoaMvcDto::new)
				.orElseThrow(() -> new NaoEncontradoException("Pessoa ID " + publicId + " não encontrada"));
	}

	@Override
	@Transactional
	public PessoaDto incluirNovo(@Valid PessoaInclusaoDto dto) {
		PessoaEntity entity = createEntityFromDto(dto);
		pessoaRepository.save(entity);
		return new PessoaDto(entity);
	}

	@Override
	@Transactional
	public PessoaDto alterar(UUID publicId, @Valid PessoaAlteracaoDto dto) {
		PessoaEntity entity = pessoaRepository.findByPublicId(publicId)
				.orElseThrow(() -> new NaoEncontradoException("Pessoa ID " + publicId + " não encontrada"));
		entity = updateEntityFromDto(entity, dto);
		pessoaRepository.save(entity);
		return new PessoaDto(entity);
	}


	@Override
	@Transactional
	public void excluir(UUID publicId) {
		boolean exists = pessoaRepository.existsByPublicId(publicId);
		if (!exists) {
			throw new NaoEncontradoException("Pessoa ID " + publicId + " não encontrada");
		}
		pessoaRepository.deleteByPublicId(publicId);
	}

	// Metodos abaixo criados para atender fluxo MVC
	// durante correção do Converter dos erros

	@Deprecated(forRemoval = true)
	private PessoaEntity createEntityFromMvcDto(PessoaMvcDto dto) {
		PessoaEntity entity = new PessoaEntity();
		entity.setUsername(dto.getUsername());
		entity.setNome(dto.getNome());
		entity.setEmail(dto.getEmail());
		entity.setTelefone(dto.getTelefone());
		entity.setDataNascimento(dto.getDataNascimento());
		entity.setHashSenha(passwordEncoder.encode(dto.getSenha()));
		entity.setInteresses(new HashSet<>(interesseRepository.findByIdIn(dto.getInteressesIds())));
		return entity;
	}

	@Deprecated(forRemoval = true)
	private PessoaEntity updateEntityFromMvcDto(PessoaEntity entity, PessoaMvcDto dto) {
		entity.setNome(dto.getNome());
		entity.setEmail(dto.getEmail());
		entity.setTelefone(dto.getTelefone());
		entity.setDataNascimento(dto.getDataNascimento());
		entity.setInteresses(new HashSet<>(interesseRepository.findByIdIn(dto.getInteressesIds())));
		return entity;
	}

	@Deprecated(forRemoval = true)
	@Transactional
	public PessoaMvcDto incluirNovoMvc(@Valid PessoaMvcDto dto) {
		PessoaEntity entity = createEntityFromMvcDto(dto);
		pessoaRepository.save(entity);
		return new PessoaMvcDto(entity);
	}

	@Deprecated(forRemoval = true)
	@Transactional
	public PessoaMvcDto alterarMvc(UUID publicId, @Valid PessoaMvcDto dto) {
		PessoaEntity entity = pessoaRepository.findByPublicId(publicId)
				.orElseThrow(() -> new NaoEncontradoException("Pessoa ID " + publicId + " não encontrada"));
		entity = updateEntityFromMvcDto(entity, dto);
		pessoaRepository.save(entity);
		return new PessoaMvcDto(entity);
	}

}
