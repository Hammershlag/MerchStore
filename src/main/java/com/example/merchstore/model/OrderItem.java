package com.example.merchstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem implements DataDisplay{
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

    public OrderItem(OrderItem other) {
        this.orderItemId = other.orderItemId;
        this.order = other.order;
        this.item = other.item;
        this.quantity = other.quantity;
        this.price = other.price;
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

