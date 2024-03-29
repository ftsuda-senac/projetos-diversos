package br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio;

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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Produto;
import java.util.Optional;
import javax.persistence.NoResultException;

@Repository
public class ProdutoRepositoryJpaImpl implements ProdutoRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Produto> findAll(int pagina, int quantidade) {
        // calculando offset
        int offset = pagina * quantidade;

//        TypedQuery<Produto> jpqlQuery =
//                em.createQuery("SELECT p FROM Produto p", Produto.class)
//                        .setFirstResult(offset)
//                        .setMaxResults(quantidade);
        TypedQuery<Produto> jpqlQuery = em.createNamedQuery("Produto.findAll", Produto.class)
                .setFirstResult(offset).setMaxResults(quantidade);
        return jpqlQuery.getResultList();
    }

    // Exemplos Criteria API:
    // https://www.objectdb.com/java/jpa/query/criteria#Parameters_in_Criteria_Queries
    public List<Produto> findAllCriteria(int pagina, int quantidade) {
        // calculando offset
        int offset = pagina * quantidade;

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = cb.createQuery(Produto.class);
        criteriaQuery.from(Produto.class);

        TypedQuery<Produto> query = em.createQuery(criteriaQuery)
                .setFirstResult(offset).setMaxResults(quantidade);
        return query.getResultList();
    }

    @Override
    public List<Produto> findByCategoria(List<Integer> idsCat, int pagina, int quantidade) {
        // calculando offset
        int offset = pagina * quantidade;
        TypedQuery<Produto> jpqlQuery
                = em.createNamedQuery("Produto.findAllByCategorias_Id", Produto.class)
                        .setParameter("idsCat", idsCat);
        return jpqlQuery.getResultList();
    }

    public List<Produto> findByCategoriaCriteria(List<Integer> idsCat, int pagina, int quantidade) {
        // calculando offset
        int offset = pagina * quantidade;

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = cb.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);
        Expression<Integer> categoriaExpression = root.get("Produto.categorias");
        Predicate categoriaPredicate = categoriaExpression.in(idsCat);
        criteriaQuery.where(categoriaPredicate);

        TypedQuery<Produto> query
                = em.createQuery(criteriaQuery).setFirstResult(offset).setMaxResults(quantidade);
        return query.getResultList();
    }

    // OBS: FUNÇÃO PADRÃO QUE SÓ FUNCIONA COM open-in-view=TRUE NO ARQUIVO application.properties
    // SE CONFIGURAR open-in-view=false NO ARQUIVO application.properties, IRÁ OCORRER UM ERRO 
    @Override
    public Optional<Produto> findById(Integer id) {
        TypedQuery<Produto> jpqlQuery
                = em.createNamedQuery("Produto.findById", Produto.class);
        jpqlQuery.setParameter("idProd", id);
        try {
            Produto p = jpqlQuery.getSingleResult();
            return Optional.of(p);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    // OBS: PARA TESTAR, CONFIGURAR open-in-view=false NO ARQUIVO application.properties
    public Produto findByIdComJoinFetch(Integer id) {
        TypedQuery<Produto> jpqlQuery
                = em.createNamedQuery("Produto.findByIdComJoinFetch", Produto.class)
                        .setParameter("idProd", id);
        Produto prod = jpqlQuery.getSingleResult();
        return prod;
    }

    public Produto findByIdCriteria(Integer id) {
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
    // javax.persistence.loadgraph --> Campos que serão retornados são adicionais aos campos padrao
    // definidos no modelo
    // OBS: PARA TESTAR, CONFIGURAR open-in-view=false NO ARQUIVO application.properties
    public Produto findByIdEntityGraph(Integer id) {
        EntityGraph<Produto> entityGraph = em.createEntityGraph(Produto.class);
        entityGraph.addAttributeNodes("categorias", "imagens");

        Query jpqlQuery = em.createQuery("SELECT p FROM Produto p WHERE p.id = :idProd")
                .setParameter("idProd", id)
                .setHint("javax.persistence.loadgraph", entityGraph);
        return (Produto) jpqlQuery.getSingleResult();
    }

    // https://thoughts-on-java.org/jpa-21-entity-graph-part-1-named-entity/
    // OBS: PARA TESTAR, CONFIGURAR open-in-view=false NO ARQUIVO application.properties
    public Produto findByIdNamedEntityGraph(Integer id) {
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
            // Incluir novo produto
            em.persist(prod);
        } else {
            // Atualizar um produto existente
            prod = em.merge(prod);
        }
        return prod;
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        // TEM QUE FAZER CONSULTA PARA OBJETO FICAR ASSOCIADO (ATTACHED) 
        // AO ENTITY MANAGER 
        Produto prod = em.find(Produto.class, id);
        em.remove(prod);
    }

}
