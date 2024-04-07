package HAD.project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import HAD.project.backend.Model.Request;

import java.util.*;

@RestController
@CrossOrigin(origins = "*") 
public class WebHookController {

    @Autowired
    private RequestService requestService;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();


    @PostMapping("/v0.5/users/auth/on-fetch-modes")
    public void handleWebhookEvent(@RequestBody String payload) {
        try {
            Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
            
            Map<String, Object> resp = (Map<String, Object>) payloadMap.get("resp");
            String requestId = (String) resp.get("requestId");
            // System.out.println("Request ID: " + requestId);

            String newRequestId = (String) payloadMap.get("requestId");
            System.out.println(newRequestId);

            Request requestRecord = requestService.getRequestByRequestId(requestId);
            requestRecord.setRequestId(newRequestId);
            requestService.updateRequest(requestRecord);
            
            Map<String, Object> auth = (Map<String, Object>) payloadMap.get("auth");
            List<String> modes = (List<String>) auth.get("modes");
            System.out.println("Modes: " + modes);
        
            Map<String, Object> demographicData = new HashMap<>();
            demographicData.put("modes", modes);
            demographicData.put("requestId", newRequestId);

            // String frontendUrl = "http://localhost:3000/updateDemographicData"; 

            // HttpHeaders headers = new HttpHeaders();
            // headers.setContentType(MediaType.APPLICATION_JSON);

            // HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(demographicData, headers);
            // RestTemplate restTemplate = new RestTemplate();
            // ResponseEntity<String> response = restTemplate.postForEntity(frontendUrl, requestEntity, String.class);
            // if (response.getStatusCode() == HttpStatus.OK) {
            //     System.out.println("Demographic data sent successfully to the frontend.");
            // } else {
            //     System.out.println("Failed to send demographic data to the frontend. Status code: " + response.getStatusCodeValue());
            // }
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
            // System.out.println("Request ID: " + requestId);

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

            // String frontendUrl = "http://localhost:3000/initAuth"; 

            // HttpHeaders headers = new HttpHeaders();
            // headers.setContentType(MediaType.APPLICATION_JSON);

            // HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(responseJson, headers);
            // RestTemplate restTemplate = new RestTemplate();
            // ResponseEntity<String> response = restTemplate.postForEntity(frontendUrl, requestEntity, String.class);
            // if (response.getStatusCode() == HttpStatus.OK) {
            //     System.out.println("Demographic data sent successfully to the frontend.");
            // } else {
            //     System.out.println("Failed to send demographic data to the frontend. Status code: " + response.getStatusCodeValue());
            // }
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
            String transactionId = (String) auth.get("transactionId");
            System.out.println("Transaction ID: " + transactionId);

            String accessToken = (String) payloadMap.get("accessToken");
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
            HttpEntity<Map<String, Object>> getRequestEntity = new HttpEntity<>(requestToSendJson, getRequestHeaders);
            ResponseEntity<Map<String, Object>> getRequestResponseEntity = restTemplate.exchange(apiToGetRequest, HttpMethod.POST, getRequestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
            


            Map<String, Object> responseJson = new HashMap<>();                
            responseJson.put("accessToken", accessToken);
            responseJson.put("transactionId", transactionId);
            responseJson.put("requestId", newRequestId);  
           
            // String frontendUrl = "http://localhost:3000/confirmAuth"; 

            // HttpHeaders headers = new HttpHeaders();
            // headers.setContentType(MediaType.APPLICATION_JSON);

            // HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(responseJson, headers);
            // RestTemplate restTemplate = new RestTemplate();
            // ResponseEntity<String> response = restTemplate.postForEntity(frontendUrl, requestEntity, String.class);
            // if (response.getStatusCode() == HttpStatus.OK) {
            //     System.out.println("Demographic data sent successfully to the frontend.");
            // } else {
            //     System.out.println("Failed to send demographic data to the frontend. Status code: " + response.getStatusCodeValue());
            // }
        } catch (JsonProcessingException e) {
            e.printStackTrace(); 
        }
    }
}
