package HAD.project.backend.DAO;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import HAD.project.backend.Model.Bundle;

@Repository
public class BundleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Bundle save(Bundle bundle) {
        entityManager.persist(bundle);
        return bundle;
    }

    @Transactional(readOnly = true)
    public Bundle findById(Long id) {
        return entityManager.find(Bundle.class, id);
    }

    @Transactional(readOnly = true)
    public List<Bundle> findAll() {
        return entityManager.createQuery("SELECT b FROM Bundle b", Bundle.class).getResultList();
    }

    @Transactional
    public Bundle update(Bundle bundle) {
        return entityManager.merge(bundle);
    }

    @Transactional
    public void delete(Bundle bundle) {
        entityManager.remove(entityManager.contains(bundle) ? bundle : entityManager.merge(bundle));
    }

    @Transactional
    public void deleteById(Long id) {
        Bundle bundle = findById(id);
        if (bundle != null) {
            entityManager.remove(bundle);
        }
    }
}
