package HAD.project.backend.DAO;

import HAD.project.backend.Model.Document;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class DocumentDAO implements DocumentDAOIN{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Document create(Document document) {
        entityManager.persist(document);
        return document;
    }

    @Override
    @Transactional
    public void update(Document document) {
        entityManager.merge(document);
    }

    @Override
    @Transactional(readOnly = true)
    public Document read(long id) {
        return entityManager.find(Document.class, id);
    }

    @Override
    @Transactional
    public void delete(Document document) {
        entityManager.remove(document);
    }

}
