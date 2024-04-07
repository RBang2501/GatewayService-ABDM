package HAD.project.backend.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import HAD.project.backend.Model.Request;
import HAD.project.backend.Model.TokenToLink;
import HAD.project.backend.Service.RequestService;
import HAD.project.backend.Service.TokenToLinkService;

import java.util.*;

@RestController
@CrossOrigin("*")
public class AbhaLinkRecordController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RequestService requestService;

    @Autowired
    private TokenToLinkService tokenToLinkService;

    @Value("${abdm.clientId}")
    private String clientId;

    @Value("${abdm.clientSecret}")
    private String clientSecret;

    @PostMapping("/generateToken")
    public String generateToken() {
        String apiUrl = "https://dev.abdm.gov.in/gateway/v0.5/sessions"; // Replace with your actual API URL

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("clientId", clientId);
        requestBody.put("clientSecret", clientSecret);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<Map<String, String>>() {});
        Map<String, String> responseBodyMap = responseEntity.getBody();
        String accessToken = responseBodyMap.get("accessToken");
        return accessToken;
    }

    @PostMapping("/auth/fetch-modes")
    public ResponseEntity<Map<String, Object>> fetchAuthModes(@RequestBody Map<String, Object> requestJson) {
        String id = (String) requestJson.get("id");
        String purpose = (String) requestJson.get("purpose");
        String requesterType = (String) requestJson.get("requesterType");
        String requesterId = (String) requestJson.get("requesterId");

        String apiToGetRequest = "http://localhost:8087/requests/create";
        HttpHeaders getRequestHeaders = new HttpHeaders();
        getRequestHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> abha = new HashMap<>();
        abha.put("abhaAddress", id);
        abha.put("requesterType", requesterType);
        abha.put("requesterId", requesterId);
        abha.put("purpose", purpose);
        HttpEntity<Map<String, Object>> getRequestEntity = new HttpEntity<>(abha, getRequestHeaders);
        ResponseEntity<Map<String, Object>> getRequestResponseEntity = restTemplate.exchange(apiToGetRequest, HttpMethod.POST, getRequestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
        
        Map<String, Object> requestRecord = getRequestResponseEntity.getBody();

        Map<String, Object> responseJson = new HashMap<>();
        responseJson.put("requestId", requestRecord.get("requestId"));
        responseJson.put("timestamp", requestRecord.get("timeStamp"));

        Map<String, Object> query = new HashMap<>();

        query.put("id", id);
        query.put("purpose", purpose);
        Map<String, Object> requester = new HashMap<>();
        requester.put("type", requesterType);
        requester.put("id", requesterId);
        query.put("requester", requester);
        responseJson.put("query", query);


        String accessToken = generateToken();

        String apiToFetchAuth = "https://dev.abdm.gov.in/gateway/v0.5/users/auth/fetch-modes";
        HttpHeaders fetchAuthHeaders = new HttpHeaders();
        fetchAuthHeaders.setContentType(MediaType.APPLICATION_JSON);
        fetchAuthHeaders.set("Authorization", "Bearer " + accessToken);
        fetchAuthHeaders.set("X-CM-ID", "sbx");
        HttpEntity<Map<String, Object>> fetchAuthEntity = new HttpEntity<>(responseJson, fetchAuthHeaders);
        ResponseEntity<Map<String, Object>> fetchAuthResponseEntity = restTemplate.exchange(apiToFetchAuth, HttpMethod.POST, fetchAuthEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
        return new ResponseEntity<>(fetchAuthResponseEntity.getBody(), fetchAuthResponseEntity.getStatusCode());

    }


    @PostMapping("/auth/init")
    public ResponseEntity<Map<String, Object>> authInit(@RequestBody Map<String, Object> requestJson) {
        String id = (String) requestJson.get("id");
        String purpose = (String) requestJson.get("purpose");
        String requesterType = (String) requestJson.get("requesterType");
        String requesterId = (String) requestJson.get("requesterId");
        String authMode = (String) requestJson.get("authMode");
        String requestId = (String) requestJson.get("requestId");

        Request requestRecord = requestService.getRequestByRequestId(requestId);

        Map<String, Object> responseJson = new HashMap<>();
        responseJson.put("requestId", requestId);
        responseJson.put("timestamp", requestRecord.getTimeStamp());

        Map<String, Object> query = new HashMap<>();
        query.put("id", id);
        query.put("purpose", purpose);
        query.put("authMode", authMode);
        Map<String, Object> requester = new HashMap<>();
        requester.put("type", requesterType);
        requester.put("id", requesterId);
        query.put("requester", requester);

        responseJson.put("query", query);

        // return responseJson;
        
        String accessToken = generateToken();

        String apiToFetchAuth = "https://dev.abdm.gov.in/gateway/v0.5/users/auth/init";
        HttpHeaders fetchAuthHeaders = new HttpHeaders();
        fetchAuthHeaders.setContentType(MediaType.APPLICATION_JSON);
        fetchAuthHeaders.set("Authorization", "Bearer " + accessToken);
        fetchAuthHeaders.set("X-CM-ID", "sbx");
        HttpEntity<Map<String, Object>> fetchAuthEntity = new HttpEntity<>(responseJson, fetchAuthHeaders);
        ResponseEntity<Map<String, Object>> fetchAuthResponseEntity = restTemplate.exchange(apiToFetchAuth, HttpMethod.POST, fetchAuthEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
        return new ResponseEntity<>(fetchAuthResponseEntity.getBody(), fetchAuthResponseEntity.getStatusCode());        

    }

    @PostMapping("/auth/confirm")
    public ResponseEntity<Map<String, Object>> confirmAuth(@RequestBody Map<String, Object> requestJson) {  
        String requestId = (String) requestJson.get("requestId");
        String otp = (String) requestJson.get("otp");

        Request requestRecord = requestService.getRequestByRequestId(requestId);

        Map<String, Object> responseJson = new HashMap<>();
        responseJson.put("requestId", requestId);
        responseJson.put("timestamp", requestRecord.getTimeStamp());
        responseJson.put("transactionId", requestRecord.getTransactionId());

        Map<String, Object> credential = new HashMap<>();
        credential.put("authCode", otp);
        responseJson.put("credential", credential);
        
        String accessToken = generateToken();

        String apiToFetchAuth = "https://dev.abdm.gov.in/gateway/v0.5/users/auth/confirm";
        HttpHeaders fetchAuthHeaders = new HttpHeaders();
        fetchAuthHeaders.setContentType(MediaType.APPLICATION_JSON);
        fetchAuthHeaders.set("Authorization", "Bearer " + accessToken);
        fetchAuthHeaders.set("X-CM-ID", "sbx");
        HttpEntity<Map<String, Object>> fetchAuthEntity = new HttpEntity<>(responseJson, fetchAuthHeaders);
        ResponseEntity<Map<String, Object>> fetchAuthResponseEntity = restTemplate.exchange(apiToFetchAuth, HttpMethod.POST, fetchAuthEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
        return new ResponseEntity<>(fetchAuthResponseEntity.getBody(), fetchAuthResponseEntity.getStatusCode());        
    }


    @PostMapping("/auth/add-care-context")
    public ResponseEntity<Map<String, Object>> addCareContext(@RequestBody Map<String, Object> requestJson) {
        String abhaAddress = (String) requestJson.get("abhaAddress");
        String referenceNumber = (String) requestJson.get("referenceNumber");
        String display = (String) requestJson.get("display");
    
        TokenToLink tokenToLinkRecord = tokenToLinkService.getTokenByPatientAbhaAddress(abhaAddress);
    
        Map<String, Object> responseJson = new HashMap<>();
        responseJson.put("requestId", tokenToLinkRecord.getRequestId());
        responseJson.put("timestamp", tokenToLinkRecord.getTimeStampOfGeneration());
    
        Map<String, Object> link = new HashMap<>();
        link.put("accessToken", tokenToLinkRecord.getAccessToken());
    
        Map<String, Object> patient = new HashMap<>();
        patient.put("referenceNumber", referenceNumber);
        patient.put("display", display);
    
        List<Map<String, Object>> careContexts = (List<Map<String, Object>>) requestJson.get("careContexts");
        
        patient.put("careContexts", careContexts);
        link.put("patient", patient);
        responseJson.put("link", link);

    
        String jwtToken = generateToken();
    
        String apiToFetchAuth = "https://dev.abdm.gov.in/gateway/v0.5/links/link/add-contexts";
        HttpHeaders fetchAuthHeaders = new HttpHeaders();
        fetchAuthHeaders.setContentType(MediaType.APPLICATION_JSON);
        fetchAuthHeaders.set("Authorization", "Bearer " + jwtToken);
        fetchAuthHeaders.set("X-CM-ID", "sbx");
        HttpEntity<Map<String, Object>> fetchAuthEntity = new HttpEntity<>(responseJson, fetchAuthHeaders);
        ResponseEntity<Map<String, Object>> fetchAuthResponseEntity = restTemplate.exchange(apiToFetchAuth, HttpMethod.POST, fetchAuthEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
        return new ResponseEntity<>(fetchAuthResponseEntity.getBody(), fetchAuthResponseEntity.getStatusCode());
    }
    







   



    



    
}
