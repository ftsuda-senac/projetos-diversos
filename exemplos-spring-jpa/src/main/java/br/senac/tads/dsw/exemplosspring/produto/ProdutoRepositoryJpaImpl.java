package br.senac.tads.dsw.exemplosspring.produto;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

public class ProdutoRepositoryJpaImpl implements ProdutoRepository {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Produto> findAll(int offset, int quantidade) {
		Query jpqlQuery = em.createNamedQuery("Produto.findAll").setFirstResult(offset)
				.setMaxResults(quantidade);
		return jpqlQuery.getResultList();
	}

	@Override
	public List<Produto> findByCategoria(List<Integer> idsCat, int offset, int quantidade) {
		Query jpqlQuery = em.createNamedQuery("Produto.findByCategoria").setParameter("idCat", idsCat);
		return jpqlQuery.getResultList();
	}

	@Override
	public Produto findById(Long id) {
		Query jpqlQuery = em.createNamedQuery("Produto.findById").setParameter("idProd", id);
		Produto p = (Produto) jpqlQuery.getSingleResult();
		return p;
	}

	@Override
	@Transactional
	public Produto save(Produto prod) {
		if (prod.getId() == null) {
			// Salva um novo produto
			em.persist(prod);
		} else {
			// Atualiza um produto existente
			prod = em.merge(prod);
		}
		return prod;
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		// TEM QUE FAZER CONSULTA PARA ESTAR ASSOCIADO AO
		// ENTITY MANAGER (ATTACHED)
		Produto prod = em.find(Produto.class, id);
		em.remove(prod);
	}

}
