package br.senac.tads.dsw.exemplosspring.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.senac.tads.dsw.exemplosspring.item.domain.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

	@Override
	@EntityGraph(type = EntityGraphType.LOAD, value = "graph.PedidoEnderecoItens")
	List<Pedido> findAll();

}
