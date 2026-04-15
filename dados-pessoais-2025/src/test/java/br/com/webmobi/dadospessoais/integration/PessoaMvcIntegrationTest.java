package br.com.webmobi.dadospessoais.integration;

import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;
import br.com.webmobi.dadospessoais.dominio.repository.InteresseRepository;
import br.com.webmobi.dadospessoais.dominio.repository.PessoaRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class PessoaMvcIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private InteresseRepository interesseRepository;

    /** Diretório temporário para armazenar fotos durante os testes. */
    private static final Path UPLOAD_TEMP_DIR;
    static {
        try {
            UPLOAD_TEMP_DIR = Files.createTempDirectory("dados-pessoais-uploads-test");
        } catch (IOException e) {
            throw new RuntimeException("Falha ao criar diretório temporário de upload", e);
        }
    }

    @DynamicPropertySource
    static void configurarUploadPath(DynamicPropertyRegistry registry) {
        registry.add("app.upload-path", () -> UPLOAD_TEMP_DIR.toAbsolutePath() + "/");
    }

    /** publicId da pessoa de semente — usada nos testes de leitura e upload de foto. */
    private UUID pessoaPublicId;

    /** IDs dos interesses disponíveis — usados nos formulários de inclusão e alteração. */
    private List<Integer> interessesIds;

    /**
     * publicId da pessoa criada no teste de inclusão — passada entre testes de
     * alteração e exclusão.
     */
    private UUID pessoaTestId;

    /** Nome do arquivo de foto salvo — usado no teste de exclusão de foto. */
    private String nomeArquivoFoto;

    @BeforeAll
    public void setup() {
        interessesIds = interesseRepository
                .saveAllAndFlush(List.of(
                        new InteresseEntity("Programação"),
                        new InteresseEntity("Cloud")))
                .stream()
                .map(InteresseEntity::getId)
                .toList();

        PessoaEntity seed = new PessoaEntity();
        seed.setUsername("mvcseed1");
        seed.setNome("MVC Seed Um");
        seed.setEmail("mvcseed1@email.com");
        seed.setTelefone("(11) 99999-0001");
        seed.setDataNascimento(LocalDate.parse("1995-03-15"));
        seed.setInteresses(new HashSet<>());

        pessoaPublicId = pessoaRepository.save(seed).getPublicId();
    }

    // -------------------------------------------------------------------------
    // GET /mvc/pessoas  —  listagem paginada
    // -------------------------------------------------------------------------

    @Test
    @Order(1)
    void testGivenPessoasSalvasWhenListarThenRenderizarViewListaComDados() throws Exception {

        // When/Act + Then/Assert
        mockMvc.perform(get("/mvc/pessoas").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("pessoas/lista"))
                .andExpect(model().attributeExists("itens"))
                .andExpect(content().string(containsString("mvcseed1")))
                .andExpect(content().string(containsString("MVC Seed Um")));
    }

    // -------------------------------------------------------------------------
    // GET /mvc/pessoas/incluir  —  formulário de inclusão
    // -------------------------------------------------------------------------

    @Test
    @Order(2)
    void testWhenIncluirFormThenRenderizarViewFormComInteresses() throws Exception {

        // When/Act + Then/Assert
        mockMvc.perform(get("/mvc/pessoas/incluir").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("pessoas/form"))
                .andExpect(model().attributeExists("item"))
                .andExpect(model().attributeExists("opcoesInteresses"))
                .andExpect(content().string(containsString("Incluir nova pessoa")))
                .andExpect(content().string(containsString("Programação")))
                .andExpect(content().string(containsString("Cloud")));
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/incluir  —  inclusão válida
    // -------------------------------------------------------------------------

    @Test
    @Order(3)
    void testGivenFormValidoWhenIncluirNovoThenRedirecionarEPersistirNoBanco() throws Exception {

        String username = "mvcinttest";

        // When/Act
        mockMvc.perform(post("/mvc/pessoas/incluir")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", username)
                        .param("nome", "MVC Integration Test")
                        .param("email", "mvcinttest@email.com")
                        .param("telefone", "(11) 98888-0001")
                        .param("dataNascimento", "1990-06-15")
                        .param("senha", "Abcd%1234")
                        .param("senhaConfirmacao", "Abcd%1234")
                        .param("interessesIds", String.valueOf(interessesIds.get(0))))
                .andExpect(status().is3xxRedirection())
                .andDo(print())
                .andExpect(redirectedUrl("/mvc/pessoas"))
                .andExpect(flash().attributeExists("msg"));

        // Then/Assert — verificar persistência no banco
        then(pessoaRepository.existsByUsername(username)).isTrue();
        pessoaTestId = pessoaRepository.findByUsername(username)
                .map(PessoaEntity::getPublicId)
                .orElseThrow(() -> new AssertionError("Pessoa não encontrada no banco após inclusão"));
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/incluir  —  falha de validação no serviço
    // -------------------------------------------------------------------------

    @Test
    @Order(4)
    void testGivenNomeEmBrancoWhenIncluirNovoThenRenderizarFormComErros() throws Exception {

        // When/Act + Then/Assert
        mockMvc.perform(post("/mvc/pessoas/incluir")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "mvcerrortest")
                        .param("nome", "")                         // @NotBlank viola constraint
                        .param("email", "mvcerror@email.com")
                        .param("senha", "Abcd%1234")
                        .param("senhaConfirmacao", "Abcd%1234")
                        .param("interessesIds", String.valueOf(interessesIds.get(0))))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("pessoas/form"))
                .andExpect(model().attributeExists("opcoesInteresses"));
    }

    // -------------------------------------------------------------------------
    // GET /mvc/pessoas/{id}/alterar  —  formulário de alteração
    // -------------------------------------------------------------------------

    @Test
    @Order(5)
    void testGivenIdExistenteWhenAlterarFormThenRenderizarViewFormComDadosDaPessoa() throws Exception {

        // When/Act + Then/Assert
        mockMvc.perform(get("/mvc/pessoas/{id}/alterar", pessoaPublicId).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("pessoas/form"))
                .andExpect(model().attributeExists("item"))
                .andExpect(model().attributeExists("opcoesInteresses"))
                .andExpect(content().string(containsString("Alterar pessoa")))
                .andExpect(content().string(containsString("mvcseed1")));
    }

    @Test
    @Order(6)
    void testGivenIdInexistenteWhenAlterarFormThenRetornar404() throws Exception {

        // When/Then
        mockMvc.perform(get("/mvc/pessoas/{id}/alterar", UUID.randomUUID()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/{id}/alterar  —  alteração válida
    // -------------------------------------------------------------------------

    @Test
    @Order(7)
    void testGivenFormAlteracaoValidoWhenAlterarThenRedirecionarEAtualizarNoBanco() throws Exception {

        // When/Act
        mockMvc.perform(post("/mvc/pessoas/{id}/alterar", pessoaTestId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", "MVC Integration Alterado")
                        .param("email", "alterado@email.com")
                        .param("telefone", "(11) 97777-9999")
                        .param("dataNascimento", "1992-03-10")
                        .param("interessesIds", String.valueOf(interessesIds.get(0)),
                                               String.valueOf(interessesIds.get(1))))
                .andExpect(status().is3xxRedirection())
                .andDo(print())
                .andExpect(redirectedUrl("/mvc/pessoas"))
                .andExpect(flash().attributeExists("msg"));

        // Then/Assert — verificar atualização no banco
        PessoaEntity atualizada = pessoaRepository.findByPublicId(pessoaTestId)
                .orElseThrow(() -> new AssertionError("Pessoa não encontrada no banco após alteração"));
        then(atualizada.getNome()).isEqualTo("MVC Integration Alterado");
        then(atualizada.getEmail()).isEqualTo("alterado@email.com");
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/{id}/alterar  —  falha de validação no serviço
    // -------------------------------------------------------------------------

    @Test
    @Order(8)
    void testGivenNomeEmBrancoWhenAlterarThenRenderizarFormComErros() throws Exception {

        // When/Act + Then/Assert
        mockMvc.perform(post("/mvc/pessoas/{id}/alterar", pessoaTestId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", "")                         // @NotBlank viola constraint
                        .param("email", "valido@email.com")
                        .param("interessesIds", String.valueOf(interessesIds.get(0))))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("pessoas/form"))
                .andExpect(model().attributeExists("opcoesInteresses"));
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/{id}/alterar  —  ID não encontrado
    // -------------------------------------------------------------------------

    @Test
    @Order(9)
    void testGivenIdInexistenteWhenAlterarThenRetornar404() throws Exception {

        // When/Then
        mockMvc.perform(post("/mvc/pessoas/{id}/alterar", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", "Qualquer")
                        .param("email", "qualquer@email.com")
                        .param("interessesIds", String.valueOf(interessesIds.get(0))))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/{pessoaId}/fotos  —  upload de foto
    // -------------------------------------------------------------------------

    @Test
    @Order(10)
    void testGivenArquivoValidoWhenIncluirFotoThenRetornar201ComLocationEPersistir() throws Exception {

        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo", "foto-teste.jpg", MediaType.IMAGE_JPEG_VALUE,
                "fake-jpeg-content".getBytes());

        // When/Act
        MvcResult result = mockMvc.perform(
                        multipart("/mvc/pessoas/{pessoaId}/fotos", pessoaPublicId)
                                .file(arquivo)
                                .param("legenda", "Foto de integração"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(header().exists("Location"))
                .andReturn();

        // Then/Assert — extrair nome do arquivo da URL retornada no Location
        String location = result.getResponse().getHeader("Location");
        nomeArquivoFoto = URI.create(location).getPath();
        nomeArquivoFoto = nomeArquivoFoto.substring(nomeArquivoFoto.lastIndexOf('/') + 1);

        then(nomeArquivoFoto).isNotBlank();
        then(nomeArquivoFoto).endsWith(".jpg");
    }

    // -------------------------------------------------------------------------
    // DELETE /mvc/pessoas/{pessoaId}/fotos/{nomeArquivo}  —  exclusão de foto
    // -------------------------------------------------------------------------

    @Test
    @Order(11)
    void testGivenNomeFotoExistenteWhenExcluirFotoThenRetornar204ERemoverArquivo() throws Exception {

        // When/Then
        mockMvc.perform(
                        delete("/mvc/pessoas/{pessoaId}/fotos/{nomeArquivo}",
                                pessoaPublicId, nomeArquivoFoto))
                .andExpect(status().isNoContent())
                .andDo(print());

        // Assert — arquivo removido do disco
        Path arquivoEsperado = UPLOAD_TEMP_DIR
                .resolve(pessoaPublicId.toString())
                .resolve("fotos")
                .resolve(nomeArquivoFoto);
        then(Files.exists(arquivoEsperado)).isFalse();
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/{id}/excluir  —  exclusão válida
    // -------------------------------------------------------------------------

    @Test
    @Order(12)
    void testGivenIdExistenteWhenExcluirThenRedirecionarERemoverDoBanco() throws Exception {

        // When/Act
        mockMvc.perform(post("/mvc/pessoas/{id}/excluir", pessoaTestId))
                .andExpect(status().is3xxRedirection())
                .andDo(print())
                .andExpect(redirectedUrl("/mvc/pessoas"))
                .andExpect(flash().attributeExists("msg"));

        // Then/Assert — verificar remoção no banco
        then(pessoaRepository.existsByPublicId(pessoaTestId)).isFalse();
    }

    // -------------------------------------------------------------------------
    // POST /mvc/pessoas/{id}/excluir  —  ID não encontrado
    // -------------------------------------------------------------------------

    @Test
    @Order(13)
    void testGivenIdInexistenteWhenExcluirThenRetornar404() throws Exception {

        // When/Then
        mockMvc.perform(post("/mvc/pessoas/{id}/excluir", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

}
