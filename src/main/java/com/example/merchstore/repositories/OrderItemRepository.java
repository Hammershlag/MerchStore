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

    List<OrderItem> findAllByOrder(Order order);

    boolean existsByItemAndOrder(Item item, Order order);

}
