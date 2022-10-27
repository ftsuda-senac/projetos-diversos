package br.senac.tads.dsw.exemplosspring.item.dominio.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.senac.tads.dsw.exemplosspring.item.dominio.entidade.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
