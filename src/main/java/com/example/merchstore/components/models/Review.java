package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The Review class represents a review in the system.
 * It implements the DataDisplay interface to control the display of data.
 *
 * It has seven fields:
 * <ul>
 *     <li>reviewId: The ID of the review.</li>
 *     <li>user: The user who wrote the review.</li>
 *     <li>item: The item that the review is about.</li>
 *     <li>description: The text of the review.</li>
 *     <li>createdAt: The date and time when the review was created.</li>
 *     <li>updatedAt: The date and time when the review was last updated.</li>
 *     <li>starRating: The star rating given in the review.</li>
 * </ul>
 *
 * It also includes methods to display data and limited display data.
 *
 * This class is also a JPA entity, meaning it is mapped to a corresponding table in the database.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 10.06.2024
 */
@Data @AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Review implements DataDisplay {

    /**
     * The ID of the review.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    /**
     * The user who wrote the review.
     * @see User
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The item that the review is about.
     * @see Item
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    /**
     * The text of the review.
     */
    @Column(name = "description", length = 1500, nullable = false)
    private String description;

    /**
     * The date and time when the review was created.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * The date and time when the review was last updated.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * The star rating given in the review.
     */
    @Column(name = "star_rating", nullable = false)
    private int starRating;

    /**
     * Copy constructor for the Review class.
     * @param other The Review object to copy.
     */
    public Review(Review other) {
        this.reviewId = other.reviewId;
        this.user = other.user;
        this.item = other.item;
        this.description = other.description;
        this.createdAt = other.createdAt;
        this.updatedAt = other.updatedAt;
        this.starRating = other.starRating;
    }

    /**
     * Displays the data of the review.
     *
     * @see DataDisplay
     *
     * @return The data display of the review.
     */
    @Override
    public DataDisplay displayData() {
        return new Review(this);
    }

    /**
     * Displays limited data of the review.
     *
     * @see DataDisplay
     *
     * @return The limited data display of the review.
     */
    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}