package HAD.project.backend.Service;

import HAD.project.backend.Model.Bundle;
import HAD.project.backend.DAO.BundleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BundleService {

    @Autowired
    private BundleDAO bundleDAO;

    public Bundle save(Bundle bundle) {
        return bundleDAO.save(bundle);
    }

    public Optional<Bundle> findById(Long id) {
        return Optional.ofNullable(bundleDAO.findById(id));
    }

    public List<Bundle> findAll() {
        return bundleDAO.findAll();
    }

    public Bundle update(Bundle bundle) {
        return bundleDAO.update(bundle);
    }

    public void delete(Bundle bundle) {
        bundleDAO.delete(bundle);
    }

    public void deleteById(Long id) {
        bundleDAO.deleteById(id);
    }
}
