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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "description", length = 1500, nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "star_rating", nullable = false)
    private int starRating;

    public Review(Review other) {
        this.reviewId = other.reviewId;
        this.user = other.user;
        this.item = other.item;
        this.description = other.description;
        this.createdAt = other.createdAt;
        this.updatedAt = other.updatedAt;
        this.starRating = other.starRating;
    }

    @Override
    public DataDisplay displayData() {
        return new Review(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}