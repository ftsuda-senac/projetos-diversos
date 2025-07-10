package br.com.webmobi.dadospessoais.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.BDDAssertions.then;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.webmobi.dadospessoais.dominio.dto.PessoaDto;
import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;
import br.com.webmobi.dadospessoais.dominio.repository.InteresseRepository;
import br.com.webmobi.dadospessoais.dominio.repository.PessoaRepository;
import br.com.webmobi.dadospessoais.rest.PagedContentContainerDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PessoaIntegrationTest {

	private RequestSpecification specification;

	@LocalServerPort
	int port;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private InteresseRepository interesseRepository;

	@Autowired
	private ObjectMapper objectMapper;

	PessoaEntity criarPessoa(int id) {
		PessoaEntity pessoaMock = new PessoaEntity();
		// pessoaMock.setId(id);
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
		pessoaMock.setInteresses(new HashSet<>(interesseRepository.findAll()));
		return pessoaMock;
	}

	@BeforeAll
	public void setup() {
		interesseRepository.saveAllAndFlush(List.of(new InteresseEntity("Java"), new InteresseEntity("Web")));
		for (int i = 1; i <= 10; i++) {
			pessoaRepository.save(criarPessoa(i));
		}

		specification = new RequestSpecBuilder() //
				.setPort(port) //
				.setBasePath("/api/pessoas") //
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)) //
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL)) //
				.build();
	}

	//	@BeforeEach
	//	@Transactional
	//	void beforeEach() {
	//		pessoaRepository.deleteAll();
	//		pessoaRepository.flush();
	//
	//	}

	@Test
	@Order(1)
	public void integrationTestGivenPageRequestWhenListarThenRetornarPaginaPessoas()
			throws JsonMappingException, JsonProcessingException {

		// Given
		var response = given().spec(specification) //
				.when() //
				.get() //
				.then() // 
				.statusCode(200) //
				.extract().body().asString(); //

		PagedContentContainerDto<PessoaDto> pageResult = objectMapper.readValue(response,
				new TypeReference<PagedContentContainerDto<PessoaDto>>() {
		});

		then(pageResult.page().size()).isGreaterThan(0);
		then(pageResult.page().totalElements()).isGreaterThan(0);
		// then(pageResult.getPage().totalElements()).isEqualTo(10L);

	}

}
