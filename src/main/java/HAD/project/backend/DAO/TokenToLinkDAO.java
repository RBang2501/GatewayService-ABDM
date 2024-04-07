package HAD.project.backend.DAO;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import HAD.project.backend.Model.TokenToLink;

@Repository
public class TokenToLinkDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public TokenToLink save(TokenToLink token) {
        entityManager.persist(token);
        return token;
    }

    @Transactional(readOnly = true)
    public TokenToLink findById(String patientAbhaAddress) {
        return entityManager.find(TokenToLink.class, patientAbhaAddress);
    }

    @Transactional(readOnly = true)
    public List<TokenToLink> findAll() {
        return entityManager.createQuery("SELECT t FROM TokenToLink t", TokenToLink.class).getResultList();
    }

    @Transactional
    public TokenToLink update(TokenToLink token) {
        return entityManager.merge(token);
    }

    @Transactional
    public void deleteById(String patientAbhaAddress) {
        TokenToLink token = entityManager.find(TokenToLink.class, patientAbhaAddress);
        if (token != null) {
            entityManager.remove(token);
        }
    }

    @Transactional(readOnly = true)
    public TokenToLink findByPatientAbhaAddress(String patientAbhaAddress) {
        return entityManager.createQuery("SELECT t FROM TokenToLink t WHERE t.patientAbhaAddress = :patientAbhaAddress", TokenToLink.class)
                .setParameter("patientAbhaAddress", patientAbhaAddress)
                .getSingleResult();
    }

}
