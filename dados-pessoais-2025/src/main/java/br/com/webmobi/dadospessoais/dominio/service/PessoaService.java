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
	@Transactional(readOnly = true)
	public Page<PessoaDto> listar(Pageable pageable) {
		return pessoaRepository.findAll(pageable).map(PessoaDto::new);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PessoaDto> listarTudo() {
		return pessoaRepository.findAll().stream().map(PessoaDto::new).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public PessoaDto buscarPorId(UUID publicId) {
		return pessoaRepository.findByPublicId(publicId).map(PessoaDto::new)
				.orElseThrow(() -> new NaoEncontradoException("Pessoa ID " + publicId + " não encontrada"));
	}

	@Transactional(readOnly = true)
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

}
