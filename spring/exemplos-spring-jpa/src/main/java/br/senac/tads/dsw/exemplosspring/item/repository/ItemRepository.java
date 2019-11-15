package br.senac.tads.dsw.exemplosspring.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.senac.tads.dsw.exemplosspring.item.domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
