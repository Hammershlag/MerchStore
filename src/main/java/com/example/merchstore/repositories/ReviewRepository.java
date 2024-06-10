package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Item;
import com.example.merchstore.components.models.Review;
import com.example.merchstore.components.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 10.06.2024
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByItem(Item item);

    boolean existsByItemAndUser(Item item, User user);

}
