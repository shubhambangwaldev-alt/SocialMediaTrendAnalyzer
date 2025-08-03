package exception;

/**
 * Exception thrown when an external API call fails with a non-2xx response.
 * Wraps details about the external service, HTTP status code, and optional response body.
 * Useful for handling integration errors and propagating them with context.
 */
public class ExternalApiException extends ApiException {
    private final String serviceName;
    private final int httpStatusCode;
    private final String responseBody;

    /**
     * Constructs an ExternalApiException with service name, status code, and response body.
     *
     * @param serviceName    the name of the external service
     * @param httpStatusCode the HTTP status code returned
     * @param responseBody   the response body (optional, may be null)
     */
    public ExternalApiException(String serviceName, int httpStatusCode, String responseBody) {
        super(buildMessage(serviceName, httpStatusCode, responseBody));
        this.serviceName = serviceName;
        this.httpStatusCode = httpStatusCode;
        this.responseBody = responseBody;
    }

    /**
     * Constructs an ExternalApiException with service name, status code, response body, and cause.
     *
     * @param serviceName    the name of the external service
     * @param httpStatusCode the HTTP status code returned
     * @param responseBody   the response body (optional, may be null)
     * @param cause          the underlying cause
     */
    public ExternalApiException(String serviceName, int httpStatusCode, String responseBody, Throwable cause) {
        super(buildMessage(serviceName, httpStatusCode, responseBody), cause);
        this.serviceName = serviceName;
        this.httpStatusCode = httpStatusCode;
        this.responseBody = responseBody;
    }

    private static String buildMessage(String serviceName, int httpStatusCode, String responseBody) {
        StringBuilder sb = new StringBuilder();
        sb.append("External API call to '").append(serviceName)
                .append("' failed with status ").append(httpStatusCode);
        if (responseBody != null && !responseBody.isEmpty()) {
            sb.append(". Response body: ").append(responseBody);
        }
        return sb.toString();
    }

    /**
     * @return the name of the external service
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @return the HTTP status code returned by the external service
     */
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    /**
     * @return the response body from the external service, or null if not available
     */
    public String getResponseBody() {
        return responseBody;
    }
}
