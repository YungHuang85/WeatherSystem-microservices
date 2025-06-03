package client;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class NotificationClient {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String NOTIFICATION_SERVICE_URL = "http://localhost:8083/notification";
    
    public void sendTeamsMessage(String message) {
        String url = NOTIFICATION_SERVICE_URL + "/api/teams/send";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, String> payload = Map.of("message", message);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);
        
        restTemplate.postForEntity(url, request, String.class);
    }
}