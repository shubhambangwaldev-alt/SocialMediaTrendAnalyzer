package service;

import client.TwitterApiClient;
import model.SocialPost;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.SocialPostRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing social posts and integrating with external APIs.
 */
@Service
public class SocialPostService {

    private static final Logger logger = LoggerFactory.getLogger(SocialPostService.class);

    private final SocialPostRepository socialPostRepository;
    private final TwitterApiClient twitterApiClient;

    public SocialPostService(SocialPostRepository socialPostRepository, TwitterApiClient twitterApiClient) {
        this.socialPostRepository = socialPostRepository;
        this.twitterApiClient = twitterApiClient;
    }

    /**
     * Returns all social posts sorted by postTimestamp descending.
     *
     * @return list of all social posts
     */
    public List<SocialPost> getAllPosts() {
        return socialPostRepository.findAllByOrderByPostTimestampDesc(Pageable.unpaged()).getContent();
    }

    /**
     * Fetches tweets by keyword, maps to SocialPost, deduplicates, saves new posts, and logs the process.
     * Idempotent for the same keyword invocation.
     *
     * @param keyword the keyword to search tweets for
     */
    @Transactional
    public void fetchAndSavePosts(String keyword) {
        logger.info("Starting fetch for keyword: {}", keyword);
        List<SocialPost> fetchedPosts;
        try {
            fetchedPosts = twitterApiClient.searchTweets(keyword)
                    .stream()
                    .map(tweet -> SocialPost.builder()
                            .platform("TWITTER")
                            .sourceId(String.valueOf(tweet.getId()))
                            .author(tweet.getAuthor())
                            .content(tweet.getContent())
                            .postTimestamp(tweet.getCreatedAt())
                            .build()
                    )
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to fetch tweets for keyword: {}", keyword, e);
            throw new RuntimeException("Failed to fetch tweets for keyword: " + keyword, e);
        }

        Set<String> existingKeys = socialPostRepository
                .findByPlatform("TWITTER", Pageable.unpaged())
                .getContent()
                .stream()
                .map(post -> post.getPlatform() + ":" + post.getSourceId())
                .collect(Collectors.toSet());

        List<SocialPost> newPosts = fetchedPosts.stream()
                .filter(post -> !existingKeys.contains(post.getPlatform() + ":" + post.getSourceId()))
                .collect(Collectors.toList());

        socialPostRepository.saveAll(newPosts);
        logger.info("Fetch complete for keyword: {}. New posts saved: {}", keyword, newPosts.size());
    }

    /**
     * Searches posts by keyword in content, case-insensitively, with pagination.
     *
     * @param keyword the keyword to search for
     * @param pageable pagination information
     * @return list of matching social posts
     */
    public List<SocialPost> searchPosts(String keyword, Pageable pageable) {
        return socialPostRepository.findByContentContainingIgnoreCase(keyword, pageable).getContent();
    }
}
