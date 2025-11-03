package FoodSeer.service;

import FoodSeer.dto.ChatRequestDto;
import FoodSeer.dto.ChatResponseDto;

/**
 * Service interface for AI chat functionality.
 */
public interface ChatService {
    /**
     * Sends a message to the Ollama AI and returns the response.
     *
     * @param chatRequest the chat request containing the user's message
     * @return the AI's response
     */
    ChatResponseDto sendMessage(ChatRequestDto chatRequest);
}

