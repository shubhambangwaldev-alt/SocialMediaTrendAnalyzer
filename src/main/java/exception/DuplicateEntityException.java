package exception;

/**
 * Exception thrown to indicate an attempt to persist an entity that already exists,
 * such as when saving with the same platform and sourceId.
 * Used to prevent duplicate persistence in the system.
 */
public class DuplicateEntityException extends ApiException {

    /**
     * Constructs a new DuplicateEntityException with a detailed message.
     *
     * @param entityType the type of the entity (e.g., "SocialPost")
     * @param key the identifying key (e.g., "platform+sourceId")
     */
    public DuplicateEntityException(String entityType, String key) {
        super(String.format("Duplicate %s detected for key: %s. Entity already exists.", entityType, key));
    }

    /**
     * Constructs a new DuplicateEntityException with a custom message and cause.
     *
     * @param entityType the type of the entity
     * @param key the identifying key
     * @param cause the cause of the exception
     */
    public DuplicateEntityException(String entityType, String key, Throwable cause) {
        super(String.format("Duplicate %s detected for key: %s. Entity already exists.", entityType, key), cause);
    }

    /**
     * Static factory method for SocialPost duplicates.
     *
     * @param platform the platform name
     * @param sourceId the source identifier
     * @return a DuplicateEntityException for SocialPost
     */
    public static DuplicateEntityException forSocialPost(String platform, String sourceId) {
        String key = platform + "+" + sourceId;
        return new DuplicateEntityException("SocialPost", key);
    }
}