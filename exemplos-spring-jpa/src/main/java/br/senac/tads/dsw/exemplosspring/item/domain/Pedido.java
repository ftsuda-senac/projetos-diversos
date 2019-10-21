package br.senac.tads.dsw.exemplosspring.item.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

@Entity
public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String codigo;

	@Column
	private LocalDateTime dataHoraFechamento;

	@OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY)
	private Set<PedidoItem> itens;

	@OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	private Endereco enderecoEntrega;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public LocalDateTime getDataHoraFechamento() {
		return dataHoraFechamento;
	}

	public void setDataHoraFechamento(LocalDateTime dataHoraFechamento) {
		this.dataHoraFechamento = dataHoraFechamento;
	}

	public Set<PedidoItem> getItens() {
		return itens;
	}

	public void setItens(Set<PedidoItem> itens) {
		this.itens = itens;
	}

	public Endereco getEnderecoEntrega() {
		return enderecoEntrega;
	}

	public void setEnderecoEntrega(Endereco enderecoEntrega) {
		this.enderecoEntrega = enderecoEntrega;
	}

	@PrePersist
	public void prePersist() {
		this.dataHoraFechamento = LocalDateTime.now();
		// Gera um código baseado no timestamp;
		this.codigo = dataHoraFechamento.format(DateTimeFormatter.ofPattern("yyMM")) + "-"
				+ dataHoraFechamento.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

}
