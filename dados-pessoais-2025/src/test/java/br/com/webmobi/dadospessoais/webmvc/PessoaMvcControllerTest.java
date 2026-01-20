package br.com.webmobi.dadospessoais.webmvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.webmobi.dadospessoais.dominio.UrlMapper;
import br.com.webmobi.dadospessoais.dominio.dto.InteresseDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaInclusaoDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaMvcDto;
import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;
import br.com.webmobi.dadospessoais.dominio.service.InteresseService;
import br.com.webmobi.dadospessoais.dominio.service.PessoaFotoService;
import br.com.webmobi.dadospessoais.dominio.service.PessoaService;
import jakarta.validation.ConstraintViolationException;

@WebMvcTest(controllers = PessoaMvcController.class, excludeAutoConfiguration = {
		OAuth2ResourceServerAutoConfiguration.class, SecurityAutoConfiguration.class })
@AutoConfigureMockMvc
class PessoaMvcControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PessoaService pessoaService;

	@MockitoBean
	private PessoaFotoService pessoaFotoService;

	@MockitoBean
	private InteresseService interesseService;

	@MockitoBean
	private UrlMapper urlMapper;

	private PessoaEntity criarPessoa(int id) {
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

	private PessoaMvcDto criarPessoaMvcDto(UUID publicId) {
		return new PessoaMvcDto(publicId, "fulano", "Fulano da Silva", "fulano@email.com", "(11) 99999-1234",
				LocalDate.parse("2000-05-20"), List.of(1, 2));
	}

	@Test
	void listarDeveRetornarPaginaComItens() throws Exception {
		given(pessoaService.listar(any(Pageable.class)))
				.willReturn(new PageImpl<>(List.of(new PessoaDto(criarPessoa(1)))));

		mockMvc.perform(get("/mvc/pessoas")).andExpect(status().isOk())
				.andExpect(view().name("pessoas/lista")).andExpect(model().attributeExists("itens"));
	}

	@Test
	void incluirFormDeveRetornarFormulario() throws Exception {
		given(interesseService.listarTudo()).willReturn(List.of(new InteresseDto(1, "Java")));

		mockMvc.perform(get("/mvc/pessoas/incluir")).andExpect(status().isOk())
				.andExpect(view().name("pessoas/form")).andExpect(model().attributeExists("item"))
				.andExpect(model().attributeExists("opcoesInteresses"));
	}

	@Test
	void alterarFormDeveRetornarFormularioComItem() throws Exception {
		UUID publicId = UUID.randomUUID();
		given(pessoaService.buscarPorIdMvc(publicId)).willReturn(criarPessoaMvcDto(publicId));
		given(interesseService.listarTudo()).willReturn(List.of(new InteresseDto(1, "Java")));

		mockMvc.perform(get("/mvc/pessoas/{id}/alterar", publicId)).andExpect(status().isOk())
				.andExpect(view().name("pessoas/form")).andExpect(model().attributeExists("item"))
				.andExpect(model().attributeExists("opcoesInteresses"));
	}

	@Test
	void incluirNovoDeveRedirecionarQuandoSucesso() throws Exception {
		PessoaEntity pessoa = criarPessoa(1);
		given(pessoaService.incluirNovo(any(PessoaInclusaoDto.class))).willReturn(new PessoaDto(pessoa));

		mockMvc.perform(post("/mvc/pessoas/incluir").param("username", "fulano").param("nome", "Fulano da Silva")
				.param("email", "fulano@email.com").param("telefone", "(11) 99999-1234")
				.param("dataNascimento", "2000-05-20").param("senha", "Abcd1234")
				.param("senhaConfirmacao", "Abcd1234").param("interessesIds", "1", "2"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/mvc/pessoas"))
				.andExpect(flash().attributeExists("msg"));
	}

	@Test
	void incluirNovoDeveRetornarFormularioQuandoErroValidacao() throws Exception {
		given(interesseService.listarTudo()).willReturn(List.of(new InteresseDto(1, "Java")));
		given(pessoaService.incluirNovo(any(PessoaInclusaoDto.class)))
				.willThrow(new ConstraintViolationException(Set.of()));

		mockMvc.perform(post("/mvc/pessoas/incluir").param("username", "fulano").param("nome", "Fulano da Silva")
				.param("email", "fulano@email.com").param("telefone", "(11) 99999-1234")
				.param("dataNascimento", "2000-05-20").param("senha", "Abcd1234")
				.param("senhaConfirmacao", "Abcd1234").param("interessesIds", "1", "2"))
				.andExpect(status().isOk()).andExpect(view().name("pessoas/form"))
				.andExpect(model().attributeExists("opcoesInteresses"));
	}

	@Test
	void alterarDeveRedirecionarQuandoSucesso() throws Exception {
		UUID publicId = UUID.randomUUID();
		PessoaEntity pessoa = criarPessoa(1);
		given(pessoaService.alterar(any(UUID.class), any())).willReturn(new PessoaDto(pessoa));

		mockMvc.perform(post("/mvc/pessoas/{id}/alterar", publicId).param("username", "fulano")
				.param("nome", "Fulano da Silva").param("email", "fulano@email.com")
				.param("telefone", "(11) 99999-1234").param("dataNascimento", "2000-05-20")
				.param("interessesIds", "1", "2"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/mvc/pessoas"))
				.andExpect(flash().attributeExists("msg"));
	}

	@Test
	void excluirDeveRedirecionarQuandoSucesso() throws Exception {
		UUID publicId = UUID.randomUUID();

		mockMvc.perform(post("/mvc/pessoas/{id}/excluir", publicId)).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/mvc/pessoas")).andExpect(flash().attributeExists("msg"));
	}
}
