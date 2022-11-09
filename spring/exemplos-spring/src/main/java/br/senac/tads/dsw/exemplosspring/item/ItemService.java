package br.senac.tads.dsw.exemplosspring.item;

import java.util.List;

public interface ItemService {

    List<Item> findAll();

    Item findById(Integer id);

    Item save(Item item);

    void deleteById(Integer id);

}
