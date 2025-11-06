package FoodSeer.service.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import FoodSeer.dto.ChatRequestDto;
import FoodSeer.dto.ChatResponseDto;
import FoodSeer.service.ChatService;

/**
 * Implementation of ChatService for communicating with Ollama AI.
 */
@Service
public class ChatServiceImpl implements ChatService {
    
    /** Ollama API endpoint */
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    
    /** Model to use */
    private static final String MODEL = "gemma3:1b";
    
    /** REST template for HTTP requests */
    private final RestTemplate restTemplate;
    
    /** JSON object mapper */
    private final ObjectMapper objectMapper;
    
    /**
     * Constructor for ChatServiceImpl.
     */
    public ChatServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public ChatResponseDto sendMessage(final ChatRequestDto chatRequest) {
        try {
            // Create request body for Ollama
            final ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", MODEL);
            requestBody.put("prompt", chatRequest.getMessage());
            requestBody.put("stream", false);
            
            // Set headers
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Make request
            final HttpEntity<String> entity = new HttpEntity<>(
                objectMapper.writeValueAsString(requestBody), 
                headers
            );
            
            final ResponseEntity<String> response = restTemplate.postForEntity(
                OLLAMA_URL, 
                entity, 
                String.class
            );
            
            // Parse response
            if (response.getBody() != null) {
                final JsonNode responseJson = objectMapper.readTree(response.getBody());
                final String aiResponse = responseJson.get("response").asText();
                return new ChatResponseDto(aiResponse);
            }
            
            return new ChatResponseDto("No response from AI");
            
        } catch (final Exception e) {
            System.err.println("Error communicating with Ollama: " + e.getMessage());
            e.printStackTrace();
            return new ChatResponseDto("Error: " + e.getMessage());
        }
    }
}

