package exception;

import exception.ExternalApiException;

import java.util.Map;

/**
 * Exception thrown when a Twitter API request fails.
 * <p>
 * This exception provides access to HTTP status code, response body, headers, and the underlying cause.
 * Callers can use {@link #isRateLimited()} to detect if the error is due to Twitter rate limiting and
 * inspect headers for retry-after information to implement retry logic.
 */
public class TwitterApiException extends ExternalApiException {
    private final int statusCode;
    private final String responseBody;
    private final Map<String, String> headers;

    /**
     * Constructs a TwitterApiException.
     *
     * @param statusCode   HTTP status code from Twitter API response
     * @param responseBody Response body from Twitter API
     * @param headers      Response headers from Twitter API
     */
    public TwitterApiException(int statusCode, String responseBody, Map<String, String> headers) {
        super("Twitter API request failed", statusCode,responseBody);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.headers = headers;
    }

    /**
     * Constructs a TwitterApiException with a cause.
     *
     * @param statusCode   HTTP status code from Twitter API response
     * @param responseBody Response body from Twitter API
     * @param headers      Response headers from Twitter API
     * @param cause        The underlying cause of the failure
     */
    public TwitterApiException(int statusCode, String responseBody, Map<String, String> headers, Throwable cause) {

        super("Twitter API request failed", statusCode,responseBody, cause);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.headers = headers;
    }

    /**
     * Returns true if the response headers indicate Twitter rate limiting.
     * Checks for presence of rate-limit or retry-after headers.
     *
     * @return true if rate limited, false otherwise
     */
    public boolean isRateLimited() {
        if (headers == null) return false;
        return headers.containsKey("x-rate-limit-remaining") && "0".equals(headers.get("x-rate-limit-remaining"))
                || headers.containsKey("retry-after");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}