package FoodSeer.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for the FoodSeer API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException
     *
     * @param ex
     *            The thrown exception
     * @param request
     *            The web request
     * @return ResponseEntity containing ErrorDetails
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex,
            WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles FoodSeerAPIException
     *
     * @param ex
     *            The thrown exception
     * @param request
     *            The web request
     * @return ResponseEntity containing ErrorDetails
     */
    @ExceptionHandler(FoodSeerAPIException.class)
    public ResponseEntity<ErrorDetails> handleFoodSeerAPIException(FoodSeerAPIException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, ex.getStatus());
    }

    /**
     * Handles MethodArgumentNotValidException (validation errors)
     *
     * @param ex
     *            The thrown exception
     * @param request
     *            The web request
     * @return ResponseEntity containing ErrorDetails
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(MethodArgumentNotValidException ex,
            WebRequest request) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), message, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles AccessDeniedException
     *
     * @param ex
     *            The thrown exception
     * @param request
     *            The web request
     * @return ResponseEntity containing ErrorDetails
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException ex,
            WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Access Denied", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles AuthenticationException (unauthorized errors)
     *
     * @param ex
     *            The thrown exception
     * @param request
     *            The web request
     * @return ResponseEntity containing ErrorDetails
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDetails> handleAuthenticationException(AuthenticationException ex,
            WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Unauthorized: " + ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles all other exceptions
     *
     * @param ex
     *            The thrown exception
     * @param request
     *            The web request
     * @return ResponseEntity containing ErrorDetails
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
