package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Item;
import com.example.merchstore.components.models.Review;
import com.example.merchstore.components.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The ReviewRepository interface is a Spring Data JPA repository for the Review entity.
 * It provides CRUD operations and methods to find Review entities by associated Item, and to check if a Review exists by associated Item and User.
 *
 * It has two methods:
 * <ul>
 *     <li>findAllByItem(Item item): This method retrieves a list of Review entities associated with the provided Item entity.</li>
 *     <li>existsByItemAndUser(Item item, User user): This method checks if a Review entity exists that is associated with the provided Item and User entities.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 10.06.2024
 */

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * This method retrieves a list of Review entities associated with the provided Item entity.
     *
     * @param item The Item entity to retrieve the Review entities for.
     * @return A list of Review entities associated with the provided Item entity.
     */
    List<Review> findAllByItem(Item item);

    /**
     * This method checks if a Review entity exists that is associated with the provided Item and User entities.
     *
     * @param item The Item entity to check the Review entity for.
     * @param user The User entity to check the Review entity for.
     * @return A boolean value indicating if a Review entity exists that is associated with the provided Item and User entities.
     */
    boolean existsByItemAndUser(Item item, User user);

}
