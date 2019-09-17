package br.senac.tads.dsw.exemplolocal;

import java.util.List;

public interface CategoriaService {
	
	public List<Categoria> findAll();
	
	public Categoria findById(int id);

}
