package com.thejatech.whatsapp.service.impl;

import com.thejatech.whatsapp.service.WhatsAppService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppServiceImpl implements WhatsAppService {

    private final String apiUrl;
    private final String apiToken;
    private final RestTemplate restTemplate;

    public WhatsAppServiceImpl(
            @Value("${whatsapp.api.url}") String apiUrl,
            @Value("${whatsapp.api.token}") String apiToken,
            RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
        this.restTemplate = restTemplate;
    }

    @Async
    @Override
    public void sendWhatsAppMessage(String to, String messageText) {
        if (to == null || to.isEmpty()) {
            System.err.println("Cannot send WhatsApp message: Recipient phone number is missing.");
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("recipient_type", "individual");
        body.put("to", to);
        body.put("type", "text");

        Map<String, String> text = new HashMap<>();
        text.put("body", messageText);
        body.put("text", text);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            // MOCK IMPLEMENTATION: Simulating a successful API call
            System.out.println("--- MOCK WHATSAPP MESSAGE START ---");
            System.out.println("To: " + to);
            System.out.println("Message: " + messageText);
            System.out.println("--- MOCK WHATSAPP MESSAGE END ---");
            
            // Simulating API Response
            String mockResponse = "{\"messaging_product\":\"whatsapp\",\"contacts\":[{\"input\":\"" + to + "\",\"wa_id\":\"" + to + "\"}],\"messages\":[{\"id\":\"wamid.HBgMREAA...\"}]}";
            System.out.println("WhatsApp message sent (MOCKED): " + mockResponse);
            
            /* UNCOMMENT WHEN API CREDENTIALS ARE AVAILABLE
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            System.out.println("WhatsApp message sent: " + response.getBody());
            */
        } catch (Exception e) {
            System.err.println("Error sending WhatsApp message: " + e.getMessage());
        }
    }
}
