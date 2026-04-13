package br.com.webmobi.dadospessoais.service;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.webmobi.dadospessoais.dominio.dto.PessoaAlteracaoDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaInclusaoDto;
import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;
import br.com.webmobi.dadospessoais.dominio.exception.NaoEncontradoException;
import br.com.webmobi.dadospessoais.dominio.repository.InteresseRepository;
import br.com.webmobi.dadospessoais.dominio.repository.PessoaRepository;
import br.com.webmobi.dadospessoais.dominio.service.PessoaService;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PessoaServiceTest {

	@Mock
	PessoaRepository pessoaRepository;

	@Mock
	InteresseRepository interesseRepository;

	@Mock
	PasswordEncoder passwordEncoder;

	@InjectMocks
	PessoaService pessoaService;

	UUID pessoaPublicId;

	PessoaEntity pessoaDefault;

	static PessoaEntity criarPessoaEntity(int id) {
		PessoaEntity pessoaMock = new PessoaEntity();
		pessoaMock.setId(1);
		pessoaMock.setPublicId(UUID.randomUUID());
		String username = "pessoa" + id;
		pessoaMock.setUsername(username);
		pessoaMock.setNome("Pessoa " + id);
		pessoaMock.setEmail(username + "@email.com");
		pessoaMock.setTelefone("(11) 99999-1234");
		pessoaMock.setDataNascimento(LocalDate.parse("2000-05-20"));
		Instant now = Instant.now();
		pessoaMock.setDataCriacao(now);
		pessoaMock.setDataAtualizacao(now);
		pessoaMock.setInteresses(new HashSet<>(List.of(new InteresseEntity(1, "Java"), new InteresseEntity(2, "Web"))));
		return pessoaMock;
	}

	PessoaEntity createPessoaEntity() {
		if (this.pessoaDefault == null) {
			this.pessoaDefault = criarPessoaEntity(1);
			this.pessoaPublicId = pessoaDefault.getPublicId();
		}
		return pessoaDefault;
	}

	// -------------------------------------------------------------------------
	// listar
	// -------------------------------------------------------------------------

	@Test
	@Order(1)
	void testGivenPageRequestWhenListarThenRetornarPaginaPessoas() {

		List<PessoaEntity> pessoasEntities = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			pessoasEntities.add(criarPessoaEntity(i));
		}

		// Given/Arrange
		given(pessoaRepository.findAll(any(Pageable.class))).willAnswer(invocation -> {
			Pageable pageable = invocation.getArgument(0);
			return new PageImpl<PessoaEntity>(pessoasEntities.subList(0, pageable.getPageSize()));
		});
		int size = 2;

		// When/Act
		Page<PessoaDto> resposta = pessoaService.listar(PageRequest.of(1, size));

		// Then/Assert
		then(pessoaRepository).should(times(1)).findAll(any(Pageable.class));
		BDDAssertions.then(resposta.getNumberOfElements()).isEqualTo(size);
	}

	// -------------------------------------------------------------------------
	// listarTudo
	// -------------------------------------------------------------------------

	@Test
	@Order(2)
	void testWhenListarTudoThenRetornarListaPessoas() {

		List<PessoaEntity> pessoasEntities = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			pessoasEntities.add(criarPessoaEntity(i));
		}

		// Given/Arrange
		given(pessoaRepository.findAll()).willReturn(pessoasEntities);

		// When/Act
		List<PessoaDto> resposta = pessoaService.listarTudo();

		// Then/Assert
		then(pessoaRepository).should(times(1)).findAll();
		BDDAssertions.then(resposta).hasSize(pessoasEntities.size());
		BDDAssertions.then(resposta.get(0).username()).isEqualTo(pessoasEntities.get(0).getUsername());
	}

	// -------------------------------------------------------------------------
	// buscarPorId
	// -------------------------------------------------------------------------

	@Test
	@Order(3)
	void testGivenPublicIdWhenBuscarPorPublicIdThenRetornarPessoa() {

		// Given/Arrange
		createPessoaEntity();
		given(pessoaRepository.findByPublicId(any(UUID.class))).willReturn(Optional.of(pessoaDefault));

		// When/Act
		PessoaDto resposta = pessoaService.buscarPorId(pessoaPublicId);

		// Then/Assert
		then(pessoaRepository).should(times(1)).findByPublicId(any());
		BDDAssertions.then(resposta).isNotNull();
		BDDAssertions.then(resposta.nome()).isEqualTo(pessoaDefault.getNome());
		BDDAssertions.then(resposta.email()).isEqualTo(pessoaDefault.getEmail());
	}

	@Test
	@Order(4)
	void testGivenIdInexistenteWhenBuscarPorIdThenLancaNaoEncontradoException() {

		// Given/Arrange
		given(pessoaRepository.findByPublicId(any(UUID.class))).willReturn(Optional.empty());

		// When/Then
		thenThrownBy(() -> pessoaService.buscarPorId(UUID.randomUUID()))
				.isInstanceOf(NaoEncontradoException.class);
	}

	// -------------------------------------------------------------------------
	// incluirNovo
	// -------------------------------------------------------------------------

	@Test
	@Order(5)
	void testGivenInclusaoDtoWhenIncluirNovoThenRetornarPessoaCriada() {

		// Given/Arrange
		var dto = new PessoaInclusaoDto(
				"novouser", "Novo Usuário", "novo@email.com", "(11) 98888-0000",
				LocalDate.parse("1995-06-15"), "Senha@123", "Senha@123",
				List.of(1, 2));

		var interesses = List.of(new InteresseEntity(1, "Java"), new InteresseEntity(2, "Web"));
		PessoaEntity entitySalva = criarPessoaEntity(99);
		entitySalva.setUsername(dto.username());
		entitySalva.setNome(dto.nome());
		entitySalva.setEmail(dto.email());

		given(passwordEncoder.encode(dto.senha())).willReturn("{bcrypt}hashed");
		given(interesseRepository.findByIdIn(dto.interessesIds())).willReturn(interesses);
		given(pessoaRepository.save(any(PessoaEntity.class))).willReturn(entitySalva);

		// When/Act
		PessoaDto resposta = pessoaService.incluirNovo(dto);

		// Then/Assert
		then(passwordEncoder).should(times(1)).encode(dto.senha());
		then(pessoaRepository).should(times(1)).save(any(PessoaEntity.class));
		BDDAssertions.then(resposta).isNotNull();
		BDDAssertions.then(resposta.username()).isEqualTo(dto.username());
		BDDAssertions.then(resposta.nome()).isEqualTo(dto.nome());
		BDDAssertions.then(resposta.email()).isEqualTo(dto.email());
	}

	// -------------------------------------------------------------------------
	// alterar
	// -------------------------------------------------------------------------

	@Test
	@Order(6)
	void testGivenAlteracaoDtoWhenAlterarThenRetornarPessoaAtualizada() {

		// Given/Arrange
		createPessoaEntity();
		var dto = new PessoaAlteracaoDto(
				"Nome Alterado", "alterado@email.com", "(11) 97777-0000",
				LocalDate.parse("1990-03-10"), List.of(1));

		given(pessoaRepository.findByPublicId(pessoaPublicId)).willReturn(Optional.of(pessoaDefault));
		given(interesseRepository.findByIdIn(dto.interessesIds()))
				.willReturn(List.of(new InteresseEntity(1, "Java")));
		given(pessoaRepository.save(any(PessoaEntity.class))).willAnswer(inv -> inv.getArgument(0));

		// When/Act
		PessoaDto resposta = pessoaService.alterar(pessoaPublicId, dto);

		// Then/Assert
		then(pessoaRepository).should(times(1)).findByPublicId(pessoaPublicId);
		then(pessoaRepository).should(times(1)).save(any(PessoaEntity.class));
		BDDAssertions.then(resposta).isNotNull();
		BDDAssertions.then(resposta.nome()).isEqualTo(dto.nome());
		BDDAssertions.then(resposta.email()).isEqualTo(dto.email());
	}

	@Test
	@Order(7)
	void testGivenIdInexistenteWhenAlterarThenLancaNaoEncontradoException() {

		// Given/Arrange
		var dto = new PessoaAlteracaoDto(
				"Nome Qualquer", "qualquer@email.com", null, null, List.of(1));

		given(pessoaRepository.findByPublicId(any(UUID.class))).willReturn(Optional.empty());

		// When/Then
		thenThrownBy(() -> pessoaService.alterar(UUID.randomUUID(), dto))
				.isInstanceOf(NaoEncontradoException.class);

		then(pessoaRepository).should(times(0)).save(any(PessoaEntity.class));
	}

	// -------------------------------------------------------------------------
	// excluir
	// -------------------------------------------------------------------------

	@Test
	@Order(8)
	void testGivenPublicIdWhenExcluirThenDeletaRegistro() {

		// Given/Arrange
		createPessoaEntity();
		given(pessoaRepository.existsByPublicId(pessoaPublicId)).willReturn(true);

		// When/Act
		pessoaService.excluir(pessoaPublicId);

		// Then/Assert
		then(pessoaRepository).should(times(1)).deleteByPublicId(pessoaPublicId);
	}

	@Test
	@Order(9)
	void testGivenIdInexistenteWhenExcluirThenLancaNaoEncontradoException() {

		// Given/Arrange
		UUID idInexistente = UUID.randomUUID();
		given(pessoaRepository.existsByPublicId(idInexistente)).willReturn(false);

		// When/Then
		thenThrownBy(() -> pessoaService.excluir(idInexistente))
				.isInstanceOf(NaoEncontradoException.class);

		then(pessoaRepository).should(times(0)).deleteByPublicId(any(UUID.class));
	}

}
