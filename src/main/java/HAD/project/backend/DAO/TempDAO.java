package HAD.project.backend.DAO;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import HAD.project.backend.Model.Temp;


@Repository
public class TempDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Temp save(Temp temp) {
        entityManager.persist(temp);
        return temp;
    }

    @Transactional(readOnly = true)
    public Temp findById(String id) {
        return entityManager.find(Temp.class, id);
    }

    @Transactional(readOnly = true)
    public List<Temp> findAll() {
        return entityManager.createQuery("SELECT t FROM Temp t", Temp.class).getResultList();
    }

    @Transactional
    public Temp update(Temp temp) {
        return entityManager.merge(temp);
    }

    @Transactional
    public void deleteById(String id) {
        Temp temp = entityManager.find(Temp.class, id);
        if (temp != null) {
            entityManager.remove(temp);
        }
    }
    
    @Transactional
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Temp").executeUpdate();
    }
}
