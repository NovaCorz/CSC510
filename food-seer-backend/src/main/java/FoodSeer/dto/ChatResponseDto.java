package FoodSeer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for chat response from Ollama.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDto {
    /** The AI's response message */
    private String message;
}

