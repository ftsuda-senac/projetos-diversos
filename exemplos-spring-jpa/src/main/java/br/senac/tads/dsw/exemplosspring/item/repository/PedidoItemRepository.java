package br.senac.tads.dsw.exemplosspring.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.senac.tads.dsw.exemplosspring.item.domain.PedidoItem;
import br.senac.tads.dsw.exemplosspring.item.domain.PedidoItemId;

@Repository
public interface PedidoItemRepository extends JpaRepository<PedidoItem, PedidoItemId>{

}
