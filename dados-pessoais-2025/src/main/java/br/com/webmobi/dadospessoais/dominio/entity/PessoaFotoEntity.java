package br.com.webmobi.dadospessoais.dominio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "PessoaFoto")
@Table(name = "tb_pessoa_foto", uniqueConstraints = @UniqueConstraint(name = "uk_pessoa_foto__pessoa_id_nome_arquivo", columnNames = {
		"pessoa_id", "nome_arquivo" }))
public class PessoaFotoEntity {

	@Id
	@SequenceGenerator(name = "seq_pessoa_foto_id", allocationSize = 1, initialValue = 101)
	@GeneratedValue(generator = "seq_pessoa_foto_id")
	private Integer id;

	@Column(name = "nome_arquivo", nullable = false, columnDefinition = "text")
	private String nomeArquivo;

	@Column(nullable = false, columnDefinition = "text")
	private String nomeArquivoOriginal;

	@Column(columnDefinition = "text")
	private String legenda;

	@ManyToOne
	@JoinColumn(name = "pessoa_id", foreignKey = @ForeignKey(name = "fk_pessoa_foto__pessoa_id"))
	private PessoaEntity pessoa;

}
