package br.com.webmobi.dadospessoais.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import br.com.webmobi.dadospessoais.dominio.UrlMapper;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaDto;
import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;
import br.com.webmobi.dadospessoais.dominio.service.PessoaFotoService;
import br.com.webmobi.dadospessoais.dominio.service.PessoaService;

// Ver https://stackoverflow.com/questions/47593537/disable-spring-security-config-class-for-webmvctest-in-spring-boot/
@WebMvcTest(controllers = PessoaRestController.class, excludeAutoConfiguration = {
		OAuth2ResourceServerAutoConfiguration.class, SecurityAutoConfiguration.class })
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

	@Test
	@Order(2)
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
				get("/api/pessoas/{id}", publicId).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON_VALUE));

		// Then/Assert
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.id", is(publicId.toString())));
	}

}
