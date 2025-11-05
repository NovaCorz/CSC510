package FoodSeer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import FoodSeer.dto.ChatRequestDto;
import FoodSeer.dto.ChatResponseDto;
import FoodSeer.service.ChatService;

/**
 * Controller for AI chat functionality.
 * Provides endpoint for communicating with Ollama AI.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    /** Connection to ChatService */
    @Autowired
    private ChatService chatService;
    
    /**
     * Sends a message to the AI and returns the response.
     *
     * @param chatRequest the chat request containing the user's message
     * @return ResponseEntity containing the AI's response
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @PostMapping
    public ResponseEntity<ChatResponseDto> sendMessage(@RequestBody final ChatRequestDto chatRequest) {
        final ChatResponseDto response = chatService.sendMessage(chatRequest);
        return ResponseEntity.ok(response);
    }
}

