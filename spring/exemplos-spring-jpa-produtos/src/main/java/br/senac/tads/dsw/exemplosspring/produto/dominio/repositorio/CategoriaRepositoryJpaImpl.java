package br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Categoria;
import java.util.Optional;
import javax.persistence.NoResultException;

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
    public Optional<Categoria> findById(Integer id) {
        TypedQuery<Categoria> jpqlQuery =
                em.createQuery("SELECT c FROM Categoria c WHERE c.id = :idCat", Categoria.class);
        jpqlQuery.setParameter("idCat", id);
        try {
            Categoria cat = jpqlQuery.getSingleResult();
            return Optional.of(cat);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Categoria save(Categoria cat) {
        if (cat.getId() == null) {
            // Incluir nova categoria
            em.persist(cat);
        } else {
            // Atualizar categoria existente
            cat = em.merge(cat);
        }
        return cat;
    }

}
