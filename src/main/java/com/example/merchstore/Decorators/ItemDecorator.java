package com.example.merchstore.Decorators;

import com.example.merchstore.components.models.Item;

import java.math.BigDecimal;

/**
 * The ItemDecorator class is a decorator for the Item class in the application.
 * It provides a method to create a default Item object with predefined values.
 *
 * It has one method:
 * <ul>
 *     <li>create(): This static method creates a default Item object with predefined values and returns it.</li>
 * </ul>
 *
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
