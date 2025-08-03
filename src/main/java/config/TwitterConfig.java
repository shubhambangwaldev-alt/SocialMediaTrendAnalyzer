package config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for calling the Twitter API.
 */
@ConfigurationProperties(prefix = "twitter.api")
public class TwitterConfig {

    @NotBlank
    private final String baseUrl;

    @NotBlank
    private final String bearerToken;

    private final int timeoutSeconds;

    public TwitterConfig(String baseUrl, String bearerToken, Integer timeoutSeconds) {
        this.baseUrl = baseUrl;
        this.bearerToken = bearerToken;
        this.timeoutSeconds = (timeoutSeconds == null) ? 5 : timeoutSeconds;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }
}
