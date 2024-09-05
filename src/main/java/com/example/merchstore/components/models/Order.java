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
 * The Order class represents an order in the system.
 * It implements the DataDisplay interface to control the display of data.
 *
 * It has ten fields:
 * <ul>
 *     <li>orderId: The ID of the order.</li>
 *     <li>user: The user who placed the order.</li>
 *     <li>orderDate: The date and time when the order was placed.</li>
 *     <li>status: The status of the order.</li>
 *     <li>totalAmount: The total amount of the order before discount.</li>
 *     <li>totalAfterDiscount: The total amount of the order after discount.</li>
 *     <li>discount: The discount applied to the order.</li>
 *     <li>currency: The currency used for the order.</li>
 *     <li>exchangeRate: The exchange rate used for the order.</li>
 *     <li>serviceFee: The service fee applied to the order.</li>
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

    @Column(name = "total_after_discount", nullable = false)
    private BigDecimal totalAfterDiscount;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "exchange_rate_id")
    private ExchangeRate exchangeRate;

    @Column(name = "service_fee")
    private BigDecimal serviceFee;


    public Order(Order other) {
        this.orderId = other.orderId;
        this.user = other.user;
        this.orderDate = other.orderDate;
        this.status = other.status;
        this.totalAmount = other.totalAmount;
        this.discount = other.discount;
        this.currency = other.currency;
        this.exchangeRate = other.exchangeRate;
        this.totalAfterDiscount = other.totalAfterDiscount;
        this.serviceFee = other.serviceFee;
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

