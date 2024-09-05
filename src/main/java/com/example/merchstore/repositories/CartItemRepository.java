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
 * The CartItemRepository interface is a Spring Data JPA repository for the CartItem entity.
 * It provides CRUD operations and methods to find, check existence, update, and delete CartItem entities associated with specific User and Item entities.
 *
 * It has five methods:
 * <ul>
 *     <li>findAllByUser(User user): This method retrieves all CartItem entities associated with the provided User entity.</li>
 *     <li>findByUserAndItem(User user, Item item): This method retrieves a CartItem entity associated with the provided User and Item entities.</li>
 *     <li>existsByUserAndItem(User user, Item item): This method checks if a CartItem entity associated with the provided User and Item entities exists.</li>
 *     <li>updateByQuantityByUserAndItem(User user, Item item, int quantity): This method updates the quantity of a CartItem entity associated with the provided User and Item entities.</li>
 *     <li>deleteByUserAndItem(User user, Item item): This method deletes a CartItem entity associated with the provided User and Item entities.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 04.06.2024
 */

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * This method retrieves all CartItem entities associated with the provided User entity.
     *
     * @param user The User entity to retrieve the CartItem entities for.
     * @return A list of CartItem entities associated with the provided User entity.
     */
    List<CartItem> findAllByUser(User user);

    /**
     * This method retrieves a CartItem entity associated with the provided User and Item entities.
     *
     * @param user The User entity to retrieve the CartItem entity for.
     * @param item The Item entity to retrieve the CartItem entity for.
     * @return A CartItem entity associated with the provided User and Item entities.
     */
    CartItem findByUserAndItem(User user, Item item);

    /**
     * This method checks if a CartItem entity associated with the provided User and Item entities exists.
     *
     * @param user The User entity to check the CartItem entity for.
     * @param item The Item entity to check the CartItem entity for.
     * @return A boolean value indicating if a CartItem entity associated with the provided User and Item entities exists.
     */
    boolean existsByUserAndItem(User user, Item item);

    /**
     * This method updates the quantity of a CartItem entity associated with the provided User and Item entities.
     *
     * @param user The User entity to update the CartItem entity for.
     * @param item The Item entity to update the CartItem entity for.
     * @param quantity The new quantity of the CartItem entity.
     */
    @Modifying
    @Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.user = :user AND c.item = :item")
    void updateByQuantityByUserAndItem(@Param("user") User user, @Param("item") Item item, @Param("quantity") int quantity);

    /**
     * This method deletes a CartItem entity associated with the provided User and Item entities.
     *
     * @param user The User entity to delete the CartItem entity for.
     * @param item The Item entity to delete the CartItem entity for.
     */
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user = :user AND c.item = :item")
    void deleteByUserAndItem(@Param("user") User user, @Param("item") Item item);
}
