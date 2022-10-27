package br.senac.tads.dsw.exemplosspring.item.dominio.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.senac.tads.dsw.exemplosspring.item.dominio.entidade.PedidoItem;
import br.senac.tads.dsw.exemplosspring.item.dominio.entidade.PedidoItemId;

@Repository
public interface PedidoItemRepository extends JpaRepository<PedidoItem, PedidoItemId> {

    void deleteById_IdPedido(Long idPedido);

}
