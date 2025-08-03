package repository;


import model.SocialPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for managing SocialPost entities.
 */
public interface SocialPostRepository extends JpaRepository<SocialPost, Long> {

    /**
     * Finds SocialPosts by platform with pagination.
     * @param platform the platform name
     * @param pageable pagination information
     * @return a page of SocialPosts for the given platform
     */
    Page<SocialPost> findByPlatform(String platform, Pageable pageable);

    /**
     * Finds a SocialPost by platform and sourceId.
     * @param platform the platform name
     * @param sourceId the source identifier
     * @return an Optional containing the SocialPost if found
     */
    Optional<SocialPost> findByPlatformAndSourceId(String platform, String sourceId);

    /**
     * Finds SocialPosts whose content contains the given keyword (case-insensitive), with pagination.
     * @param keyword the keyword to search for
     * @param pageable pagination information
     * @return a page of SocialPosts containing the keyword
     */
    Page<SocialPost> findByContentContainingIgnoreCase(String keyword, Pageable pageable);

    /**
     * Finds all SocialPosts ordered by postTimestamp descending, with pagination.
     * @param pageable pagination information
     * @return a page of SocialPosts ordered by postTimestamp descending
     */
    Page<SocialPost> findAllByOrderByPostTimestampDesc(Pageable pageable);

    /**
     * Saves the SocialPost if it does not already exist by platform and sourceId.
     * @param post the SocialPost to save
     * @return Optional containing the saved SocialPost if it was not present, otherwise Optional.empty()
     */
    default Optional<SocialPost> saveIfNotExists(SocialPost post) {
        Optional<SocialPost> existing = findByPlatformAndSourceId(post.getPlatform(), post.getSourceId());
        if (existing.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(save(post));
    }
}