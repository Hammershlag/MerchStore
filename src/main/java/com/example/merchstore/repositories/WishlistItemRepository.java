package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Item;
import com.example.merchstore.components.models.User;
import com.example.merchstore.components.models.WishlistItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 17.09.2024
 */

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

    WishlistItem findByItemAndUser(Item item, User user);

    List<WishlistItem> findByUser(User user);

    boolean existsByItemAndUser(Item item, User user);

    @Transactional
    void deleteByItemAndUser(Item item, User user);

}
