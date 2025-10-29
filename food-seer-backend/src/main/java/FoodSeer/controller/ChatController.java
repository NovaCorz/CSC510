package FoodSeer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ollama.url:http://localhost:11434}")
    private String ollamaUrl;

    @PostMapping
    public ResponseEntity<Map<String, Object>> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gemma3:1b"); // Or whatever model you’ve pulled
        body.put("prompt", userMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Call Ollama’s local API
        ResponseEntity<String> response = restTemplate.postForEntity(
                ollamaUrl + "/api/generate", entity, String.class);

        // Return the raw text response to frontend
        Map<String, Object> result = new HashMap<>();
        result.put("response", response.getBody());

        return ResponseEntity.ok(result);
    }
}