package exception;

/**
 * Exception thrown when a requested resource (e.g., SocialPost by id) does not exist.
 * <p>
 * Typically used in service or controller layers to indicate that an entity could not be found.
 * </p>
 */
public class ResourceNotFoundException extends ApiException {

    /**
     * Constructs a new ResourceNotFoundException with a detailed message.
     *
     * @param resourceName the name of the resource (e.g., "SocialPost")
     * @param identifier   the identifier of the resource (e.g., id value)
     */
    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(resourceName + " with id " + identifier + " not found");
    }

    /**
     * Constructs a new ResourceNotFoundException with a custom message.
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}