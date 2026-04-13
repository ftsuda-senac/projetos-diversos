package br.com.webmobi.dadospessoais.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import br.com.webmobi.dadospessoais.dominio.dto.InteresseDto;
import br.com.webmobi.dadospessoais.dominio.repository.InteresseRepository;
import br.com.webmobi.dadospessoais.dominio.service.InteresseService;

@WebMvcTest(controllers = InteresseRestController.class, excludeAutoConfiguration = {
		SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class,
		ServletWebSecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class })
@AutoConfigureMockMvc
public class InteresseRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private InteresseService interesseService;

	// InteresseUnicoValidator injeta InteresseRepository via Spring — precisa estar
	// no contexto mesmo que o @WebMvcTest não carregue a camada JPA
	@MockitoBean
	private InteresseRepository interesseRepository;

	// -------------------------------------------------------------------------
	// GET /api/interesses  —  listagem completa
	// -------------------------------------------------------------------------

	@Test
	@Order(1)
	void testWhenListarThenRetornarListaInteresses() throws Exception {

		// Given/Arrange
		List<InteresseDto> dtos = List.of(
				new InteresseDto(1, "Java"),
				new InteresseDto(2, "Web"),
				new InteresseDto(3, "Spring"));
		given(interesseService.listarTudo()).willReturn(dtos);

		// When/Act
		ResultActions response = mockMvc.perform(
				get("/api/interesses").accept(MediaType.APPLICATION_JSON_VALUE));

		// Then/Assert
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].nome", is("Java")))
				.andExpect(jsonPath("$[1].nome", is("Web")));
	}

	// -------------------------------------------------------------------------
	// POST /api/interesses  —  inclusão
	// -------------------------------------------------------------------------

	@Test
	@Order(2)
	void testGivenInteresseDtoWhenIncluirNovoThenRetornar201() throws Exception {

		// Given/Arrange
		InteresseDto dtoSalvo = new InteresseDto(10, "Python");
		given(interesseService.incluirNovo(any(InteresseDto.class))).willReturn(dtoSalvo);

		String requestBody = """
				{"nome":"Python"}
				""";

		// When/Act
		ResultActions response = mockMvc.perform(
				post("/api/interesses")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON_VALUE));

		// Then/Assert
		response.andExpect(status().isCreated())
				.andDo(print())
				.andExpect(header().exists("Location"))
				.andExpect(jsonPath("$.id", is(10)))
				.andExpect(jsonPath("$.nome", is("Python")));
	}

	@Test
	@Order(3)
	void testGivenNomeEmBrancoWhenIncluirNovoThenRetornar400() throws Exception {

		// O controller tem @Valid no @RequestBody, então a validação ocorre no
		// nível do Spring MVC e lança MethodArgumentNotValidException → 400,
		// sem chegar ao service.
		String requestBody = """
				{"nome":""}
				""";

		// When/Then
		mockMvc.perform(
				post("/api/interesses")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest());
	}

}
