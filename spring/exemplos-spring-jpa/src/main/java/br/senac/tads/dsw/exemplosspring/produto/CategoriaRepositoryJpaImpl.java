package br.senac.tads.dsw.exemplosspring.produto;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
public class CategoriaRepositoryJpaImpl implements CategoriaRepository {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Categoria> findAll() {
		Query jpqlQuery = em.createQuery("SELECT c FROM Categoria c");
		return jpqlQuery.getResultList();
	}

	@Override
	public Categoria findById(Integer id) {
		Query jpqlQuery = em.createQuery("SELECT c FROM Categoria c WHERE c.id = :idCat");
		jpqlQuery.setParameter("idCat", id);
		Categoria cat = (Categoria) jpqlQuery.getSingleResult();
		return cat;
	}

	@Transactional
	@Override
	public Categoria save(Categoria cat) {
		if (cat.getId() == null) {
			// Incluindo nova categoria
			em.persist(cat);
		} else {
			// Atualiza categoria existente
			cat = em.merge(cat);
		}
		return cat;
	}

}
