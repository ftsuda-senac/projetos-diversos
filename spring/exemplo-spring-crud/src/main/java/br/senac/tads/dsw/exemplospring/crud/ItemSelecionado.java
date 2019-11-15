/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplospring.crud;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.senac.tads.dsw.exemplospring.crud.item.Item;

/**
 *
 * @author ftsuda
 */
public class ItemSelecionado implements Serializable {

	private static final long serialVersionUID = 1L;

	private Item item;

	private int quantidade;

	private BigDecimal precoFinal;

	private LocalDateTime dataHoraInclusao;

	public ItemSelecionado() {

	}

	public ItemSelecionado(Item item, int quantidade) {
		this.item = item;
		this.quantidade = quantidade;
		this.dataHoraInclusao = LocalDateTime.now();
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPrecoFinal() {
		return precoFinal;
	}

	public void setPrecoFinal(BigDecimal precoFinal) {
		this.precoFinal = precoFinal;
	}

	public LocalDateTime getDataHoraInclusao() {
		return dataHoraInclusao;
	}

	public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
		this.dataHoraInclusao = dataHoraInclusao;
	}

	public int getSubtotal() {
		return item.getValor() * quantidade;
	}
}
