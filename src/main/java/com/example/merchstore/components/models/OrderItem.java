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

    /**
     * The ID of the order item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    /**
     * The order to which the item belongs.
     * @see Order
     */
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * The item in the order.
     * @see Item
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    /**
     * The quantity of the item in the order.
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * The price of the item in the order.
     */
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    /**
     * The price of the item in the order after discount.
     */
    @Column(name = "price_after_discount", nullable = false)
    private BigDecimal priceAfterDiscount;

    /**
     * Copy constructor for the OrderItem class.
     * @param other The OrderItem object to copy.
     */
    public OrderItem(OrderItem other) {
        this.orderItemId = other.orderItemId;
        this.order = other.order;
        this.item = other.item;
        this.quantity = other.quantity;
        this.price = other.price;
        this.priceAfterDiscount = other.priceAfterDiscount;
    }

    /**
     * Display the data of the order item.
     *
     * @see DataDisplay
     *
     * @return The data display of the order item.
     */
    @Override
    public DataDisplay displayData() {
        return new OrderItem(this);
    }

    /**
     * Display limited data of the order item.
     *
     * @see DataDisplay
     *
     * @return The limited data display of the order item.
     */
    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}

