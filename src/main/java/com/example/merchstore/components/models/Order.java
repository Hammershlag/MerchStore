package com.example.merchstore.components.models;

import com.example.merchstore.components.enums.OrderStatus;
import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order implements DataDisplay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    public Order(Order other) {
        this.orderId = other.orderId;
        this.user = other.user;
        this.orderDate = other.orderDate;
        this.status = other.status;
        this.totalAmount = other.totalAmount;
        this.discount = other.discount;
    }

    @Override
    public DataDisplay displayData() {
        return new Order(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}

