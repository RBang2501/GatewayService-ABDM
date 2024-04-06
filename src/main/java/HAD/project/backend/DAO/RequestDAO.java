    package HAD.project.backend.DAO;

    import org.springframework.stereotype.Repository;
    import jakarta.persistence.EntityManager;
    import jakarta.persistence.PersistenceContext;
    import java.util.List;
    import org.springframework.transaction.annotation.Transactional;
    import HAD.project.backend.Model.Request;

    @Repository
    public class RequestDAO {

        @PersistenceContext
        private EntityManager entityManager;

        @Transactional
        public Request save(String abhaAddress, String requesterType, String requesterId, String purpose) {
            Request request = new Request(abhaAddress, requesterType, requesterId, purpose);
            entityManager.persist(request);
            return request;
        }

        @Transactional(readOnly = true)
        public Request findById(long id) {
            return entityManager.find(Request.class, id);
        }

        @Transactional(readOnly = true)
        public List<Request> findAll() {
            return entityManager.createQuery("SELECT r FROM Request r", Request.class).getResultList();
        }

        @Transactional
        public Request update(Request request) {
            return entityManager.merge(request);
        }

        @Transactional
        public void deleteById(long id) {
            Request request = entityManager.find(Request.class, id);
            if (request != null) {
                entityManager.remove(request);
            }
        }

        @Transactional(readOnly = true)
        public Request findByRequestId(String id) {
            return entityManager.createQuery("SELECT r FROM Request r WHERE r.requestId = :id", Request.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }

    }
