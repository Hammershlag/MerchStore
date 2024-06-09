package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 09.06.2024
 */
@Data @AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discount_items")
public class ItemDiscount implements DataDisplay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountItemId;

    @ManyToOne
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    public ItemDiscount(ItemDiscount other) {
        this.discountItemId = other.discountItemId;
        this.discount = other.discount;
        this.item = other.item;
    }

    @Override
    public DataDisplay displayData() {
        return new ItemDiscount(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}
