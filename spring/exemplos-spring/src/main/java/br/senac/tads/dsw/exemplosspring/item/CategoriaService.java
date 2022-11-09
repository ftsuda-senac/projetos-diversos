package br.senac.tads.dsw.exemplosspring.item;

import java.util.List;

public interface CategoriaService {

    List<Categoria> findAll();

    Categoria findById(Integer id);

}
