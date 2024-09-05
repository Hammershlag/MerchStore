package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Order;
import com.example.merchstore.components.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The OrderRepository interface is a Spring Data JPA repository for the Order entity.
 * It provides CRUD operations and methods to find Order entities by order id and by associated User.
 *
 * It has two methods:
 * <ul>
 *     <li>findByOrderId(Long orderId): This method retrieves an Order entity with the provided order id.</li>
 *     <li>findAllOrdersByUser(User currentUser): This method retrieves a list of Order entities associated with the provided User entity.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 09.06.2024
 */

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * This method retrieves an Order entity with the provided order id.
     *
     * @param orderId The order id of the Order entity to retrieve.
     * @return An Order entity with the provided order id.
     */
    Order findByOrderId(Long orderId);

    /**
     * This method retrieves a list of Order entities associated with the provided User entity.
     *
     * @param currentUser The User entity to retrieve the Order entities for.
     * @return A list of Order entities associated with the provided User entity.
     */
    List<Order> findAllOrdersByUser(User currentUser);
}
