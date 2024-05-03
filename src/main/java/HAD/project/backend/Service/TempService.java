package HAD.project.backend.Service;

import HAD.project.backend.Model.Temp;
import HAD.project.backend.DAO.TempDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TempService {

    @Autowired
    private TempDAO tempDAO;

    public Temp save(Temp temp) {
        return tempDAO.save(temp);
    }

    public Optional<Temp> findById(UUID id) {
        return Optional.ofNullable(tempDAO.findById(id));
    }

    public List<Temp> findAll() {
        return tempDAO.findAll();
    }

    public void deleteById(UUID id) {
        tempDAO.deleteById(id);
    }
    
    public void deleteAll() {
        tempDAO.deleteAll();
    }
}
