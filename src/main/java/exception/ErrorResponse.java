package exception;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Immutable DTO representing error response details for API exceptions.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ErrorResponse {
    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final String errorCode;

    /**
     * Constructs an ErrorResponse.
     *
     * @param timestamp the time the error occurred
     * @param status the HTTP status code
     * @param error the error type
     * @param message the error message
     * @param path the request path
     * @param errorCode optional error code
     */
    public ErrorResponse(Instant timestamp, int status, String error, String message, String path, String errorCode) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.errorCode = errorCode;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public String getErrorCode() {
        return errorCode;
    }
}