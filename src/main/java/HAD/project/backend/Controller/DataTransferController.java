package HAD.project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import HAD.project.backend.DAO.BundleDAO;
import HAD.project.backend.Model.Bundle;
import HAD.project.backend.Model.Document;
import HAD.project.backend.Service.DocumentService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class DataTransferController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private BundleDAO bundleDAO; 

    @Autowired
    private DocumentService documentService;

    @Value("${abdm.clientId}")
    private String clientId;

    @Value("${abdm.clientSecret}")
    private String clientSecret;

    @PostMapping("/generate-data-transfer-token")
    public String generateToken() {
        String apiUrl = "https://dev.abdm.gov.in/gateway/v0.5/sessions";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("clientId", clientId);
        requestBody.put("clientSecret", clientSecret);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity,
                new ParameterizedTypeReference<Map<String, String>>() {});
        Map<String, String> responseBodyMap = responseEntity.getBody();
        String accessToken = responseBodyMap.get("accessToken");
        return accessToken;
    }

    @PostMapping("/hoja-data-transfer-sim-sim")
    public ResponseEntity<String> receiveDataFile(@RequestBody Map<String, Object> requestJson) {
        try {
            String display = (String) requestJson.get("display");
            String jsonString = (String) requestJson.get("jsonString");

            Bundle bundle = new Bundle();
            bundle.setDisplay(display);
            bundle.setJsonString(jsonString);

            bundleDAO.save(bundle);

            return ResponseEntity.ok("Bundle object created and stored successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error receiving or processing data.");
        }
    }

    @PostMapping("/request-data-share-url")
    public Map<String, Object> getDataShareURL() {
        Map<String, Object> responseJson = new HashMap<>();
        responseJson.put("url", "http://localhost:8087/hoja-data-transfer-sim-sim");
        return responseJson;
    }

    @PostMapping("/consent-request")
    public String fetchConsent(@RequestBody Map<String, Object> requestJson) {
        String accessToken = generateToken();
        String apiToFetchAuth = "https://dev.abdm.gov.in/gateway/v0.5/consent-requests/init";
        HttpHeaders fetchAuthHeaders = new HttpHeaders();
        fetchAuthHeaders.setContentType(MediaType.APPLICATION_JSON);
        fetchAuthHeaders.set("Authorization", "Bearer " + accessToken);
        fetchAuthHeaders.set("X-CM-ID", "sbx");
        HttpEntity<Map<String, Object>> fetchAuthEntity = new HttpEntity<>(requestJson, fetchAuthHeaders);
        ResponseEntity<Map<String, Object>> fetchAuthResponseEntity = restTemplate.exchange(apiToFetchAuth,
                HttpMethod.POST, fetchAuthEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
        new ResponseEntity<>(fetchAuthResponseEntity.getBody(), fetchAuthResponseEntity.getStatusCode());

        return "Consent Sent Successfully!";
    }

}
