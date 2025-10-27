package FoodSeer.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom API exception for FoodSeer application.
 */
public class FoodSeerAPIException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** HTTP status associated with this exception */
    private HttpStatus status;

    /** Error message */
    private String message;

    /**
     * Constructs a new API exception with status and message.
     *
     * @param status
     *            The HTTP status code
     * @param message
     *            The error message
     */
    public FoodSeerAPIException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    /**
     * @return The HTTP status
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * @return The error message
     */
    @Override
    public String getMessage() {
        return message;
    }
}
