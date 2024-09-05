package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * The OrderItem class represents an item in an order in the system.
 * It implements the DataDisplay interface to control the display of data.
 *
 * It has six fields:
 * <ul>
 *     <li>orderItemId: The ID of the order item.</li>
 *     <li>order: The order to which the item belongs.</li>
 *     <li>item: The item in the order.</li>
 *     <li>quantity: The quantity of the item in the order.</li>
 *     <li>price: The price of the item in the order.</li>
 *     <li>priceAfterDiscount: The price of the item in the order after discount.</li>
 * </ul>
 *
 * It also includes methods to display data and limited display data.
 *
 * This class is also a JPA entity, meaning it is mapped to a corresponding table in the database.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem implements DataDisplay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "price_after_discount", nullable = false)
    private BigDecimal priceAfterDiscount;

    public OrderItem(OrderItem other) {
        this.orderItemId = other.orderItemId;
        this.order = other.order;
        this.item = other.item;
        this.quantity = other.quantity;
        this.price = other.price;
        this.priceAfterDiscount = other.priceAfterDiscount;
    }

    @Override
    public DataDisplay displayData() {
        return new OrderItem(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}

