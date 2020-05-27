package br.senac.tads.dsw.exemplorest.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String nome;

	@Column
	private int sexo; // 0-feminino, 1-masculino

	@Column
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dataNascimento;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "pessoa_interesse",
		joinColumns = @JoinColumn(name = "pessoa_id"),
		inverseJoinColumns = @JoinColumn(name = "interesse_id"))
	private Set<Interesse> interesses;

	// Mantem temporariamente os IDs dos interesses selecionados ao salvar.
	@Transient
	private transient List<Integer> interessesId = new ArrayList<>();

	public Pessoa() {

	}

	public Pessoa(String nome, int sexo, LocalDate dataNascimento, Set<Interesse> interesses) {
		this.nome = nome;
		this.sexo = sexo;
		this.dataNascimento = dataNascimento;
		this.interesses = interesses;
	}

	public Pessoa(Integer id, String nome, int sexo, LocalDate dataNascimento, Set<Interesse> interesses) {
		this.id = id;
		this.nome = nome;
		this.sexo = sexo;
		this.dataNascimento = dataNascimento;
		this.interesses = interesses;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getSexo() {
		return sexo;
	}

	public void setSexo(int sexo) {
		this.sexo = sexo;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Set<Interesse> getInteresses() {
		return interesses;
	}

	public void setInteresses(Set<Interesse> interesses) {
		this.interesses = interesses;
	}

	public List<Integer> getInteressesId() {
		return interessesId;
	}

	public void setInteressesId(List<Integer> interessesId) {
		this.interessesId = interessesId;
	}
	
	// ***** LISTENERS DO JPA *****
	// PERMITEM EXECUTAR ACOES ANTES E APOS SALVAR/OBTER DADOS DO BANCO

	// Configura a lista de IDs dos interesses selecionados após carregar dados da pessoa
	@PostLoad
	public void prepararIdsSelecionados() {
		for (Interesse interesse : interesses) {
			interessesId.add(interesse.getId());
		}
	}

}
