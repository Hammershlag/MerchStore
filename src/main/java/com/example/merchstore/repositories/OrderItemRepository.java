package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Item;
import com.example.merchstore.components.models.Order;
import com.example.merchstore.components.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The OrderItemRepository interface is a Spring Data JPA repository for the OrderItem entity.
 * It provides CRUD operations and methods to find OrderItem entities by associated Order, and to check if an OrderItem exists by associated Item and Order.
 *
 * It has two methods:
 * <ul>
 *     <li>findAllByOrder(Order order): This method retrieves a list of OrderItem entities associated with the provided Order entity.</li>
 *     <li>existsByItemAndOrder(Item item, Order order): This method checks if an OrderItem entity exists that is associated with the provided Item and Order entities.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 09.06.2024
 */

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * This method retrieves a list of OrderItem entities associated with the provided Order entity.
     *
     * @param order The Order entity to retrieve the OrderItem entities for.
     * @return A list of OrderItem entities associated with the provided Order entity.
     */
    List<OrderItem> findAllByOrder(Order order);

    /**
     * This method checks if an OrderItem entity exists that is associated with the provided Item and Order entities.
     *
     * @param item The Item entity to check the OrderItem entity for.
     * @param order The Order entity to check the OrderItem entity for.
     * @return A boolean value indicating if an OrderItem entity exists that is associated with the provided Item and Order entities.
     */
    boolean existsByItemAndOrder(Item item, Order order);

}
