package HAD.project.backend.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/abhaGenerator")
public class AbhaGenerationController {

    private final RestTemplate restTemplate;

    public AbhaGenerationController() {
        this.restTemplate = new RestTemplate();
    }

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

    @PostMapping("/publicKey")
    public String generatePublicKey() {
        String token = generateToken();

        String apiUrl = "https://healthidsbx.abdm.gov.in/api/v2/auth/cert";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, httpEntity, String.class);
        String responseBody = responseEntity.getBody();

        // Store the public key in the controller
        // this.publicKey = responseBody;

        return responseBody;
    }

    @PostMapping("/generateOTP")
    public String generateOTP(@RequestBody Map<String, String> request) {
        String aadhaar = request.get("aadhaar");
        String token = generateToken();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("aadhaar", aadhaar);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(requestBody, headers);
        String apiUrl = "https://healthidsbx.abdm.gov.in/api/v2/registration/aadhaar/generateOtp"; 
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<Map<String, String>>() {});
        Map<String, String> responseBodyMap = responseEntity.getBody();
        String txnId = responseBodyMap.get("txnId");
        // this.txnId = txnId;
        return txnId;
    }


    @PostMapping("/verifyOTP")
    public String verifyOTP(@RequestBody Map<String, String> request) {
        String otp = request.get("otp");
        String transactionId = request.get("txnId");
        String token = generateToken();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("otp", otp);
        requestBody.put("txnId", transactionId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(requestBody, headers);
        String apiUrl = "https://healthidsbx.abdm.gov.in/api/v2/registration/aadhaar/verifyOTP"; 
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String, Object> responseBody = responseEntity.getBody();
        String txnId = (String) responseBody.get("txnId");

        return txnId;
    }

    @PostMapping("/generateMobileOTP")
    public ResponseEntity<Map<String, Object>> generateMobileOTP(@RequestBody Map<String, Object> request) {
        Long mobileNumber = (Long)request.get("mobile");
        String transactionId = (String) request.get("txnId");
        String token = generateToken();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("mobile", mobileNumber);
        requestBody.put("txnId", transactionId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);
        String apiUrl = "https://healthidsbx.abdm.gov.in/api/v2/registration/aadhaar/checkAndGenerateMobileOTP"; 
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
        return responseEntity;
        // Map<String, Object> responseBodyMap = responseEntity.getBody();
        // return responseBodyMap;
        // String txnId = (String) responseBodyMap.get("txnId");
        // String mobileLinked = (boolean) responseBodyMap.get("mobile")
        
    }

    @PostMapping("/verifyMobileOTP")
    public String verifyMobileOTP(@RequestBody Map<String, Object> request) {
        String otp = (String) request.get("otp");
        String transactionId = (String) request.get("txnId");
        String token = generateToken();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("otp", otp);
        requestBody.put("txnId", transactionId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);
        String apiUrl = "https://healthidsbx.abdm.gov.in/api/v2/registration/aadhaar/verifyMobileOTP"; 
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String, Object> responseBody = responseEntity.getBody();
        String txnId = (String) responseBody.get("txnId");

        return txnId;
    }

    @PostMapping("/generateAbhaNumber")
    public String generateAbhaNumber(@RequestBody Map<String, Object> request) {
        String transactionId = (String) request.get("txnId");
        String token = generateToken();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("txnId", transactionId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);
        String apiUrl = "https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/createHealthIdWithPreVerified"; 
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String, Object> responseBody = responseEntity.getBody();
        String healthIdNumber= (String) responseBody.get("healthIdNumber");

        return healthIdNumber;
    }
}
