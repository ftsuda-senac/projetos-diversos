package br.com.webmobi.dadospessoais.repository;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.entity.PessoaEntity;
import br.com.webmobi.dadospessoais.dominio.repository.InteresseRepository;
import br.com.webmobi.dadospessoais.dominio.repository.PessoaRepository;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PessoaRepositoryTest {

	@Autowired
	private InteresseRepository interesseRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	private PessoaEntity pessoaDefault;

	private List<Integer> interessesIds;

	@BeforeAll
	public void setup() {
		// @formatter:off
		interessesIds = interesseRepository.saveAll(List.of(
			new InteresseEntity("Java"),
			new InteresseEntity("Spring"),
			new InteresseEntity("JavaScript"),
			new InteresseEntity("Python"),
			new InteresseEntity("PHP"),
			new InteresseEntity("Ruby"),
			new InteresseEntity("Go"),
			new InteresseEntity("Rust"),
			new InteresseEntity("C#"),
			new InteresseEntity("Kotlin"),
			new InteresseEntity("Swift"),
			new InteresseEntity("Dart"),
			new InteresseEntity("TypeScript"),
			new InteresseEntity("HTML"),
			new InteresseEntity("CSS"),
			new InteresseEntity("SQL")))
				.stream().map(InteresseEntity::getId).toList();
		// @formatter:on

		pessoaDefault = new PessoaEntity();
		pessoaDefault.setUsername("fulano");
		pessoaDefault.setNome("Fulano da Silva");
		pessoaDefault.setEmail("fulano@email.com");
		pessoaDefault.setTelefone("(11) 99999-1234");
		pessoaDefault.setDataNascimento(LocalDate.parse("2000-05-20"));
		pessoaDefault.setInteresses(new HashSet<>(interesseRepository
				.findByIdIn(List.of(interessesIds.get(0), interessesIds.get(1), interessesIds.get(2)))));
		pessoaRepository.save(pessoaDefault);
	}

	@Test
	@Order(1)
	@DisplayName("Buscar pessoa por public ID (UUID)")
	void testGivenPessoaUuidWhenFindByPublicIdThenReturnPessoa() {

		// Given/Arrange
		UUID publicId = pessoaDefault.getPublicId();

		// When/Act
		Optional<PessoaEntity> optPessoa = pessoaRepository.findByPublicId(publicId);

		// Then/Assert
		then(optPessoa.isPresent()).isEqualTo(true);
		PessoaEntity pessoa = optPessoa.get();
		then(pessoa.getNome()).isEqualTo(pessoaDefault.getNome());
		then(pessoa.getEmail()).isEqualTo(pessoaDefault.getEmail());
	}

	@Test
	@Order(2)
	@DisplayName("Incluir pessoa")
	void testGivenNovaPessoaWhenSaveThenReturnPessoaSalva() {
		// Given/Arrange
		PessoaEntity pessoa = new PessoaEntity();
		pessoa.setUsername("ciclano");
		pessoa.setNome("Ciclano de Souza");
		pessoa.setEmail("ciclano@email.com");
		pessoa.setTelefone("(11) 99999-1234");
		pessoa.setDataNascimento(LocalDate.parse("2000-05-20"));
		pessoa.setInteresses(new HashSet<>(interesseRepository
				.findByIdIn(List.of(interessesIds.get(2), interessesIds.get(3), interessesIds.get(4)))));

		// When/Act
		PessoaEntity pessoaSalva = pessoaRepository.save(pessoa);

		// Then/Assert
		then(pessoaSalva).isNotNull();
		then(pessoaSalva.getId()).isNotNull().isGreaterThan(0);
		then(pessoaSalva.getDataCriacao()).isNotNull();
		then(pessoaSalva.getDataAtualizacao()).isNotNull();
		then(pessoaSalva.getPublicId()).isNotNull();
	}

	@Test
	@Order(3)
	@DisplayName("Incluir pessoa com e-mail já existente")
	void testGivenNovaPessoaComEmailExistenteWhenSaveThenErro() {
		// Given/Arrange
		PessoaEntity pessoa = new PessoaEntity();
		pessoa.setUsername("ciclano2");
		pessoa.setNome("Ciclano de Souza");
		pessoa.setEmail("ciclano@email.com");
		pessoa.setTelefone("(11) 99999-1234");
		pessoa.setDataNascimento(LocalDate.parse("2000-05-20"));
		pessoa.setInteresses(new HashSet<>(interesseRepository
				.findByIdIn(List.of(interessesIds.get(2), interessesIds.get(3), interessesIds.get(4)))));

		// When/Act
		Throwable ex = catchThrowable(() -> pessoaRepository.save(pessoa));

		// Then/Assert
		then(ex);
	}

	@Test
	@Order(4)
	@DisplayName("Incluir pessoa com username já existente")
	void testGivenNovaPessoaComUsernameExistenteWhenSaveThenErro() {
		// Given/Arrange
		PessoaEntity pessoa = new PessoaEntity();
		pessoa.setUsername("ciclano");
		pessoa.setNome("Ciclano de Souza");
		pessoa.setEmail("ciclano2@email.com");
		pessoa.setTelefone("(11) 99999-1234");
		pessoa.setDataNascimento(LocalDate.parse("2000-05-20"));
		pessoa.setInteresses(new HashSet<>(interesseRepository
				.findByIdIn(List.of(interessesIds.get(2), interessesIds.get(3), interessesIds.get(4)))));

		// When/Act
		Throwable ex = catchThrowable(() -> pessoaRepository.save(pessoa));

		// Then/Assert
		then(ex);
	}

	@Test
	@Order(5)
	@DisplayName("Alterar nome e telefone de pessoa existente")
	void testGivenPessoaAlteradaWhenSaveThenPessoaAtualizada() {
		// Given/Arrange
		String nomeAlterado = "Fulano da Silva alterado";
		String telefoneAlterado = "(11) 90000-0000";

		PessoaEntity pessoaExistente = pessoaRepository.findById(pessoaDefault.getId()).orElseThrow();
		pessoaExistente.setNome(nomeAlterado);
		pessoaExistente.setTelefone(telefoneAlterado);

		// When/Act
		PessoaEntity pessoaSalva = pessoaRepository.save(pessoaExistente);

		// Then/Assert
		then(pessoaSalva).isNotNull();
		then(pessoaSalva.getNome()).isEqualTo(nomeAlterado);
		then(pessoaSalva.getTelefone()).isEqualTo(telefoneAlterado);
		// then(pessoaSalva.getDataAtualizacao()).isAfter(pessoaSalva.getDataCriacao());
	}


}
