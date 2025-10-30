package FoodSeer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
// Import JSONObject
import org.json.JSONObject;
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
        body.put("model", "gemma3:1b"); // We can change the model to a better one later
        body.put("prompt", userMessage);

        // Reports the answer in one batch instead of many small ones
        body.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Call Ollama’s local API
        ResponseEntity<String> response = restTemplate.postForEntity(
                ollamaUrl + "/api/generate", entity, String.class);

        // Return the raw text response to frontend
        Map<String, Object> result = new HashMap<>();
        // Put the response in the body field.
        // May want to specifically put the response's "response" field specifically.
        String ollamaResponse = new JSONObject(response.getBody()).get("response").toString();
        result.put("response", ollamaResponse);
        System.out.println("Ollama response: " + ollamaResponse);
        return ResponseEntity.ok(result);
    }
}