package br.com.webmobi.dadospessoais.integration;

import static org.assertj.core.api.BDDAssertions.then;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

import br.com.webmobi.dadospessoais.dominio.dto.PessoaAlteracaoDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaInclusaoDto;
import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;
import br.com.webmobi.dadospessoais.dominio.repository.InteresseRepository;
import br.com.webmobi.dadospessoais.dominio.repository.PessoaRepository;
import br.com.webmobi.dadospessoais.rest.ListContentContainerDto;
import br.com.webmobi.dadospessoais.rest.PagedContentContainerDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class PessoaIntegrationTest {

	@LocalServerPort
	int port;

	private RestTestClient restTestClient;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private InteresseRepository interesseRepository;

	/** publicId de uma pessoa criada no setup — usada nos testes de leitura. */
	private UUID pessoaPublicId;

	/** IDs dos interesses disponíveis — usados nos testes de criação/alteração. */
	private List<Integer> interessesIds;

	/** publicId da pessoa criada no teste de inclusão — usada nos testes de alteração e exclusão. */
	private UUID pessoaTestId;

	PessoaEntity criarPessoa(int id) {
		PessoaEntity pessoaMock = new PessoaEntity();
		String username = "pessoa" + id;
		pessoaMock.setUsername(username);
		pessoaMock.setNome("Pessoa " + id);
		pessoaMock.setEmail(username + "@email.com");
		pessoaMock.setTelefone("(11) 99999-1234");
		pessoaMock.setDataNascimento(LocalDate.parse("2000-05-20"));
		pessoaMock.setInteresses(java.util.Collections.emptySet());
		return pessoaMock;
	}

	@BeforeAll
	public void setup() {
		restTestClient = RestTestClient.bindToServer()
				.baseUrl("http://localhost:" + port)
				.build();

		interessesIds = interesseRepository
				.saveAllAndFlush(List.of(new InteresseEntity("Java"), new InteresseEntity("Web")))
				.stream().map(InteresseEntity::getId).toList();

		PessoaEntity primeira = pessoaRepository.save(criarPessoa(1));
		pessoaPublicId = primeira.getPublicId();

		for (int i = 2; i <= 10; i++) {
			pessoaRepository.save(criarPessoa(i));
		}
	}

	// -------------------------------------------------------------------------
	// GET /api/pessoas  —  listagem paginada
	// -------------------------------------------------------------------------

	@Test
	@Order(1)
	void testGivenPageRequestWhenListarThenRetornarPaginaPessoas() {

		restTestClient.get()
				.uri("/api/pessoas")
				.exchange()
				.expectStatus().isOk()
				.expectBody(new ParameterizedTypeReference<PagedContentContainerDto<PessoaDto>>() {})
				.value(pageResult -> {
					then(pageResult.page().size()).isGreaterThan(0);
					then(pageResult.page().totalElements()).isGreaterThan(0);
				});
	}

	// -------------------------------------------------------------------------
	// GET /api/pessoas?all  —  listagem completa
	// -------------------------------------------------------------------------

	@Test
	@Order(2)
	void testWhenListarTudoThenRetornarTodasAsPessoas() {

		restTestClient.get()
				.uri("/api/pessoas?all")
				.exchange()
				.expectStatus().isOk()
				.expectBody(new ParameterizedTypeReference<ListContentContainerDto<PessoaDto>>() {})
				.value(result -> {
					then(result.content()).isNotEmpty();
					then(result.content()).hasSizeGreaterThanOrEqualTo(10);
				});
	}

	// -------------------------------------------------------------------------
	// GET /api/pessoas/{id}  —  busca por ID
	// -------------------------------------------------------------------------

	@Test
	@Order(3)
	void testGivenPublicIdWhenBuscarPorIdThenRetornarPessoa() {

		restTestClient.get()
				.uri("/api/pessoas/{id}", pessoaPublicId)
				.exchange()
				.expectStatus().isOk()
				.expectBody(PessoaDto.class)
				.value(pessoa -> {
					then(pessoa.id()).isEqualTo(pessoaPublicId);
					then(pessoa.username()).isEqualTo("pessoa1");
					then(pessoa.email()).isEqualTo("pessoa1@email.com");
				});
	}

	@Test
	@Order(4)
	void testGivenIdInexistenteWhenBuscarPorIdThenRetornar404() {

		restTestClient.get()
				.uri("/api/pessoas/{id}", UUID.randomUUID())
				.exchange()
				.expectStatus().isNotFound();
	}

	// -------------------------------------------------------------------------
	// POST /api/pessoas  —  inclusão
	// -------------------------------------------------------------------------

	@Test
	@Order(5)
	void testGivenNovaPessoaWhenIncluirNovoThenRetornar201ComPessoaCriada() {

		var dto = new PessoaInclusaoDto(
				"integrationtest", "Integration Test User", "integration@email.com",
				"(11) 98888-0001", LocalDate.parse("1990-06-15"),
				"Abcd%1234", "Abcd%1234",
				interessesIds);

		var result = restTestClient.post()
				.uri("/api/pessoas")
				.contentType(MediaType.APPLICATION_JSON)
				.body(dto)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().exists("Location")
				.expectBody(PessoaDto.class)
				.returnResult();

		PessoaDto criada = result.getResponseBody();
		then(criada).isNotNull();
		then(criada.id()).isNotNull();
		then(criada.username()).isEqualTo(dto.username());
		then(criada.email()).isEqualTo(dto.email());

		pessoaTestId = criada.id();
	}

	@Test
	@Order(6)
	void testGivenDtoInvalidoWhenIncluirNovoThenRetornar4xx() {

		// Senha em branco e senhas divergentes devem falhar na validação
		var dto = new PessoaInclusaoDto(
				"usuarioinvalido", "Usuário Inválido", "invalido@email.com",
				null, null,
				"", "",
				interessesIds);

		restTestClient.post()
				.uri("/api/pessoas")
				.contentType(MediaType.APPLICATION_JSON)
				.body(dto)
				.exchange()
				.expectStatus().is4xxClientError();
	}

	// -------------------------------------------------------------------------
	// PUT /api/pessoas/{id}  —  alteração
	// -------------------------------------------------------------------------

	@Test
	@Order(7)
	void testGivenAlteracaoDtoWhenAlterarThenRetornarPessoaAtualizada() {

		var dto = new PessoaAlteracaoDto(
				"Nome Atualizado", "atualizado@email.com",
				"(11) 97777-0000", LocalDate.parse("1992-03-10"),
				interessesIds);

		restTestClient.put()
				.uri("/api/pessoas/{id}", pessoaTestId)
				.contentType(MediaType.APPLICATION_JSON)
				.body(dto)
				.exchange()
				.expectStatus().isOk()
				.expectBody(PessoaDto.class)
				.value(atualizada -> {
					then(atualizada.id()).isEqualTo(pessoaTestId);
					then(atualizada.nome()).isEqualTo(dto.nome());
					then(atualizada.email()).isEqualTo(dto.email());
				});
	}

	@Test
	@Order(8)
	void testGivenIdInexistenteWhenAlterarThenRetornar404() {

		var dto = new PessoaAlteracaoDto(
				"Nome Qualquer", "qualquer@email.com", null, null, interessesIds);

		restTestClient.put()
				.uri("/api/pessoas/{id}", UUID.randomUUID())
				.contentType(MediaType.APPLICATION_JSON)
				.body(dto)
				.exchange()
				.expectStatus().isNotFound();
	}

	// -------------------------------------------------------------------------
	// DELETE /api/pessoas/{id}  —  exclusão
	// -------------------------------------------------------------------------

	@Test
	@Order(9)
	void testGivenPublicIdWhenExcluirThenRetornar204() {

		restTestClient.delete()
				.uri("/api/pessoas/{id}", pessoaTestId)
				.exchange()
				.expectStatus().isNoContent();

		// Verifica que a pessoa foi de fato removida
		restTestClient.get()
				.uri("/api/pessoas/{id}", pessoaTestId)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	@Order(10)
	void testGivenIdInexistenteWhenExcluirThenRetornar404() {

		restTestClient.delete()
				.uri("/api/pessoas/{id}", UUID.randomUUID())
				.exchange()
				.expectStatus().isNotFound();
	}

}
