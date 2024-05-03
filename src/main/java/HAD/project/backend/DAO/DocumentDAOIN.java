package HAD.project.backend.DAO;

import HAD.project.backend.Model.Document;
import java.util.List;

public interface DocumentDAOIN {

    Document create(Document document);

    void update(Document document);

    Document read(long id);

    void delete(Document document);

}
