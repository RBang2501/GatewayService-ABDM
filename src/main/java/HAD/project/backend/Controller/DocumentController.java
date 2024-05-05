package HAD.project.backend.Controller;

import HAD.project.backend.Model.Document;
import HAD.project.backend.Service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin("*")
@RequestMapping("/received-store/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/create")
    public ResponseEntity<String> createDocument(@RequestBody Document document) {
        Document storedDocument = documentService.storeDocument(document);
        return ResponseEntity.status(HttpStatus.CREATED).body("Document created with ID: " + storedDocument.getMedicalRecordId());
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Document> readDocument(@PathVariable("id") long id) {
        Document document = documentService.readDocument(id);
        if (document != null) {
            return ResponseEntity.ok(document);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateDocument(@RequestBody Document document) {
        documentService.updateDocument(document);
        return ResponseEntity.ok("Document updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable("id") long id) {
        try {
            documentService.deleteDocument(id);
            return ResponseEntity.ok("Document deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
