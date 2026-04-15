package br.com.webmobi.dadospessoais.webmvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import br.com.webmobi.dadospessoais.dominio.dto.InteresseDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaFotoDto;
import br.com.webmobi.dadospessoais.dominio.dto.PessoaMvcDto;
import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;
import br.com.webmobi.dadospessoais.dominio.exception.NaoEncontradoException;
import br.com.webmobi.dadospessoais.dominio.service.InteresseService;
import br.com.webmobi.dadospessoais.dominio.service.PessoaFotoService;
import br.com.webmobi.dadospessoais.dominio.service.PessoaService;
import br.com.webmobi.dadospessoais.webmvc.util.ConstraintViolationsToErrorsConverter;
import jakarta.validation.ConstraintViolationException;

@WebMvcTest(controllers = PessoaMvcController.class, excludeAutoConfiguration = {
        SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class,
        ServletWebSecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class })
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PessoaMvcControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PessoaService pessoaService;

    @MockitoBean
    private PessoaFotoService pessoaFotoService;

    @MockitoBean
    private InteresseService interesseService;

    @MockitoBean
    private ConstraintViolationsToErrorsConverter constraintViolationsConverter;

    @MockitoBean
    private UrlMapper urlMapper;

    static PessoaEntity criarPessoa(int id) {
        PessoaEntity entity = new PessoaEntity();
        entity.setId(id);
        entity.setPublicId(UUID.randomUUID());
        String username = "pessoa" + id;
        entity.setUsername(username);
        entity.setNome("Pessoa " + id);
        entity.setEmail(username + "@email.com");
        entity.setTelefone("(11) 99999-1234");
        entity.setDataNascimento(LocalDate.parse("2000-05-20"));
        Instant now = Instant.now();
        entity.setDataCriacao(now);
        entity.setDataAtualizacao(now);
        entity.setInteresses(new HashSet<>(List.of(new InteresseEntity(1, "Java"), new InteresseEntity(2, "Web"))));
        entity.setFotos(new HashSet<>());
        return entity;
    }

    static List<InteresseDto> criarInteresses() {
        return List.of(new InteresseDto(1, "Java"), new InteresseDto(2, "Web"));
    }

    // -------------------------------------------------------------------------
    // GET /mvc/pessoas  —  listagem paginada
    // -------------------------------------------------------------------------

    @Test
    @Order(1)
    void testGivenPageRequestWhenListarThenRetornarViewListaComItens() throws Exception {

        // Given/Arrange
        List<PessoaDto> dtos = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            dtos.add(new PessoaDto(criarPessoa(i)));
        }
        given(pessoaService.listar(any(Pageable.class))).willAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(0);
            return new PageImpl<>(dtos, pageable, dtos.size());
        });
        given(urlMapper.getImagemUrlPath(any(UUID.class), any(String.class)))
                .willReturn("/uploads/test/fotos/test.jpg");

        // When/Act
        ResultActions response = mockMvc.perform(get("/mvc/pessoas").accept(MediaType.TEXT_HTML));

        // Then/Assert
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("pessoas/lista"))
                .andExpect(model().attributeExists("itens"));
    }

    // -------------------------------------------------------------------------
    // GET /mvc/pessoas/incluir  —  formulário de inclusão
    // -------------------------------------------------------------------------

    @Test
    @Order(2)
    void testWhenIncluirFormThenRetornarViewFormComAtributos() throws Exception {

        // Given/Arrange
        given(interesseService.listarTudo()).willReturn(criarInteresses());

        // When/Act
        ResultActions response = mockMvc.perform(get("/mvc/pessoas/incluir").accept(MediaType.TEXT_HTML));

        // Then/Assert
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("pessoas/form"))
                .andExpect(model().attributeExists("item"))
                .andExpect(model().attributeExists("opcoesInteresses"));
    }

    // -------------------------------------------------------------------------
    // GET /mvc/pessoas/{id}/alterar  —  formulário de alteração
    // -------------------------------------------------------------------------

    @Test
    @Order(3)
    void testGivenIdExistenteWhenAlterarFormThenRetornarViewFormComDadosDaPessoa() throws Exception {

        // Given/Arrange
        PessoaEntity entity = criarPessoa(1);
        UUID publicId = entity.getPublicId();
        given(pessoaService.buscarPorIdMvc(publicId)).willReturn(new PessoaMvcDto(entity));
        given(interesseService.listarTudo()).willReturn(criarInteresses());

        // When/Act
        ResultActions response = mockMvc
                .perform(get("/mvc/pessoas/{id}/alterar", publicId).accept(MediaType.TEXT_HTML));

        // Then/Assert
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("pessoas/form"))
                .andExpect(model().attributeExists("item"))
                .andExpect(model().attributeExists("opcoesInteresses"));
    }

    @Test
    @Order(4)
    void testGivenIdInexistenteWhenAlterarFormThenRetornar404() throws Exception {

        // Given/Arrange
        given(pessoaService.buscarPorIdMvc(any(UUID.class)))
                .willThrow(new NaoEncontradoException("Pessoa não encontrada"));

        // When/Then
        mockMvc.perform(get("/mvc/pessoas/{id}/alterar", UUID.randomUUID()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/incluir  —  inclusão com sucesso
    // -------------------------------------------------------------------------

    @Test
    @Order(5)
    void testGivenFormValidoWhenIncluirNovoThenRedirecionarParaListaComMensagemSucesso() throws Exception {

        // Given/Arrange
        PessoaEntity entity = criarPessoa(99);
        given(pessoaService.incluirNovo(any())).willReturn(new PessoaDto(entity));

        // When/Act
        ResultActions response = mockMvc.perform(post("/mvc/pessoas/incluir")
                .param("username", "pessoa99")
                .param("nome", "Pessoa 99")
                .param("email", "pessoa99@email.com")
                .param("telefone", "(11) 99999-1234")
                .param("dataNascimento", "2000-05-20")
                .param("senha", "Abcd@1234")
                .param("senhaConfirmacao", "Abcd@1234")
                .param("interessesIds", "1", "2"));

        // Then/Assert
        response.andExpect(status().is3xxRedirection())
                .andDo(print())
                .andExpect(redirectedUrl("/mvc/pessoas"))
                .andExpect(flash().attributeExists("msg"));
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/incluir  —  falha de validação no serviço
    // -------------------------------------------------------------------------

    @Test
    @Order(8)
    void testGivenFormInvalidoWhenIncluirNovoThenRetornarFormComOpcoesInteresses() throws Exception {

        // Given/Arrange
        given(pessoaService.incluirNovo(any()))
                .willThrow(new ConstraintViolationException("Validação falhou", Set.of()));
        given(interesseService.listarTudo()).willReturn(criarInteresses());

        // When/Act
        ResultActions response = mockMvc.perform(post("/mvc/pessoas/incluir")
                .param("username", "x")
                .param("nome", "Pessoa Invalida")
                .param("email", "invalido")
                .param("senha", "fraco")
                .param("senhaConfirmacao", "diferente")
                .param("interessesIds", "1"));

        // Then/Assert
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("pessoas/form"))
                .andExpect(model().attributeExists("opcoesInteresses"));
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/{id}/alterar  —  alteração com sucesso
    // -------------------------------------------------------------------------

    @Test
    @Order(9)
    void testGivenFormAlteracaoValidoWhenAlterarThenRedirecionarParaListaComMensagemSucesso() throws Exception {

        // Given/Arrange
        PessoaEntity entity = criarPessoa(1);
        UUID publicId = entity.getPublicId();
        entity.setNome("Nome Alterado");
        entity.setEmail("alterado@email.com");
        given(pessoaService.alterar(any(UUID.class), any())).willReturn(new PessoaDto(entity));

        // When/Act
        ResultActions response = mockMvc.perform(post("/mvc/pessoas/{id}/alterar", publicId)
                .param("nome", "Nome Alterado")
                .param("email", "alterado@email.com")
                .param("telefone", "(11) 99999-1234")
                .param("dataNascimento", "2000-05-20")
                .param("interessesIds", "1", "2"));

        // Then/Assert
        response.andExpect(status().is3xxRedirection())
                .andDo(print())
                .andExpect(redirectedUrl("/mvc/pessoas"))
                .andExpect(flash().attributeExists("msg"));
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/{id}/alterar  —  falha de validação no serviço
    // -------------------------------------------------------------------------

    @Test
    @Order(10)
    void testGivenFormAlteracaoInvalidoWhenAlterarThenRetornarFormComOpcoesInteresses() throws Exception {

        // Given/Arrange
        UUID publicId = UUID.randomUUID();
        given(pessoaService.alterar(any(UUID.class), any()))
                .willThrow(new ConstraintViolationException("Validação falhou", Set.of()));
        given(interesseService.listarTudo()).willReturn(criarInteresses());

        // When/Act
        ResultActions response = mockMvc.perform(post("/mvc/pessoas/{id}/alterar", publicId)
                .param("nome", "")
                .param("email", "invalido")
                .param("interessesIds", "1"));

        // Then/Assert
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("pessoas/form"))
                .andExpect(model().attributeExists("opcoesInteresses"));
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/{id}/alterar  —  pessoa não encontrada
    // -------------------------------------------------------------------------

    @Test
    @Order(11)
    void testGivenIdInexistenteWhenAlterarThenRetornar404() throws Exception {

        // Given/Arrange
        given(pessoaService.alterar(any(UUID.class), any()))
                .willThrow(new NaoEncontradoException("Pessoa não encontrada"));

        // When/Then
        mockMvc.perform(post("/mvc/pessoas/{id}/alterar", UUID.randomUUID())
                        .param("nome", "Qualquer")
                        .param("email", "qualquer@email.com")
                        .param("interessesIds", "1"))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/{id}/excluir  —  exclusão com sucesso
    // -------------------------------------------------------------------------

    @Test
    @Order(12)
    void testGivenIdExistenteWhenExcluirThenRedirecionarParaListaComMensagemSucesso() throws Exception {

        // Given/Arrange
        willDoNothing().given(pessoaService).excluir(any(UUID.class));

        // When/Act
        ResultActions response = mockMvc.perform(post("/mvc/pessoas/{id}/excluir", UUID.randomUUID()));

        // Then/Assert
        response.andExpect(status().is3xxRedirection())
                .andDo(print())
                .andExpect(redirectedUrl("/mvc/pessoas"))
                .andExpect(flash().attributeExists("msg"));
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/{id}/excluir  —  pessoa não encontrada
    // -------------------------------------------------------------------------

    @Test
    @Order(13)
    void testGivenIdInexistenteWhenExcluirThenRetornar404() throws Exception {

        // Given/Arrange
        willThrow(new NaoEncontradoException("Pessoa não encontrada"))
                .given(pessoaService).excluir(any(UUID.class));

        // When/Then
        mockMvc.perform(post("/mvc/pessoas/{id}/excluir", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/{pessoaId}/fotos  —  upload de foto
    // -------------------------------------------------------------------------

    @Test
    @Order(14)
    void testGivenFotoWhenIncluirFotoThenRetornar201ComLocationHeader() throws Exception {

        // Given/Arrange
        UUID pessoaId = UUID.randomUUID();
        String nomeArquivo = "foto.jpg";
        String urlPath = "/uploads/" + pessoaId + "/fotos/" + nomeArquivo;

        given(pessoaFotoService.salvar(any(UUID.class), any(FotoInputDto.class)))
                .willReturn(new PessoaFotoDto(nomeArquivo, "Legenda de teste"));
        given(urlMapper.getImagemUrlPath(any(UUID.class), any(String.class))).willReturn(urlPath);

        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo", nomeArquivo, MediaType.IMAGE_JPEG_VALUE, "fake-image-content".getBytes());

        // When/Then
        mockMvc.perform(multipart("/mvc/pessoas/{pessoaId}/fotos", pessoaId)
                        .file(arquivo)
                        .param("legenda", "Legenda de teste"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(header().exists("Location"));
    }

    // -------------------------------------------------------------------------
    // DELETE /mvc/pessoas/{pessoaId}/fotos/{nomeArquivo}  —  exclusão de foto
    // -------------------------------------------------------------------------

    @Test
    @Order(15)
    void testGivenNomeFotoWhenExcluirFotoThenRetornar204() throws Exception {

        // Given/Arrange
        willDoNothing().given(pessoaFotoService).excluir(any(UUID.class), any(String.class));

        // When/Then
        mockMvc.perform(
                        delete("/mvc/pessoas/{pessoaId}/fotos/{nomeArquivo}", UUID.randomUUID(), "foto.jpg"))
                .andExpect(status().isNoContent());
    }

}
