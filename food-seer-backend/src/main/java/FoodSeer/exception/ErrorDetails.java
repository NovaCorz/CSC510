package FoodSeer.exception;

import java.util.Date;

/**
 * Error details object used in API exception responses.
 */
public class ErrorDetails {

    /** The timestamp of the error */
    private Date timestamp;

    /** The error message */
    private String message;

    /** The request path or description */
    private String details;

    /**
     * Constructs an ErrorDetails object.
     *
     * @param timestamp
     *            The date/time of the error
     * @param message
     *            The message describing the error
     * @param details
     *            Additional request details
     */
    public ErrorDetails(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    /**
     * @return The timestamp of the error
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @return The error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return The request details
     */
    public String getDetails() {
        return details;
    }
}
