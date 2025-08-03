package exception;

/**
 * Base runtime exception for domain and external API errors.
 * Serves as the superclass for all application-specific exceptions.
 */
public class ApiException extends RuntimeException {
    private final String errorCode;

    /**
     * Constructs a new ApiException with the specified detail message.
     * @param message the detail message
     */
    public ApiException(String message) {
        super(message);
        this.errorCode = null;
    }

    /**
     * Constructs a new ApiException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    /**
     * Constructs a new ApiException with the specified cause.
     * @param cause the cause
     */
    public ApiException(Throwable cause) {
        super(cause);
        this.errorCode = null;
    }

    /**
     * Constructs a new ApiException with the specified detail message and error code.
     * @param message the detail message
     * @param errorCode the error code
     */
    public ApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Gets the error code associated with this exception.
     * @return the error code, or null if not set
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Converts this exception to a log-friendly string.
     * @return a string representation suitable for logging
     */
    public String toLogString() {
        return String.format("ApiException: %s, errorCode=%s, cause=%s",
                getMessage(),
                errorCode,
                getCause() != null ? getCause().toString() : "none");
    }
}
