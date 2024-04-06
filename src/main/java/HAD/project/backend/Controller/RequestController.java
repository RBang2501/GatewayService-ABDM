package HAD.project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import HAD.project.backend.Model.Request;
import HAD.project.backend.Service.RequestService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @GetMapping("/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable long id) {
        Request request = requestService.getRequestById(id);
        if (request != null) {
            return new ResponseEntity<>(request, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests() {
        List<Request> requests = requestService.getAllRequests();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Request> createRequest(@RequestBody Map<String, Object> requestJson) {
        String abhaAddress = (String) requestJson.get("abhaAddress");
        String requesterType = (String) requestJson.get("requesterType");
        String requesterId = (String) requestJson.get("requesterId");
        String purpose = (String) requestJson.get("purpose");
        
        Request createdRequest = requestService.saveRequest(abhaAddress, requesterType, requesterId, purpose);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Request> updateRequest(@PathVariable long id, @RequestBody Request updatedRequest) {
        Request existingRequest = requestService.getRequestById(id);
        if (existingRequest != null) {
            updatedRequest.setId(id); // Ensure the ID remains unchanged
            Request updated = requestService.updateRequest(updatedRequest);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable long id) {
        requestService.deleteRequestById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
