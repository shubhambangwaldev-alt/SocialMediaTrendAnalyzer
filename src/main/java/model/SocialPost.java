package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * JPA entity representing a social media post from various platforms.
 * Includes sentiment analysis fields and audit timestamps.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
@Entity
@Table(
        name = "social_post",
        uniqueConstraints = @UniqueConstraint(columnNames = {"platform", "source_id"}),
        indexes = {
                @Index(name = "idx_platform", columnList = "platform")
        }
)
public class SocialPost {

    /**
     * Auto-generated primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Social media platform (e.g., "twitter", "reddit").
     */
    @NotBlank
    @Column(nullable = false)
    private String platform;

    /**
     * Original post ID from the platform.
     */
    @NotBlank
    @Column(name = "source_id", nullable = false)
    private String sourceId;

    /**
     * Author of the post.
     */
    @NotBlank
    @Column(nullable = false)
    private String author;

    /**
     * Post content (potentially long text).
     */
    @NotBlank
    @Lob
    @Column(nullable = false)
    private String content;

    /**
     * Timestamp when the post was created on the platform.
     */
    @Column(name = "post_timestamp")
    private Instant postTimestamp;

    /**
     * Link to the original post.
     */
    @Column
    private String url;

    /**
     * Sentiment score (nullable).
     */
    @Column
    private Double sentimentScore;

    /**
     * Sentiment label (enum: POSITIVE, NEUTRAL, NEGATIVE), stored as string.
     */
    @Enumerated(EnumType.STRING)
    @Column
    private SentimentLabel sentimentLabel;

    /**
     * Audit timestamp: when the entity was created.
     */
    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    /**
     * Audit timestamp: when the entity was last updated.
     */
    @UpdateTimestamp
    @Column
    private Instant updatedAt;

    /**
     * Helper method to set sentimentScore and derive sentimentLabel if label is null.
     * @param score the sentiment score
     */
    public void setSentimentScoreAndLabel(Double score) {
        this.sentimentScore = score;
        if (this.sentimentLabel == null && score != null) {
            if (score > 0.1) {
                this.sentimentLabel = SentimentLabel.POSITIVE;
            } else if (score < -0.1) {
                this.sentimentLabel = SentimentLabel.NEGATIVE;
            } else {
                this.sentimentLabel = SentimentLabel.NEUTRAL;
            }
        }
    }

    /**
     * Sentiment label enum.
     */
    public enum SentimentLabel {
        POSITIVE, NEUTRAL, NEGATIVE
    }

    /**
     * Equality based on id if present, otherwise on platform and sourceId.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SocialPost)) return false;
        SocialPost that = (SocialPost) o;
        if (id != null && that.id != null) {
            return id.equals(that.id);
        }
        return platform.equals(that.platform) && sourceId.equals(that.sourceId);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : (platform + sourceId).hashCode();
    }
}
