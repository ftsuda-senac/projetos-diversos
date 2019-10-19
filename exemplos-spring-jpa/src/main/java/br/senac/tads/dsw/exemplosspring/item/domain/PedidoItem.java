package br.senac.tads.dsw.exemplosspring.item.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class PedidoItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PedidoItemId id;

	@Column
	private int quantidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idPedido")
	@JoinColumn(name = "id_pedido", foreignKey = @ForeignKey(name = "fk_pedido"))
	private Pedido pedido;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idItem")
	@JoinColumn(name = "id_item", foreignKey = @ForeignKey(name = "fk_item"))
	private Item item;

	public PedidoItemId getId() {
		return id;
	}

	public void setId(PedidoItemId id) {
		this.id = id;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
		if (id == null) {
			id = new PedidoItemId();
		}
		id.setIdPedido(pedido.getId());
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
		if (id == null) {
			id = new PedidoItemId();
		}
		id.setIdItem(item.getId());
	}
	
	

}
