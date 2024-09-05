package com.example.merchstore.components.enums;

/**
 * The OrderStatus enum represents the status of an order in the system.
 * It can have one of the following values:
 * <ul>
 *     <li>UNPAID: The order has been created but not yet paid.</li>
 *     <li>PENDING: The order has been paid and is awaiting confirmation.</li>
 *     <li>CONFIRMED: The order has been confirmed.</li>
 *     <li>PROCESSING: The order is being processed.</li>
 *     <li>SHIPPED: The order has been shipped to the customer.</li>
 *     <li>DELIVERED: The order has been delivered to the customer.</li>
 *     <li>CANCELED: The order has been canceled.</li>
 *     <li>RETURNED: The order has been returned by the customer.</li>
 *     <li>REFUNDED: The order has been refunded to the customer.</li>
 *     <li>ON_HOLD: The order is on hold.</li>
 *     <li>FAILED: The order has failed for some reason.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 09.06.2024
 */
public enum OrderStatus {
    /**
     * Represents an unpaid order.
     */
    UNPAID,

    /**
     * Represents a pending order.
     */
    PENDING,

    /**
     * Represents a confirmed order.
     */
    CONFIRMED,

    /**
     * Represents an order that is being processed.
     */
    PROCESSING,

    /**
     * Represents an order that has been shipped.
     */
    SHIPPED,

    /**
     * Represents an order that has been delivered.
     */
    DELIVERED,

    /**
     * Represents a canceled order.
     */
    CANCELED,

    /**
     * Represents an order that has been returned.
     */
    RETURNED,

    /**
     * Represents an order that has been refunded.
     */
    REFUNDED,

    /**
     * Represents an order that is on hold.
     */
    ON_HOLD,

    /**
     * Represents an order that has failed.
     */
    FAILED
}
