package HAD.project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import HAD.project.backend.DAO.DocumentDAO;

import HAD.project.backend.Model.Document;

@Service
public class DocumentService {
    @Autowired 
    private DocumentDAO documentDAO;
    
    @Transactional
    public Document storeDocument(Document document) {
        Document storedDoc = documentDAO.create(document);
        return storedDoc; 
    }

    @Transactional
    public void updateDocument(Document document) {
        documentDAO.update(document);
    }

    @Transactional(readOnly = true)
    public Document readDocument(long id) {
        return documentDAO.read(id);
    }

    @Transactional
    public void deleteDocument(Long id){

        Document document = documentDAO.read(id);
        if (document != null) {
            documentDAO.delete(document);
        } else {
            throw new RuntimeException("Doctor not found with ID: " + id);
        }
    }

    






}
