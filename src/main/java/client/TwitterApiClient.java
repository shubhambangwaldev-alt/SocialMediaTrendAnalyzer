package client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TwitterConfig;
import exception.TwitterApiException;
import lombok.extern.slf4j.Slf4j;
import model.SocialPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Component responsible for calling Twitter API v2 to search recent tweets by keyword.
 * Handles authentication, rate limiting, and pagination.
 */
@Slf4j
@Component
public class TwitterApiClient {

    private final TwitterConfig config;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public TwitterApiClient(TwitterConfig config) {
        this.config = config;
        this.restTemplate = new RestTemplate();
        // Example: configure timeouts from config
        // var factory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        // factory.setConnectTimeout(config.getConnectTimeout());
        // factory.setReadTimeout(config.getReadTimeout());
    }

    /**
     * Searches recent tweets by keyword using Twitter API v2.
     * @param keyword the keyword to search for
     * @return list of SocialPost objects
     * @throws TwitterApiException if API call fails or rate limited
     */
    public List<SocialPost> searchTweets(String keyword) throws TwitterApiException {
        String url = String.format("%s/tweets/search/recent?query=%s&tweet.fields=created_at,author_id&expansions=author_id&user.fields=username",
                config.getBaseUrl(), keyword);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(config.getBearerToken());
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        log.info("Calling Twitter API for keyword: {}", keyword);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // Rate limit detection
            if (isRateLimited(response.getHeaders())) {
                log.warn("Twitter API rate limit reached. Headers: {}", response.getHeaders());
                throw new TwitterApiException(response.getStatusCodeValue(), response.getBody(), response.getHeaders().toSingleValueMap());
            }

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Twitter API non-2xx response: {} Body: {}", response.getStatusCodeValue(), response.getBody());
                throw new TwitterApiException(response.getStatusCodeValue(), response.getBody(), response.getHeaders().toSingleValueMap());
            }

            List<SocialPost> posts = parseTweets(response.getBody());
            log.info("Twitter API call successful for keyword: {}. Fetched {} tweets.", keyword, posts.size());
            // Pagination: structure for future use (single page for now)
            // String nextToken = extractNextToken(response.getBody());
            return posts;
        } catch (HttpStatusCodeException ex) {
            log.error("Twitter API error: {} Headers: {}", ex.getResponseBodyAsString(), ex.getResponseHeaders());
            throw new TwitterApiException(ex.getRawStatusCode(), ex.getResponseBodyAsString(), ex.getResponseHeaders().toSingleValueMap());
        } catch (Exception ex) {
            log.error("Error calling Twitter API", ex);
            throw new TwitterApiException(500, ex.getMessage(), null);
        }
    }

    private boolean isRateLimited(HttpHeaders headers) {
        List<String> retryAfter = headers.get("retry-after");
        List<String> rateLimitRemaining = headers.get("x-rate-limit-remaining");
        if ((retryAfter != null && !retryAfter.isEmpty()) ||
                (rateLimitRemaining != null && !rateLimitRemaining.isEmpty() && "0".equals(rateLimitRemaining.get(0)))) {
            return true;
        }
        return false;
    }

    private List<SocialPost> parseTweets(String json) throws Exception {
        List<SocialPost> posts = new ArrayList<>();
        JsonNode root = objectMapper.readTree(json);
        JsonNode data = root.path("data");
        JsonNode users = root.path("includes").path("users");

        // Map author_id to username
        HashMap<String, String> userMap = new HashMap<>();
        if (users.isArray()) {
            for (JsonNode user : users) {
                userMap.put(user.path("id").asText(), user.path("username").asText());
            }
        }

        if (data.isArray()) {
            for (JsonNode tweet : data) {
                String tweetId = tweet.path("id").asText();
                String authorId = tweet.path("author_id").asText();
                String username = userMap.getOrDefault(authorId, "");
                String text = tweet.path("text").asText();
                String createdAt = tweet.path("created_at").asText();
                String url = String.format("https://twitter.com/%s/status/%s", username, tweetId);

                SocialPost post = new SocialPost();
                post.setPlatform("twitter");
                post.setSourceId(tweetId);
                post.setAuthor(username);
                post.setContent(text);
                post.setPostTimestamp(OffsetDateTime.parse(createdAt).toInstant());
                post.setUrl(url);
                post.setSentimentScore(null);
                post.setSentimentLabel(null);

                posts.add(post);
            }
        }
        return posts;
    }

    // For future pagination support
    // private String extractNextToken(String json) throws Exception {
    //     JsonNode root = objectMapper.readTree(json);
    //     return root.path("meta").path("next_token").asText(null);
    // }
}