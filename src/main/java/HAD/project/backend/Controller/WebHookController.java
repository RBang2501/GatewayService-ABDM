package HAD.project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
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

    private final ObjectMapper objectMapper = new ObjectMapper();


    @PostMapping("/v0.5/users/auth/on-fetch-modes")
    public void handleWebhookEvent(@RequestBody String payload) {
        try {
            Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
            
            Map<String, Object> resp = (Map<String, Object>) payloadMap.get("resp");
            String requestId = (String) resp.get("requestId");
            System.out.println("Request ID: " + requestId);

            // Request requestRecord = requestService.getRequestByRequestId(requestId);
            // requestRecord.setTransactionId("Rakshit Chal Raha");
            // requestService.updateRequest(requestRecord);
            
            Map<String, Object> auth = (Map<String, Object>) payloadMap.get("auth");
            List<String> modes = (List<String>) auth.get("modes");
            System.out.println("Modes: " + modes);
        
            Map<String, Object> demographicData = new HashMap<>();
            demographicData.put("modes", modes);
            demographicData.put("requestId", requestId);

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


    @PostMapping("/v0.5/users/auth/init")
    public void handleWebhookEventAuthInit(@RequestBody String payload) {
        try {
            Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
            
            Map<String, Object> resp = (Map<String, Object>) payloadMap.get("resp");
            String requestId = (String) resp.get("requestId");
            System.out.println("Request ID: " + requestId);

            Map<String, Object> auth = (Map<String, Object>) payloadMap.get("auth");
            String transactionId = (String) auth.get("transactionId");
            System.out.println("Transaction ID: " + transactionId);

            Request requestRecord = requestService.getRequestByRequestId(requestId);
            requestRecord.setTransactionId(transactionId);
            requestService.updateRequest(requestRecord);

            Map<String, Object> responseJson = new HashMap<>();
            responseJson.put("requestId", requestId);
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
}
