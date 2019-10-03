package br.senac.tads.dsw.exemplolocal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CategoriaServiceMock implements CategoriaService {
	
	private static final Map<Integer, Categoria>  MOCK_DATA = new ConcurrentHashMap<Integer, Categoria>();
	
	static {
		Categoria c = new Categoria(1, "Comida");
		MOCK_DATA.put(c.getId(), c);
		
		c = new Categoria(2, "Bebida");
		MOCK_DATA.put(c.getId(), c);
		
		c = new Categoria(3, "Higiene pessoal");
		MOCK_DATA.put(c.getId(), c);
		
		c = new Categoria(4, "Alcólico");
		MOCK_DATA.put(c.getId(), c);
		
		c = new Categoria(5, "Doce");
		MOCK_DATA.put(c.getId(), c);
		
		c = new Categoria(6, "Salgado");
		MOCK_DATA.put(c.getId(), c);
	}

	@Override
	public List<Categoria> findAll() {
		return new ArrayList<>(MOCK_DATA.values());
	}

	@Override
	public Categoria findById(int id) {
		return MOCK_DATA.get(id);
	}

}
