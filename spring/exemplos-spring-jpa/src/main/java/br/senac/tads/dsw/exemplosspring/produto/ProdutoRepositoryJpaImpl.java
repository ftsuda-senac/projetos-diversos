package br.senac.tads.dsw.exemplosspring.produto;

import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
public class ProdutoRepositoryJpaImpl implements ProdutoRepository {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Produto> findAll(int offset, int quantidade) {
		Query jpqlQuery = em.createNamedQuery("Produto.findAll").setFirstResult(offset)
				.setMaxResults(quantidade);
		return jpqlQuery.getResultList();
	}

	// Exemplos Criteria API: https://www.objectdb.com/java/jpa/query/criteria#Parameters_in_Criteria_Queries
	public List<Produto> findAllCriteria(int offset, int quantidade) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Produto> criteriaQuery = cb.createQuery(Produto.class);
		criteriaQuery.from(Produto.class);
		TypedQuery<Produto> query = em.createQuery(criteriaQuery)
				.setFirstResult(offset).setMaxResults(quantidade);
		return query.getResultList();
	}

	@Override
	public List<Produto> findByCategoria(List<Integer> idsCat, int offset, int quantidade) {
		Query jpqlQuery = em.createNamedQuery("Produto.findAllByCategorias_Id").setParameter("idCat", idsCat);
		return jpqlQuery.getResultList();
	}
	
	public List<Produto> findByCategoriaCriteria(List<Integer> idsCat, int offset, int quantidade) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Produto> criteriaQuery = cb.createQuery(Produto.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);
		Expression<Integer> categoriaExpression = root.get("Produto.categorias");
		Predicate categoriaPredicate = categoriaExpression.in(idsCat);
		criteriaQuery.where(categoriaPredicate);
		TypedQuery<Produto> query = em.createQuery(criteriaQuery)
				.setFirstResult(offset).setMaxResults(quantidade);
		return query.getResultList();
	}

	@Override
	public Produto findById(Long id) {
		Query jpqlQuery = em.createNamedQuery("Produto.findById").setParameter("idProd", id);
		Produto p = (Produto) jpqlQuery.getSingleResult();
		return p;
	}
	
	public Produto findByIdCriteria(Long id) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Produto> criteriaQuery = cb.createQuery(Produto.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);
		criteriaQuery.where(cb.equal(root.get("id"), id));
		TypedQuery<Produto> query = em.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	// Exemplos entityGraph
	// - https://www.baeldung.com/jpa-entity-graph
	// - https://thoughts-on-java.org/jpa-21-entity-graph-part-2-define/
	// - https://docs.oracle.com/javaee/7/tutorial/persistence-entitygraphs001.htm
	// javax.persistence.fetchgraph --> Campos que serão retornados devem estar explicitos
	// javax.persistence.loadgraph --> Campos que serão retornados são adicionais aos campos padrao definidos no modelo
	public Produto findByIdEntityGraph(Long id) {
		EntityGraph<Produto> entityGraph = em.createEntityGraph(Produto.class);
		entityGraph.addAttributeNodes("categorias", "imagens");

		Query jpqlQuery = em.createQuery("SELECT p FROM Produto p WHERE p.id = :idProd")
				.setParameter("idProd", id)
				.setHint("javax.persistence.loadgraph", entityGraph);
		return (Produto) jpqlQuery.getSingleResult();
	}

	// https://thoughts-on-java.org/jpa-21-entity-graph-part-1-named-entity/
	public Produto findByIdNamedEntityGraph(Long id) {
		EntityGraph<?> namedEntityGraph = em.getEntityGraph("graph.ProdutoCategoriasImagens");
		 
		Query jpqlQuery = em.createNamedQuery("Produto.findById")
				.setParameter("idProd", id)
				.setHint("javax.persistence.loadgraph", namedEntityGraph);
		return (Produto) jpqlQuery.getSingleResult();
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
