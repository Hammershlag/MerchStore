package com.example.merchstore.repositories;

import com.example.merchstore.components.models.CartItem;
import com.example.merchstore.components.models.Item;
import com.example.merchstore.components.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 04.06.2024
 */

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    //Find
    public List<CartItem> findAllByUser(User user);
    public CartItem findByUserAndItem(User user, Item item);

    //Exists
    public boolean existsByUserAndItem(User user, Item item);

    //Update
    @Modifying
    @Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.user = :user AND c.item = :item")
    void updateByQuantityByUserAndItem(@Param("user") User user, @Param("item") Item item, @Param("quantity") int quantity);

    //Delete
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user = :user AND c.item = :item")
    void deleteByUserAndItem(@Param("user") User user, @Param("item") Item item);
}
