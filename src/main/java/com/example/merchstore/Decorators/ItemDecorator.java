package com.example.merchstore.Decorators;

import com.example.merchstore.components.models.Item;

import java.math.BigDecimal;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 09.06.2024
 */
public class ItemDecorator {
    public static Item create() {
        Item item = new Item();
        item.setItemId(-1L);
        item.setName("Default");
        item.setDescription("Default");
        item.setPrice(BigDecimal.ZERO);
        item.setStockQuantity(0);
        item.setCreatedAt(null);
        item.setUpdatedAt(null);
        item.setImage(null);
        return item;
    }
}
