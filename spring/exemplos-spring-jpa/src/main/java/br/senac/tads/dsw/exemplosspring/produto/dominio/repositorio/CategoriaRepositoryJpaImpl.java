package br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Categoria;

@Repository
public class CategoriaRepositoryJpaImpl implements CategoriaRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Categoria> findAll() {
        TypedQuery<Categoria> jpqlQuery =
                em.createQuery("SELECT c FROM Categoria c", Categoria.class);
        return jpqlQuery.getResultList();
    }

    @Override
    public Categoria findById(Integer id) {
        TypedQuery<Categoria> jpqlQuery =
                em.createQuery("SELECT c FROM Categoria c WHERE c.id = :idCat", Categoria.class);
        jpqlQuery.setParameter("idCat", id);
        Categoria cat = jpqlQuery.getSingleResult();
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
