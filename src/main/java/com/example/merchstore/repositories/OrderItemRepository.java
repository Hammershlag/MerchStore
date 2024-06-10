package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Item;
import com.example.merchstore.components.models.Order;
import com.example.merchstore.components.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 09.06.2024
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByOrder(Order order);

    boolean existsByItemAndOrder(Item item, Order order);

}
