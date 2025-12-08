package br.com.webmobi.dadospessoais.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.webmobi.dadospessoais.dominio.dto.PessoaDto;
import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;
import br.com.webmobi.dadospessoais.dominio.repository.InteresseRepository;
import br.com.webmobi.dadospessoais.dominio.repository.PessoaRepository;
import br.com.webmobi.dadospessoais.dominio.service.PessoaService;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {

	@Mock
	PessoaRepository pessoaRepository;

	@Mock
	InteresseRepository interesseRepository;

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

	@Test
	@Order(2)
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

}
