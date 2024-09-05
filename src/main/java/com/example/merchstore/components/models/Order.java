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

    /**
     * The ID of the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    /**
     * The user who placed the order.
     * @see User
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The date and time when the order was placed.
     */
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    /**
     * The status of the order.
     * @see OrderStatus
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    /**
     * The total amount of the order before discount.
     */
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    /**
     * The total amount of the order after discount.
     */
    @Column(name = "total_after_discount", nullable = false)
    private BigDecimal totalAfterDiscount;

    /**
     * The discount applied to the order.
     * @see Discount
     */
    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    /**
     * The currency used for the order.
     * @see Currency
     */
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    /**
     * The exchange rate used for the order.
     * @see ExchangeRate
     */
    @ManyToOne
    @JoinColumn(name = "exchange_rate_id")
    private ExchangeRate exchangeRate;

    /**
     * The service fee applied to the order.
     */
    @Column(name = "service_fee")
    private BigDecimal serviceFee;

    /**
     * The copy constructor for the Order class.
     * @param other The Order object to copy.
     */
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

    /**
     * Display the data of the order.
     *
     * @see DataDisplay
     *
     * @return a DataDisplay object representing the order data.
     */
    @Override
    public DataDisplay displayData() {
        return new Order(this);
    }

    /**
     * Display the limited data of the order.
     *
     * @see DataDisplay
     *
     * @return a DataDisplay object representing the limited order data.
     */
    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}

