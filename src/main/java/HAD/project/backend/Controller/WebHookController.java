package HAD.project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;

import HAD.project.backend.Service.RequestService;
import HAD.project.backend.Service.TempService;
import HAD.project.backend.Service.TokenToLinkService;
import HAD.project.backend.Model.Request;
import HAD.project.backend.Model.Temp;
import HAD.project.backend.Model.TokenToLink;

import java.util.*;

@RestController
@CrossOrigin(origins = "*") 
public class WebHookController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private TempService tempService;

    @Autowired
    private TokenToLinkService tokenToLinkService;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Value("${abdm.clientId}")
    private String clientId;

    @Value("${abdm.clientSecret}")
    private String clientSecret;

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


    @PostMapping("/v0.5/users/auth/on-fetch-modes")
    public void handleWebhookEvent(@RequestBody String payload) {
        try {
            Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
            
            Map<String, Object> resp = (Map<String, Object>) payloadMap.get("resp");
            String requestId = (String) resp.get("requestId");
            System.out.println("Request ID: " + requestId);

            String newRequestId = (String) payloadMap.get("requestId");
            System.out.println(newRequestId);

            Request requestRecord = requestService.getRequestByRequestId(requestId);
            requestRecord.setRequestId(newRequestId);
            requestService.updateRequest(requestRecord);

            Temp temp = new Temp();
            temp.setRequestId(newRequestId);
            temp.setJsonString(payload);
            tempService.save(temp);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); 
        }

    }


    @PostMapping("/v0.5/users/auth/on-init")
    public void handleWebhookEventAuthInit(@RequestBody String payload) {
        try {
            Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
            
            Map<String, Object> resp = (Map<String, Object>) payloadMap.get("resp");
            String requestId = (String) resp.get("requestId");
            Map<String, Object> auth = (Map<String, Object>) payloadMap.get("auth");
            String transactionId = (String) auth.get("transactionId");
            System.out.println("Transaction ID: " + transactionId);

            String newRequestId = (String) payloadMap.get("requestId");
            System.out.println("NewRequest ID: " + newRequestId);

            Request requestRecord = requestService.getRequestByRequestId(requestId);
            requestRecord.setRequestId(newRequestId);
            requestRecord.setTransactionId(transactionId);
            requestService.updateRequest(requestRecord);

            Map<String, Object> responseJson = new HashMap<>();
            responseJson.put("requestId", newRequestId);
            responseJson.put("transactionId", transactionId);

            Temp temp = new Temp();
            temp.setRequestId(newRequestId);
            temp.setJsonString(payload);
            tempService.save(temp);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); 
        }
    }


    @PostMapping("/v0.5/users/auth/on-confirm")
    public void handleWebhookEventAuthConfirm(@RequestBody String payload) {
        try {
            Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
            
            Map<String, Object> resp = (Map<String, Object>) payloadMap.get("resp");
            String requestId = (String) resp.get("requestId");
            System.out.println("Request ID: " + requestId);

            Map<String, Object> auth = (Map<String, Object>) payloadMap.get("auth");
            String accessToken = (String) auth.get("accessToken");
            String expiry = (String) auth.get("expiry");
            System.out.println("Access Token: " + accessToken);

            String newRequestId = (String) payloadMap.get("requestId");
            String timeStampOfGeneration = (String) payloadMap.get("timestamp");

            Request requestRecord = requestService.getRequestByRequestId(requestId);
            String patientAbhaAddress = requestRecord.getAbhaAddress();

            requestRecord.setAccessToken(accessToken);
            requestRecord.setRequestId(newRequestId);
            requestRecord.setTimeStamp(timeStampOfGeneration);
            requestService.updateRequest(requestRecord);

            String apiToGetRequest = "http://localhost:8087/medicalRecordLink/create-token";
            HttpHeaders getRequestHeaders = new HttpHeaders();
            getRequestHeaders.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> requestToSendJson = new HashMap<>();
            requestToSendJson.put("patientAbhaAddress", patientAbhaAddress);
            requestToSendJson.put("accessToken", accessToken);
            requestToSendJson.put("timeStampOfGeneration", timeStampOfGeneration);
            requestToSendJson.put("requestId", newRequestId);
            requestToSendJson.put("expiryTimeStamp", expiry);
            HttpEntity<Map<String, Object>> getRequestEntity = new HttpEntity<>(requestToSendJson, getRequestHeaders);
            ResponseEntity<Map<String, Object>> getRequestResponseEntity = restTemplate.exchange(apiToGetRequest, HttpMethod.POST, getRequestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
    
            
            Temp temp = new Temp();
            temp.setRequestId(newRequestId);
            temp.setJsonString(payload);
            tempService.save(temp);
           
        } catch (JsonProcessingException e) {
            e.printStackTrace(); 
        }
    }

    @PostMapping("/v0.5/consents/hip/notify")
    public void handleWebhookEventConsentNotify(@RequestBody String payload) {
        try {
            Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});

            Map<String, Object> notification = (Map<String, Object>) payloadMap.get("notification");
            Map<String, Object> consentDetail = (Map<String, Object>) notification.get("consentDetail");
            Map<String, Object> patient = (Map<String, Object>) consentDetail.get("patient");
            String patientId = (String) patient.get("id");
            
            Map<String, Object> hip = (Map<String, Object>) consentDetail.get("hip");
            String hipId = (String) hip.get("id");
            String hipName = (String) hip.get("name");

            TokenToLink tokenToLink = tokenToLinkService.getTokenByPatientAbhaAddress(patientId);
            String requestId = tokenToLink.getRequestId();
            String timeStamp = tokenToLink.getTimeStampOfGeneration();
        
            Map<String, Object> responseJson = new HashMap<>();
            responseJson.put("requestId", requestId);
            responseJson.put("timestamp", timeStamp);
        
            Map<String, Object> notificationJson = new HashMap<>();
            notificationJson.put("phoneNo", "+91-7249338277"); 
            Map<String, Object> hipJson = new HashMap<>();
            hipJson.put("name", hipName);
            hipJson.put("id", hipId);
            notificationJson.put("hip", hipJson);
        
            responseJson.put("notification", notificationJson);

            String jwtToken = generateToken();
            String apiToGetRequest = "https://dev.abdm.gov.in/gateway/v0.5/patients/sms/notify2";
            HttpHeaders getRequestHeaders = new HttpHeaders();
            getRequestHeaders.setContentType(MediaType.APPLICATION_JSON);
            getRequestHeaders.set("Authorization", "Bearer " + jwtToken);
            getRequestHeaders.set("X-CM-ID", "sbx");
            HttpEntity<Map<String, Object>> getRequestEntity = new HttpEntity<>(responseJson, getRequestHeaders);
            ResponseEntity<Map<String, Object>> getRequestResponseEntity = restTemplate.exchange(apiToGetRequest, HttpMethod.POST, getRequestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
            System.out.println("MileStone 2 Done !");
        
        } catch (JsonProcessingException e) {
            e.printStackTrace(); 
        }
    }

    @PostMapping("/v0.5/consent-requests/init")
    public void handleWebhookEventCareContext(@RequestBody String payload) {
        try {
            Map<String, Object> requestJson = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
            Temp temp = new Temp();
            temp.setRequestId("Consent Request");
            temp.setJsonString(payload);
            tempService.save(temp);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); 
        }
    }
    



}
