package br.senac.tads.dsw.exemplosspring.item.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Item implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String nome;

	@Column(precision = 9, scale = 2)
	private BigDecimal preco;

	@OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
	private Set<PedidoItem> itens;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Set<PedidoItem> getItens() {
		return itens;
	}

	public void setItens(Set<PedidoItem> itens) {
		this.itens = itens;
	}

}
