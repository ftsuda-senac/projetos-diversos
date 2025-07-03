package br.com.webmobi.dadospessoais.dominio.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Pessoa")
@Table(name = "tb_pessoa", uniqueConstraints = {
		@UniqueConstraint(name = "uk_pessoa_username", columnNames = { "username" }),
		@UniqueConstraint(name = "uk_pessoa_public_id", columnNames = { "public_id" }) })
public class PessoaEntity {

	@Id
	@SequenceGenerator(name = "seq_pessoa_id", allocationSize = 1, initialValue = 101)
	@GeneratedValue(generator = "seq_pessoa_id")
	private Integer id;

	@Column(updatable = false, nullable = false, columnDefinition = "text")
	private String username;

	@Column(name = "public_id", updatable = false, nullable = false, columnDefinition = "text")
	private UUID publicId;

	@Column(nullable = false, columnDefinition = "text")
	private String nome;

	@Column(nullable = false, columnDefinition = "text")
	private String email;

	@Column(columnDefinition = "text")
	private String telefone;

	@Column(columnDefinition = "text")
	private String hashSenha;

	private LocalDate dataNascimento;

	private Instant dataCriacao;

	private Instant dataAtualizacao;

	@OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
	private Set<PessoaFotoEntity> fotos;

	@ManyToMany
	@JoinTable(name = "tb_pessoa_interesse", joinColumns = @JoinColumn(name = "pessoa_id", foreignKey = @ForeignKey(name = "fk_pessoa_interesse__pessoa_id")), inverseJoinColumns = @JoinColumn(name = "interesse_id"), foreignKey = @ForeignKey(name = "fk_pessoa_interesse__interesse_id"))
	private Set<InteresseEntity> interesses;

	@PrePersist
	public void onCreate() {
		this.publicId = UUID.randomUUID();
		this.dataCriacao = this.dataAtualizacao = Instant.now();
	}

	@PreUpdate
	public void onUpdate() {
		this.dataAtualizacao = Instant.now();
	}

}
