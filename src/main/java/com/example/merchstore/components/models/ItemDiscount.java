package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The ItemDiscount class represents a discount applied to an item in the system.
 * It implements the DataDisplay interface to control the display of data.
 *
 * It has three fields:
 * <ul>
 *     <li>discountItemId: The ID of the item discount.</li>
 *     <li>discount: The discount applied to the item.</li>
 *     <li>item: The item to which the discount is applied.</li>
 * </ul>
 *
 * It also includes methods to display data and limited display data.
 *
 * This class is also a JPA entity, meaning it is mapped to a corresponding table in the database.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 09.06.2024
 */
@Data @AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discount_items")
public class ItemDiscount implements DataDisplay {

    /**
     * The ID of the item discount.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountItemId;

    /**
     * The discount applied to the item.
     * @see Discount
     */
    @ManyToOne
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

    /**
     * The item to which the discount is applied.
     * @see Item
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    /**
     * Copy constructor for the ItemDiscount class.
     * @param other The ItemDiscount object to copy.
     */
    public ItemDiscount(ItemDiscount other) {
        this.discountItemId = other.discountItemId;
        this.discount = other.discount;
        this.item = other.item;
    }

    /**
     * Displays the data of the item discount.
     *
     * @see DataDisplay
     *
     * @return The data display of the item discount.
     */
    @Override
    public DataDisplay displayData() {
        return new ItemDiscount(this);
    }

    /**
     * Displays limited data of the item discount.
     *
     * @see DataDisplay
     *
     * @return The limited data display of the item discount.
     */
    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}
