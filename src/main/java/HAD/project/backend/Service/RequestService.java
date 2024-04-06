package HAD.project.backend.Service;

import HAD.project.backend.Model.Request;
import HAD.project.backend.DAO.RequestDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RequestService {

    private final RequestDAO requestDAO;

    @Autowired
    public RequestService(RequestDAO requestDAO) {
        this.requestDAO = requestDAO;
    }

    // Method to save a new Request
    public Request saveRequest(String abhaAddress, String requesterType, String requesterId, String purpose) {
        return requestDAO.save(abhaAddress, requesterType, requesterId, purpose);
    }

    // Method to retrieve all Requests
    public List<Request> getAllRequests() {
        return requestDAO.findAll();
    }

    // Method to retrieve a Request by its ID
    public Request getRequestById(long id) {
        return requestDAO.findById(id);
    }

    // Method to update a Request
    public Request updateRequest(Request request) {
        return requestDAO.update(request);
    }

    // Method to delete a Request by its ID
    public void deleteRequestById(long id) {
        requestDAO.deleteById(id);
    }

    public Request getRequestByRequestId(String abha){
        return requestDAO.findByRequestId(abha);
    }
}
