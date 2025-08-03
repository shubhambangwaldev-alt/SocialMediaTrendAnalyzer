package exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;



/**
 * Centralized exception handler for API endpoints.
 * Formats error responses consistently for API clients, mapping exceptions to appropriate HTTP status codes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Map<String, Object>> buildErrorResponse(Exception ex, HttpStatus status, WebRequest request, String errorCode) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));
        if (errorCode != null) {
            body.put("errorCode", errorCode);
        }
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        logger.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request, "RESOURCE_NOT_FOUND");
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEntity(DuplicateEntityException ex, WebRequest request) {
        logger.warn("Duplicate entity: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request, "DUPLICATE_ENTITY");
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<Map<String, Object>> handleExternalApi(ExternalApiException ex, WebRequest request) {
        logger.error("External API error: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex, HttpStatus.SERVICE_UNAVAILABLE, request, "EXTERNAL_API_ERROR");
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex, WebRequest request) {
        logger.warn("API exception: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request, "API_VALIDATION_ERROR");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        Exception safeEx = new Exception("An unexpected error occurred. Please contact support.");
        return buildErrorResponse(safeEx, HttpStatus.INTERNAL_SERVER_ERROR, request, null);
    }
}