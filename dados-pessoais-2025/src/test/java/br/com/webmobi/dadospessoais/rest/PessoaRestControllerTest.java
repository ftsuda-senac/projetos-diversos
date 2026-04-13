package br.com.webmobi.dadospessoais.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import br.com.webmobi.dadospessoais.dominio.UrlMapper;
import br.com.webmobi.dadospessoais.dominio.dto.FotoInputDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaFotoDto;
import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;
import br.com.webmobi.dadospessoais.dominio.exception.NaoEncontradoException;
import br.com.webmobi.dadospessoais.dominio.service.PessoaFotoService;
import br.com.webmobi.dadospessoais.dominio.service.PessoaService;
import jakarta.validation.ConstraintViolationException;

@WebMvcTest(controllers = PessoaRestController.class, excludeAutoConfiguration = {
		SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class,
		ServletWebSecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class })
@AutoConfigureMockMvc
public class PessoaRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PessoaService pessoaService;

	@MockitoBean
	private PessoaFotoService pessoaFotoService;

	@MockitoBean
	private UrlMapper urlMapper;

	static PessoaEntity criarPessoa(int id) {
		PessoaEntity pessoaMock = new PessoaEntity();
		pessoaMock.setId(id);
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

	// -------------------------------------------------------------------------
	// GET /api/pessoas  —  listagem paginada
	// -------------------------------------------------------------------------

	@Test
	@Order(1)
	void testGivenPageRequestWhenListarThenRetornarPaginaPessoas() throws Exception {

		List<PessoaDto> pessoasDtos = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			pessoasDtos.add(new PessoaDto(criarPessoa(i)));
		}

		// Given/Arrange
		given(pessoaService.listar(any(Pageable.class))).willAnswer(invocation -> {
			Pageable pageable = invocation.getArgument(0);
			return new PageImpl<PessoaDto>(pessoasDtos.subList(0, pageable.getPageSize()));
		});
		int size = 2;

		// When/Act
		ResultActions response = mockMvc
				.perform(get("/api/pessoas").param("size", String.valueOf(size))
						.accept(MediaType.APPLICATION_JSON_VALUE));

		// Then/Assert
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.page.size", is(size)));
	}

	// -------------------------------------------------------------------------
	// GET /api/pessoas?all  —  listagem completa
	// -------------------------------------------------------------------------

	@Test
	@Order(2)
	void testWhenListarTudoThenRetornarListaPessoas() throws Exception {

		// Given/Arrange
		List<PessoaDto> dtos = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			dtos.add(new PessoaDto(criarPessoa(i)));
		}
		given(pessoaService.listarTudo()).willReturn(dtos);

		// When/Act
		ResultActions response = mockMvc.perform(
				get("/api/pessoas").param("all", "").accept(MediaType.APPLICATION_JSON_VALUE));

		// Then/Assert
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.content", hasSize(5)));
	}

	// -------------------------------------------------------------------------
	// GET /api/pessoas/{id}  —  busca por ID
	// -------------------------------------------------------------------------

	@Test
	@Order(3)
	void testGivenPublicIdWhenBuscarPorPublicIdThenRetornarPessoa() throws Exception {

		// Given/Arrange
		given(pessoaService.buscarPorId(any(UUID.class))).willAnswer(invocation -> {
			UUID publicId = invocation.getArgument(0);
			PessoaEntity entity = criarPessoa(1);
			entity.setPublicId(publicId);
			return new PessoaDto(entity);
		});

		// When/Act
		UUID publicId = UUID.randomUUID();
		ResultActions response = mockMvc.perform(
				get("/api/pessoas/{id}", publicId).accept(MediaType.APPLICATION_JSON_VALUE));

		// Then/Assert
		response.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.id", is(publicId.toString())))
				.andExpect(jsonPath("$.username", is("pessoa1")));
	}

	@Test
	@Order(4)
	void testGivenIdInexistenteWhenBuscarPorIdThenRetornar404() throws Exception {

		// Given/Arrange
		given(pessoaService.buscarPorId(any(UUID.class)))
				.willThrow(new NaoEncontradoException("Pessoa não encontrada"));

		// When/Then
		mockMvc.perform(get("/api/pessoas/{id}", UUID.randomUUID())
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound());
	}

	// -------------------------------------------------------------------------
	// POST /api/pessoas  —  inclusão
	// -------------------------------------------------------------------------

	@Test
	@Order(5)
	void testGivenNovaPessoaWhenIncluirNovoThenRetornar201() throws Exception {

		// Given/Arrange
		PessoaEntity entity = criarPessoa(99);
		given(pessoaService.incluirNovo(any())).willReturn(new PessoaDto(entity));

		String requestBody = """
				{"username":"pessoa99","nome":"Pessoa 99","email":"pessoa99@email.com",
				 "telefone":"(11) 99999-1234","dataNascimento":"2000-05-20",
				 "senha":"Abcd%1234","senhaConfirmacao":"Abcd%1234","interessesIds":[1,2]}
				""";

		// When/Act
		ResultActions response = mockMvc.perform(
				post("/api/pessoas")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON_VALUE));

		// Then/Assert
		response.andExpect(status().isCreated())
				.andDo(print())
				.andExpect(header().exists("Location"))
				.andExpect(jsonPath("$.username", is(entity.getUsername())))
				.andExpect(jsonPath("$.email", is(entity.getEmail())));
	}

	@Test
	@Order(6)
	void testGivenDtoInvalidoWhenIncluirNovoThenRetornar4xx() throws Exception {

		// Given/Arrange — serviço lança exceção de validação (ex.: senha fraca, campos obrigatórios)
		given(pessoaService.incluirNovo(any()))
				.willThrow(new ConstraintViolationException("Validação falhou", Set.of()));

		String requestBody = """
				{"username":"x","nome":"","email":"invalido","senha":"fraco","senhaConfirmacao":"diferente","interessesIds":[]}
				""";

		// When/Then
		mockMvc.perform(
				post("/api/pessoas")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is4xxClientError());
	}

	// -------------------------------------------------------------------------
	// PUT /api/pessoas/{id}  —  alteração
	// -------------------------------------------------------------------------

	@Test
	@Order(7)
	void testGivenAlteracaoDtoWhenAlterarThenRetornar200() throws Exception {

		// Given/Arrange
		PessoaEntity entity = criarPessoa(1);
		UUID publicId = entity.getPublicId();
		given(pessoaService.alterar(any(UUID.class), any())).willAnswer(inv -> {
			entity.setNome("Nome Alterado");
			entity.setEmail("alterado@email.com");
			return new PessoaDto(entity);
		});

		String requestBody = """
				{"nome":"Nome Alterado","email":"alterado@email.com","interessesIds":[1]}
				""";

		// When/Act
		ResultActions response = mockMvc.perform(
				put("/api/pessoas/{id}", publicId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON_VALUE));

		// Then/Assert
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.nome", is("Nome Alterado")))
				.andExpect(jsonPath("$.email", is("alterado@email.com")));
	}

	@Test
	@Order(8)
	void testGivenIdInexistenteWhenAlterarThenRetornar404() throws Exception {

		// Given/Arrange
		given(pessoaService.alterar(any(UUID.class), any()))
				.willThrow(new NaoEncontradoException("Pessoa não encontrada"));

		String requestBody = """
				{"nome":"Qualquer","email":"qualquer@email.com","interessesIds":[1]}
				""";

		// When/Then
		mockMvc.perform(
				put("/api/pessoas/{id}", UUID.randomUUID())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound());
	}

	// -------------------------------------------------------------------------
	// DELETE /api/pessoas/{id}  —  exclusão
	// -------------------------------------------------------------------------

	@Test
	@Order(9)
	void testGivenIdWhenExcluirThenRetornar204() throws Exception {

		// Given/Arrange
		willDoNothing().given(pessoaService).excluir(any(UUID.class));

		// When/Then
		mockMvc.perform(delete("/api/pessoas/{id}", UUID.randomUUID()))
				.andExpect(status().isNoContent());
	}

	@Test
	@Order(10)
	void testGivenIdInexistenteWhenExcluirThenRetornar404() throws Exception {

		// Given/Arrange
		willThrow(new NaoEncontradoException("Pessoa não encontrada"))
				.given(pessoaService).excluir(any(UUID.class));

		// When/Then
		mockMvc.perform(delete("/api/pessoas/{id}", UUID.randomUUID()))
				.andExpect(status().isNotFound());
	}

	// -------------------------------------------------------------------------
	// POST /api/pessoas/{pessoaId}/fotos  —  upload de foto
	// -------------------------------------------------------------------------

	@Test
	@Order(11)
	void testGivenFotoWhenIncluirFotoThenRetornar201() throws Exception {

		// Given/Arrange
		UUID pessoaId = UUID.randomUUID();
		String nomeArquivo = "foto.jpg";
		String urlPath = "/uploads/" + pessoaId + "/fotos/" + nomeArquivo;

		given(pessoaFotoService.salvar(any(UUID.class), any(FotoInputDto.class)))
				.willReturn(new PessoaFotoDto(nomeArquivo, "Legenda de teste"));
		given(urlMapper.getImagemUrlPath(any(UUID.class), any(String.class)))
				.willReturn(urlPath);

		MockMultipartFile arquivo = new MockMultipartFile(
				"arquivo", nomeArquivo, MediaType.IMAGE_JPEG_VALUE, "fake-image-content".getBytes());

		// When/Then
		mockMvc.perform(
				multipart("/api/pessoas/{pessoaId}/fotos", pessoaId)
						.file(arquivo)
						.param("legenda", "Legenda de teste"))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"));
	}

	// -------------------------------------------------------------------------
	// DELETE /api/pessoas/{pessoaId}/fotos/{nomeArquivo}  —  exclusão de foto
	// -------------------------------------------------------------------------

	@Test
	@Order(12)
	void testGivenNomeFotoWhenExcluirFotoThenRetornar204() throws Exception {

		// Given/Arrange
		willDoNothing().given(pessoaFotoService).excluir(any(UUID.class), any(String.class));

		// When/Then
		mockMvc.perform(
				delete("/api/pessoas/{pessoaId}/fotos/{nomeArquivo}", UUID.randomUUID(), "foto.jpg"))
				.andExpect(status().isNoContent());
	}

}
